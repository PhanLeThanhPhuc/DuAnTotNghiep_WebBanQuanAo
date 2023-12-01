package com.poly.elnr.service.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Product;
import com.poly.elnr.entity.ProductDetails;
import com.poly.elnr.service.DiscountCheckService;
import com.poly.elnr.service.ProductDetailService;
import com.poly.elnr.service.ProductService;
import com.poly.elnr.utils.DiscountCheck;

@Service
public class DiscountCheckServiceImpl implements DiscountCheckService {

	@Autowired
	private DiscountCheck discountCheck;

	@Autowired
	private ProductService productRepository;
	@Autowired
	ProductDetailService detailService;

	@Override
	public List<Product> getAllDiscountProducts(List<Product> productList) {

		for (Product product : productList) {
			Date currentDate = new Date();

			if (discountCheck.isDateInRangeAndAllProducts(currentDate)
					|| discountCheck.isDateInRangeAndProductIdInConfig(String.valueOf(product.getId()), currentDate)) {
				applyDiscount(product);
			}
		}

		return productList;
	}
	
	public List<Product> getDiscountProducts2(List<Product> productList) {
		Date currentDate = new Date();
		List<Product> discountedProducts = productList.stream()
				.filter(product -> discountCheck.isDateInRangeAndAllProducts(currentDate) || discountCheck
						.isDateInRangeAndProductIdInConfig(String.valueOf(product.getId()), currentDate))
				.map(product -> {
					applyDiscount(product);
					return product;
				}).collect(Collectors.toList());

		return discountedProducts;
	}

	@Override
	public Product DiscountProduct(Product product) {
		Date currentDate = new Date();

		if (discountCheck.isDateInRangeAndAllProducts(currentDate)
				|| discountCheck.isDateInRangeAndProductIdInConfig(String.valueOf(product.getId()), currentDate)) {
			applyDiscount(product);
		}
		return product;
	}
	
	@Override
	public boolean isDiscountProduct(Product product) {
		Date currentDate = new Date();

		if (discountCheck.isDateInRangeAndAllProducts(currentDate)
				|| discountCheck.isDateInRangeAndProductIdInConfig(String.valueOf(product.getId()), currentDate)) {
			return true;
		}
		return false;
		
	}
	@Override
	public List<Product> getDiscountedProducts() {
		List<Product> allProducts = productRepository.findAll();

		Date currentDate = new Date();
		List<Product> discountedProducts = allProducts.stream()
				.filter(product -> discountCheck.isDateInRangeAndAllProducts(currentDate) || discountCheck
						.isDateInRangeAndProductIdInConfig(String.valueOf(product.getId()), currentDate))
				.map(product -> {
					applyDiscount(product);
					return product;
				}).collect(Collectors.toList());

		return discountedProducts;
	}
	
	 public ProductDetails getProductDetails(ProductDetails aaa) {
		 	Date currentDate = new Date();
		 		     
	        if (discountCheck.isDateInRangeAndAllProducts(currentDate)
					|| discountCheck.isDateInRangeAndProductIdInConfig(String.valueOf(aaa.getProduct().getId()), currentDate)) {
				applyDiscount(aaa.getProduct());
			}

	        return aaa;
	    }

	private void applyDiscount(Product product) {
		double originalPrice = product.getPrice();
		double percentSale = discountCheck.getPercentSale(new Date());
		double originalDiscountPrice=100-((product.getDiscountPrice()/originalPrice)*100);
		if(originalDiscountPrice<percentSale || product.isSale()==false) {
			product.setSale(true);
			double discountedPrice = originalPrice - (originalPrice * percentSale / 100);
			product.setDiscountPrice(discountedPrice);
		}
		
		
	}

}
