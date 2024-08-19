package org.example.java_practice.model.entity.base;

import java.io.Serializable;

public interface IdentifyEntity<I extends Serializable> {
    /**
     * Retrieve ID of entity.
     *
     * @return {@link Serializable} instance.
     */
    I getId();
}
