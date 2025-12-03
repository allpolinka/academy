package academy.tochkavhoda.competition.model;

import java.util.Objects;

public class Rating {
    private final String expertLogin; //Логин эксперта
    private final int applicationId; //ID заявки
    private int value; //Значение оценки (1-5)

    public Rating(String expertLogin, int applicationId, int value){
        this.expertLogin = expertLogin;
        this.applicationId = applicationId;
        this.value = value;
    }

    public String getExpertLogin() {return expertLogin;}

    public int getApplicationId() {return applicationId;}

    public int getValue() {return value;}

    //Сеттер для value. Логика: Позволяет изменить оценку, с проверкой диапазона.
    public void setValue(int value){
        if (value < 1 || value > 5) throw new IllegalArgumentException("Рейтинг должен быть от 1 до 5");
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return applicationId == rating.applicationId && Objects.equals(expertLogin, rating.expertLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expertLogin, applicationId);
    }
}
