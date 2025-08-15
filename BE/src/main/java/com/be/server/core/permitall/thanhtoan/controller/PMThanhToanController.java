package com.be.server.core.permitall.thanhtoan.controller;

import com.be.server.core.admin.khachhang.service.ADKhachHangService;
import com.be.server.core.admin.nhanvien.service.ADNhanVienService;
import com.be.server.core.permitall.thanhtoan.model.request.PMPhieuGiamGia;
import com.be.server.core.permitall.thanhtoan.model.request.PMThanhToan;
import com.be.server.core.permitall.thanhtoan.service.Impl.thanhtoanserviceImpl;
import com.be.server.entity.HoaDon;
import com.be.server.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class PMThanhToanController {

    @Autowired
    private final thanhtoanserviceImpl orderService;

    public final ADKhachHangService service;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody PMThanhToan order, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        System.out.println("Start createOrder - HoTen: " + order.getHoTen());
        HoaDon savedOrder = orderService.createOrder(order);
        System.out.println("Time to create order: " + (System.currentTimeMillis() - startTime) + "ms");

        if (savedOrder != null) {
            if ("VNPAY".equals(order.getHinhThucThanhToan())) {
                String ipAddr = request.getRemoteAddr();
                long vnpayStartTime = System.currentTimeMillis();
                Map<String, String> vnpayResponse = orderService.createVNPayPaymentUrl(savedOrder, ipAddr);
                System.out.println("Time to create VNPAY URL: " + (System.currentTimeMillis() - vnpayStartTime) + "ms");
                return ResponseEntity.ok(vnpayResponse);
            }
        }
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> handleVNPayReturn(@RequestParam Map<String, String> vnpParams) {
        String result = orderService.handleVNPayReturn(vnpParams);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/pgg")
    public ResponseEntity<?> getPGG(PMPhieuGiamGia req ) {
        return ResponseEntity.ok(orderService.getPhieuGiamGia(req));
    }

    @PostMapping("/pgg/list")
    public ResponseEntity<?> getAllApplicablePGG( PMPhieuGiamGia req) {

        System.out.println(req.getTongTien());

        return ResponseEntity.ok(orderService.getAllApplicablePGG(req.getIdKH(), req.getTongTien()));
    }

    @PostMapping("/khach-hang/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {

        return Helper.createResponseEntity(service.getKhachHangById(id));
    }

}
