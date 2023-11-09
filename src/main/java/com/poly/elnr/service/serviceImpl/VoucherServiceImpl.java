package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Voucher;
import com.poly.elnr.repository.VoucherRepository;
import com.poly.elnr.service.VoucherService;

@Service
public class VoucherServiceImpl implements VoucherService{
	@Autowired 
	VoucherRepository voucherRepository;

	public List<Voucher> findAll() {
		// TODO Auto-generated method stub
		return voucherRepository.findAll();
	}

	public Voucher findById(Integer id) {
		// TODO Auto-generated method stub
		return voucherRepository.findById(id).get();
	}

	public void create(Voucher voucher) {
		// TODO Auto-generated method stub
		voucherRepository.save(voucher);
	}

	public Voucher update(Voucher voucher) {
		// TODO Auto-generated method stub
		return voucherRepository.save(voucher);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		voucherRepository.deleteById(id);
		
	}

	

	


}
