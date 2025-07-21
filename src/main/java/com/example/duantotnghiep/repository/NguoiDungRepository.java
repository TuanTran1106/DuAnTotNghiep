package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    Optional<NguoiDung> findBySdt(String sdt);

    @Query("SELECT nv FROM NguoiDung nv WHERE nv.hoTen LIKE %:keyword% OR nv.sdt LIKE %:keyword%")
    List<NguoiDung> searchNguoiDung(@Param("keyword") String keyword);

    @Query("SELECT nv FROM NguoiDung nv WHERE nv.hoTen LIKE %:keyword% OR nv.sdt LIKE %:keyword%")
    Page<NguoiDung> searchNguoiDungWithPagination(@Param("keyword") String keyword, Pageable pageable);

    Page<NguoiDung> findAll(Pageable pageable);

    @Query("SELECT COUNT(nd) FROM NguoiDung nd WHERE nd.ngayTao BETWEEN :startDate AND :endDate")
    Long countNewUsersBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
