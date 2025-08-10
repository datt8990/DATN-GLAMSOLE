package com.be.server.core.permitall.donmua.service;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.core.permitall.donmua.model.request.UpdateDeliveryDTO;

public interface DonMuaService {

    ResponseObject<?> getAllHoaDon(ADHoaDonSearchRequest request);

    ResponseObject<?>  suaThongTin(UpdateDeliveryDTO request);
}
