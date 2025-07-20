package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    @Query("SELECT nv FROM NguoiDung nv WHERE nv.hoTen LIKE %:keyword% OR nv.sdt LIKE %:keyword%")
    List<NguoiDung> searchNguoiDung(@Param("keyword") String keyword);

    @Query("SELECT nv FROM NguoiDung nv WHERE nv.hoTen LIKE %:keyword% OR nv.sdt LIKE %:keyword%")
    Page<NguoiDung> searchNguoiDungWithPagination(@Param("keyword") String keyword, Pageable pageable);

    Page<NguoiDung> findAll(Pageable pageable);
}
