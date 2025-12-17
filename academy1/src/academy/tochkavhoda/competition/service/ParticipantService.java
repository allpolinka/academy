package academy.tochkavhoda.competition.service;

import academy.tochkavhoda.competition.dao.ApplicationDao;
import academy.tochkavhoda.competition.dao.ParticipantDao;
import academy.tochkavhoda.competition.datebase.Database;
import academy.tochkavhoda.competition.dto.request.LoginDtoRequest;
import academy.tochkavhoda.competition.dto.request.RegisterParticipantDtoRequest;
import academy.tochkavhoda.competition.model.Participant;
import academy.tochkavhoda.competition.server.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParticipantService {

    private final ParticipantDao participantDao;
    private final Database database;
    private final Gson gson;

    public ParticipantService(ParticipantDao participantDao, ApplicationDao applicationDao, Database database) {
        this.participantDao = participantDao;
        this.database = database;
        this.gson = new Gson();
    }

    public ServerResponse registerParticipant(String requestJsonString) {
        try {
            RegisterParticipantDtoRequest request = gson.fromJson(requestJsonString, RegisterParticipantDtoRequest.class);

            // Валидация полей (по ТЗ: все поля обязательны, логин уникален)
            if (request.getFirstName() == null || request.getFirstName().trim().isEmpty() ||
                    request.getLastName() == null || request.getLastName().trim().isEmpty() ||
                    request.getCompanyName() == null || request.getCompanyName().trim().isEmpty() ||
                    request.getLogin() == null || request.getLogin().trim().isEmpty() ||
                    request.getPassword() == null || request.getPassword().length() < 6) {

                Map<String, String> error = new HashMap<>();
                error.put("error", "Все поля обязательны. Пароль должен быть не менее 6 символов.");
                return new ServerResponse(400, gson.toJson(error));
            }
            String login = request.getLogin().trim();

            if (participantDao.getByLogin(login) != null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Логин уже используется");
                return new ServerResponse(400, gson.toJson(error));
            }
            Participant participant = new Participant(
                    request.getFirstName().trim(),
                    request.getLastName().trim(),
                    request.getCompanyName().trim(),
                    login,
                    request.getPassword() // в реальном проекте — хэшировать!
            );

            // Сохраняем в DAO (в памяти)
            participantDao.insert(participant);
        } catch (JsonSyntaxException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Неверный формат JSON");
            return new ServerResponse(400, gson.toJson(error));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при регистрации: " + e.getMessage());
            return new ServerResponse(400, gson.toJson(error));
        }
        return null;
    }

    public ServerResponse login(String requestJsonString){
        try {
            LoginDtoRequest request = gson.fromJson(requestJsonString, LoginDtoRequest.class);

            if (request.getLogin() == null || request.getPassword() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Логин и пароль обязательны");
                return new ServerResponse(400, gson.toJson(error));
            }

            Participant participant = participantDao.getByLogin(request.getLogin());

            if (participant == null || !participant.getPassword().equals(request.getPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Неверный логин или пароль");
                return new ServerResponse(400, gson.toJson(error));
            }

            // Генерируем токен сессии
            String token = UUID.randomUUID().toString();

            // Сохраняем токен в Database (для проверки в других методах)
            database.saveToken(token, participant.getLogin(), "participant");

            // Ответ с токеном
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return new ServerResponse(200, gson.toJson(response));

        } catch (JsonSyntaxException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Неверный формат JSON");
            return new ServerResponse(400, gson.toJson(error));
        }
    }
    public ServerResponse logout(String token){
        if (token == null || token.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Токен обязателен");
            return new ServerResponse(400, gson.toJson(error));
        }

        // Удаляем токен из базы
        if (database.removeToken(token)) {
            return new ServerResponse(200, "{}");
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Неверный или просроченный токен");
            return new ServerResponse(400, gson.toJson(error));
        }
    }
}