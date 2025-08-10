package com.be.server.core.permitall.donmua.service.impl;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.response.HoaDonPageResponse1;
import com.be.server.core.admin.hoadon.repository.ADHoaDonRepository;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.core.permitall.donmua.service.DonMuaService;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonMuaServiceImpl implements DonMuaService {

    public final ADHoaDonRepository adHoaDonRepository;

    @Override
    public ResponseObject<?> getAllHoaDon(ADHoaDonSearchRequest request) {
        try {
            HoaDonPageResponse1 result = adHoaDonRepository.getAllHoaDonResponse1(request);

            return new ResponseObject<>(
                    result,
                    HttpStatus.OK,
                    "Lấy danh sách lịch sử đơn hàng thành công"
            );
        } catch (Exception e) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage()
            );
        }
    }
}
