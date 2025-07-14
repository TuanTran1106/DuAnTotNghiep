package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phuong_thuc_thanh_toan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_phuong_thuc")
    private String tenPhuongThuc;
}

