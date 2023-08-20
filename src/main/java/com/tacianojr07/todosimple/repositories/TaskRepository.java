package com.tacianojr07.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tacianojr07.todosimple.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    

    List<Task> findByUserId(Long id);
}
