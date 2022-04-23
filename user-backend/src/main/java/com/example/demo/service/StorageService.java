package com.example.demo.service;

import com.example.demo.model.request.UploadForm;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void createFolder(String path);

    String save(int id, MultipartFile file);

    byte[] readFile(int id, String fileName);
}
