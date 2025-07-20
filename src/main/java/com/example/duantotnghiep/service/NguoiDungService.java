package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NguoiDung;

import java.util.List;

public interface NguoiDungService {
    List<NguoiDung> getAllNguoiDung();
    PageResponse<NguoiDung> getAllNguoiDungWithPagination(int page, int size);
    NguoiDung saveNguoiDung(NguoiDung nguoiDung);
    NguoiDung getNguoiDungById(int id);
    void deleteNguoiDung(int id);
    List<NguoiDung> searchNguoiDung(String keyword);
    PageResponse<NguoiDung> searchNguoiDungWithPagination(String keyword, int page, int size);
    boolean updateTrangThai(int id, boolean trangThai);
}
