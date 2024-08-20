package org.example.java_practice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.java_practice.dao.AuthorityDao;
import org.example.java_practice.dao.BaseRepository;
import org.example.java_practice.dao.UserDao;
import org.example.java_practice.model.dto.LoginResponse;
import org.example.java_practice.model.dto.TokenRequest;
import org.example.java_practice.model.entity.User;
import org.example.java_practice.service.AuthenticationService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"userAuthorityCache"})
public class AuthenticationServiceImpl extends GenericServiceImpl<User, Integer> implements
        AuthenticationService {

    @lombok.Getter
    private final UserDao userDao;

    private final AuthorityDao authorityDao;

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

    public Authentication getUserAuthenticationInfo(Integer userId, String token) {
        Authentication authentication = null;
        User user = this.userDao.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User not found"));
        if (user.getToken().equals(token)) {
            authentication = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword(),
                    this.getAuthorities(user));
        }
        return authentication;
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> authorityNames = this.authorityDao.getFunctionByUserId(user.getId()).stream()
                .filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(authorityNames)) {
            authorities.add(new SimpleGrantedAuthority("USER_VIEW"));
        } else {
            authorities = this.authorityDao.getFunctionByUserId(user.getId())
                    .stream().distinct().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return authorities;
    }
}
