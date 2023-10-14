package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.service.ProductService;


@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	ProductRepository daoProductRepository;

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return daoProductRepository.findAll();
	}

	@Override
	public Product findById(Integer id) {
		// TODO Auto-generated method stub
		return daoProductRepository.findById(id).get();
	}

	
	
	
	


}
