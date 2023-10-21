package com.poly.elnr.service.serviceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.ImageProduct;
import com.poly.elnr.repository.ImageRepository;
import com.poly.elnr.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
	@Autowired
	ImageRepository imageRepository;
	@Override
	public List<ImageProduct> findByProductID(Integer id) {
		// TODO Auto-generated method stub
		return imageRepository.findByProductID(id);
	}

}
