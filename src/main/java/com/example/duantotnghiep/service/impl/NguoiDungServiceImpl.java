package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NguoiDung;
import com.example.duantotnghiep.entity.PhanQuyen;
import com.example.duantotnghiep.repository.NguoiDungRepository;
import com.example.duantotnghiep.repository.PhanQuyenRepository;
import com.example.duantotnghiep.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Autowired
    private PhanQuyenRepository phanQuyenRepository;

    @Override
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }

    @Override
    public PageResponse<NguoiDung> getAllNguoiDungWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NguoiDung> nguoiDungPage = nguoiDungRepository.findAll(pageable);

        return new PageResponse<>(
                nguoiDungPage.getContent(),
                page,
                nguoiDungPage.getTotalPages(),
                nguoiDungPage.getTotalElements(),
                size
        );
    }

    @Override
    public NguoiDung saveNguoiDung(NguoiDung nguoiDung) {
        // Set ngày tạo nếu là người dùng mới
        if (nguoiDung.getId() == null) {
            nguoiDung.setNgayTao(LocalDateTime.now());
            
            // Set role mặc định cho người dùng mới (khách hàng)
            if (nguoiDung.getPhanQuyen() == null) {
                PhanQuyen defaultRole = phanQuyenRepository.findByRoleName("khach_hang").orElse(null);
                if (defaultRole != null) {
                    nguoiDung.setPhanQuyen(defaultRole);
                }
            }
        } else {
            // Khi update, lấy thông tin người dùng hiện tại từ database
            NguoiDung existingNguoiDung = nguoiDungRepository.findById(nguoiDung.getId()).orElse(null);
            if (existingNguoiDung != null) {
                // Giữ nguyên ngày tạo ban đầu
                nguoiDung.setNgayTao(existingNguoiDung.getNgayTao());
                nguoiDung.setMatKhau(existingNguoiDung.getMatKhau());
                
                // Cập nhật ngày sửa
                nguoiDung.setNgaySua(LocalDateTime.now());
                
                // Giữ nguyên role nếu không có thay đổi
                if (nguoiDung.getPhanQuyen() == null) {
                    nguoiDung.setPhanQuyen(existingNguoiDung.getPhanQuyen());
                }
            }
        }
        
        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public NguoiDung getNguoiDungById(int id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteNguoiDung(int id) {
        nguoiDungRepository.deleteById(id);
    }

    @Override
    public List<NguoiDung> searchNguoiDung(String keyword) {
        return nguoiDungRepository.searchNguoiDung(keyword);
    }

    @Override
    public PageResponse<NguoiDung> searchNguoiDungWithPagination(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NguoiDung> nguoiDungPage = nguoiDungRepository.searchNguoiDungWithPagination(keyword, pageable);

        return new PageResponse<>(
                nguoiDungPage.getContent(),
                page,
                nguoiDungPage.getTotalPages(),
                nguoiDungPage.getTotalElements(),
                size
        );
    }

    @Override
    public boolean updateTrangThai(int id, boolean trangThai) {
        NguoiDung nd = nguoiDungRepository.findById(id).orElse(null);
        if (nd == null) return false;
        nd.setTrangThai(trangThai);
        nd.setNgaySua(LocalDateTime.now()); // Cập nhật ngày sửa khi thay đổi trạng thái
        // Giữ nguyên các thông tin khác
        nguoiDungRepository.save(nd);
        return true;
    }
}
