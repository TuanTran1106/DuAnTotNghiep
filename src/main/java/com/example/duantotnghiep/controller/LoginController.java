package com.example.duantotnghiep.controller;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.duantotnghiep.repository.NguoiDungRepository;
import com.example.duantotnghiep.repository.NhanVienRepository;
import com.example.duantotnghiep.entity.NguoiDung;
import com.example.duantotnghiep.entity.NhanVien;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Controller
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {
    private final NguoiDungRepository nguoiDungRepository;
    private final NhanVienRepository nhanVienRepository;

    @GetMapping()
    public String showLoginForm() {
        return "quan-tri/login";
    }

    @PostMapping("/dang-nhap")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        Optional<NhanVien> nhanVienOpt = nhanVienRepository.findByEmailAndMatKhau(username, password);
        if (nhanVienOpt.isPresent()) {
            session.setAttribute("tenDangNhap", nhanVienOpt.get().getHoTen());
            return "redirect:/quan-ly";
        }
        Optional<NguoiDung> nguoiDungFind = nguoiDungRepository.findByEmailAndMatKhau(username, password);
        if (nguoiDungFind.isPresent()) {
            NguoiDung nguoiDung = nguoiDungFind.get();
            session.setAttribute("tenDangNhap", nguoiDung.getHoTen());
            if (nguoiDung.getPhanQuyen().getId() != null && nguoiDung.getPhanQuyen().getId() == 1) {
                return "redirect:/quan-ly";
            } else {
                return "redirect:/";
            }
        }
        return "redirect:/login?error";
    }
}
