package com.example.duantotnghiep.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_nguoi_dung")
    private String maNguoiDung;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "email")
    private String email;

    @Column(name = "mat_khau")
    private String matKhau;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_sua")
    private LocalDateTime ngaySua;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private PhanQuyen phanQuyen;
}

