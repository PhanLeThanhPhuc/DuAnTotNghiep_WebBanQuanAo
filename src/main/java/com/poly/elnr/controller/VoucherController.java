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

}
