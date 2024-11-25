package org.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;
    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;
    @NotBlank(message = "Email не может быть пустым")
    private String email;
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
    private String role;
}
