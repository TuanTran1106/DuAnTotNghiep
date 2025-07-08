package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.DiaChiNguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaChiNguoiDungRepository extends JpaRepository<DiaChiNguoiDung, Integer> {
}
