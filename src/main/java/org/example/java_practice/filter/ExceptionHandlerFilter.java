package org.example.java_practice.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.java_practice.util.MyPageUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final MessageSource messageSource;
    private static final Logger logger = LogManager.getLogger(ExceptionHandlerFilter.class);

    public ExceptionHandlerFilter(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED.value(), "E000009", response, ex);
        } catch (SignatureException | MalformedJwtException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED.value(), "E000010", response, ex);
        }
    }

    private void setErrorResponse(int httpCode, String errorCode, HttpServletResponse response,
                                  Throwable ex) {
        logger.info("Error with token: " + ex.getMessage());
        String message = this.messageSource.getMessage(errorCode, null,
                LocaleContextHolder.getLocale());
        MyPageUtils.getInstance()
                .createFailResponse(response, httpCode, errorCode, message, "jwt token authentication");
    }
}
