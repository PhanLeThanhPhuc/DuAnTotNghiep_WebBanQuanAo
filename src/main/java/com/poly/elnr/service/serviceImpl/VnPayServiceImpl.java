package com.poly.elnr.service.serviceImpl;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.poly.elnr.config.VNPayConfig;
import com.poly.elnr.constant.VnPayConstant;
import com.poly.elnr.entity.Order;
import com.poly.elnr.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


@Service
public class VnPayServiceImpl implements VnPayService {


	@Override
	public String createOrder(int total, String orderInfor, String urlReturn, int orderId) {

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConstant.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConstant.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VnPayConstant.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total*100));
        vnp_Params.put("vnp_CurrCode", "VND");
        
        vnp_Params.put("vnp_TxnRef", String.valueOf(orderId));
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", VnPayConstant.orderType);
//        vnp_Params.put("vnp_OrderId", String.valueOf(orderId));
       
        vnp_Params.put("vnp_Locale", VnPayConstant.locate);

//        urlReturn += VnPayConstant.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", VnPayConstant.vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {

                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VnPayConstant.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConstant.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
	}

	@Override
	public int orderReturn(HttpServletRequest request) {
		Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
	}

    @Override
    public String refundVnPay(Order order) throws IOException {

        String vnp_RequestId = VNPayConfig.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "refund";
        String vnp_TmnCode = VnPayConstant.vnp_TmnCode;
        String vnp_TransactionType = "02";
        String vnp_TxnRef = String.valueOf(order.getId());
        long amount = (long) ((order.getTotal() - order.getTotalDiscount() + order.getShipFee()) * 100);
        String vnp_Amount = String.valueOf(amount);
        String vnp_OrderInfo = "Hoan tien GD OrderId:" + vnp_TxnRef;
        String vnp_TransactionNo = ""; // Assuming value of the parameter "vnp_TransactionNo" does not exist on your system.
        String vnp_TransactionDate = order.getPaymentTime();
        String vnp_CreateBy = order.getPhone();
        String vnp_IpAddr = "127.0.0.1";

        JsonObject vnp_Params = new JsonObject();

        // Add properties to vnp_Params
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TransactionType", vnp_TransactionType);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_Amount", vnp_Amount);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);

        if (vnp_TransactionNo != null && !vnp_TransactionNo.isEmpty()) {
            vnp_Params.addProperty("vnp_TransactionNo", "{get value of vnp_TransactionNo}");
        }

        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        vnp_Params.addProperty("vnp_CreateBy", vnp_CreateBy);

        // Generate vnp_CreateDate and add it to vnp_Params
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);

        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode,
                vnp_TransactionType, vnp_TxnRef, vnp_Amount, vnp_TransactionNo, vnp_TransactionDate,
                vnp_CreateBy, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VnPayConstant.vnp_HashSecret, hash_Data.toString());

        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);

        URL url = new URL("https://sandbox.vnpayment.vn/merchant_webapi/api/transaction");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("C" +
                ".ontent-Type", "application/json");
        con.setDoOutput(true);
        // Send the vnp_Params JSON object to the API
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(vnp_Params.toString());
            wr.flush();
        }

        // Handle the response from the VNPay API (responseCode and response data)
        int responseCode = con.getResponseCode();
        System.out.println("Sending 'POST' request to URL: " + url);
        System.out.println("Post Data: " + vnp_Params);
        System.out.println("Response Code: " + responseCode);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            System.out.println(response.toString());

            com.google.gson.JsonObject jsonResponse = new JsonParser().parse(response.toString()).getAsJsonObject();
            String resultCode = jsonResponse.get("vnp_ResponseCode").getAsString();
            String paymentTime = jsonResponse.get("vnp_PayDate").getAsString();
            System.out.println("THỜI GIAN GĐ HOÀN TIỀN: "+paymentTime);
            if ("00".equals(resultCode)) {
                System.out.println("COde return: "+resultCode);
                System.out.println("Hoàn tiền thành công");
                return paymentTime;
            } else {
                System.out.println("Hoàn tiền thất bại");
                return paymentTime;
            }
        }
    }
}
