package com.poly.elnr.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.entity.Role;
import com.poly.elnr.repository.RoleRepository;
import com.poly.elnr.service.RoleService;


@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleRepository dao;

	public List<Role> findAll() {
		return dao.findAll();
	}

}
