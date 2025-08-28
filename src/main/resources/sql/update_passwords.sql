-- Script để cập nhật mật khẩu nhân viên hiện có từ plain text sang BCrypt
-- Chạy các câu lệnh này sau khi deploy ứng dụng mới

-- Mật khẩu được mã hóa bằng BCrypt
UPDATE nhan_vien SET mat_khau = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa' WHERE ma_nhan_vien = 'NV001';
UPDATE nhan_vien SET mat_khau = '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfgqwAG6SWaR0bJgJgJgJgJgJgJgJgJg' WHERE ma_nhan_vien = 'NV002';
UPDATE nhan_vien SET mat_khau = '$2a$10$9L2q/b1eM2MYNJpFEESgxPgqrxBH7TXbS1CkKkKkKkKkKkKkKkKkK' WHERE ma_nhan_vien = 'NV003';
UPDATE nhan_vien SET mat_khau = '$2a$10$0M3r/c2fN3OZKqGFFTThzQhrsyCI8UYcT2DlLlLlLlLlLlLlLlLlL' WHERE ma_nhan_vien = 'NV004';
UPDATE nhan_vien SET mat_khau = '$2a$10$1N4s/d3gO4PALrHGUGUiARisTzDJ9VZdU3EmMmMmMmMmMmMmMmMmM' WHERE ma_nhan_vien = 'NV005';
UPDATE nhan_vien SET mat_khau = '$2a$10$2O5t/e4hP5QBMsIHVHVjBSjtUzEK0WAeV4FnNnNnNnNnNnNnNnNnN' WHERE ma_nhan_vien = 'NV006';
UPDATE nhan_vien SET mat_khau = '$2a$10$3P6u/f5iQ6RCNtJIWIWkCTkuUzFL1XBfW5GoOoOoOoOoOoOoOoOoO' WHERE ma_nhan_vien = 'NV007';
UPDATE nhan_vien SET mat_khau = '$2a$10$4Q7v/g6jR7SDNuKJXJXlDUlvVzGM2YCgX6HpPpPpPpPpPpPpPpPpPp' WHERE ma_nhan_vien = 'NV008';
UPDATE nhan_vien SET mat_khau = '$2a$10$5R8w/h7kS8TEOvLKYLZmEVmwWzHN3ZDhY7IqQqQqQqQqQqQqQqQqQq' WHERE ma_nhan_vien = 'NV009';
UPDATE nhan_vien SET mat_khau = '$2a$10$6S9x/i8lT9UFPwMLZMAnFWnxXzIO4AEiZ8JrRrRrRrRrRrRrRrRrRr' WHERE ma_nhan_vien = 'NV010';

-- Lưu ý: Các mật khẩu trên chỉ là ví dụ, trong thực tế sẽ được tạo động bởi BCrypt
-- Để có mật khẩu chính xác, hãy chạy ứng dụng và sử dụng PasswordUtil.main() để tạo

-- Lưu ý: Người dùng không cần cập nhật mật khẩu vì vẫn sử dụng plain text
