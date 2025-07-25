package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_san_pham")
    private String maSanPham;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, message = "Tên sản phẩm phải có ít nhất 3 ký tự")

    @Column(name = "ten_san_pham")
    private String tenSanPham;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @NotNull(message = "Giá nhập không được để trống")
    @DecimalMin(value = "0.01", message = "Giá nhập phải lớn hơn 0")
    @Column(name = "gia_nhap")
    private BigDecimal giaNhap;

    @Column(name = "ngay_nhap")
    private LocalDateTime ngayNhap;

    @Column(name = "ngay_sua")
    private LocalDateTime ngaySua;

    @Column(name = "mo_ta")
    private String moTa;

    @NotNull(message = "Trạng thái không được để trống")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @NotNull(message = "Thương hiệu không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu")
    private ThuongHieu thuongHieu;

    @NotNull(message = "Danh mục không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_danh_muc")
    private DanhMuc danhMuc;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Transient
    private List<SanPhamChiTiet> chiTietList;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public BigDecimal getGiaNhap() { return giaNhap; }
    public void setGiaNhap(BigDecimal giaNhap) { this.giaNhap = giaNhap; }

    public LocalDateTime getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDateTime ngayNhap) { this.ngayNhap = ngayNhap; }

    public LocalDateTime getNgaySua() { return ngaySua; }
    public void setNgaySua(LocalDateTime ngaySua) { this.ngaySua = ngaySua; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }

    public ThuongHieu getThuongHieu() { return thuongHieu; }
    public void setThuongHieu(ThuongHieu thuongHieu) { this.thuongHieu = thuongHieu; }

    public DanhMuc getDanhMuc() { return danhMuc; }
    public void setDanhMuc(DanhMuc danhMuc) { this.danhMuc = danhMuc; }

    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public List<SanPhamChiTiet> getChiTietList() { return chiTietList; }
    public void setChiTietList(List<SanPhamChiTiet> chiTietList) { this.chiTietList = chiTietList; }
}
