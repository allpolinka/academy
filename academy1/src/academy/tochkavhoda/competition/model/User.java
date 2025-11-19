package academy.tochkavhoda.competition.model;

public class User {
    private final String firstName; //Имя
    private final String lastName; //Фамилия
    private final String login; //Логин
    private final String password; //Пароль

    //Конструктор - создает объект User с заданными параметрами
    public User(String firstName, String lastName, String login, String password){
        this.firstName = firstName;    //Инициализируем поле firstName
        this.lastName = lastName;      //Инициализируем поле lastName
        this.login = login;            //Инициализируем поле login
        this.password = password;      //Инициализируем поле password
    }

    public String getFirstName(){ return firstName; } //Безопасное получение (get) firstName (возвращаем его)

    public String getLastName(){ return lastName; }

    public String getLogin(){ return login; }

    public String getPassword(){ return password; }
}
