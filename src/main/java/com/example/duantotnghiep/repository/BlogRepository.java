package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findByTieuDeContainingIgnoreCaseOrNoiDungContainingIgnoreCase(String tieuDe, String noiDung, Pageable pageable);
} 