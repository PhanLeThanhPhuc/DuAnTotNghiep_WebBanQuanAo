package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;
import com.poly.elnr.repository.ProductDetailsRepository;
import com.poly.elnr.service.ProductDetailService;

@Service
public class ProductDetailServiceImpl implements ProductDetailService{
	@Autowired
	ProductDetailsRepository dao;

	@Override
	public List<ProductDetails> findByProductID(Integer id) {
		// TODO Auto-generated method stub
		return dao.findByProductID(id);
	}

	@Override
	public ProductDetails findProductDetial(Integer id, Integer sizeid) {
		// TODO Auto-generated method stub
		return dao.findProductDetial(id, sizeid);
	}

	@Override
	public ProductDetails create(ProductDetails productdetail) {
		// TODO Auto-generated method stub
		return dao.save(productdetail);
	}

	@Override
	public ProductDetails update(ProductDetails productdetail) {
		// TODO Auto-generated method stub
		return dao.save(productdetail);
	}
	

	


}
