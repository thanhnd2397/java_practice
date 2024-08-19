package org.example.java_practice.service;

import org.example.java_practice.model.entity.User;

import java.util.List;

public interface UserService extends GenericService<User, Integer> {

    List<UserResponse> getAllUsersSync();

    void updateUser(Integer userId, UserRequest userRequest);

    UserResponse getUser(Integer userId);

    User getUserByName(String email);
}
