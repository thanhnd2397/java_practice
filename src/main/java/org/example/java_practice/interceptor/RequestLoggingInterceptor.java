package org.example.java_practice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.java_practice.config.CachedBodyHttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestLoggingInterceptor implements HandlerInterceptor {
    private final String TIME_LOG_KEY = "TIME_LOG";

    private final Logger logger = LogManager.getLogger(RequestLoggingInterceptor.class);

    private final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        request.setAttribute(TIME_LOG_KEY, System.currentTimeMillis());
        CachedBodyHttpServletRequest cachedRequest = this.wrapRequest(request);
        String requestBody = this.processRequestBody(cachedRequest);

        if (!requestBody.isEmpty()) {
            logger.info(method.getMethod().getDeclaringClass().getName() + " START "
                    + cachedRequest.getRequestURI() + " " + request.getMethod() + " Request Body "
                    + requestBody);
        } else {
            logger.info(method.getMethod().getDeclaringClass().getName() + " START "
                    + cachedRequest.getRequestURI() + " " + request.getMethod());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex)
            throws Exception {
        if (!(handler instanceof HandlerMethod method)) {
            return;
        }

        Long startTime = (Long) request.getAttribute(TIME_LOG_KEY);
        if (startTime != null) {
            long processTime = System.currentTimeMillis() - startTime;
            logger.info(
                    method.getMethod().getDeclaringClass().getName() + " FINISH " + processTime + " msec");
        }
    }

    private CachedBodyHttpServletRequest wrapRequest(HttpServletRequest request) {
        try {
            return new CachedBodyHttpServletRequest(request);
        } catch (IOException ex) {
            throw new RuntimeException("IO Exception");
        }
    }

    private String processRequestBody(CachedBodyHttpServletRequest request) {
        return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
