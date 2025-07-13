package com.be.server.core.permitall.sanpham.controller;

import com.be.server.core.permitall.sanpham.dto.request.PMSanPhamMoiSearchRequest;
import com.be.server.core.permitall.sanpham.service.PMSanPhamService;
import com.be.server.infrastructure.constant.MappingConstants;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(MappingConstants.API_PERMITALL_SANPHAM)
public class PMSanPhamController {

    public final PMSanPhamService PMSanPhamService;

    @GetMapping("/get-all/san-pham-giam-gia")
    public ResponseEntity<?> getAllSanPhamGiamGia(PMSanPhamMoiSearchRequest request) {
        return Helper.createResponseEntity(PMSanPhamService.getAllSanPhamGiamGia(request));
    }

    @GetMapping("/get-all/san-pham-moi")
    public ResponseEntity<?> getAllSanPhamMoi(PMSanPhamMoiSearchRequest request) {
        return Helper.createResponseEntity(PMSanPhamService.getAllSanPhamMoi(request));
    }

}
