package com.be.server.core.admin.hoadon.service;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.common.base.ResponseObject;

public interface ADHoaDonService {

    ResponseObject<?> getAllHoaDon(ADHoaDonSearchRequest request);

    ResponseObject<?> getAllHoaDonCT(String maHoaDon);

//    ResponseObject<?> getKhachHangById(String id);
//
//    ResponseObject<?> modifyKhachHang(ADHoaDonRequest request);
//
//    ResponseObject<?> changeKhachHangStatus(String id);


}
