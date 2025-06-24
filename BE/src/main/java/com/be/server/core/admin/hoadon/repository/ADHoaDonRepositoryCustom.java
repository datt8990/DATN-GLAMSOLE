package com.be.server.core.admin.hoadon.repository;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponse;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ADHoaDonRepositoryCustom {
    Page<ADHoaDonResponse> getAllHoaDonResponse(ADHoaDonSearchRequest request, Pageable pageable);

    List<ADHoaDonChiTietResponse> getAllHoaDonChiTietResponse(String maHoaDon);
}
