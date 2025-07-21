package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.entity.ThuongHieu;
import com.example.duantotnghiep.repository.ThuongHieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/thuong-hieu")
public class ThuongHieuRestController {
    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @PostMapping("/add")
    public ThuongHieu addThuongHieu(@RequestParam("tenThuongHieu") String tenThuongHieu) {
        ThuongHieu th = new ThuongHieu();
        th.setTenThuongHieu(tenThuongHieu);
        return thuongHieuRepository.save(th);
    }
} 