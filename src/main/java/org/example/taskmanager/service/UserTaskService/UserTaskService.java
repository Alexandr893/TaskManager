package org.example.taskmanager.service.UserTaskService;

import lombok.AllArgsConstructor;
import org.example.taskmanager.dao.entity.Comment;
import org.example.taskmanager.dao.entity.Task;
import org.example.taskmanager.dao.entity.User;
import org.example.taskmanager.dao.repository.CommentRepository;
import org.example.taskmanager.dao.repository.TaskRepository;
import org.example.taskmanager.dao.repository.UserRepository;
import org.example.taskmanager.dto.CommentDto;
import org.example.taskmanager.enums.Status;
import org.example.taskmanager.exceptions.TaskNotFoundException;
import org.example.taskmanager.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserTaskService implements IUserTaskService {

    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private CommentRepository commentRepository;

    @Override
    public ResponseEntity<String> updateTaskStatus(Long taskId, Status newStatus, String username) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        User user = findUserByEmail(username);

        if (isUserAssignee(task, user)) {
            task.setStatus(newStatus);
            taskRepository.save(task);
            return ResponseEntity.ok("Статус задачи обновлен");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещен: не назначенный исполнитель");
        }
    }

    @Override
    public ResponseEntity<String> addCommentToTask(Long taskId, CommentDto commentDTO, String username) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        User user = findUserByEmail(username);

        if (isUserAssignee(task, user)) {
            Comment comment = new Comment();
            comment.setText(commentDTO.getText());
            comment.setAuthor(user);
            comment.setTask(task);
            commentRepository.save(comment);
            return ResponseEntity.ok("Комментарий добавлен");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещен: не назначенный исполнитель");
        }
    }

    @Override
    public User findUserByEmail(String username) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        return userOptional.orElseThrow(() -> new UserNotFoundException(username));
    }

    private boolean isUserAssignee(Task task, User user) {
        return task.getAssignee() != null && task.getAssignee().getId().equals(user.getId());
    }

}
