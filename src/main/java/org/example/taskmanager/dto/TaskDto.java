package org.example.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {
    private Long id;
    @NotBlank(message = "Укажите заголовок")
    private String title;
    @NotBlank(message = "описание")
    private String description;
    private String username;
    private Long authorId;
    private Long assigneeId;
    private Status status;
    private Priority priority;
}
