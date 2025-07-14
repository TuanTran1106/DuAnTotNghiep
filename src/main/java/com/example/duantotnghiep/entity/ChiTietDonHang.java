package com.example.duantotnghiep.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "chi_tiet_don_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_don_hang")
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "id_san_pham")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "so_luong_dat")
    private Integer soLuongDat;

    @Column(name = "don_gia")
    private BigDecimal donGia;


}
