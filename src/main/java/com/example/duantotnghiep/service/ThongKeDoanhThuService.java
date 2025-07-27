package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.DashboardThongKeDto;
import com.example.duantotnghiep.entity.ThongKeDoanhThu;
import com.example.duantotnghiep.dto.ThongKeDoanhThuDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ThongKeDoanhThuService {

    DashboardThongKeDto thongKeDashboard(LocalDateTime from, LocalDateTime to);



}
