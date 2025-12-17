package academy.tochkavhoda.competition.daoimpl;

import academy.tochkavhoda.competition.dao.RatingDao;
import academy.tochkavhoda.competition.model.Rating;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RatingDaoImpl implements RatingDao {
    private final ConcurrentHashMap<String, Rating> ratings = new ConcurrentHashMap<>();

    @Override
    public void insert(Rating rating) {
        if (rating == null) {
            throw new IllegalArgumentException("Оценка не может быть null");
        }
        String key = rating.getExpertId() + ":" + rating.getApplicationId();
        ratings.put(key, rating);
    }

    @Override
    public Rating getByExpertAndApplication(String expertLogin, int applicationId) {
        String key = expertLogin + ":" + applicationId;
        return ratings.get(key);
    }

    @Override
    public List<Rating> getByExpertLogin(String login) {
        return ratings.values().stream()
                .filter(r -> r.getExpertId().equals(login))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rating> getByApplicationId(int id) {
        return ratings.values().stream()
                .filter(r -> r.getApplicationId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByExpertAndApplication(String expertLogin, int applicationId) {
        String key = expertLogin + ":" + applicationId;
        ratings.remove(key);
    }

    public void deleteAllByExpertLogin(String expertLogin) {
        ratings.keySet().removeIf(key -> key.startsWith(expertLogin + ":"));
    }
}
