package academy.tochkavhoda.competition.dao;

import academy.tochkavhoda.competition.model.Rating;

import java.util.List;

public interface RatingDao {
    void insert(Rating rating); //добавить новую оценку в "хранилище".
    Rating getByExpertAndApplication(String expertLogin, int applicationId); //найти оценку по логину эксперта и по ID заявки
    List<Rating> getByExpertLogin(String login); // Оценки одного эксперта
    List<Rating> getByApplicationId(int id); // Оценки одной заявки
    void deleteByExpertAndApplication(String expertLogin, int applicationId);
}
