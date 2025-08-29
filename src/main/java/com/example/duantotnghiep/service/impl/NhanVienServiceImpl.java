package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.entity.NhanVien;
import com.example.duantotnghiep.entity.PhanQuyen;
import com.example.duantotnghiep.repository.NhanVienRepository;
import com.example.duantotnghiep.repository.PhanQuyenRepository;
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
    private PhanQuyenRepository phanQuyenRepository;
    
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
            
            // Set role mặc định cho nhân viên mới (nếu chưa có)
            if (nhanVien.getPhanQuyen() == null) {
                PhanQuyen defaultRole = phanQuyenRepository.findByRoleName("nhan_vien").orElse(null);
                if (defaultRole == null) {
                    // Nếu không có role "nhan_vien", lấy role đầu tiên
                    defaultRole = phanQuyenRepository.findAll().stream().findFirst().orElse(null);
                }
                nhanVien.setPhanQuyen(defaultRole);
            }
            
            // Mã hóa mật khẩu cho nhân viên mới
            if (nhanVien.getMatKhau() != null && !nhanVien.getMatKhau().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(nhanVien.getMatKhau());
                nhanVien.setMatKhau(encodedPassword);
            }
        } else {
            // Khi update, lấy thông tin nhân viên hiện tại từ database
            NhanVien existingNhanVien = nhanVienRepository.findById(nhanVien.getId()).orElse(null);
            if (existingNhanVien != null) {
                // Giữ nguyên ngày tạo ban đầu
                nhanVien.setNgayTao(existingNhanVien.getNgayTao());
                
                // Cập nhật ngày sửa
                nhanVien.setNgaySua(LocalDateTime.now());
                
                // Giữ nguyên role nếu không có thay đổi
                if (nhanVien.getPhanQuyen() == null) {
                    nhanVien.setPhanQuyen(existingNhanVien.getPhanQuyen());
                }
                
                // Kiểm tra xem có thay đổi mật khẩu không
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
        
        // Lưu nhân viên lần đầu để lấy ID
        nhanVien = nhanVienRepository.save(nhanVien);
        
        // Nếu là nhân viên mới, cập nhật mã nhân viên và lưu lại
        if (nhanVien.getId() != null && nhanVien.getMaNhanVien() == null) {
            nhanVien.setMaNhanVien("NV0" + nhanVien.getId());
            return nhanVienRepository.save(nhanVien);
        }
        
        return nhanVien;
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
        nv.setNgaySua(LocalDateTime.now()); // Cập nhật ngày sửa khi thay đổi trạng thái
        // Giữ nguyên các thông tin khác
        nhanVienRepository.save(nv);
        return true;
    }
}