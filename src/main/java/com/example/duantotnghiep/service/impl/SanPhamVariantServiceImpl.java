package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.repository.SanPhamChiTietRepository;
import com.example.duantotnghiep.service.SanPhamVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamVariantServiceImpl implements SanPhamVariantService {
    
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    
    @Override
    public List<String> getAllMauSac() {
        return sanPhamChiTietRepository.findAllDistinctMauSac().stream()
                .filter(mauSac -> !mauSac.equals("Chưa xác định"))
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<String> getAllKichThuoc() {
        return sanPhamChiTietRepository.findAllDistinctKichThuoc().stream()
                .filter(kichThuoc -> !kichThuoc.equals("Tiêu chuẩn"))
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<String> getAllChatLieu() {
        return sanPhamChiTietRepository.findAllDistinctChatLieu().stream()
                .filter(chatLieu -> !chatLieu.equals("Chưa xác định"))
                .collect(java.util.stream.Collectors.toList());
    }
} 