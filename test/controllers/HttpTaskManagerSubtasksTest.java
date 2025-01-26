package controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HttpTaskManagerSubtasksTest {
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
    void subtasksDeleteSubtaskByIdCode200_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksDeleteSubtaskByIdCode404_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksDeleteSubtaskByIdException_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksGetListSubtaskCode200_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksGetSubtaskById_Code200_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksGetSubtaskById_Code404_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksGetSubtaskById_Exception_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksPostAddNewSubtaskCode201_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksPostAddNewSubtaskCode406_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksPostAddNewSubtaskException_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksPostUpdateSubtaskCode201_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksPostUpdateSubtaskCode406_Z()  throws IOException, InterruptedException {}

    @Test
    void subtasksPostUpdateSubtaskException_Z()  throws IOException, InterruptedException {}
    //--------------------------------------------------------------------
}
