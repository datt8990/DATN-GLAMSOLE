package com.be.server.core.permitall.chitietsanpham.service.impl;

import com.be.server.core.common.base.ResponseObject;
import com.be.server.core.permitall.chitietsanpham.dto.request.PMSanPhamChiTietRequest;
import com.be.server.core.permitall.chitietsanpham.service.PMSanPhamChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class PMSanPhamChiTietServiceImpl implements PMSanPhamChiTietService {

    @Override
    public ResponseObject<?> getSanPhamChiTiet(PMSanPhamChiTietRequest request) {
        return null;
    }

}
