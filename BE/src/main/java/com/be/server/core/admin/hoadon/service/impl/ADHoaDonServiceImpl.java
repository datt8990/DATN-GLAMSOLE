package com.be.server.core.admin.hoadon.service.impl;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponse;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponseDetail;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonResponse;
import com.be.server.core.admin.hoadon.repository.ADHoaDonChiTietRepository;
import com.be.server.core.admin.hoadon.repository.ADHoaDonRepository;
import com.be.server.core.admin.hoadon.service.ADHoaDonService;
import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ADHoaDonServiceImpl implements ADHoaDonService {

    public final ADHoaDonRepository adHoaDonRepository;

    public final ADHoaDonChiTietRepository adHoaDonChiTietRepository;

    @Override
    public ResponseObject<?> getAllHoaDon(ADHoaDonSearchRequest request) {
        try {
            Pageable pageable = Helper.createPageable(request, "created_date");
            Page<ADHoaDonResponse> page = adHoaDonRepository.getAllHoaDonResponse(request, pageable);

            return new ResponseObject<>(
                    PageableObject.of(page),
                    HttpStatus.OK,
                    "Lấy danh sách hóa đơn thành công"
            );
        } catch (Exception e) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi lấy danh sách hóa đơn: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseObject<?> getAllHoaDonCT(String maHoaDon) {
        try {
            List<ADHoaDonChiTietResponseDetail> page = adHoaDonChiTietRepository.getAllHoaDonChiTietResponse(maHoaDon);

            return new ResponseObject<>(
                    page,
                    HttpStatus.OK,
                    "Lấy danh sách chi tiết hóa đơn thành công"
            );
        } catch (Exception e) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage()
            );
        }
    }


}
