package com.be.server.core.admin.voucher.repository;

import com.be.server.entity.PhieuGiamGia;
import com.be.server.entity.ThuongHieu;
import com.be.server.repository.VoucherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ADVoucherRepository extends VoucherRepository {

    Page<PhieuGiamGia> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);

}
