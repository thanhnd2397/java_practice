package org.example.java_practice.model.entity.base;

public interface LockableEntity {
    /**
     * Get current count update time of the entity.
     *
     * @return integer number present count of update of the entity.
     */
    Integer getUpdateCount();
}
