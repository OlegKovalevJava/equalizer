package org.example;

import org.example.errors.ErrorsPresentation;
import org.example.repo.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TasksRestControllerTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    TasksRestController controller;

    @Test
    @DisplayName("GET /api/tasks returns HTTP response with status 200 ОК and list tasks")
    void handleGetAllTasks_ReturnsValidResponseEntity() {
        var tasks = List.of(new Task(UUID.randomUUID(), "TaskOne", false),
                new Task(UUID.randomUUID(), "TaskTwo", true));

        doReturn(tasks).when(this.taskRepository).findAll();

        var responseEntity = this.controller.handleGetAllTasks();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    void handleCreateNewTask_PayloadValid_ReturnsValidResponseEntity() {
        var details = "TaskOne";

        var responseEntity = this.controller.handleCreateNewTask(new NewTaskPayload(details),
                UriComponentsBuilder.fromUriString("http://lochalhost:8080"), Locale.ENGLISH);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        if (responseEntity.getBody() instanceof Task task) {
            assertNotNull(task.id());
            assertEquals(details, task.details());
            assertFalse(task.completed());

            assertEquals(URI.create("http://lochalhost:8080/api/tasks/" + task.id()),
                    responseEntity.getHeaders().getLocation());

            verify(this.taskRepository).save(task);
        } else {
            assertInstanceOf(Task.class, responseEntity.getBody());
        }

        verifyNoMoreInteractions(this.taskRepository);
    }

    @Test
    void handleCreateNewTask_PayloadInvalid_ReturnsValidResponseEntity() {
        var details = "   ";
        var locale = Locale.US;
        var errorMessage = "Details is empty";

        doReturn(errorMessage).when(this.messageSource)
                .getMessage("task.create.details.errors.not_set", new Object[0], locale);

        var responseEntity = this.controller.handleCreateNewTask(new NewTaskPayload(details),
                UriComponentsBuilder.fromUriString("http://lochalhost:8080"), locale);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(new ErrorsPresentation(List.of(errorMessage)), responseEntity.getBody());

        verifyNoInteractions(taskRepository);
    }
}
