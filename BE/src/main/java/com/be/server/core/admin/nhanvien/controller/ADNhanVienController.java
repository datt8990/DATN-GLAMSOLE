package com.be.server.core.admin.nhanvien.controller;

import com.be.server.core.admin.nhanvien.model.request.ADNhanVienRequest;
import com.be.server.core.admin.nhanvien.model.request.ADNhanVienSearchRequest;
import com.be.server.core.admin.nhanvien.service.ADNhanVienService;
import com.be.server.infrastructure.constant.MappingConstants;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(MappingConstants.API_ADMIN_NHAN_VIEN)
@Slf4j
@CrossOrigin(origins = "*")
public class ADNhanVienController {

    public final ADNhanVienService  service;

    @GetMapping
    public ResponseEntity<?> getALl(ADNhanVienSearchRequest request) {
        return Helper.createResponseEntity(service.getAllNhanVien(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {

        return Helper.createResponseEntity(service.getNhanVienById(id));
    }

    @PostMapping
    public ResponseEntity<?> modifyProduct(@ModelAttribute ADNhanVienRequest request) {

        return Helper.createResponseEntity(service.modifyMNhanVien(request));
    }

    @PutMapping("/{id}/change-status")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String id) {
        return Helper.createResponseEntity(service.changeNhanVienStatus(id));
    }
}

