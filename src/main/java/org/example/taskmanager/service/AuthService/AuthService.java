package org.example.taskmanager.service.AuthService;

import lombok.AllArgsConstructor;
import org.example.taskmanager.dao.entity.User;
import org.example.taskmanager.dao.repository.UserRepository;
import org.example.taskmanager.dto.ReqResDto;
import org.example.taskmanager.exceptions.InvalidCredentialsException;
import org.example.taskmanager.exceptions.TokenValidationException;
import org.example.taskmanager.exceptions.UserAlreadyExistsException;
import org.example.taskmanager.utill.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public ReqResDto signUp(ReqResDto registrationRequest) {
        ReqResDto resp = new ReqResDto();
        try {
            Optional<User> existingUser = userRepository.findByEmail(registrationRequest.getEmail());
            if (existingUser.isPresent()) {
                throw new UserAlreadyExistsException("Пользователь с данной электронной почтой уже существует");
            }
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setRole(registrationRequest.getRole());
            User userResult = userRepository.save(user);
            if (userResult.getId() > 0) {
                resp.setUser(userResult);
                resp.setMessage("Пользователь сохранен");
                resp.setStatusCode(200);
            }
        } catch (UserAlreadyExistsException e) {
            resp.setStatusCode(409); // Conflict
            resp.setError(e.getMessage());
        } catch (Exception e) {
            resp.setStatusCode(500); // Internal Server Error
            resp.setError("Ошибка сервера");
        }
        return resp;
    }

    @Override
    public ReqResDto signIn(ReqResDto signinRequest) {
        ReqResDto response = new ReqResDto();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
            var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(() -> new InvalidCredentialsException("Неверные учетные данные"));
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Пользователь вошел");
        } catch (InvalidCredentialsException e) {
            response.setStatusCode(401); // Unauthorized
            response.setError(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Ошибка сервера");
        }
        return response;
    }

    @Override
    public ReqResDto refreshToken(ReqResDto refreshTokenRequest) {
        ReqResDto response = new ReqResDto();
        try {
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("Недопустимый токен"));
            if (!jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                throw new TokenValidationException("Токен недействителен");
            }
            var jwt = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Успешное обновление токена");
        } catch (InvalidCredentialsException | TokenValidationException e) {
            response.setStatusCode(401); // Unauthorized
            response.setError(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Ошибка сервера");
        }
        return response;
    }
}
