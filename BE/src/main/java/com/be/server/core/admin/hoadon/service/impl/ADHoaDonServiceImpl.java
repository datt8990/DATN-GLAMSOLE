package com.be.server.core.admin.hoadon.service.impl;

import com.be.server.core.admin.hoadon.model.request.ADChangeStatusRequest;
import com.be.server.core.admin.hoadon.model.request.ADHoaDonDetailRequest;
import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.request.ThanhToanRequest;
import com.be.server.core.admin.hoadon.model.response.*;
import com.be.server.core.admin.hoadon.repository.ADHoaDonChiTietRepository;
import com.be.server.core.admin.hoadon.repository.ADHoaDonRepository;
import com.be.server.core.admin.hoadon.repository.ADLichSuThanhToanRepository;
import com.be.server.core.admin.hoadon.repository.LichSuTrangThaiRepository;
import com.be.server.core.admin.hoadon.service.ADHoaDonService;
import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.HoaDon;
import com.be.server.entity.LichSuThanhToan;
import com.be.server.entity.LichSuTrangThaiHoaDon;
import com.be.server.entity.NhanVien;
import com.be.server.repository.NhanVienRepository;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ADHoaDonServiceImpl implements ADHoaDonService {

    public final ADHoaDonRepository adHoaDonRepository;

    public final ADHoaDonChiTietRepository adHoaDonChiTietRepository;

    public final LichSuTrangThaiRepository lichSuTrangThaiRepository;

    public final ADLichSuThanhToanRepository adLichSuThanhToanRepository;

    public final NhanVienRepository nhanVienRepository;

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
    public ResponseObject<?> getAllHoaDonCT(ADHoaDonDetailRequest request) {
        try {
            Pageable pageable = Helper.createPageable(request, "created_date");
            Page<ADHoaDonChiTietResponseDetail> page = adHoaDonChiTietRepository.getAllHoaDonChiTietResponse(request.getMaHoaDon(), pageable);
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

    @Override
    public ResponseObject<?> getLSTT(String id) {
        List<LichSuThanhToanResponse> lichSuThanhToans = adLichSuThanhToanRepository.getLichSuThanhToanByHoaDonId(id);
        return new ResponseObject<>(
                lichSuThanhToans,
                HttpStatus.OK,
                "Lấy danh sách lịch sử thanh toán hóa đơn thành công"
        );
    }

    @Override
    public ResponseObject<?>  thanhToanHoaDon(ThanhToanRequest request) {
        HoaDon hoaDon = adHoaDonRepository.findById(request.getHoaDonId())
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        NhanVien nhanVien = nhanVienRepository.findById(request.getNhanVienId())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        LichSuThanhToan lichSu = new LichSuThanhToan();
        lichSu.setHoaDon(hoaDon);
        Double soTien = request.getSoTienKhachDua() - request.getSoTienTraLai();
        lichSu.setSoTien(soTien);
        lichSu.setThoiGian(LocalDateTime.now());
        lichSu.setGhiChu(request.getGhiChu());
        lichSu.setLoaiGiaoDich(request.getLoaiGiaoDich());
        lichSu.setNhanVien(nhanVien);
        lichSu.setMaGiaoDich(UUID.randomUUID().toString());

        adLichSuThanhToanRepository.save(lichSu);

        hoaDon.setTongTienSauGiam(soTien);
        hoaDon.setTongTien(request.getSoTienGoc());
        hoaDon.setTrangThaiHoaDon(request.getTrangThai());

        adHoaDonRepository.save(hoaDon);

        HoaDon hoaDon1 = new HoaDon();
        return new ResponseObject<>(
                hoaDon1,
                HttpStatus.OK,
                "Thanh toán thành công"
        );
    }

}
