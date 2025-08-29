package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.KhuyenMai;
import com.example.duantotnghiep.entity.SanPham;
import com.example.duantotnghiep.repository.SanPhamChiTietRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/khuyen-mai")
public class KhuyenMaiController {
    @Autowired
    private KhuyenMaiService khuyenMaiService;

    @Autowired
    private SanPhamChiTietRepository spctRepository;

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
        model.addAttribute("listSpct", spctRepository.findAll());
        return "quan-tri/khuyen-mai-add";
    }
    @PostMapping("/create")
    public String createKhuyenMai(@ModelAttribute KhuyenMai khuyenMai,
                                  @RequestParam("spctIds") List<Integer> spctIds) {
        khuyenMaiService.saveKhuyenMai(khuyenMai, spctIds);
        return "redirect:/khuyen-mai";
    }
    
    @PostMapping("/save")
    public String saveKhuyenMai(@ModelAttribute("khuyenMai") KhuyenMai khuyenMai,
                                @RequestParam(value = "spctIds", required = false) List<Integer> spctIds,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        // Set giá trị mặc định cho maKhuyenMai nếu là tạo mới
        if (khuyenMai.getId() == null && (khuyenMai.getMaKhuyenMai() == null || khuyenMai.getMaKhuyenMai().trim().isEmpty())) {
            khuyenMai.setMaKhuyenMai("TEMP_KM");
        }

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            });
            redirectAttributes.addFlashAttribute("error", errorMessage.toString().trim());
            if (khuyenMai.getId() != null) {
                return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
            }
            return "redirect:/khuyen-mai/add";
        }
        

        if (khuyenMai.getNgayKetThuc() != null && khuyenMai.getNgayKetThuc().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Ngày kết thúc không được là quá khứ. Vui lòng chọn ngày trong tương lai.");
            if (khuyenMai.getId() != null) {
                return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
            }
            return "redirect:/khuyen-mai/add";
        }
        

        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null && 
            khuyenMai.getNgayBatDau().isAfter(khuyenMai.getNgayKetThuc())) {
            redirectAttributes.addFlashAttribute("error", "Ngày bắt đầu không được sau ngày kết thúc.");
            if (khuyenMai.getId() != null) {
                return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
            }
            return "redirect:/khuyen-mai/add";
        }
        
        // Kiểm tra ngày bắt đầu khi TẠO MỚI (không cho chọn quá khứ)
        if (khuyenMai.getId() == null) {
            if (khuyenMai.getNgayBatDau() == null) {
                redirectAttributes.addFlashAttribute("error", "Ngày bắt đầu không được để trống.");
                return "redirect:/khuyen-mai/add";
            }
            if (khuyenMai.getNgayBatDau().isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "Ngày bắt đầu không được là quá khứ. Vui lòng chọn ngày từ hôm nay trở đi.");
                return "redirect:/khuyen-mai/add";
            }
        }

        // Bắt buộc chọn ít nhất 1 sản phẩm áp dụng
        if (spctIds == null || spctIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ít nhất một sản phẩm chi tiết áp dụng.");
            if (khuyenMai.getId() != null) {
                return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
            }
            return "redirect:/khuyen-mai/add";
        }
        
        // Ngày tạo sẽ được tự động set trong service


        
        try {
            khuyenMaiService.saveKhuyenMai(khuyenMai, spctIds);
            redirectAttributes.addFlashAttribute("success", "Khuyến mãi đã được lưu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lưu khuyến mãi: " + e.getMessage());
            if (khuyenMai.getId() != null) {
                return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
            }
            return "redirect:/khuyen-mai/add";
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
        KhuyenMai khuyenMai = khuyenMaiService.getKhuyenMaiById(id);
        if (khuyenMai == null) {
            return "redirect:/khuyen-mai";
        }
        model.addAttribute("khuyenMai", khuyenMai);
        model.addAttribute("listSpct", spctRepository.findAll());
        return "/quan-tri/khuyen-mai-update";
    }
    
    @PostMapping("/update")
    public String updateKhuyenMai(@Valid @ModelAttribute("khuyenMai") KhuyenMai khuyenMai,
                                  @RequestParam(value = "spctIds", required = false) List<Integer> spctIds,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            });
            redirectAttributes.addFlashAttribute("error", errorMessage.toString().trim());
            return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
        }
        
        // Custom validation logic
        if (khuyenMai.getNgayKetThuc() != null && khuyenMai.getNgayKetThuc().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Ngày kết thúc không được là quá khứ. Vui lòng chọn ngày trong tương lai.");
            return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
        }
        
        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null && 
            khuyenMai.getNgayBatDau().isAfter(khuyenMai.getNgayKetThuc())) {
            redirectAttributes.addFlashAttribute("error", "Ngày bắt đầu không được sau ngày kết thúc.");
            return "redirect:/khuyenMai/update/" + khuyenMai.getId();
        }
        
        // Bỏ validation ngày bắt đầu khi UPDATE (giữ nguyên ngày cũ)
        
        if (spctIds == null || spctIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ít nhất một sản phẩm chi tiết áp dụng.");
            return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
        }
        
        try {
            khuyenMaiService.saveKhuyenMai(khuyenMai, spctIds);
            redirectAttributes.addFlashAttribute("success", "Khuyến mãi đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật khuyến mãi: " + e.getMessage());
            return "redirect:/khuyen-mai/update/" + khuyenMai.getId();
        }
        
        return "redirect:/khuyen-mai";
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
                        if (ngayKetThuc.isBefore(ngayBatDau)) {
                            errors.put("ngayKetThuc", "Ngày kết thúc không được trước ngày bắt đầu");
                        }
                    } catch (Exception e) {
                        // ignore, đã có lỗi ở trên nếu parse thất bại
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
