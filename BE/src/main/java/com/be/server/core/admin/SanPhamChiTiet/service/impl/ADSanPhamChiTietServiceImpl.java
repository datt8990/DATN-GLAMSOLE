package com.be.server.core.admin.SanPhamChiTiet.service.impl;

import com.be.server.core.admin.SanPhamChiTiet.model.request.ADSPCTRequest;
import com.be.server.core.admin.SanPhamChiTiet.model.request.ADSPCTSearchRequest;
import com.be.server.core.admin.SanPhamChiTiet.model.response.ADSanPhamChiTietResponse;
import com.be.server.core.admin.SanPhamChiTiet.repository.ADSanPhamChiTietRepository;
import com.be.server.core.admin.SanPhamChiTiet.service.ADSanPhamChiTietService;
import com.be.server.core.admin.chatlieu.repository.ADChatLieuRepository;
import com.be.server.core.admin.kichthuoc.repository.ADKichThuocRepository;
import com.be.server.core.admin.loaide.repository.ADLoaiDeRepository;
import com.be.server.core.admin.loaigiay.repository.ADLoaiGiayRepository;
import com.be.server.core.admin.mausac.repository.ADMauSacRepository;
import com.be.server.core.admin.sanpham.repository.ADSanPhamRepository;

import com.be.server.core.admin.xuatxu.repository.ADXuatXuRepository;
import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.ChatLieu;
import com.be.server.entity.DanhMuc;
import com.be.server.entity.KichCo;
import com.be.server.entity.LoaiDe;

import com.be.server.entity.MauSac;
import com.be.server.entity.SanPham;
import com.be.server.entity.SanPhamChiTiet;

import com.be.server.entity.ThuongHieu;
import com.be.server.entity.XuatSu;
import com.be.server.infrastructure.constant.EntityStatus;
import com.be.server.repository.ChatLieuRepository;
import com.be.server.repository.DanhMucRepository;
import com.be.server.repository.LoaiDeRepository;
import com.be.server.repository.ThuongHieuRepository;
import com.be.server.repository.XuatSuRepository;
import com.be.server.utils.CloudinaryUtils;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ADSanPhamChiTietServiceImpl implements ADSanPhamChiTietService {

    private final ADSanPhamChiTietRepository adSanPhamChiTietRepository;
    private final ADSanPhamRepository adSanPhamRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final ADMauSacRepository adMauSacRepository;
    private final ADKichThuocRepository adKichThuocRepository;
    private final DanhMucRepository danhMucRepository;
    private final LoaiDeRepository loaiDeRepository;
    private final XuatSuRepository xuatSuRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final ADXuatXuRepository adXuatXuRepository;
    private final ADLoaiDeRepository adLoaiDeRepository;
    private final ADLoaiGiayRepository adLoaiGiayRepository;
    private final ADChatLieuRepository adChatLieuRepository;
    private final CloudinaryUtils cloudinaryUtils;

    SanPham newSanPhamChiTiet = new SanPham();


    @Override
    public ResponseObject<?> getAll(ADSPCTSearchRequest id) {
        Pageable pageable = Helper.createPageable(id, "created_date");

        System.out.println(id.getStatus());
        if (id.getStatus() != null && !id.getStatus().isEmpty()) {
            if (id.getStatus().equals("0")){
                id.setEntityStatus(EntityStatus.INACTIVE);
            }
            else {
                id.setEntityStatus(EntityStatus.ACTIVE);
            }
        }

        Page<ADSanPhamChiTietResponse> page = adSanPhamChiTietRepository.getAllSanPhamChiTietByFilter(pageable, id);

        return new ResponseObject<>(
                PageableObject.of(page),
                HttpStatus.OK,
                "Lấy danh sách sản phẩm chi tiết thành công"
        );
    }

    @Override
    public ResponseObject<?> getDetailSPCT(String id) {
        return adSanPhamChiTietRepository.getSanPhamChiTietID(id)
                .map(technology -> new ResponseObject<>(technology, HttpStatus.OK, "sản phẩm thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));
    }

    @Override
    public ResponseObject<?> getSPCTById(String id) {
        return adSanPhamChiTietRepository.getSanPhamID(id)
                .map(technology -> new ResponseObject<>(technology, HttpStatus.OK, "sản phẩm thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));
    }

    @Override
    public ResponseObject<?> changeSanPhamStatus(String id) {
        Optional<SanPhamChiTiet> nemberOptional = adSanPhamChiTietRepository.findById(id);

        nemberOptional.map(nember -> {
            nember.setStatus(nember.getStatus() == EntityStatus.ACTIVE ? EntityStatus.INACTIVE : EntityStatus.ACTIVE);
            return new ResponseObject(adSanPhamChiTietRepository.save(nember), HttpStatus.OK, "Thay đổi trạng thái thành công");
        });

        return nemberOptional
                .map(product -> ResponseObject.successForward(HttpStatus.OK, "Đổi trạng thái thành công"))
                .orElseGet(() -> ResponseObject.successForward(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm"));
    }


    @Override
    public ResponseObject<?> getListSize() {
        return new ResponseObject<>(adSanPhamChiTietRepository.getListSize(), HttpStatus.OK, "Lấy thành công danh sách thương hiệu");
    }

    @Override
    public ResponseObject<?> getListColor() {
        return new ResponseObject<>(adSanPhamChiTietRepository.getListColor(), HttpStatus.OK, "Lấy thành công danh sách thương hiệu");
    }

    @Override
    public ResponseObject<?> modifySanPham(ADSPCTRequest requestItem) {

        SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();

        sanPhamChiTiet.setGiaBan(requestItem.getGiaBan());

        sanPhamChiTiet.setSoLuong(requestItem.getSoLuong());


            Optional<KichCo> size = adKichThuocRepository.findById(requestItem.getIdSize());

            KichCo kichCo = size.get();
            sanPhamChiTiet.setKichCo(kichCo);




            Optional<MauSac> mauSac = adMauSacRepository.findById(requestItem.getIdMau());

            MauSac mauSac1 = mauSac.get();
            sanPhamChiTiet.setMauSac(mauSac1);

            sanPhamChiTiet.setStatus(EntityStatus.ACTIVE);


        if (requestItem.getIdSP() != null) {

            Optional<SanPham> thuongHieuOptional = adSanPhamRepository.findById(requestItem.getIdSP());

            SanPham sanPham = thuongHieuOptional.get();

            sanPhamChiTiet.setSanPham(sanPham);

        } else {
            if (requestItem.getCheck().equals("0")) {
                SanPham newSanPham = new SanPham();

                newSanPham.setTen(requestItem.getTen());

                newSanPham.setMoTa(requestItem.getMoTa());

                if (requestItem.getIdThuongHieu() != null) {
                    Optional<ThuongHieu> thuongHieuOptional = thuongHieuRepository.findById(requestItem.getIdThuongHieu());
                    if (thuongHieuOptional.isPresent()) {
                        ThuongHieu thuongHieu = thuongHieuOptional.get();
                        newSanPham.setThuongHieu(thuongHieu);
                    } else {
                        // Xử lý trường hợp không tìm thấy ThuongHieu
                    }
                }

                if (requestItem.getIdDanhMuc() != null) {
                    Optional<DanhMuc> danhMucOptional = danhMucRepository.findById(requestItem.getIdDanhMuc());
                    if (danhMucOptional.isPresent()) {
                        DanhMuc danhMuc = danhMucOptional.get();
                        newSanPham.setDanhMuc(danhMuc);
                    } else {
                        // Xử lý trường hợp không tìm thấy DanhMuc
                    }
                }

                if (requestItem.getIdLoaiDe() != null) {
                    Optional<LoaiDe> loaiDeOptional = loaiDeRepository.findById(requestItem.getIdLoaiDe());
                    if (loaiDeOptional.isPresent()) {
                        LoaiDe loaiDe = loaiDeOptional.get();
                        newSanPham.setLoaiDe(loaiDe);
                    } else {
                        // Xử lý trường hợp không tìm thấy LoaiDe
                    }
                }

                if (requestItem.getIdChatLieu() != null) {
                    Optional<ChatLieu> xuatSuOptional = chatLieuRepository.findById(requestItem.getIdChatLieu());
                    if (xuatSuOptional.isPresent()) {
                        ChatLieu xuatSu = xuatSuOptional.get();
                        newSanPham.setChatLieu(xuatSu);
                    } else {
                        // Xử lý trường hợp không tìm thấy XuatSu
                    }
                }


                newSanPham.setStatus(EntityStatus.ACTIVE);

                adSanPhamRepository.save(newSanPham);

                sanPhamChiTiet.setSanPham(newSanPham);

                newSanPhamChiTiet = newSanPham;
            } else {


                sanPhamChiTiet.setSanPham(newSanPhamChiTiet);
            }

        }




        try {
            byte[] imageData = requestItem.getAnh().getBytes();
            CompletableFuture.runAsync(() -> {
                String imgPath = cloudinaryUtils.uploadImage(imageData, requestItem.getId());
                sanPhamChiTiet.setAnh(imgPath);
                adSanPhamChiTietRepository.save(sanPhamChiTiet);
            });
        } catch (IOException e) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi khi đọc file ảnh: " + e.getMessage());
        }


        adSanPhamChiTietRepository.save(sanPhamChiTiet);


        return new ResponseObject<>(sanPhamChiTiet, HttpStatus.CREATED, "Tạo sản phẩm thành công");
    }
}
