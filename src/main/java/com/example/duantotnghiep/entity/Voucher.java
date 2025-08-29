package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "voucher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Mã voucher không được để trống")
    @Column(name = "ma_voucher")
    private String maVoucher;

    @NotBlank(message = "Tên voucher không được để trống")
    @Size(min = 3, max = 150, message = "Tên voucher phải từ 3 đến 150 ký tự")
    @Column(name = "ten_voucher")
    private String tenVoucher;

    @Size(max = 255, message = "Mô tả không được quá 255 ký tự")
    @Column(name = "mo_ta")
    private String moTa;

    @NotNull(message = "Kiểu giảm không được để trống")
    @Column(name = "kieu_giam")
    private Boolean kieuGiam;

    @NotNull(message = "Mức giảm không được để trống")
    @DecimalMin(value = "0.01", message = "Mức giảm phải lớn hơn 0")
    @Column(name = "muc_giam")
    private BigDecimal mucGiam;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "so_luong_con")
    private Integer soLuongCon;

    //    @FutureOrPresent(message = "Ngày bắt đầu phải là hiện tại hoặc tương lai")
    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
//    @Future(message = "Ngày kết thúc phải là tương lai")
    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @DecimalMin(value = "0", message = "Điều kiện tối thiểu không được âm")
    @Column(name = "dieu_kien_toi_thieu")
    private BigDecimal dieuKienToiThieu;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    /**
     * Custom validation method để kiểm tra ngày kết thúc phải sau ngày bắt đầu
     */
    @AssertTrue(message = "Ngày kết thúc phải không trước ngày bắt đầu")
    public boolean isNgayKetThucSauHoacBangNgayBatDau() {
        if (ngayBatDau == null || ngayKetThuc == null) {
            return true; // Skip validation if either date is null
        }
        return !ngayKetThuc.isBefore(ngayBatDau);
    }
}
