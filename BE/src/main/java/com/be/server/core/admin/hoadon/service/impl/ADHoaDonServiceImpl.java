package com.be.server.core.admin.hoadon.service.impl;

import com.be.server.core.admin.hoadon.model.request.ADChangeStatusRequest;
import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponseDetail;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonResponse;
import com.be.server.core.admin.hoadon.model.response.HoaDonPageResponse;
import com.be.server.core.admin.hoadon.model.response.TrangThaiThoiGianResponse;
import com.be.server.core.admin.hoadon.repository.ADHoaDonChiTietRepository;
import com.be.server.core.admin.hoadon.repository.ADHoaDonRepository;
import com.be.server.core.admin.hoadon.repository.LichSuTrangThaiRepository;
import com.be.server.core.admin.hoadon.service.ADHoaDonService;
import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.HoaDon;
import com.be.server.entity.LichSuTrangThaiHoaDon;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ADHoaDonServiceImpl implements ADHoaDonService {

    public final ADHoaDonRepository adHoaDonRepository;

    public final ADHoaDonChiTietRepository adHoaDonChiTietRepository;

    public final LichSuTrangThaiRepository lichSuTrangThaiRepository;

    @Override
    public ResponseObject<?> getAllHoaDon(ADHoaDonSearchRequest request) {
        try {
            Pageable pageable = Helper.createPageable(request, "created_date");
            HoaDonPageResponse result = adHoaDonRepository.getAllHoaDonResponse(request, pageable);

            return new ResponseObject<>(
                    result,
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

    @Override
    public ResponseObject<?> changeStatus(ADChangeStatusRequest adChangeStatusRequest) {
        HoaDon hoaDon = adHoaDonRepository.updateTrangThaiHoaDon(adChangeStatusRequest.getMaHoaDon(), adChangeStatusRequest.getStatus());

        HoaDon hoaDon1 = new HoaDon();

        LichSuTrangThaiHoaDon statusHistory = new LichSuTrangThaiHoaDon();
        statusHistory.setHoaDon(hoaDon);
        statusHistory.setTrangThai(adChangeStatusRequest.getStatus());
        statusHistory.setThoiGian(LocalDateTime.now());
        statusHistory.setNote(adChangeStatusRequest.getNote());
        // Save the status history
        lichSuTrangThaiRepository.save(statusHistory);
        return new ResponseObject<>(
                hoaDon1,
                HttpStatus.OK,
                "Thay đổi thành công "
        );
    }

    @Override
    public ResponseObject<?> getLSTTHD(String id) {
        List<TrangThaiThoiGianResponse> lichSuTrangThaiHoaDons = lichSuTrangThaiRepository.findTrangThaiVaThoiGianByHoaDonId(id);
        return new ResponseObject<>(
                lichSuTrangThaiHoaDons,
                HttpStatus.OK,
                "Lấy danh sách lịch sử trạng thái hóa đơn thành công"
        );
    }


}
