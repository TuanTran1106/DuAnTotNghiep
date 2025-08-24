# Hướng dẫn sử dụng trạng thái đơn hàng mới

## Tổng quan
Hệ thống đã được bổ sung thêm các trạng thái đơn hàng mới để quản lý quy trình xử lý đơn hàng một cách chi tiết và chuyên nghiệp hơn.

## Các trạng thái đơn hàng mới

### 1. Trạng thái cơ bản
- **Chờ xác nhận**: Đơn hàng mới được đặt, chờ nhân viên xác nhận
- **Đang chuẩn bị hàng**: Đơn hàng đã được xác nhận, đang chuẩn bị hàng để giao
- **Đang giao**: Đơn hàng đang được vận chuyển đến khách hàng
- **Đã giao**: Đơn hàng đã được giao thành công
- **Đã hủy**: Đơn hàng đã bị hủy

### 2. Trạng thái thanh toán
- **Đã thanh toán - Chờ xác nhận**: Khách hàng đã thanh toán, chờ xác nhận
- **Đã thanh toán - Đang chuẩn bị hàng**: Đã thanh toán và đang chuẩn bị hàng
- **Đã thanh toán - Đang giao**: Đã thanh toán và đang giao hàng

### 3. Trạng thái yêu cầu
- **Yêu cầu hoàn**: Khách hàng yêu cầu hoàn tiền sau khi đã giao hàng
- **Yêu cầu hủy**: Khách hàng yêu cầu hủy đơn hàng

## Quy trình chuyển trạng thái

### Quy trình chuẩn:
1. **Chờ xác nhận** → **Đang chuẩn bị hàng**
2. **Đang chuẩn bị hàng** → **Đang giao**
3. **Đang giao** → **Đã giao**

### Quy trình thanh toán trước:
1. **Đã thanh toán - Chờ xác nhận** → **Đã thanh toán - Đang chuẩn bị hàng**
2. **Đã thanh toán - Đang chuẩn bị hàng** → **Đã thanh toán - Đang giao**
3. **Đã thanh toán - Đang giao** → **Đã giao**

### Các trường hợp đặc biệt:
- **Bất kỳ trạng thái nào** → **Đã hủy** (nếu có lý do chính đáng)
- **Đã giao** → **Yêu cầu hoàn** (nếu khách hàng yêu cầu)

## Các tính năng mới

### 1. Nút "Duyệt"
- Hiển thị cho các trạng thái có thể chuyển tiếp
- Tự động chuyển sang trạng thái tiếp theo theo quy trình

### 2. Nút "Hủy"
- Hiển thị cho các trạng thái có thể hủy
- Yêu cầu nhập lý do hủy
- Chỉ áp dụng cho đơn hàng chưa giao

### 3. Nút "Yêu cầu hoàn"
- Chỉ hiển thị khi đơn hàng đã giao
- Yêu cầu nhập lý do hoàn tiền
- Chuyển trạng thái sang "Yêu cầu hoàn"

## Màu sắc badge

- **badge-success**: Hoàn tất
- **badge-delivered**: Đã giao
- **badge-warning**: Chờ xác nhận
- **badge-danger**: Đã hủy
- **badge-payment**: Các trạng thái đã thanh toán
- **badge-refund**: Yêu cầu hoàn
- **badge-cancel-request**: Yêu cầu hủy

## API Endpoints mới

### 1. Chuyển trạng thái
```
PATCH /quan-ly/don-hang/api/{id}/next-status
```

### 2. Hủy đơn hàng
```
PATCH /quan-ly/don-hang/api/{id}/cancel
Body: { "reason": "Lý do hủy" }
```

### 3. Yêu cầu hoàn tiền
```
PATCH /quan-ly/don-hang/api/{id}/refund
Body: { "reason": "Lý do hoàn tiền" }
```

## Lưu ý quan trọng

1. **Kiểm tra quyền**: Chỉ nhân viên có quyền mới có thể thay đổi trạng thái
2. **Ghi chú**: Lý do hủy/hoàn tiền sẽ được lưu vào ghi chú đơn hàng
3. **Lịch sử**: Mọi thay đổi trạng thái đều được ghi nhận
4. **Validation**: Hệ thống kiểm tra tính hợp lệ của việc chuyển trạng thái

## Cách sử dụng

1. **Xem danh sách đơn hàng**: Sử dụng các tab để lọc theo trạng thái
2. **Chuyển trạng thái**: Nhấn nút "Duyệt" để chuyển sang trạng thái tiếp theo
3. **Hủy đơn hàng**: Nhấn nút "Hủy" và nhập lý do
4. **Yêu cầu hoàn tiền**: Nhấn nút "Yêu cầu hoàn" và nhập lý do
5. **Xem chi tiết**: Click vào dòng đơn hàng để xem thông tin chi tiết

## Troubleshooting

### Lỗi thường gặp:
1. **Không thể chuyển trạng thái**: Kiểm tra quyền và trạng thái hiện tại
2. **Nút không hiển thị**: Kiểm tra trạng thái đơn hàng
3. **Lỗi API**: Kiểm tra log và quyền truy cập

### Liên hệ hỗ trợ:
Nếu gặp vấn đề, vui lòng liên hệ đội kỹ thuật để được hỗ trợ.
