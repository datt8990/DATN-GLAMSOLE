package com.be.server.repository;

import com.be.server.entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<PhieuGiamGia, String> {
}
