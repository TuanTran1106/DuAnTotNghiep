package com.example.duantotnghiep.dto;

import com.example.duantotnghiep.entity.SanPham;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class SanPhamEditDto {
    private SanPham sanPham;
    private BigDecimal giaBan;
    private Integer soLuong;
    private String mauSac;
    private String kichThuoc;
    private String chatLieu;
    private List<SanPhamVariantDto> variants;
} 