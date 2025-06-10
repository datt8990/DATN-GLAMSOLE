package com.be.server.core.admin.nhanvien.service.impl;

import com.be.server.core.admin.nhanvien.model.request.ADNhanVienRequest;
import com.be.server.core.admin.nhanvien.model.request.ADNhanVienSearchRequest;
import com.be.server.core.admin.nhanvien.repository.ADNhanVienRepository;
import com.be.server.core.admin.nhanvien.service.ADNhanVienService;
import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.NhanVien;
import com.be.server.infrastructure.constant.EntityRole;
import com.be.server.infrastructure.constant.EntityStatus;
import com.be.server.utils.CloudinaryUtils;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class ADNhanVienServiceImpl implements ADNhanVienService {

    private final ADNhanVienRepository adNhanVienRepository;
    private final CloudinaryUtils cloudinaryUtils;

    @Override
    public ResponseObject<?> getAllNhanVien(ADNhanVienSearchRequest request) {
        Pageable pageable = Helper.createPageable(request, "created_date");
        System.out.println(request.getQ());
        Page<NhanVien> page;
        if (request.getQ() == null || request.getQ().isEmpty()) {
            page = adNhanVienRepository.findAll(pageable);
        } else {
            page = adNhanVienRepository.findByMaContainingOrTenContaining(request.getQ(), request.getQ(), pageable);
        }

        return new ResponseObject<>(
                PageableObject.of(page),
                HttpStatus.OK,
                "Lấy danh sách nhân viên thành công"
        );
    }

    @Override
    public ResponseObject<?> getNhanVienById(String id) {
        return adNhanVienRepository.findById(id)
                .map(technology -> new ResponseObject<>(technology, HttpStatus.OK, "Nhân viên thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy Màu sắc"));
    }

    @Override
    public ResponseObject<?> modifyMNhanVien(ADNhanVienRequest request) {
        if (request.getId() != null && StringUtils.hasLength(request.getId())) {
            Optional<NhanVien> exsitingMemberOpt = adNhanVienRepository.findById(request.getId());

            if (exsitingMemberOpt.isPresent()) {

                NhanVien nhanVien = exsitingMemberOpt.get();

                nhanVien.setMa(request.getCode());

                nhanVien.setTen(request.getTen());

                nhanVien.setEmail(request.getEmail());

                nhanVien.setSdt(request.getSdt());

                nhanVien.setDiaChi(request.getDiaChi());

                nhanVien.setGioiTimh(request.getGioiTinh());

                nhanVien.setChucVu(EntityRole.STAFF);

                nhanVien.setStatus(EntityStatus.ACTIVE);

                try {
                    byte[] imageData = request.getAvatar().getBytes();
                    CompletableFuture.runAsync(() -> {
                        String imgPath = cloudinaryUtils.uploadImage(imageData, request.getId());
                        nhanVien.setAvatar(imgPath);
                        adNhanVienRepository.save(nhanVien);
                    });
                } catch (IOException e) {
                    return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi khi đọc file ảnh: " + e.getMessage());
                }
                adNhanVienRepository.save(nhanVien);

                return new ResponseObject<>(nhanVien, HttpStatus.OK, "Cập nhật nhân viên thành công");
            }
        }


        NhanVien nhanVien = new NhanVien();

        nhanVien.setMa(request.getCode());

        nhanVien.setTen(request.getTen());

        nhanVien.setEmail(request.getEmail());

        nhanVien.setSdt(request.getSdt());

        nhanVien.setDiaChi(request.getDiaChi());

        nhanVien.setGioiTimh(request.getGioiTinh());

        nhanVien.setMatKhau("123");

        nhanVien.setChucVu(EntityRole.STAFF);

        nhanVien.setStatus(EntityStatus.ACTIVE);

        try {
            byte[] imageData = request.getAvatar().getBytes();
            CompletableFuture.runAsync(() -> {
                String imgPath = cloudinaryUtils.uploadImage(imageData, request.getId());
                nhanVien.setAvatar(imgPath);
                adNhanVienRepository.save(nhanVien);
            });
        } catch (IOException e) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi khi đọc file ảnh: " + e.getMessage());
        }

        System.out.println("nhân viên");
        adNhanVienRepository.save(nhanVien);

        return new ResponseObject<>(nhanVien, HttpStatus.CREATED, "Tạo nhân viên thành công");
    }

    @Override
    public ResponseObject<?> changeNhanVienStatus(String id) {
        Optional<NhanVien> nemberOptional = adNhanVienRepository.findById(id);

        nemberOptional.map(nember -> {
            nember.setStatus(nember.getStatus() == EntityStatus.ACTIVE ? EntityStatus.INACTIVE : EntityStatus.ACTIVE);
            return new ResponseObject(adNhanVienRepository.save(nember), HttpStatus.OK, "Thay đổi trạng thái thành công");
        });

        return nemberOptional
                .map(product -> ResponseObject.successForward(HttpStatus.OK, "Đổi trạng thái thành công"))
                .orElseGet(() -> ResponseObject.successForward(HttpStatus.NOT_FOUND, "Không tìm nhân viên "));
    }
}
