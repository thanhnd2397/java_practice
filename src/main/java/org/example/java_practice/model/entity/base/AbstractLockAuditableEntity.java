package org.example.java_practice.model.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.example.java_practice.model.converter.DeleteStatusConverter;
import org.example.java_practice.model.enumeration.DeleteStatus;

import java.io.Serial;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractLockAuditableEntity<I extends Serializable> extends
        AuditableEntity implements Serializable, LockableEntity, IdentifyEntity<I>  {
    @Serial
    private static final long serialVersionUID = -566334026844128419L;
    @Column(name = "update_count")
    private Integer updateCount;

    @Column(name = "dlt_flg")
    @Convert(converter = DeleteStatusConverter.class)
    private DeleteStatus delFlg;
}
