package academy.tochkavhoda.competition.dao;

import academy.tochkavhoda.competition.model.Expert;

public interface ExpertDao {
    void insert(Expert e); //добавить нового эксперта в "хранилище".
    Expert getByLogin(String login); //найти эксперта по логину
    Expert getById(String id); //найти эксперта по id
    void remove(String id); //удалить эксперта из системы
}
