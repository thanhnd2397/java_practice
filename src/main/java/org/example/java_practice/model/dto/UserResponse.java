package org.example.java_practice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.java_practice.model.dto.base.OptimisticLockBaseDto;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends OptimisticLockBaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8130271893643895387L;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("tel")
    private String tel;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String mail;

    @JsonProperty("login_fail_count")
    private Integer loginFailCount;

    @JsonProperty("last-change-password")
    private String lastChangePassword;

    @JsonProperty("department_name")
    private String departmentName;

    @Builder
    public UserResponse(Integer userId, String userName, String tel, String address, String mail,
                        Integer loginFailCount, String lastChangePassword, String departmentName, Integer updateCount) {
        super(updateCount);
        this.userId = userId;
        this.userName = userName;
        this.tel = tel;
        this.address = address;
        this.mail = mail;
        this.loginFailCount = loginFailCount;
        this.lastChangePassword = lastChangePassword;
        this.departmentName = departmentName;
    }
}
