# Hướng dẫn Validation Khuyến Mãi & Voucher

## Tổng quan
Hệ thống đã được cập nhật với logic validation mạnh mẽ để đảm bảo ngày kết thúc khuyến mãi và voucher phải là tương lai và các quy tắc business khác.

## Các quy tắc validation

### 1. Validation ngày (Áp dụng cho cả Khuyến Mãi và Voucher)
- **Ngày bắt đầu**: Phải là hiện tại hoặc tương lai (không được là quá khứ)
- **Ngày kết thúc**: Phải là tương lai (không được là quá khứ)
- **Quan hệ ngày**: Ngày kết thúc phải sau ngày bắt đầu

### 2. Validation dữ liệu Khuyến Mãi
- **Tên chương trình**: Bắt buộc, từ 3-150 ký tự
- **Số lượng**: Bắt buộc, phải lớn hơn 0
- **Mức giảm giá**: Bắt buộc, phải lớn hơn 0
- **Kiểu giảm**: Bắt buộc (Phần trăm hoặc Tiền mặt)

### 3. Validation dữ liệu Voucher
- **Tên voucher**: Bắt buộc, từ 3-150 ký tự
- **Số lượng**: Bắt buộc, phải lớn hơn 0
- **Mức giảm**: Bắt buộc, phải lớn hơn 0
- **Kiểu giảm**: Bắt buộc (Tiền mặt hoặc Phần trăm)
- **Điều kiện tối thiểu**: Không được âm

## Các tầng validation

### 1. Client-side (JavaScript)
- Validation real-time khi người dùng nhập liệu
- Hiển thị thông báo lỗi ngay lập tức
- Ngăn chặn submit form nếu có lỗi
- Sử dụng API để validate ngày:
  - `/khuyen-mai/api/validate-dates`
  - `/voucher/api/validate-dates`

### 2. Server-side (Controller)
- Bean Validation với annotations
- Custom validation logic
- Xử lý BindingResult và hiển thị lỗi
- Redirect với thông báo lỗi

### 3. Service Layer
- Business logic validation
- Kiểm tra các quy tắc nghiệp vụ
- Ném exception với thông báo chi tiết

### 4. Entity Layer
- Bean Validation annotations
- Custom validation method với `@AssertTrue`

## API Endpoints

### POST `/khuyen-mai/api/validate-dates`
Validate ngày real-time cho Khuyến Mãi

### POST `/voucher/api/validate-dates`
Validate ngày real-time cho Voucher

**Request Body:**
```json
{
  "ngayBatDau": "2024-01-15",
  "ngayKetThuc": "2024-01-20"
}
```

**Response:**
```json
{
  "valid": true,
  "errors": {}
}
```

hoặc

```json
{
  "valid": false,
  "errors": {
    "ngayKetThuc": "Ngày kết thúc không được là quá khứ"
  }
}
```

## Cách sử dụng

### 1. Thêm Khuyến Mãi mới
1. Truy cập `/khuyen-mai/add`
2. Điền thông tin khuyến mãi
3. Hệ thống sẽ validate real-time
4. Nếu có lỗi, sẽ hiển thị thông báo
5. Chỉ có thể submit khi tất cả validation pass

### 2. Cập nhật Khuyến Mãi
1. Truy cập `/khuyen-mai/update/{id}`
2. Chỉnh sửa thông tin
3. Validation tương tự như thêm mới

### 3. Thêm Voucher mới
1. Truy cập `/voucher/add`
2. Điền thông tin voucher
3. Hệ thống sẽ validate real-time
4. Nếu có lỗi, sẽ hiển thị thông báo
5. Chỉ có thể submit khi tất cả validation pass

### 4. Cập nhật Voucher
1. Truy cập `/voucher/update/{id}`
2. Chỉnh sửa thông tin
3. Validation tương tự như thêm mới

## Thông báo lỗi

### Lỗi ngày (Chung cho cả Khuyến Mãi và Voucher)
- "Ngày bắt đầu không được là quá khứ"
- "Ngày kết thúc không được là quá khứ"
- "Ngày kết thúc phải sau ngày bắt đầu"

### Lỗi dữ liệu Khuyến Mãi
- "Tên chương trình không được để trống"
- "Số lượng phải lớn hơn 0"
- "Mức giảm giá phải lớn hơn 0"

### Lỗi dữ liệu Voucher
- "Tên voucher không được để trống"
- "Số lượng phải lớn hơn 0"
- "Mức giảm phải lớn hơn 0"
- "Điều kiện tối thiểu không được âm"

## Cải tiến UX

### 1. Visual Feedback
- Border màu đỏ cho lỗi
- Border màu xanh cho hợp lệ
- Thông báo lỗi rõ ràng

### 2. Real-time Validation
- Validate ngay khi người dùng thay đổi giá trị
- Không cần submit form để biết lỗi

### 3. Min Date Attribute
- Input date tự động set min date là hôm nay
- Ngăn chặn chọn ngày quá khứ

## Troubleshooting

### Lỗi thường gặp
1. **Ngày không hợp lệ**: Kiểm tra format ngày (YYYY-MM-DD)
2. **Validation không hoạt động**: Kiểm tra JavaScript console
3. **API không response**: Kiểm tra network tab

### Debug
1. Mở Developer Tools (F12)
2. Kiểm tra Console tab cho JavaScript errors
3. Kiểm tra Network tab cho API calls
4. Kiểm tra Application tab cho localStorage

## Cấu hình

### Bean Validation
Đảm bảo dependency trong `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### JavaScript
Validation script được include trong templates:
- `khuyen-mai-add.html`
- `khuyen-mai-update.html`
- `voucher-add.html`
- `voucher-update.html`

## Bảo mật

### Server-side Validation
- Tất cả validation đều được thực hiện ở server
- Client-side chỉ để UX tốt hơn
- Không tin tưởng dữ liệu từ client

### Input Sanitization
- Validate và sanitize tất cả input
- Sử dụng prepared statements cho database
- Escape HTML output

## Performance

### Optimization
- Validation real-time với debounce
- API calls được optimize
- Caching validation results

### Monitoring
- Log validation errors
- Monitor API performance
- Track validation success rate

## Files đã được cập nhật

### Backend
- `KhuyenMai.java` - Entity với Bean Validation
- `Voucher.java` - Entity với Bean Validation
- `KhuyenMaiController.java` - Controller với validation logic
- `VoucherController.java` - Controller với validation logic
- `KhuyenMaiServiceImpl.java` - Service với business validation
- `VoucherServiceImpl.java` - Service với business validation

### Frontend
- `khuyen-mai-add.html` - Template với JavaScript validation
- `khuyen-mai-update.html` - Template với JavaScript validation
- `voucher-add.html` - Template với JavaScript validation
- `voucher-update.html` - Template với JavaScript validation

### Documentation
- `VALIDATION_GUIDE.md` - Hướng dẫn chi tiết 