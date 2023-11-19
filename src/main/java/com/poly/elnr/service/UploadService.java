package com.poly.elnr.service;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	File save(MultipartFile file, String folder);

	String saveImageUser(MultipartFile multipartFile)  throws IOException;

	
}