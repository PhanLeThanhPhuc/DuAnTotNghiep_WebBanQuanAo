package com.poly.elnr.service.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poly.elnr.service.UploadService;
import com.poly.elnr.utils.UploadCloudinaryUtils;

import jakarta.servlet.ServletContext;


@Service
public class UploadServiceImpl implements UploadService{
	@Autowired
	UploadCloudinaryUtils uploadCloudinaryUtils;
	@Override
	public File save(MultipartFile file, String folder) {
		File dir = new File(this.getClass().getResource("/").getPath()+"/static/assets/user/user/" + folder);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		try {
			File saveFile = new File(dir,file.getOriginalFilename());
			file.transferTo(saveFile);
			System.out.println(saveFile);
			return saveFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String saveImageUser(MultipartFile multipartFile) throws IOException {
		String imageURL = uploadCloudinaryUtils.uploadFileCloudinary(multipartFile);
		return imageURL;
	}
	
}