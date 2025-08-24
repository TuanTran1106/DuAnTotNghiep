package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {

    List<SanPhamChiTiet> findBySanPham_Id(Integer sanPhamId);
    
    // Tìm kiếm sản phẩm theo tên hoặc mã
    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE " +
           "LOWER(spct.sanPham.tenSanPham) LIKE LOWER(CONCAT('%', :tuKhoa, '%')) OR " +
           "LOWER(spct.sanPham.maSanPham) LIKE LOWER(CONCAT('%', :tuKhoa, '%'))")
    List<SanPhamChiTiet> findBySanPham_TenSanPhamContainingIgnoreCaseOrSanPham_MaSanPhamContainingIgnoreCase(
            @Param("tuKhoa") String tuKhoa, @Param("tuKhoa") String tuKhoa2);
    
    // Tìm kiếm sản phẩm theo mã
    Optional<SanPhamChiTiet> findBySanPham_MaSanPham(String maSanPham);
    
    // Tìm kiếm sản phẩm còn hàng
    List<SanPhamChiTiet> findBySoLuongGreaterThan(Integer soLuong);
}
