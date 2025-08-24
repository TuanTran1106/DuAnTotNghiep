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
        
        khuyenMaiRepository.save(khuyenMai);
        khuyenMai.setMaKhuyenMai("KM0" + khuyenMai.getId());
        return khuyenMaiRepository.save(khuyenMai);
    }
    
    /**
     * Validate business rules for KhuyenMai
     */
    private void validateKhuyenMai(KhuyenMai khuyenMai) {
        LocalDate today = LocalDate.now();
        
        // Kiểm tra ngày bắt đầu không được là quá khứ
        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayBatDau().isBefore(today)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được là quá khứ. Vui lòng chọn ngày từ hôm nay trở đi.");
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
