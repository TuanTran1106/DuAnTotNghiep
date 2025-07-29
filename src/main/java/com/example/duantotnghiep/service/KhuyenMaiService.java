package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.KhuyenMai;
import com.example.duantotnghiep.entity.Voucher;

import java.util.List;

public interface KhuyenMaiService {
    List<KhuyenMai> getAllKhuyenMai();
    PageResponse<KhuyenMai> getAllKhuyenMaiWithPagination(int page, int size);
    KhuyenMai saveKhuyenMai(KhuyenMai khuyenMai);
    KhuyenMai getKhuyenMaiById(int id);
    List<KhuyenMai> searchKhuyenMai(String keyword);
    PageResponse<KhuyenMai> searchKhuyenMaiWithPagination(String keyword, int page, int size);
    boolean updateTrangThai(int id, int trangThai);
}
