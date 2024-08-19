package org.example.java_practice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, I extends Serializable> extends JpaRepository<T, I> {
    @Query("SELECT t FROM #{#entityName} t WHERE t.id = :id and t.delFlg = false")
    Optional<T> findById(@Param("id") I id);
}
