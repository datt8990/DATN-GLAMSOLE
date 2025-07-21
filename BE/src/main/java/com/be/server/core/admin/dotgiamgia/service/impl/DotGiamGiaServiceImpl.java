package com.be.server.core.admin.dotgiamgia.service.impl;
import com.be.server.core.admin.dotgiamgia.model.request.CreateDotGiamGiaRequest;
import com.be.server.core.admin.dotgiamgia.model.request.FindDotGiamGiaRequest;
import com.be.server.core.admin.dotgiamgia.model.request.IdProductDetail;
import com.be.server.core.admin.dotgiamgia.model.request.UpdateDotGiamGiaRequest;
import com.be.server.core.admin.dotgiamgia.model.response.DotGiamGiaByIdResponse;
import com.be.server.core.admin.dotgiamgia.model.response.DotGiamGiaResponse;
import com.be.server.core.admin.dotgiamgia.model.response.PromotionByProDuctDetail;
import com.be.server.core.admin.dotgiamgia.repository.ADDotGiamGiaChiTietRepository;
import com.be.server.core.admin.dotgiamgia.repository.ADDotGiamGiaRepository;
import com.be.server.core.admin.dotgiamgia.service.DotGiamGiaService;
import com.be.server.core.common.base.PageableObject;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.core.exception.BadRequestException;
import com.be.server.entity.DotGiamGia;
import com.be.server.entity.DotGiamGiaChiTietSanPham;
import com.be.server.entity.SanPham;
import com.be.server.entity.SanPhamChiTiet;
import com.be.server.infrastructure.constant.Status;
import com.be.server.infrastructure.constant.StatusPromotion;
import com.be.server.repository.DotGiamGiaRepository;
import com.be.server.repository.SanPhamChiTietRepository;
import com.be.server.repository.SanPhamRepository;
import com.be.server.utils.Helper;
import com.be.server.utils.RandomNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DotGiamGiaServiceImpl implements DotGiamGiaService {

    @Autowired
    private ADDotGiamGiaRepository dotGiamGiaRepository;

    @Autowired
    private ADDotGiamGiaChiTietRepository dotGiamGiaChiTietRepository;

    @Autowired private SanPhamRepository sanPhamRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Override
    public ResponseObject<?> getAll(FindDotGiamGiaRequest findDotGiamGiaRequest) {
        Pageable pageable = Helper.createPageable(findDotGiamGiaRequest, "created_date");
        Page<DotGiamGiaResponse> page = dotGiamGiaRepository.getAllDotGiamGia(findDotGiamGiaRequest, pageable);

        return new ResponseObject<>(
                PageableObject.of(page),
                HttpStatus.OK,
                "Lấy danh sách đợt giảm giá thành công"
        );
    }

    @Override
    public List<SanPham> getSanPham() {
        List<SanPham> sanPhams = sanPhamRepository.findAllSanPham();
        return sanPhams;
    }

    @Override
    public List<SanPhamChiTiet> getSanPhamCT(String id) {
        return sanPhamChiTietRepository.findBySanPham(id);
    }

    @Override
    public List<SanPhamChiTiet> getSanPhamByDot(String id) {
        return  sanPhamChiTietRepository.detailSPCTByDot(id);
    }

    @Override
    public DotGiamGia add(CreateDotGiamGiaRequest request) throws Exception {
        Optional<DotGiamGia> optionalPromotion = dotGiamGiaRepository.findByTen(request.getName());
        if (optionalPromotion.isPresent()) {
            throw new BadRequestException("Tên khuyến mại đã tồn tại");
        }
        if (ObjectUtils.isEmpty(request.getIdProductDetails())) {
            throw new BadRequestException("Không có sản phẩm");
        }
        for (IdProductDetail x : request.getIdProductDetails()) {
            Optional<SanPhamChiTiet> optional = sanPhamChiTietRepository.findById(x.getId());
            if (!optional.isPresent()) {
                throw new BadRequestException("Có sản phẩm không tồn tại");
            }
        }
        long currentMillis = System.currentTimeMillis();

// Lấy ngày hiện tại (today)
        LocalDate today = Instant.ofEpochMilli(currentMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

// Lấy ngày từ request
        LocalDate startDate = Instant.ofEpochMilli(request.getStartDate())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

// Chỉ reject nếu startDate nằm **trước hôm nay**
        if (startDate.isBefore(today)) {
            throw new BadRequestException("Ngày bắt đầu không được nằm trong quá khứ");
        }
        if (request.getEndDate() < request.getStartDate()) {
            throw new BadRequestException("Ngày kết thúc phải lớn hơn ngày bắt đầu");
        }
//        if (hasOverlappingDiscounts(request.getIdProductDetails(), request.getStartDate(), request.getEndDate())) {
//            throw new BadRequestException("Một số sản phẩm đã nằm trong đợt giảm giá khác trong khoảng thời gian đã chọn.");
//        }
        StatusPromotion status = getStatusPromotion(request.getStartDate(), request.getEndDate());
        DotGiamGia dotGiamGia = DotGiamGia.builder().ma(new RandomNumberGenerator().randomToString("KM", 900000000))
                .ten(request.getName()).phanTramGiam(request.getValue())
                .ngayBatDau(request.getStartDate()).ngayKetThuc(request.getEndDate())
                .trangThai(status).build();
        dotGiamGiaRepository.save(dotGiamGia);

        List<DotGiamGiaChiTietSanPham> promotionProductDetails = new ArrayList<>();
        for (IdProductDetail x : request.getIdProductDetails()) {
            Optional<SanPhamChiTiet> optional = sanPhamChiTietRepository.findById(x.getId());
            DotGiamGiaChiTietSanPham promotionProductDetail = new DotGiamGiaChiTietSanPham();
            promotionProductDetail.setMa("DGCTSP-" + UUID.randomUUID());
            promotionProductDetail.setDotGiamGia(dotGiamGia);
            promotionProductDetail.setSanPhamChiTiet(optional.get());
            promotionProductDetail.setTrangThai(Status.DANG_SU_DUNG);
            promotionProductDetails.add(promotionProductDetail);
        }
        dotGiamGiaChiTietRepository.saveAll(promotionProductDetails);
        return dotGiamGia;
    }

    @Override
    @Transactional
    public DotGiamGia update(UpdateDotGiamGiaRequest request) {

        log.info("Request update Đợt giảm giá : ====>{}  ",request.toString());
        Optional<DotGiamGia> optional = dotGiamGiaRepository.findById(request.getId());
        if (!optional.isPresent()) {
            throw new RuntimeException("Khuyến mại không tồn tại");
        }
        log.info("Danh sách id product");

        for (IdProductDetail x : request.getIdProductDetails()) {
            log.info(x.toString());
            Optional<SanPhamChiTiet> optional1 = sanPhamChiTietRepository.findById(x.getId());
            if (!optional1.isPresent()) {
                throw new RuntimeException("Có sản phẩm không tồn tại");
            }
        }
        long currentMillis = System.currentTimeMillis();

// Lấy ngày hiện tại (today)
        LocalDate today = Instant.ofEpochMilli(currentMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

// Lấy ngày từ request
        LocalDate startDate = Instant.ofEpochMilli(request.getStartDate())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

// Chỉ reject nếu startDate nằm **trước hôm nay**
        if (startDate.isBefore(today)) {
            throw new BadRequestException("Ngày bắt đầu không được nằm trong quá khứ");
        }
        if (request.getEndDate() < request.getStartDate()) {
            throw new BadRequestException("Ngày kết thúc phải lớn hơn ngày bắt đầu");
        }

        StatusPromotion status = getStatusPromotion(request.getStartDate(), request.getEndDate());
        DotGiamGia promotion = optional.get();
        promotion.setTen(request.getName());
        promotion.setPhanTramGiam(request.getValue());
        promotion.setNgayBatDau(request.getStartDate());
        promotion.setNgayKetThuc(request.getEndDate());
        promotion.setTrangThai(status);
        dotGiamGiaRepository.save(promotion);

        //nếu status == HET_HAN_KICH_HOAT => các sản phẩm không được sử dụng khuyến mại này
        boolean checkStatus = updateProductDetailsStatus(promotion.getId(), promotion.getTrangThai());

        if (checkStatus == false) {
            DotGiamGiaByIdResponse promotionByIdRespone = dotGiamGiaRepository.getByIdPromotion(request.getId());
            List<DotGiamGiaChiTietSanPham> promotionProductDetails = new ArrayList<>();
            if (promotionByIdRespone.getProductDetailUpdate() == null) {
                for (IdProductDetail idProductDetailNew : request.getIdProductDetails()) {
                    DotGiamGiaChiTietSanPham promotionProductDetail = new DotGiamGiaChiTietSanPham();
                    promotionProductDetail.setDotGiamGia(promotion);
                    promotionProductDetail.setSanPhamChiTiet(sanPhamChiTietRepository.findById(idProductDetailNew.getId()).get());
                    promotionProductDetail.setTrangThai(getStatus(status));
                    promotionProductDetails.add(promotionProductDetail);
                }
                dotGiamGiaChiTietRepository.saveAll(promotionProductDetails);
            } else {
                for (String idProductDetailOld : promotionByIdRespone.getProductDetailUpdate().split(",")) {
                    boolean foundInNew = false;
                    for (IdProductDetail idProductDetailNew : request.getIdProductDetails()) {
                        if (idProductDetailNew.getId().contains(idProductDetailOld)) {
                            foundInNew = true;
                            break;
                        }
                    }

                    if (!foundInNew) {
                        DotGiamGiaChiTietSanPham promotionProductDetail = dotGiamGiaChiTietRepository.getByProductDetailAndPromotion(idProductDetailOld, promotionByIdRespone.getId());
                        promotionProductDetail.setTrangThai(Status.KHONG_SU_DUNG);
                        dotGiamGiaChiTietRepository.save(promotionProductDetail);
                    } else {
                        DotGiamGiaChiTietSanPham promotionProductDetail = dotGiamGiaChiTietRepository.getByProductDetailAndPromotion(idProductDetailOld, promotionByIdRespone.getId());
                        promotionProductDetail.setTrangThai(Status.DANG_SU_DUNG);
                        dotGiamGiaChiTietRepository.save(promotionProductDetail);
                    }
                }

                for (IdProductDetail idProductDetailNew : request.getIdProductDetails()) {
                    boolean foundInOld = false;

                    for (String idProductDetailOld : promotionByIdRespone.getProductDetailUpdate().split(",")) {
                        if (idProductDetailOld.contains(idProductDetailNew.getId())) {
                            foundInOld = true;
                            break;
                        }
                    }

                    if (!foundInOld) {
                        DotGiamGiaChiTietSanPham promotionProductDetail = new DotGiamGiaChiTietSanPham();
                        promotionProductDetail.setDotGiamGia(promotion);
                        promotionProductDetail.setSanPhamChiTiet(sanPhamChiTietRepository.findById(idProductDetailNew.getId()).get());
                        promotionProductDetail.setTrangThai(Status.DANG_SU_DUNG);
                        dotGiamGiaChiTietRepository.save(promotionProductDetail);
                    }
                }
            }
        }
        return promotion;
    }

    @Override
    public DotGiamGia updateStatus(String id) {
        Optional<DotGiamGia> optional = dotGiamGiaRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("Khuyến mại không tồn tại");
        }
        DotGiamGia promotion = optional.get();
        StatusPromotion status = getStatusPromotion(promotion.getNgayBatDau(), promotion.getNgayKetThuc());
        promotion.setTrangThai(status);
        dotGiamGiaRepository.save(promotion);

        //nếu status == HET_HAN_KICH_HOAT => các sản phẩm không được sử dụng khuyến mại này
        updateProductDetailsStatus(promotion.getId(),promotion.getTrangThai());
        return promotion;
    }

    @Override
    public DotGiamGiaByIdResponse getByIdPromotion(String id) {
        return dotGiamGiaRepository.getByIdPromotion(id);
    }

    @Override
    public List<PromotionByProDuctDetail> getByIdProductDetail(String id) {
        return dotGiamGiaRepository.getByIdProductDetail(id);
    }

    private Status getStatus(StatusPromotion status) {
        return status == StatusPromotion.DANG_KICH_HOAT || status == StatusPromotion.CHUA_KICH_HOAT ? Status.DANG_SU_DUNG : Status.KHONG_SU_DUNG;
    }

    private StatusPromotion getStatusPromotion(long startDate, long endDate) {
        LocalDate currentDate = Instant.ofEpochMilli(System.currentTimeMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate start = Instant.ofEpochMilli(startDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate end = Instant.ofEpochMilli(endDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (start.isAfter(currentDate)) {
            return StatusPromotion.CHUA_KICH_HOAT;
        } else if (end.isBefore(currentDate)) {
            return StatusPromotion.HET_HAN_KICH_HOAT;
        } else {
            return StatusPromotion.DANG_KICH_HOAT;
        }
    }


    private boolean updateProductDetailsStatus(String idPromotion, StatusPromotion status) {
        List<DotGiamGiaChiTietSanPham> promotionProductDetailList = dotGiamGiaChiTietRepository.findAllByIdPromotion(idPromotion);
        if (status.equals(StatusPromotion.HET_HAN_KICH_HOAT)) {
            List<DotGiamGiaChiTietSanPham> updatePromotionProductDetails = promotionProductDetailList.stream().map(promotionProductDetail -> {
                promotionProductDetail.setTrangThai(Status.KHONG_SU_DUNG);
                return promotionProductDetail;
            }).collect(Collectors.toList());
            dotGiamGiaChiTietRepository.saveAll(updatePromotionProductDetails);
            return true;
        }
        return false;
    }

    public boolean hasOverlappingDiscounts(List<IdProductDetail> productDetails, long startDate, long endDate) {
        List<String> productDetailIds = productDetails.stream()
                .map(IdProductDetail::getId)
                .collect(Collectors.toList());

        List<DotGiamGia> existingDiscounts = dotGiamGiaRepository.findAllByProductDetails(productDetailIds);

        for (DotGiamGia discount : existingDiscounts) {
            if ((startDate < discount.getNgayKetThuc() && endDate > discount.getNgayBatDau())) {
                return true;
            }
        }
        return false;
    }

}