package com.webapplication.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TaskRouter {

    @Bean
    public RouterFunction<ServerResponse> taskRoutes(TaskHandler taskHandler) {
        return route()
                .POST("/tasks", taskHandler::createNewTask) // Create a new task
                .GET("/tasks", taskHandler::getAllTasks) // Retrieve all tasks
                .GET("/tasks/{id}", taskHandler::getTaskById) // Retrieve a task by ID
                .PUT("/tasks/{id}", taskHandler::updateExistingTask) // Update a task by ID
                .DELETE("/tasks/{id}", taskHandler::deleteTask) // Delete a task by ID
                .build();
    }
}

