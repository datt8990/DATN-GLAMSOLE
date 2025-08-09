package com.be.server.core.permitall.donmua.controller;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.permitall.donmua.service.DonMuaService;
import com.be.server.infrastructure.constant.MappingConstants;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(MappingConstants.API_PERMITALL_ORDER_HISTORY)
public class DonMuaController {

    public final DonMuaService service;

    @GetMapping
    public ResponseEntity<?> getAll(ADHoaDonSearchRequest request) {
        return Helper.createResponseEntity(service.getAllHoaDon(request));
    }
}
