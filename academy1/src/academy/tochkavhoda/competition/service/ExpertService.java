package academy.tochkavhoda.competition.service;

import academy.tochkavhoda.competition.dao.ExpertDao;
import academy.tochkavhoda.competition.dao.RatingDao;
import academy.tochkavhoda.competition.datebase.Database;
import academy.tochkavhoda.competition.dto.request.LoginDtoRequest;
import academy.tochkavhoda.competition.dto.request.RegisterExpertDtoRequest;
import academy.tochkavhoda.competition.model.Expert;
import academy.tochkavhoda.competition.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExpertService {

    private ExpertDao expertDao;
    private RatingDao ratingDao;
    private final Database database;
    private final Gson gson;

    public ExpertService(ExpertDao expertDao, RatingDao ratingDao, Database database){
        this.expertDao = expertDao;
        this.ratingDao = ratingDao;
        this.database = Database.getInstance();
        this.gson = new Gson();
    }
    public ServerResponse registerExpert(String requestJsonString){
        try {
            RegisterExpertDtoRequest request = gson.fromJson(requestJsonString, RegisterExpertDtoRequest.class);

            // Валидация (по ТЗ: все поля обязательны, направления не пустые)
            if (request.getFirstName() == null || request.getFirstName().trim().isEmpty() ||
                    request.getLastName() == null || request.getLastName().trim().isEmpty() ||
                    request.getDirections() == null || request.getDirections().isEmpty() ||
                    request.getLogin() == null || request.getLogin().trim().isEmpty() ||
                    request.getPassword() == null || request.getPassword().length() < 6) {

                return errorResponse("Все поля обязательны. Должен быть хотя бы один direction. Пароль ≥6 символов.");
            }

            String login = request.getLogin().trim();

            // Уникальность логина
            if (expertDao.getByLogin(login) != null) {
                return errorResponse("Логин уже используется");
            }

            // Создаём модель Expert
            Expert expert = new Expert(
                    request.getFirstName().trim(),
                    request.getLastName().trim(),
                    request.getDirections(),
                    login,
                    request.getPassword()
            );

            expertDao.insert(expert);

            return successResponse("Эксперт успешно зарегистрирован");

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        }
    } //Регистрация нового эксперта в системе

    public ServerResponse login(String requestJsonString){
        try {
            LoginDtoRequest request = gson.fromJson(requestJsonString, LoginDtoRequest.class);

            Expert expert = expertDao.getByLogin(request.getLogin());
            if (expert == null || !expert.getPassword().equals(request.getPassword())) {
                return errorResponse("Неверный логин или пароль");
            }

            String token = UUID.randomUUID().toString();
            database.saveToken(token, expert.getLogin(), "expert");

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return new ServerResponse(200, gson.toJson(response));

        } catch (JsonSyntaxException e) {
            return errorResponse("Неверный формат JSON");
        }
    } //Авторизация эксперта в системе

    public ServerResponse logout(String token){
        if (database.removeToken(token)) {
            return successResponse("Сессия успешно завершена");
        }
        return errorResponse("Неверный токен");
    }//Завершение сессии эксперта

    public ServerResponse deleteExpert(String token){
        String login = database.getLoginByToken(token);
        String role = database.getRoleByToken(token);
        if (login == null || !"expert".equals(role)) {
            return errorResponse("Неверный токен или доступ запрещён");
        }

        Expert expert = expertDao.getByLogin(login);
        if (expert == null) {
            return errorResponse("Эксперт не найден");
        }

        // Удаляем все оценки эксперта
        ratingDao.getByExpertLogin(login)
                .forEach(r -> ratingDao.deleteByExpertAndApplication(login, r.getApplicationId()));

        // Удаляем эксперта
        expertDao.remove(Integer.valueOf(login));

        // Инвалидируем токен
        database.removeToken(token);

        return successResponse("Учётная запись эксперта удалена");
    } //Удаление учетной записи эксперта

    public ServerResponse getExpertProfile(String token){
        String login = database.getLoginByToken(token);
        String role = database.getRoleByToken(token);
        if (login == null || !"expert".equals(role)) {
            return errorResponse("Неверный токен или доступ запрещён");
        }

        Expert expert = expertDao.getByLogin(login);
        if (expert == null) {
            return errorResponse("Эксперт не найден");
        }

        // Формируем ответ (можно создать ExpertResponse DTO)
        Map<String, Object> profile = new HashMap<>();
        profile.put("firstName", expert.getFirstName());
        profile.put("lastName", expert.getLastName());
        profile.put("directions", expert.getExpertDirections());
        profile.put("login", expert.getLogin());

        return new ServerResponse(200, gson.toJson(profile));
    }  //Получение профиля эксперта

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