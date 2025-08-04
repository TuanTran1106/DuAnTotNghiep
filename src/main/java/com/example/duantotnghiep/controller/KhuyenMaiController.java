package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.KhuyenMai;
import com.example.duantotnghiep.entity.Voucher;
import com.example.duantotnghiep.service.KhuyenMaiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
    public String saveKhuyenMai(@Valid @ModelAttribute("khuyenMai") KhuyenMai khuyenMai, 
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            });
            redirectAttributes.addFlashAttribute("error", errorMessage.toString().trim());
            return "redirect:/khuyen-mai/add";
        }
        

        if (khuyenMai.getNgayKetThuc() != null && khuyenMai.getNgayKetThuc().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Ngày kết thúc không được là quá khứ. Vui lòng chọn ngày trong tương lai.");
            return "redirect:/khuyen-mai/add";
        }
        

        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null && 
            khuyenMai.getNgayBatDau().isAfter(khuyenMai.getNgayKetThuc())) {
            redirectAttributes.addFlashAttribute("error", "Ngày bắt đầu phải trước ngày kết thúc.");
            return "redirect:/khuyen-mai/add";
        }
        
        // Validation: Kiểm tra ngày bắt đầu không được là quá khứ
        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayBatDau().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Ngày bắt đầu không được là quá khứ. Vui lòng chọn ngày từ hôm nay trở đi.");
            return "redirect:/khuyen-mai/add";
        }
        
        if (khuyenMai.getId() == null) { // Thêm mới
            khuyenMai.setNgayTao(LocalDateTime.now());
        }
        
        try {
            khuyenMaiService.saveKhuyenMai(khuyenMai);
            redirectAttributes.addFlashAttribute("success", "Khuyến mãi đã được lưu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lưu khuyến mãi: " + e.getMessage());
        }
        
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
    

    @PostMapping("/api/validate-dates")
    @ResponseBody
    public Map<String, Object> validateDates(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        String ngayBatDauStr = request.get("ngayBatDau");
        String ngayKetThucStr = request.get("ngayKetThuc");
        
        LocalDate today = LocalDate.now();
        
        if (ngayBatDauStr != null && !ngayBatDauStr.isEmpty()) {
            try {
                LocalDate ngayBatDau = LocalDate.parse(ngayBatDauStr);
                if (ngayBatDau.isBefore(today)) {
                    errors.put("ngayBatDau", "Ngày bắt đầu không được là quá khứ");
                }
            } catch (Exception e) {
                errors.put("ngayBatDau", "Ngày bắt đầu không hợp lệ");
            }
        }
        
        if (ngayKetThucStr != null && !ngayKetThucStr.isEmpty()) {
            try {
                LocalDate ngayKetThuc = LocalDate.parse(ngayKetThucStr);
                if (ngayKetThuc.isBefore(today)) {
                    errors.put("ngayKetThuc", "Ngày kết thúc không được là quá khứ");
                }
                

                if (ngayBatDauStr != null && !ngayBatDauStr.isEmpty()) {
                    try {
                        LocalDate ngayBatDau = LocalDate.parse(ngayBatDauStr);
                        if (ngayKetThuc.isBefore(ngayBatDau) || ngayKetThuc.isEqual(ngayBatDau)) {
                            errors.put("ngayKetThuc", "Ngày kết thúc phải sau ngày bắt đầu");
                        }
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {
                errors.put("ngayKetThuc", "Ngày kết thúc không hợp lệ");
            }
        }
        
        response.put("valid", errors.isEmpty());
        response.put("errors", errors);
        
        return response;
    }
}
