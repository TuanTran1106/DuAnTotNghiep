package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.DashboardThongKeDto;
import com.example.duantotnghiep.service.ThongKeDoanhThuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/quan-tri/thong-ke-doanh-thu")
@AllArgsConstructor
public class ThongKeDoanhThuController {

    private final ThongKeDoanhThuService thongKeDoanhThuService;




    @GetMapping("")
    public String thongKeDashboard(
            @RequestParam(value = "fromDate", required = false) String fromDateStr,
            @RequestParam(value = "toDate", required = false) String toDateStr,
            Model model) {
        LocalDateTime fromDate, toDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Nếu không truyền ngày, mặc định là 7 ngày gần nhất
        if ((fromDateStr == null || fromDateStr.isEmpty()) && (toDateStr == null || toDateStr.isEmpty())) {
            LocalDate now = LocalDate.now();
            toDate = now.atTime(LocalTime.MAX);
            fromDate = now.minusDays(29).atStartOfDay();
        } else {
            if (fromDateStr != null && !fromDateStr.isEmpty()) {
                fromDate = LocalDate.parse(fromDateStr, formatter).atStartOfDay();
            } else {
                LocalDate now = LocalDate.now();
                fromDate = now.withDayOfMonth(1).atStartOfDay();
            }
            if (toDateStr != null && !toDateStr.isEmpty()) {
                toDate = LocalDate.parse(toDateStr, formatter).atTime(LocalTime.MAX);
            } else {
                toDate = LocalDate.now().atTime(LocalTime.MAX);
            }
        }

        // Validate date range
        if (fromDate.isAfter(toDate)) {
            model.addAttribute("dateError", "Lỗi: Ngày bắt đầu không được lớn hơn ngày kết thúc.");
            model.addAttribute("dashboard", new DashboardThongKeDto());
            model.addAttribute("thongKeListJson", "[]");
            model.addAttribute("fromDate", fromDate.toLocalDate());
            model.addAttribute("toDate", toDate.toLocalDate());
            return "quan-tri/thong-ke-doanh-thu";
        }

        DashboardThongKeDto dashboard = thongKeDoanhThuService.thongKeDashboard(fromDate, toDate);
        String thongKeListJson = "[]";
        try {
            ObjectMapper mapper = new ObjectMapper();
            thongKeListJson = mapper.writeValueAsString(dashboard.getThongKeList());
        } catch (Exception e) {
            thongKeListJson = "[]";
        }
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("fromDate", fromDate.toLocalDate());
        model.addAttribute("toDate", toDate.toLocalDate());
        model.addAttribute("thongKeListJson", thongKeListJson);
        return "quan-tri/thong-ke-doanh-thu";
    }

}
