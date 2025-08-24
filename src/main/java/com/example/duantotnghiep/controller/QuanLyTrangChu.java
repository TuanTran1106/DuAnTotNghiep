package com.example.duantotnghiep.controller;


import com.example.duantotnghiep.service.DonHangService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/trang-chu")
@AllArgsConstructor
public class QuanLyTrangChu {

    private final DonHangService donHangService;

    @GetMapping("")
    public String showLoginForm(Model model) {
        Long choXacNhan = donHangService.countByTrangThai("Chờ xác nhận");
        Long dangChuanBi = donHangService.countByTrangThai("Đang chuẩn bị hàng");
        model.addAttribute("soDonChoXacNhan", choXacNhan);
        model.addAttribute("soDonDangChuanBi", dangChuanBi);
        return "quan-tri/trang-chu";
    }

}
