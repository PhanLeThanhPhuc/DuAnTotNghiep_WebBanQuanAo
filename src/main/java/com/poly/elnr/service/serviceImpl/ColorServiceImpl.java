package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Color;
import com.poly.elnr.repository.ColorRepository;
import com.poly.elnr.service.ColorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService{

	private final ColorRepository colorRepository;
	
	@Override
	public List<Color> findAllColor() {
		return colorRepository.findAll();
	}

}
