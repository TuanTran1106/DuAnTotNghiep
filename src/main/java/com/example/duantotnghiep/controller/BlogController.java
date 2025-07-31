package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.entity.Blog;
import com.example.duantotnghiep.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("")
    public String listBlogs(@RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogPage;
        if (keyword != null && !keyword.isEmpty()) {
            blogPage = blogService.searchBlogs(keyword, pageable);
        } else {
            blogPage = blogService.getAllBlogs(pageable);
        }
        model.addAttribute("blogPage", blogPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "/quan-tri/blog";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "/quan-tri/blog-add";
    }

    @PostMapping("/add")
    public String addBlog(@ModelAttribute @Valid Blog blog, BindingResult result, Model model,
                          @RequestParam(value = "file", required = false) MultipartFile file,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "/quan-tri/blog-add";
        }
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String rootPath = System.getProperty("user.dir");
            Path uploadPath = Paths.get(rootPath, "uploads");
            try {
                java.nio.file.Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());
                blog.setHinhAnh(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            blog.setHinhAnh("default.jpg");
        }
        blog.setNgayDang(LocalDateTime.now());
        blogService.addBlog(blog);
        redirectAttributes.addFlashAttribute("message", "Thêm blog thành công!");
        return "redirect:/blog";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Blog> blogOpt = blogService.getBlogById(id);
        if (blogOpt.isEmpty()) {
            return "redirect:/blog";
        }
        model.addAttribute("blog", blogOpt.get());
        return "/quan-tri/blog-edit";
    }

    @PostMapping("/edit/{id}")
    public String editBlog(@PathVariable Integer id, @ModelAttribute Blog blog, BindingResult result, Model model,
                           @RequestParam(value = "file", required = false) MultipartFile file,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "/quan-tri/blog-edit";
        }
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String rootPath = System.getProperty("user.dir");
            Path uploadPath = Paths.get(rootPath, "uploads");
            try {
                java.nio.file.Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());
                blog.setHinhAnh(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Blog oldBlog = blogService.getBlogById(id).orElse(null);
            blog.setHinhAnh(oldBlog != null ? oldBlog.getHinhAnh() : "default.jpg");
        }
        blog.setNgayDang(LocalDateTime.now());
        blogService.updateBlog(id, blog);
        redirectAttributes.addFlashAttribute("message", "Cập nhật blog thành công!");
        return "redirect:/blog";
    }

    @GetMapping("/delete/{id}")
    public String confirmDelete(@PathVariable Integer id, Model model) {
        Optional<Blog> blogOpt = blogService.getBlogById(id);
        if (blogOpt.isEmpty()) {
            return "redirect:/blog";
        }
        model.addAttribute("blog", blogOpt.get());
        return "/quan-tri/blog-confirm-delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message", "Đã xóa blog thành công!");
        return "redirect:/blog";
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

    @GetMapping("/detail/{id}")
    public String viewDetail(@PathVariable Integer id, Model model) {
        Optional<Blog> blogOpt = blogService.getBlogById(id);
        if (blogOpt.isEmpty()) {
            return "redirect:/blog";
        }
        model.addAttribute("blog", blogOpt.get());
        return "/quan-tri/blog-detail";
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