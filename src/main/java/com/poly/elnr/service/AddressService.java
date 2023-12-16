package com.poly.elnr.service;

import com.poly.elnr.entity.Address;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressService {

    List<Address> findAddressByIdUser(String usename);

    Address insertAddress(Address address);

    void delete(Address id);
}
