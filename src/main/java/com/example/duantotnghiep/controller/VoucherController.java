package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NhanVien;
import com.example.duantotnghiep.entity.Voucher;
import com.example.duantotnghiep.service.NhanVienService;
import com.example.duantotnghiep.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
    public String saveVoucher(@Valid @ModelAttribute("voucher") Voucher voucher, 
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        // Kiểm tra Bean Validation errors
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            });
            redirectAttributes.addFlashAttribute("error", errorMessage.toString().trim());
            return "redirect:/voucher/add";
        }
        
        // Custom validation logic
        if (voucher.getNgayKetThuc() != null && voucher.getNgayKetThuc().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Ngày kết thúc không được là quá khứ. Vui lòng chọn ngày trong tương lai.");
            return "redirect:/voucher/add";
        }
        
        // Validation: Kiểm tra ngày bắt đầu phải trước ngày kết thúc
        if (voucher.getNgayBatDau() != null && voucher.getNgayKetThuc() != null && 
            voucher.getNgayBatDau().isAfter(voucher.getNgayKetThuc())) {
            redirectAttributes.addFlashAttribute("error", "Ngày bắt đầu phải trước ngày kết thúc.");
            return "redirect:/voucher/add";
        }
        
        // Validation: Kiểm tra ngày bắt đầu không được là quá khứ
        if (voucher.getNgayBatDau() != null && voucher.getNgayBatDau().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Ngày bắt đầu không được là quá khứ. Vui lòng chọn ngày từ hôm nay trở đi.");
            return "redirect:/voucher/add";
        }
        
        try {
            voucherService.saveVoucher(voucher);
            redirectAttributes.addFlashAttribute("success", "Voucher đã được lưu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lưu voucher: " + e.getMessage());
        }
        
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
    
    /**
     * REST API để validate ngày real-time
     */
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
                
                // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
                if (ngayBatDauStr != null && !ngayBatDauStr.isEmpty()) {
                    try {
                        LocalDate ngayBatDau = LocalDate.parse(ngayBatDauStr);
                        if (ngayKetThuc.isBefore(ngayBatDau) || ngayKetThuc.isEqual(ngayBatDau)) {
                            errors.put("ngayKetThuc", "Ngày kết thúc phải sau ngày bắt đầu");
                        }
                    } catch (Exception e) {
                        // Ignore if ngayBatDau is invalid
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