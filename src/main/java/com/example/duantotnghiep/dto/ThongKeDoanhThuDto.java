package com.example.duantotnghiep.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeDoanhThuDto {

    private Date thoiGian;
    private BigDecimal tongDoanhThu;


}
