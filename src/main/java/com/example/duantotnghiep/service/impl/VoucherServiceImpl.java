package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NhanVien;
import com.example.duantotnghiep.entity.Voucher;
import com.example.duantotnghiep.repository.VoucherRepository;
import com.example.duantotnghiep.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public List<Voucher> getAllVoucher() {
        return voucherRepository.findAll();
    }

    @Override
    public PageResponse<Voucher> getAllVoucherWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Voucher> voucherPage = voucherRepository.findAll(pageable);

        return new PageResponse<>(
                voucherPage.getContent(),
                page,
                voucherPage.getTotalPages(),
                voucherPage.getTotalElements(),
                size
        );
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        // Business validation
        validateVoucher(voucher);
        
        if (voucher.getId() == null) {
            // Tạo mới - set giá trị mặc định để pass validation
            voucher.setMaVoucher("TEMP_VOUCHER");
            voucher = voucherRepository.save(voucher);
            voucher.setMaVoucher("VOUCHER0" + voucher.getId());
            return voucherRepository.save(voucher);
        } else {
            // Cập nhật - lấy mã voucher hiện tại từ database
            Voucher existing = voucherRepository.findById(voucher.getId()).orElse(null);
            if (existing != null && existing.getMaVoucher() != null) {
                voucher.setMaVoucher(existing.getMaVoucher());
            }
            return voucherRepository.save(voucher);
        }
    }
    
    /**
     * Validate business rules for Voucher
     */
    private void validateVoucher(Voucher voucher) {
        LocalDate today = LocalDate.now();
        
        // Không cho phép sửa ngày bắt đầu khi cập nhật
        if (voucher.getId() != null && voucher.getNgayBatDau() != null) {
            Voucher existing = voucherRepository.findById(voucher.getId()).orElse(null);
            if (existing != null) {
                LocalDate oldStart = existing.getNgayBatDau();
                LocalDate newStart = voucher.getNgayBatDau();
                boolean changed = (oldStart == null && newStart != null) ||
                        (oldStart != null && !oldStart.isEqual(newStart));
                if (changed) {
                    throw new IllegalArgumentException("Không được chỉnh sửa ngày bắt đầu khi cập nhật voucher.");
                }
            }
        }

        // Bắt buộc ngày bắt đầu khi tạo mới
        if (voucher.getId() == null && voucher.getNgayBatDau() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu không được để trống.");
        }
        
        // Kiểm tra ngày kết thúc không được là quá khứ
        if (voucher.getNgayKetThuc() != null && voucher.getNgayKetThuc().isBefore(today)) {
            throw new IllegalArgumentException("Ngày kết thúc không được là quá khứ. Vui lòng chọn ngày trong tương lai.");
        }
        
        // Kiểm tra ngày bắt đầu phải trước ngày kết thúc
        if (voucher.getNgayBatDau() != null && voucher.getNgayKetThuc() != null && 
            voucher.getNgayBatDau().isAfter(voucher.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc.");
        }
        
        // Kiểm tra số lượng phải dương
        if (voucher.getSoLuong() != null && voucher.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }
        
        // Kiểm tra mức giảm phải dương
        if (voucher.getMucGiam() != null && voucher.getMucGiam().doubleValue() <= 0) {
            throw new IllegalArgumentException("Mức giảm phải lớn hơn 0.");
        }
        
        // Kiểm tra điều kiện tối thiểu không được âm
        if (voucher.getDieuKienToiThieu() != null && voucher.getDieuKienToiThieu().doubleValue() < 0) {
            throw new IllegalArgumentException("Điều kiện tối thiểu không được âm.");
        }
        
        // Set số lượng còn lại bằng số lượng ban đầu nếu là voucher mới
        if (voucher.getSoLuongCon() == null && voucher.getSoLuong() != null) {
            voucher.setSoLuongCon(voucher.getSoLuong());
        }
    }

    @Override
    public Voucher getVoucherById(int id) {
        return voucherRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteVoucher(int id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public List<Voucher> searchVoucher(String keyword) {
        return voucherRepository.searchVoucher(keyword);
    }

    @Override
    public PageResponse<Voucher> searchVoucherWithPagination(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Voucher> voucherPage = voucherRepository.searchVoucherWithPagination(keyword, pageable);

        return new PageResponse<>(
                voucherPage.getContent(),
                page,
                voucherPage.getTotalPages(),
                voucherPage.getTotalElements(),
                size
        );
    }

    @Override
    public boolean updateTrangThai(int id, boolean trangThai) {
        Voucher vc = voucherRepository.findById(id).orElse(null);
        if (vc == null) return false;
        vc.setTrangThai(trangThai);
        voucherRepository.save(vc);
        return true;
    }
}