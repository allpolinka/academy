package academy.tochkavhoda.competition.daoimpl;

import academy.tochkavhoda.competition.dao.ExpertDao;
import academy.tochkavhoda.competition.datebase.Database;
import academy.tochkavhoda.competition.model.Expert;

public class ExpertDaoImpl implements ExpertDao {
    private final Database db = Database.getInstance();

    @Override
    public void insert(Expert e) {db.insertExpert(e);}

    @Override
    public Expert getByLogin(String login){return db.getExpertByLogin(login);}

    @Override
    public Expert getById (Integer id) {return db.getExpertById(id);}

    @Override
    public void remove(Integer id) {db.removeExpert(id);}

}
