package org.example.java_practice.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.java_practice.dao.AuthorityDao;
import org.example.java_practice.dao.BaseRepository;
import org.example.java_practice.dao.UserDao;
import org.example.java_practice.filter.TokenAuthentication;
import org.example.java_practice.model.dto.LoginResponse;
import org.example.java_practice.model.dto.TokenRequest;
import org.example.java_practice.model.entity.User;
import org.example.java_practice.service.AuthenticationService;
import org.example.java_practice.util.MyPageUtils;
import org.example.java_practice.util.exception.AccountNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
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

    private final UserDao userDao;

    private final AuthorityDao authorityDao;

    @Override
    public BaseRepository<User, Integer> getDao() {
        return this.userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new ServiceException("username is empty");
        }

        User user = this.userDao.getUserByEmail(username)
                .orElseThrow(() -> new AccountNotFoundException("User not existed"));
        List<SimpleGrantedAuthority> authorities = this.getAuthorities(user);
        LocalDateTime lastChangePassword = Objects.isNull(user.getLastChangePassword()) ?
                MyPageUtils.getInstance().getCurrentUTCDate() : user.getLastChangePassword();
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId().toString())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(user.getLoginFailCount() > 4)
                .disabled(false)
                .credentialsExpired(lastChangePassword.plusDays(90)
                        .isBefore(MyPageUtils.getInstance().getCurrentJapanDate()))
                .build();
    }

    @Override
    @Transactional
    public LoginResponse issueUserRefreshToken(Integer userId, TokenRequest tokenRequest)
            throws IOException {
        User user = this.userDao.getUserByUserIdAndRefreshToken(userId, tokenRequest.getToken(),
                tokenRequest.getRefreshToken()).orElseThrow(() -> new AccountNotFoundException("E000008"));
        String newToken = TokenAuthentication.createToken(user.getId().toString());
        String newRefreshToken = TokenAuthentication.createRefreshToken(userId.toString());
        List<String> authoriryList = this.getAuthorities(user).stream().map(
                SimpleGrantedAuthority::toString).collect(Collectors.toList());

        // Update token and new token
        user.setToken(newToken);
        user.setRefreshToken(newRefreshToken);
        this.save(user);

        return new LoginResponse(userId, newToken, newRefreshToken, authoriryList);
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
