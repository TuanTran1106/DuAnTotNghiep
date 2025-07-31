package com.example.duantotnghiep.service;

import com.example.duantotnghiep.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BlogService {
    Blog addBlog(Blog blog);
    Blog updateBlog(Integer id, Blog blog);
    void deleteBlog(Integer id);
    Optional<Blog> getBlogById(Integer id);
    Page<Blog> getAllBlogs(Pageable pageable);
    Page<Blog> searchBlogs(String keyword, Pageable pageable);
} 