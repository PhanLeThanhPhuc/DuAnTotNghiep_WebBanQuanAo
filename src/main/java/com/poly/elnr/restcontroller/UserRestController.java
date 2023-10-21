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

import com.poly.elnr.entity.Users;
import com.poly.elnr.service.UserService;



@CrossOrigin("*")
@RestController
@RequestMapping("/rest/users")
public class UserRestController {
	@Autowired
	UserService userService;
	
	@GetMapping
	public List<Users> findAll() {
		return userService.findAll();
	}
	
	@PostMapping
	public Users post(@RequestBody  Users user) {
		return userService.create(user);
		 
	}
	
	@PutMapping("{id}")
	public Users put(@PathVariable("id") Integer id, @RequestBody Users user) {
		return userService.update(user);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Users id) {
		userService.delete(id);
	}
	
}
