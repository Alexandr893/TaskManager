package org.example.taskmanager.dao.repository;

import org.example.taskmanager.dao.entity.Task;
import org.example.taskmanager.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAuthor(User author, Pageable pageable);
    Page<Task> findByAssignee(User assignee,Pageable pageable);
}
