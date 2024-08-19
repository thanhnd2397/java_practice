package org.example.java_practice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.java_practice.model.dto.base.OptimisticLockBaseDto;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest extends OptimisticLockBaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -6335840756487866021L;
    @JsonProperty("name")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("address")
    private String address;

    @JsonProperty("password")
    private String password;

    @JsonProperty("department_id")
    private Integer departmentId;

    @Builder
    public UserRequest(Integer updateCount, String userName,
                       String email, String tel, String address, String password, Integer departmentId) {
        super(updateCount);
        this.userName = userName;
        this.email = email;
        this.tel = tel;
        this.address = address;
        this.password = password;
        this.departmentId = departmentId;
    }
}
