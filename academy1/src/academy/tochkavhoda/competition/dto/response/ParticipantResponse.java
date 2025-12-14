package academy.tochkavhoda.competition.dto.response;

public class ParticipantResponse {
    private String firstName;
    private String lastName;
    private String companyName;
    private String login;

    public ParticipantResponse(String firstName, String lastName, String companyName, String login){
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.login = login;
    }

    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public String getCompanyName(){return companyName;}
    public String getLogin(){return login;}
}
