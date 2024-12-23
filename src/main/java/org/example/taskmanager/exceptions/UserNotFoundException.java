package org.example.taskmanager.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("Пользователь с email " + email + " не найден");
    }
}
