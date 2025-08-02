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

        Date currentDate = new Date();
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
        Date ngayBatDau = voucher.getNgayBatDau();
        Date ngayKetThuc = voucher.getNgayKetThuc();

        if (ngayBatDau == null || ngayKetThuc == null) {
            return EntityStatus.INACTIVE;
        }

        long currentDateTruncated = truncateTime(currentDate);
        long ngayBatDauTruncated = truncateTime(ngayBatDau);
        long ngayKetThucTruncated = truncateTime(ngayKetThuc);

        if (currentDateTruncated >= ngayBatDauTruncated && currentDateTruncated <= ngayKetThucTruncated) {
            return EntityStatus.ACTIVE;
        } else {
            return EntityStatus.INACTIVE;
        }
    }

    private long truncateTime(Date date) {
        if (date == null) {
            return 0;
        }
        return new java.sql.Date(date.getTime()).getTime();
    }
}
