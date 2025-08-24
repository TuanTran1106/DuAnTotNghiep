package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.ChiTietDonHang;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietDonHangRepository extends CrudRepository<ChiTietDonHang, Integer> {

    // Lấy chi tiết đơn hàng theo ID đơn hàng
    List<ChiTietDonHang> findByDonHang_Id(Integer donHangId);
    
    // Xóa chi tiết đơn hàng theo ID đơn hàng
    @Modifying
    @Query("DELETE FROM ChiTietDonHang ctdh WHERE ctdh.donHang.id = :donHangId")
    void deleteByDonHangId(@Param("donHangId") Integer donHangId);
}
