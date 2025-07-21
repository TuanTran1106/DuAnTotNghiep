package com.example.duantotnghiep.controller;

import com.example.duantotnghiep.entity.DanhMuc;
import com.example.duantotnghiep.repository.DanhMucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/danh-muc")
public class DanhMucRestController {
    @Autowired
    private DanhMucRepository danhMucRepository;

    @PostMapping("/add")
    public DanhMuc addDanhMuc(@RequestParam("tenDanhMuc") String tenDanhMuc) {
        DanhMuc dm = new DanhMuc();
        dm.setTenDanhMuc(tenDanhMuc);
        return danhMucRepository.save(dm);
    }
} 