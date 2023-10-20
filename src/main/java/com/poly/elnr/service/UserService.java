package com.poly.elnr.service;

import java.util.List;

import com.poly.elnr.entity.Category;
import com.poly.elnr.entity.User;


public interface UserService {

	public List<User> findAll();

	User create(User user);

	public User update(User user);

	public void delete(User id);

}
