
package com.example.duantotnghiep.repository;

import com.example.duantotnghiep.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    @Query("SELECT vc FROM Voucher vc WHERE vc.tenVoucher LIKE %:keyword% OR vc.maVoucher LIKE %:keyword%")
    List<Voucher> searchVoucher(@Param("keyword") String keyword);

    @Query("SELECT vc FROM Voucher vc WHERE vc.tenVoucher LIKE %:keyword% OR vc.maVoucher LIKE %:keyword%")
    Page<Voucher> searchVoucherWithPagination(@Param("keyword") String keyword, Pageable pageable);

    Page<Voucher> findAll(Pageable pageable);
}
