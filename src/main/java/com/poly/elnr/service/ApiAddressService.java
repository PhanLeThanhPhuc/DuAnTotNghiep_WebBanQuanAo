package com.poly.elnr.service;

public interface ApiAddressService {

    String listProvinces();

    String listDistrict(int province_id);

    String listWard(int district_id);
}
