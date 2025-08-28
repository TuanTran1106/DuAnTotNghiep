package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.entity.NhanVien;
import com.example.duantotnghiep.repository.NhanVienRepository;
import com.example.duantotnghiep.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.duantotnghiep.dto.PageResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NhanVienServiceImpl implements NhanVienService {
    @Autowired
    private NhanVienRepository nhanVienRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<NhanVien> getAllNhanVien() {
        return nhanVienRepository.findAll();
    }

    @Override
    public PageResponse<NhanVien> getAllNhanVienWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NhanVien> nhanVienPage = nhanVienRepository.findAll(pageable);

        return new PageResponse<>(
                nhanVienPage.getContent(),
                page,
                nhanVienPage.getTotalPages(),
                nhanVienPage.getTotalElements(),
                size
        );
    }

    @Override
    public NhanVien saveNhanVien(NhanVien nhanVien) {
        // Set ngày tạo nếu là nhân viên mới
        if (nhanVien.getId() == null) {
            nhanVien.setNgayTao(LocalDateTime.now());
            // Mã hóa mật khẩu cho nhân viên mới
            if (nhanVien.getMatKhau() != null && !nhanVien.getMatKhau().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(nhanVien.getMatKhau());
                nhanVien.setMatKhau(encodedPassword);
            }
        } else {
            nhanVien.setNgaySua(LocalDateTime.now());
            // Kiểm tra xem có thay đổi mật khẩu không
            NhanVien existingNhanVien = nhanVienRepository.findById(nhanVien.getId()).orElse(null);
            if (existingNhanVien != null) {
                // Nếu mật khẩu mới không rỗng và khác mật khẩu cũ, thì mã hóa
                if (nhanVien.getMatKhau() != null && !nhanVien.getMatKhau().isEmpty() 
                    && !passwordEncoder.matches(nhanVien.getMatKhau(), existingNhanVien.getMatKhau())) {
                    String encodedPassword = passwordEncoder.encode(nhanVien.getMatKhau());
                    nhanVien.setMatKhau(encodedPassword);
                } else {
                    // Giữ nguyên mật khẩu cũ
                    nhanVien.setMatKhau(existingNhanVien.getMatKhau());
                }
            }
        }
        
        nhanVienRepository.save(nhanVien);
        nhanVien.setMaNhanVien("NV0" + nhanVien.getId());
        return nhanVienRepository.save(nhanVien);
    }

    @Override
    public NhanVien getNhanVienById(int id) {
        return nhanVienRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteNhanVien(int id) {
        nhanVienRepository.deleteById(id);
    }

    @Override
    public List<NhanVien> searchNhanVien(String keyword) {
        return nhanVienRepository.searchNhanVien(keyword);
    }

    @Override
    public PageResponse<NhanVien> searchNhanVienWithPagination(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NhanVien> nhanVienPage = nhanVienRepository.searchNhanVienWithPagination(keyword, pageable);

        return new PageResponse<>(
                nhanVienPage.getContent(),
                page,
                nhanVienPage.getTotalPages(),
                nhanVienPage.getTotalElements(),
                size
        );
    }

    @Override
    public boolean updateTrangThai(int id, boolean trangThai) {
        NhanVien nv = nhanVienRepository.findById(id).orElse(null);
        if (nv == null) return false;
        nv.setTrangThai(trangThai);
        nhanVienRepository.save(nv);
        return true;
    }
}