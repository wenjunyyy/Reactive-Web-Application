package com.webapplication.demo;

import com.webapplication.demo.exceptions.InvalidTaskException;
import com.webapplication.demo.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Flux<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Mono<Task> getTaskById(Long id) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException("Task with id " + id + "not found")));
    }

    public Mono<Task> createNewTask(Task task) {
        return taskRepository.save(task)
                .switchIfEmpty(Mono.error(new InvalidTaskException("Task title cannot be empty")));

    }

    public Mono<Task> updateExistingTask(Long id, Task newtask) {
        return taskRepository.findById(id)
                .flatMap(existingtask -> {
                    existingtask.setTitle(newtask.getTitle());
                    existingtask.setDescription(newtask.getDescription());
                    existingtask.setStatus(newtask.getStatus());
                    existingtask.setDueDate(newtask.getDueDate());
                    return taskRepository.save(existingtask);
                })
                .switchIfEmpty(Mono.error(new TaskNotFoundException("Task with id " + id + "not found or exist for update")));
    }

    public Mono<Void> deleteTask(Long id) {
        return taskRepository.findById(id)
                .flatMap(existingTask -> taskRepository.deleteById(id))
                .switchIfEmpty(Mono.error(new TaskNotFoundException("Task with id " + id + "not found or exist for delete")));
    }
}
