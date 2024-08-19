package org.example.java_practice.dao;

import org.example.java_practice.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends BaseRepository<User, Integer> {
    @Query("Select u from User u where u.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE")
    List<User> getAllUsers();

    @Query("Select count(u) from User u where u.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE")
    Integer countUsers();

    @Query("Select u from User u where u.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE and u.email = :email")
    Optional<User> getUserByEmail(@Param("email") String email);

    @Query("Select u from User u where u.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE and u.id = :id")
    Optional<User> getUserById(@Param("id") Integer userId);

    @Query("Select u from User u where u.id = :userId and u.refreshToken = :refreshToken "
            + "and u.token = :token and u.delFlg = org.example.java_practice.model.enumeration.DeleteStatus.ACTIVE")
    Optional<User> getUserByUserIdAndRefreshToken(@Param("userId") Integer userId,
                                                  @Param("token") String token, @Param("refreshToken") String refreshToken);

}
