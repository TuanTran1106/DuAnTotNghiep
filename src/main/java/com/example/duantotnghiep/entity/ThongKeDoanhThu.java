package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "thong_ke_doanh_thu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeDoanhThu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer thang;

    private Integer nam;

    private Integer soLuongBan;

    private Double doanhThu;

    @ManyToOne
    @JoinColumn(name = "id_san_pham_chi_tiet")
    private SanPhamChiTiet sanPhamChiTiet;
}
