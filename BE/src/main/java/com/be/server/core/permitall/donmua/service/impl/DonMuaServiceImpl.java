package com.be.server.core.permitall.donmua.service.impl;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.response.HoaDonPageResponse1;
import com.be.server.core.admin.hoadon.repository.ADHoaDonRepository;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.core.permitall.donmua.model.request.UpdateDeliveryDTO;
import com.be.server.core.permitall.donmua.service.DonMuaService;
import com.be.server.entity.HoaDon;
import com.be.server.infrastructure.constant.EntityTrangThaiHoaDon;
import com.be.server.repository.HoaDonRepository;
import com.be.server.utils.Helper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonMuaServiceImpl implements DonMuaService {

    public final ADHoaDonRepository adHoaDonRepository;

    private final HoaDonRepository hoaDonRepository;
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

    @Override
    public ResponseObject<?> suaThongTin(UpdateDeliveryDTO request) {
        // Tìm hóa đơn
        HoaDon hoaDon = hoaDonRepository.findByMa(request.getMaHoaDon())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn với mã: " + request.getMaHoaDon()));

        // Chỉ cho phép cập nhật nếu trạng thái là "Chờ xác nhận" (0)
        if (!hoaDon.getTrangThaiHoaDon().equals(EntityTrangThaiHoaDon.CHO_XAC_NHAN)) {
            throw new IllegalStateException("Chỉ có thể cập nhật thông tin giao hàng khi đơn hàng đang chờ xác nhận");
        }

        // Cập nhật thông tin giao hàng
        hoaDon.setTenKH(request.getTenKhachHang());
        hoaDon.setSdt(request.getSdtKhachHang());
        hoaDon.setEmail(request.getEmail());
        hoaDon.setDiaChi(request.getDiaChi());
        hoaDon.setPhiVanChuyen(request.getPhiVanChuyen());
        hoaDon.setTongTienSauGiam(request.getTongTienSauGiam());

        // Lưu hóa đơn
         hoaDonRepository.save(hoaDon);

        return new ResponseObject<>(
                hoaDon,
                HttpStatus.OK,
                "Lấy danh sách lịch sử đơn hàng thành công");
    }
}
