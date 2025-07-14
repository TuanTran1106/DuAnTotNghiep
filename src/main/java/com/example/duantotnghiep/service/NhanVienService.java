package com.example.duantotnghiep.service;

import com.example.duantotnghiep.entity.NhanVien;

import java.util.List;

public interface NhanVienService {
    List<NhanVien> getAllNhanVien();

    NhanVien saveNhanVien(NhanVien nhanVien);

    NhanVien getNhanVienById(int id);

    void deleteNhanVien(int id);

    List<NhanVien> searchNhanVien(String keyword);
}
