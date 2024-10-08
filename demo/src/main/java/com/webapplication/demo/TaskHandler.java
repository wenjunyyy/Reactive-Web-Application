package com.webapplication.demo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;

@Component
public class TaskHandler {

    private final TaskService taskService;

    public TaskHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    public Mono<ServerResponse> getAllTasks(ServerRequest request) {
        Flux<Task> tasks = taskService.getAllTasks();
        return ServerResponse.ok() //200 is ok
                .contentType(MediaType.APPLICATION_JSON) //indicate the response is in JSON format
                .body(tasks, Task.class);
        //return ServerResponse.ok().body(taskService.getAllTasks(), Task.class);
    }

    public Mono<ServerResponse> getTaskById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id")); //retrieve 'id' from URL and convert to Long
        Mono<Task> taskMono = taskService.getTaskById(id);
        return taskMono
                .flatMap(task -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(task))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createNewTask(ServerRequest request) {
        Mono<Task> taskMono = request.bodyToMono(Task.class);
        return taskMono
                .flatMap(taskService::createNewTask)
                .flatMap(task -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(task));
    }

    public Mono<ServerResponse> updateExistingTask(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Task> taskMono = request.bodyToMono(Task.class);
        return taskMono
                .flatMap(task -> taskService.updateExistingTask(id, task))
                .flatMap(updatedTask -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedTask))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteTask(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return taskService.deleteTask(id)
                .then(ServerResponse.noContent().build());
    }

}
