package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserRequest;
import com.example.demo.model.request.UpdatePasswordRequest;
import com.example.demo.model.request.UpdateUserRequest;
import com.example.demo.model.request.UploadForm;
import com.example.demo.service.StorageService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final StorageService storageService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUser() {
        List<UserDto> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUser(@RequestParam(required = false, value = "") String name) {
        List<UserDto> users = userService.searchUser(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        UserDto userDto = userService.createUser(request);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request, @PathVariable int id) {
        UserDto userDto = userService.updateUser(request, id);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("users/{id}/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request, @PathVariable int id) {
        userService.updatePassword(request, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/{id}/forgot-password")
    public ResponseEntity<?> forgotPassword(@PathVariable int id) {
        String newPassword = userService.forgotPassword(id);
        return ResponseEntity.ok(newPassword);
    }

    @PostMapping("/users/{id}/upload-file")
    public ResponseEntity<?> uploadFile(@PathVariable int id, @ModelAttribute("file") MultipartFile file) {
        System.out.println(file);
        String filePath = storageService.save(id, file);
        return ResponseEntity.ok(filePath);
    }

    @GetMapping("/users/{id}/files/{fileName}")
    public ResponseEntity<?> readFile(@PathVariable int id, @PathVariable String fileName) {
        byte[] bytes = storageService.readFile(id, fileName);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(bytes);
    }
}
