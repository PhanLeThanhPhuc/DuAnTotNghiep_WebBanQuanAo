package com.poly.elnr.restcontroller;

import java.io.File;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poly.elnr.service.ProductService;
import com.poly.elnr.service.UploadService;
import com.poly.elnr.utils.UploadCloudinaryUtils;

import jakarta.websocket.server.PathParam;


@CrossOrigin("*")
@RestController
public class UploadRestController {
	@Autowired
	UploadService uploadService;
	
	@Autowired
	UploadCloudinaryUtils uploadCloudinaryUtils;
	
//	@PostMapping("/rest/upload/{folder}")
//	public JsonNode upload(@PathParam("file") MultipartFile file, @PathVariable("folder") String folder) {
//		File savedFile = uploadService.save(file, folder);
//		ObjectMapper mapper = new ObjectMapper();
//		ObjectNode node = mapper.createObjectNode();
//		node.put("name", savedFile.getName());
//		node.put("size", savedFile.length());
//		return node;
//	}
//	
	@ResponseBody
	@PostMapping("/rest/upload")
	public JsonNode uploadFile(@RequestParam("uploadfile") MultipartFile multipartFile,
							 Model model) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("image", uploadService.saveImageUser(multipartFile));
		return node;
	}
	
	@PostMapping("/rest/uploadmulti")
	public JsonNode uploadFilemulti(@RequestParam("uploadfiles") MultipartFile[] multipartFiles,
	                                Model model) throws IOException {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode node = mapper.createObjectNode();

	    ArrayNode arrayNode = mapper.createArrayNode();

	    for (MultipartFile multipartFile : multipartFiles) {
	        if (!multipartFile.isEmpty()) {
	            String imagePath = uploadService.saveImageUser(multipartFile);
	            arrayNode.add(imagePath);
	        }
	    }

	    node.put("images", arrayNode);
	    return node;
	}
	

}