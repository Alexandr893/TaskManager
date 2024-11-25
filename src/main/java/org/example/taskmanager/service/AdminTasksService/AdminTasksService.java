package org.example.taskmanager.service.AdminTasksService;

import lombok.AllArgsConstructor;
import org.example.taskmanager.dao.entity.Comment;
import org.example.taskmanager.dao.entity.Task;
import org.example.taskmanager.dao.entity.User;
import org.example.taskmanager.dao.repository.CommentRepository;
import org.example.taskmanager.dao.repository.TaskRepository;
import org.example.taskmanager.dao.repository.UserRepository;
import org.example.taskmanager.dto.CommentDto;
import org.example.taskmanager.dto.TaskDto;
import org.example.taskmanager.enums.Priority;
import org.example.taskmanager.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminTasksService implements IAdminTasksService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;


    @Override
    public ResponseEntity<String> createTask(TaskDto taskDto) {
        User assignee = userRepository.findById(taskDto.getAssigneeId()).orElse(null);
        User author = userRepository.findById(taskDto.getAuthorId()).orElse(null);

        if (assignee == null || author == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Assignee or Author not found");
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setAssignee(assignee);
        task.setAuthor(author);

        taskRepository.save(task);
        return ResponseEntity.ok("Task created successfully");
    }

    @Override
    public ResponseEntity<String> updateTask(Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        User assignee = userRepository.findById(taskDto.getAssigneeId()).orElse(null);
        if (assignee == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Assignee not found");
        }

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
        task.setAssignee(assignee);

        taskRepository.save(task);
        return ResponseEntity.ok("Task updated successfully");
    }

    @Override
    public ResponseEntity<String> deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        taskRepository.delete(task);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @Override
    public ResponseEntity<String> updateTaskStatus(Long taskId, Status newStatus) {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        task.setStatus(newStatus);
        taskRepository.save(task);
        return ResponseEntity.ok("Task status updated");
    }

    @Override
    public ResponseEntity<String> updateTaskPriority(Long taskId, Priority newPriority) {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        task.setPriority(newPriority);
        taskRepository.save(task);
        return ResponseEntity.ok("Task priority updated");
    }

    @Override
    public ResponseEntity<String> addCommentToTask(Long taskId, CommentDto commentDto) {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        User author = userRepository.findById(commentDto.getAuthorId()).orElse(null);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author not found");
        }

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAuthor(author);
        comment.setTask(task);
        commentRepository.save(comment);
        return ResponseEntity.ok("Comment added");
    }

    @Override
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok(tasks);
    }

    @Override
    public Page<Task> getTasksByAuthor(Long authorId, Pageable pageable) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + authorId));
        return taskRepository.findByAuthor(author, pageable);
    }

    @Override
    public Page<Task> getTasksByAssignee(Long assigneeId, Pageable pageable) {
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found with id: " + assigneeId));
        return taskRepository.findByAssignee(assignee, pageable);
    }
}
