package com.poly.elnr.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.elnr.entity.Size;
import com.poly.elnr.entity.Voucher;
import com.poly.elnr.service.VoucherService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/voucher")
public class VoucherRestController {

	@Autowired
	VoucherService voucherService;
	
	@GetMapping
	public List<Voucher> getAll(){
		return voucherService.findAll();
	}
	
	@GetMapping("{id}")
	public Voucher getOne(@PathVariable("id") Integer id) {
		return voucherService.findById(id);
	}
	
	@PostMapping
	public Voucher post(@RequestBody Voucher voucher) {
		voucherService.create(voucher);
		return voucher;
	}
	@PutMapping("{id}")
	public Voucher put(@PathVariable("id") Integer id, @RequestBody Voucher voucher) {
		return voucherService.update(voucher);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		voucherService.delete(id);
	}

	@GetMapping("/date")
	public List<Voucher> findVoucherDate(){
		return voucherService.findAllVoucherDate();
	}
}
