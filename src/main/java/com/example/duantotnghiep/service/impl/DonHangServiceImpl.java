package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.repository.*;
import com.example.duantotnghiep.service.DonHangService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;


    @Override
    public List<DonHangDto> findOrder() {
        return donHangRepository.findOrder();
    }

    @Override
    public List<DonHangChiTietDto> findChiTietByDonHangId(Integer donHangId) {
        return donHangRepository.findOrderDetail(donHangId);
    }

}
