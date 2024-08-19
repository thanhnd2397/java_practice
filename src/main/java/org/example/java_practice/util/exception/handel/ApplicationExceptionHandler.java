package org.example.java_practice.util.exception.handel;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.java_practice.model.dto.ErrorModel;
import org.example.java_practice.model.dto.RestResponse;
import org.example.java_practice.util.exception.NotFoundException;
import org.example.java_practice.util.exception.UnModifiableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
    private final MessageSource messageSource;

    public ApplicationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = {AccountNotFoundException.class, NotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(Exception ex) {
        RestResponse response = createError(ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
        RestResponse response = createError("E000009", ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SignatureException.class, MalformedJwtException.class})
    public ResponseEntity<?> handleJwtException(Exception ex) {
        RestResponse response = createError("E000010", ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        RestResponse response = createError("E000011", ex);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {UnModifiableException.class})
    public ResponseEntity<?> handleUnModifiableException(UnModifiableException ex) {
        RestResponse response = createError("E000012", ex);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleException(Exception ex) {
        RestResponse response = createError("E000999", ex);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private RestResponse createError(String errorCode, Throwable ex) {
        RestResponse response = new RestResponse();
        String errorMessage = this.messageSource.getMessage(errorCode, null,
                LocaleContextHolder.getLocale());
        ErrorModel error = new ErrorModel(errorCode, errorMessage);
        List<ErrorModel> errors = new ArrayList<>();
        errors.add(error);
        response.setSuccess(false);
        response.setError(errors);
        logger.error(ExceptionUtils.getStackTrace(ex));
        return response;
    }
}
