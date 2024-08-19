package org.example.java_practice.filter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.java_practice.util.MyPageUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationEntryPoint.class);
    private MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        if (this.messageSource == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webAppContext =
                    Optional.ofNullable(WebApplicationContextUtils.getWebApplicationContext(servletContext))
                            .orElseThrow(() -> new ServiceException("Cant get WebApplicationContext in Entry Point"));
            this.messageSource = webAppContext.getBean(MessageSource.class);
        }
        logger.error("Current request is: {}", request.getRequestURL().toString());
        logger.error("Unknown error: {}", ExceptionUtils.getStackTrace(authException));
        String message = this.messageSource.getMessage("E000999", null,
                LocaleContextHolder.getLocale());
        MyPageUtils.getInstance()
                .createFailResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "E000999",
                        message, "Authentication");
    }
}
