package org.example.java_practice.util.exception;

import java.io.Serial;

public class AccountNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3728302510723294775L;

    public AccountNotFoundException(String msg) {
        super(msg);
    }

    public AccountNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
