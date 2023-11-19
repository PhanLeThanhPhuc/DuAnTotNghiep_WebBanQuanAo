package com.poly.elnr.utils;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class UploadCloudinaryUtils {

    private final Cloudinary cloudinary;

    @Autowired
    public UploadCloudinaryUtils(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFileCloudinary(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }

}
