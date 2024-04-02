package org.example;

import java.util.List;

public interface TaskRepository {
    List<Task> findAll();

    void save(Task task);
}
