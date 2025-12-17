package academy.tochkavhoda.competition.dto.request;

import java.util.List;

public class AddApplicationDtoRequest {
    public String title;
    public String description;
    public List<String> directions;
    public double requestedAmount;

    public AddApplicationDtoRequest(){
        this.title = title;
        this.description = description;
        this.directions = directions;
        this.requestedAmount = requestedAmount;
    }

    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public List<String> getDirections(){return directions;}
    public double getRequestedAmount(){return requestedAmount;}
}
