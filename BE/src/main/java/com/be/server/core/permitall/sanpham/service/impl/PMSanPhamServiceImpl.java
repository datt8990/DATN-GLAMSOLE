package com.be.server.core.permitall.sanpham.service.impl;

import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.core.permitall.sanpham.dto.request.PMSanPhamMoiSearchRequest;
import com.be.server.core.permitall.sanpham.dto.response.DotGiamGiaInfo;
import com.be.server.core.permitall.sanpham.dto.response.PMSanPhamMoiResponse;
import com.be.server.core.permitall.sanpham.repository.*;
import com.be.server.core.permitall.sanpham.service.PMSanPhamService;
import com.be.server.entity.DotGiamGia;
import com.be.server.entity.DotGiamGiaChiTietSanPham;
import com.be.server.entity.SanPhamChiTiet;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class PMSanPhamServiceImpl implements PMSanPhamService {

    private final PMSanPhamRepository pmsanphamRepository;

    private final PMSanPhamChiTietRepository pmsanphamChiTietRepository;

    private final PMDotGiamGiaChiTietSanPhamRepository pmdotGiamGiaChiTietSanPhamRepository;

    private final PMKichCoRepository pmkichcoRepository;

    private final PMMauSacRepository pmMauSacRepository;

    private final PMDotGiamGiaRepository pmDotGiamGiaRepository;

    @Override
    public ResponseObject<?> getAllSanPhamMoi(PMSanPhamMoiSearchRequest request) {
        Pageable pageable = Helper.createPageable(request, "created_date");
        Page<PMSanPhamMoiResponse> page = pmsanphamRepository.getSanPhamMoiResponsePage(pageable);

        Long currentTimestamp = System.currentTimeMillis();

        for (PMSanPhamMoiResponse dto : page.getContent()) {
            dto.setKichCo(pmkichcoRepository.getDistinctBySanPhamId(dto.getId()));
            dto.setMauSac(pmMauSacRepository.getBySanPhamId(dto.getId()));

            List<String> dsAnh = pmsanphamChiTietRepository.getAllAnhBySanPhamId(dto.getId());
            dto.setDsAnh(dsAnh);

            List<SanPhamChiTiet> chiTietList = pmsanphamChiTietRepository.findBySanPham(dto.getId());
            DotGiamGiaInfo maxGiamGiaInfo = null;

            for (SanPhamChiTiet chiTiet : chiTietList) {
                Optional<DotGiamGiaChiTietSanPham> giamGia =
                        pmdotGiamGiaChiTietSanPhamRepository.findHieuLucBySanPhamChiTietId(
                                chiTiet.getId(), currentTimestamp);

                if (giamGia.isPresent()) {
                    DotGiamGiaChiTietSanPham chiTietGiamGia = giamGia.get();
                    DotGiamGia dotGiamGia = chiTietGiamGia.getDotGiamGia();

                    DotGiamGiaInfo tmp = new DotGiamGiaInfo(
                            dotGiamGia.getTen(),
                            dotGiamGia.getPhanTramGiam(),
                            chiTietGiamGia.getGiaTruoc(),
                            chiTietGiamGia.getGiaSau(),
                            dotGiamGia.getNgayBatDau(),
                            dotGiamGia.getNgayKetThuc()
                    );
                    if (maxGiamGiaInfo == null || tmp.getPhanTramGiam() > maxGiamGiaInfo.getPhanTramGiam()) {
                        maxGiamGiaInfo = tmp;
                    }
                }
            }
            dto.setDotGiamGia(maxGiamGiaInfo);
        }

        return new ResponseObject<>(
                PageableObject.of(page),
                HttpStatus.OK,
                "Lấy danh sách sản phẩm mới thành công"
        );
    }

    @Override
    public ResponseObject<?> getAllSanPhamGiamGia(PMSanPhamMoiSearchRequest request) {
        Pageable pageable = Helper.createPageable(request, "created_date");
        Long currentTimestamp = System.currentTimeMillis();

        Page<PMSanPhamMoiResponse> page = pmsanphamRepository.getSanPhamGiamGiaResponsePage(currentTimestamp, pageable);

        for (PMSanPhamMoiResponse dto : page.getContent()) {
            dto.setKichCo(pmkichcoRepository.getDistinctBySanPhamId(dto.getId()));
            dto.setMauSac(pmMauSacRepository.getBySanPhamId(dto.getId()));
            dto.setDsAnh(pmsanphamChiTietRepository.getAllAnhBySanPhamId(dto.getId()));

            List<SanPhamChiTiet> chiTietList = pmsanphamChiTietRepository.findBySanPham(dto.getId());
            DotGiamGiaInfo maxGiamGiaInfo = null;

            for (SanPhamChiTiet chiTiet : chiTietList) {
                Optional<DotGiamGiaChiTietSanPham> giamGia =
                        pmdotGiamGiaChiTietSanPhamRepository.findHieuLucBySanPhamChiTietId(
                                chiTiet.getId(), currentTimestamp);

                if (giamGia.isPresent()) {
                    DotGiamGiaChiTietSanPham chiTietGiamGia = giamGia.get();
                    DotGiamGia dotGiamGia = chiTietGiamGia.getDotGiamGia();

                    DotGiamGiaInfo tmp = new DotGiamGiaInfo(
                            dotGiamGia.getTen(),
                            dotGiamGia.getPhanTramGiam(),
                            chiTietGiamGia.getGiaTruoc(),
                            chiTietGiamGia.getGiaSau(),
                            dotGiamGia.getNgayBatDau(),
                            dotGiamGia.getNgayKetThuc()
                    );
                    if (maxGiamGiaInfo == null || tmp.getPhanTramGiam() > maxGiamGiaInfo.getPhanTramGiam()) {
                        maxGiamGiaInfo = tmp;
                    }
                }
            }
            dto.setDotGiamGia(maxGiamGiaInfo);
        }

        return new ResponseObject<>(
                PageableObject.of(page),
                HttpStatus.OK,
                "Lấy danh sách sản phẩm giảm giá thành công"
        );
    }

}
