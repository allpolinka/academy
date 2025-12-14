package academy.tochkavhoda.competition.dto.response;

import java.util.List;

public class ApplicationResponse {
    private int id;                     // ID заявки
    private String title;
    private String description;
    private List<String> directions;
    private double requestedAmount; //Запрашиваемая сумма
    private String participantId;
    private double rating;

    public ApplicationResponse(int id, String title, String description, List<String> directions, double requestedAmount, String participantId, double rating){
        this.id = id;
        this.title = title;
        this.description = description;
        this.directions = directions;
        this.requestedAmount = requestedAmount;
        this.participantId = participantId;
        this.rating = rating;
    }

    public int getId(){return id;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public List<String> getDirections(){return directions;}
    public double getRequestedAmount(){return requestedAmount;}
    public String getParticipantId(){return participantId;}
    public double getRating(){return rating;}
}
