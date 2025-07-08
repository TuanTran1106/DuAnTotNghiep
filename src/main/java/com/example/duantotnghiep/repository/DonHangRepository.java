package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.entity.ChiTietDonHang;
import com.example.duantotnghiep.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

    @Query("SELECT new com.example.duantotnghiep.dto.DonHangDto(" +
            "d.id, d.maDonHang, nd.hoTen, d.tongGia, d.ngayMua, tt.tenTrangThai, pt.tenPhuongThuc) " +
            "FROM DonHang d " +
            "JOIN d.nguoiDung nd " +
            "JOIN d.trangThaiDonHang tt " +
            "JOIN d.phuongThucThanhToan pt " +
            "ORDER BY d.ngayMua DESC")
    List<DonHangDto> findOrder();

    @Query("SELECT new com.example.duantotnghiep.dto.DonHangChiTietDto(" +
            "sp.hinhAnh, sp.tenSanPham, ct.soLuong, ct.donGia) " +
            "FROM ChiTietDonHang ct " +
            "JOIN ct.sanPhamChiTiet spct " +
            "JOIN spct.sanPham sp " +
            "WHERE ct.donHang.id = :donHangId")
    List<DonHangChiTietDto> findOrderDetail(@Param("donHangId") Integer donHangId);



}
