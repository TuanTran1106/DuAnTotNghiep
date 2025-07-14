package com.example.duantotnghiep.service;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;

import java.util.List;

public interface DonHangService {

    List<DonHangDto> getAllOrders();

    DonHangChiTietDto getOrderDetail(Integer orderId);

    List<DonHangChiTietDto> getOrderProducts(Integer orderId);

    boolean nextOrderStatus(Integer orderId);
}
