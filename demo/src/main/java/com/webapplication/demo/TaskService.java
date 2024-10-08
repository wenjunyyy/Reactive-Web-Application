package com.webapplication.demo;

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

    public Flux<Task> getAllTask() {
        return taskRepository.findAll();
    }

    public Mono<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Mono<Task> createNewTask(Task task) {
        return taskRepository.save(task);
    }

    public Mono<Task> updateExistingTask(Long id, Task newtask) {
        return taskRepository.findById(id)
                .flatMap(existingtask -> {
                    existingtask.setTitle(newtask.getTitle());
                    existingtask.setDescription(newtask.getDescription());
                    existingtask.setStatus(newtask.getStatus());
                    existingtask.setDueDate(newtask.getDueDate());
                    return taskRepository.save(existingtask);
                });
    }

    public Mono<Void> deleteTask(Long id) {
        return taskRepository.deleteById(id);
    }
}
