package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "san_pham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_san_pham")
    private String maSanPham;

    @Column(name = "ten_san_pham")
    private String tenSanPham;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column(name = "gia_nhap")
    private BigDecimal giaNhap;

    @Column(name = "ngay_nhap")
    private LocalDateTime ngayNhap;

    @Column(name = "ngay_sua")
    private LocalDateTime ngaySua;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu")
    private ThuongHieu thuongHieu;

    @ManyToOne
    @JoinColumn(name = "id_danh_muc")
    private DanhMuc danhMuc;
}
