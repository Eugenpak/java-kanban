package controllers;

import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class HttpTaskManagerEpicsTest {
    private HttpTaskServer taskServer;
    private TaskManager tm;

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @BeforeEach
    void init() throws IOException {
        tm = Managers.getDefault();
        taskServer = new HttpTaskServer(tm);
        taskServer.start();
    }

    @Test
    void epicsDeleteEpicByIdCode200_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsDeleteEpicByIdCode404_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsDeleteEpicByIdException_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsGetListEpicCode200_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsGetEpicById_Code200_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsGetEpicById_Code404_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsGetEpicById_Exception_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsPostAddNewEpicCode201_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsPostAddNewEpicCode406_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsPostAddNewEpicException_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsPostUpdateEpicCode201_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsPostUpdateEpicCode406_Z()  throws IOException, InterruptedException {}

    @Test
    void epicsPostUpdateEpicException_Z()  throws IOException, InterruptedException {}
    //--------------------------------------------------------------------
    class EpicListTypeToken extends TypeToken<List<Epic>> {
    }
}
