package com.example.duantotnghiep.dto;

import java.math.BigDecimal;

public class SanPhamVariantDto {
    private String mauSac;
    private String kichThuoc;
    private String chatLieu;
    private Integer soLuong;
    private BigDecimal giaBan;

    // Constructors
    public SanPhamVariantDto() {}

    public SanPhamVariantDto(String mauSac, String kichThuoc, String chatLieu, Integer soLuong, BigDecimal giaBan) {
        this.mauSac = mauSac;
        this.kichThuoc = kichThuoc;
        this.chatLieu = chatLieu;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    // Getters and Setters
    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    @Override
    public String toString() {
        return "SanPhamVariantDto{" +
                "mauSac='" + mauSac + '\'' +
                ", kichThuoc='" + kichThuoc + '\'' +
                ", chatLieu='" + chatLieu + '\'' +
                ", soLuong=" + soLuong +
                ", giaBan=" + giaBan +
                '}';
    }
}
