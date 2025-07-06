package com.be.server.core.admin.banhang.service.impl;

import com.be.server.core.admin.SanPhamChiTiet.repository.ADSanPhamChiTietRepository;
import com.be.server.core.admin.banhang.model.request.ADCapNhatPhuongTHucThanhToanRequest;
import com.be.server.core.admin.banhang.model.request.ADThemKhachHangRequest;
import com.be.server.core.admin.banhang.model.request.ADThemSanPhamRequest;
import com.be.server.core.admin.banhang.model.request.ADXoaSanPhamRequest;
import com.be.server.core.admin.banhang.model.response.ADChonKhachHangRespones;
import com.be.server.core.admin.banhang.model.response.ADGioHangRespones;
import com.be.server.core.admin.banhang.model.response.ADPhuongThucThanhToanRespones;
import com.be.server.core.admin.banhang.model.response.ADThanhToanRespones;
import com.be.server.core.admin.banhang.model.response.ListHoaDon;
import com.be.server.core.admin.banhang.repository.ADTaoHoaDonChiTietRepository;
import com.be.server.core.admin.banhang.repository.ADTaoHoaDonRepository;
import com.be.server.core.admin.banhang.service.ADBanHangService;
import com.be.server.core.admin.khachhang.repository.ADKhachHangRepository;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.HoaDon;
import com.be.server.entity.HoaDonChiTiet;
import com.be.server.entity.KhachHang;
import com.be.server.entity.SanPhamChiTiet;
import com.be.server.infrastructure.constant.EntityPhuongThucThanhToan;
import com.be.server.infrastructure.constant.EntityTrangThaiHoaDon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ADBanHangServiceImpl implements ADBanHangService {

    public final ADTaoHoaDonRepository adTaoHoaDonRepository;
    public final ADSanPhamChiTietRepository adSanPhamRepository;
    public final ADTaoHoaDonChiTietRepository adTaoHoaDonChiTietRepository;
    public final ADKhachHangRepository adKhachHangRepository;

    @Override
    public List<ListHoaDon> getHoaDon() {
        return adTaoHoaDonRepository.getAll();
    }

    @Override
    public ResponseObject<?> createHoaDon() {

        HoaDon hoaDon = new HoaDon();

        hoaDon.setTongTien(0d);

        hoaDon.setTrangThaiHoaDon(EntityTrangThaiHoaDon.CHO_XAC_NHAN);

        adTaoHoaDonRepository.save(hoaDon);

        return new ResponseObject<>(hoaDon, HttpStatus.CREATED, "Tạo hóa đơn thành công");
    }

    @Override
    public ResponseObject<?> createThemSanPham(ADThemSanPhamRequest request) {

        String idHDCT = adTaoHoaDonChiTietRepository.checkGioHang(request);

        if (idHDCT == null || idHDCT.isEmpty()) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            SanPhamChiTiet sanPham = adSanPhamRepository.findById(request.getIdSP()).get();

            HoaDon hoaDon = adTaoHoaDonRepository.findById(request.getIdHD()).get();

            hoaDonChiTiet.setHoaDon(hoaDon);

            hoaDonChiTiet.setSpct(sanPham);

            hoaDonChiTiet.setSoLuong(Integer.parseInt(request.getSoLuong()));

            double gia = sanPham.getGiaBan() * Double.parseDouble(request.getSoLuong());

            hoaDonChiTiet.setGia(gia);

            adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);

            return new ResponseObject<>(hoaDonChiTiet, HttpStatus.CREATED, "thêm sản phẩm");
        } else {

            HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT).get();

            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + Integer.parseInt(request.getSoLuong()));

            adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);

            return new ResponseObject<>(hoaDonChiTiet, HttpStatus.CREATED, "thêm sản phẩm");
        }
    }

    @Override
    public List<ADGioHangRespones> getListGioHang(String id) {
        System.out.println(id);
        return adTaoHoaDonChiTietRepository.getAllGioHang(id);
    }

    @Override
    public void xoaHoaDonChiTiet(ADXoaSanPhamRequest adXoaSanPhamRequest) {

        System.out.println("hd" + adXoaSanPhamRequest.getIdHD());
        System.out.println("sp" + adXoaSanPhamRequest.getIdSP());

        String idHDCT = adTaoHoaDonChiTietRepository.getHoaDonChiTiet(adXoaSanPhamRequest);

        System.out.println("hdct" + idHDCT);

        HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT).get();

        adTaoHoaDonChiTietRepository.delete(hoaDonChiTiet);
    }


    @Override
    public void ThemSoLuong(ADXoaSanPhamRequest adXoaSanPhamRequest) {

        String idHDCT = adTaoHoaDonChiTietRepository.getHoaDonChiTiet(adXoaSanPhamRequest);

        HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT).get();

        hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + 1);

        adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    @Override
    public void xoaSoLuong(ADXoaSanPhamRequest adXoaSanPhamRequest) {

        String idHDCT = adTaoHoaDonChiTietRepository.getHoaDonChiTiet(adXoaSanPhamRequest);

        HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT).get();

        hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() - 1);

        if (hoaDonChiTiet.getSoLuong() == 0) {
            adTaoHoaDonChiTietRepository.delete(hoaDonChiTiet);
        } else {
            adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);
        }


    }

    @Override
    public List<ADChonKhachHangRespones> listKhachHang() {
        return adTaoHoaDonChiTietRepository.getAllList();
    }

    @Override
    public void themKhachHang(ADThemKhachHangRequest id) {

        KhachHang khachHang = adKhachHangRepository.findById(id.getIdKH()).get();

        HoaDon hoaDon = adTaoHoaDonRepository.findById(id.getIdHD()).get();

        hoaDon.setKhachHang(khachHang);

        adTaoHoaDonRepository.save(hoaDon);

    }

    @Override
    public ADChonKhachHangRespones getKhachHang(String id) {

        return adTaoHoaDonChiTietRepository.getKhachHang(id);
    }

    @Override
    public ADThanhToanRespones getThanhToan(String id) {
        return adTaoHoaDonChiTietRepository.getThanhToan(id);
    }

    @Override
    public  List<ADPhuongThucThanhToanRespones>  getPhuongThucThanhToan(String id) {
        return adTaoHoaDonChiTietRepository.getPhuongThucThanhToan(id);
    }

    @Override
    public void capNhatPhuongThucThanhToan(ADCapNhatPhuongTHucThanhToanRequest id) {

        System.out.println(id.getIdHD());
        System.out.println(id.getPhuongThucThanhToan());
        HoaDon hoaDon = adTaoHoaDonRepository.findById(id.getIdHD()).get();
        if (id.getPhuongThucThanhToan().equals("0")) {

            hoaDon.setPhuongThucThanhToan(EntityPhuongThucThanhToan.TIEN_MAT);
        } else if (id.getPhuongThucThanhToan().equals("1")) {

            hoaDon.setPhuongThucThanhToan(EntityPhuongThucThanhToan.CHUYEN_KHOAN);
        } else {

            hoaDon.setPhuongThucThanhToan(EntityPhuongThucThanhToan.THE);
        }
        System.out.println("chạy vào đay1");
        adTaoHoaDonRepository.save(hoaDon);

    }

    @Override
    public ResponseObject<?> thanhToanThanhCong(String id) {
        HoaDon hoaDon =  adTaoHoaDonRepository.findById(id).get();

        hoaDon.setTrangThaiHoaDon(EntityTrangThaiHoaDon.DA_XAC_NHAN);

        adTaoHoaDonRepository.save(hoaDon);

        return new ResponseObject<>(hoaDon, HttpStatus.CREATED, "thanh toán thành công");
    }


}
