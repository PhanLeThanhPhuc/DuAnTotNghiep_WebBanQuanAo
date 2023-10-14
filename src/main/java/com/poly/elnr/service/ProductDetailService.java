package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;

public interface ProductDetailService {

	List<ProductDetails> findByProductID(Integer id);

	ProductDetails findProductDetial(Integer id, Integer sizeid);




}
