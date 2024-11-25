package org.example.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.taskmanager.dao.entity.User;

@Getter
@Setter
public class ReqResDto {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String email;
    private String role;
    private String password;
    private User user;
}
