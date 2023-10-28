package com.poly.elnr.service;

import java.io.File;


import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
	public File save(MultipartFile file, String folder) ;
}