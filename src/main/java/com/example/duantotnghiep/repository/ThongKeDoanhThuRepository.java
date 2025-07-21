package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.dto.DashboardThongKeDto;
import com.example.duantotnghiep.dto.SanPhamBanChayDto;
import com.example.duantotnghiep.dto.ThongKeDoanhThuDto;
import com.example.duantotnghiep.entity.ThongKeDoanhThu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThongKeDoanhThuRepository extends JpaRepository<ThongKeDoanhThu, Integer> {


    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayTao BETWEEN :from AND :to")
    Long countDonHangByDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(SUM(ctdh.donGia * ctdh.soLuongDat), 0) FROM ChiTietDonHang ctdh JOIN ctdh.donHang d WHERE d.ngayTao BETWEEN :from AND :to")
    BigDecimal sumDoanhThuByDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(SUM(ctdh.soLuongDat), 0) FROM ChiTietDonHang ctdh JOIN ctdh.donHang d WHERE d.ngayTao BETWEEN :from AND :to")
    Long sumSanPhamBanByDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COUNT(nd) FROM NguoiDung nd WHERE nd.ngayTao BETWEEN :from AND :to")
    Long countKhachHangMoiByDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "SELECT CONVERT(date, d.ngay_tao) AS thoi_gian, SUM(ctdh.don_gia * ctdh.so_luong_dat) AS tong_doanh_thu " +
            "FROM chi_tiet_don_hang ctdh JOIN don_hang d ON ctdh.id_don_hang = d.id " +
            "WHERE d.ngay_tao BETWEEN :from AND :to " +
            "GROUP BY CONVERT(date, d.ngay_tao) ORDER BY CONVERT(date, d.ngay_tao)",
            nativeQuery = true)
    List<ThongKeDoanhThuDto> thongKeDoanhThuTheoNgay(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);


    @Query("SELECT new com.example.duantotnghiep.dto.SanPhamBanChayDto(sp.tenSanPham, SUM(ctdh.soLuongDat), SUM(ctdh.donGia * ctdh.soLuongDat), spct.soLuong) " +
            "FROM ChiTietDonHang ctdh JOIN ctdh.sanPhamChiTiet spct JOIN spct.sanPham sp JOIN ctdh.donHang d " +
            "WHERE d.ngayTao BETWEEN :from AND :to " +
            "GROUP BY sp.id, sp.tenSanPham, spct.soLuong " +
            "ORDER BY SUM(ctdh.soLuongDat) DESC")
    List<SanPhamBanChayDto> topSanPhamBanChay(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);




}

