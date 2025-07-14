package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.service.DonHangService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Controller
@RequestMapping("/quan-ly/don-hang")
@AllArgsConstructor
public class QuanLyDonHangController {
    private final DonHangService donHangService;

    @PatchMapping("/api/{id}/next-status")
    @ResponseBody
    public boolean nextOrderStatus(@PathVariable Integer id) {
        return donHangService.nextOrderStatus(id);
    }


}
