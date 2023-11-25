package com.poly.elnr.service.serviceImpl;

import com.poly.elnr.entity.Address;
import com.poly.elnr.entity.Order;
import com.poly.elnr.entity.Users;
import com.poly.elnr.repository.AddressRepository;
import com.poly.elnr.repository.UserRepository;
import com.poly.elnr.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.elnr.service.AddressService;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<Address> findAddressByIdUser(String username) {
        Users user = new Users();
        if(RegexUtils.isPhoneNumber(username)) {
            user = userRepository.findByPhone(username);
            return addressRepository.findAddressByIdUser(user.getId());
        }else{
            user = userRepository.findEmail(username);
//            if(user.getPhone() ==  null){
//                return null;
//            }else{
                System.out.println();
                return addressRepository.findAddressByIdUser(user.getId());
//            }
        }
//        return null;
    }

    @Override
    public Address insertAddress(Address address) {
        return addressRepository.save(address);
    }
}
