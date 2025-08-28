# Hướng dẫn cập nhật mật khẩu nhân viên sang BCrypt và role người dùng

## Tổng quan
Sau khi cập nhật hệ thống để sử dụng BCrypt cho mã hóa mật khẩu nhân viên và tự động set role cho người dùng mới, các nhân viên hiện có trong database sẽ không thể đăng nhập được vì mật khẩu của họ vẫn đang ở dạng plain text.

## Các thay đổi đã thực hiện

### 1. Thêm dependency BCrypt
- Đã thêm `spring-security-crypto` vào `pom.xml`

### 2. Tạo configuration cho BCrypt
- Tạo `PasswordEncoder.java` trong package `config`

### 3. Cập nhật service
- `NhanVienServiceImpl` đã được cập nhật để mã hóa mật khẩu khi thêm nhân viên mới
- Logic cập nhật mật khẩu khi chỉnh sửa nhân viên
- `NguoiDungServiceImpl` đã được cập nhật để tự động set role mặc định cho người dùng mới

### 4. Cập nhật login controller
- `LoginController` đã được cập nhật để so sánh mật khẩu đã mã hóa cho nhân viên
- Người dùng vẫn sử dụng plain text password

### 5. Cập nhật form
- Form thêm nhân viên: thay đổi input type từ text sang password
- Form cập nhật nhân viên: thêm trường mật khẩu (optional)
- Người dùng: tự động set role khi thêm mới

## Cách cập nhật mật khẩu cho nhân viên hiện có và role cho người dùng

### Bước 1: Chạy ứng dụng
```bash
mvn spring-boot:run
```

### Bước 2: Tạo script cập nhật mật khẩu
Chạy class `PasswordUtil` để tạo script SQL:
```bash
mvn compile exec:java -Dexec.mainClass="com.example.duantotnghiep.util.PasswordUtil"
```

### Bước 3: Thực thi script SQL
Copy các câu lệnh SQL được tạo ra và chạy trong database của bạn.

### Bước 4: Cập nhật role cho người dùng (nếu cần)
Nếu người dùng hiện có có role_id = NULL, chạy script sau:
```sql
UPDATE nguoi_dung SET role_id = 2 WHERE role_id IS NULL;
```

### Bước 5: Kiểm tra
Sau khi cập nhật, các nhân viên có thể đăng nhập với mật khẩu cũ:
- Email: nhan1@example.com, Password: pass1
- Email: nhan2@example.com, Password: pass2
- ...

Người dùng có thể đăng nhập bình thường:
- Email: a@example.com, Password: matkhau1
- Email: b@example.com, Password: matkhau2
- ...

## Lưu ý quan trọng

1. **Bảo mật**: Mật khẩu nhân viên mới sẽ được mã hóa bằng BCrypt với salt ngẫu nhiên
2. **Tương thích**: Nhân viên mới được thêm sẽ tự động có mật khẩu được mã hóa
3. **Cập nhật**: Khi cập nhật thông tin nhân viên, mật khẩu chỉ thay đổi nếu được nhập mới
4. **Đăng nhập**: Hệ thống sẽ tự động so sánh mật khẩu đã mã hóa cho nhân viên
5. **Người dùng**: Mật khẩu vẫn sử dụng plain text, role được tự động set khi thêm mới

## Mật khẩu mặc định cho nhân viên hiện có

| Mã NV | Email | Mật khẩu |
|-------|-------|----------|
| NV001 | nhan1@example.com | pass1 |
| NV002 | nhan2@example.com | pass2 |
| NV003 | nhan3@example.com | pass3 |
| NV004 | nhan4@example.com | pass4 |
| NV005 | nhan5@example.com | pass5 |
| NV006 | nhan6@example.com | pass6 |
| NV007 | nhan7@example.com | pass7 |
| NV008 | nhan8@example.com | pass8 |
| NV009 | nhan9@example.com | pass9 |
| NV010 | nhan10@example.com | pass10 |

## Troubleshooting

### Lỗi đăng nhập sau khi cập nhật
- Đảm bảo đã chạy script SQL cập nhật mật khẩu
- Kiểm tra xem mật khẩu có được mã hóa đúng không
- Restart ứng dụng sau khi cập nhật database

### Lỗi khi thêm nhân viên mới
- Kiểm tra xem dependency BCrypt đã được thêm chưa
- Kiểm tra log để xem có lỗi gì không
