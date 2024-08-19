package org.example.java_practice.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.java_practice.dao.BaseRepository;
import org.example.java_practice.dao.UserDao;
import org.example.java_practice.model.dto.LoginResponse;
import org.example.java_practice.model.dto.TokenRequest;
import org.example.java_practice.model.entity.User;
import org.example.java_practice.service.AuthenticationService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"userAuthorityCache"})
public class AuthenticationServiceImpl extends GenericServiceImpl<User, Integer> implements
        AuthenticationService {

    private final UserDao userDao;

    @Override
    public LoginResponse issueUserRefreshToken(Integer userId, TokenRequest tokenRequest) throws IOException {
        return null;
    }

    @Override
    public Authentication getUserAuthenticationInfo(Integer userId, String token) {
        return null;
    }

    @Override
    public BaseRepository<User, Integer> getDao() {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
