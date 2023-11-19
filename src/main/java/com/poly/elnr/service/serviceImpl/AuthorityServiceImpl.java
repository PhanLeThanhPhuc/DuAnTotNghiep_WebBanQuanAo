package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Authority;
import com.poly.elnr.entity.Users;
import com.poly.elnr.repository.AuthorityRepository;
import com.poly.elnr.repository.UserRepository;
import com.poly.elnr.service.AuthorityService;



@Service
public class AuthorityServiceImpl implements AuthorityService{
	@Autowired
	AuthorityRepository dao;
	@Autowired
	UserRepository userRepository;
	@Override
	public List<Authority> findAuthoritiesOfAdministrators() {
		// TODO Auto-generated method stub
		List<Users> users = userRepository.findAllUserByIdRole();
		return dao.authoritiesOf(users);
	}

	@Override
	public List<Authority> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public Authority create(Authority auth) {
		// TODO Auto-generated method stub
		return  dao.save(auth);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		dao.deleteById(id);
	}
	
	




}
