package org.example.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String text;
    private Long authorId;
    private Long taskId;
}
