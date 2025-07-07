package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;


import java.util.List;

public interface DonHangService {

    // ds hóa đơn
    List<DonHangDto> findOrder();

    List<DonHangChiTietDto> findChiTietByDonHangId(Integer donHangId);
}
