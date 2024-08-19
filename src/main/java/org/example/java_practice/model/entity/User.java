package org.example.java_practice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.java_practice.model.entity.base.AbstractLockAuditableEntity;
import org.example.java_practice.model.entity.base.AuditableEntity;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Table(name = "m_users")
@Getter
@Setter
@DynamicUpdate
public class User extends AbstractLockAuditableEntity<Integer> {

    @Serial
    private static final long serialVersionUID = -1526569247363089092L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "tel")
    private String tel;

    @Column(name = "password")
    private String password;

    @Column(name = "login_fail_count")
    private Integer loginFailCount;

    @Column(name = "last_change_password")
    private LocalDateTime lastChangePassword;

    @Column(name = "token")
    private String token;

    @Column(name = "refresh_token")
    private String refreshToken;
}
