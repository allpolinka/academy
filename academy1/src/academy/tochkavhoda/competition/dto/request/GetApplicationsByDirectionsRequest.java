package academy.tochkavhoda.competition.dto.request;

import java.util.Set;

public class GetApplicationsByDirectionsRequest {
    private Set<String> directions;

    public Set<String> getDirections() {
        return directions;
    }

    public void setDirections(Set<String> directions) {
        this.directions = directions;
    }
}
