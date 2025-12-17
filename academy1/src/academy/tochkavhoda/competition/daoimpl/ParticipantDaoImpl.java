package academy.tochkavhoda.competition.daoimpl;

import academy.tochkavhoda.competition.datebase.Database;
import academy.tochkavhoda.competition.model.Participant;
import academy.tochkavhoda.competition.dao.ParticipantDao;

public class ParticipantDaoImpl implements ParticipantDao{
    private final Database db = Database.getInstance();

    @Override
    public void insert(Participant p) {db.insertParticipant(p);} // Добавляем или перезаписываем

    @Override
    public Participant getByLogin(String login){return db.getParticipantByLogin(login);}

    @Override
    public Participant getById(Integer id){return db.getParticipantById(id);}

    @Override
    public void remove(Integer id) {db.removeParticipant(String.valueOf(id));}
}
