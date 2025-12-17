package academy.tochkavhoda.competition.dto.request;

public class CancelApplicationRequest {
    private int applicationId;

    public CancelApplicationRequest(int applicationId){
        this.applicationId = applicationId;
    }

    public int getApplicationId(){return applicationId;}
}
