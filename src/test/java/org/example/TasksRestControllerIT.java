package org.example;

import org.example.repo.InMemTaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class TasksRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    InMemTaskRepository taskRepository;

    @Test
    void handleGetAllTasks_ReturnsValidResponseEntity() throws Exception {
        var requestBuilder = get("/api/tasks");


    }

}
