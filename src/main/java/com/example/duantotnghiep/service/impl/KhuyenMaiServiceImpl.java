package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.KhuyenMai;
import com.example.duantotnghiep.repository.KhuyenMaiRepository;
import com.example.duantotnghiep.service.KhuyenMaiService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class KhuyenMaiServiceImpl implements KhuyenMaiService {
    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @Override
    public List<KhuyenMai> getAllKhuyenMai() {
        return khuyenMaiRepository.findAll();
    }

    @Override
    public PageResponse<KhuyenMai> getAllKhuyenMaiWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<KhuyenMai> voucherPage = khuyenMaiRepository.findAll(pageable);

        return new PageResponse<>(
                voucherPage.getContent(),
                page,
                voucherPage.getTotalPages(),
                voucherPage.getTotalElements(),
                size
        );
    }

    @Override
    public KhuyenMai saveKhuyenMai(KhuyenMai khuyenMai) {
        khuyenMaiRepository.save(khuyenMai);
        khuyenMai.setMaKhuyenMai("KM0" + khuyenMai.getId());
        return khuyenMaiRepository.save(khuyenMai);
    }

    @Override
    public KhuyenMai getKhuyenMaiById(int id) {
        return khuyenMaiRepository.findById(id).orElse(null);
    }


    @Override
    public List<KhuyenMai> searchKhuyenMai(String keyword) {
        return khuyenMaiRepository.searchKhuyenMai(keyword);
    }

    @Override
    public PageResponse<KhuyenMai> searchKhuyenMaiWithPagination(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<KhuyenMai> voucherPage = khuyenMaiRepository.searchKhuyenMaiWithPagination(keyword, pageable);

        return new PageResponse<>(
                voucherPage.getContent(),
                page,
                voucherPage.getTotalPages(),
                voucherPage.getTotalElements(),
                size
        );
    }

    @Override
    public boolean updateTrangThai(int id, int trangThai) {
        KhuyenMai km = khuyenMaiRepository.findById(id).orElse(null);
        if (km == null) return false;
        km.setTrangThai(trangThai);
        khuyenMaiRepository.save(km);
        return true;
    }
}
