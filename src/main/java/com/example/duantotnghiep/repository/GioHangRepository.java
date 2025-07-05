package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Integer> {}
