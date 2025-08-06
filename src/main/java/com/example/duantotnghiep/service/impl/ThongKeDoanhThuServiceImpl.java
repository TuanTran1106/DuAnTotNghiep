package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.DashboardThongKeDto;
import com.example.duantotnghiep.repository.ThongKeDoanhThuRepository;
import com.example.duantotnghiep.service.ThongKeDoanhThuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@AllArgsConstructor
public class ThongKeDoanhThuServiceImpl implements ThongKeDoanhThuService {


    private final ThongKeDoanhThuRepository thongKeDoanhThuRepository;

    @Override
    public DashboardThongKeDto thongKeDashboard(LocalDateTime from, LocalDateTime to) {
        DashboardThongKeDto dto = new DashboardThongKeDto();

        // Khởi tạo giá trị mặc định để tránh null
        dto.setTongSoDon(thongKeDoanhThuRepository.countDonHangByDate(from, to) != null ? thongKeDoanhThuRepository.countDonHangByDate(from, to) : 0L);
        dto.setTongDoanhThu(thongKeDoanhThuRepository.sumDoanhThuByDate(from, to) != null ? thongKeDoanhThuRepository.sumDoanhThuByDate(from, to) : BigDecimal.ZERO);
        dto.setTongSanPhamBan(thongKeDoanhThuRepository.sumSanPhamBanByDate(from, to) != null ? thongKeDoanhThuRepository.sumSanPhamBanByDate(from, to) : 0L);
        dto.setSoKhachHangMoi(thongKeDoanhThuRepository.countKhachHangMoiByDate(from, to) != null ? thongKeDoanhThuRepository.countKhachHangMoiByDate(from, to) : 0L);
        dto.setThongKeList(thongKeDoanhThuRepository.thongKeDoanhThuTheoNgay(from, to) != null ? thongKeDoanhThuRepository.thongKeDoanhThuTheoNgay(from, to) : Collections.emptyList());
        // Nếu sử dụng topSanPhamBanChay, bỏ comment dòng dưới sau khi sửa query
        dto.setSanPhamBanChayList(thongKeDoanhThuRepository.topSanPhamBanChay(from, to) != null ? thongKeDoanhThuRepository.topSanPhamBanChay(from, to) : Collections.emptyList());

        return dto;
    }
}
