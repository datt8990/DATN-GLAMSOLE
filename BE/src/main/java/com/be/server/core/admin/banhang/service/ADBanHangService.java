package com.be.server.core.admin.banhang.service;

import com.be.server.core.admin.banhang.model.request.ADCapNhatPhuongTHucThanhToanRequest;
import com.be.server.core.admin.banhang.model.request.ADThemGioHangRequest;
import com.be.server.core.admin.banhang.model.request.ADThemKhachHangRequest;
import com.be.server.core.admin.banhang.model.request.ADThemSanPhamRequest;
import com.be.server.core.admin.banhang.model.request.ADXoaSanPhamRequest;
import com.be.server.core.admin.banhang.model.response.ADChonKhachHangRespones;
import com.be.server.core.admin.banhang.model.response.ADGioHangRespones;
import com.be.server.core.admin.banhang.model.response.ADPhuongThucThanhToanRespones;
import com.be.server.core.admin.banhang.model.response.ADThanhToanRespones;
import com.be.server.core.admin.banhang.model.response.ListHoaDon;
import com.be.server.core.admin.loaide.model.request.ADLoaiDeRequest;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.HoaDon;
import com.be.server.entity.KhachHang;

import java.util.List;

public interface ADBanHangService {

    List<ListHoaDon> getHoaDon();

    ResponseObject<?> createHoaDon();

    ResponseObject<?> createThemSanPham(ADThemSanPhamRequest adThemSanPhamRequest);

    List<ADGioHangRespones> getListGioHang(String id);

    void xoaHoaDonChiTiet(ADXoaSanPhamRequest adXoaSanPhamRequest);

    void ThemSoLuong(ADXoaSanPhamRequest adXoaSanPhamRequest);

    void xoaSoLuong(ADXoaSanPhamRequest adXoaSanPhamRequest);

    List<ADChonKhachHangRespones> listKhachHang();

    void themKhachHang(ADThemKhachHangRequest id);

    ADChonKhachHangRespones getKhachHang(String id);

    ADThanhToanRespones getThanhToan(String id);

    List<ADPhuongThucThanhToanRespones> getPhuongThucThanhToan(String id);

    void capNhatPhuongThucThanhToan(ADCapNhatPhuongTHucThanhToanRequest id);



    ResponseObject<?> thanhToanThanhCong(String id);
}