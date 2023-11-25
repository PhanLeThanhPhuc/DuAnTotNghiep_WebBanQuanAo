package com.poly.elnr.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class TwilioUtils {


    public void sendSms (String otp, String phone) {

        String ACCOUNT_SID = "AC7f4ee91be493dfb4e57b9bfab38d5f28";
        String AUTH_TOKEN = "cbe3b798c173d832fc482c3b875fcbd6";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        phone = removeLeadingZero(phone);

        Message message = Message
                .creator(
                        new PhoneNumber("+84"+phone),
                        new PhoneNumber("+19165896827"),
                        otp
                )
                .create();
    }
    private String removeLeadingZero(String phone) {
        if (phone.startsWith("0")) {
            return phone.substring(1);
        }
        return phone;
    }
}
