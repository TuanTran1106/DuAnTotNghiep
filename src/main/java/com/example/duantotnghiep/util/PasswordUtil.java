package com.example.duantotnghiep.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public static String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
    
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
    
    public static void main(String[] args) {
        // Script để mã hóa mật khẩu cho nhân viên hiện có
        String[] nhanVienPasswords = {"pass1", "pass2", "pass3", "pass4", "pass5", "pass6", "pass7", "pass8", "pass9", "pass10"};
        
        System.out.println("-- Script để cập nhật mật khẩu nhân viên trong database --");
        System.out.println("-- Chạy các câu lệnh SQL sau trong database --");
        System.out.println();
        
        for (int i = 0; i < nhanVienPasswords.length; i++) {
            String encodedPassword = encodePassword(nhanVienPasswords[i]);
            System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodedPassword + "' WHERE ma_nhan_vien = 'NV00" + (i + 1) + "';");
        }
        
        System.out.println();
        System.out.println("-- Hoặc cập nhật tất cả cùng lúc --");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass1") + "' WHERE ma_nhan_vien = 'NV001';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass2") + "' WHERE ma_nhan_vien = 'NV002';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass3") + "' WHERE ma_nhan_vien = 'NV003';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass4") + "' WHERE ma_nhan_vien = 'NV004';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass5") + "' WHERE ma_nhan_vien = 'NV005';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass6") + "' WHERE ma_nhan_vien = 'NV006';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass7") + "' WHERE ma_nhan_vien = 'NV007';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass8") + "' WHERE ma_nhan_vien = 'NV008';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass9") + "' WHERE ma_nhan_vien = 'NV009';");
        System.out.println("UPDATE nhan_vien SET mat_khau = '" + encodePassword("pass10") + "' WHERE ma_nhan_vien = 'NV010';");
        
        System.out.println();
        System.out.println("-- Lưu ý: Người dùng không cần cập nhật mật khẩu vì vẫn sử dụng plain text --");
    }
}
