package academy.tochkavhoda.competition.datebase;

import academy.tochkavhoda.competition.model.Application;
import academy.tochkavhoda.competition.model.Expert;
import academy.tochkavhoda.competition.model.Participant;
import academy.tochkavhoda.competition.model.Rating;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    // Единственный экземпляр
    private static final Database INSTANCE = new Database();

    private final Map<String, Participant> participants = new ConcurrentHashMap<>(); // ключ: login
    private final Map<String, Expert> experts = new ConcurrentHashMap<>();           // ключ: login
    private final Map<Integer, Application> applications = new ConcurrentHashMap<>(); // ключ: id заявки
    private final Map<String, Rating> ratings = new ConcurrentHashMap<>();           // ключ: expertLogin + ":" + applicationId
    private final Map<String, String> tokens = new ConcurrentHashMap<>(); // token → login
    private final Map<String, String> tokenRoles = new ConcurrentHashMap<>(); // token → "participant"/"expert"

    private Database() {    } // Приватный конструктор — запрещаем создание извне

    // Метод получения единственного экземпляра
    public static Database getInstance() {
        return INSTANCE;
    }

    //Методы для участников
    public void insertParticipant(Participant participant) {
        participants.put(participant.getLogin(), participant);
    }

    public Participant getParticipantByLogin(String login) {return participants.get(login);}

    public Participant getParticipantById(Integer id){return participants.get(id);}

    public void removeParticipant(String login) {participants.remove(login);}

    public List<Participant> getAllParticipants() {return new ArrayList<>(participants.values());}

    //Методы для экспертов
    public void insertExpert(Expert expert) {experts.put(expert.getLogin(), expert);}

    public Expert getExpertByLogin(String login) {return experts.get(login);}

    public Expert getExpertById(Integer id){ return experts.get(id);}

    public void removeExpert(Integer id) { experts.remove(id);}

    public List<Expert> getAllExperts() {return new ArrayList<>(experts.values());}

    //Методы для заявок
    public void insertApplication(Application application) {    //Создание заявки
        applications.put(application.getId(), application);
    }

    public Application getApplicationById(int id) {  //Поиск заявки по уникальному id
        return applications.get(id);
    }

    public void removeApplication(int id) {  //Удаление заявки по id
        applications.remove(id);
    }

    public List<Application> getAllApplications() {
        return new ArrayList<>(applications.values());
    }

    public List<Application> getApplicationsByParticipantLogin(String login) {
        return applications.values().stream()
                .filter(app -> app.getParticipiantLogin().equals(login))
                .toList();
    }

    public List<Application> getApplicationsByDirections(Set<String> directions) {
        return applications.values().stream()
                .filter(app -> !Collections.disjoint(app.getDirections(), directions))
                .toList();
    }

    //Метод для оценок
    public void insertOrUpdateRating(Rating rating) {
        String key = rating.getExpertId() + ":" + rating.getApplicationId();
        ratings.put(key, rating);
    }

    public Rating getRating(String expertId, int applicationId) {
        String key = expertId + ":" + applicationId;
        return ratings.get(key);
    }

    public void removeRating(String expertId, int applicationId) {
        String key = expertId + ":" + applicationId;
        ratings.remove(key);
    }

    public void removeAllRatingsByExpert(String expertId) {
        ratings.keySet().removeIf(key -> key.startsWith(expertId + ":"));
    }

    public List<Rating> getRatingsByApplication(int applicationId) {
        return ratings.values().stream()
                .filter(r -> r.getApplicationId() == applicationId)
                .toList();
    }

    public List<Rating> getRatingsByExpert(String expertId) {
        return ratings.values().stream()
                .filter(r -> r.getExpertId().equals(expertId))
                .toList();
    }

    public void saveToken(String token, String login, String role) {
        tokens.put(token, login);
        tokenRoles.put(token, role);
    }

    public boolean removeToken(String token) {
        if (tokens.remove(token) != null) {
            tokenRoles.remove(token);
            return true;
        }
        return false;
    }

    public String getLoginByToken(String token) {
        return tokens.get(token);
    }

    public String getRoleByToken(String token) {
        return tokenRoles.get(token);
    }
}
