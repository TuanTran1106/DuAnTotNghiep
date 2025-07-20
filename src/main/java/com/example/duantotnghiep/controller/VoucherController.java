package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NhanVien;
import com.example.duantotnghiep.entity.Voucher;
import com.example.duantotnghiep.service.NhanVienService;
import com.example.duantotnghiep.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public String listVoucher(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        PageResponse<Voucher> pageResponse = voucherService.getAllVoucherWithPagination(page, size);
        model.addAttribute("listVoucher", pageResponse.getContent());
        model.addAttribute("currentPage", pageResponse.getCurrentPage());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("pageSize", pageResponse.getPageSize());
        model.addAttribute("hasNext", pageResponse.isHasNext());
        model.addAttribute("hasPrevious", pageResponse.isHasPrevious());
        return "/quan-tri/voucher";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        return "quan-tri/voucher-add";
    }
    @PostMapping("/save")
    public String saveVoucher(@ModelAttribute("voucher") Voucher voucher) {
        voucherService.saveVoucher(voucher);
        return "redirect:/voucher";
    }

    @PostMapping("/update-status")
    @ResponseBody
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> payload) {
        Integer id = Integer.valueOf(payload.get("id").toString());
        Boolean trangThai = Boolean.valueOf(payload.get("trangThai").toString());
        boolean result = voucherService.updateTrangThai(id, trangThai);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return response;
    }


    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        model.addAttribute("voucher", voucherService.getVoucherById(id));
        return "/quan-tri/voucher-update";
    }

    @GetMapping("/search")
    public String searchVoucher(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        PageResponse<Voucher> pageResponse;
        if (keyword != null && !keyword.isEmpty()) {
            pageResponse = voucherService.searchVoucherWithPagination(keyword, page, size);
        } else {
            pageResponse = voucherService.getAllVoucherWithPagination(page, size);
        }

        model.addAttribute("listVoucher", pageResponse.getContent());
        model.addAttribute("currentPage", pageResponse.getCurrentPage());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("pageSize", pageResponse.getPageSize());
        model.addAttribute("hasNext", pageResponse.isHasNext());
        model.addAttribute("hasPrevious", pageResponse.isHasPrevious());
        model.addAttribute("keyword", keyword);
        return "/quan-tri/voucher";
    }
}