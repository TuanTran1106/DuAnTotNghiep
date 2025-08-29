package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.KhuyenMai;
import com.example.duantotnghiep.entity.SanPhamChiTiet;
import com.example.duantotnghiep.repository.KhuyenMaiRepository;
import com.example.duantotnghiep.repository.SanPhamChiTietRepository;
import com.example.duantotnghiep.service.KhuyenMaiService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class KhuyenMaiServiceImpl implements KhuyenMaiService {
    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;


    @Autowired
    private SanPhamChiTietRepository spctRepository;

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
    public KhuyenMai saveKhuyenMai(KhuyenMai khuyenMai, List<Integer> spctIds) {
        // Business validation
        validateKhuyenMai(khuyenMai);

        List<SanPhamChiTiet> spcts = spctRepository.findAllById(spctIds);
        khuyenMai.setSanPhamChiTiets(spcts);
        
        if (khuyenMai.getId() == null) {
            // Tạo mới - set ngày tạo và giá trị mặc định để pass validation
            khuyenMai.setNgayTao(LocalDateTime.now());
            khuyenMai.setMaKhuyenMai("TEMP_KM");
            khuyenMai = khuyenMaiRepository.save(khuyenMai);
            khuyenMai.setMaKhuyenMai("KM0" + khuyenMai.getId());
            return khuyenMaiRepository.save(khuyenMai);
        } else {
            // Cập nhật - lấy mã khuyến mãi hiện tại từ database và cập nhật ngày tạo
            KhuyenMai existing = khuyenMaiRepository.findById(khuyenMai.getId()).orElse(null);
            if (existing != null && existing.getMaKhuyenMai() != null) {
                khuyenMai.setMaKhuyenMai(existing.getMaKhuyenMai());
            }
            // Cập nhật ngày tạo khi update
            khuyenMai.setNgayTao(LocalDateTime.now());
            return khuyenMaiRepository.save(khuyenMai);
        }
    }
    
    /**
     * Validate business rules for KhuyenMai
     */
    private void validateKhuyenMai(KhuyenMai khuyenMai) {
        LocalDate today = LocalDate.now();
        
        // Không cho phép sửa ngày bắt đầu khi cập nhật
        if (khuyenMai.getId() != null && khuyenMai.getNgayBatDau() != null) {
            KhuyenMai existing = khuyenMaiRepository.findById(khuyenMai.getId()).orElse(null);
            if (existing != null) {
                LocalDate oldStart = existing.getNgayBatDau();
                LocalDate newStart = khuyenMai.getNgayBatDau();
                boolean changed = (oldStart == null && newStart != null) ||
                        (oldStart != null && !oldStart.isEqual(newStart));
                if (changed) {
                    throw new IllegalArgumentException("Không được chỉnh sửa ngày bắt đầu khi cập nhật khuyến mãi.");
                }
            }
        }
        
        // Kiểm tra ngày kết thúc không được là quá khứ
        if (khuyenMai.getNgayKetThuc() != null && khuyenMai.getNgayKetThuc().isBefore(today)) {
            throw new IllegalArgumentException("Ngày kết thúc không được là quá khứ. Vui lòng chọn ngày trong tương lai.");
        }
        
        // Kiểm tra ngày bắt đầu phải trước ngày kết thúc
        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null && 
            khuyenMai.getNgayBatDau().isAfter(khuyenMai.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc.");
        }
        
        // Kiểm tra số lượng phải dương
        if (khuyenMai.getSoLuong() != null && khuyenMai.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }
        
        // Kiểm tra mức giảm giá phải dương
        if (khuyenMai.getMucGiamGia() != null && khuyenMai.getMucGiamGia().doubleValue() <= 0) {
            throw new IllegalArgumentException("Mức giảm giá phải lớn hơn 0.");
        }
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
