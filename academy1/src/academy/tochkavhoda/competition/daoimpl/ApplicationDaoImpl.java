package academy.tochkavhoda.competition.daoimpl;

import academy.tochkavhoda.competition.dao.ApplicationDao;
import academy.tochkavhoda.competition.datebase.Database;
import academy.tochkavhoda.competition.model.Application;

import java.util.*;

public class ApplicationDaoImpl implements ApplicationDao {
    private final Database db = Database.getInstance();

    @Override
    public void insert(Application a){db.insertApplication(a);}

    @Override
    public Application getById(Integer id){return db.getApplicationById(id);}

    @Override
    public List<Application> getAll(){return db.getAllApplications();}

    @Override
    public List<Application> getByDirections(Set<String> directions){return db.getApplicationsByDirections(directions);}

    @Override
    public List<Application> getApplicationsByParticipantLogin(String login){return db.getApplicationsByParticipantLogin(login);}

    @Override
    public void remove(Integer id) {db.removeApplication(id);}
}

