package academy.tochkavhoda.competition.daoimpl;

import academy.tochkavhoda.competition.model.Participant;
import academy.tochkavhoda.competition.dao.ParticipantDao;
import java.util.HashMap;
import java.util.Map;

public class ParticipantDaoImpl implements ParticipantDao{
    private final Map<String, Participant> participants = new HashMap<>();

    @Override
    public void insert(Participant p) {participants.put(p.getLogin(), p);} // Добавляем или перезаписываем

    @Override
    public Participant getByLogin(String login){return participants.get(login);}

    @Override
    public Participant getById(String id){return participants.get(id);}

    @Override
    public void remove(String id) {participants.remove(id);}
}
