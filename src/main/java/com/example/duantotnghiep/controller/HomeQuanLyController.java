package com.example.duantotnghiep.controller;


import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.service.DonHangService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/quan-ly")
@AllArgsConstructor
public class HomeQuanLyController {

    private final DonHangService donHangService;

    @GetMapping("")
    public String L(Model model) {
        return "quan-tri/home-quan-ly";
    }

    @GetMapping("/don-hang")
    public String quanLyDonHang(Model model) {
        List<DonHangDto> orders = donHangService.getAllOrders();
        model.addAttribute("orders", orders);
        return "quan-tri/quan-ly-don-hang";
    }
    
    @GetMapping("/api/don-hang/{id}/san-pham")
    @ResponseBody
    public List<DonHangChiTietDto> getOrderProducts(@PathVariable Integer id) {
        return donHangService.getOrderProducts(id);
    }

}
