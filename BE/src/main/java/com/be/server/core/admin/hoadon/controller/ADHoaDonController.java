package com.be.server.core.admin.hoadon.controller;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.service.ADHoaDonService;
import com.be.server.infrastructure.constant.MappingConstants;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(MappingConstants.API_ADMIN_HOA_DON)
@Slf4j
@CrossOrigin(origins = "*")
public class ADHoaDonController {

    public final ADHoaDonService service;

    @GetMapping
    public ResponseEntity<?> test(ADHoaDonSearchRequest request) {
        return Helper.createResponseEntity(service.getAllHoaDon(request));
    }

    @GetMapping("/all/{maHoaDon}")
    public ResponseEntity<?> getHDCT(@PathVariable String maHoaDon) {
        return Helper.createResponseEntity(service.getAllHoaDonCT(maHoaDon));
    }

}

