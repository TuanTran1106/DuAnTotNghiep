package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.NguoiDung;
import com.example.duantotnghiep.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    @Query("SELECT nv FROM NguoiDung nv WHERE nv.hoTen LIKE %:keyword% OR nv.sdt LIKE %:keyword%")
    List<NguoiDung> searchNguoiDung(@Param("keyword") String keyword);
}
