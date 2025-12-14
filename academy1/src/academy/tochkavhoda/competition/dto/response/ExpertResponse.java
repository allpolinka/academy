package academy.tochkavhoda.competition.dto.response;

import java.util.List;

public class ExpertResponse {
    private String firstName;
    private String lastName;
    private List<String> directions;
    private String login;

    public ExpertResponse(String firstName, String lastName, List<String> directions, String login){
        this.firstName = firstName;
        this.lastName = lastName;
        this.directions = directions;
        this.login = login;
    }

    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public List<String> getDirections(){return directions;}
    public String getLogin(){return login;}
}
