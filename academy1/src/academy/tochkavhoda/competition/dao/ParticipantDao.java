package academy.tochkavhoda.competition.dao;

import academy.tochkavhoda.competition.model.Participant;

public interface ParticipantDao {
    void insert(Participant p); //добавить нового участника в "хранилище".
    Participant getByLogin(String login); //найти участника по логину
    Participant getById(String id); //найти участника по id
    void remove(String id); //удалить участника из системы
}
