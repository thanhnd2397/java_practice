package org.example.java_practice.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.java_practice.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/auth/login-token",
            HttpMethod.POST.toString());
    private UserService userService;
    private MessageSource messageSource;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String userName = this.getUserName(request);
        String password = this.getPassword(request);
        return this.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(userName, userName + password,
                        Collections.emptyList()));
    }

    private String getUserName(HttpServletRequest request) {
        return StringUtils.isEmpty(request.getParameter("email")) ? "" : request.getParameter("email");
    }

    private String getPassword(HttpServletRequest request) {
        return StringUtils.isEmpty(request.getParameter("password")) ? "" : request.getParameter("password");
    }
}
