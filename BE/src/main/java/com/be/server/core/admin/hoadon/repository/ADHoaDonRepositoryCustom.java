package com.be.server.core.admin.hoadon.repository;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponse;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonResponse;
import com.be.server.core.admin.hoadon.model.response.HoaDonPageResponse;
import com.be.server.entity.HoaDon;
import com.be.server.infrastructure.constant.EntityTrangThaiHoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ADHoaDonRepositoryCustom {
    HoaDonPageResponse getAllHoaDonResponse(ADHoaDonSearchRequest request, Pageable pageable);

    List<ADHoaDonChiTietResponse> getAllHoaDonChiTietResponse(String maHoaDon);

    HoaDon updateTrangThaiHoaDon(String maHoaDon, EntityTrangThaiHoaDon TrangThai);
}
