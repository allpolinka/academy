package academy.tochkavhoda.competition.model;

import java.util.Set;

public class Expert extends User{
    private final Integer id;
    private Set<String> expertDirections; //Множество направлений экспертов

    public Expert(String firstName, String lastName, String login, String password, Integer id, Set<String> expertDirections){
        super(firstName, lastName, login, password);
        this.id = id;
        this.expertDirections = expertDirections;
    }

    public Set<String> getExpertDirections() {
        return expertDirections;
    }
}
