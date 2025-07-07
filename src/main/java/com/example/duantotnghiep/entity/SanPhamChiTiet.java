package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "san_pham_chi_tiet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_san_pham")
    private SanPham sanPham;

    @Column(name = "mau_sac")
    private String mauSac;

    @Column(name = "kich_thuoc")
    private String kichThuoc;

    @Column(name = "chat_lieu")
    private String chatLieu;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "gia_ban")
    private BigDecimal gia_ban;

    @Column(name = "trang_thai")
    private Integer trang_thai;

}
