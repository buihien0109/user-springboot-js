package com.example.demo.service.impl;

import com.example.demo.exception.BadRequest;
import com.example.demo.model.User;
import com.example.demo.service.StorageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private UserService userService;

    // file:///Users/buihien/Documents/techmaster-training/class/java-7/demo/user/user-backend/uploads
    // Đường dẫn gốc đên thư mục uploads
    private final Path UPLOAD_DIR = Paths.get("uploads");

    public StorageServiceImpl() {
        createFolder(UPLOAD_DIR.toString());
    }

    @Override
    public void createFolder(String path) {
        // Tạo thư mục với path được chỉ định
        // Nếu có thư mục rồi thì thôi
        // Nếu không có thì tạo thư mục mới
        File uploadDir = new File(path);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    public String save(int userId, MultipartFile file) {
        // Lấy ra tên của fileUpload
        String filename = file.getOriginalFilename();

        // Check tên file
        if (filename == null || filename.length() == 0) {
            throw new BadRequest("Tên file không được để trống");
        }

        // TODO : Kiểm tra đuôi file

        // TODO : Kiểm tra dung lượng file

        try {
            // Create thư mục lưu trữ cho từng user theo user_id
            File dirOfUser = new File(UPLOAD_DIR + "/" + userId);
            createFolder(dirOfUser.toString());

            File serverFile = new File(dirOfUser.toString() + "/" + filename);

            // Sử dụng Buffer để lưu dữ liệu từ file
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

            // Copy các byte data từ file -> fileServer
            stream.write(file.getBytes());
            stream.close();

            String filePath = "/api/v1/users/" + userId + "/files/" + filename;
            // Lấy user tương ứng với id
            User user = userService.getUserById(userId);
            // Set avatar cho user
            user.setAvatar(filePath);

            return filePath;
        } catch (Exception ex) {
            throw new BadRequest("Lỗi khi upload file");
        }
    }

    @Override
    public byte[] readFile(int userId, String fileName) {
        // Lấy đường dẫn file tương ứng với user_id
        Path userPath = UPLOAD_DIR.resolve(String.valueOf(userId));

        // Kiểm tra đường dẫn file có tồn tại hay không
        if (!Files.exists(userPath)) {
            throw new RuntimeException("Không thể đọc file : " + fileName);
        }

        try {
            // Lấy đường dẫn file tương ứng với user_id và file_name
            Path file = userPath.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            } else {
                throw new RuntimeException("Không thể đọc file: " + fileName);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Không thể đọc file : " + fileName);
        }
    }
}
