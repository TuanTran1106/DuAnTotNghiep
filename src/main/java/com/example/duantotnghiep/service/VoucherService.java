package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NhanVien;
import com.example.duantotnghiep.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService {
    List<Voucher> getAllVoucher();
    PageResponse<Voucher> getAllVoucherWithPagination(int page, int size);
    Voucher saveVoucher(Voucher voucher);
    Voucher getVoucherById(int id);
    void deleteVoucher(int id);
    List<Voucher> searchVoucher(String keyword);
    PageResponse<Voucher> searchVoucherWithPagination(String keyword, int page, int size);
    boolean updateTrangThai(int id, boolean trangThai);}
