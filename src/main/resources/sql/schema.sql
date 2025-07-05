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
                         kieu_giam BIT,
                         muc_giam DECIMAL(18,2),
                         so_luong INT,
                         so_luong_con INT,
                         ngay_bat_dau DATE,
                         ngay_ket_thuc DATE,
                         dieu_kien_toi_thieu DECIMAL(18,2),
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
                          id_san_pham_chi_tiet INT,
                          ngay_them DATETIME DEFAULT GETDATE(),
                          FOREIGN KEY (id_nguoi_dung) REFERENCES nguoi_dung(id),
                          FOREIGN KEY (id_san_pham_chi_tiet) REFERENCES san_pham_chi_tiet(id)
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
