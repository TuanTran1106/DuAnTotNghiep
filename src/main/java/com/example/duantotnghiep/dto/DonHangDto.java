package com.example.duantotnghiep.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DonHangDto {

    private Integer id;

    private String maDonHang;

    private String tenNguoiDung;

    private BigDecimal tongGia;

    private LocalDateTime ngayMua;

    private String tenTrangThai;

    private String tenPhuongThucThanhToan;

    public DonHangDto(Integer id, String maDonHang, String tenNguoiDung, BigDecimal tongGia,
                      LocalDateTime ngayMua, String tenTrangThai, String tenPhuongThucThanhToan) {
        this.id = id;
        this.maDonHang = maDonHang;
        this.tenNguoiDung = tenNguoiDung;
        this.tongGia = tongGia;
        this.ngayMua = ngayMua;
        this.tenTrangThai = tenTrangThai;
        this.tenPhuongThucThanhToan = tenPhuongThucThanhToan;
    }


}
