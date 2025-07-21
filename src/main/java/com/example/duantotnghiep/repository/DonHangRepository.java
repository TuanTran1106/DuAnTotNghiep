package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.dto.ThongKeDoanhThuDto;
import com.example.duantotnghiep.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

    @Query("SELECT new com.example.duantotnghiep.dto.DonHangDto(" +
            "dh.id, dh.maDonHang, dh.ngayMua, nd.hoTen, ttdh.tenTrangThai, pttt.tenPhuongThuc, " +
            "COALESCE(nv.hoTen, ''), SUM(ctdh.soLuongDat), " +
            "CAST(SUM(ctdh.donGia * ctdh.soLuongDat) AS big_decimal), " +
            "CAST(SUM(ctdh.donGia * ctdh.soLuongDat) AS big_decimal), " +
            "COALESCE(v.maVoucher, ''), COALESCE(v.tenVoucher, ''), " +
            "CASE WHEN dc.diaChi IS NOT NULL THEN CONCAT(COALESCE(dc.diaChi, ''), ', ', COALESCE(dc.phuongXa, ''), ', ', COALESCE(dc.quanHuyen, ''), ', ', COALESCE(dc.thanhPho, '')) ELSE '' END, " +

            "CASE " +
            "  WHEN v.id IS NULL THEN CAST(SUM(ctdh.donGia * ctdh.soLuongDat) AS big_decimal) " +
            "  WHEN v.maVoucher = 'VOUCHER01' THEN CAST(SUM(ctdh.donGia * ctdh.soLuongDat) - 50000 AS big_decimal) " +
            "  WHEN v.maVoucher = 'VOUCHER02' THEN CAST(SUM(ctdh.donGia * ctdh.soLuongDat) * 0.9 AS big_decimal) " +
            "  WHEN v.maVoucher = 'VOUCHER03' THEN CAST(SUM(ctdh.donGia * ctdh.soLuongDat) * 0.9 AS big_decimal) " +
            "  WHEN v.maVoucher = 'VOUCHER04' AND SUM(ctdh.donGia * ctdh.soLuongDat) >= 1000000 THEN CAST(SUM(ctdh.donGia * ctdh.soLuongDat) * 0.8 AS big_decimal) " +
            "  WHEN v.maVoucher = 'VOUCHER05' THEN CAST(SUM(ctdh.donGia * ctdh.soLuongDat) - 10000 AS big_decimal) " +
            "  ELSE CAST(SUM(ctdh.donGia * ctdh.soLuongDat) AS big_decimal) " +
            "END, " +
            "nd.sdt" +
            ") " +
            "FROM DonHang dh " +
            "JOIN dh.nguoiDung nd " +
            "JOIN dh.trangThaiDonHang ttdh " +
            "JOIN dh.phuongThucThanhToan pttt " +
            "LEFT JOIN dh.nhanVien nv " +
            "LEFT JOIN dh.voucher v " +
            "LEFT JOIN DiaChiNguoiDung dc ON dh.idDiaChi = dc.id " +
            "JOIN dh.chiTietDonHangs ctdh " +
            "GROUP BY dh.id, dh.maDonHang, dh.ngayMua, nd.hoTen, nd.sdt, ttdh.tenTrangThai, pttt.tenPhuongThuc, nv.hoTen, v.maVoucher, v.tenVoucher, v.id, v.kieuGiam, v.mucGiam, v.dieuKienToiThieu, dc.diaChi, dc.phuongXa, dc.quanHuyen, dc.thanhPho")
    List<DonHangDto> getOrder();

    @Query("SELECT new com.example.duantotnghiep.dto.DonHangChiTietDto(" +
            "ctdh.id, ctdh.sanPhamChiTiet.id, sp.maSanPham, sp.tenSanPham, " +
            "sp.hinhAnh, spct.mauSac, spct.kichThuoc, spct.chatLieu, " +
            "ctdh.soLuongDat, ctdh.donGia, (ctdh.soLuongDat * ctdh.donGia)) " +
            "FROM ChiTietDonHang ctdh " +
            "JOIN ctdh.sanPhamChiTiet spct " +
            "JOIN spct.sanPham sp " +
            "WHERE ctdh.donHang.id = :orderId")
    List<DonHangChiTietDto> getOrderProducts(@Param("orderId") Integer orderId);


}
