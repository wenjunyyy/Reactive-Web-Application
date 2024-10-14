package com.webapplication.demo.service;

import com.webapplication.demo.Task;
import com.webapplication.demo.TaskRepository;
import com.webapplication.demo.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private Task task_without_id;

    @BeforeEach
    public void setup() {
        task1 = new Task("T010", "Homework", "math page2-5", LocalDateTime.of(2024,10,10, 11,59), "on hold");
        task2 = new Task("T012", "Assignment", "report", LocalDateTime.of(2024,11,15, 11,59), "on hold");
        task_without_id = new Task(null, "ABCD", "EFGH", LocalDateTime.now(), "on hold");

    }

    @Test
    public void GetAllTaskTest() {

        when(taskRepository.findAll()).thenReturn(Flux.just(task1, task2));

        Flux<Task> result1 = taskService.getAllTasks();

        StepVerifier.create(result1)
                //.expectNextCount(2)
                .consumeNextWith(task -> assertEquals("T010", task.getId()))
                .consumeNextWith(task -> assertEquals("T012", task.getId()))
                //.expectNext(task1)
                //.expectNext(task2)
                .expectComplete()
                .verify();

    }

    @Test
    public void SearchTaskByIdTest() {
        String id = task1.getId();
        when(taskRepository.findById(id)).thenReturn(Mono.just(task1));

        Mono<Task> result2 = taskService.getTaskById(id);

        StepVerifier.create(result2)
                .expectNext(task1)
                .verifyComplete();
    }

    @Test
    public void SaveNewTaskTest() {

        when(taskRepository.save(task1)).thenReturn(Mono.just(task1));

        Mono<Task> result_test = taskService.createNewTask(task1);

        StepVerifier.create(result_test)
                .expectNext(task1)
                .expectComplete()
                .verify();
    }

    @Test
    public void UpdateExistingTaskTest() {
        String id = task1.getId();
        when(taskRepository.findById(id)).thenReturn(Mono.just(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(task_without_id));

        Mono<Task> update_result = taskService.updateExistingTask(id, task_without_id);

        StepVerifier.create(update_result)
                //.consumeNextWith(task -> assertEquals(null, task.getId()))
                .consumeNextWith(task -> {
                    assertEquals("ABCD", task.getTitle());
                    assertEquals("EFGH", task.getDescription());
                })
                .expectComplete()
                .verify();
    }


    @Test
    public void DeleteTaskTest() {
        String id = task1.getId();


        when(taskRepository.findById(id)).thenReturn(Mono.just(task1));
        when(taskRepository.deleteById(id)).thenReturn(Mono.empty());

        Mono<Void> deleted_result = taskService.deleteTask(id);

        StepVerifier.create(deleted_result)
                .expectComplete()  // Verifies that the Mono completes successfully
                .verify();

    }

}
