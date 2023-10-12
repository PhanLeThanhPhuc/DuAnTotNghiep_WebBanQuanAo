package com.poly.elnr.service.serviceImpl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
	public Page<Product> findProductByCategoryDetailFilter(int idCategoryDetail,
														   List<Integer> colorId,
														   List<Integer> sizeId,
														   Optional<String> sort,
														   Optional<Integer> p) {
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
		Pageable pageable = PageRequest.of(p.orElse(0), 3, s);
		return  productRepository.findProductByCategoryDetailFilter(idCategoryDetail, listColorId, listSizeId, pageable);
	}

}
