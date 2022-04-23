package com.example.demo.model.mapper;

import com.example.demo.model.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserRequest;

import java.util.Random;

public class UserMapper {
    public static UserDto toUserDto(User u) {
        UserDto userDto = new UserDto();
        userDto.setId(u.getId());
        userDto.setFullName(u.getFullName());
        userDto.setPhone(u.getPhone());
        userDto.setEmail(u.getEmail());
        userDto.setAvatar(u.getAvatar());
        userDto.setAddress(u.getAddress());

        return userDto;
    }

    public static User toUser(CreateUserRequest request) {
        Random rd = new Random();
        User user = new User();
        user.setId(rd.nextInt(1000));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setPassword(request.getPassword());

        return user;
    }
}
