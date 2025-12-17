package academy.tochkavhoda.competition.dto.request;

public class RegisterParticipantDtoRequest {
    public String firstName;
    public String lastName;
    public String companyName;
    public String login;
    public String password;

    public RegisterParticipantDtoRequest(){
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.login = login;
        this.password = password;
    }

    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public String getCompanyName(){return companyName;}
    public String getLogin(){return login;}
    public String getPassword(){return password;}
}
