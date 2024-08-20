package org.example.java_practice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.java_practice.model.entity.base.AbstractLockAuditableEntity;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;

@Entity
@Table(name = "t_user_roles")
@Getter
@Setter
@DynamicUpdate
public class UserRole extends AbstractLockAuditableEntity<Integer> {

    @Serial
    private static final long serialVersionUID = -8095733028042364390L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
