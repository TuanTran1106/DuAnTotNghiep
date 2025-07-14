CREATE DATABASE BanDongHo2222;
go
use BanDongHo2222;
go

-- Bảng cơ sở không phụ thuộc
CREATE TABLE thuong_hieu (
                             id INT IDENTITY(1,1) PRIMARY KEY,
                             ten_thuong_hieu NVARCHAR(100),
                             quoc_gia NVARCHAR(100)
);

CREATE TABLE phan_quyen (
                            role_id INT IDENTITY(1,1) PRIMARY KEY,
                            role_name NVARCHAR(50)
);

CREATE TABLE danh_muc (
                          id INT IDENTITY(1,1) PRIMARY KEY,
                          ten_danh_muc NVARCHAR(100)
);

CREATE TABLE trang_thai_don_hang (
                                     id INT IDENTITY(1,1) PRIMARY KEY,
                                     ten_trang_thai NVARCHAR(100)
);

CREATE TABLE phuong_thuc_thanh_toan (
                                        id INT IDENTITY(1,1) PRIMARY KEY,
                                        ten_phuong_thuc NVARCHAR(100)
);

CREATE TABLE voucher (
                         id INT IDENTITY(1,1) PRIMARY KEY,
                         ma_voucher VARCHAR(50) UNIQUE NOT NULL,
                         ten_voucher NVARCHAR(150),
                         mo_ta NVARCHAR(255),
                         kieu_giam BIT, --0 là giảm tiền mặt, 1 là giảm theo %. nếu kieu_giam = 0 là số tiền (VNĐ), nếu kieu_giam = 1 là phần trăm (%).
                         muc_giam DECIMAL(18,2),
                         so_luong INT,
                         so_luong_con INT,
                         ngay_bat_dau DATE,
                         ngay_ket_thuc DATE,
                         dieu_kien_toi_thieu DECIMAL(18,2),-- tổng tiền đơn hàng tối thiểu để áp dụng.
                         trang_thai BIT DEFAULT 1
);

CREATE TABLE khuyen_mai (
                            id INT IDENTITY(1,1) PRIMARY KEY,
                            ma_khuyen_mai VARCHAR(50) UNIQUE,
                            ten_chuong_trinh NVARCHAR(150),
                            mo_ta NVARCHAR(255),
                            ngay_tao DATETIME DEFAULT GETDATE(),
                            ngay_bat_dau DATE,
                            ngay_ket_thuc DATE,
                            so_luong INT,
                            kieu_giam BIT,
                            muc_giam_gia DECIMAL(18,2),
                            trang_thai INT DEFAULT 0
);

-- Bảng có khóa ngoại đơn
CREATE TABLE nguoi_dung (
                            id INT IDENTITY(1,1) PRIMARY KEY,
                            ma_nguoi_dung NVARCHAR(20),
                            ho_ten NVARCHAR(100),
                            sdt VARCHAR(15),
                            gioi_tinh BIT DEFAULT 0,
                            email NVARCHAR(100),
                            mat_khau VARCHAR(255),
                            hinh_anh NVARCHAR(255),
                            trang_thai BIT DEFAULT 1,
                            ngay_tao DATETIME DEFAULT GETDATE(),
                            ngay_sua DATETIME NULL,
                            role_id INT,
                            FOREIGN KEY (role_id) REFERENCES phan_quyen(role_id)
);

CREATE TABLE nhan_vien (
                           id INT IDENTITY(1,1) PRIMARY KEY,
                           ma_nhan_vien NVARCHAR(20),
                           ho_ten NVARCHAR(100),
                           email NVARCHAR(100),
                           mat_khau VARCHAR(255),
                           sdt VARCHAR(15),
                           dia_chi NVARCHAR(255),
                           gioi_tinh BIT DEFAULT 0,
                           trang_thai BIT DEFAULT 1,
                           ngay_tao DATETIME DEFAULT GETDATE(),
                           ngay_sua DATETIME NULL
);

CREATE TABLE dia_chi_nguoi_dung (
                                    id INT IDENTITY(1,1) PRIMARY KEY,
                                    id_nguoi_dung INT,
                                    dia_chi NVARCHAR(255),
                                    thanh_pho NVARCHAR(100),
                                    quan_huyen NVARCHAR(100),
                                    phuong_xa NVARCHAR(100),
                                    mac_dinh BIT DEFAULT 0,
                                    ngay_tao DATETIME DEFAULT GETDATE(),
                                    FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id)
);

CREATE TABLE blog (
                      id INT IDENTITY(1,1) PRIMARY KEY,
                      tieu_de NVARCHAR(255),
                      noi_dung NVARCHAR(MAX),
                      hinh_anh NVARCHAR(255),
                      ngay_dang DATETIME DEFAULT GETDATE()
);

CREATE TABLE san_pham (
                          id INT IDENTITY(1,1) PRIMARY KEY,
                          ma_san_pham NVARCHAR(20),
                          ten_san_pham NVARCHAR(100),
                          hinh_anh NVARCHAR(255),
                          gia_nhap DECIMAL(18,2),
                          ngay_nhap DATETIME,
                          ngay_sua DATETIME NULL,
                          mo_ta NVARCHAR(500),
                          trang_thai BIT,
                          id_thuong_hieu INT,
                          id_danh_muc INT,
                          FOREIGN KEY (id_thuong_hieu) REFERENCES thuong_hieu(id),
                          FOREIGN KEY (id_danh_muc) REFERENCES danh_muc(id)
);

-- Phụ thuộc vào bảng san_pham
CREATE TABLE san_pham_chi_tiet (
                                   id INT IDENTITY(1,1) PRIMARY KEY,
                                   id_san_pham INT,
                                   mau_sac NVARCHAR(50),
                                   kich_thuoc NVARCHAR(50),
                                   chat_lieu NVARCHAR(100),
                                   so_luong INT,
                                   gia_ban DECIMAL(18,2),
                                   FOREIGN KEY (id_san_pham) REFERENCES san_pham(id)
);

CREATE TABLE san_pham_chi_tiet_khuyen_mai (
                                              id INT IDENTITY(1,1) PRIMARY KEY,
                                              id_san_pham_chi_tiet INT,
                                              id_khuyen_mai INT,
                                              FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id),
                                              FOREIGN KEY (id_khuyen_mai) REFERENCES khuyen_mai(id)
);

CREATE TABLE thong_ke_doanh_thu (
                                    id INT IDENTITY(1,1) PRIMARY KEY,
                                    thang INT,
                                    nam INT,
                                    so_luong_ban INT,
                                    doanh_thu DECIMAL(18,2),
                                    id_san_pham_chi_tiet INT,
                                    FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id)
);

CREATE TABLE gio_hang (
                          id INT IDENTITY(1,1) PRIMARY KEY,
                          id_nguoi_dung INT,
                          ngay_them DATETIME DEFAULT GETDATE(),
                          FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id),
);


CREATE TABLE gio_hang_chi_tiet (
                                   id INT IDENTITY(1,1) PRIMARY KEY,
                                   id_gio_hang INT NOT NULL,
                                   id_san_pham_chi_tiet INT NOT NULL,
                                   so_luong INT NOT NULL,
                                   ngay_them DATETIME DEFAULT GETDATE(),
                                   FOREIGN KEY (id_gio_hang) REFERENCES gio_hang(id),
                                   FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id)
);

-- Bảng phức hợp cuối cùng
CREATE TABLE don_hang (
                          id INT IDENTITY(1,1) PRIMARY KEY,
                          ma_don_hang NVARCHAR(20),
                          tong_gia DECIMAL(18,2),
                          ngay_mua DATETIME,
                          ghi_chu NVARCHAR(500),
                          ngay_tao DATETIME DEFAULT GETDATE(),
                          ngay_sua DATETIME NULL,
                          id_nguoi_dung INT,
                          id_trang_thai INT,
                          id_phuong_thuc INT,
                          id_voucher INT,
                          id_nhan_vien INT,
                          id_dia_chi INT,
                          FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id),
                          FOREIGN KEY (id_trang_thai) REFERENCES trang_thai_don_hang(id),
                          FOREIGN KEY (id_phuong_thuc) REFERENCES phuong_thuc_thanh_toan(id),
                          FOREIGN KEY (id_voucher) REFERENCES voucher(id),
                          FOREIGN KEY (id_nhan_vien) REFERENCES nhan_vien(id),
                          FOREIGN KEY (id_dia_chi) REFERENCES dia_chi_nguoi_dung(id)
);

CREATE TABLE chi_tiet_don_hang (
                                   id INT IDENTITY(1,1) PRIMARY KEY,
                                   id_don_hang INT,
                                   id_san_pham_chi_tiet INT,
                                   so_luong_dat INT,
                                   don_gia DECIMAL(18,2),
                                   FOREIGN KEY (id_don_hang) REFERENCES don_hang(id),
                                   FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id)
);

CREATE TABLE voucher_nguoi_dung (
                                    id INT IDENTITY(1,1) PRIMARY KEY,
                                    id_voucher INT,
                                    id_nguoi_dung INT,
                                    da_su_dung BIT DEFAULT 0,
                                    FOREIGN KEY (id_voucher) REFERENCES voucher(id),
                                    FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id)
);

CREATE TABLE lich_su_dang_nhap (
                                   id INT IDENTITY(1,1) PRIMARY KEY,
                                   id_nguoi_dung INT,
                                   thoi_gian DATETIME DEFAULT GETDATE(),
                                   ip NVARCHAR(50),
                                   FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id)
);
INSERT INTO thuong_hieu (ten_thuong_hieu, quoc_gia) VALUES
                                                        (N'CASIO', N'Nhật Bản'),
                                                        (N'ORIENT', N'Nhật Bản'),
                                                        (N'CITIZEN', N'Nhật Bản'),
                                                        (N'SEIKO', N'Nhật Bản'),
                                                        (N'Daniel Wellington', N'Thụy Điển'),
                                                        (N'DW', N'Thụy Điển'),
                                                        (N'MVMT', N'Mỹ'),
                                                        (N'EDIFICE', N'Nhật Bản'),
                                                        (N'%', N'Không xác định');

INSERT INTO phan_quyen (role_name) VALUES
                                       (N'admin'),
                                       (N'khach_hang');

INSERT INTO danh_muc (ten_danh_muc) VALUES
                                        (N'Đồng hồ nam'),
                                        (N'Đồng hồ nữ'),
                                        (N'Đồng hồ đôi'),
                                        (N'Đồng hồ trẻ em'),
                                        (N'Phụ kiện');

INSERT INTO trang_thai_don_hang (ten_trang_thai) VALUES
                                                     (N'Chờ xác nhận'),
                                                     (N'Đang chuẩn bị hàng'),
                                                     (N'Đang giao'),
                                                     (N'Đã giao'),
                                                     (N'Đã hủy');

INSERT INTO phuong_thuc_thanh_toan (ten_phuong_thuc) VALUES
                                                         (N'Thanh toán khi nhận hàng'),
                                                         (N'Chuyển khoản ngân hàng'),
                                                         (N'Ví điện tử Momo'),
                                                         (N'ZaloPay'),
                                                         (N'VNPay');

INSERT INTO voucher (ma_voucher, ten_voucher, mo_ta, kieu_giam, muc_giam, so_luong, so_luong_con, ngay_bat_dau, ngay_ket_thuc, dieu_kien_toi_thieu)
VALUES
    ('VOUCHER01', N'Giảm 50K', N'Áp dụng cho đơn hàng từ 500K', 0, 50000, 100, 100, '2025-06-01', '2025-06-30', 500000),
    ('VOUCHER02', N'Giảm 10%', N'Dành cho khách hàng mới', 1, 10, 50, 50, '2025-06-01', '2025-06-30', 0),
    ('VOUCHER03', N'Freeship', N'Miễn phí vận chuyển đơn từ 300K', 0, 30000, 200, 200, '2025-06-01', '2025-06-30', 300000),
    ('VOUCHER04', N'Giảm 20%', N'Tặng sinh nhật khách hàng', 1, 20, 30, 30, '2025-06-15', '2025-06-30', 100000),
    ('VOUCHER05', N'Khuyến mãi cuối tuần', N'Giảm giá cho đơn từ 1 triệu', 0, 100000, 80, 80, '2025-06-21', '2025-06-23', 1000000);

INSERT INTO khuyen_mai (ma_khuyen_mai, ten_chuong_trinh, mo_ta, ngay_bat_dau, ngay_ket_thuc, so_luong, kieu_giam, muc_giam_gia)
VALUES
    ('KM01', N'Hè rực rỡ', N'Giảm giá mùa hè cho tất cả sản phẩm', '2025-06-01', '2025-06-30', 100, 1, 10),
    ('KM02', N'Tặng sinh nhật', N'Ưu đãi mừng sinh nhật khách hàng', '2025-06-10', '2025-06-20', 50, 0, 100000),
    ('KM03', N'Cuối tuần sôi động', N'Giảm giá sốc cho ngày cuối tuần', '2025-06-21', '2025-06-23', 200, 1, 15),
    ('KM04', N'Thiếu nhi vui vẻ', N'Ưu đãi dịp Quốc tế thiếu nhi', '2025-06-01', '2025-06-05', 80, 0, 50000),
    ('KM05', N'Mua 1 tặng 1', N'Áp dụng cho danh mục đồng hồ nam', '2025-06-05', '2025-06-15', 100, 1, 50),
    ('KM06', N'Khách VIP', N'Ưu đãi đặc biệt cho khách hàng thân thiết', '2025-06-01', '2025-06-30', 30, 0, 150000),
    ('KM07', N'Sale mừng khai trương', N'Giảm giá toàn bộ cửa hàng', '2025-06-01', '2025-06-10', 150, 1, 20),
    ('KM08', N'Giảm giá cuối tháng', N'Khuyến mãi xả kho cuối tháng', '2025-06-25', '2025-06-30', 90, 0, 70000),
    ('KM09', N'Combo đồng hồ đôi', N'Mua cặp được giảm giá', '2025-06-15', '2025-06-30', 40, 1, 25),
    ('KM10', N'Flash Sale', N'Khuyến mãi trong ngày duy nhất', '2025-06-22', '2025-06-22', 60, 0, 120000);

INSERT INTO nguoi_dung (ma_nguoi_dung, ho_ten, sdt, gioi_tinh, email, mat_khau, hinh_anh, trang_thai, role_id)
VALUES
    ('ND001', N'Nguyễn Văn A', '0912345678', 1, 'a@example.com', 'matkhau1', 'a.jpg', 1, 2),
    ('ND002', N'Trần Thị B', '0923456789', 0, 'b@example.com', 'matkhau2', 'b.jpg', 1, 2),
    ('ND003', N'Lê Văn C', '0934567890', 1, 'c@example.com', 'matkhau3', 'c.jpg', 1, 2),
    ('ND004', N'Phạm Thị D', '0945678901', 0, 'd@example.com', 'matkhau4', 'd.jpg', 1, 2),
    ('ND005', N'Hoàng Văn E', '0956789012', 1, 'e@example.com', 'matkhau5', 'e.jpg', 1, 2),
    ('ND006', N'Đỗ Thị F', '0967890123', 0, 'f@example.com', 'matkhau6', 'f.jpg', 1, 2),
    ('ND007', N'Vũ Văn G', '0978901234', 1, 'g@example.com', 'matkhau7', 'g.jpg', 1, 2),
    ('ND008', N'Ngô Thị H', '0989012345', 0, 'h@example.com', 'matkhau8', 'h.jpg', 1, 2),
    ('ND009', N'Dương Văn I', '0990123456', 1, 'i@example.com', 'matkhau9', 'i.jpg', 1, 2),
    ('ND010', N'Lý Thị K', '0901234567', 0, 'k@example.com', 'matkhau10', 'k.jpg', 1, 1); -- Admin

INSERT INTO nhan_vien (ma_nhan_vien, ho_ten, email, mat_khau, sdt, dia_chi, gioi_tinh, trang_thai)
VALUES
    ('NV001', N'Nguyễn Văn Nhân', 'nhan1@example.com', 'pass1', '0901111222', N'123 Lê Lợi, Q.1, TP.HCM', 1, 1),
    ('NV002', N'Trần Thị Hồng', 'nhan2@example.com', 'pass2', '0902222333', N'234 Trần Hưng Đạo, Q.5, TP.HCM', 0, 1),
    ('NV003', N'Lê Văn Công', 'nhan3@example.com', 'pass3', '0903333444', N'345 Nguyễn Trãi, Q.10, TP.HCM', 1, 1),
    ('NV004', N'Phạm Thị Nhung', 'nhan4@example.com', 'pass4', '0904444555', N'456 Cách Mạng, Q.3, TP.HCM', 0, 1),
    ('NV005', N'Hoàng Văn Bình', 'nhan5@example.com', 'pass5', '0905555666', N'567 Phan Đình Phùng, Q.Phú Nhuận', 1, 1),
    ('NV006', N'Đỗ Thị Lan', 'nhan6@example.com', 'pass6', '0906666777', N'678 Lý Thường Kiệt, Q.Tân Bình', 0, 1),
    ('NV007', N'Vũ Văn Hải', 'nhan7@example.com', 'pass7', '0907777888', N'789 Nguyễn Văn Cừ, Q.5', 1, 1),
    ('NV008', N'Ngô Thị Hiền', 'nhan8@example.com', 'pass8', '0908888999', N'890 Điện Biên Phủ, Q.Bình Thạnh', 0, 1),
    ('NV009', N'Dương Văn Long', 'nhan9@example.com', 'pass9', '0909999000', N'901 Hoàng Hoa Thám, Q.Tân Bình', 1, 1),
    ('NV010', N'Lý Thị Mai', 'nhan10@example.com', 'pass10', '0910000111', N'902 Võ Thị Sáu, Q.3', 0, 1);

INSERT INTO dia_chi_nguoi_dung (id_nguoi_dung, dia_chi, thanh_pho, quan_huyen, phuong_xa, mac_dinh)
VALUES
    (1, N'123 Lê Lợi', N'TP.HCM', N'Quận 1', N'Phường Bến Thành', 1),
    (2, N'234 Trần Hưng Đạo', N'TP.HCM', N'Quận 5', N'Phường 7', 1),
    (3, N'345 Nguyễn Trãi', N'TP.HCM', N'Quận 10', N'Phường 3', 1),
    (4, N'456 Cách Mạng Tháng 8', N'TP.HCM', N'Quận 3', N'Phường 12', 1),
    (5, N'567 Phan Đình Phùng', N'TP.HCM', N'Phú Nhuận', N'Phường 15', 1),
    (6, N'678 Lý Thường Kiệt', N'TP.HCM', N'Tân Bình', N'Phường 6', 1),
    (7, N'789 Nguyễn Văn Cừ', N'TP.HCM', N'Quận 5', N'Phường 1', 1),
    (8, N'890 Điện Biên Phủ', N'TP.HCM', N'Bình Thạnh', N'Phường 25', 1),
    (9, N'901 Hoàng Hoa Thám', N'TP.HCM', N'Tân Bình', N'Phường 13', 1),
    (10, N'902 Võ Thị Sáu', N'TP.HCM', N'Quận 3', N'Phường 8', 1);

INSERT INTO blog (tieu_de, noi_dung, hinh_anh, ngay_dang)
VALUES
    (N'Cách chọn đồng hồ phù hợp với phong cách', N'Để chọn đồng hồ phù hợp với phong cách, bạn nên xem xét các yếu tố như thiết kế, kích thước, chất liệu, màu sắc, và tính năng của đồng hồ, đồng thời kết hợp với phong cách cá nhân và hoàn cảnh sử dụng.    ', 'images/Blog1.jpg', GETDATE()),
    (N'Bảo quản đồng hồ đúng cách', N'Để bảo quản đồng hồ đúng cách, bạn cần lưu ý một số điểm quan trọng sau: tránh va đập mạnh, không để đồng hồ tiếp xúc với hóa chất, nhiệt độ cao hoặc từ trường mạnh, vệ sinh đồng hồ thường xuyên và bảo dưỡng định kỳ. Ngoài ra, cần chú ý đến loại dây đeo và cách bảo quản phù hợp với từng loại chất liệu. ', 'images/blog2.jpg', GETDATE()),
    (N'Lịch sử phát triển của thương hiệu Casio', N'Thương hiệu Casio, có nguồn gốc từ Nhật Bản, được thành lập vào tháng 4 năm 1946 bởi Tadao Kashio. Ban đầu, công ty tập trung vào sản xuất các sản phẩm điện tử như máy tính và thiết bị âm thanh. Năm 1974, Casio đánh dấu bước ngoặt quan trọng khi cho ra mắt chiếc đồng hồ Casiotron, chiếc đồng hồ kỹ thuật số đầu tiên có lịch tự động, mở ra kỷ nguyên mới cho ngành công nghiệp đồng hồ. Sau đó, Casio tiếp tục giới thiệu các dòng sản phẩm nổi tiếng như G-Shock (1983) với khả năng chống sốc, Baby-G (1994) dành cho nữ, và Oceanus (2004). ', 'images/blogCasio.jpg', GETDATE()),
    (N'Xu hướng đồng hồ năm 2025', N'Năm 2025, xu hướng đồng hồ sẽ tập trung vào sự kết hợp giữa công nghệ, tính bền vững và phong cách cổ điển, đồng thời, các mẫu đồng hồ thông minh cũng sẽ tiếp tục được cải tiến với nhiều tính năng theo dõi sức khỏe tiên tiến. ', 'images/blog3.jpg', GETDATE()),
    (N'Phân biệt đồng hồ thật và giả', N'Để phân biệt đồng hồ thật và giả, có thể dựa vào nhiều yếu tố như thiết kế, bộ máy, chất liệu, các chi tiết nhỏ, và giấy tờ đi kèm. Đồng hồ thật thường có các chi tiết sắc nét, bộ máy vận hành trơn tru, chất liệu cao cấp, và đi kèm đầy đủ giấy tờ bảo hành. Ngược lại, đồng hồ giả thường có các chi tiết mờ, bộ máy không ổn định, chất liệu kém, và thiếu hoặc có giấy tờ không đầy đủ.', 'images/blog4.jpg', GETDATE());

INSERT INTO san_pham (ma_san_pham, ten_san_pham, gia_nhap, ngay_nhap, mo_ta, trang_thai, id_thuong_hieu, id_danh_muc, hinh_anh)
VALUES
    ('SP001', N'Đồng hồ Casio MTP-V002', 800000, GETDATE(), N'Đồng hồ nam dây da cổ điển, chống nước cơ bản', 1, 1, 1, 'images/dongho5.jpg'),
    ('SP002', N'Đồng hồ Orient Bambino', 2500000, GETDATE(), N'Mẫu đồng hồ cơ lộ máy sang trọng, dây da nâu', 1, 2, 1, 'images/Orient.jpg'),
    ('SP003', N'Đồng hồ Citizen Eco-Drive', 3500000, GETDATE(), N'Đồng hồ dùng năng lượng ánh sáng, vỏ thép không gỉ', 1, 3, 1, 'images/citizen.jpg'),
    ('SP004', N'Đồng hồ Seiko 5 quân đội', 2800000, GETDATE(), N'Dây dù mạnh mẽ, phù hợp với phong cách thể thao', 1, 4, 1, 'images/seiko.jpg'),
    ('SP005', N'Daniel Wellington Classic Petite', 3100000, GETDATE(), N'Mẫu đồng hồ unisex dây lưới mỏng, mặt đen', 1, 5, 2, 'images/daniel-wellington.jpg'),
    ('SP006', N'Casio Baby-G BA-110', 2200000, GETDATE(), N'Đồng hồ nữ thể thao, nhiều tính năng hiện đại', 1, 1, 2, 'images/casio.jpg'),
    ('SP007', N'Citizen Automatic NH8350', 4200000, GETDATE(), N'Đồng hồ cơ, mặt trắng, dây thép', 1, 3, 3, 'images/citizen-2.jpg'),
    ('SP008', N'Seiko Chronograph SSB031', 4600000, GETDATE(), N'Mẫu đồng hồ bấm giờ thể thao, chống nước 100M', 1, 4, 4, 'images/seiko2.jpg'),
    ('SP009', N'DW Iconic Link', 3800000, GETDATE(), N'Dây kim loại tinh tế, mặt trắng sang trọng', 1, 5, 3, 'images/daniel-wellington-2.png'),
    ('SP010', N'Casio G-Shock GA-2100', 2700000, GETDATE(), N'Dòng G-Shock siêu bền, phong cách thể thao cá tính', 1, 1, 4, 'images/casio2.jpg');

INSERT INTO san_pham_chi_tiet (id_san_pham, mau_sac, kich_thuoc, chat_lieu, so_luong, gia_ban)
VALUES
    (1, N'Đen', N'40mm', N'Dây da', 50, 1200000),
    (2, N'Nâu', N'42mm', N'Dây da', 40, 2700000),
    (3, N'Bạc', N'41mm', N'Thép không gỉ', 35, 3900000),
    (4, N'Xanh quân đội', N'39mm', N'Dây vải dù', 60, 3200000),
    (5, N'Vàng hồng', N'36mm', N'Dây lưới kim loại', 45, 3600000),
    (6, N'Hồng', N'38mm', N'Nhựa cao cấp', 55, 2500000),
    (7, N'Trắng', N'40mm', N'Dây thép', 30, 4500000),
    (8, N'Xám', N'43mm', N'Thép không gỉ', 25, 4900000),
    (9, N'Bạc', N'32mm', N'Dây kim loại', 48, 4000000),
    (10, N'Đen đỏ', N'45mm', N'Nhựa chống sốc', 70, 3000000);

INSERT INTO san_pham_chi_tiet_khuyen_mai (id_san_pham_chi_tiet, id_khuyen_mai)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 6),
    (7, 7),
    (8, 8),
    (9, 9),
    (10, 10);

INSERT INTO thong_ke_doanh_thu (thang, nam, so_luong_ban, doanh_thu, id_san_pham_chi_tiet)
VALUES
    (5, 2025, 30, 36000000, 1),
    (5, 2025, 20, 54000000, 2),
    (5, 2025, 15, 58500000, 3),
    (5, 2025, 10, 32000000, 4),
    (5, 2025, 25, 90000000, 5);

INSERT INTO gio_hang (id_nguoi_dung,ngay_them)
VALUES
    (1, GETDATE()),
    (2, GETDATE()),
    (3, GETDATE()),
    (4, GETDATE()),
    (5, GETDATE());

INSERT INTO gio_hang_chi_tiet (id_gio_hang, id_san_pham_chi_tiet, so_luong)
VALUES
    (1, 1, 2),
    (1, 2, 1),
    (2, 3, 1),
    (2, 4, 2),
    (3, 5, 1),
    (3, 6, 2),
    (4, 7, 1),
    (4, 8, 1),
    (5, 9, 1),
    (5, 10, 1);

INSERT INTO don_hang (ma_don_hang, tong_gia, ngay_mua, ghi_chu, ngay_tao, ngay_sua, id_nguoi_dung, id_trang_thai, id_phuong_thuc, id_voucher, id_nhan_vien, id_dia_chi)
VALUES
    ('DH01', 3600000, '2025-06-10', N'Giao trong giờ hành chính', GETDATE(), NULL, 1, 1, 1, 1, 1, 1),
    ('DH02', 5200000, '2025-06-11', N'Giao sau 18h', GETDATE(), NULL, 2, 2, 2, 2, 2, 2),
    ('DH03', 1450000, '2025-06-12', N'Không gọi điện trước', GETDATE(), NULL, 3, 3, 3, NULL, 1, 3),
    ('DH04', 7890000, '2025-06-12', N'Có sử dụng voucher sinh nhật', GETDATE(), NULL, 4, 1, 2, 3, 2, 4),
    ('DH05', 999000,  '2025-06-13', N'Khách yêu cầu đóng gói kỹ', GETDATE(), NULL, 5, 2, 1, NULL, 1, 5),
    ('DH06', 2500000, '2025-06-14', N'Chuyển khoản trước', GETDATE(), NULL, 1, 3, 3, NULL, 2, 6),
    ('DH07', 4300000, '2025-06-15', N'Thanh toán khi nhận hàng', GETDATE(), NULL, 2, 4, 1, NULL, 1, 7),
    ('DH08', 6780000, '2025-06-16', N'Khách muốn xuất hóa đơn', GETDATE(), NULL, 3, 1, 2, 1, 2, 8),
    ('DH09', 3100000, '2025-06-17', N'Mua tặng người thân', GETDATE(), NULL, 4, 2, 3, 2, 1, 9),
    ('DH10', 4200000, '2025-06-18', N'Giao nhanh trong ngày', GETDATE(), NULL, 5, 1, 2, NULL, 2, 10);

INSERT INTO chi_tiet_don_hang (id_don_hang, id_san_pham_chi_tiet, so_luong_dat, don_gia)
VALUES
    (1, 1, 2, 1200000),
    (2, 2, 1, 2700000),
    (3, 3, 1, 3900000),
    (4, 4, 2, 3200000),
    (5, 5, 1, 3600000),
    (6, 6, 2, 2500000),
    (7, 7, 1, 4500000),
    (8, 8, 1, 4900000),
    (9, 9, 1, 4000000),
    (10, 10, 1, 3000000);

INSERT INTO voucher_nguoi_dung (id_voucher, id_nguoi_dung, da_su_dung)
VALUES
    (1, 1, 0),
    (2, 1, 1),
    (3, 2, 0),
    (4, 2, 1),
    (5, 3, 0),
    (1, 3, 1),
    (2, 4, 0),
    (3, 4, 1),
    (4, 5, 0),
    (5, 5, 1);

INSERT INTO lich_su_dang_nhap (id_nguoi_dung, thoi_gian, ip) VALUES
                                                                 (1, '2025-06-20 08:15:00', '192.168.1.10'),
                                                                 (1, '2025-06-21 09:10:00', '192.168.1.11'),
                                                                 (2, '2025-06-20 10:00:00', '192.168.1.12'),
                                                                 (2, '2025-06-21 11:45:00', '192.168.1.13'),
                                                                 (3, '2025-06-20 13:30:00', '192.168.1.14'),
                                                                 (3, '2025-06-21 14:20:00', '192.168.1.15'),
                                                                 (4, '2025-06-20 16:00:00', '192.168.1.16'),
                                                                 (4, '2025-06-21 16:45:00', '192.168.1.17'),
                                                                 (5, '2025-06-20 18:10:00', '192.168.1.18'),
                                                                 (5, '2025-06-21 19:00:00', '192.168.1.19');


SELECT * FROM thuong_hieu;
SELECT * FROM phan_quyen;
SELECT * FROM danh_muc;
SELECT * FROM trang_thai_don_hang;
SELECT * FROM phuong_thuc_thanh_toan;
SELECT * FROM voucher;
SELECT * FROM khuyen_mai;
SELECT * FROM nguoi_dung;
SELECT * FROM nhan_vien;
SELECT * FROM san_pham;
SELECT * FROM san_pham_chi_tiet;
SELECT * FROM blog;
SELECT * FROM dia_chi_nguoi_dung;
SELECT * FROM gio_hang;
SELECT * FROM gio_hang_chi_tiet;
SELECT * FROM voucher_nguoi_dung;
SELECT * FROM lich_su_dang_nhap;
SELECT * FROM don_hang;
SELECT * FROM chi_tiet_don_hang;
SELECT * FROM san_pham_chi_tiet_khuyen_mai;
SELECT * FROM thong_ke_doanh_thu;
