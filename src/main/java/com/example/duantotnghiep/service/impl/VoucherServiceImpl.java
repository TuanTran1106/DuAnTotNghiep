package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NhanVien;
import com.example.duantotnghiep.entity.Voucher;
import com.example.duantotnghiep.repository.VoucherRepository;
import com.example.duantotnghiep.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public List<Voucher> getAllVoucher() {
        return voucherRepository.findAll();
    }

    @Override
    public PageResponse<Voucher> getAllVoucherWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Voucher> voucherPage = voucherRepository.findAll(pageable);

        return new PageResponse<>(
                voucherPage.getContent(),
                page,
                voucherPage.getTotalPages(),
                voucherPage.getTotalElements(),
                size
        );
    }

    @Override
    public Voucher saveVoucher(Voucher voucher) {
        voucherRepository.save(voucher);
        voucher.setMaVoucher("VOUCHER0" + voucher.getId());
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher getVoucherById(int id) {
        return voucherRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteVoucher(int id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public List<Voucher> searchVoucher(String keyword) {
        return voucherRepository.searchVoucher(keyword);
    }

    @Override
    public PageResponse<Voucher> searchVoucherWithPagination(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Voucher> voucherPage = voucherRepository.searchVoucherWithPagination(keyword, pageable);

        return new PageResponse<>(
                voucherPage.getContent(),
                page,
                voucherPage.getTotalPages(),
                voucherPage.getTotalElements(),
                size
        );
    }

    @Override
    public boolean updateTrangThai(int id, boolean trangThai) {
        Voucher vc = voucherRepository.findById(id).orElse(null);
        if (vc == null) return false;
        vc.setTrangThai(trangThai);
        voucherRepository.save(vc);
        return true;
    }
}