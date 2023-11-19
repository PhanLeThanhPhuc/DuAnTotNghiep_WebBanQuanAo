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
import com.poly.elnr.service.SizeService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/sizes")
public class SizeRestController {
	@Autowired
	SizeService sizeService;
	
	@GetMapping
	public List<Size> getAll(){
		return sizeService.findAll();
	}
	
	@GetMapping("{id}")
	public Size getOne(@PathVariable("id") Integer id) {
		return sizeService.findById(id);
	}
	
	@PostMapping
	public Size post(@RequestBody Size size) {
		sizeService.create(size);
		return size;
	}
	@PutMapping("{id}")
	public Size put(@PathVariable("id") Integer id, @RequestBody Size size) {
		return sizeService.update(size);
	}
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Integer id) {
		sizeService.delete(id);
	}
}
