package com.example.duantotnghiep.service.impl;

import com.example.duantotnghiep.dto.DonHangChiTietDto;
import com.example.duantotnghiep.dto.DonHangDto;
import com.example.duantotnghiep.entity.DonHang;
import com.example.duantotnghiep.entity.TrangThaiDonHang;
import com.example.duantotnghiep.repository.*;
import com.example.duantotnghiep.service.DonHangService;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    @Override
    public List<DonHangDto> getOrderDtosByIds(List<Integer> ids) {
        return donHangRepository.getOrderByIds(ids);
    }

    @Override
    public ByteArrayOutputStream generateOrdersPDFByDto(List<DonHangDto> donHangs) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // In từng hóa đơn
            for (int orderIndex = 0; orderIndex < donHangs.size(); orderIndex++) {
                DonHangDto order = donHangs.get(orderIndex);
                List<DonHangChiTietDto> products = donHangRepository.getOrderProducts(order.getDonHangId());

                // Tạo hóa đơn cho từng đơn hàng
                createInvoicePage(document, order, products);

                // Thêm trang mới nếu không phải hóa đơn cuối cùng
                if (orderIndex < donHangs.size() - 1) {
                    document.newPage();
                }
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos;
    }

    @Override
    public ByteArrayOutputStream generateInvoicePDF(Integer orderId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Lấy thông tin đơn hàng
            DonHangDto order = donHangRepository.getOrderById(orderId);
            List<DonHangChiTietDto> products = donHangRepository.getOrderProducts(orderId);

            if (order == null) {
                throw new RuntimeException("Không tìm thấy đơn hàng");
            }

            // Tạo hóa đơn cho 1 đơn hàng
            createInvoicePage(document, order, products);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos;
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return "";
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    private void createInvoicePage(Document document, DonHangDto order, List<DonHangChiTietDto> products) throws DocumentException, IOException {
        // Fonts với encoding Unicode cho tiếng Việt
        BaseFont baseFont;
        try {
            // Thử sử dụng font Arial có sẵn
            baseFont = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            try {
                // Thử sử dụng font Times New Roman
                baseFont = BaseFont.createFont("C:/Windows/Fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } catch (Exception ex) {
                // Fallback về font mặc định với encoding Unicode
                baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            }
        }
        
        Font titleFont = new Font(baseFont, 18, Font.BOLD);
        Font subtitleFont = new Font(baseFont, 14, Font.BOLD);
        Font headerFont = new Font(baseFont, 12, Font.BOLD);
        Font normalFont = new Font(baseFont, 11, Font.NORMAL);
        Font smallFont = new Font(baseFont, 10, Font.NORMAL);

        // Header
        Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Thông tin đơn hàng
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{1f, 1f});

        // Cột trái - Thông tin đơn hàng
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(PdfPCell.NO_BORDER);
        leftCell.setPadding(5);

        Paragraph orderInfo = new Paragraph("THÔNG TIN ĐƠN HÀNG", subtitleFont);
        orderInfo.setSpacingAfter(10);
        leftCell.addElement(orderInfo);

        leftCell.addElement(new Paragraph("Mã đơn hàng: " + order.getMaDonHang(), normalFont));
        leftCell.addElement(new Paragraph("Ngày đặt: " + formatDateTime(order.getNgayMua()), normalFont));
        leftCell.addElement(new Paragraph("Trạng thái: " + order.getTenTrangThai(), normalFont));
        leftCell.addElement(new Paragraph("Phương thức TT: " + order.getTenPhuongThuc(), normalFont));

        // Cột phải - Thông tin khách hàng
        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(PdfPCell.NO_BORDER);
        rightCell.setPadding(5);

        Paragraph customerInfo = new Paragraph("THÔNG TIN KHÁCH HÀNG", subtitleFont);
        customerInfo.setSpacingAfter(10);
        rightCell.addElement(customerInfo);

        rightCell.addElement(new Paragraph("Tên khách hàng: " + (order.getTenKhachHang() != null ? order.getTenKhachHang() : "---"), normalFont));
        rightCell.addElement(new Paragraph("Số điện thoại: " + (order.getSdtKhachHang() != null ? order.getSdtKhachHang() : "---"), normalFont));
        rightCell.addElement(new Paragraph("Địa chỉ: " + (order.getDiaChi() != null ? order.getDiaChi() : "---"), normalFont));

        infoTable.addCell(leftCell);
        infoTable.addCell(rightCell);
        document.add(infoTable);
        document.add(new Paragraph(" "));

        // Bảng sản phẩm
        if (products != null && !products.isEmpty()) {
            PdfPTable productTable = new PdfPTable(6);
            productTable.setWidthPercentage(100);
            productTable.setWidths(new float[]{0.5f, 2f, 1f, 1f, 1f, 1f});

            // Header
            productTable.addCell(new PdfPCell(new Phrase("STT", headerFont)));
            productTable.addCell(new PdfPCell(new Phrase("Tên sản phẩm", headerFont)));
            productTable.addCell(new PdfPCell(new Phrase("Số lượng", headerFont)));
            productTable.addCell(new PdfPCell(new Phrase("Đơn giá", headerFont)));
            productTable.addCell(new PdfPCell(new Phrase("Thành tiền", headerFont)));
            productTable.addCell(new PdfPCell(new Phrase("Ghi chú", headerFont)));

            // Dữ liệu sản phẩm
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
            double totalAmount = 0;

            for (int i = 0; i < products.size(); i++) {
                DonHangChiTietDto product = products.get(i);
                double thanhTien = product.getThanhTien() != null ? product.getThanhTien().doubleValue() : 0;
                totalAmount += thanhTien;

                String productName = product.getTenSanPham() != null ? product.getTenSanPham() : "---";
                String variant = "";
                if (product.getMauSac() != null && !product.getMauSac().trim().isEmpty()) {
                    variant += product.getMauSac();
                }
                if (product.getKichThuoc() != null && !product.getKichThuoc().trim().isEmpty()) {
                    if (!variant.isEmpty()) variant += " - ";
                    variant += product.getKichThuoc();
                }
                if (!variant.isEmpty()) {
                    productName += " (" + variant + ")";
                }

                productTable.addCell(new PdfPCell(new Phrase(String.valueOf(i + 1), normalFont)));
                productTable.addCell(new PdfPCell(new Phrase(productName, normalFont)));
                productTable.addCell(new PdfPCell(new Phrase(String.valueOf(product.getSoLuongDat() != null ? product.getSoLuongDat() : 0), normalFont)));
                productTable.addCell(new PdfPCell(new Phrase(nf.format(product.getDonGia() != null ? product.getDonGia().doubleValue() : 0) + " ₫", normalFont)));
                productTable.addCell(new PdfPCell(new Phrase(nf.format(thanhTien) + " ₫", normalFont)));
                productTable.addCell(new PdfPCell(new Phrase("", normalFont)));
            }

            document.add(productTable);
            document.add(new Paragraph(" "));

            // Tổng tiền
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(50);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.setWidths(new float[]{1f, 1f});

            totalTable.addCell(new PdfPCell(new Phrase("Tổng tiền hàng:", headerFont)));
            totalTable.addCell(new PdfPCell(new Phrase(nf.format(totalAmount) + " ₫", headerFont)));

            // Voucher nếu có
            if (order.getVoucher() != null && !order.getVoucher().trim().isEmpty()) {
                totalTable.addCell(new PdfPCell(new Phrase("Voucher (" + order.getVoucher() + "):", normalFont)));
                double giamGia = (order.getThanhTien() != null ? order.getThanhTien().doubleValue() : 0) - (order.getThanhTienSauVoucher() != null ? order.getThanhTienSauVoucher().doubleValue() : 0);
                totalTable.addCell(new PdfPCell(new Phrase("-" + nf.format(giamGia) + " ₫", normalFont)));
            }

            totalTable.addCell(new PdfPCell(new Phrase("Thành tiền:", subtitleFont)));
            double finalAmount = order.getThanhTienSauVoucher() != null ? order.getThanhTienSauVoucher().doubleValue() : (order.getThanhTien() != null ? order.getThanhTien().doubleValue() : 0);
            totalTable.addCell(new PdfPCell(new Phrase(nf.format(finalAmount) + " ₫", subtitleFont)));

            document.add(totalTable);
        }

        // Footer
        document.add(new Paragraph(" "));
        Paragraph footer = new Paragraph("Cảm ơn quý khách đã mua hàng!", normalFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
}
