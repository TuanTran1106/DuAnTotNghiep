package com.example.duantotnghiep.dto;

import com.example.duantotnghiep.entity.SanPham;
import lombok.Data;
import java.util.List;

@Data
public class SanPhamAddDto {
    private SanPham sanPham;
    private List<SanPhamVariantDto> variants;
}