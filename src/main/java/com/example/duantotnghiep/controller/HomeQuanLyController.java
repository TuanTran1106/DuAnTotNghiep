package com.example.duantotnghiep.controller;


import com.example.duantotnghiep.service.DonHangService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quan-ly")
@AllArgsConstructor
public class HomeQuanLyController {

    private final DonHangService donHangService;

    @GetMapping("")
    public String L(Model model, HttpSession session) {
        Object tenDangNhap = session.getAttribute("tenDangNhap");
        if (tenDangNhap != null) {
            model.addAttribute("tenDangNhap", tenDangNhap.toString());
        } else {
            model.addAttribute("tenDangNhap", "Admin");
        }
        return "quan-tri/home-quan-ly";
    }

//    ---------------------------------------------
    @GetMapping("/thong-ke-doanh-thu")
    public String thongKeDoanhThu(Model model) {
        return "quan-tri/thong-ke-doanh-thu";
    }
//    ---------------------------------------------

}
