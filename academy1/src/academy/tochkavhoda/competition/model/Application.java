package academy.tochkavhoda.competition.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Application {
    private static int nextId = 1;
    private Integer id; //уникальный код заявки
    private String participiantLogin; //Логин участника, подавшего заявку
    private String title; //Название заявки
    private String description; //Описание
    private Set<String> directions; //Список направлений
    private double requestedAmount; //Запрашиваемая сумма
    private Set<Rating> ratings; //Список оценок

    public Application(Integer id, String participiantLogin, String title, String description, Set<String> directions, double requestedAmount, Set<Rating> ratings) {
        this.id = id;
        this.participiantLogin = participiantLogin;
        this.title = title;
        this.description = description;
        this.directions = directions;
        this.requestedAmount = requestedAmount;
        this.ratings = ratings;
    }

    public Integer getId() {return id;}

    public String getParticipiantLogin() {return participiantLogin;}

    public String getTitle() {return title;}

    public String getDescription() {return description;}

    public Set<String> getDirections() {return directions;}

    public double getRequestedAmount() {return requestedAmount;}

    public Set<Rating> getRating() {return ratings;}

    // Метод для добавления/обновления оценки. Логика: Если оценка от этого эксперта уже есть, обновляем; иначе добавляем.
    public void addOrUpdateRating(Rating rating) {
        ratings.removeIf(r -> r.getExpertLogin().equals(rating.getExpertLogin())); // Удаляем старую, если есть
        ratings.add(rating); // Добавляем новую
    }

    // Метод для удаления оценки эксперта. Логика: Удаляем по логину эксперта.
    public void removeRating(String expertLogin) {
        ratings.removeIf(r -> r.getExpertLogin().equals(expertLogin));
    }

    // Вычисление средней оценки. Логика: Если оценок нет, возвращаем 0; иначе среднее.
    public double getAverageRating() {
        if (ratings.isEmpty()) return 0;
        return ratings.stream().mapToInt(Rating::getValue).average().orElse(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
