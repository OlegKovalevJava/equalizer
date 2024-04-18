package org.example.repo;

import org.example.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    List<Task> findAll();

    void save(Task task);

    Optional<Task> findById(UUID id);

}
