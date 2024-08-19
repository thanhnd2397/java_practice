package org.example.java_practice.filter;

import org.example.java_practice.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.java_practice.util.Constants;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(JWTAuthenticationFilter.class);

    private AuthenticationService authenticationService;


    public JWTAuthenticationFilter() {
        System.out.println("JWTAuthenticationFilter starts !");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Validate if the current request's token is valid or not
        Authentication authentication = TokenAuthentication.getAuthentication(request);

        if (null == authentication) {
            if (request.getRequestURI().contains("/api/v1/auth/")
                    || request.getRequestURI().contains("/swagger-ui.html")
                    || request.getRequestURI().contains("/webjars")
                    || request.getRequestURI().contains("/swagger-ui/")
                    || request.getRequestURI().contains("/swagger-config")
                    || request.getRequestURI().contains("/v3/api-docs")
                    || request.getRequestURI().contains("/favicon")) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        } else {
            // Bind the user service instance into the current filter's servlet context
            if (null == this.authenticationService) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webAppContext =
                        Optional.ofNullable(WebApplicationContextUtils.getWebApplicationContext(servletContext))
                                .orElseThrow(() -> new ServiceException("Cant get WebApplicationContext in JWTAuthenticationFilter"));
                this.authenticationService = webAppContext.getBean(AuthenticationService.class);
            }

            //get user's userId
            try {
                String token = request.getHeader(Constants.HEADER_STRING).replace(Constants.TOKEN_PREFIX, "");
                int userId = Integer.parseInt(authentication.getName());
                authentication = this.authenticationService.getUserAuthenticationInfo(userId, token);
                if (null == authentication) {
                    logger.info(String.format("%dのアカウントはすでに削除されました。", userId));
                    SecurityContextHolder.clearContext();
                } else {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            filterChain.doFilter(request, response);
        }
    }
}
