package org.example.taskmanager.service.AuthService;

import org.example.taskmanager.dto.ReqResDto;

public interface IAuthService {
    ReqResDto signUp(ReqResDto registrationRequest);
    ReqResDto signIn(ReqResDto signinRequest);
    ReqResDto refreshToken(ReqResDto refreshTokenRequest);
}
