package org.example.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.example.taskmanager.dto.ReqResDto;
import org.example.taskmanager.service.AuthService.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private IAuthService authService;

    @Operation(summary = "Регистрация пользователя/выдача ролей", description = "Регистрация пользователя")
    @PostMapping("/signup")
    public ResponseEntity<ReqResDto> signUp(@RequestBody ReqResDto signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @Operation(summary = "Логин", description = "Аутентификация пользователя/выдача ролей")
    @PostMapping("/signin")
    public ResponseEntity<ReqResDto> signIn(@RequestBody ReqResDto signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @Operation(summary = "Обновление токена")
    @PostMapping("/refresh")
    public ResponseEntity<ReqResDto> refreshToken(@RequestBody ReqResDto refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }


}
