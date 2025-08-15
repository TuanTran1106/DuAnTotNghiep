package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.entity.SanPham;
import com.example.duantotnghiep.entity.SanPhamChiTiet;
import com.example.duantotnghiep.repository.SanPhamChiTietRepository;
import com.example.duantotnghiep.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/san-pham-chi-tiet")
public class SanPhamChiTietController {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @GetMapping("")
    public String list(Model model) {
        List<SanPhamChiTiet> list = sanPhamChiTietRepository.findAll();
        model.addAttribute("listChiTiet", list);
        return "quan-tri/san-pham-chi-tiet";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sanPhamChiTiet", new SanPhamChiTiet());
        model.addAttribute("sanPhamList", sanPhamRepository.findAll());
        return "quan-tri/san-pham-chi-tiet-add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("sanPhamChiTiet") SanPhamChiTiet chiTiet,
                      @RequestParam("gia_ban") java.math.BigDecimal giaBan,
                      @RequestParam("soLuong") Integer soLuong,
                      Model model) {

        chiTiet.setGia_ban(giaBan);
        chiTiet.setSoLuong(soLuong);

        if (chiTiet.getSanPham() == null || chiTiet.getSanPham().getId() == null
            || chiTiet.getMauSac() == null || chiTiet.getKichThuoc() == null
            || chiTiet.getChatLieu() == null || chiTiet.getSoLuong() == null
            || chiTiet.getGia_ban() == null ) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            model.addAttribute("sanPhamList", sanPhamRepository.findAll());
            return "quan-tri/san-pham-chi-tiet-add";
        }
        sanPhamChiTietRepository.save(chiTiet);
        return "redirect:/san-pham-chi-tiet";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        SanPhamChiTiet chiTiet = sanPhamChiTietRepository.findById(id).orElse(null);
        if (chiTiet == null) {
            return "redirect:/san-pham-chi-tiet";
        }
        model.addAttribute("sanPhamChiTiet", chiTiet);
        model.addAttribute("sanPhamList", sanPhamRepository.findAll());
        return "quan-tri/san-pham-chi-tiet-add";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
                      @RequestParam("mauSac") String mauSac,
                      @RequestParam("kichThuoc") String kichThuoc,
                      @RequestParam("chatLieu") String chatLieu,
                      @RequestParam("soLuong") Integer soLuong,
                      @RequestParam("gia_ban") java.math.BigDecimal giaBan,
                      @RequestParam("trang_thai") Integer trangThai,
                      @RequestParam("sanPham.id") Integer sanPhamId,
                      Model model) {
        SanPhamChiTiet chiTiet = sanPhamChiTietRepository.findById(id).orElse(null);
        if (chiTiet == null) {
            return "redirect:/san-pham-chi-tiet";
        }
        chiTiet.setMauSac(mauSac);
        chiTiet.setKichThuoc(kichThuoc);
        chiTiet.setChatLieu(chatLieu);
        chiTiet.setSoLuong(soLuong);
        chiTiet.setGia_ban(giaBan);


        if (chiTiet.getSanPham() == null || !chiTiet.getSanPham().getId().equals(sanPhamId)) {
            SanPham sp = sanPhamRepository.findById(sanPhamId).orElse(null);
            chiTiet.setSanPham(sp);
        }
        sanPhamChiTietRepository.save(chiTiet);

        return "redirect:/san-pham/edit/" + sanPhamId;
    }
} 