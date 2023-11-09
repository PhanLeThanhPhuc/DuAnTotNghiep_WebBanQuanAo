package com.poly.elnr.restcontroller;

import com.poly.elnr.entity.Address;
import com.poly.elnr.repository.AddressRepository;
import com.poly.elnr.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressRestController {

    @Autowired
    AddressService addressService;


    @GetMapping("rest/address")
    public List<Address> findAddressByIdUser(Authentication authentication){
        if(authentication == null){
            return null;
        }else{
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return addressService.findAddressByIdUser(userDetails.getUsername());
        }
    }

    @PostMapping("rest/insert-address")
    public Address insertAddress(@RequestBody Address address){
        System.out.println();
        return addressService.insertAddress(address);
    }

}
