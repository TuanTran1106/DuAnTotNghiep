package com.example.duantotnghiep.service;

import com.example.duantotnghiep.entity.NguoiDung;

import java.util.List;

public interface NguoiDungService {
    List<NguoiDung> getAllNguoiDung();
    NguoiDung saveNguoiDung(NguoiDung nguoiDung);
    NguoiDung getNguoiDungById(int id);
    void deleteNguoiDung(int id);
    List<NguoiDung> searchNguoiDung(String keyword);
    boolean updateTrangThai(int id, boolean trangThai);
}
