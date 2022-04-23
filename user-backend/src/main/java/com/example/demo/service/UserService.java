package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserRequest;
import com.example.demo.model.request.UpdatePasswordRequest;
import com.example.demo.model.request.UpdateUserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void init();

    List<UserDto> getUsers();

    List<UserDto> searchUser(String name);

    User getUserById(int id);

    UserDto createUser(CreateUserRequest request);

    UserDto updateUser(UpdateUserRequest request, int id);

    void deleteUser(int id);

    void updatePassword(UpdatePasswordRequest request, int id);

    String forgotPassword(int id);

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);
}
