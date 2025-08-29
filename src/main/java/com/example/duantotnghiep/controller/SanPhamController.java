package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.entity.SanPham;
import com.example.duantotnghiep.entity.SanPhamChiTiet;
import com.example.duantotnghiep.repository.DanhMucRepository;
import com.example.duantotnghiep.repository.SanPhamChiTietRepository;
import com.example.duantotnghiep.repository.ThuongHieuRepository;
import com.example.duantotnghiep.service.SanPhamService;
import com.example.duantotnghiep.service.SanPhamVariantService;
import com.example.duantotnghiep.dto.SanPhamAddDto;
import com.example.duantotnghiep.dto.SanPhamVariantDto;
import com.example.duantotnghiep.dto.SanPhamEditDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
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
    private final SanPhamVariantService sanPhamVariantService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        SanPhamAddDto sanPhamAddDto = new SanPhamAddDto();
        sanPhamAddDto.setSanPham(new SanPham());
        model.addAttribute("sanPhamAddDto", sanPhamAddDto);
        model.addAttribute("danhMucList", danhMucRepository.findAll());
        model.addAttribute("thuongHieuList", thuongHieuRepository.findAll());
        List<String> mauSacList = sanPhamVariantService.getAllMauSac();
        List<String> kichThuocList = sanPhamVariantService.getAllKichThuoc();
        List<String> chatLieuList = sanPhamVariantService.getAllChatLieu();
        
        System.out.println("=== DEBUG: Form add ===");
        System.out.println("Màu sắc có sẵn: " + mauSacList);
        System.out.println("Kích thước có sẵn: " + kichThuocList);
        System.out.println("Chất liệu có sẵn: " + chatLieuList);
        System.out.println("=======================");
        
        model.addAttribute("mauSacList", mauSacList);
        model.addAttribute("kichThuocList", kichThuocList);
        model.addAttribute("chatLieuList", chatLieuList);
        return "/quan-tri/san-pham-add";
    }

    @PostMapping("/add")
    public String addSanPham(@ModelAttribute("sanPhamAddDto") SanPhamAddDto sanPhamAddDto,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             @RequestParam(value = "selectedImage", required = false) String selectedImage,
                             Model model, RedirectAttributes redirectAttributes) {
        try {
        SanPham sanPham = sanPhamAddDto.getSanPham();
        List<SanPhamVariantDto> variants = sanPhamAddDto.getVariants();
        
        System.out.println("=== DEBUG: Thêm sản phẩm mới ===");
        System.out.println("SanPham: " + sanPham);
        System.out.println("File: " + (file != null ? file.getOriginalFilename() : "null"));
        System.out.println("SelectedImage: " + selectedImage);
        System.out.println("Variants: " + (variants != null ? variants.size() : "null"));
        if (variants != null) {
            for (int i = 0; i < variants.size(); i++) {
                System.out.println("  Variant " + i + ": " + variants.get(i));
                System.out.println("    - MauSac: " + variants.get(i).getMauSac());
                System.out.println("    - KichThuoc: " + variants.get(i).getKichThuoc());
                System.out.println("    - ChatLieu: " + variants.get(i).getChatLieu());
                System.out.println("    - SoLuong: " + variants.get(i).getSoLuong());
                System.out.println("    - GiaBan: " + variants.get(i).getGiaBan());
            }
        }
        System.out.println("================================");

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String rootPath = System.getProperty("user.dir");
            Path uploadPath = Paths.get(rootPath, "uploads");
            try {
                // Đảm bảo thư mục uploads tồn tại
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    System.out.println("=== DEBUG: Đã tạo thư mục uploads ===");
                }
                
                // Tạo default.jpg nếu chưa có
                Path defaultPath = uploadPath.resolve("default.jpg");
                if (!Files.exists(defaultPath)) {
                    // Copy từ một ảnh mặc định có sẵn hoặc tạo mới
                    Files.copy(getClass().getResourceAsStream("/static/images/default.jpg"), defaultPath);
                    System.out.println("=== DEBUG: Đã tạo default.jpg ===");
                }
                
                // Upload ảnh mới
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());
                
                // Kiểm tra file sau khi upload
                if (Files.exists(filePath) && Files.size(filePath) > 0) {
                    System.out.println("=== DEBUG: File đã được upload thành công ===");
                    System.out.println("File path: " + filePath);
                    System.out.println("File size: " + Files.size(filePath) + " bytes");
                sanPham.setHinhAnh(fileName);
                } else {
                    System.out.println("=== ERROR: File upload thất bại ===");
                    sanPham.setHinhAnh("default.jpg");
                }
                System.out.println("=========================================");
            } catch (IOException e) {
                System.out.println("Lỗi upload ảnh khi thêm sản phẩm: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (selectedImage != null && !selectedImage.isEmpty()) {
            sanPham.setHinhAnh(selectedImage);
            System.out.println("=== DEBUG: Thêm sản phẩm - Chọn ảnh có sẵn ===");
            System.out.println("Selected image: " + selectedImage);
            System.out.println("==========================================");
        } else {
            sanPham.setHinhAnh("default.jpg");
            System.out.println("=== DEBUG: Thêm sản phẩm - Sử dụng ảnh mặc định ===");
        }
        
        // Đảm bảo trạng thái không null
        if (sanPham.getTrangThai() == null) {
            sanPham.setTrangThai(true); // Mặc định là kích hoạt
        }
        SanPham savedSanPham = sanPhamService.addSanPham(sanPham);
        System.out.println("=== DEBUG: Sản phẩm đã lưu ===");
        System.out.println("ID: " + savedSanPham.getId());
        System.out.println("Mã: " + savedSanPham.getMaSanPham());
        System.out.println("Tên: " + savedSanPham.getTenSanPham());
        System.out.println("Hình ảnh: " + savedSanPham.getHinhAnh());
        System.out.println("=============================");

        // Xử lý lưu nhiều biến thể sản phẩm
        System.out.println("=== DEBUG: Xử lý variants ===");
        if (variants != null && !variants.isEmpty()) {
            System.out.println("Số variants: " + variants.size());
            for (int i = 0; i < variants.size(); i++) {
                SanPhamVariantDto variantDto = variants.get(i);
                System.out.println("Variant " + i + ":");
                System.out.println("  - MauSac: " + variantDto.getMauSac());
                System.out.println("  - KichThuoc: " + variantDto.getKichThuoc());
                System.out.println("  - ChatLieu: " + variantDto.getChatLieu());
                System.out.println("  - SoLuong: " + variantDto.getSoLuong());
                System.out.println("  - GiaBan: " + variantDto.getGiaBan());
                
                // Kiểm tra dữ liệu biến thể hợp lệ
                System.out.println("=== DEBUG: Kiểm tra variant " + i + " ===");
                System.out.println("  MauSac valid: " + (variantDto.getMauSac() != null && !variantDto.getMauSac().trim().isEmpty()));
                System.out.println("  KichThuoc valid: " + (variantDto.getKichThuoc() != null && !variantDto.getKichThuoc().trim().isEmpty()));
                System.out.println("  ChatLieu valid: " + (variantDto.getChatLieu() != null && !variantDto.getChatLieu().trim().isEmpty()));
                System.out.println("  SoLuong valid: " + (variantDto.getSoLuong() != null && variantDto.getSoLuong() >= 0));
                System.out.println("  GiaBan valid: " + (variantDto.getGiaBan() != null && variantDto.getGiaBan().compareTo(BigDecimal.ZERO) >= 0));
                
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
                        try {
                            // Tạo chi tiết mới
                        SanPhamChiTiet chiTiet = new SanPhamChiTiet();
                        chiTiet.setSanPham(savedSanPham);
                        chiTiet.setMauSac(variantDto.getMauSac().trim());
                        chiTiet.setKichThuoc(variantDto.getKichThuoc().trim());
                        chiTiet.setChatLieu(variantDto.getChatLieu().trim());
                        chiTiet.setSoLuong(variantDto.getSoLuong());
                        chiTiet.setGia_ban(variantDto.getGiaBan());
                        
                            // Lưu chi tiết
                            SanPhamChiTiet savedChiTiet = sanPhamChiTietRepository.saveAndFlush(chiTiet);
                            
                            // Kiểm tra sau khi lưu
                            if (savedChiTiet.getId() == null) {
                                throw new RuntimeException("Lưu chi tiết thất bại - không có ID");
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
                            System.out.println("Màu sắc: " + checkChiTiet.getMauSac());
                            System.out.println("Kích thước: " + checkChiTiet.getKichThuoc());
                            System.out.println("Chất liệu: " + checkChiTiet.getChatLieu());
                            System.out.println("=====================================");
                            
                        } catch (Exception e) {
                            System.out.println("=== ERROR: Lỗi khi lưu chi tiết ===");
                            System.out.println("Lỗi: " + e.getMessage());
                            e.printStackTrace();
        System.out.println("================================");
                            throw new RuntimeException("Lỗi khi lưu chi tiết: " + e.getMessage());
                        }
                    }
                }
            }
        }

        redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm thành công!");
        return "redirect:/san-pham";
        } catch (Exception e) {
            System.out.println("=== ERROR: Lỗi khi thêm sản phẩm ===");
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            System.out.println("================================");
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/san-pham/add";
        }
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
        System.out.println("=== DEBUG: Bắt đầu kiểm tra chi tiết sản phẩm ===");
        System.out.println("Tổng số sản phẩm: " + sanPhamPage.getContent().size());
        
        for (SanPham sp : sanPhamPage.getContent()) {
            System.out.println("--- Kiểm tra sản phẩm: " + sp.getMaSanPham() + " (ID: " + sp.getId() + ") ---");
            System.out.println("  Hình ảnh DB: " + sp.getHinhAnh());
            System.out.println("  Đường dẫn ảnh đã xử lý: " + getImagePath(sp.getHinhAnh()));
            List<SanPhamChiTiet> chiTiets = sanPhamChiTietRepository.findBySanPham_Id(sp.getId());
            System.out.println("  Số chi tiết tìm được: " + chiTiets.size());
            
            if (!chiTiets.isEmpty()) {
                // Lấy chi tiết mới nhất theo ID
                SanPhamChiTiet latestChiTiet = chiTiets.get(0); // Do đã sắp xếp DESC trong repository
                
                // Kiểm tra lại từ database
                SanPhamChiTiet checkChiTiet = sanPhamChiTietRepository.findById(latestChiTiet.getId()).orElse(null);
                if (checkChiTiet != null) {
                    chiTietMap.put(sp.getId(), checkChiTiet);
                    System.out.println("  Chi tiết mới nhất:");
                    System.out.println("    - ID: " + checkChiTiet.getId());
                    System.out.println("    - Giá bán: " + checkChiTiet.getGia_ban());
                    System.out.println("    - Số lượng: " + checkChiTiet.getSoLuong());
                }
            } else {
                System.out.println("  Không có chi tiết nào");
            }
        }
        System.out.println("Tổng số sản phẩm có chi tiết: " + chiTietMap.size());
        System.out.println("=== DEBUG: Kết thúc kiểm tra chi tiết sản phẩm ===");
        model.addAttribute("chiTietMap", chiTietMap);
        model.addAttribute("danhMucList", danhMucRepository.findAll());
        model.addAttribute("thuongHieuList", thuongHieuRepository.findAll());
        
        // Thêm helper method vào model để template có thể sử dụng
        model.addAttribute("getImagePath", new Object() {
            public String apply(String hinhAnh) {
                return getImagePath(hinhAnh);
            }
        });
        
        return "/quan-tri/san-pham-fixed";
    }

    @GetMapping("/list-images")
    @ResponseBody
    public List<String> listImages() {
        String rootPath = System.getProperty("user.dir");
        Path uploadPath = Paths.get(rootPath, "uploads");
        List<String> fileNames = new ArrayList<>();
        
        try {
            if (Files.exists(uploadPath)) {
                Files.list(uploadPath)
                    .filter(path -> Files.isRegularFile(path))
                    .forEach(path -> fileNames.add(path.getFileName().toString()));
            }
            System.out.println("=== DEBUG: Danh sách ảnh ===");
            System.out.println("Số lượng: " + fileNames.size());
            fileNames.forEach(name -> System.out.println("  - " + name));
            System.out.println("===========================");
        } catch (IOException e) {
            System.out.println("Lỗi đọc thư mục uploads: " + e.getMessage());
            e.printStackTrace();
        }
        return fileNames;
    }

    @GetMapping("/debug-images")
    @ResponseBody
    public String debugImages() {
        StringBuilder result = new StringBuilder();
        result.append("=== DEBUG HÌNH ẢNH VÀ CHI TIẾT SẢN PHẨM ===\n");
        
        List<SanPham> allSanPham = sanPhamService.getAllSanPham();
        result.append("Tổng số sản phẩm: ").append(allSanPham.size()).append("\n\n");
        
        String rootPath = System.getProperty("user.dir");
        result.append("Root path: ").append(rootPath).append("\n");
        result.append("Upload path: ").append(rootPath).append("/uploads/\n\n");
        
        for (SanPham sp : allSanPham) {
            result.append("Sản phẩm: ").append(sp.getMaSanPham()).append(" - ").append(sp.getTenSanPham()).append("\n");
            result.append("  Hình ảnh DB: ").append(sp.getHinhAnh()).append("\n");
            
            if (sp.getHinhAnh() != null) {
                File imageFile = new File(rootPath + "/uploads/" + sp.getHinhAnh());
                result.append("  File tồn tại: ").append(imageFile.exists()).append("\n");
                result.append("  Đường dẫn đầy đủ: ").append(imageFile.getAbsolutePath()).append("\n");
            }
            
            // Kiểm tra chi tiết sản phẩm từ service
            List<SanPhamChiTiet> chiTietsFromService = sanPhamService.getChiTietBySanPhamId(sp.getId());
            result.append("  Số chi tiết (từ service): ").append(chiTietsFromService.size()).append("\n");
            for (SanPhamChiTiet ct : chiTietsFromService) {
                result.append("    - ID: ").append(ct.getId()).append(", Giá bán: ").append(ct.getGia_ban()).append(", Số lượng: ").append(ct.getSoLuong()).append("\n");
            }
            result.append("\n");
        }
        
        // List files in uploads
        result.append("=== FILES TRONG UPLOADS ===\n");
        File uploadsDir = new File(rootPath + "/uploads/");
        if (uploadsDir.exists()) {
            File[] files = uploadsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        result.append("- ").append(file.getName()).append("\n");
                    }
                }
            }
        } else {
            result.append("Thư mục uploads không tồn tại!\n");
        }
        
        return result.toString();
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        SanPham sanPham = sanPhamService.getSanPhamById(id);
        if (sanPham == null) {
            return "redirect:/san-pham";
        }
        
        // Tạo DTO cho form edit
        SanPhamEditDto sanPhamEditDto = new SanPhamEditDto();
        sanPhamEditDto.setSanPham(sanPham);
        
        // Lấy chi tiết sản phẩm hiện tại
        List<SanPhamChiTiet> chiTiets = sanPhamChiTietRepository.findBySanPham_Id(id);
        SanPhamChiTiet chiTiet = null;
        
        System.out.println("=== DEBUG: Show Edit Form ===");
        System.out.println("Sản phẩm ID: " + id);
        System.out.println("Số lượng chi tiết tìm thấy: " + chiTiets.size());
        
        if (!chiTiets.isEmpty()) {
            chiTiet = chiTiets.get(0);
            System.out.println("Chi tiết ID: " + chiTiet.getId());
            System.out.println("Giá bán: " + chiTiet.getGia_ban());
            System.out.println("Số lượng: " + chiTiet.getSoLuong());
            System.out.println("Màu sắc: " + chiTiet.getMauSac());
            System.out.println("Kích thước: " + chiTiet.getKichThuoc());
            System.out.println("Chất liệu: " + chiTiet.getChatLieu());
            
            sanPhamEditDto.setGiaBan(chiTiet.getGia_ban());
            sanPhamEditDto.setSoLuong(chiTiet.getSoLuong());
            sanPhamEditDto.setMauSac(chiTiet.getMauSac());
            sanPhamEditDto.setKichThuoc(chiTiet.getKichThuoc());
            sanPhamEditDto.setChatLieu(chiTiet.getChatLieu());
        } else {
            System.out.println("Không tìm thấy chi tiết sản phẩm");
        }
        System.out.println("=============================");
        
        // Khởi tạo danh sách variants nếu chưa có
        if (sanPhamEditDto.getVariants() == null) {
            sanPhamEditDto.setVariants(new ArrayList<>());
        }
        
        model.addAttribute("sanPhamEditDto", sanPhamEditDto);
        model.addAttribute("chiTiet", chiTiet); // Thêm chi tiết hiện tại
        model.addAttribute("danhMucList", danhMucRepository.findAll());
        model.addAttribute("thuongHieuList", thuongHieuRepository.findAll());
        model.addAttribute("mauSacList", sanPhamVariantService.getAllMauSac());
        model.addAttribute("kichThuocList", sanPhamVariantService.getAllKichThuoc());
        model.addAttribute("chatLieuList", sanPhamVariantService.getAllChatLieu());
        model.addAttribute("chiTietList", chiTiets);
        
        return "/quan-tri/san-pham-edit";
    }

    @PostMapping("/edit")
    public String editSanPham(@ModelAttribute("sanPhamEditDto") SanPhamEditDto sanPhamEditDto,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              @RequestParam(value = "selectedImage", required = false) String selectedImage,
                              RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== DEBUG: Sửa sản phẩm ===");
            System.out.println("SanPhamEditDto: " + sanPhamEditDto);
            
            SanPham sanPham = sanPhamEditDto.getSanPham();
            if (sanPham == null || sanPham.getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
                return "redirect:/san-pham";
            }
            
            Integer id = sanPham.getId();
            System.out.println("ID sản phẩm: " + id);
            
            // Đảm bảo trạng thái không null
            if (sanPham.getTrangThai() == null) {
                sanPham.setTrangThai(true); // Mặc định là kích hoạt
            }
            
            // Xử lý ảnh
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String rootPath = System.getProperty("user.dir");
                Path uploadPath = Paths.get(rootPath, "uploads");
                try {
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Path filePath = uploadPath.resolve(fileName);
                    imageFile.transferTo(filePath.toFile());
                    sanPham.setHinhAnh(fileName);
                    System.out.println("Đã upload ảnh mới: " + fileName);
                } catch (IOException e) {
                    System.out.println("Lỗi upload ảnh khi sửa sản phẩm: " + e.getMessage());
                    e.printStackTrace();
                }
            } else if (selectedImage != null && !selectedImage.isEmpty()) {
                sanPham.setHinhAnh(selectedImage);
                System.out.println("Đã chọn ảnh có sẵn: " + selectedImage);
            }
            
            // Lưu sản phẩm
            sanPhamService.updateSanPham(id, sanPham);
            System.out.println("Đã cập nhật sản phẩm thành công");
            
            // Cập nhật chi tiết sản phẩm chính
            List<SanPhamChiTiet> existingChiTiets = sanPhamChiTietRepository.findBySanPham_Id(id);
            if (!existingChiTiets.isEmpty()) {
                SanPhamChiTiet chiTiet = existingChiTiets.get(0);
                chiTiet.setGia_ban(sanPhamEditDto.getGiaBan());
                chiTiet.setSoLuong(sanPhamEditDto.getSoLuong());
                chiTiet.setMauSac(sanPhamEditDto.getMauSac());
                chiTiet.setKichThuoc(sanPhamEditDto.getKichThuoc());
                chiTiet.setChatLieu(sanPhamEditDto.getChatLieu());
                sanPhamChiTietRepository.save(chiTiet);
                System.out.println("Đã cập nhật chi tiết sản phẩm chính thành công");
            } else {
                System.out.println("Không tìm thấy chi tiết sản phẩm để cập nhật");
            }
            
            // Xử lý các biến thể mới (nếu có)
            if (sanPhamEditDto.getVariants() != null && !sanPhamEditDto.getVariants().isEmpty()) {
                for (SanPhamVariantDto variant : sanPhamEditDto.getVariants()) {
                    if (variant.getGiaBan() != null && variant.getSoLuong() != null) {
                        SanPhamChiTiet newChiTiet = new SanPhamChiTiet();
                        newChiTiet.setSanPham(sanPham);
                        newChiTiet.setGia_ban(variant.getGiaBan());
                        newChiTiet.setSoLuong(variant.getSoLuong());
                        newChiTiet.setMauSac(variant.getMauSac());
                        newChiTiet.setKichThuoc(variant.getKichThuoc());
                        newChiTiet.setChatLieu(variant.getChatLieu());
                        
                        sanPhamChiTietRepository.saveAndFlush(newChiTiet);
                        System.out.println("=== DEBUG: Đã thêm biến thể mới: " + variant.getMauSac() + " - " + variant.getKichThuoc() + " ===");
                    }
                }
            }
            
            System.out.println("=== DEBUG: Hoàn thành sửa sản phẩm ===");
            redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
            return "redirect:/san-pham";
            
        } catch (Exception e) {
            System.out.println("=== ERROR: Lỗi khi sửa sản phẩm ===");
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            System.out.println("================================");
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/san-pham/edit/" + sanPhamEditDto.getSanPham().getId();
        }
    }
    
    // Helper method để xử lý đường dẫn ảnh
    private String getImagePath(String hinhAnh) {
        if (hinhAnh == null || hinhAnh.trim().isEmpty()) {
            return "/uploads/default.jpg";
        }
        
        // Nếu đường dẫn bắt đầu bằng "images/", loại bỏ prefix này
        if (hinhAnh.startsWith("images/")) {
            return "/uploads/" + hinhAnh.substring(7);
        }
        
        // Nếu không có prefix, thêm /uploads/
        return "/uploads/" + hinhAnh;
    }
    
    @GetMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            SanPham sanPham = sanPhamService.getSanPhamById(id);
            if (sanPham == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
                return "redirect:/san-pham";
            }
            
            // Đảo ngược trạng thái
            boolean newStatus = !sanPham.getTrangThai();
            sanPham.setTrangThai(newStatus);
            
            // Lưu thay đổi
            sanPhamService.updateSanPham(id, sanPham);
            
            String statusText = newStatus ? "kích hoạt" : "ngừng bán";
            redirectAttributes.addFlashAttribute("message", "Đã " + statusText + " sản phẩm thành công!");
            
            System.out.println("=== DEBUG: Thay đổi trạng thái sản phẩm ===");
            System.out.println("ID: " + id);
            System.out.println("Trạng thái mới: " + newStatus);
            System.out.println("===========================================");
            
        } catch (Exception e) {
            System.out.println("=== ERROR: Lỗi khi thay đổi trạng thái ===");
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thay đổi trạng thái: " + e.getMessage());
        }
        
        return "redirect:/san-pham";
    }

    @GetMapping("/delete/{id}")
    public String deleteSanPham(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            SanPham sanPham = sanPhamService.getSanPhamById(id);
            if (sanPham == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
                return "redirect:/san-pham";
            }
            
            // Xóa chi tiết sản phẩm trước
            List<SanPhamChiTiet> chiTiets = sanPhamChiTietRepository.findBySanPham_Id(id);
            for (SanPhamChiTiet chiTiet : chiTiets) {
                sanPhamChiTietRepository.delete(chiTiet);
            }
            
            // Xóa sản phẩm
            sanPhamService.deleteSanPham(id);
            
            redirectAttributes.addFlashAttribute("message", "Đã xóa sản phẩm thành công!");
            
            System.out.println("=== DEBUG: Xóa sản phẩm ===");
            System.out.println("ID: " + id);
            System.out.println("Tên sản phẩm: " + sanPham.getTenSanPham());
            System.out.println("Số chi tiết đã xóa: " + chiTiets.size());
            System.out.println("==========================");
            
        } catch (Exception e) {
            System.out.println("=== ERROR: Lỗi khi xóa sản phẩm ===");
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
        
        return "redirect:/san-pham";
    }

    @GetMapping("/debug-chi-tiet/{sanPhamId}")
    public String debugChiTiet(@PathVariable Integer sanPhamId, Model model) {
        System.out.println("=== DEBUG: Kiểm tra chi tiết sản phẩm ===");
        System.out.println("SanPham ID: " + sanPhamId);
        
        // Lấy tất cả chi tiết
        List<SanPhamChiTiet> allChiTiets = sanPhamChiTietRepository.findAll();
        System.out.println("Tổng số chi tiết trong DB: " + allChiTiets.size());
        
        for (SanPhamChiTiet ct : allChiTiets) {
            System.out.println("Chi tiết ID: " + ct.getId());
            System.out.println("  - SanPham ID: " + (ct.getSanPham() != null ? ct.getSanPham().getId() : "null"));
            System.out.println("  - Màu sắc: " + ct.getMauSac());
            System.out.println("  - Kích thước: " + ct.getKichThuoc());
            System.out.println("  - Chất liệu: " + ct.getChatLieu());
            System.out.println("  - Số lượng: " + ct.getSoLuong());
            System.out.println("  - Giá bán: " + ct.getGia_ban());
        }
        
        // Lấy chi tiết theo sản phẩm
        List<SanPhamChiTiet> chiTiets = sanPhamChiTietRepository.findBySanPham_Id(sanPhamId);
        System.out.println("Chi tiết cho sản phẩm " + sanPhamId + ": " + chiTiets.size());
        
        for (SanPhamChiTiet ct : chiTiets) {
            System.out.println("  Chi tiết ID: " + ct.getId());
            System.out.println("    - Màu sắc: " + ct.getMauSac());
            System.out.println("    - Kích thước: " + ct.getKichThuoc());
            System.out.println("    - Chất liệu: " + ct.getChatLieu());
            System.out.println("    - Số lượng: " + ct.getSoLuong());
            System.out.println("    - Giá bán: " + ct.getGia_ban());
        }
        System.out.println("================================");
        
        model.addAttribute("allChiTiets", allChiTiets);
        model.addAttribute("chiTiets", chiTiets);
        return "debug-chi-tiet";
    }
}