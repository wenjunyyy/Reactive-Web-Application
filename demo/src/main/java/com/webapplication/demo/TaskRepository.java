package com.webapplication.demo;


import com.webapplication.demo.Task;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ReactiveCrudRepository<Task, Long> {
    // Additional query methods can be defined here if needed
}
