package com.be.server.core.permitall.donmua.service;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.common.base.ResponseObject;

public interface DonMuaService {

    ResponseObject<?> getAllHoaDon(ADHoaDonSearchRequest request);
}
