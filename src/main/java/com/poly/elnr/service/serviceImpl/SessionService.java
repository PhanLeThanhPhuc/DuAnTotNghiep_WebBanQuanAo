package com.poly.elnr.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import jakarta.servlet.http.HttpSession;

@Service("sessionService")
public class SessionService {

	@Autowired
	HttpSession session;

	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T) session.getAttribute(name);
	}

	public <T> T get(String name, T defaulValue) {
		T value = get(name);
		return value != null ? value : defaulValue;
	}

	// thay đổi hoặc tạo mới attribute trong session
	public void set(String name, Object value) {
		session.setAttribute(name, value);
	}

	// xoá attribute trong session
	public void remove(String name) {
		session.removeAttribute(name);
	}

//	public void saveLoggedInUser(HttpServletRequest request, UserInfoUserDetails userDetails) {
//		HttpSession session = request.getSession();
//	    session.setAttribute("loggedInUser", userDetails);
//	}
}
