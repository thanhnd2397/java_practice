package org.example.java_practice.model.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptimisticLockBaseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 3675620304462388098L;

    @NotNull
    @JsonProperty("update-count")
    private Integer updateCount;
}
