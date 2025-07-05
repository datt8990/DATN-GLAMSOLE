package com.be.server.core.admin.voucher.service;

import com.be.server.core.admin.thuonghieu.model.request.ADThuongHieuRequest;
import com.be.server.core.admin.thuonghieu.model.request.ADThuongHieuSearchRequest;
import com.be.server.core.admin.voucher.model.request.ADVoucherRequest;
import com.be.server.core.admin.voucher.model.request.ADVoucherSearchRequest;
import com.be.server.core.common.base.ResponseObject;

import java.util.List;


public interface ADVoucherService {

    ResponseObject<?> getAllVoucher(ADVoucherSearchRequest id);

    ResponseObject<?> getVoucherById(String id);

    List<String> getListKH(String id);

    ResponseObject<?> modifyVoucher(ADVoucherRequest request);

    ResponseObject<?> changeSizeStatus(String id);

}
