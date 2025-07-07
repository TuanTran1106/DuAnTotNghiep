package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.ChiTietDonHang;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietDonHangRepository extends CrudRepository<ChiTietDonHang, Integer> {




}
