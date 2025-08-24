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

import java.time.*;
import java.time.temporal.IsoFields;

@Controller
@RequestMapping("/quan-tri/thong-ke-doanh-thu")
@AllArgsConstructor
public class ThongKeDoanhThuController {

    private final ThongKeDoanhThuService thongKeDoanhThuService;

    @GetMapping("")
    public String thongKeDashboard(
            @RequestParam(value = "filterType", defaultValue = "month") String filterType,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "week", required = false) Integer week,
            @RequestParam(value = "year", required = false) Integer year,
            Model model) {
        LocalDateTime fromDate, toDate;

        // Default to current year if not provided
        if (year == null) {
            year = YearMonth.now().getYear();
        }

        // Log parameters for debugging
        System.out.println("Filter Type: " + filterType + ", Week: " + week + ", Month: " + month + ", Year: " + year);

        // Validate year
        if (year < 1900 || year > YearMonth.now().getYear()) {
            model.addAttribute("dateError", "Lỗi: Năm không hợp lệ.");
            model.addAttribute("dashboard", new DashboardThongKeDto());
            model.addAttribute("thongKeListJson", "[]");
            model.addAttribute("filterType", filterType);
            model.addAttribute("month", month);
            model.addAttribute("week", week);
            model.addAttribute("year", year);
            return "quan-tri/thong-ke-doanh-thu";
        }

        if (filterType.equals("week")) {
            // Default to current week if not provided
            if (week == null) {
                LocalDate today = LocalDate.now();
                week = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                if (year != YearMonth.now().getYear()) {
                    // Adjust for different year
                    LocalDate lastDayOfYear = LocalDate.of(year, 12, 31);
                    week = Math.min(week, lastDayOfYear.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
                }
            }

            // Validate week
            if (week < 1 || week > 53) {
                model.addAttribute("dateError", "Lỗi: Tuần không hợp lệ.");
                model.addAttribute("dashboard", new DashboardThongKeDto());
                model.addAttribute("thongKeListJson", "[]");
                model.addAttribute("filterType", filterType);
                model.addAttribute("month", month);
                model.addAttribute("week", week);
                model.addAttribute("year", year);
                return "quan-tri/thong-ke-doanh-thu";
            }

            // Calculate the first day of the year
            LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
            // Find the first Monday of the year
            LocalDate firstMonday = firstDayOfYear.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, 1)
                    .with(DayOfWeek.MONDAY);
            // Calculate the start of the target week
            LocalDate weekStart = firstMonday.plusWeeks(week - 1);
            // Calculate the end of the week (Sunday)
            LocalDate weekEnd = weekStart.plusDays(6);

            // Ensure the week doesn't exceed the year
            if (weekEnd.getYear() > year) {
                weekEnd = LocalDate.of(year, 12, 31);
            }

            fromDate = weekStart.atStartOfDay();
            toDate = weekEnd.atTime(LocalTime.MAX);

            // Log date range for debugging
            System.out.println("Week Filter - From Date: " + fromDate + ", To Date: " + toDate);
        } else {
            // Month filter
            if (month == null) {
                month = YearMonth.now().getMonthValue();
            }
            if (month < 1 || month > 12) {
                model.addAttribute("dateError", "Lỗi: Tháng không hợp lệ.");
                model.addAttribute("dashboard", new DashboardThongKeDto());
                model.addAttribute("thongKeListJson", "[]");
                model.addAttribute("filterType", filterType);
                model.addAttribute("month", month);
                model.addAttribute("week", week);
                model.addAttribute("year", year);
                return "quan-tri/thong-ke-doanh-thu";
            }

            YearMonth yearMonth = YearMonth.of(year, month);
            fromDate = yearMonth.atDay(1).atStartOfDay();
            toDate = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

            // Log date range for debugging
            System.out.println("Month Filter - From Date: " + fromDate + ", To Date: " + toDate);
        }

        // Fetch dashboard data
        DashboardThongKeDto dashboard = thongKeDoanhThuService.thongKeDashboard(fromDate, toDate);
        String thongKeListJson = "[]";
        try {
            ObjectMapper mapper = new ObjectMapper();
            thongKeListJson = mapper.writeValueAsString(dashboard.getThongKeList());
            // Log dashboard data for debugging
            System.out.println("Dashboard Data: " + dashboard.getThongKeList());
        } catch (Exception e) {
            System.out.println("Error serializing thongKeList: " + e.getMessage());
            thongKeListJson = "[]";
        }

        // Add attributes to model
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("filterType", filterType);
        model.addAttribute("month", month);
        model.addAttribute("week", week);
        model.addAttribute("year", year);
        model.addAttribute("thongKeListJson", thongKeListJson);
        return "quan-tri/thong-ke-doanh-thu";
    }
}