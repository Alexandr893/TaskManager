package org.example.taskmanager.service.AdminTasksService;

import org.example.taskmanager.dao.entity.Task;
import org.example.taskmanager.dto.CommentDto;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.enums.Priority;
import org.example.taskmanager.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAdminTasksService {

    ResponseEntity<String> createTask(TaskDto taskDto);

    ResponseEntity<String> updateTask(Long taskId, TaskDto taskDto);

    ResponseEntity<String> deleteTask(Long taskId);

    ResponseEntity<String> updateTaskStatus(Long taskId, Status newStatus);

    ResponseEntity<String> updateTaskPriority(Long taskId, Priority newPriority);

    ResponseEntity<String> addCommentToTask(Long taskId, CommentDto commentDto);

    ResponseEntity<List<Task>> getAllTasks();

    Page<Task> getTasksByAuthor(Long authorId, Pageable pageable);

    Page<Task> getTasksByAssignee(Long assigneeId, Pageable pageable);
}
