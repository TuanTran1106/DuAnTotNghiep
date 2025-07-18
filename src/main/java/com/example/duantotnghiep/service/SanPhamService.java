package com.example.duantotnghiep.service;

import com.example.duantotnghiep.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SanPhamService {
    List<SanPham> getAllSanPham();
    SanPham getSanPhamById(Integer id);
    SanPham addSanPham(SanPham sanPham);
    SanPham updateSanPham(Integer id, SanPham sanPham);
    void deleteSanPham(Integer id);
    List<SanPham> searchSanPham(String keyword);
    String generateMaSanPham();
    List<SanPham> getByTrangThai(Boolean trangThai);
    List<SanPham> searchByTrangThaiAndKeyword(Boolean trangThai, String keyword);
    Page<SanPham> getAllSanPham(Pageable pageable);
    Page<SanPham> searchSanPham(String keyword, Pageable pageable);
    Page<SanPham> getByTrangThai(Boolean trangThai, Pageable pageable);
    Page<SanPham> searchByTrangThaiAndKeyword(Boolean trangThai, String keyword, Pageable pageable);
    List<com.example.duantotnghiep.entity.SanPhamChiTiet> getChiTietBySanPhamId(Integer sanPhamId);
    Page<SanPham> findByDanhMucIdAndThuongHieuId(Integer danhMucId, Integer thuongHieuId, Pageable pageable);
    Page<SanPham> findByDanhMucId(Integer danhMucId, Pageable pageable);
    Page<SanPham> findByThuongHieuId(Integer thuongHieuId, Pageable pageable);
} 