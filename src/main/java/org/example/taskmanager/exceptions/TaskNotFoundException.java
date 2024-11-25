package org.example.taskmanager.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Задача с ID " + taskId + " не найдена");
    }
}