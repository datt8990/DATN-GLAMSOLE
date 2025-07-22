package com.be.server.core.admin.banhang.service.impl;

import com.be.server.core.admin.SanPhamChiTiet.model.request.ADSPCTSearchRequest;
import com.be.server.core.admin.SanPhamChiTiet.model.response.ADSanPhamChiTietResponse;
import com.be.server.core.admin.SanPhamChiTiet.repository.ADSanPhamChiTietRepository;
import com.be.server.core.admin.banhang.model.request.ADCapNhatPhuongTHucThanhToanRequest;
import com.be.server.core.admin.banhang.model.request.ADHuyRequest;
import com.be.server.core.admin.banhang.model.request.ADNhanVienRequest;
import com.be.server.core.admin.banhang.model.request.ADThanhToanRequest;
import com.be.server.core.admin.banhang.model.request.ADThemKhachHangRequest;
import com.be.server.core.admin.banhang.model.request.ADThemSanPhamRequest;
import com.be.server.core.admin.banhang.model.request.ADXoaSanPhamRequest;
import com.be.server.core.admin.banhang.model.request.ChonPhieuGiamGiaRequest;
import com.be.server.core.admin.banhang.model.request.ListSanPhamRequest;
import com.be.server.core.admin.banhang.model.request.giaoHangRequest;
import com.be.server.core.admin.banhang.model.response.ADChonKhachHangRespones;
import com.be.server.core.admin.banhang.model.response.ADGioHangRespones;
import com.be.server.core.admin.banhang.model.response.ADPhuongThucThanhToanRespones;
import com.be.server.core.admin.banhang.model.response.ADThanhToanRespones;
import com.be.server.core.admin.banhang.model.response.ListHoaDon;
import com.be.server.core.admin.banhang.repository.ADSanPhamBanHangRepository;
import com.be.server.core.admin.banhang.repository.ADTaoHoaDonChiTietRepository;
import com.be.server.core.admin.banhang.repository.ADTaoHoaDonRepository;
import com.be.server.core.admin.banhang.service.ADBanHangService;
import com.be.server.core.admin.hoadon.repository.ADLichSuThanhToanRepository;
import com.be.server.core.admin.khachhang.repository.ADKhachHangRepository;
import com.be.server.core.admin.nhanvien.repository.ADNhanVienRepository;
import com.be.server.core.admin.voucher.model.response.ADPhieuGiamGiaResponse;
import com.be.server.core.admin.voucher.repository.ADVoucherRepository;
import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.HoaDon;
import com.be.server.entity.HoaDonChiTiet;
import com.be.server.entity.KhachHang;
import com.be.server.entity.LichSuThanhToan;
import com.be.server.entity.LichSuTrangThaiHoaDon;
import com.be.server.entity.NhanVien;
import com.be.server.entity.PhieuGiamGia;
import com.be.server.entity.SanPhamChiTiet;
import com.be.server.infrastructure.constant.EntityLoaiHoaDon;
import com.be.server.infrastructure.constant.EntityPhuongThucThanhToan;
import com.be.server.infrastructure.constant.EntityStatus;
import com.be.server.infrastructure.constant.EntityTrangThaiHoaDon;
import com.be.server.repository.LichSuTrangThaiHoaDonRepository;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ADBanHangServiceImpl implements ADBanHangService {

    public final ADTaoHoaDonRepository adTaoHoaDonRepository;
    public final ADSanPhamChiTietRepository adSanPhamRepository;
    public final ADTaoHoaDonChiTietRepository adTaoHoaDonChiTietRepository;
    public final ADKhachHangRepository adKhachHangRepository;
    public final ADSanPhamBanHangRepository adSanPhamBanHangRepository;
    public final ADNhanVienRepository adNhanVienRepository;
    private final LichSuTrangThaiHoaDonRepository lichSuTrangThaiHoaDonRepository;
    public final ADLichSuThanhToanRepository adLichSuThanhToanRepository;
    public final ADVoucherRepository adVoucherRepository;

    @Override
    public List<ListHoaDon> getHoaDon() {
        return adTaoHoaDonRepository.getAll();
    }

    @Override
    public ResponseObject<?> createHoaDon(ADNhanVienRequest adNhanVienRequest) {


        HoaDon hoaDon = new HoaDon();

        hoaDon.setTongTien(0d);

        NhanVien nhanVien = adNhanVienRepository.findById(adNhanVienRequest.getIdNV()).get();

        hoaDon.setNhanVien(nhanVien);

        hoaDon.setTrangThaiHoaDon(EntityTrangThaiHoaDon.CHO_XAC_NHAN);

        hoaDon.setLoaiHoaDon(EntityLoaiHoaDon.OFFLINE);

        adTaoHoaDonRepository.save(hoaDon);

        LichSuTrangThaiHoaDon lichSuTrangThaiHoaDon = new LichSuTrangThaiHoaDon();

        lichSuTrangThaiHoaDon.setHoaDon(hoaDon);

        lichSuTrangThaiHoaDon.setTrangThai(EntityTrangThaiHoaDon.CHO_XAC_NHAN);

        lichSuTrangThaiHoaDonRepository.save(lichSuTrangThaiHoaDon);
        return new ResponseObject<>(hoaDon, HttpStatus.CREATED, "Tạo hóa đơn thành công");
    }

    @Override
    public ResponseObject<?> createThemSanPham(ADThemSanPhamRequest request) {

        String idHDCT = adTaoHoaDonChiTietRepository.checkGioHang(request);

        if (idHDCT == null || idHDCT.isEmpty()) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            SanPhamChiTiet sanPham = adSanPhamRepository.findById(request.getIdSP()).get();


            if (sanPham.getSoLuong() < Integer.valueOf(request.getSoLuong())) {
                return new ResponseObject<>(null, HttpStatus.OK, "Số lượng sản phẩm thêm vào nhiều hơn số lượng trong kho");
            } else {


                HoaDon hoaDon = adTaoHoaDonRepository.findById(request.getIdHD()).get();

                hoaDonChiTiet.setHoaDon(hoaDon);

                hoaDonChiTiet.setSpct(sanPham);

                hoaDonChiTiet.setSoLuong(Integer.parseInt(request.getSoLuong()));

                double gia = sanPham.getGiaBan() * Double.parseDouble(request.getSoLuong());

                hoaDonChiTiet.setGia(gia);

                adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);

                return new ResponseObject<>(hoaDonChiTiet, HttpStatus.CREATED, "thêm sản phẩm");

            }
        } else {

            HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT).get();

            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + Integer.parseInt(request.getSoLuong()));

            SanPhamChiTiet sanPham = adSanPhamRepository.findById(request.getIdSP()).get();
            if (sanPham.getSoLuong() < hoaDonChiTiet.getSoLuong()) {
                return new ResponseObject<>(null, HttpStatus.OK, "Số lượng sản phẩm thêm vào nhiều hơn số lượng trong kho");
            } else {

                adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);

                return new ResponseObject<>(hoaDonChiTiet, HttpStatus.CREATED, "thêm sản phẩm");

            }


        }
    }

    @Override
    public List<ADGioHangRespones> getListGioHang(String id) {

        return adTaoHoaDonChiTietRepository.getAllGioHang(id);
    }

    @Override
    public void xoaHoaDonChiTiet(ADXoaSanPhamRequest adXoaSanPhamRequest) {


        String idHDCT = adTaoHoaDonChiTietRepository.getHoaDonChiTiet(adXoaSanPhamRequest);


        HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT).get();

        adTaoHoaDonChiTietRepository.delete(hoaDonChiTiet);
    }


    @Override
    public ResponseObject<?> ThemSoLuong(ADXoaSanPhamRequest adXoaSanPhamRequest) {


        SanPhamChiTiet sanPham = adSanPhamRepository.findById(adXoaSanPhamRequest.getIdSP()).get();


        String idHDCT = adTaoHoaDonChiTietRepository.getHoaDonChiTiet(adXoaSanPhamRequest);

        HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT).get();

        hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + 1);

        if (sanPham.getSoLuong() < Integer.valueOf(hoaDonChiTiet.getSoLuong())) {

            return new ResponseObject<>(null, HttpStatus.OK, "Số lượng sản phẩm thêm vào nhiều hơn số lượng trong kho");
        }

        adTaoHoaDonChiTietRepository.save(hoaDonChiTiet);

        return new ResponseObject<>(null, HttpStatus.OK, "");
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
    public List<ADPhuongThucThanhToanRespones> getPhuongThucThanhToan(String id) {
        return adTaoHoaDonChiTietRepository.getPhuongThucThanhToan(id);
    }

    @Override
    public void capNhatPhuongThucThanhToan(ADCapNhatPhuongTHucThanhToanRequest id) {

        HoaDon hoaDon = adTaoHoaDonRepository.findById(id.getIdHD()).get();
        if (id.getPhuongThucThanhToan().equals("0")) {

            hoaDon.setPhuongThucThanhToan(EntityPhuongThucThanhToan.TIEN_MAT);
        } else if (id.getPhuongThucThanhToan().equals("1")) {

            hoaDon.setPhuongThucThanhToan(EntityPhuongThucThanhToan.CHUYEN_KHOAN);
        } else {

            hoaDon.setPhuongThucThanhToan(EntityPhuongThucThanhToan.TIEN_MAT_CHUYEN_KHOAN);
        }
        adTaoHoaDonRepository.save(hoaDon);

    }

    @Override
    public ResponseObject<?> getAll(ListSanPhamRequest id) {
        Pageable pageable = Helper.createPageable(id, "created_date");

//        if (id.getStatus() != null && !id.getStatus().isEmpty()) {
//            if (id.getStatus().equals("0")) {
//                id.setEntityStatus(EntityStatus.INACTIVE);
//            } else {
//                id.setEntityStatus(EntityStatus.ACTIVE);
//            }
//        }

        Page<ADSanPhamChiTietResponse> page = adSanPhamBanHangRepository.getAllSanPhamChiTietByFilter(pageable, id);

        return new ResponseObject<>(
                PageableObject.of(page),
                HttpStatus.OK,
                "Lấy danh sách sản phẩm chi tiết thành công"
        );
    }

    @Override
    public ResponseObject<?> thanhToanThanhCong(ADThanhToanRequest id) {

        HoaDon hoaDon = adTaoHoaDonRepository.findById(id.getIdHD()).get();


        if (hoaDon.getLoaiHoaDon() == EntityLoaiHoaDon.GIAO_HANG) {
            hoaDon.setTrangThaiHoaDon(EntityTrangThaiHoaDon.DA_XAC_NHAN);

            hoaDon.setTongTien(id.getTienHang());

            hoaDon.setTen(id.getTen());

            hoaDon.setDiaChi(id.getDiaChi());

            hoaDon.setSdt(id.getSdt());

            hoaDon.setPhiVanChuyen(id.getTienShip());

            hoaDon.setTongTienSauGiam(id.getTongTien());

            PhieuGiamGia phieuGiamGia = adVoucherRepository.findById(id.getIdPGG()).get();

            phieuGiamGia.setSoLuongPhieu(phieuGiamGia.getSoLuongPhieu() - 1);

            adVoucherRepository.save(phieuGiamGia);

            hoaDon.setVoucher(phieuGiamGia);

            adTaoHoaDonRepository.save(hoaDon);


            String idLSTT = adTaoHoaDonChiTietRepository.getHoaDonLichSuTrangThai(id.getIdHD());

            System.out.println(idLSTT);

            LichSuTrangThaiHoaDon lichSuTrangThaiHoaDon = lichSuTrangThaiHoaDonRepository.findById(idLSTT).get();


            lichSuTrangThaiHoaDon.setTrangThai(EntityTrangThaiHoaDon.DA_XAC_NHAN);

            lichSuTrangThaiHoaDonRepository.save(lichSuTrangThaiHoaDon);

            System.out.println("xong trạng thái");

            HoaDon hoaDon1 = adTaoHoaDonRepository.findById(id.getIdHD())
                    .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

            NhanVien nhanVien = adNhanVienRepository.findById(id.getIdNV()).get();

            LichSuThanhToan lichSu = new LichSuThanhToan();
            lichSu.setHoaDon(hoaDon1);
            lichSu.setSoTien(id.getTongTien());
            lichSu.setThoiGian(LocalDateTime.now());
            lichSu.setNhanVien(nhanVien);
            lichSu.setMaGiaoDich(UUID.randomUUID().toString());

            adLichSuThanhToanRepository.save(lichSu);


            List<String> idHDCT = adTaoHoaDonChiTietRepository.getHoaDonChiTiet(id.getIdHD());

            System.out.println("hdct" + idHDCT);

            for (int i = 0; i < idHDCT.size(); i++) {
                System.out.println("hdct" + idHDCT.get(i));
                HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT.get(i)).get();

                String idSPCT = adTaoHoaDonChiTietRepository.getSanPhamChiTiet(idHDCT.get(i));

                System.out.println("spct" + idSPCT);

                SanPhamChiTiet sanPhamChiTiet = adSanPhamBanHangRepository.findById(idSPCT).get();

                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuong());

                System.out.println(sanPhamChiTiet.getSoLuong());
                adSanPhamBanHangRepository.save(sanPhamChiTiet);
            }

            return new ResponseObject<>(null, HttpStatus.CREATED, "Xác nhận giao hàng thành công");

        }

        hoaDon.setTrangThaiHoaDon(EntityTrangThaiHoaDon.HOAN_THANH);

        hoaDon.setTongTien(id.getTienHang());

        hoaDon.setTen(id.getTen());

        hoaDon.setDiaChi(id.getDiaChi());

        hoaDon.setSdt(id.getSdt());

        hoaDon.setPhiVanChuyen(id.getTienShip());

        hoaDon.setTongTienSauGiam(id.getTongTien());

        PhieuGiamGia phieuGiamGia = adVoucherRepository.findById(id.getIdPGG()).get();

        phieuGiamGia.setSoLuongPhieu(phieuGiamGia.getSoLuongPhieu() - 1);

        adVoucherRepository.save(phieuGiamGia);

        hoaDon.setVoucher(phieuGiamGia);

        adTaoHoaDonRepository.save(hoaDon);


        String idLSTT = adTaoHoaDonChiTietRepository.getHoaDonLichSuTrangThai(id.getIdHD());

        System.out.println(idLSTT);

        LichSuTrangThaiHoaDon lichSuTrangThaiHoaDon = lichSuTrangThaiHoaDonRepository.findById(idLSTT).get();


        lichSuTrangThaiHoaDon.setTrangThai(EntityTrangThaiHoaDon.HOAN_THANH);

        lichSuTrangThaiHoaDonRepository.save(lichSuTrangThaiHoaDon);

        System.out.println("xong trạng thái");

        HoaDon hoaDon1 = adTaoHoaDonRepository.findById(id.getIdHD())
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        NhanVien nhanVien = adNhanVienRepository.findById(id.getIdNV()).get();

        LichSuThanhToan lichSu = new LichSuThanhToan();
        lichSu.setHoaDon(hoaDon1);
        lichSu.setSoTien(id.getTongTien());
        lichSu.setThoiGian(LocalDateTime.now());
        lichSu.setNhanVien(nhanVien);
        lichSu.setMaGiaoDich(UUID.randomUUID().toString());

        adLichSuThanhToanRepository.save(lichSu);


        List<String> idHDCT = adTaoHoaDonChiTietRepository.getHoaDonChiTiet(id.getIdHD());

        System.out.println("hdct" + idHDCT);

        for (int i = 0; i < idHDCT.size(); i++) {
            System.out.println("hdct" + idHDCT.get(i));
            HoaDonChiTiet hoaDonChiTiet = adTaoHoaDonChiTietRepository.findById(idHDCT.get(i)).get();

            String idSPCT = adTaoHoaDonChiTietRepository.getSanPhamChiTiet(idHDCT.get(i));

            System.out.println("spct" + idSPCT);

            SanPhamChiTiet sanPhamChiTiet = adSanPhamBanHangRepository.findById(idSPCT).get();

            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuong());

            System.out.println(sanPhamChiTiet.getSoLuong());
            adSanPhamBanHangRepository.save(sanPhamChiTiet);
        }


        return new ResponseObject<>(null, HttpStatus.CREATED, "Thanh toán thành công");

    }

    @Override
    public ResponseObject<?> danhSachPhieuGiamGia(ChonPhieuGiamGiaRequest id) {

        Double tongTien = id.getIdHD();

        if (tongTien == null) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Tổng tiền không được để trống");
        }

        List<PhieuGiamGia> phieuGiamGias = adTaoHoaDonRepository.getPhieuGiamGia(id.getIdKH(), id.getIdHD());
        if (phieuGiamGias == null) {
            phieuGiamGias = new ArrayList<>();
        }
        if (phieuGiamGias.size() == 0) {
            return new ResponseObject<>(null, HttpStatus.CREATED, "Lây giá trị phiếu giảm giá thành công");
        }

        System.out.println(phieuGiamGias.size());

        if (phieuGiamGias.size() > 0) {
            phieuGiamGias.forEach(pg -> {
                if (pg.getPhanTramGiam() != null && pg.getGiaGiam() != null) {
                    if (pg.getKieuGiam() == true) {
                        pg.setGiaTriGiamThucTe(tongTien * (pg.getPhanTramGiam() / 100));
                        if (pg.getGiaTriGiamThucTe() >= pg.getGiaGiam()) {
                            pg.setGiaTriGiamThucTe(pg.getGiaGiam());
                        }
                    } else {

                        pg.setGiaTriGiamThucTe(tongTien - (tongTien - pg.getPhanTramGiam()));
                        System.out.println("giá trị" + pg.getGiaTriGiamThucTe());
                    }
                }
            });
        }

        System.out.println("giảm1" + phieuGiamGias.get(0).getGiaTriGiamThucTe());

        if (phieuGiamGias.size() > 0) {
            phieuGiamGias = phieuGiamGias.stream()
                    .sorted(Comparator.comparing(PhieuGiamGia::getGiaTriGiamThucTe, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            phieuGiamGias = new ArrayList<>();
        }

        return new ResponseObject<>(phieuGiamGias, HttpStatus.CREATED, "Lây giá trị phiếu giảm giá thành công");

    }

    @Override
    public ResponseObject<?> giaoHang(String request) {
        System.out.println("idHD" + request);
        HoaDon hoaDon = adTaoHoaDonRepository.findById(request).get();

        hoaDon.setLoaiHoaDon(hoaDon.getLoaiHoaDon() == EntityLoaiHoaDon.OFFLINE ? EntityLoaiHoaDon.GIAO_HANG : EntityLoaiHoaDon.OFFLINE);

        adTaoHoaDonRepository.save(hoaDon);

        return new ResponseObject<>(null, HttpStatus.CREATED, "Lây giá trị phiếu giảm giá thành công");
    }

    @Override
    public ResponseObject<?> huy(ADHuyRequest request) {
        HoaDon hoaDon = adTaoHoaDonRepository.findById(request.getIdHD()).get();

        hoaDon.setTrangThaiHoaDon(EntityTrangThaiHoaDon.DA_HUY);


        adTaoHoaDonRepository.save(hoaDon);

        LichSuTrangThaiHoaDon lichSuTrangThaiHoaDon = new LichSuTrangThaiHoaDon();
        lichSuTrangThaiHoaDon.setHoaDon(hoaDon);
        lichSuTrangThaiHoaDon.setTrangThai(EntityTrangThaiHoaDon.DA_HUY);
        lichSuTrangThaiHoaDon.setThoiGian(LocalDateTime.now());

        lichSuTrangThaiHoaDonRepository.save(lichSuTrangThaiHoaDon);

        return new ResponseObject<>(null, HttpStatus.CREATED, "Hủy hóa đơn thành công");
    }


}
