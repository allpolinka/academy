package academy.tochkavhoda.competition.dto.request;

public class RemoveRatingRequest {
    private int applicationId;

    public RemoveRatingRequest(int applicationId){
        this.applicationId = applicationId;
    }

    public int getApplicationId(){return applicationId;}
}
