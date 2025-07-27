package com.example.duantotnghiep.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamBanChayDto {

    private String tenSanPham;
    private Long soLuongBan;
    private BigDecimal doanhThu;
    private Integer tonKho;

}
