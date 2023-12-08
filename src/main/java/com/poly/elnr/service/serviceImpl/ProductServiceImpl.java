package com.poly.elnr.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poly.elnr.entity.Category;
import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ProductRepository;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ColorRepository;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.repository.SizeRepository;
import com.poly.elnr.service.ProductService;
import com.poly.elnr.utils.UploadCloudinaryUtils;


@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	SizeRepository sizeRepository;

	@Autowired
	ColorRepository colorRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UploadCloudinaryUtils uploadCloudinaryUtils;
	
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	
	public Product findById(Integer id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).get();
	}
	
	
	

	@Override
	public Page<Product> findProductByCategoryDetailFilter(int idCategoryDetail,
														   List<Integer> colorId,
														   List<Integer> sizeId,
														   Optional<String> sort,
														   Optional<Integer> p,
														   String min, String max) {
		Sort s;
		if (sort.isEmpty() || sort.get().equals("price-asc")) {
			s = Sort.by(Sort.Direction.ASC, "price");
		} else if (sort.get().equals("price-desc")) {
			s = Sort.by(Sort.Direction.DESC, "price");
		} else if (sort.get().equals("name-az")) {
			s = Sort.by(Sort.Direction.ASC, "name");
		} else {
			s = Sort.by(Sort.Direction.DESC, "name");
		}
		
		List<Integer> listColorId = colorId == null || colorId.isEmpty() ? colorRepository.findAllColorId() : colorId;
		List<Integer> listSizeId = sizeId == null || sizeId.isEmpty() ? sizeRepository.findAllSizeId() : sizeId;
		Pageable pageable = PageRequest.of(p.orElse(0), 12, s);
		return productRepository.findProductByCategoryDetailFilter(idCategoryDetail, listColorId, listSizeId, pageable,min,max);
	}	  

	@Override
	public Page<Product> findProductByCategoryFilter(int idCategory, 
			   List<Integer> colorId,
			   List<Integer> sizeId,
			   Optional<String> sort,
			   Optional<Integer> p,
			   String min, String max) {
		Sort s;
		if (sort.isEmpty() || sort.get().equals("price-asc")) {
			s = Sort.by(Sort.Direction.ASC, "price");
		} else if (sort.get().equals("price-desc")) {
			s = Sort.by(Sort.Direction.DESC, "price");
		} else if (sort.get().equals("name-az")) {
			s = Sort.by(Sort.Direction.ASC, "name");
		} else {
			s = Sort.by(Sort.Direction.DESC, "name");
		}

		List<Integer> listColorId = colorId == null || colorId.isEmpty() ? colorRepository.findAllColorId() : colorId;
		List<Integer> listSizeId = sizeId == null || sizeId.isEmpty() ? sizeRepository.findAllSizeId() : sizeId;
		Pageable pageable = PageRequest.of(p.orElse(0), 12, s);
		return productRepository.findProductByCategoryFilter(idCategory,  listColorId, listSizeId, pageable,min,max);
	}
	
	@Override
	public Page<Product> findProductSearch(List<Integer> colorId, List<Integer> sizeId, Optional<String> sort,
			Optional<Integer> p, String search,String min, String max) {
		Sort s;
		if (sort.isEmpty() || sort.get().equals("price-asc")) {
			s = Sort.by(Sort.Direction.ASC, "price");
		} else if (sort.get().equals("price-desc")) {
			s = Sort.by(Sort.Direction.DESC, "price");
		} else if (sort.get().equals("name-az")) {
			s = Sort.by(Sort.Direction.ASC, "name");
		} else {
			s = Sort.by(Sort.Direction.DESC, "name");
		}

		List<Integer> listColorId = colorId == null || colorId.isEmpty() ? colorRepository.findAllColorId() : colorId;
		List<Integer> listSizeId = sizeId == null || sizeId.isEmpty() ? sizeRepository.findAllSizeId() : sizeId;
		Pageable pageable = PageRequest.of(p.orElse(0), 12, s);
		return productRepository.findProductSearch(listColorId, listSizeId, pageable, "%"+search+"%",min,max);
	}
	@Override
	public List<Product> findSale(List<Integer> colorId, List<Integer> sizeId) {
		

		List<Integer> listColorId = colorId == null || colorId.isEmpty() ? colorRepository.findAllColorId() : colorId;
		List<Integer> listSizeId = sizeId == null || sizeId.isEmpty() ? sizeRepository.findAllSizeId() : sizeId;
		return productRepository.findProductSale( listColorId, listSizeId);
	}


	@Override
	public Product create(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}


	@Override
	public void delete(Integer id) {
		productRepository.deleteById(id);
		
	}


	@Override
	public Product update(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}


	@Override
	public String saveImage(MultipartFile multipartFile) throws IOException {
		// TODO Auto-generated method stub
		String imageURL = uploadCloudinaryUtils.uploadFileCloudinary(multipartFile);
		return imageURL;
	}


	@Override
	public Optional<Product> findByID2(Integer id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id);
	}

	@Override
	public List<Product> findByIdsProduct(int[] idProduct) {
		return productRepository.findByIdsProduct(idProduct);
	}


	

}
