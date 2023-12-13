package com.poly.elnr.restcontroller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.elnr.entity.Users;
import com.poly.elnr.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/users")
public class UserRestController {
	@Autowired
	UserService userService;
	
	@PostMapping
	public Users post(@RequestBody  Users user) {
		return userService.create(user);
		 
	}

	@GetMapping("/userid")
	public Map<String, Object> findIdUser(Authentication authentication){
		Map<String, Object> model = new HashMap<>();
		if(authentication == null){
			System.out.println("Chua login");
			model.put("statusLogin", false);
			return model;
		}else{
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			model.put("statusLogin", true);
			model.put("user", userService.findByUserNamePhoneAndEmail(userDetails.getUsername()));
			return model;
		}
	}

	
	@PutMapping("{id}")
	public Users put(@PathVariable("id") Integer id, @RequestBody Users user) {
		return userService.update(user);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Users id) {
		userService.delete(id);
	}
	@GetMapping
	public List<Users> getAccounts(@RequestParam("admin") Optional<Boolean> admin) {
		if(admin.orElse(false)) {
			return userService.findAllUserByIdRole();
		}
		return userService.findAll();
	}
	
	@GetMapping("{id}")
	public Users getOne(@PathVariable("id") Integer id) {
		return userService.findById(id);
	}

	@GetMapping("register-phone")
	public ResponseEntity<?> registerPhoneNumber (@RequestParam("phone") String phone, @RequestParam("email") String email){
		Map<String, Object> response = new HashMap<>();
		Users user = userService.findByUserNamePhoneAndEmail(phone);
		if(user != null){
			response.put("message", "Số điện thoại đã được đăng ký!");
			response.put("data", null);
			return ResponseEntity.ok(response);
		}else{
			response.put("message", "Số chưa đăng ky");
			response.put("data", userService.registerPhoneNumber(phone,email));
			return ResponseEntity.ok(response);
		}
	}

	@GetMapping("confirm-otp")
	public ResponseEntity<?> conFirmOtp (@RequestParam("otp") String otp, @RequestParam("email") String email, @RequestParam("phone") String phone){
		Map<String, Object> response = new HashMap<>();
		Users user = userService.findByUserNamePhoneAndEmail(email);
		boolean statusOtp = userService.checkOtp(otp, email, phone);
		if(calculateRemainingTime(user) == 0){
			response.put("message", "Hết thời gian nhập mã!");
			response.put("status", "timeout");
			return ResponseEntity.ok(response);
		}else{
			if(statusOtp){
				response.put("message", "Đăng ký số điện thoại thành công!");
				response.put("status", true);
				return ResponseEntity.ok(response);
			}else{
				response.put("message", "Mã otp không đúng, vui lòng nhập lại");
				response.put("status", false);
				return ResponseEntity.ok(response);
			}
		}
	}

	@GetMapping("realtime-otp")
	public ResponseEntity<?> realTimeOtp (@RequestParam("email") String email){
		Map<String, Object> response = new HashMap<>();
		Users user = userService.findByUserNamePhoneAndEmail(email);
		long remainingTime = calculateRemainingTime(user);
		if(remainingTime == 0){
			response.put("message", "Hết thời gian nhập mã!");
			response.put("remainingTime", remainingTime);
			return ResponseEntity.ok(response);
		}else{
			response.put("message", "Thời gian còn lại");
			response.put("remainingTime", remainingTime);
			return ResponseEntity.ok(response);
		}
	}

	private long calculateRemainingTime(Users user) {
		if (user.isPasswordReset()) {
			long currentTime = System.currentTimeMillis();
			Date otpCreationTime = user.getTimeOtp();

			long elapsedTime = (currentTime - otpCreationTime.getTime()) / 1000;
			long remainingTime = Math.max(0, 60 - elapsedTime);

			return remainingTime;
		} else {
			return 0;
		}
	}

}
