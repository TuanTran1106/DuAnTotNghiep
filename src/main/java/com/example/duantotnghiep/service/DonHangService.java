package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface DonHangService {

    List<DonHangDto> getAllOrders();

    DonHangChiTietDto getOrderDetail(Integer orderId);

    List<DonHangChiTietDto> getOrderProducts(Integer orderId);

    boolean nextOrderStatus(Integer orderId);


    // trang chu
    Long countByTrangThai(String tenTrangThai);

    // in pdf
    List<DonHangDto> getOrderDtosByIds(List<Integer> ids);
    ByteArrayOutputStream generateOrdersPDFByDto(List<DonHangDto> donHangs);
    ByteArrayOutputStream generateInvoicePDF(Integer orderId);

    Integer getLatestOrderId();
}
