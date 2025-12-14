package academy.tochkavhoda.competition.dto.request;

public class SetRatingDtoRequest {
    private int applicationId;
    private int value;

    public SetRatingDtoRequest( int applicationId, int value){
        this.applicationId = applicationId;
        this.value = value;
    }
}
