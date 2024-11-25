package org.example.taskmanager.service.UserTaskService;

import org.example.taskmanager.dao.entity.User;
import org.example.taskmanager.dto.CommentDto;
import org.example.taskmanager.enums.Status;
import org.springframework.http.ResponseEntity;

public interface IUserTaskService {
    ResponseEntity<String> updateTaskStatus(Long taskId, Status newStatus, String username);
    ResponseEntity<String> addCommentToTask(Long taskId, CommentDto commentDTO, String username);
    User findUserByEmail(String username);
}
