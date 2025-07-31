package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.entity.SanPham;
import com.example.duantotnghiep.entity.SanPhamChiTiet;
import com.example.duantotnghiep.repository.DanhMucRepository;
import com.example.duantotnghiep.repository.SanPhamChiTietRepository;
import com.example.duantotnghiep.repository.ThuongHieuRepository;
import com.example.duantotnghiep.service.SanPhamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/san-pham")
@AllArgsConstructor
public class SanPhamController {
    private final SanPhamService sanPhamService;
    private final DanhMucRepository danhMucRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("danhMucList", danhMucRepository.findAll());
        model.addAttribute("thuongHieuList", thuongHieuRepository.findAll());
        return "/quan-tri/san-pham-add";
    }

    @PostMapping("/add")
    public String addSanPham(@ModelAttribute @Valid SanPham sanPham, BindingResult result, Model model,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             @RequestParam(value = "selectedImage", required = false) String selectedImage,
                             @RequestParam(value = "variants", required = false) List<com.example.duantotnghiep.dto.SanPhamVariantDto> variants,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("danhMucList", danhMucRepository.findAll());
            model.addAttribute("thuongHieuList", thuongHieuRepository.findAll());
            return "/quan-tri/san-pham-add";
        }
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String rootPath = System.getProperty("user.dir");
            Path uploadPath = Paths.get(rootPath, "uploads");
            try {
                java.nio.file.Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());
                sanPham.setHinhAnh(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (selectedImage != null && !selectedImage.isEmpty()) {
            sanPham.setHinhAnh(selectedImage);
        } else {
            sanPham.setHinhAnh("default.jpg");
        }
        SanPham savedSanPham = sanPhamService.addSanPham(sanPham);

        // Xử lý lưu nhiều biến thể sản phẩm
        if (variants != null && !variants.isEmpty()) {
            for (com.example.duantotnghiep.dto.SanPhamVariantDto variantDto : variants) {
                // Kiểm tra dữ liệu biến thể hợp lệ
                if (variantDto.getMauSac() != null && !variantDto.getMauSac().trim().isEmpty() &&
                    variantDto.getKichThuoc() != null && !variantDto.getKichThuoc().trim().isEmpty() &&
                    variantDto.getChatLieu() != null && !variantDto.getChatLieu().trim().isEmpty() &&
                    variantDto.getSoLuong() != null && variantDto.getSoLuong() >= 0 &&
                    variantDto.getGiaBan() != null && variantDto.getGiaBan().compareTo(BigDecimal.ZERO) >= 0) {
                    
                    // Kiểm tra trùng lặp biến thể
                    boolean isDuplicate = false;
                    List<SanPhamChiTiet> existingVariants = sanPhamChiTietRepository.findBySanPham_Id(savedSanPham.getId());
                    for (SanPhamChiTiet existing : existingVariants) {
                        if (existing.getMauSac().equalsIgnoreCase(variantDto.getMauSac().trim()) &&
                            existing.getKichThuoc().equalsIgnoreCase(variantDto.getKichThuoc().trim()) &&
                            existing.getChatLieu().equalsIgnoreCase(variantDto.getChatLieu().trim())) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    
                    if (!isDuplicate) {
                        SanPhamChiTiet chiTiet = new SanPhamChiTiet();
                        chiTiet.setSanPham(savedSanPham);
                        chiTiet.setMauSac(variantDto.getMauSac().trim());
                        chiTiet.setKichThuoc(variantDto.getKichThuoc().trim());
                        chiTiet.setChatLieu(variantDto.getChatLieu().trim());
                        chiTiet.setSoLuong(variantDto.getSoLuong());
                        chiTiet.setGia_ban(variantDto.getGiaBan());
                        
                        sanPhamChiTietRepository.save(chiTiet);
                    }
                }
            }
        }
        redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm thành công!");
        return "redirect:/san-pham";
    }


    public static class SanPhamEditDto {
        private SanPham sanPham;
        private List<SanPhamChiTiet> chiTietList;
        public SanPham getSanPham() { return sanPham; }
        public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }
        public List<SanPhamChiTiet> getChiTietList() { return chiTietList; }
        public void setChiTietList(List<SanPhamChiTiet> chiTietList) { this.chiTietList = chiTietList; }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        SanPham sanPham = sanPhamService.getSanPhamById(id);
        List<SanPhamChiTiet> chiTietList = sanPhamService.getChiTietBySanPhamId(id);
        SanPhamEditDto dto = new SanPhamEditDto();
        dto.setSanPham(sanPham);
        dto.setChiTietList(chiTietList);
        model.addAttribute("sanPhamEditDto", dto);
        model.addAttribute("danhMucList", danhMucRepository.findAll());
        model.addAttribute("thuongHieuList", thuongHieuRepository.findAll());
        return "/quan-tri/san-pham-edit";
    }

    @PostMapping("/edit/{id}")
    public String editSanPham(@PathVariable Integer id, @ModelAttribute SanPhamEditDto sanPhamEditDto, BindingResult result, Model model,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "selectedImage", required = false) String selectedImage,
                              RedirectAttributes redirectAttributes) {
        SanPham sanPham = sanPhamEditDto.getSanPham();
        if (result.hasErrors()) {
            model.addAttribute("danhMucList", danhMucRepository.findAll());
            model.addAttribute("thuongHieuList", thuongHieuRepository.findAll());
            return "/quan-tri/san-pham-edit";
        }
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String rootPath = System.getProperty("user.dir");
            Path uploadPath = Paths.get(rootPath, "uploads");
            try {
                java.nio.file.Files.createDirectories(uploadPath); // Đảm bảo thư mục tồn tại
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());
                sanPham.setHinhAnh(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (selectedImage != null && !selectedImage.isEmpty()) {
            sanPham.setHinhAnh(selectedImage);
        } else {
            SanPham spCu = sanPhamService.getSanPhamById(id);
            sanPham.setHinhAnh(spCu != null ? spCu.getHinhAnh() : "default.jpg");
        }
        sanPhamService.updateSanPham(id, sanPham);

        List<SanPhamChiTiet> chiTietList = sanPhamEditDto.getChiTietList();
        if (chiTietList != null) {
            List<SanPhamChiTiet> oldList = sanPhamService.getChiTietBySanPhamId(id);
            java.util.Set<Integer> newIds = new java.util.HashSet<>();
            for (SanPhamChiTiet ct : chiTietList) {
                ct.setSanPham(sanPhamService.getSanPhamById(id));
                if (ct.getId() == null) {
                    sanPhamChiTietRepository.save(ct);
                } else {
                    newIds.add(ct.getId());
                    sanPhamChiTietRepository.save(ct);
                }
            }
            for (SanPhamChiTiet old : oldList) {
                if (old.getId() != null && !newIds.contains(old.getId())) {
                    sanPhamChiTietRepository.deleteById(old.getId());
                }
            }
        }
        redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
        return "redirect:/san-pham";
    }



    @GetMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        SanPham sp = sanPhamService.getSanPhamById(id);
        if (sp != null) {
            sp.setTrangThai(!Boolean.TRUE.equals(sp.getTrangThai()));
            sanPhamService.updateSanPham(id, sp);
            redirectAttributes.addFlashAttribute("message", "Đã cập nhật trạng thái sản phẩm!");
        }
        return "redirect:/san-pham";
    }

    @GetMapping("")
    public String listSanPham(@RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "trangThai", required = false) String trangThaiStr,
                              @RequestParam(value = "danhMucId", required = false) Integer danhMucId,
                              @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
                              @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                              @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                              Model model) {
        Page<SanPham> sanPhamPage;
        Boolean trangThai = null;
        if (trangThaiStr != null && !trangThaiStr.isEmpty() && !trangThaiStr.equals("all")) {
            trangThai = Boolean.valueOf(trangThaiStr);
        }
        Pageable pageable = PageRequest.of(page, size);

        if ((danhMucId != null && danhMucId > 0) && (thuongHieuId != null && thuongHieuId > 0)) {
            sanPhamPage = sanPhamService.findByDanhMucIdAndThuongHieuId(danhMucId, thuongHieuId, pageable);
        } else if (danhMucId != null && danhMucId > 0) {
            sanPhamPage = sanPhamService.findByDanhMucId(danhMucId, pageable);
        } else if (thuongHieuId != null && thuongHieuId > 0) {
            sanPhamPage = sanPhamService.findByThuongHieuId(thuongHieuId, pageable);
        } else if (trangThai != null && keyword != null && !keyword.isEmpty()) {
            sanPhamPage = sanPhamService.searchByTrangThaiAndKeyword(trangThai, keyword, pageable);
        } else if (trangThai != null) {
            sanPhamPage = sanPhamService.getByTrangThai(trangThai, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            sanPhamPage = sanPhamService.searchSanPham(keyword, pageable);
        } else {
            sanPhamPage = sanPhamService.getAllSanPham(pageable);
        }
        model.addAttribute("sanPhamPage", sanPhamPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThaiStr);
        model.addAttribute("danhMucId", danhMucId);
        model.addAttribute("thuongHieuId", thuongHieuId);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        // Thêm map chứa chi tiết sản phẩm cho từng sản phẩm
        java.util.Map<Integer, SanPhamChiTiet> chiTietMap = new java.util.HashMap<>();
        for (SanPham sp : sanPhamPage.getContent()) {
            List<SanPhamChiTiet> chiTiets = sanPhamService.getChiTietBySanPhamId(sp.getId());
            if (!chiTiets.isEmpty()) {
                chiTietMap.put(sp.getId(), chiTiets.get(0));
            }
        }
        model.addAttribute("chiTietMap", chiTietMap);
        model.addAttribute("danhMucList", danhMucRepository.findAll());
        model.addAttribute("thuongHieuList", thuongHieuRepository.findAll());
        return "/quan-tri/san-pham";
    }

    @GetMapping("/uploads/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            Path file = Paths.get("uploads").resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list-images")
    @ResponseBody
    public List<String> listImages() {
        File folder = new File("uploads");
        File[] files = folder.listFiles();
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }
}
