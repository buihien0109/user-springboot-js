package com.example.demo.service.impl;

import com.example.demo.exception.BadRequest;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.CreateUserRequest;
import com.example.demo.model.request.UpdatePasswordRequest;
import com.example.demo.model.request.UpdateUserRequest;
import com.example.demo.service.UserService;
import com.example.demo.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private List<User> users = new ArrayList<>();

    public UserServiceImpl() {
        init();
    }

    @Override
    public void init() {
        users.add(new User(1, "Nguyễn Văn A", "a@gmail.com", "0987654321",
                "Tỉnh Thái Bình", null,
                "111"));
        users.add(new User(2, "Trần văn B", "b@gmail.com", "0988887777",
                "Thành phố Hà Nội", null,
                "222"));
        users.add(new User(3, "Ngô Thị C", "c@gmail.com", "0986665555",
                "Tỉnh Nam Định", null,
                "333"));
    }

    @Override
    public List<UserDto> getUsers() {
        return users
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUser(String name) {
        return users
                .stream()
                .filter(user -> user.getFullName().toLowerCase().contains(name.toLowerCase()))
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(int id) {
        Optional<User> userOptional = findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new NotFoundException("Not found user with id = " + id);
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        // Kiểm tra xem email đã tồn tại hay chưa
        if (findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequest("Email = " + request.getEmail() + " is existed!");
        }

        // Nếu email chưa tồn tại thì tạo user mới
        User newUser = UserMapper.toUser(request);
        users.add(newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto updateUser(UpdateUserRequest request, int id) {
        // Kiểm tra xem email đã tồn tại hay chưa
        if (findById(id).isEmpty()) {
            throw new BadRequest("Id = " + id + " is not exist!");
        }

//        if(findByEmail(request.getEmail()).isPresent()) {
//            throw new BadRequest("Email = " + request.getEmail() + " is existed!");
//        }

        User user = findById(id).get();

        // Đặt lại các thông tin cho user
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());

        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(int id) {
        // Kiểm tra xem id có tồn tại hay không?
        // Nếu không thì báo lỗi
        if (findById(id).isEmpty()) {
            throw new BadRequest("Id = " + id + " is not exist!");
        }

        // Nếu có tồn tại thì xóa
        users.removeIf(u -> u.getId() == id);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request, int id) {
        // Kiểm tra xem id có tồn tại hay không?
        // Nếu không thì báo lỗi
        if (findById(id).isEmpty()) {
            throw new BadRequest("Id = " + id + " is not exist!");
        }

        // Nếu new password = old password thì báo lỗi
        if (request.getNewPassword().equals(request.getOldPassword())) {
            throw new BadRequest("old password and new password cannot be the same!");
        }

        User user = findById(id).get();

        // Nếu old password không trùng với password của user thì báo lỗi
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new BadRequest("old password is incorrecr!");
        }

        // Đặt lại pass mới cho user
        user.setPassword(request.getNewPassword());
    }

    @Override
    public String forgotPassword(int id) {
        // Kiểm tra xem id có tồn tại hay không?
        // Nếu không thì báo lỗi
        if (findById(id).isEmpty()) {
            throw new BadRequest("Id = " + id + " is not exist!");
        }

        // Random chuỗi password mới cho user
        String newPassword = StringUtil.randomString(3);

        // Lấy thông tin của user và đặt lại password mới cho user
        User user = findById(id).get();
        user.setPassword(newPassword);

        // Trả về thông tin password mới
        return newPassword;
    }

    // Helper method -> tìm kiếm user theo id
    @Override
    public Optional<User> findById(int id) {
        return users
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    // Helper method -> tìm kiếm user theo email
    @Override
    public Optional<User> findByEmail(String email) {
        return users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }
}
