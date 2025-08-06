package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.DashboardThongKeDto;

import java.time.LocalDateTime;

public interface ThongKeDoanhThuService {

    DashboardThongKeDto thongKeDashboard(LocalDateTime from, LocalDateTime to);



}
