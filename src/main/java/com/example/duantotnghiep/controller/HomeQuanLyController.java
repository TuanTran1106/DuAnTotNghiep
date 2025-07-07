package com.example.duantotnghiep.controller;


import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.service.DonHangService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/trang-chu")
    public String home(Model model) {
        return "quan-tri/trang-chu";
    }

    @GetMapping("/don-hang")
    public String getOrder (Model model) {
        model.addAttribute("order",donHangService.findOrder());
        return "quan-tri/quan-ly-don-hang";
    }

    @GetMapping("/chi-tiet/{id}")
    @ResponseBody
    public List<DonHangChiTietDto> getOrderDetails(@PathVariable Integer id) {
        return donHangService.findChiTietByDonHangId(id);
    }


}
