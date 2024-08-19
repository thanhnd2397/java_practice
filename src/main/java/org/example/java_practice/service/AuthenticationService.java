package org.example.java_practice.service;

import org.example.java_practice.model.dto.LoginResponse;
import org.example.java_practice.model.dto.TokenRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

public interface AuthenticationService extends UserDetailsService {
    LoginResponse issueUserRefreshToken(Integer userId, TokenRequest tokenRequest) throws IOException;

    Authentication getUserAuthenticationInfo(Integer userId, String token);
}
