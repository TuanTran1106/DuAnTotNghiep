package com.example.duantotnghiep.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phan_quyen")
public class PhanQuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name", length = 50)
    private String roleName;

    // Constructors
    public PhanQuyen() {}

    public PhanQuyen(Integer id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
