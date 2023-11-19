package com.poly.elnr.service.serviceImpl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Description;
import com.poly.elnr.repository.DescriptionRepository;
import com.poly.elnr.service.DescriptionService;

@Service
public class DescriptionServiceImpl implements DescriptionService{
	
	@Autowired
	DescriptionRepository descriptionRepository;
	
	@Override
	public List<Description> findAll() {
		// TODO Auto-generated method stub
		return descriptionRepository.findAll();
	}

	@Override
	public Description findById(Integer id) {
		// TODO Auto-generated method stub
		return descriptionRepository.findById(id).get();
	}

	@Override
	public void create(Description description) {
		// TODO Auto-generated method stub
		descriptionRepository.save(description);
	}

	@Override
	public Description update(Description description) {
		// TODO Auto-generated method stub
		return descriptionRepository.save(description);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		descriptionRepository.deleteById(id);
		
	}

	

}
