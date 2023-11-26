package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.ImageProduct;

public interface ImageService {

	List<ImageProduct> findByProductID(Integer id);

	List<ImageProduct> findAll();

	ImageProduct findById(Integer id);

	List<ImageProduct> create(List<ImageProduct> images);

//	void create (ImageService imageService);

	ImageProduct update(ImageProduct image);

	void delete(Integer id);

	List<ImageProduct> findByProductId(Integer id);


}
