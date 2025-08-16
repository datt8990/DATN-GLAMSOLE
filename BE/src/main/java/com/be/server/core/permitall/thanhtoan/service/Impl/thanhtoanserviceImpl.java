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
import com.be.server.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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

        System.out.println(order.getHinhThucThanhToan());

        hoaDon.setPhuongThucThanhToan(order.getHinhThucThanhToan().equals("VNPAY")  ? EntityPhuongThucThanhToan.CHUYEN_KHOAN : EntityPhuongThucThanhToan.TIEN_MAT);

        System.out.println(order.getMaGiamGia());

        System.out.println(hoaDon.getPhuongThucThanhToan());

        hoaDon.setEmail(order.getEmail());

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

        if (order.getMaGiamGia() != null && !order.getMaGiamGia().isEmpty() && !order.getMaGiamGia().equals("")) {

            String idPGG = pmPhieuGiamGiaThanhToan.getPGG(order.getMaGiamGia());

            if (idPGG == null) {
                PhieuGiamGia phieuGiamGia = adVoucherRepository.findById(idPGG).get();

                phieuGiamGia.setSoLuongPhieu(phieuGiamGia.getSoLuongPhieu() - 1);

                pmPhieuGiamGiaThanhToan.save(phieuGiamGia);

                hoaDon.setVoucher(phieuGiamGia);
            }
        }

        System.out.println('1');

        if (order.getKhachHang() != null) {

            if (order.getKhachHang().equals("khách lẻ")) {

            } else {

                KhachHang voucher = adKhachHangRepository.findById(order.getKhachHang()).get();

                hoaDon.setKhachHang(voucher);
            }

        }

        pmHoaDonReposiitory.save(hoaDon);


        if (order.getSanPham() != null) {

            for (int i = 0; i < order.getSanPham().size(); i++) {

                ADThemSanPhamRequest request = new ADThemSanPhamRequest();

                request.setIdHD(hoaDon.getId());

                request.setIdSP(order.getSanPham().get(i).getId());

                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

                hoaDonChiTiet.setHoaDon(hoaDon);

                SanPhamChiTiet sanPhamChiTiet = adSanPhamRepository.findById(order.getSanPham().get(i).getId()).get();

                hoaDonChiTiet.setSpct(sanPhamChiTiet);

                hoaDonChiTiet.setStatus(EntityStatus.ACTIVE);

                hoaDonChiTiet.setSoLuong(order.getSanPham().get(i).getQuantity());

                System.out.println(hoaDonChiTiet);
                hoaDonChiTiet.setGia(sanPhamChiTiet.getGiaBan()*order.getSanPham().get(i).getQuantity());
                adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);

                System.out.println(sanPhamChiTiet.getSoLuong());
                System.out.println(order.getSanPham().get(i).getQuantity());
                if (sanPhamChiTiet.getSoLuong() < order.getSanPham().get(i).getQuantity()) {

                    return null;
                }


                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - order.getSanPham().get(i).getQuantity());

                adSanPhamRepository.save(sanPhamChiTiet);

                System.out.println(order.getKhachHang());


                if (!order.getKhachHang().equals("khách lẻ")) {

                    KhachHang khachHang1  =  adKhachHangRepository.findById(order.getKhachHang()).get();
                    String idCart = cartRepository.findByIdKH(khachHang1.getId());

                    Cart cart = cartRepository.findById(idCart).get();

                    String  idGHCT = pmChiTietGioHangRepository.getIDGioHang(sanPhamChiTiet.getId() , cart.getId());

                    if(idGHCT != null){
                        CartDetail cartDetail = pmChiTietGioHangRepository.findById(idGHCT).get();

                        pmChiTietGioHangRepository.deleteById(cartDetail.getId());
                    }

                }

            }


        }

        String email = order.getEmail();
        String subject = "Thanh toán đơn hàng thành công";

        String trackingUrl = "http://localhost:6688/don-mua-detail/" + hoaDon.getMa() + "/" + hoaDon.getId();

        String content =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border:1px solid #e0e0e0; border-radius:8px; overflow:hidden;'>"
                        + "  <div style='background:#fff; padding:20px; text-align:center;'>"
                        + "    <h2 style='color:#333; margin:0;'>GLAMSOLE</h2>"
                        + "    <p style='color:#666; margin:8px 0 0;'>Đơn đặt hàng số: <b>" + hoaDon.getMa() + "</b></p>"
                        + "  </div>"
                        + "  <hr style='border:none; border-top:1px solid #eee;'/>"
                        + "  <div style='padding:20px;'>"
                        + "    <h3 style='color:#333; margin-top:0;'>Cảm ơn bạn đã đặt hàng</h3>"
                        + "    <p style='color:#555;'>Xin chào <b>" + order.getHoTen() + "</b>, đơn hàng của bạn đã được thanh toán thành công và sẵn sàng để vận chuyển.</p>"
                        + "    <p style='color:#555;'>Bạn có thể theo dõi trạng thái đơn hàng bằng cách nhấn vào nút bên dưới:</p>"
                        + "    <div style='text-align:center; margin:30px 0;'>"
                        + "      <a href='" + trackingUrl + "' style='background:#f5c542; color:#000; text-decoration:none; padding:12px 20px; border-radius:6px; font-weight:bold;'>Xem đơn đặt hàng</a>"
                        + "    </div>"
                        + "    <p style='color:#777; font-size:14px;'>-- GLAMSOLE team --</p>"
                        + "  </div>"
                        + "</div>";

        CompletableFuture.runAsync(() -> EmailService.sendEmail(email, subject, content));



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

    ;public ResponseObject<?> getAllApplicablePGG(String idKH, Double tongTien) {
        List<PhieuGiamGia> allPGG = adVoucherRepository.findAvailableVouchers(idKH); // Viết query chỉ lấy voucher ACTIVE
        List<PhieuGiamGia> applicable = new ArrayList<>();

        for (PhieuGiamGia pgg : allPGG) {
            if (pgg.getSoLuongPhieu() <= 0) continue; // Hết số lượng
            if (pgg.getDieuKien() > tongTien) continue; // Không đủ điều kiện tối thiểu

            // Tính giá trị giảm thực tế
            double giamThucTe;
            if (pgg.getKieuGiam() == false) {
                giamThucTe = pgg.getGiaGiam();
            } else {
                giamThucTe = Math.min((tongTien * pgg.getPhanTramGiam() / 100), pgg.getGiaGiam());
            }
            pgg.setGiaTriGiamThucTe(giamThucTe);
            applicable.add(pgg);
        }

        // Sắp xếp để voucher tốt nhất đứng đầu
        applicable.sort((a, b) -> Double.compare(b.getGiaTriGiamThucTe(), a.getGiaTriGiamThucTe()));

        return new ResponseObject<>(applicable, HttpStatus.OK, "Danh sách phiếu giảm giá hợp lệ");
    }


}
