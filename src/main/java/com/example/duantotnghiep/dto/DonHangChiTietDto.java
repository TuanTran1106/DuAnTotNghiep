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
public class DonHangChiTietDto {

    private Integer chiTietId;

    private Integer sanPhamChiTietId;

    private String maSanPham;

    private String tenSanPham;

    private String hinhAnh;

    private String mauSac;

    private String kichThuoc;

    private String chatLieu;

    private Integer soLuongDat;

    private BigDecimal donGia;

    private BigDecimal thanhTien;


}
