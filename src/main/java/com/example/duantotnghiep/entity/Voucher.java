package com.example.duantotnghiep.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_voucher")
    private String maVoucher;

    @Column(name = "ten_voucher")
    private String tenVoucher;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "kieu_giam")
    private Boolean kieuGiam;

    @Column(name = "muc_giam")
    private BigDecimal mucGiam;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "so_luong_con")
    private Integer soLuongCon;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "dieu_kien_toi_thieu")
    private BigDecimal dieuKienToiThieu;

    @Column(name = "trang_thai")
    private Boolean trangThai;
}
