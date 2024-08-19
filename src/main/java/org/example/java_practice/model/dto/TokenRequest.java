package org.example.java_practice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

@Getter
@Setter
public class TokenRequest implements Serializable {

    private static final long serialVersionUID = 8489486428822397614L;
    @NotBlank
    @JsonProperty("token")
    String token;

    @NotBlank
    @JsonProperty("refresh_token")
    String refreshToken;
}
