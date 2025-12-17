package academy.tochkavhoda.competition.dto.request;

public class RateApplicationRequest {
    public int applicationId;
    private int value;

    public RateApplicationRequest(int applicationId, int value){
        this.applicationId = applicationId;
        this.value = value;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getValue(){return value;}
}
