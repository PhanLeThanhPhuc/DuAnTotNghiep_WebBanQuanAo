package com.poly.elnr.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Product;
import com.poly.elnr.repository.ColorRepository;
import com.poly.elnr.repository.ProductRepository;
import com.poly.elnr.repository.SizeRepository;
import com.poly.elnr.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	SizeRepository sizeRepository;

	@Autowired
	ColorRepository colorRepository;

	@Autowired
	ProductRepository productRepository;

	@Override
	public Page<Product> findProductByCategoryDetailFilter(String name,
														   List<Integer> colorId,
														   List<Integer> sizeId,
														   Optional<String> sort,
														   Optional<Integer> p) {
		Sort s;
		if(sort.isEmpty() || sort.get().equals("price-asc") ){
			s = Sort.by(Sort.Direction.ASC, "price");
		}else if(sort.get().equals("price-desc")){
			s = Sort.by(Sort.Direction.DESC, "price");
		}else if(sort.get().equals("name-az")){
			s = Sort.by(Sort.Direction.ASC, "name");
		}else {
			s = Sort.by(Sort.Direction.DESC, "name");
		}

		Pageable pageable = PageRequest.of(p.orElse(0), 2, s);
		List<Integer> listColorId = colorId == null || colorId.isEmpty() ? colorRepository.findAllColorId() : colorId;
		List<Integer> listSizeId = sizeId == null || sizeId.isEmpty() ? sizeRepository.findAllSizeId() : sizeId;
		return productRepository.findProductByCategoryDetailFilter(name, listColorId, listSizeId, pageable);
	}

	@Override
	public Page<Product> findProductByCategoryDetail(String name, Optional<Integer> p) {
		Pageable pageable = PageRequest.of(p.orElse(0), 8);
		return productRepository.findProductByCategoryDetail(name, pageable);
	}

}
