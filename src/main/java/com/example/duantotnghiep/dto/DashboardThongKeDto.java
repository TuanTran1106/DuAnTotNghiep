package com.example.duantotnghiep.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class DashboardThongKeDto {

    private Long tongSoDon;
    private BigDecimal tongDoanhThu;
    private Long tongSanPhamBan;
    private Long soKhachHangMoi;
    private List<ThongKeDoanhThuDto> thongKeList;
    private List<SanPhamBanChayDto> sanPhamBanChayList;

}
