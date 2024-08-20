package org.example.java_practice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.java_practice.model.entity.base.AbstractLockAuditableEntity;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.util.List;

@Entity
@Table(name = "m_roles")
@Getter
@Setter
@DynamicUpdate
public class Role extends AbstractLockAuditableEntity<Integer> {

    @Serial
    private static final long serialVersionUID = 730147193146851972L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<RoleAuthority> roleAuthorities;

    @Column(name = "role_name")
    private String roleName;
}
