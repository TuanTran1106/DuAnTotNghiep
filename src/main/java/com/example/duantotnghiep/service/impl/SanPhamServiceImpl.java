package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.entity.SanPham;
import com.example.duantotnghiep.entity.SanPhamChiTiet;
import com.example.duantotnghiep.repository.SanPhamChiTietRepository;
import com.example.duantotnghiep.repository.SanPhamRepository;
import com.example.duantotnghiep.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamServiceImpl implements SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Override
    public List<SanPham> getAllSanPham() {
        return sanPhamRepository.findAll();
    }

    @Override
    public SanPham getSanPhamById(Integer id) {
        return sanPhamRepository.findById(id).orElse(null);
    }

    @Override
    public SanPham addSanPham(SanPham sanPham) {
        if (sanPham.getMaSanPham() == null || sanPham.getMaSanPham().isEmpty()) {
            long count = sanPhamRepository.count() + 1;
            String newCode = String.format("SP%04d", count);
            sanPham.setMaSanPham(newCode);
        }
        return sanPhamRepository.save(sanPham);
    }

    @Override
    public SanPham updateSanPham(Integer id, SanPham sanPham) {
        Optional<SanPham> existing = sanPhamRepository.findById(id);
        if (existing.isPresent()) {
            SanPham sp = existing.get();
            sp.setTenSanPham(sanPham.getTenSanPham());
            sp.setHinhAnh(sanPham.getHinhAnh());
            sp.setGiaNhap(sanPham.getGiaNhap());
            sp.setNgayNhap(sanPham.getNgayNhap());
            sp.setNgaySua(sanPham.getNgaySua());
            sp.setMoTa(sanPham.getMoTa());
            sp.setTrangThai(sanPham.getTrangThai());
            sp.setThuongHieu(sanPham.getThuongHieu());
            sp.setDanhMuc(sanPham.getDanhMuc());
            return sanPhamRepository.save(sp);
        }
        return null;
    }

    @Override
    public void deleteSanPham(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    public List<SanPham> searchSanPham(String keyword) {
        return sanPhamRepository.findByTenSanPhamContainingIgnoreCaseOrMaSanPhamContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public List<SanPham> getByTrangThai(Boolean trangThai) {
        return sanPhamRepository.findByTrangThai(trangThai);
    }

    @Override
    public List<SanPham> searchByTrangThaiAndKeyword(Boolean trangThai, String keyword) {
        return sanPhamRepository.findByTrangThaiAndTenSanPhamContainingIgnoreCaseOrTrangThaiAndMaSanPhamContainingIgnoreCase(trangThai, keyword, trangThai, keyword);
    }

    @Override
    public String generateMaSanPham() {
        long count = sanPhamRepository.count() + 1;
        return String.format("SP%04d", count);
    }

    @Override
    public Page<SanPham> getAllSanPham(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }
    @Override
    public Page<SanPham> searchSanPham(String keyword, Pageable pageable) {
        return sanPhamRepository.searchByTenOrMa(keyword, pageable);
    }
    @Override
    public Page<SanPham> getByTrangThai(Boolean trangThai, Pageable pageable) {
        return sanPhamRepository.findByTrangThai(trangThai, pageable);
    }
    @Override
    public Page<SanPham> searchByTrangThaiAndKeyword(Boolean trangThai, String keyword, Pageable pageable) {
        return sanPhamRepository.findByTrangThaiAndTenSanPhamContainingIgnoreCaseOrTrangThaiAndMaSanPhamContainingIgnoreCase(trangThai, keyword, trangThai, keyword, pageable);
    }

    @Override
    public Page<SanPham> findByDanhMucIdAndThuongHieuId(Integer danhMucId, Integer thuongHieuId, Pageable pageable) {
        return sanPhamRepository.findByDanhMuc_IdAndThuongHieu_Id(danhMucId, thuongHieuId, pageable);
    }
    @Override
    public Page<SanPham> findByDanhMucId(Integer danhMucId, Pageable pageable) {
        return sanPhamRepository.findByDanhMuc_Id(danhMucId, pageable);
    }
    @Override
    public Page<SanPham> findByThuongHieuId(Integer thuongHieuId, Pageable pageable) {
        return sanPhamRepository.findByThuongHieu_Id(thuongHieuId, pageable);
    }

    @Override
    public List<SanPhamChiTiet> getChiTietBySanPhamId(Integer sanPhamId) {
        return sanPhamChiTietRepository.findBySanPham_Id(sanPhamId);
    }
} 