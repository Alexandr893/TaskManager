package org.example.taskmanager.service.AuthService;

import org.example.taskmanager.dao.entity.User;
import org.example.taskmanager.dao.repository.UserRepository;
import org.example.taskmanager.dto.ReqResDto;
import org.example.taskmanager.utill.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class IAuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUpSuccess() {
        ReqResDto request = new ReqResDto();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");

        ReqResDto response = authService.signUp(request);

        assertEquals(200, response.getStatusCode());
        assertEquals(user, response.getUser());
    }

    @Test
    public void testSignInSuccess() {
        ReqResDto request = new ReqResDto();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("jwtToken");

        HashMap<String, Object> mockedMap = new HashMap<>();
        when(jwtUtils.generateRefreshToken(any(HashMap.class), eq(user))).thenReturn("refreshToken");

        ReqResDto response = authService.signIn(request);

        assertEquals(200, response.getStatusCode());
        assertEquals("jwtToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

}