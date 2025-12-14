package academy.tochkavhoda.competition.dao;

import academy.tochkavhoda.competition.model.Application;

import java.util.List;
import java.util.Set;

public interface ApplicationDao {
    void insert(Application a); //добавить новую заявку в "хранилище".
    Application getById(Integer id); //найти заявку по id
    List<Application> getAll(); //
    List<Application> getByDirections(Set<String> directions); // Заявки по направлениям (для эксперта).
    List<Application> getParticipiantLogin(String login); // Заявки участника (для удаления).
    void remove(int id);
}
