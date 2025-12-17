package academy.tochkavhoda.competition.server;

import academy.tochkavhoda.competition.dto.request.*;
import academy.tochkavhoda.competition.dto.response.LoginDtoResponse;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import com.google.gson.Gson;


public class TestServer {
    private final Gson gson = new Gson();
    @Test
    public void testFullFlow() {
        Server server = new Server();
        // регистрируем участника
        RegisterParticipantDtoRequest p = new RegisterParticipantDtoRequest();
        p.firstName = "Ivan"; p.lastName = "Ivanov"; p.companyName = "StartupInc"; p.login = "p1"; p.password = "pass";
        ServerResponse r1 = server.registrParticipiant(gson.toJson(p));
        assertEquals(200, r1.getResponseCode());

        // регистрируем эксперта
        RegisterExpertDtoRequest ex = new RegisterExpertDtoRequest();
        ex.firstName = "E1"; ex.lastName = "Expert"; ex.topics = java.util.Arrays.asList("mathematics","informatics"); ex.login = "e1"; ex.password = "pass";
        ServerResponse r2 = server.registerExpert(gson.toJson(ex));
        assertEquals(200, r2.getResponseCode());

        // вход участника
        LoginDtoRequest lp = new LoginDtoRequest(); lp.login = "p1"; lp.password = "pass";
        ServerResponse r3 = server.login(gson.toJson(lp));
        assertEquals(200, r3.getResponseCode());
        LoginDtoResponse lr = gson.fromJson(r3.getResponseData(), LoginDtoResponse.class);
        assertNotNull(lr.getToken());

        // добавляем приложение
        AddApplicationDtoRequest app = new AddApplicationDtoRequest();
        app.title = "Project X"; app.description = "Cool"; app.directions = java.util.Arrays.asList("mathematics"); app.requestedAmount = 1000;
        ServerResponse r4 = server.addApplication(lr.getToken(), gson.toJson(app));
        assertEquals(200, r4.getResponseCode());

        // вход эксперта в систему
        LoginDtoRequest le = new LoginDtoRequest(); le.login = "e1"; le.password = "pass";
        ServerResponse r5 = server.login(gson.toJson(le));
        assertEquals(200, r5.getResponseCode());
        LoginDtoResponse er = gson.fromJson(r5.getResponseData(), LoginDtoResponse.class);
        assertNotNull(er.getToken());
    }
}

