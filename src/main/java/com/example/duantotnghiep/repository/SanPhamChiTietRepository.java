package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {



    @Query("SELECT s FROM SanPhamChiTiet s WHERE s.sanPham.id = ?1 ORDER BY s.id DESC")
    List<SanPhamChiTiet> findBySanPham_Id(Integer sanPhamId);

    @Query("SELECT s FROM SanPhamChiTiet s WHERE s.sanPham.id = ?1 AND s.gia_ban > 0 AND s.soLuong > 0 ORDER BY s.id DESC")
    List<SanPhamChiTiet> findValidBySanPham_Id(Integer sanPhamId);

    @Query("SELECT DISTINCT s.mauSac FROM SanPhamChiTiet s WHERE s.mauSac IS NOT NULL AND s.mauSac != '' ORDER BY s.mauSac")
    List<String> findAllDistinctMauSac();

    @Query("SELECT DISTINCT s.kichThuoc FROM SanPhamChiTiet s WHERE s.kichThuoc IS NOT NULL AND s.kichThuoc != '' ORDER BY s.kichThuoc")
    List<String> findAllDistinctKichThuoc();

    @Query("SELECT DISTINCT s.chatLieu FROM SanPhamChiTiet s WHERE s.chatLieu IS NOT NULL AND s.chatLieu != '' ORDER BY s.chatLieu")
    List<String> findAllDistinctChatLieu();
}
