package org.example.repo;

import org.example.Task;

import java.util.List;

public interface TaskRepository {
    List<Task> findAll();

    void save(Task task);
}
