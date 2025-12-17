package academy.tochkavhoda.competition.server;

public class ServerResponse {
    private int responseCode;
    private String responseData;

    public ServerResponse(int responseCode, String responseData) {
        if (responseCode != 200 && responseCode != 400) {
            throw new IllegalArgumentException("responseCode должен быть 200 или 400");
        }
        if (responseData == null) {
            throw new IllegalArgumentException("responseData не может быть null");
        }
        this.responseCode = responseCode;
        this.responseData = responseData;
    }
    public int getResponseCode() { return responseCode; }
    public String getResponseData() { return responseData; }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "responseCode=" + responseCode +
                ", responseData='" + responseData + '\'' +
                '}';
    }
}
