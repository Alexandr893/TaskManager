package org.example.taskmanager.service.UserTaskService;

import org.example.taskmanager.dao.entity.Comment;
import org.example.taskmanager.dao.entity.Task;
import org.example.taskmanager.dao.entity.User;
import org.example.taskmanager.dao.repository.CommentRepository;
import org.example.taskmanager.dao.repository.TaskRepository;
import org.example.taskmanager.dao.repository.UserRepository;
import org.example.taskmanager.dto.CommentDto;
import org.example.taskmanager.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserTaskServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private UserTaskService userTaskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateTaskStatus_Success() {
        Long taskId = 1L;
        Status newStatus = Status.IN_PROGRESS;
        String username = "user@example.com";

        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userTaskService.updateTaskStatus(taskId, newStatus, username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Статус задачи обновлен", response.getBody());
        verify(taskRepository).save(task);
    }

    @Test
    public void testUpdateTaskStatus_TaskNotFound() {
        Long taskId = 1L;
        String username = "user@example.com";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = userTaskService.updateTaskStatus(taskId, Status.IN_PROGRESS, username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Задача с ID " + taskId + " не найдена", response.getBody());
    }

    @Test
    public void testAddCommentToTask_Success() {
        Long taskId = 1L;
        String username = "user@example.com";

        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Отличная работа!");

        ResponseEntity<String> response = userTaskService.addCommentToTask(taskId, commentDto, username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Комментарий добавлен", response.getBody());
        verify(commentRepository).save(ArgumentMatchers.any(Comment.class));
    }

    @Test
    public void testAddCommentToTask_TaskNotFound() {
        Long taskId = 1L;
        String username = "user@example.com";
        CommentDto commentDto = new CommentDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = userTaskService.addCommentToTask(taskId, commentDto, username);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Задача с ID " + taskId + " не найдена", response.getBody());
    }




}