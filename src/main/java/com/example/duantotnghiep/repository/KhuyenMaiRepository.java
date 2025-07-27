package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.KhuyenMai;
import com.example.duantotnghiep.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {
    @Query("SELECT km FROM KhuyenMai km WHERE km.tenChuongTrinh LIKE %:keyword% OR km.maKhuyenMai LIKE %:keyword%")
    List<KhuyenMai> searchKhuyenMai(@Param("keyword") String keyword);

    @Query("SELECT km FROM KhuyenMai km WHERE km.tenChuongTrinh LIKE %:keyword% OR km.maKhuyenMai LIKE %:keyword%")
    Page<KhuyenMai> searchKhuyenMaiWithPagination(@Param("keyword") String keyword, Pageable pageable);

    Page<KhuyenMai> findAll(Pageable pageable);
}