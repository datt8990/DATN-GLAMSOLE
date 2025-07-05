package com.be.server.core.admin.voucher.service.impl;

import com.be.server.core.admin.khachhang.repository.ADKhachHangRepository;
import com.be.server.core.admin.thuonghieu.model.request.ADThuongHieuRequest;
import com.be.server.core.admin.thuonghieu.model.request.ADThuongHieuSearchRequest;
import com.be.server.core.admin.voucher.model.request.ADVoucherRequest;
import com.be.server.core.admin.voucher.model.request.ADVoucherSearchRequest;
import com.be.server.core.admin.voucher.repository.ADVoucherRepository;
import com.be.server.core.admin.voucher.service.ADVoucherService;
import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.KhachHang;
import com.be.server.entity.PhieuGiamGia;
import com.be.server.entity.PhieuGiamGiaChiTiet;
import com.be.server.entity.ThuongHieu;
import com.be.server.infrastructure.constant.EntityStatus;
import com.be.server.repository.PhieuGiamGiaChiTietRepository;
import com.be.server.service.EmailService;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ADVoucherServiceImpl implements ADVoucherService {

    private final ADVoucherRepository advoucherRepository;
    private final ADKhachHangRepository adKhachHangRepository;
    private final PhieuGiamGiaChiTietRepository phieuGiamGiaChiTietRepository;

    @Override
    public ResponseObject<?> getAllVoucher(ADVoucherSearchRequest request) {
        Pageable pageable = Helper.createPageable(request, "created_date");
        Page<PhieuGiamGia> page;
        if (request.getQ() == null || request.getQ().isEmpty()) {
            page = advoucherRepository.findAll(pageable);
        } else {
            page = advoucherRepository.findByMaContainingOrTenContaining(request.getQ(), request.getQ(), pageable);
        }

        return new ResponseObject<>(PageableObject.of(page), HttpStatus.OK, "Lấy danh sách thương hiệu thành công");
    }

    @Override
    public ResponseObject<?> getVoucherById(String id) {


        return advoucherRepository.getNhanVienID(id).map(technology -> new ResponseObject<>(technology, HttpStatus.OK, "Lấy phiếu giảm giá thành công")).orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy phiếu giảm giá"));
    }

    @Override
    public List<String> getListKH(String id) {
        return advoucherRepository.getDanhSachKhachHang(id);


    }

    @Override
    public ResponseObject<?> modifyVoucher(ADVoucherRequest request) {
        if (request.getId() != null && StringUtils.hasLength(request.getId())) {
            Optional<PhieuGiamGia> exsitingMemberOpt = advoucherRepository.findById(request.getId());

            if (exsitingMemberOpt.isPresent()) {

                PhieuGiamGia voucher = exsitingMemberOpt.get();

                voucher.setTen(request.getTen());

                voucher.setDieuKien(request.getDieuKien());

                voucher.setGiaGiam(request.getGiaGiamToiDa());

                voucher.setLoaiGiam(request.getLoaiGiam());

                voucher.setNgayBatDau(request.getNgayBatDau());

                voucher.setKieuGiam(request.getKieuGiam());

                voucher.setNgayKetThuc(request.getNgayKetThuc());

                voucher.setSoLuongPhieu(request.getSoLuongPhieu());

                if (request.getLoaiGiam() == true) {
                    voucher.setPhanTramGiam(request.getPhanTramGiam());
                } else {
                    voucher.setPhanTramGiam(request.getGiaGiamToiDa());
                }

                advoucherRepository.save(voucher);

                if (request.getKhachHangIds() != null && request.getKhachHangIds().size() > 0) {
                    for (int i = 0; i < request.getKhachHangIds().size(); i++) {


                        KhachHang khachHang = adKhachHangRepository.findById(request.getKhachHangIds().get(i)).get();

                        PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = new PhieuGiamGiaChiTiet();

                        phieuGiamGiaChiTiet.setPhieuGiamGia(voucher);

                        phieuGiamGiaChiTiet.setKhachHang(khachHang);

                        phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);

                        String email = khachHang.getEmail();
                        String subject = "Khuyến mại của cửa hàng";
                        String content = "Chào " + khachHang.getTen() + ",\n\n" + "Cửa hàng chúng tôi chúc mừng bạn đã được 1 phiếu giảm giá từ cửa hàng chúng tôi\n" + "Mã phiếu giảm giá là: " + voucher.getMa() + "\n" + "Cảm ơn đã tin tưởng cửa hàng của chúng tôi";

                        EmailService.sendEmail(email, subject, content);

                    }


                }

                return new ResponseObject<>(voucher, HttpStatus.OK, "Cập nhật size thành công");
            }
        }


        PhieuGiamGia voucher = new PhieuGiamGia();

        voucher.setTen(request.getTen());

        voucher.setDieuKien(request.getDieuKien());

        voucher.setGiaGiam(request.getGiaGiamToiDa());

        voucher.setLoaiGiam(request.getLoaiGiam());

        voucher.setNgayBatDau(request.getNgayBatDau());

        voucher.setNgayKetThuc(request.getNgayKetThuc());

        voucher.setSoLuongPhieu(request.getSoLuongPhieu());

        voucher.setKieuGiam(request.getKieuGiam());

        if (request.getLoaiGiam() == true) {
            voucher.setPhanTramGiam(request.getPhanTramGiam());
        } else {
            voucher.setPhanTramGiam(request.getGiaGiamToiDa());
        }


        voucher.setStatus(EntityStatus.ACTIVE);

        advoucherRepository.save(voucher);

        for (int i = 0; i < request.getKhachHangIds().size(); i++) {
            System.out.println(request.getKhachHangIds().get(i));
        }

        if (request.getKhachHangIds() != null && request.getKhachHangIds().size() > 0) {
            for (int i = 0; i < request.getKhachHangIds().size(); i++) {
                System.out.println(request.getKhachHangIds().get(i));

                KhachHang khachHang = adKhachHangRepository.findById(request.getKhachHangIds().get(i)).get();


                PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = new PhieuGiamGiaChiTiet();

                phieuGiamGiaChiTiet.setPhieuGiamGia(voucher);

                phieuGiamGiaChiTiet.setKhachHang(khachHang);

                phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);


                String email = khachHang.getEmail();
                String subject = "Khuyến mại của cửa hàng";
                String content = "Chào " + khachHang.getTen() + ",\n\n" + "Cửa hàng chúng tôi chúc mừng bạn đã được 1 phiếu giảm giá từ cửa hàng chúng tôi\n" + "Mã phiếu giảm giá là: " + voucher.getMa() + "\n" + "Cảm ơn đã tin tưởng cửa hàng của chúng tôi";

                CompletableFuture.runAsync(() -> EmailService.sendEmail(email, subject, content));


            }


        }

        return new ResponseObject<>(voucher, HttpStatus.CREATED, "Tạo size thành công");
    }

    @Override
    public ResponseObject<?> changeSizeStatus(String id) {
        Optional<PhieuGiamGia> nemberOptional = advoucherRepository.findById(id);

        nemberOptional.map(voucher -> {
            voucher.setStatus(voucher.getStatus() == EntityStatus.ACTIVE ? EntityStatus.INACTIVE : EntityStatus.ACTIVE);
            return new ResponseObject(advoucherRepository.save(voucher), HttpStatus.OK, "Thay đổi trạng thái thành công");
        });

        return nemberOptional.map(product -> ResponseObject.successForward(HttpStatus.OK, "Đổi trạng thái thành công")).orElseGet(() -> ResponseObject.successForward(HttpStatus.NOT_FOUND, "Không tìm voucher"));
    }
}
