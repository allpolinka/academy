package academy.tochkavhoda.competition.model;

public class Participant extends User{
    private final String companyName; //Название компании, которая хочет получить грант

    public Participant(String firstName, String lastName, String login, String password, String companyName){
        super(firstName, lastName, login, password);
        this.companyName = companyName;
    }
}
