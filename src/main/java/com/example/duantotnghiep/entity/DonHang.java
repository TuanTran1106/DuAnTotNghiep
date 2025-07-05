package com.example.duantotnghiep.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "don_hang")
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_don_hang")
    private String maDonHang;

    @Column(name = "tong_gia")
    private BigDecimal tongGia;

    @Column(name = "ngay_mua")
    private LocalDateTime ngayMua;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_sua")
    private LocalDateTime ngaySua;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "id_trang_thai")
    private TrangThaiDonHang trangThaiDonHang;

    @ManyToOne
    @JoinColumn(name = "id_phuong_thuc")
    private PhuongThucThanhToan phuongThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien")
    private NhanVien nhanVien;

    @Column(name = "id_dia_chi")
    private Integer idDiaChi;
}

