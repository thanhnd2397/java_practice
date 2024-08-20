package org.example.java_practice.dao;

import org.example.java_practice.model.entity.Authority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityDao extends BaseRepository<Authority, Integer>{

    @Query("""
            select a.authorityName from User u left join UserRole ur
            on u.id = ur.user.id and ur.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE
            left join Role r on ur.role.id = r.id and r.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE
            left join RoleAuthority ra on r.id = ra.role.id and ra.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE
            left join Authority a on ra.authority.id = a.id and a.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE
            where u.id = :userId and u.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE
            """)
    List<String> getFunctionByUserId(@Param("userId") Integer userId);
    
}
