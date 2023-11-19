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

	public List<Size> findAll() {
		// TODO Auto-generated method stub
		return sizeRepository.findAll();
	}

	@Override
	public Size findById(Integer id) {
		// TODO Auto-generated method stub
		return sizeRepository.findById(id).get();
	}

	@Override
	public void create(Size size) {
		// TODO Auto-generated method stub
		sizeRepository.save(size);
		
	}

	@Override
	public Size update(Size size) {
		// TODO Auto-generated method stub
		return sizeRepository.save(size);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		sizeRepository.deleteById(id);
		
	}

}
