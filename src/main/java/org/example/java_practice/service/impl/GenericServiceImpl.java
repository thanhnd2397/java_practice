package org.example.java_practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.java_practice.dao.BaseRepository;
import org.example.java_practice.model.entity.base.AbstractLockAuditableEntity;
import org.example.java_practice.model.entity.base.EntityVersionHolder;
import org.example.java_practice.service.GenericService;
import org.example.java_practice.util.exception.UnModifiableException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public abstract class GenericServiceImpl<T extends AbstractLockAuditableEntity<I>, I extends Serializable>
        implements GenericService<T, I> {

    private static final Logger logger = LogManager.getLogger(GenericServiceImpl.class);

    private final EntityVersionHolder entityVersionHolder;

    private synchronized Map<String, Integer> getVersionHolder() {
        if (null == this.entityVersionHolder.getVersionHolder()) {
            Map<String, Integer> map = new ConcurrentHashMap<>();
            this.entityVersionHolder.setVersionHolder(map);
        }
        return this.entityVersionHolder.getVersionHolder();
    }


    @Override
    public T get(I id) throws ServiceException {
        T result = null;
        try {
            BaseRepository<T, I> repository = getDao();
            if (repository != null) {
                result = repository.findById(id).orElse(null);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public List<T> get(List<I> ids) throws ServiceException {
        List<T> result = new ArrayList<>();
        try {
            BaseRepository<T, I> repository = getDao();
            if (repository != null) {
                Iterable<T> tmp = repository.findAllById(ids);
                tmp.forEach(result::add);
            }
        } catch (Exception ex) {
            throw new ServiceException("E000014", ex);
        }
        return result;
    }

    @Override
    @Deprecated
    public T getForOptimisticLock(I id) throws ServiceException {
        T entity = this.get(id);
        if (entity != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(null == authentication || authentication instanceof AnonymousAuthenticationToken)) {
                Integer version = entity.getUpdateCount();
                // put the version of the current entity into memory
                this.getVersionHolder().put(this.getKey(authentication, entity, id), version);
            }
            return entity;
        } else {
            return null;
        }
    }

    @Override
    public List<T> getAll() throws ServiceException {
        List<T> result = new LinkedList<>();
        Iterable<T> iter = this.getDao().findAll();
        iter.forEach(result::add);
        return result;
    }

    @Override
    @Transactional
    public void delete(T entity) throws UnModifiableException, ServiceException {
        try {
            I id = entity.getId();
            if (id != null && this.getDao().existsById(id)) {
                if (Objects.equals(this.get(id).getUpdateCount(), entity.getUpdateCount())) {
                    this.getDao().delete(entity);
                } else {
                    logger.error("テーブル更新時に競合が発生しました、再検索してください。");
                    throw new UnModifiableException("テーブル更新時に競合が発生しました、再検索してください。");
                }
            }
        } catch (Exception ex) {
            throw new ServiceException("E000014", ex);
        }
    }

    @Override
    @Transactional
    public void deleteAll(List<T> entities) throws ServiceException {
        try {
            this.getDao().deleteAll(entities);
        } catch (Exception ex) {
            throw new ServiceException("E000014", ex);
        }
    }

    @Override
    public List<T> bulkSave(List<T> entities) throws ServiceException {
        List<T> results = new ArrayList<>();

        try {
            Iterable<T> temp = this.getDao().saveAll(entities);
            temp.forEach(results::add);
        } catch (Exception ex) {
            throw new ServiceException("E000014", ex);
        }
        return results;
    }

    @Override
    public T save(T entity) {
        T result;
        try {
            Integer updateCount = entity.getUpdateCount();
            // 更新の場合、更新回数に１を加算
            if (Objects.nonNull(updateCount)) {
                entity.setUpdateCount(updateCount + 1);
            } else {
                entity.setUpdateCount(0);
            }
            result = this.getDao().save(entity);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new UnModifiableException("E000012", ex);
        } catch (Exception ex) {
            throw new ServiceException("E000014", ex);
        }
        return result;
    }

    public void checkOptimisticLock(T entity, Integer updateCount) {
        if (!Objects.equals(entity.getUpdateCount(), updateCount)) {
            throw new UnModifiableException("E000012");
        }
    }

    @Override
    @Deprecated
    public T updateWithOptimisticLock(T entity) {
        T result;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || authentication instanceof AnonymousAuthenticationToken) {
            throw new ServiceException("");
        }
        // Compare the entity's in-memory's current version with its DB's current version
        String key = this.getKey(authentication, entity, entity.getId());
        int version = Optional.ofNullable(this.getVersionHolder().get(key))
                .orElseThrow(() -> new ServiceException(
                        String.format("Cant get version of this entity %s", entity.getClass())));
        if (version != entity.getUpdateCount()) {
            throw new UnModifiableException("E000012");
        }
        entity.setUpdateCount(version + 1);
        result = this.getDao().save(entity);
        //TODO remove entity version from version holder
        this.getVersionHolder().remove(key);

        return result;
    }

    @Override
    public T saveWithOptimisticLock(T entity, Integer requestUpdateCount) {
        T result;
        try {
            // 更新の場合
            if (!Objects.isNull(entity.getId())) {
                // 排他制御実施
                Integer entityUpdateCount = entity.getUpdateCount();
                if (!Objects.equals(entityUpdateCount, requestUpdateCount)) {
                    logger.error("テーブル更新時に競合が発生しました、再検索してください。");
                    throw new UnModifiableException("E000012");
                }
                // 更新回数に１を加算
                entity.setUpdateCount(entityUpdateCount + 1);
            } else {
                entity.setUpdateCount(0);
            }
            result = this.getDao().save(entity);
        } catch (Exception ex) {
            throw new ServiceException("E000014", ex);
        }
        return result;
    }

    private String getKey(Authentication authentication, T entity, I id) {
        String name = entity.getClass().getName();
        return authentication.getPrincipal() + "_" + name + "_" + id;
    }
}