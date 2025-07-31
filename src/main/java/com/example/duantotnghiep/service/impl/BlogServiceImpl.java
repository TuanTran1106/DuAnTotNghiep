package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.entity.Blog;
import com.example.duantotnghiep.repository.BlogRepository;
import com.example.duantotnghiep.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;

    @Override
    public Blog addBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Integer id, Blog blog) {
        blog.setId(id);
        return blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(Integer id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Optional<Blog> getBlogById(Integer id) {
        return blogRepository.findById(id);
    }

    @Override
    public Page<Blog> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> searchBlogs(String keyword, Pageable pageable) {
        return blogRepository.findByTieuDeContainingIgnoreCaseOrNoiDungContainingIgnoreCase(keyword, keyword, pageable);
    }
} 