package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query("SELECT nv FROM NhanVien nv WHERE nv.hoTen LIKE %:keyword% OR nv.sdt LIKE %:keyword%")
    List<NhanVien> searchNhanVien(@Param("keyword") String keyword);

    @Query("SELECT nv FROM NhanVien nv WHERE nv.hoTen LIKE %:keyword% OR nv.sdt LIKE %:keyword%")
    Page<NhanVien> searchNhanVienWithPagination(@Param("keyword") String keyword, Pageable pageable);

    Page<NhanVien> findAll(Pageable pageable);

    Optional<NhanVien> findByEmailAndMatKhau(String email, String matKhau);
}
