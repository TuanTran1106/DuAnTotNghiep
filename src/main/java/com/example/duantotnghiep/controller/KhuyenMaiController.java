package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.KhuyenMai;
import com.example.duantotnghiep.entity.Voucher;
import com.example.duantotnghiep.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/khuyen-mai")
public class KhuyenMaiController {
    @Autowired
    private KhuyenMaiService khuyenMaiService;

    @GetMapping
    public String listKhuyenMai(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        PageResponse<KhuyenMai> pageResponse = khuyenMaiService.getAllKhuyenMaiWithPagination(page, size);
        model.addAttribute("listKhuyenMai", pageResponse.getContent());
        model.addAttribute("currentPage", pageResponse.getCurrentPage());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("pageSize", pageResponse.getPageSize());
        model.addAttribute("hasNext", pageResponse.isHasNext());
        model.addAttribute("hasPrevious", pageResponse.isHasPrevious());
        return "/quan-tri/khuyen-mai";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("khuyenMai", new KhuyenMai());
        return "quan-tri/khuyen-mai-add";
    }
    @PostMapping("/save")
    public String saveKhuyenMai(@ModelAttribute("khuyenMai") KhuyenMai khuyenMai) {
        if (khuyenMai.getId() == null) { // Thêm mới
            khuyenMai.setNgayTao(LocalDateTime.now());
        }
        khuyenMaiService.saveKhuyenMai(khuyenMai);
        return "redirect:/khuyen-mai";
    }

    @PostMapping("/update-status")
    @ResponseBody
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> payload) {
        Integer id = Integer.valueOf(payload.get("id").toString());
        Integer trangThai = Integer.valueOf(payload.get("trangThai").toString());
        boolean result = khuyenMaiService.updateTrangThai(id, trangThai);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return response;
    }


    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        model.addAttribute("khuyenMai", khuyenMaiService.getKhuyenMaiById(id));
        return "/quan-tri/khuyen-mai-update";
    }

    @GetMapping("/search")
    public String searchVoucher(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        PageResponse<KhuyenMai> pageResponse;
        if (keyword != null && !keyword.isEmpty()) {
            pageResponse = khuyenMaiService.searchKhuyenMaiWithPagination(keyword, page, size);
        } else {
            pageResponse = khuyenMaiService.getAllKhuyenMaiWithPagination(page, size);
        }

        model.addAttribute("listKhuyenMai", pageResponse.getContent());
        model.addAttribute("currentPage", pageResponse.getCurrentPage());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("pageSize", pageResponse.getPageSize());
        model.addAttribute("hasNext", pageResponse.isHasNext());
        model.addAttribute("hasPrevious", pageResponse.isHasPrevious());
        model.addAttribute("keyword", keyword);
        return "/quan-tri/khuyen-mai";
    }
}
