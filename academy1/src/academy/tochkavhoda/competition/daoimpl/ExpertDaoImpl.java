package academy.tochkavhoda.competition.daoimpl;

import academy.tochkavhoda.competition.dao.ExpertDao;
import academy.tochkavhoda.competition.model.Expert;
import java.util.HashMap;
import java.util.Map;

public class ExpertDaoImpl implements ExpertDao {
    private final Map<String, Expert> experts = new HashMap<>();

    @Override
    public void insert(Expert e) {experts.put(e.getLogin(), e);}

    @Override
    public Expert getByLogin(String login){return experts.get(login);}

    @Override
    public Expert getById (String id) {return experts.get(id);}

    @Override
    public void remove(String id) { experts.remove(id);}

}
