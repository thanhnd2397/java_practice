package org.example.java_practice.model.entity.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@ApplicationScope
@Component
@Getter
@Setter
public class EntityVersionHolder implements Serializable {

    @Serial
    private static final long serialVersionUID = -1824454755964104635L;

    private Map<String, Integer> versionHolder;

    public EntityVersionHolder() {
        System.out.println("EntityVersionHolder is start !");
    }
}
