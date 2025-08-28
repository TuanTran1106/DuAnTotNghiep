package com.example.duantotnghiep;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Test mã hóa mật khẩu
        String rawPassword = "pass1";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("Mật khẩu gốc: " + rawPassword);
        System.out.println("Mật khẩu đã mã hóa: " + encodedPassword);
        
        // Test so sánh mật khẩu
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("So sánh mật khẩu: " + matches);
        
        // Test với mật khẩu sai
        boolean wrongMatches = encoder.matches("wrongpassword", encodedPassword);
        System.out.println("So sánh mật khẩu sai: " + wrongMatches);
        
        // Tạo script SQL cho tất cả nhân viên
        System.out.println("\n--- Script SQL để cập nhật mật khẩu ---");
        String[] passwords = {"pass1", "pass2", "pass3", "pass4", "pass5", "pass6", "pass7", "pass8", "pass9", "pass10"};
        String[] maNhanVien = {"NV001", "NV002", "NV003", "NV004", "NV005", "NV006", "NV007", "NV008", "NV009", "NV010"};
        
        for (int i = 0; i < passwords.length; i++) {
            String encoded = encoder.encode(passwords[i]);
            System.out.println("UPDATE nhan_vien SET mat_khau = '" + encoded + "' WHERE ma_nhan_vien = '" + maNhanVien[i] + "';");
        }
    }
}
