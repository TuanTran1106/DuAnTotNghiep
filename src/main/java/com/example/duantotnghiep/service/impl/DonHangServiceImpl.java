package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.entity.DonHang;
import com.example.duantotnghiep.entity.TrangThaiDonHang;
import com.example.duantotnghiep.repository.*;
import com.example.duantotnghiep.service.DonHangService;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;
    private final TrangThaiDonHangRepository trangThaiDonHangRepository;

    @Override
    public List<DonHangDto> getAllOrders() {
        return donHangRepository.getOrder();
    }

    @Override
    public DonHangChiTietDto getOrderDetail(Integer orderId) {
        return null;
    }

    @Override
    public List<DonHangChiTietDto> getOrderProducts(Integer orderId) {
        return donHangRepository.getOrderProducts(orderId);
    }

    @Override
    public boolean nextOrderStatus(Integer orderId) {
        DonHang donHang = donHangRepository.findById(orderId).orElse(null);
        if (donHang == null) return false;
        TrangThaiDonHang current = donHang.getTrangThaiDonHang();
        if (current == null) return false;
        var allStatus = trangThaiDonHangRepository.findAll();
        allStatus.sort((a, b) -> a.getId() - b.getId());
        int idx = -1;
        for (int i = 0; i < allStatus.size(); i++) {
            if (allStatus.get(i).getId().equals(current.getId())) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return false;
        if (idx < allStatus.size() - 1) {
            TrangThaiDonHang next = allStatus.get(idx + 1);
            if ("Đã hủy".equalsIgnoreCase(next.getTenTrangThai())) {
                return false;
            }
            donHang.setTrangThaiDonHang(next);
            donHangRepository.save(donHang);
            return true;
        }
        return false;
    }

    @Override
    public Long countByTrangThai(String tenTrangThai) {
        return donHangRepository.countByTrangThai(tenTrangThai);
    }

//    @Override
//    public List<DonHangDto> getOrderDtosByIds(List<Integer> ids) {
//        return donHangRepository.getOrderByIds(ids);
//    }
//
//    @Override
//    public ByteArrayOutputStream generateOrdersPDFByDto(List<DonHangDto> donHangs) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, baos);
//            document.open();
//
//            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
//            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
//            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
//
//            document.add(new Paragraph("Danh sách đơn hàng", titleFont));
//            document.add(new Paragraph(" "));
//
//            PdfPTable table = new PdfPTable(9);
//            table.setWidthPercentage(100);
//            table.setWidths(new float[]{1.2f, 2.5f, 2.8f, 2.8f, 2.5f, 2.5f, 2.5f, 2.5f, 2.5f});
//
//            table.addCell(new PdfPCell(new Phrase("STT", headerFont)));
//            table.addCell(new PdfPCell(new Phrase("Mã đơn hàng", headerFont)));
//            table.addCell(new PdfPCell(new Phrase("Ngày mua", headerFont)));
//            table.addCell(new PdfPCell(new Phrase("Khách hàng", headerFont)));
//            table.addCell(new PdfPCell(new Phrase("SĐT khách", headerFont)));
//            table.addCell(new PdfPCell(new Phrase("Thành tiền", headerFont)));
//            table.addCell(new PdfPCell(new Phrase("Trạng thái", headerFont)));
//            table.addCell(new PdfPCell(new Phrase("Phương thức", headerFont)));
//            table.addCell(new PdfPCell(new Phrase("Voucher", headerFont)));
//
//            int stt = 1;
//            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//            java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
//            for (DonHangDto dh : donHangs) {
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(stt++), cellFont)));
//                table.addCell(new PdfPCell(new Phrase(dh.getMaDonHang() != null ? dh.getMaDonHang() : "", cellFont)));
//                String ngayMua = dh.getNgayMua() != null ? dtf.format(dh.getNgayMua()) : "";
//                table.addCell(new PdfPCell(new Phrase(ngayMua, cellFont)));
//                String tenKhach = dh.getTenKhachHang() != null ? dh.getTenKhachHang() : "";
//                table.addCell(new PdfPCell(new Phrase(tenKhach, cellFont)));
//                String sdt = dh.getSdtKhachHang() != null ? dh.getSdtKhachHang() : "";
//                table.addCell(new PdfPCell(new Phrase(sdt, cellFont)));
//                String tongGia = dh.getThanhTienSauVoucher() != null ? nf.format(dh.getThanhTienSauVoucher()) + " ₫"
//                        : (dh.getThanhTien() != null ? nf.format(dh.getThanhTien()) + " ₫" : "");
//                table.addCell(new PdfPCell(new Phrase(tongGia, cellFont)));
//                String trangThai = dh.getTenTrangThai() != null ? dh.getTenTrangThai() : "";
//                table.addCell(new PdfPCell(new Phrase(trangThai, cellFont)));
//                String phuongThuc = dh.getTenPhuongThuc() != null ? dh.getTenPhuongThuc() : "";
//                table.addCell(new PdfPCell(new Phrase(phuongThuc, cellFont)));
//                String voucher = dh.getVoucher() != null ? dh.getVoucher() : "";
//                table.addCell(new PdfPCell(new Phrase(voucher, cellFont)));
//            }
//            document.add(table);
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return baos;
//    }
}
