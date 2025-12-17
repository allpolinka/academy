package academy.tochkavhoda.competition.service;

import academy.tochkavhoda.competition.dao.ApplicationDao;
import academy.tochkavhoda.competition.dao.ExpertDao;
import academy.tochkavhoda.competition.dao.ParticipantDao;
import academy.tochkavhoda.competition.dao.RatingDao;
import academy.tochkavhoda.competition.datebase.Database;
import academy.tochkavhoda.competition.dto.request.*;
import academy.tochkavhoda.competition.dto.response.ApplicationResponse;
import academy.tochkavhoda.competition.model.Application;
import academy.tochkavhoda.competition.model.Expert;
import academy.tochkavhoda.competition.model.Rating;
import academy.tochkavhoda.competition.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.*;

public class ApplicationService {

    private final ApplicationDao applicationDao;
    private final ParticipantDao participantDao;
    private final ExpertDao expertDao;
    private RatingDao ratingDao;
    private final Database database;
    private final Gson gson = new Gson();

    public ApplicationService(ApplicationDao applicationDao, ParticipantDao participantDao, ExpertDao expertDao, RatingDao ratingDao, Database database) {
        this.applicationDao = applicationDao;
        this.participantDao = participantDao;
        this.expertDao = expertDao;
        this.ratingDao = ratingDao;
        this.database = database;
    }

    public ServerResponse addApplication(String token, String requestJsonString) {
        try {
            // Проверка токена
            String login = database.getLoginByToken(token);
            String role = database.getRoleByToken(token);
            if (login == null || !"participant".equals(role)) {
                return errorResponse("Неверный токен или доступ запрещён");
            }

            // Парсим DTO с данными заявки
            AddApplicationDtoRequest request = gson.fromJson(requestJsonString, AddApplicationDtoRequest.class);

            // Валидация
            if (request.getTitle() == null || request.getTitle().trim().isEmpty() ||
                    request.getDescription() == null || request.getDescription().trim().isEmpty() ||
                    request.getDirections() == null || request.getDirections().isEmpty() ||
                    request.getRequestedAmount() <= 0) {
                return errorResponse("Все поля обязательны, сумма должна быть положительной");
            }

            // Создаём модель заявки
            Application application = new Application(
                    request.getTitle().trim(),
                    request.getDescription().trim(),
                    new HashSet<>(request.getDirections()),
                    request.getRequestedAmount(),
                    login
            );

            // Сохраняем
            applicationDao.insert(application);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Заявка успешно добавлена");
            response.put("applicationId", application.getId());
            return new ServerResponse(200, gson.toJson(response));

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        } catch (Exception e) {
            return errorResponse("Ошибка при добавлении заявки: " + e.getMessage());
        }
    }

    public ServerResponse cancelApplication(String token, String requestJsonString) {
        try {
            String login = database.getLoginByToken(token);
            String role = database.getRoleByToken(token);
            if (login == null || !"participant".equals(role)) {
                return errorResponse("Неверный токен или доступ запрещён");
            }

            // Парсим ID заявки
            CancelApplicationRequest request = gson.fromJson(requestJsonString, CancelApplicationRequest.class);
            int appId = request.getApplicationId();

            Application app = applicationDao.getById(appId);
            if (app == null) {
                return errorResponse("Заявка не найдена");
            }
            if (!app.getParticipiantLogin().equals(login)) {
                return errorResponse("Вы можете отменить только свою заявку");
            }

            applicationDao.remove(appId);

            return successResponse("Заявка успешно отменена");

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        }
    }

    public ServerResponse rateApplication(String token, String requestJsonString) {
        try {
            String login = database.getLoginByToken(token);
            String role = database.getRoleByToken(token);
            if (login == null || !"expert".equals(role)) {
                return errorResponse("Неверный токен или доступ запрещён");
            }

            RateApplicationRequest request = gson.fromJson(requestJsonString, RateApplicationRequest.class);

            Application app = applicationDao.getById(request.getApplicationId());
            if (app == null) {
                return errorResponse("Заявка не найдена");
            }

            Expert expert = expertDao.getByLogin(login);
            if (expert == null || Collections.disjoint(app.getDirections(), expert.getExpertDirections())) {
                return errorResponse("Вы не можете оценивать эту заявку: направления не совпадают с вашей экспертизой");
            }

            if (request.getValue() < 1 || request.getValue() > 5) {
                return errorResponse("Оценка должна быть от 1 до 5");
            }

            Rating rating = new Rating(login, app.getId(), request.getValue());
            ratingDao.insert(rating);

            return successResponse("Оценка успешно сохранена");

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        }
    }

    public ServerResponse getApplicationsForExpert(String token, String requestJsonString) {
        try {
            // Проверка токена и роли
            String login = database.getLoginByToken(token);
            String role = database.getRoleByToken(token);
            if (login == null || !"expert".equals(role)) {
                return errorResponse("Доступ запрещён или токен недействителен");
            }

            // Получаем эксперта
            Expert expert = expertDao.getByLogin(login);
            if (expert == null) {
                return errorResponse("Эксперт не найден");
            }
            Set<String> expertDirections = expert.getExpertDirections();

            // Парсим запрос — набор направлений от эксперта
            GetApplicationsByDirectionsRequest request = gson.fromJson(requestJsonString, GetApplicationsByDirectionsRequest.class);
            Set<String> requestedDirections = request.getDirections();

            // Проверка: запрошенные направления должны быть подмножеством экспертизы
            if (requestedDirections == null || requestedDirections.isEmpty() || !expertDirections.containsAll(requestedDirections)) {
                return errorResponse("Вы можете запрашивать только направления из вашей экспертизы");
            }

            // Получаем заявки по направлениям
            List<Application> applications = applicationDao.getByDirections(requestedDirections);

            // Формируем ответ — список заявок в JSON
            List<ApplicationResponse> responseList = applications.stream()
                    .map(app -> new ApplicationResponse(
                            app.getId(),
                            app.getTitle(),
                            app.getDescription(),
                            (List<String>) app.getDirections(),
                            app.getRequestedAmount(),
                            app.getParticipiantLogin(),
                            app.getAverageRating() // средняя оценка
                    ))
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("applications", responseList);
            return new ServerResponse(200, gson.toJson(response));

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        } catch (Exception e) {
            return errorResponse("Ошибка при получении заявок: " + e.getMessage());
        }
    }

    public ServerResponse removeRating(String token, String requestJsonString) {
        try {
            String login = database.getLoginByToken(token);
            String role = database.getRoleByToken(token);
            if (login == null || !"expert".equals(role)) {
                return errorResponse("Неверный токен или доступ запрещён");
            }

            RemoveRatingRequest request = gson.fromJson(requestJsonString, RemoveRatingRequest.class);

            Rating existing = ratingDao.getByExpertAndApplication(login, request.getApplicationId());
            if (existing == null) {
                return errorResponse("Оценка не найдена");
            }

            ratingDao.deleteByExpertAndApplication(login, request.getApplicationId());

            return successResponse("Оценка успешно удалена");

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        }
    }

    private ServerResponse successResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return new ServerResponse(200, gson.toJson(response));
    }

    private ServerResponse errorResponse(String errorMessage) {
        Map<String, String> error = new HashMap<>();
        error.put("error", errorMessage);
        return new ServerResponse(400, gson.toJson(error));
    }
}
