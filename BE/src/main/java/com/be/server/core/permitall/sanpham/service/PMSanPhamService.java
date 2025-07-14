package com.be.server.core.permitall.sanpham.service;

import com.be.server.core.common.base.ResponseObject;
import com.be.server.core.permitall.sanpham.dto.request.PMSanPhamMoiSearchRequest;

public interface PMSanPhamService {

    ResponseObject<?> getAllSanPhamMoi(PMSanPhamMoiSearchRequest request);

    ResponseObject<?> getAllSanPhamGiamGia(PMSanPhamMoiSearchRequest request);

}
