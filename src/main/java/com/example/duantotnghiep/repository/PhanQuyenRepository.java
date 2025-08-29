package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.PhanQuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhanQuyenRepository extends JpaRepository<PhanQuyen, Integer> {
    Optional<PhanQuyen> findByRoleName(String roleName);
}
