package com.poly.elnr.controller;

import com.twilio.Twilio;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.service.annotation.GetExchange;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
@Controller
public class VoucherController {

	@GetMapping("user/voucher")
	public String viewVoucher() {
		return "user/product/voucher";
	}

//	@GetMapping("user/sms")
//	public String viewSms() {
//
//		 String ACCOUNT_SID = "AC7f4ee91be493dfb4e57b9bfab38d5f28";
//		String AUTH_TOKEN = "cbe3b798c173d832fc482c3b875fcbd6";
//
//		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//		Message message = Message
//				.creator(
//						new PhoneNumber("+84937672392"),
//						new PhoneNumber("+19165896827"), "This is the ship that made the Kessel Run in fourteen parsecs?"
//				)
//				.create();
//
//		System.out.println(message.getSid());
//
//		return "user/product/voucher";
//	}

}
