package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.dto.PageResponse;
import com.example.duantotnghiep.entity.NguoiDung;
import com.example.duantotnghiep.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/nguoi-dung")
public class NguoiDungController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping
    public String listNguoiDung(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        PageResponse<NguoiDung> pageResponse = nguoiDungService.getAllNguoiDungWithPagination(page, size);
        model.addAttribute("listNguoiDung", pageResponse.getContent());
        model.addAttribute("currentPage", pageResponse.getCurrentPage());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("pageSize", pageResponse.getPageSize());
        model.addAttribute("hasNext", pageResponse.isHasNext());
        model.addAttribute("hasPrevious", pageResponse.isHasPrevious());
        return "/quan-tri/nguoi-dung";
    }

    @PostMapping("/save")
    public String saveNguoiDung(@ModelAttribute("nguoiDung") NguoiDung nguoiDung,
                                @RequestParam("hinhAnh") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads", fileName);
                Files.createDirectories(filePath.getParent());
                file.transferTo(filePath.toFile());

                nguoiDung.setHinhAnh(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        nguoiDungService.saveNguoiDung(nguoiDung);
        return "redirect:/nguoi-dung";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        NguoiDung nd = nguoiDungService.getNguoiDungById(id);
        model.addAttribute("nguoiDung", nguoiDungService.getNguoiDungById(id));
        return "/quan-tri/nguoi-dung-update";
    }

    @PostMapping("/update")
    public String updateNguoiDung(
            @ModelAttribute("nguoiDung") NguoiDung nguoiDung,
            BindingResult result,
            @RequestParam("fileUpload") MultipartFile file) {

        try {
            if (!file.isEmpty()) {
                String uploadDir = "E:/DuAnTotNghiep/uploads";
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path filePath = Paths.get(uploadDir, fileName);

                file.transferTo(filePath.toFile());

                nguoiDung.setHinhAnh(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        nguoiDungService.saveNguoiDung(nguoiDung);
        return "redirect:/nguoi-dung";
    }

    @PostMapping("/update-status")
    @ResponseBody
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> payload) {
        Integer id = Integer.valueOf(payload.get("id").toString());
        Boolean trangThai = Boolean.valueOf(payload.get("trangThai").toString());
        boolean result = nguoiDungService.updateTrangThai(id, trangThai);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return response;
    }

    @GetMapping("/search")
    public String searchNguoiDung(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        PageResponse<NguoiDung> pageResponse;
        if (keyword != null && !keyword.isEmpty()) {
            pageResponse = nguoiDungService.searchNguoiDungWithPagination(keyword, page, size);
        } else {
            pageResponse = nguoiDungService.getAllNguoiDungWithPagination(page, size);
        }

        model.addAttribute("listNguoiDung", pageResponse.getContent());
        model.addAttribute("currentPage", pageResponse.getCurrentPage());
        model.addAttribute("totalPages", pageResponse.getTotalPages());
        model.addAttribute("totalElements", pageResponse.getTotalElements());
        model.addAttribute("pageSize", pageResponse.getPageSize());
        model.addAttribute("hasNext", pageResponse.isHasNext());
        model.addAttribute("hasPrevious", pageResponse.isHasPrevious());
        model.addAttribute("keyword", keyword);
        return "/quan-tri/nguoi-dung";
    }

    @GetMapping("/uploads/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Path file = Paths.get("uploads").resolve(fileName);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok().body(resource);
        } else {
            throw new RuntimeException("Không thể đọc file: " + fileName);
        }
    }
}
