package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Discount;
import com.poly.elnr.repository.DiscountRepository;
import com.poly.elnr.service.DiscountService;

@Service
public class DiscountServiceImpl implements DiscountService{
	
	@Autowired
	DiscountRepository discountRepository;
	@Override
	public List<Discount> findAll() {
		// TODO Auto-generated method stub
		return discountRepository.findAll();
	}

	@Override
	public void create(Discount discount) {
		// TODO Auto-generated method stub
		discountRepository.save(discount);
		
	}

	@Override
	public Discount update(Discount discount) {
		// TODO Auto-generated method stub
		return discountRepository.save(discount);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		discountRepository.deleteById(id);;
		
	}

	@Override
	public Discount findById(Integer id) {
		// TODO Auto-generated method stub
		return discountRepository.findById(id).get();
	}

}
