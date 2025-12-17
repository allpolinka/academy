package academy.tochkavhoda.competition.dto.request;

public class LoginDtoRequest {
    public String login;
    public String password;

    public LoginDtoRequest(){
        this.login = login;
        this.password = password;
    }

    public String getLogin(){return login;}
    public String getPassword(){return password;}
}
