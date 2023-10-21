package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.ImageProduct;

public interface ImageService {

	List<ImageProduct> findByProductID(Integer id);

}
