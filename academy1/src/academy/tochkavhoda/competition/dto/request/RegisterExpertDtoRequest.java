package academy.tochkavhoda.competition.dto.request;

import java.util.List;

public class RegisterExpertDtoRequest {
    public String firstName;
    public String lastName;
    public List<String> topics;
    private List<String> directions;
    public String login;
    public String password;

    public RegisterExpertDtoRequest(){
        this.firstName = firstName;
        this.lastName = lastName;
        this.directions = directions;
        this.login = login;
        this.password = password;
    }

    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public List<String> getDirections(){return directions;}
    public String getLogin(){return login;}
    public String getPassword(){return password;}
}
