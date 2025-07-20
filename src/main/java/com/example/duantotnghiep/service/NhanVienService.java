package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NhanVien;

import java.util.List;

public interface NhanVienService {
    List<NhanVien> getAllNhanVien();
    PageResponse<NhanVien> getAllNhanVienWithPagination(int page, int size);
    NhanVien saveNhanVien(NhanVien nhanVien);
    NhanVien getNhanVienById(int id);
    void deleteNhanVien(int id);
    List<NhanVien> searchNhanVien(String keyword);
    PageResponse<NhanVien> searchNhanVienWithPagination(String keyword, int page, int size);
    boolean updateTrangThai(int id, boolean trangThai);
}

