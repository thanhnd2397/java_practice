package org.example.java_practice.service;

import org.example.java_practice.dao.BaseRepository;
import org.example.java_practice.model.entity.base.AbstractLockAuditableEntity;
import org.example.java_practice.util.exception.UnModifiableException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

public interface GenericService<T extends AbstractLockAuditableEntity<I>, I extends Serializable> {

    /**
     * sub-class must implement this method to get exactly {@link CrudRepository} instance for
     * specific DAO.<br/> Sample:<br/>
     * <code>
     * &commat;Service public class UserServiceImpl extends GenericService&lt;User, Long&gt;
     * implements UserService { &commat;Autowired private UserDao userDao; public
     * CrudRepository&lt;User, Long&gt; getDao() { return this.userDao; } }
     * </code>
     *
     * @return {@link BaseRepository} instance.
     */
    BaseRepository<T, I> getDao();

    /**
     * Get an active entity by given entity id.(delFlag = false)
     *
     * @param id {@link Serializable} is @@Id field from Entity
     * @throws ServiceException if any issue cause by persistence.
     */
    T get(I id) throws ServiceException;

    /**
     * Get entity by given entity list of ids.
     *
     * @param ids List of {@link Serializable} is @@Id field from Entity
     * @throws ServiceException if any issue cause by persistence.
     */
    List<T> get(List<I> ids) throws ServiceException;

    /**
     * Get activated entity by given entity id. if only if entity is instance of
     * This method is used for optimistic lock
     *
     * @param id {@link Serializable} is @@Id field from Entity
     * @throws ServiceException if any issue cause by persistence.
     */
    T getForOptimisticLock(I id) throws ServiceException;

    /**
     * Get all entity.
     *
     * @return list of all entity.
     * @throws ServiceException if any issue cause by persistence.
     */
    List<T> getAll() throws ServiceException;

    /**
     * Delete entity by given entity. Checking entity is modifiable or not.
     *
     * @throws ServiceException      if any issue cause by persistence.
     */
    void delete(T entity) throws UnModifiableException, ServiceException;

    void deleteAll(List<T> entities) throws ServiceException;

    /**
     * Bulk insert entities.
     *
     * @return list of entities after saving
     * @throws ServiceException if there is any errors when persisting DB
     */
    List<T> bulkSave(List<T> entities) throws ServiceException;

    /**
     * Save an entity. In case of updating, we won't check optimistic lock.
     *
     * @throws ServiceException ServiceException if any issue cause by persistence.
     */
    T save(T entity);

    /**
     * Update an entity with Optimistic Lock (using a version holder to check).
     *
     * @return entity after save
     */
    T updateWithOptimisticLock(T entity);

    /**
     * create or update an entity. in case of update, optimistic lock will be checked.
     *
     * @param requestUpdateCount {@link Integer}
     * @return entity after save
     */
    T saveWithOptimisticLock(T entity, Integer requestUpdateCount);

    /**
     * 排他制御チェック.
     *
     * @param updateCount {@link Integer}
     */
    void checkOptimisticLock(T entity, Integer updateCount);
}
