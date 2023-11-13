package com.poly.elnr.service.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Product;
import com.poly.elnr.service.DiscountCheckService;
import com.poly.elnr.service.ProductService;
import com.poly.elnr.utils.DiscountCheck;

@Service
public class DiscountCheckServiceImpl implements DiscountCheckService{

    @Autowired
    private DiscountCheck discountCheck;

    @Autowired
    private ProductService productRepository;
    
	@Override
	public List<Product> getAllDiscountProducts(List<Product> productList) {

        for (Product product : productList) {
            Date currentDate = new Date();

            if (discountCheck.isDateInRangeAndAllProducts(currentDate) ||
            		discountCheck.isDateInRangeAndProductIdInConfig(String.valueOf(product.getId()), currentDate)) {
                // Apply discount based on percent sale
                applyDiscount(product);
            }
        }

        return productList;
	}

	@Override
	public Optional<Product> getDiscountProductById(int id) {
		Optional<Product> product = productRepository.findByID2(id);

        product.ifPresent(p -> {
            Date currentDate = new Date();

            if (discountCheck.isDateInRangeAndAllProducts(currentDate) ||
            		discountCheck.isDateInRangeAndProductIdInConfig(String.valueOf(p.getId()), currentDate)) {
                // Apply discount based on percent sale
                applyDiscount(p);
            }
        });

        return product;
	}

	@Override
	public Product saveDiscountProduct(Product product) {
		Date currentDate = new Date();

        if (discountCheck.isDateInRangeAndAllProducts(currentDate) ||
        		discountCheck.isDateInRangeAndProductIdInConfig(String.valueOf(product.getId()), currentDate)) {
            // Apply discount based on percent sale
            applyDiscount(product);
        }

     // Return the original product if no discount is applied

        return product;
	}
	
	 private void applyDiscount(Product product) {
	        double originalPrice = product.getPrice();
	        double percentSale = discountCheck.getPercentSale(new Date());
	        product.setSale(true);
	        double discountedPrice = originalPrice - (originalPrice * percentSale / 100);
	        product.setDiscountPrice(discountedPrice);
	    }

}
