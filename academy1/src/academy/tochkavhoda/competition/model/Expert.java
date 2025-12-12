package academy.tochkavhoda.competition.model;

import java.util.HashSet;
import java.util.Set;

public class Expert extends User{
    private Set<String> expertDirections; //Множество направлений экспертов

    public Expert(String firstName, String lastName, String login, String password, Set<String> expertDirections){
        super(firstName, lastName, login, password);
        this.expertDirections = expertDirections; //HashSet - копирует содержимое объекта и не похволяет его изменить
    }

    public Set<String> getExpertDirections() {
        return expertDirections;
    }
}
