package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.entity.DonHang;
import com.example.duantotnghiep.entity.TrangThaiDonHang;
import com.example.duantotnghiep.repository.*;
import com.example.duantotnghiep.service.DonHangService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;
    private final TrangThaiDonHangRepository trangThaiDonHangRepository;

    @Override
    public List<DonHangDto> getAllOrders() {
        return donHangRepository.getOrder();
    }

    @Override
    public DonHangChiTietDto getOrderDetail(Integer orderId) {
        return null;
    }

    @Override
    public List<DonHangChiTietDto> getOrderProducts(Integer orderId) {
        return donHangRepository.getOrderProducts(orderId);
    }

    @Override
    public boolean nextOrderStatus(Integer orderId) {
        DonHang donHang = donHangRepository.findById(orderId).orElse(null);
        if (donHang == null) return false;
        TrangThaiDonHang current = donHang.getTrangThaiDonHang();
        if (current == null) return false;
        var allStatus = trangThaiDonHangRepository.findAll();
        allStatus.sort((a, b) -> a.getId() - b.getId());
        int idx = -1;
        for (int i = 0; i < allStatus.size(); i++) {
            if (allStatus.get(i).getId().equals(current.getId())) {
                idx = i;
                break;
            }
        }
        if (idx == -1 || idx == allStatus.size() - 1) return false;
        TrangThaiDonHang next = allStatus.get(idx + 1);
        donHang.setTrangThaiDonHang(next);
        donHangRepository.save(donHang);
        return true;
    }

}
