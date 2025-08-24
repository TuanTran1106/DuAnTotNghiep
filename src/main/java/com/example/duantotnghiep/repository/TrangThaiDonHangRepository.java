package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.TrangThaiDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrangThaiDonHangRepository extends JpaRepository<TrangThaiDonHang, Integer> {
    Optional<TrangThaiDonHang> findByTenTrangThai(String tenTrangThai);

    TrangThaiDonHang findByTenTrangThaiIgnoreCase(String nextName);
}