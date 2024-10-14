package com.webapplication.demo.repository;


import com.webapplication.demo.Task;
import com.webapplication.demo.TaskRepository;
import com.webapplication.demo.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAndFindById() {

        Task task1 = new Task(null, "Test Task", "This is a test task", LocalDateTime.of(2024, 10,15,10,0),"IN_PROGRESS");

        when(taskRepository.save(any(Task.class))).thenReturn(Mono.just(task1));
        when(taskRepository.findById(task1.getId())).thenReturn(Mono.just(task1));

        //save task
        Mono<Task> savedResult = taskRepository.save(task1);
        //find task by id
        Mono<Task> findResult = taskRepository.findById(task1.getId());

        StepVerifier.create(savedResult)
                .expectNextMatches(savedTask -> savedTask.getTitle().equals("Test Task"))
                .verifyComplete();

        StepVerifier.create(findResult)
                .expectNext(task1)
                .verifyComplete();
    }
}
