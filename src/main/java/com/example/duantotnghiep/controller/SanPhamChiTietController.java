package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.entity.SanPham;
import com.example.duantotnghiep.entity.SanPhamChiTiet;
import com.example.duantotnghiep.repository.SanPhamChiTietRepository;
import com.example.duantotnghiep.repository.SanPhamRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;

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
    public String showAddForm(@RequestParam(value = "sanPhamId", required = false) Integer sanPhamId, Model model) {
        SanPhamChiTiet chiTiet = new SanPhamChiTiet();
        if (sanPhamId != null) {
            SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElse(null);
            if (sanPham != null) {
                chiTiet.setSanPham(sanPham);
            }
        }
        model.addAttribute("sanPhamChiTiet", chiTiet);
        model.addAttribute("sanPhamList", sanPhamRepository.findAll());
        return "quan-tri/san-pham-chi-tiet-add";
    }

    @PostMapping("/add")
    public String add(@RequestParam(value = "sanPham.id", required = false) Integer sanPhamId,
                      @RequestParam(value = "mauSac", required = false) String mauSac,
                      @RequestParam(value = "kichThuoc", required = false) String kichThuoc,
                      @RequestParam(value = "chatLieu", required = false) String chatLieu,
                      @RequestParam(value = "gia_ban", required = false) String giaBanStr,
                      @RequestParam(value = "soLuong", required = false) String soLuongStr,
                      Model model,
                      RedirectAttributes redirectAttributes) {

        // Tạo đối tượng SanPhamChiTiet từ RequestParam
        SanPhamChiTiet chiTiet = new SanPhamChiTiet();
        
        // Lấy sản phẩm
        if (sanPhamId != null) {
            SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElse(null);
            if (sanPham != null) {
                chiTiet.setSanPham(sanPham);
            }
        }
        
        // Set các thuộc tính
        chiTiet.setMauSac(mauSac);
        chiTiet.setKichThuoc(kichThuoc);
        chiTiet.setChatLieu(chatLieu);
        
        // Xử lý giá bán và số lượng từ RequestParam
        try {
            if (giaBanStr != null && !giaBanStr.trim().isEmpty()) {
                chiTiet.setGia_ban(new BigDecimal(giaBanStr));
            }
            if (soLuongStr != null && !soLuongStr.trim().isEmpty()) {
                chiTiet.setSoLuong(Integer.parseInt(soLuongStr));
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Giá bán hoặc số lượng không hợp lệ!");
            model.addAttribute("sanPhamList", sanPhamRepository.findAll());
            return "quan-tri/san-pham-chi-tiet-add";
        }
        
        // Debug logging
        System.out.println("=== DEBUG: Thêm chi tiết sản phẩm ===");
        System.out.println("SanPham ID: " + (chiTiet.getSanPham() != null ? chiTiet.getSanPham().getId() : "null"));
        System.out.println("Màu sắc: " + chiTiet.getMauSac());
        System.out.println("Kích thước: " + chiTiet.getKichThuoc());
        System.out.println("Chất liệu: " + chiTiet.getChatLieu());
        System.out.println("Số lượng: " + chiTiet.getSoLuong());
        System.out.println("Giá bán: " + chiTiet.getGia_ban());
        System.out.println("GiaBanStr: " + giaBanStr);
        System.out.println("SoLuongStr: " + soLuongStr);
        System.out.println("================================");
        
        // Validation chi tiết
        if (chiTiet.getSanPham() == null || chiTiet.getSanPham().getId() == null) {
            model.addAttribute("error", "Vui lòng chọn sản phẩm!");
            model.addAttribute("sanPhamList", sanPhamRepository.findAll());
            model.addAttribute("sanPhamChiTiet", chiTiet);
            return "quan-tri/san-pham-chi-tiet-add";
        }
        if (chiTiet.getMauSac() == null || chiTiet.getMauSac().trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập màu sắc!");
            model.addAttribute("sanPhamList", sanPhamRepository.findAll());
            model.addAttribute("sanPhamChiTiet", chiTiet);
            return "quan-tri/san-pham-chi-tiet-add";
        }
        if (chiTiet.getKichThuoc() == null || chiTiet.getKichThuoc().trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập kích thước!");
            model.addAttribute("sanPhamList", sanPhamRepository.findAll());
            model.addAttribute("sanPhamChiTiet", chiTiet);
            return "quan-tri/san-pham-chi-tiet-add";
        }
        if (chiTiet.getChatLieu() == null || chiTiet.getChatLieu().trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập chất liệu!");
            model.addAttribute("sanPhamList", sanPhamRepository.findAll());
            model.addAttribute("sanPhamChiTiet", chiTiet);
            return "quan-tri/san-pham-chi-tiet-add";
        }
        if (chiTiet.getSoLuong() == null || chiTiet.getSoLuong() <= 0) {
            model.addAttribute("error", "Số lượng phải lớn hơn 0!");
            model.addAttribute("sanPhamList", sanPhamRepository.findAll());
            model.addAttribute("sanPhamChiTiet", chiTiet);
            return "quan-tri/san-pham-chi-tiet-add";
        }
        if (chiTiet.getGia_ban() == null || chiTiet.getGia_ban().compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", "Giá bán phải lớn hơn 0!");
            model.addAttribute("sanPhamList", sanPhamRepository.findAll());
            model.addAttribute("sanPhamChiTiet", chiTiet);
            return "quan-tri/san-pham-chi-tiet-add";
        }
        try {
            // Lưu chi tiết
            SanPhamChiTiet savedChiTiet = sanPhamChiTietRepository.save(chiTiet);
            
            // Kiểm tra sau khi lưu
            if (savedChiTiet.getId() == null || savedChiTiet.getGia_ban() == null || savedChiTiet.getSoLuong() == null) {
                throw new RuntimeException("Lưu chi tiết thất bại - dữ liệu không đầy đủ");
            }
            
            // Kiểm tra lại từ database
            SanPhamChiTiet checkChiTiet = sanPhamChiTietRepository.findById(savedChiTiet.getId()).orElse(null);
            if (checkChiTiet == null) {
                throw new RuntimeException("Không tìm thấy chi tiết sau khi lưu");
            }
            
            System.out.println("=== DEBUG: Chi tiết đã lưu thành công ===");
            System.out.println("ID: " + checkChiTiet.getId());
            System.out.println("SanPham ID: " + checkChiTiet.getSanPham().getId());
            System.out.println("Giá bán: " + checkChiTiet.getGia_ban());
            System.out.println("Số lượng: " + checkChiTiet.getSoLuong());
            System.out.println("=====================================");
            
            // Thêm timestamp để force refresh trang
            redirectAttributes.addFlashAttribute("message", "Thêm chi tiết sản phẩm thành công!");
            return "redirect:/san-pham?t=" + System.currentTimeMillis();
            
        } catch (Exception e) {
            System.out.println("=== ERROR: Lỗi khi lưu chi tiết ===");
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            System.out.println("================================");
            
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/san-pham-chi-tiet/add?sanPhamId=" + sanPhamId;
        }
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
                      @RequestParam("gia_ban") BigDecimal giaBan,
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
    
    // Method test để kiểm tra việc lưu chi tiết sản phẩm
    @GetMapping("/test-add/{sanPhamId}")
    public String testAdd(@PathVariable Integer sanPhamId, RedirectAttributes redirectAttributes) {
        try {
            SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElse(null);
            if (sanPham == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
                return "redirect:/san-pham";
            }
            
            SanPhamChiTiet chiTiet = new SanPhamChiTiet();
            chiTiet.setSanPham(sanPham);
            chiTiet.setMauSac("Đen");
            chiTiet.setKichThuoc("40mm");
            chiTiet.setChatLieu("Thép không gỉ");
            chiTiet.setSoLuong(10);
            chiTiet.setGia_ban(new BigDecimal("500000"));
            
            SanPhamChiTiet savedChiTiet = sanPhamChiTietRepository.save(chiTiet);
            System.out.println("=== TEST: Đã tạo chi tiết sản phẩm test ===");
            System.out.println("ID: " + savedChiTiet.getId());
            System.out.println("SanPham: " + savedChiTiet.getSanPham().getMaSanPham());
            System.out.println("Giá bán: " + savedChiTiet.getGia_ban());
            System.out.println("Số lượng: " + savedChiTiet.getSoLuong());
            System.out.println("================================");
            
            // Kiểm tra lại sau khi lưu
            List<SanPhamChiTiet> checkList = sanPhamChiTietRepository.findBySanPham_Id(sanPhamId);
            System.out.println("TEST - Số chi tiết của sản phẩm " + sanPham.getMaSanPham() + ": " + checkList.size());
            for (SanPhamChiTiet ct : checkList) {
                System.out.println("  - Chi tiết ID: " + ct.getId() + ", Giá: " + ct.getGia_ban() + ", SL: " + ct.getSoLuong());
            }
            
            redirectAttributes.addFlashAttribute("message", "Đã tạo chi tiết test cho " + sanPham.getMaSanPham());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/san-pham";
    }
    
    // Endpoint để kiểm tra trực tiếp database
    @GetMapping("/delete-default")
    @ResponseBody
    public String deleteDefaultDetails() {
        try {
            // Xóa các chi tiết mặc định (có giá bán = 0 hoặc số lượng = 1)
            List<SanPhamChiTiet> allDetails = sanPhamChiTietRepository.findAll();
            int deletedCount = 0;
            
            for (SanPhamChiTiet ct : allDetails) {
                if ((ct.getGia_ban() == null || ct.getGia_ban().compareTo(BigDecimal.ZERO) == 0) &&
                    (ct.getSoLuong() == null || ct.getSoLuong() == 1)) {
                    sanPhamChiTietRepository.deleteById(ct.getId());
                    deletedCount++;
                }
            }
            
            return "Đã xóa " + deletedCount + " chi tiết mặc định";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @GetMapping("/debug/{sanPhamId}")
    @ResponseBody
    public String debugSanPham(@PathVariable Integer sanPhamId) {
        StringBuilder result = new StringBuilder();
        result.append("=== DEBUG SAN PHAM ").append(sanPhamId).append(" ===\n");
        
        // Kiểm tra sản phẩm
        SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElse(null);
        if (sanPham != null) {
            result.append("Sản phẩm: ").append(sanPham.getMaSanPham()).append(" - ").append(sanPham.getTenSanPham()).append("\n");
            
            // Kiểm tra chi tiết
            List<SanPhamChiTiet> chiTiets = sanPhamChiTietRepository.findBySanPham_Id(sanPhamId);
            result.append("Số chi tiết: ").append(chiTiets.size()).append("\n");
            
            for (SanPhamChiTiet ct : chiTiets) {
                result.append("  - ID: ").append(ct.getId())
                      .append(", Giá: ").append(ct.getGia_ban())
                      .append(", SL: ").append(ct.getSoLuong())
                      .append(", Màu: ").append(ct.getMauSac())
                      .append("\n");
            }
        } else {
            result.append("Không tìm thấy sản phẩm!\n");
        }
        
        return result.toString();
    }
} 