package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.entity.NhanVien;
import com.example.duantotnghiep.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/nhan-vien")
public class NhanVienController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping
    public String listNhanVien(Model model) {
        model.addAttribute("listNhanVien", nhanVienService.getAllNhanVien());
        return "/quan-tri/nhan-vien";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("nhanVien", new NhanVien());
        return "/quan-tri/nhan-vien-add";
    }
    @PostMapping("/update-status")
    @ResponseBody
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> payload) {
        Integer id = Integer.valueOf(payload.get("id").toString());
        Boolean trangThai = Boolean.valueOf(payload.get("trangThai").toString());
        boolean result = nhanVienService.updateTrangThai(id, trangThai);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return response;
    }

    @PostMapping("/save")
    public String saveNhanVien(@ModelAttribute("nhanVien") NhanVien nhanVien) {
        nhanVienService.saveNhanVien(nhanVien);
        return "redirect:/nhan-vien";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        model.addAttribute("nhanVien", nhanVienService.getNhanVienById(id));
        return "/quan-tri/nhan-vien-update";
    }

//    @GetMapping("/delete/{id}")
//    public String deleteNhanVien(@PathVariable int id) {
//        nhanVienService.deleteNhanVien(id);
//        return "redirect:/nhan-vien";
//    }
    @GetMapping("/search")
    public String searchNhanVien(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<NhanVien> danhSachNhanVien;
        if (keyword != null && !keyword.isEmpty()) {
            danhSachNhanVien = nhanVienService.searchNhanVien(keyword);
        } else {
            danhSachNhanVien = nhanVienService.getAllNhanVien();
        }
        model.addAttribute("listNhanVien", danhSachNhanVien);
        model.addAttribute("keyword", keyword);
        return "/quan-tri/nhan-vien";
    }
}