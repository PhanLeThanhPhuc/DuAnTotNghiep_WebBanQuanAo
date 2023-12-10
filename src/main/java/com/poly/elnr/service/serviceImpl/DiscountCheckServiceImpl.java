package com.poly.elnr.service.serviceImpl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	public Page<Product> getDiscountProducts2(List<Product> productList, Optional<String> sort, Optional<Integer> p,double min,double max) {
	    Date currentDate = new Date();
	    List<Product> discountedProducts = productList.stream()
	            .filter(product -> discountCheck.isDateInRangeAndAllProducts(currentDate) || discountCheck
	                    .isDateInRangeAndProductIdInConfig(String.valueOf(product.getId()), currentDate))
	            .map(product -> {
	                applyDiscount(product);
	                return product;
	            })
	            
	            .sorted((p1, p2) -> {
	            	 if (sort.isEmpty()) {
	            		 return Double.compare(p1.getDiscountPrice(), p2.getDiscountPrice());
	                 }
	                switch (sort.get()) {
	                    case "price-asc":
	                        return Double.compare(p1.getDiscountPrice(), p2.getDiscountPrice());
	                    case "price-desc":
	                        return Double.compare(p2.getDiscountPrice(), p1.getDiscountPrice());
	                    case "name-az":
	                        return p1.getName().compareToIgnoreCase(p2.getName());
	                    case "name-desc":
	                        return p2.getName().compareToIgnoreCase(p1.getName());
	                    default:
	                    	return Double.compare(p1.getDiscountPrice(), p2.getDiscountPrice());
	                }
	            })
	            .filter(product-> min<=product.getDiscountPrice() && product.getDiscountPrice() <= max)
	            .collect(Collectors.toList());

	    Pageable pageable = PageRequest.of(p.orElse(0), 12);
	    int start = p.orElse(0) * 12;
	    int end = Math.min((start + 12), discountedProducts.size());
	    List<Product> subList = discountedProducts.subList(start, end);

	    return new PageImpl<>(subList, pageable, discountedProducts.size());
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
