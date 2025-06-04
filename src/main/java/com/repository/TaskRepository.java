package com.repository;

import com.orm.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByStatus(String status);


    List<Task> findByCategory(String category);


    Optional<Task> findById(Long id);


    List<Task> findAll();


    void deleteById(Long id);
}