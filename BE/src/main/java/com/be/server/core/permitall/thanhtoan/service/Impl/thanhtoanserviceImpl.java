package com.be.server.core.permitall.thanhtoan.service.Impl;

import com.be.server.core.admin.SanPhamChiTiet.repository.ADSanPhamChiTietRepository;
import com.be.server.core.admin.banhang.model.request.ADThemSanPhamRequest;
import com.be.server.core.admin.banhang.repository.ADTaoHoaDonChiTietRepository;
import com.be.server.core.admin.hoadon.repository.ADLichSuThanhToanRepository;
import com.be.server.core.admin.khachhang.repository.ADKhachHangRepository;
import com.be.server.core.admin.voucher.repository.ADVoucherRepository;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.core.permitall.thanhtoan.model.request.PMPhieuGiamGia;
import com.be.server.core.permitall.thanhtoan.model.request.PMThanhToan;
import com.be.server.core.permitall.thanhtoan.repository.PMChiTietGioHangRepository;
import com.be.server.core.permitall.thanhtoan.repository.PMHoaDonReposiitory;
import com.be.server.core.permitall.thanhtoan.repository.PMPhieuGiamGiaThanhToan;
import com.be.server.core.permitall.thanhtoan.service.ThanhToanService;
import com.be.server.core.vnpay.VNPayConfig;
import com.be.server.core.vnpay.VNPayUtil;
import com.be.server.entity.*;
import com.be.server.infrastructure.constant.EntityLoaiHoaDon;
import com.be.server.infrastructure.constant.EntityPhuongThucThanhToan;
import com.be.server.infrastructure.constant.EntityStatus;
import com.be.server.infrastructure.constant.EntityTrangThaiHoaDon;
import com.be.server.repository.CartRepository;
import com.be.server.repository.KhachHangRepository;
import com.be.server.repository.LichSuTrangThaiHoaDonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
public class thanhtoanserviceImpl {
    public final ADSanPhamChiTietRepository adSanPhamRepository;
    public final ADTaoHoaDonChiTietRepository adTaoHoaDonChiTietRepository;
    private final PMHoaDonReposiitory pmHoaDonReposiitory;
    private final LichSuTrangThaiHoaDonRepository lichSuTrangThaiHoaDonRepository;
    private final ADLichSuThanhToanRepository adLichSuThanhToanRepository;
    public final ADVoucherRepository adVoucherRepository;
    public final ADKhachHangRepository adKhachHangRepository;
    public final PMPhieuGiamGiaThanhToan pmPhieuGiamGiaThanhToan;
    private final CartRepository cartRepository;
    private final PMChiTietGioHangRepository pmChiTietGioHangRepository;
    @Autowired
    private VNPayConfig vnpayConfig;

    public HoaDon createOrder(PMThanhToan order) {

        HoaDon hoaDon = new HoaDon();

        hoaDon.setSdt(order.getSoDienThoai());

        hoaDon.setTenKH(order.getHoTen());

        hoaDon.setDiaChi(order.getDiaChi());

        hoaDon.setLoaiHoaDon(EntityLoaiHoaDon.ONLINE);

        hoaDon.setTongTien(order.getTongTien());

        hoaDon.setTongTienSauGiam(order.getTongCong());

        hoaDon.setStatus(EntityStatus.ACTIVE);

        hoaDon.setPhuongThucThanhToan(order.getHinhThucThanhToan() == "VNPAY" ? EntityPhuongThucThanhToan.CHUYEN_KHOAN : EntityPhuongThucThanhToan.TIEN_MAT);

        hoaDon.setGhiChu(order.getGhiChu());

        hoaDon.setPhiVanChuyen(order.getPhiShip());

        hoaDon.setTrangThaiHoaDon(EntityTrangThaiHoaDon.CHO_XAC_NHAN);

//        LichSuTrangThaiHoaDon lichSuTrangThaiHoaDon = new LichSuTrangThaiHoaDon();
//
//        lichSuTrangThaiHoaDon.setThoiGian(LocalDateTime.now());
//
//        lichSuTrangThaiHoaDon.setNote("Đơn hàng đã được đặt và chờ được xác nhận.");
//
//        lichSuTrangThaiHoaDon.setHoaDon(hoaDon);
//
//        lichSuTrangThaiHoaDon.setTrangThai(EntityTrangThaiHoaDon.DA_XAC_NHAN);
//
//        lichSuTrangThaiHoaDonRepository.save(lichSuTrangThaiHoaDon);
//
//        LichSuThanhToan lichSu = new LichSuThanhToan();
//        lichSu.setHoaDon(hoaDon);
//        lichSu.setSoTien(order.getTongTien());
//        if(order.getHinhThucThanhToan() == "VNPAY") {
//            lichSu.setLoaiGiaoDich("CHUYEN_KHOAN");
//        }
//        else {
//            lichSu.setLoaiGiaoDich("TIEN_MAT");
//        }
//        lichSu.setThoiGian(LocalDateTime.now());
//        lichSu.setMaGiaoDich(UUID.randomUUID().toString());
//
//        adLichSuThanhToanRepository.save(lichSu);

        System.out.println("đã chạy đến lưu hóa dơn1");
        System.out.println("MaGiamGia: " + order.getMaGiamGia());
        if (order.getMaGiamGia() != null && !order.getMaGiamGia().isEmpty() && !order.getMaGiamGia().equals("")) {
            System.out.println("pgg" + order.getMaGiamGia());
            String idPGG = pmPhieuGiamGiaThanhToan.getPGG(order.getMaGiamGia());

            if (idPGG == null) {
                PhieuGiamGia phieuGiamGia = adVoucherRepository.findById(idPGG).get();
                System.out.println("đã chạy đến lưu hóa đơn14");
                phieuGiamGia.setSoLuongPhieu(phieuGiamGia.getSoLuongPhieu() - 1);
                System.out.println("đã chạy đến lưu hóa đơn16");
                pmPhieuGiamGiaThanhToan.save(phieuGiamGia);

                hoaDon.setVoucher(phieuGiamGia);
            }
        }
        System.out.println("đã chạy đến lưu hóa dơn1");
        if (order.getKhachHang() != null) {

            if (order.getKhachHang().equals("khách lẻ")) {

            } else {

                KhachHang voucher = adKhachHangRepository.findById(order.getKhachHang()).get();

                hoaDon.setKhachHang(voucher);
            }

        }

        pmHoaDonReposiitory.save(hoaDon);

        System.out.println("đã chạy đến lưu hóa dơn1");

        System.out.println("sp" + order.getSanPham());
        if (order.getSanPham() != null) {
            System.out.println("sp");
            for (int i = 0; i < order.getSanPham().size(); i++) {

                ADThemSanPhamRequest request = new ADThemSanPhamRequest();

                request.setIdHD(hoaDon.getId());
                System.out.println("1");
                request.setIdSP(order.getSanPham().get(i).getId());
                System.out.println("2");
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                System.out.println("3");
                hoaDonChiTiet.setHoaDon(hoaDon);
                System.out.println("4");
                System.out.println(order.getSanPham().get(i).getId());
                SanPhamChiTiet sanPhamChiTiet = adSanPhamRepository.findById(order.getSanPham().get(i).getId()).get();
                System.out.println(sanPhamChiTiet.getId());
                hoaDonChiTiet.setSpct(sanPhamChiTiet);
                System.out.println("6");
                hoaDonChiTiet.setStatus(EntityStatus.ACTIVE);
                System.out.println("7");
                hoaDonChiTiet.setSoLuong(order.getSanPham().get(i).getQuantity());
                System.out.println("8");
                System.out.println(hoaDonChiTiet);
                hoaDonChiTiet.setGia(sanPhamChiTiet.getGiaBan()*order.getSanPham().get(i).getQuantity());
                adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);

                System.out.println(sanPhamChiTiet.getSoLuong());
                System.out.println(order.getSanPham().get(i).getQuantity());
                if (sanPhamChiTiet.getSoLuong() < order.getSanPham().get(i).getQuantity()) {
                    System.out.println("00");
                    return null;
                }

                System.out.println("0");
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - order.getSanPham().get(i).getQuantity());
                System.out.println("1");
                adSanPhamRepository.save(sanPhamChiTiet);
                System.out.println("2");
                System.out.println(order.getKhachHang());


                if (!order.getKhachHang().equals("khách lẻ")) {
                    System.out.println("đã chạy đênns hóa dơn chi tiết 2");
                    KhachHang khachHang1  =  adKhachHangRepository.findById(order.getKhachHang()).get();
                    String idCart = cartRepository.findByIdKH(khachHang1.getId());
                    Cart cart = cartRepository.findById(idCart).get();
                    String  idGHCT = pmChiTietGioHangRepository.getIDGioHang(sanPhamChiTiet.getId() , cart.getId());
                    CartDetail cartDetail = pmChiTietGioHangRepository.findById(idGHCT).get();
                    pmChiTietGioHangRepository.deleteById(cartDetail.getId());
                }

                System.out.println("đã chạy đênns hóa dơn chi tiết 2");
            }


        }

        System.out.println("đã chạy đến lưu hóa dơn2");

        return hoaDon;
    }

    public Map<String, String> createVNPayPaymentUrl(HoaDon order, String ipAddr) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "Thanh toan don hang " + order.getId();
        String vnp_OrderType = "billpayment";
        String vnp_TxnRef = String.valueOf(order.getId());
        String vnp_IpAddr = ipAddr;
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String vnp_ExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(
                new Date(System.currentTimeMillis() + 15 * 60 * 1000) // 15 minutes from now
        );

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnpayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((long) (order.getTongTienSauGiam() * 100))); // VNPAY requires amount in VND * 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Create secure hash
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            hashData.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)).append("&");
        }
        hashData.deleteCharAt(hashData.length() - 1); // Remove last '&'
        String secureHash = VNPayUtil.hmacSHA512(vnpayConfig.vnp_HashSecret, hashData.toString());
        vnp_Params.put("vnp_SecureHash", secureHash);

        // Build payment URL
        StringBuilder paymentUrl = new StringBuilder(vnpayConfig.vnp_PayUrl).append("?");
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            paymentUrl.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        }
        paymentUrl.deleteCharAt(paymentUrl.length() - 1); // Remove last '&'

        Map<String, String> result = new HashMap<>();
        result.put("orderId", String.valueOf(order.getId()));
        result.put("paymentUrl", paymentUrl.toString());
        return result;
    }

    public String handleVNPayReturn(Map<String, String> vnpParams) {
        String vnp_SecureHash = vnpParams.get("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHash");

        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : new TreeMap<>(vnpParams).entrySet()) {
            hashData.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)).append("&");
        }
        hashData.deleteCharAt(hashData.length() - 1);
        String calculatedHash = VNPayUtil.hmacSHA512(vnpayConfig.vnp_HashSecret, hashData.toString());

        if (calculatedHash.equals(vnp_SecureHash)) {
            String vnp_ResponseCode = vnpParams.get("vnp_ResponseCode");
            String orderId = vnpParams.get("vnp_TxnRef");
            if ("00".equals(vnp_ResponseCode)) {
                // Update order status to PAID (implement based on your needs)
                return "Thanh toán thành công cho đơn hàng " + orderId;
            } else {
                return "Thanh toán thất bại cho đơn hàng " + orderId;
            }
        } else {
            return "Invalid secure hash";
        }
    }

    public ResponseObject<?> getPhieuGiamGia(PMPhieuGiamGia req) {

        if (pmPhieuGiamGiaThanhToan.getPGG(req.getMaPGG()) != null) {
            String idPGG = pmPhieuGiamGiaThanhToan.getPGG(req.getMaPGG());
            PhieuGiamGia phieuGiamGia = adVoucherRepository.findById(idPGG).get();
            if (phieuGiamGia.getStatus() == EntityStatus.INACTIVE) {
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Phiếu giảm giá này đã hết hạn sử dụng");
            } else {
                if (phieuGiamGia.getSoLuongPhieu() <= 0) {
                    return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Phiếu giảm giá này đã hết");
                } else {
                    if (phieuGiamGia.getLoaiGiam() == true) {
                        if (pmPhieuGiamGiaThanhToan.checkKhachHang(idPGG, req.getIdKH()) == null) {
                            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Phiếu giảm giá không áp dụng cho tài khoản này");
                        } else {
                            if (phieuGiamGia.getDieuKien() > req.getTongTien()) {
                                Double giaTri = phieuGiamGia.getDieuKien() - req.getTongTien();
                                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Đơn hàng chưa đủ để áp dụng phiếu giảm giá hãy mua thêm " + giaTri + "đ để áp phiếu giảm giá");
                            } else {
                                return new ResponseObject<>(phieuGiamGia, HttpStatus.NOT_FOUND, "Áp dụng phiếu giảm giá thành công");
                            }
                        }
                    } else {
                        if (phieuGiamGia.getDieuKien() > req.getTongTien()) {
                            Double giaTri = phieuGiamGia.getDieuKien() - req.getTongTien();
                            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Đơn hàng chưa đủ để áp dụng phiếu giảm giá hãy mua thêm " + giaTri + "đ để áp phiếu giảm giá");
                        } else {
                            return new ResponseObject<>(phieuGiamGia, HttpStatus.NOT_FOUND, "Áp dụng phiếu giảm giá thành công");
                        }
                    }
                }
            }
        } else {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Phiếu giảm giá không tồn tại");
        }

    }

    ;

}
