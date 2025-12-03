package academy.tochkavhoda.competition.dao;

import academy.tochkavhoda.competition.model.Expert;
import academy.tochkavhoda.competition.model.Participant;

public interface ExpertDao {
    void insert(Expert e); //добавить нового эксперта в "хранилище".
    Participant getByLogin(String login); //найти эксперта по логину
    Participant getById(String id); //найти эксперта по id
    void remove(String id); //удалить эксперта из системы
}
