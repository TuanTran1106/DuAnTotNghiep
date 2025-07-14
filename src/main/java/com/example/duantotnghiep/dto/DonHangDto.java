package com.example.duantotnghiep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonHangDto {


    private Integer donHangId;

    private String maDonHang;

    private  LocalDateTime ngayMua;

    private String tenKhachHang;

    private String sdtKhachHang;

    private  String tenTrangThai;

    private  String tenPhuongThuc;

    private  String tenNhanVien;

    private  Long soLuongDat;

    private  BigDecimal donGia;

    private  BigDecimal thanhTien;

    private  String voucher;
    private  String tenVoucher;

    private  String diaChi;

    private BigDecimal thanhTienSauVoucher;

    public DonHangDto(Integer donHangId, String maDonHang, LocalDateTime ngayMua, String tenKhachHang, String tenTrangThai, String tenPhuongThuc, String tenNhanVien, Long soLuongDat, BigDecimal donGia, BigDecimal thanhTien, String voucher, String tenVoucher, String diaChi, BigDecimal thanhTienSauVoucher, String sdtKhachHang) {
        this.donHangId = donHangId;
        this.maDonHang = maDonHang;
        this.ngayMua = ngayMua;
        this.tenKhachHang = tenKhachHang;
        this.tenTrangThai = tenTrangThai;
        this.tenPhuongThuc = tenPhuongThuc;
        this.tenNhanVien = tenNhanVien;
        this.soLuongDat = soLuongDat;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.voucher = voucher;
        this.tenVoucher = tenVoucher;
        this.diaChi = diaChi;
        this.thanhTienSauVoucher = thanhTienSauVoucher;
        this.sdtKhachHang = sdtKhachHang;
    }

}
