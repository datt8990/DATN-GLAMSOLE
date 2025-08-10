package com.be.server.core.admin.voucher.util;

import com.be.server.entity.PhieuGiamGia;
import com.be.server.infrastructure.constant.EntityStatus;
import com.be.server.repository.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PhieuGiamGiaScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PhieuGiamGiaScheduler.class);

    @Autowired
    private VoucherRepository voucherRepository;

    @Scheduled(cron = "0 1 0 * * ?")
    public void updateVoucherStatus() {
        logger.info("Bắt đầu cập nhật trạng thái phiếu giảm giá...");

        Date currentDate = truncateTime(new Date());

        List<PhieuGiamGia> vouchers = voucherRepository.findAll();

        for (PhieuGiamGia voucher : vouchers) {
            try {
                EntityStatus currentStatus = voucher.getStatus();
                EntityStatus newStatus = determineStatus(voucher, currentDate);

                if (currentStatus != newStatus) {
                    voucher.setStatus(newStatus);
                    voucherRepository.save(voucher);
                    logger.info("Cập nhật trạng thái phiếu giảm giá {} (ID: {}) từ {} sang {}",
                            voucher.getTen(), voucher.getId(), currentStatus, newStatus);
                }
            } catch (Exception e) {
                logger.error("Lỗi khi cập nhật trạng thái phiếu giảm giá ID {}: {}", voucher.getId(), e.getMessage());
            }
        }

        logger.info("Hoàn tất cập nhật trạng thái phiếu giảm giá.");
    }

    private EntityStatus determineStatus(PhieuGiamGia voucher, Date currentDate) {
        Date ngayBatDau = truncateTime(voucher.getNgayBatDau());
        Date ngayKetThuc = truncateTime(voucher.getNgayKetThuc());

        if (ngayBatDau == null || ngayKetThuc == null) {
            return EntityStatus.INACTIVE;
        }

        if (!currentDate.before(ngayBatDau) && !currentDate.after(ngayKetThuc)) {
            return EntityStatus.ACTIVE;
        }

        if (currentDate.after(ngayKetThuc)) {
            return EntityStatus.INACTIVE;
        }

        return EntityStatus.INACTIVE;
    }

    private Date truncateTime(Date date) {
        if (date == null) return null;

        return java.sql.Date.valueOf(date.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate());
    }
}
