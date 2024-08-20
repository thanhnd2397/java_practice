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
@Table(name = "m_authorities")
@Getter
@Setter
@DynamicUpdate
public class Authority extends AbstractLockAuditableEntity<Integer> {

    @Serial
    private static final long serialVersionUID = 5166566965272762380L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "authority_name")
    private String authorityName;

    @Column(name = "authority_description")
    private String authorityDescription;

    @OneToMany(mappedBy = "authority", fetch = FetchType.LAZY)
    private List<RoleAuthority> roleFunctions;


}
