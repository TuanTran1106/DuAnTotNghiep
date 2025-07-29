package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    java.util.List<SanPham> findByTenSanPhamContainingIgnoreCaseOrMaSanPhamContainingIgnoreCase(String ten, String ma);
    java.util.List<SanPham> findByTrangThai(Boolean trangThai);
    java.util.List<SanPham> findByTrangThaiAndTenSanPhamContainingIgnoreCaseOrTrangThaiAndMaSanPhamContainingIgnoreCase(Boolean trangThai1, String ten, Boolean trangThai2, String ma);
    Page<SanPham> findAll(Pageable pageable);
    Page<SanPham> findByTenSanPhamContainingIgnoreCaseOrMaSanPhamContainingIgnoreCase(String ten, String ma, Pageable pageable);
    Page<SanPham> findByTrangThai(Boolean trangThai, Pageable pageable);
    Page<SanPham> findByTrangThaiAndTenSanPhamContainingIgnoreCaseOrTrangThaiAndMaSanPhamContainingIgnoreCase(Boolean trangThai1, String ten, Boolean trangThai2, String ma, Pageable pageable);
    org.springframework.data.domain.Page<SanPham> findByDanhMuc_IdAndThuongHieu_Id(Integer danhMucId, Integer thuongHieuId, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<SanPham> findByDanhMuc_Id(Integer danhMucId, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<SanPham> findByThuongHieu_Id(Integer thuongHieuId, org.springframework.data.domain.Pageable pageable);
    @Query("SELECT s FROM SanPham s WHERE LOWER(s.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.maSanPham) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SanPham> searchByTenOrMa(@Param("keyword") String keyword, Pageable pageable);
}
