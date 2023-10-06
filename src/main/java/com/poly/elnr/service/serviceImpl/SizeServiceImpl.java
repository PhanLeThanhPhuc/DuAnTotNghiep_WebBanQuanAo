package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Size;
import com.poly.elnr.repository.SizeRepository;
import com.poly.elnr.service.SizeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

	private final SizeRepository sizeRepository;
	
	@Override
	public List<Size> findAllSizeStatusTrue() {
		return sizeRepository.findByStatusTrue();
	}

}
