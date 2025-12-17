package academy.tochkavhoda.competition.service;

import academy.tochkavhoda.competition.dao.ApplicationDao;
import academy.tochkavhoda.competition.dao.ExpertDao;
import academy.tochkavhoda.competition.dao.RatingDao;
import academy.tochkavhoda.competition.datebase.Database;
import academy.tochkavhoda.competition.dto.request.RateApplicationRequest;
import academy.tochkavhoda.competition.dto.request.RemoveRatingRequest;
import academy.tochkavhoda.competition.model.Application;
import academy.tochkavhoda.competition.model.Expert;
import academy.tochkavhoda.competition.model.Rating;
import academy.tochkavhoda.competition.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingService {

    private final RatingDao ratingDao;
    private final ApplicationDao applicationDao;
    private final ExpertDao expertDao;
    private final Database database;
    private final Gson gson = new Gson();

    public RatingService(RatingDao ratingDao, ApplicationDao applicationDao, ExpertDao expertDao, Database database) {
        this.ratingDao = ratingDao;
        this.applicationDao = applicationDao;
        this.expertDao = expertDao;
        this.database = database;
    }

    public ServerResponse setRating(String token, String requestJsonString) {
        try {
            // Проверка токена и роли
            String login = database.getLoginByToken(token);
            String role = database.getRoleByToken(token);
            if (login == null || !"expert".equals(role)) {
                return errorResponse("Доступ запрещён или токен недействителен");
            }

            // Парсим запрос
            RateApplicationRequest request = gson.fromJson(requestJsonString, RateApplicationRequest.class);

            int appId = request.getApplicationId();
            int value = request.getValue();

            // Проверка заявки
            Application app = applicationDao.getById(appId);
            if (app == null) {
                return errorResponse("Заявка не найдена");
            }

            // Проверка эксперта и направлений
            Expert expert = expertDao.getByLogin(login);
            if (expert == null || Collections.disjoint(app.getDirections(), expert.getExpertDirections())) {
                return errorResponse("Вы не можете оценивать эту заявку: направления не совпадают с вашей экспертизой");
            }

            // Валидация оценки
            if (value < 1 || value > 5) {
                return errorResponse("Оценка должна быть от 1 до 5");
            }

            // Создаём или обновляем оценку (insert = update)
            Rating rating = new Rating(login, appId, value);
            ratingDao.insert(rating);

            return successResponse("Оценка успешно сохранена");

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        } catch (Exception e) {
            return errorResponse("Ошибка при сохранении оценки: " + e.getMessage());
        }
    } //Установка или изменение оценки заявки экспертом.

    public ServerResponse deleteRating(String token, String requestJsonString) {
        try {
            String login = database.getLoginByToken(token);
            String role = database.getRoleByToken(token);
            if (login == null || !"expert".equals(role)) {
                return errorResponse("Доступ запрещён или токен недействителен");
            }

            RemoveRatingRequest request = gson.fromJson(requestJsonString, RemoveRatingRequest.class);
            int appId = request.getApplicationId();

            // Проверяем, существует ли оценка от этого эксперта
            Rating existing = ratingDao.getByExpertAndApplication(login, appId);
            if (existing == null) {
                return errorResponse("Оценка не найдена");
            }

            ratingDao.deleteByExpertAndApplication(login, appId);

            return successResponse("Оценка успешно удалена");

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        }
    } //Удаление оценки экспертом.

    public ServerResponse getExpertRatings(String token) {
        try {
            String login = database.getLoginByToken(token);
            String role = database.getRoleByToken(token);
            if (login == null || !"expert".equals(role)) {
                return errorResponse("Доступ запрещён или токен недействителен");
            }

            List<Rating> ratings = ratingDao.getByExpertLogin(login);

            Map<String, Object> response = new HashMap<>();
            response.put("ratings", ratings);
            return new ServerResponse(200, gson.toJson(response));

        } catch (Exception e) {
            return errorResponse("Ошибка при получении оценок");
        }
    } //Получение всех оценок эксперта.

    public ServerResponse getApplicationRatings(String token, String requestJsonString) {
        try {
            String role = database.getRoleByToken(token);
            if (!"expert".equals(role)) {
                return errorResponse("Доступ разрешён только экспертам");
            }

            // Парсим ID заявки
            Map<String, Integer> req = gson.fromJson(requestJsonString, Map.class);
            int appId = req.get("applicationId").intValue();

            Application app = applicationDao.getById(appId);
            if (app == null) {
                return errorResponse("Заявка не найдена");
            }

            List<Rating> ratings = ratingDao.getByApplicationId(appId);

            Map<String, Object> response = new HashMap<>();
            response.put("applicationId", appId);
            response.put("ratings", ratings);
            response.put("averageRating", app.getAverageRating()); // можно добавить среднюю
            return new ServerResponse(200, gson.toJson(response));

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        } catch (Exception e) {
            return errorResponse("Ошибка при получении оценок заявки");
        }
    } //Получение всех оценок для конкретной заявки.

    private ServerResponse successResponse(String message) {
        Map<String, String> resp = new HashMap<>();
        resp.put("message", message);
        return new ServerResponse(200, gson.toJson(resp));
    }

    private ServerResponse errorResponse(String error) {
        Map<String, String> resp = new HashMap<>();
        resp.put("error", error);
        return new ServerResponse(400, gson.toJson(resp));
    }
}
