package com.poly.elnr.restcontroller;

import com.poly.elnr.entity.Address;
import com.poly.elnr.entity.Users;
import com.poly.elnr.repository.AddressRepository;
import com.poly.elnr.repository.UserRepository;
import com.poly.elnr.service.AddressService;
import com.poly.elnr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    UserService userService;

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
    public ResponseEntity<?> insertAddress(@RequestBody Address address, Authentication authentication){
        if(address.getUser() ==  null){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Users users = userService.findByUserNamePhoneAndEmail(userDetails.getUsername());
            address.setUser(new Users(users.getId()));
        }
        return ResponseEntity.ok(addressService.insertAddress(address));
    }

}
