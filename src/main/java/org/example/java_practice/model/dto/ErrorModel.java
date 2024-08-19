package org.example.java_practice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorModel implements Serializable {

    private static final long serialVersionUID = -2537909859084826345L;
    private String errorCode;
    private String errorMessage;
}
