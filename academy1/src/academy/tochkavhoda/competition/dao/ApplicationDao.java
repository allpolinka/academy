package academy.tochkavhoda.competition.dao;

import academy.tochkavhoda.competition.model.Application;
import academy.tochkavhoda.competition.model.Expert;
import academy.tochkavhoda.competition.model.Rating;

import java.util.List;

public interface ApplicationDao {
    void insert(Application a); //добавить новую заявку в "хранилище".
    Application getById(String id); //найти заявку по id
    List<Application> getAll(); //
    void insertRating(Rating r); //добавить новую оценку в "хранилище".
    List<Rating> getRatingsForApplication(String appId); //
    void removeRating(String ratingId); //удалить эксперта из системы
}
