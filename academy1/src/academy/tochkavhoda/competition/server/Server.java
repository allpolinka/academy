package academy.tochkavhoda.competition.server;

import academy.tochkavhoda.competition.dao.ApplicationDao;
import academy.tochkavhoda.competition.dao.ExpertDao;
import academy.tochkavhoda.competition.dao.ParticipantDao;
import academy.tochkavhoda.competition.dao.RatingDao;
import academy.tochkavhoda.competition.daoimpl.ApplicationDaoImpl;
import academy.tochkavhoda.competition.daoimpl.ExpertDaoImpl;
import academy.tochkavhoda.competition.daoimpl.ParticipantDaoImpl;
import academy.tochkavhoda.competition.daoimpl.RatingDaoImpl;
import academy.tochkavhoda.competition.datebase.Database;
import academy.tochkavhoda.competition.service.*;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class Server {
    private Gson gson = new Gson();
    private final ParticipantService participantService;
    private final ExpertService expertService;
    private final ApplicationService applicationService;
    private final RatingService ratingService;
    private final Database database;

    public Server() {
        ParticipantDao participantDao = new ParticipantDaoImpl();
        ExpertDao expertDao = new ExpertDaoImpl();
        ApplicationDao applicationDao = new ApplicationDaoImpl();
        RatingDao ratingDao = new RatingDaoImpl();

        this.database = Database.getInstance();

        this.participantService = new ParticipantService(participantDao, applicationDao, database);
        this.expertService = new ExpertService(expertDao, ratingDao, database);
        this.applicationService = new ApplicationService(applicationDao, participantDao, expertDao, ratingDao, database);
        this.ratingService = new RatingService(ratingDao, applicationDao, expertDao, database);
    }

    //Регистрация участников
    public ServerResponse registrParticipiant (String requestJsonString){
        return participantService.registerParticipant(requestJsonString);
    }

    //Регистрация экспертов
    public ServerResponse registerExpert(String requestJsonString) {
        return expertService.registerExpert(requestJsonString);
    }

    public ServerResponse login(String requestJsonString) {
        try {
            ServerResponse participantResponse = participantService.login(requestJsonString);
            if (participantResponse.getResponseCode() == 200) {
                return participantResponse;
            }
            return expertService.login(requestJsonString);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка авторизации");
            return new ServerResponse(400, gson.toJson(error));
        }
    }

    public ServerResponse logout(String token) {
        if (database.removeToken(token)) {
            Map<String, String> success = new HashMap<>();
            success.put("message", "Выход выполнен успешно");
            return new ServerResponse(200, gson.toJson(success));
        }
        return errorResponse("Неверный токен");
    }

    public ServerResponse addApplication(String token, String requestJsonString){
        if (!isValidToken(token, "participant")) {
            return errorResponse("Доступ запрещён или токен недействителен");
        }
        return applicationService.addApplication(token, requestJsonString);
    }

    public ServerResponse cancelApplication(String token, String requestJsonString){
        if (!isValidToken(token, "participant")) {
            return errorResponse("Доступ запрещён или токен недействителен");
        }
        return applicationService.cancelApplication(token, requestJsonString);
    }

    public ServerResponse leaveServerAsParticipant(String token) {
        String login = database.getLoginByToken(token);
        if (!isValidToken(token, "participant")) {
            return errorResponse("Доступ запрещён");
        }
        ServerResponse response = participantService.logout(login);
        database.removeToken(token);
        return response;
    }

    public ServerResponse rateApplication(String token, String requestJsonString){
        if (!isValidToken(token, "expert")) {
            return errorResponse("Доступ запрещён или токен недействителен");
        }
        return applicationService.rateApplication(token, requestJsonString);
    }

    public ServerResponse removeRating(String token, String requestJsonString){
        if (!isValidToken(token, "expert")) {
            return errorResponse("Доступ запрещён или токен недействителен");
        }
        return ratingService.deleteRating(token, requestJsonString);
    }

    public ServerResponse getApplicationsForExpert(String token, String requestJsonString){
        if (!isValidToken(token, "expert")) {
            return errorResponse("Доступ запрещён или токен недействителен");
        }
        return applicationService.getApplicationsForExpert(token, requestJsonString);
    }

    public ServerResponse getRatedApplications(String token, String requestJsonString){
        if (!isValidToken(token, "expert")) {
            return errorResponse("Доступ запрещён или токен недействителен");
        }
        return ratingService.getApplicationRatings(token, requestJsonString);
    }

    public ServerResponse leaveServerAsExpert(String token) {
        String login = database.getLoginByToken(token);
        if (!isValidToken(token, "expert")) {
            return errorResponse("Доступ запрещён");
        }
        ServerResponse response = expertService.deleteExpert(login);
        database.removeToken(token);
        return response;
    }

    private boolean isValidToken(String token, String requiredRole) {
        if (token == null) return false;
        String role = database.getRoleByToken(token);
        return requiredRole.equals(role);
    }

    private ServerResponse errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return new ServerResponse(400, gson.toJson(error));
    }
}
