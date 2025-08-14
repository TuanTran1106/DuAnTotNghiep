package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "khuyen_mai")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_khuyen_mai")
    private String maKhuyenMai;

    @NotBlank(message = "Tên chương trình không được để trống")
    @Size(min = 3, max = 150, message = "Tên chương trình phải từ 3 đến 150 ký tự")
    @Column(name = "ten_chuong_trinh")
    private String tenChuongTrinh;

    @Size(max = 255, message = "Mô tả không được quá 255 ký tự")
    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hiện tại hoặc tương lai")
    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải là tương lai")
    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @Column(name = "so_luong")
    private Integer soLuong;

    @NotNull(message = "Kiểu giảm không được để trống")
    @Column(name = "kieu_giam")
    private Boolean kieuGiam;

    @NotNull(message = "Mức giảm giá không được để trống")
    @DecimalMin(value = "0.01", message = "Mức giảm giá phải lớn hơn 0")
    @Column(name = "muc_giam_gia")
    private BigDecimal mucGiamGia;

    @Column(name = "trang_thai")
    private Integer trangThai;
    

    @AssertTrue(message = "Ngày kết thúc phải sau ngày bắt đầu")
    public boolean isNgayKetThucAfterNgayBatDau() {
        if (ngayBatDau == null || ngayKetThuc == null) {
            return true; // Skip validation if either date is null
        }
        return ngayKetThuc.isAfter(ngayBatDau);
    }
}

