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

}
