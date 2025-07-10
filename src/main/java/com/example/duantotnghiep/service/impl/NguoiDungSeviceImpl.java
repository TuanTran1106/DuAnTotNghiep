package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.entity.NguoiDung;
import com.example.duantotnghiep.repository.NguoiDungRepository;
import com.example.duantotnghiep.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NguoiDungSeviceImpl implements NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungRepository.findAll();
    }

    @Override
    public NguoiDung saveNguoiDung(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public NguoiDung getNguoiDungById(int id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteNguoiDung(int id) {
        nguoiDungRepository.deleteById(id);
    }

}
