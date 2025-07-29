package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.entity.DonHang;
import com.example.duantotnghiep.service.DonHangService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
@AllArgsConstructor
public class QuanLyDonHangController {
    private final DonHangService donHangService;

    @PatchMapping("/api/{id}/next-status")
    @ResponseBody
    public boolean nextOrderStatus(@PathVariable Integer id) {
        return donHangService.nextOrderStatus(id);
    }

    @GetMapping("/quan-ly/don-hang")
    public String quanLyDonHang(Model model) {
        List<DonHangDto> orders = donHangService.getAllOrders();
        model.addAttribute("orders", orders);
        return "quan-tri/quan-ly-don-hang";
    }

    @GetMapping("/quan-ly/api/don-hang/{id}/san-pham")
    @ResponseBody
    public List<DonHangChiTietDto> getOrderProducts(@PathVariable Integer id) {
        return donHangService.getOrderProducts(id);
    }

    @GetMapping("/quan-ly/don-hang/print")
    public void printOrdersToPDF(@RequestParam("ids") String ids, HttpServletResponse response) throws IOException {
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<DonHangDto> donHangs = donHangService.getOrderDtosByIds(idList);
        ByteArrayOutputStream baos = donHangService.generateOrdersPDFByDto(donHangs);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=hoa-don-don-hang.pdf");
        response.setContentLength(baos.size());
        baos.writeTo(response.getOutputStream());
        response.getOutputStream().flush();
    }

    @GetMapping("/quan-ly/don-hang/{id}/invoice")
    public void printInvoicePDF(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream baos = donHangService.generateInvoicePDF(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice-" + id + ".pdf");
        response.setContentLength(baos.size());
        baos.writeTo(response.getOutputStream());
        response.getOutputStream().flush();
    }

}
