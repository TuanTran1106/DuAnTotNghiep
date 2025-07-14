package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trang_thai_don_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrangThaiDonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_trang_thai")
    private String tenTrangThai;
}
