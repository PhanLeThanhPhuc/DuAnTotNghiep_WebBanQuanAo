package com.poly.elnr.service;

import java.io.IOException;
import java.util.List;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.poly.elnr.entity.Category;
import com.poly.elnr.entity.Product;

public interface ProductService {
	public List<Product> findAll();

	Product findById(Integer id);
	public Product create(Product product) ;
	public void delete(Integer id) ;

	Page<Product> findProductByCategoryDetailFilter(int idCategoryDetail,
													List<Integer> colorId,
													List<Integer> sizeId,
													Optional<String> sort,
													Optional<Integer> p, String min, String max);
	
	Page<Product> findProductByCategoryFilter(int idCategory,
													List<Integer> colorId,
													List<Integer> sizeId,
													Optional<String> sort,
													Optional<Integer> p, String min, String max);

	public Product update(Product product);

	public String saveImage(MultipartFile multipartFile)throws IOException;
	
	
	Optional<Product> findByID2(Integer id);

	List<Product> findByIdsProduct(int[] idProduct);

	public List<Product> findSale(List<Integer> colorId, List<Integer> sizeId);

	public Page<Product> findProductSearch(List<Integer> colorId, List<Integer> sizeId, Optional<String> sort,
			Optional<Integer> p, String search, String min, String max);

}
