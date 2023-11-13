package com.poly.elnr.utils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Discount;
import com.poly.elnr.service.DiscountService;

@Component
public class DiscountCheck {

	@Autowired
	DiscountService discountService;
	


	public boolean isDateInRangeAndAllProducts(Date dateToCheck) {
		List<Discount> configurations = discountService.findAll();

		for (Discount config : configurations) {
			if (config.isActive() && isDateInRange(dateToCheck, config) && config.isAllproduct()) {
				return true;
			}
		}

		return false;
	}

	private boolean isDateInRange(Date dateToCheck, Discount config) {
		Date startDate = config.getStartdate();
		Date endDate = config.getEnddate();
		return !dateToCheck.before(startDate) && !dateToCheck.after(endDate);
	}
	public double getPercentSale(Date dateToCheck) {
        List<Discount> configurations = discountService.findAll();

        for (Discount config : configurations) {
            if (config.isActive() && isDateInRange(dateToCheck, config)) {
                return config.getDiscount();
            }
        }

        return 0.0;
    }
	
	public boolean isDateInRangeAndProductIdInConfig(String productId, Date dateToCheck) {
        List<Discount> configurations = discountService.findAll();

        for (Discount config : configurations) {
            if (config.isActive() && isDateInRange(dateToCheck, config) && config.getProduct_id().contains(productId)) {
                return true;
            }
        }

        return false;
    }
}
