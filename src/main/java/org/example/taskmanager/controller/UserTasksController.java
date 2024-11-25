package org.example.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.taskmanager.dto.CommentDto;
import org.example.taskmanager.enums.Status;
import org.example.taskmanager.service.UserTaskService.IUserTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/user/")
@AllArgsConstructor
public class UserTasksController {

    private IUserTaskService userTaskService;

    @Operation(summary = "Обновить статус задачи ", description = "доступно только пользователю")
    @PostMapping("/{taskId}/status")
    public ResponseEntity<String> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam Status newStatus,
            @RequestParam String username) {

        return userTaskService.updateTaskStatus(taskId, newStatus, username);
    }

    @Operation(summary = "Добавить комментарий к задаче ", description = "доступно только пользователю")
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<String> addCommentToTask(
            @PathVariable Long taskId,
            @RequestBody CommentDto commentDTO,
            @RequestParam String username) {

        return userTaskService.addCommentToTask(taskId, commentDTO, username);
    }
}

