package com.poly.elnr.service.serviceImpl;


import java.util.ArrayList;
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
	public List<ImageProduct> findAll() {
		// TODO Auto-generated method stub
		return imageRepository.findAll();
	}
	
	public ImageProduct findById(Integer id) {
		// TODO Auto-generated method stub
		return imageRepository.findById(id).get();
	}
	public List<ImageProduct> create(List<ImageProduct> images) {
		List<ImageProduct> imagesListReturn = new ArrayList<>();
		imageRepository.deleteByProductId(images.get(0).getProduct().getId());
		for(ImageProduct imageProduct : images){
			imageRepository.save(imageProduct);
			imagesListReturn.add(imageProduct);
		}
		return imagesListReturn;
	}

	public ImageProduct update(ImageProduct image) {
		// TODO Auto-generated method stub
		return imageRepository.save(image);
	}
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		imageRepository.deleteById(id);
		
	}
	@Override
	public List<ImageProduct> findByProductId(Integer id) {
		// TODO Auto-generated method stub
		return imageRepository.findByProductID(id);
	}

	@Override
	public void deleteByProduct(int idProduct) {
		imageRepository.deleteByProductId(idProduct);
	}

}
