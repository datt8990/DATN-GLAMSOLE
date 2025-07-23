package com.be.server.core.admin.dotgiamgia.util;

import com.be.server.entity.DotGiamGia;
import com.be.server.entity.DotGiamGiaChiTietSanPham;
import com.be.server.entity.SanPhamChiTiet;
import com.be.server.infrastructure.constant.StatusPromotion;
import com.be.server.repository.DotGiamGiaChiTietSanPhamRepository;
import com.be.server.repository.DotGiamGiaRepository;
import com.be.server.repository.SanPhamChiTietRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DotGiamGiaScheduler {

    private final DotGiamGiaRepository dotGiamGiaRepository;
    private final DotGiamGiaChiTietSanPhamRepository dotGiamGiaChiTietSanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    @Scheduled(fixedDelay = 1000) // mỗi phút
    public void autoUpdatePromotionStatuses() {
        long now = System.currentTimeMillis();

        LocalDate today = LocalDate.now(); // lấy ngày hiện tại
        long epochMillisStartOfToday = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();


        // 1. Cập nhật sang DANG_KICH_HOAT nếu đến thời gian bắt đầu
        List<DotGiamGia> toActivate = dotGiamGiaRepository
                .findAllByTrangThaiAndNgayBatDauLessThanEqual(StatusPromotion.CHUA_KICH_HOAT, now);

        for (DotGiamGia dgg : toActivate) {
            dgg.setTrangThai(StatusPromotion.DANG_KICH_HOAT);
            updateGiaSanPhamTheoDotGiamGia(dgg.getId(), StatusPromotion.DANG_KICH_HOAT);
        }

        // 2. Cập nhật sang HET_HAN_KICH_HOAT nếu đã hết hạn
        List<DotGiamGia> toExpire = dotGiamGiaRepository
                .findAllByTrangThaiAndNgayKetThucLessThan(StatusPromotion.DANG_KICH_HOAT, epochMillisStartOfToday);

        for (DotGiamGia dgg : toExpire) {
            dgg.setTrangThai(StatusPromotion.HET_HAN_KICH_HOAT);
            updateGiaSanPhamTheoDotGiamGia(dgg.getId(), StatusPromotion.HET_HAN_KICH_HOAT);
        }

        dotGiamGiaRepository.saveAll(toActivate);
        dotGiamGiaRepository.saveAll(toExpire);

        if (!toActivate.isEmpty() || !toExpire.isEmpty()) {
            log.info("Đã cập nhật trạng thái {} đợt giảm giá", toActivate.size() + toExpire.size());
        }
    }

    private void updateGiaSanPhamTheoDotGiamGia(String idDotGiamGia, StatusPromotion trangThaiMoi) {
        List<DotGiamGiaChiTietSanPham> chiTietList = dotGiamGiaChiTietSanPhamRepository.findAllByDotGiamGiaId(idDotGiamGia);

        for (DotGiamGiaChiTietSanPham chiTiet : chiTietList) {
            SanPhamChiTiet spct = chiTiet.getSanPhamChiTiet();
            if (trangThaiMoi.equals(StatusPromotion.DANG_KICH_HOAT)) {
                spct.setGiaBan(chiTiet.getGiaSau());
            } else {
                spct.setGiaBan(chiTiet.getGiaTruoc());
            }
            sanPhamChiTietRepository.save(spct);
        }
    }

}
