package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {}