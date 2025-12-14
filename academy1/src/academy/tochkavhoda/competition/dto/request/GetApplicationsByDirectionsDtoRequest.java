package academy.tochkavhoda.competition.dto.request;

import java.util.List;

public class GetApplicationsByDirectionsDtoRequest {
    private List<String> directions;

    public GetApplicationsByDirectionsDtoRequest(List<String> directions){
        this.directions = directions;
    }

    public List<String> getDirections() {return directions;}
}
