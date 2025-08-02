package com.be.server.repository;

import com.be.server.entity.DotGiamGia;
import com.be.server.entity.PhieuGiamGia;
import com.be.server.infrastructure.constant.EntityStatus;
import com.be.server.infrastructure.constant.StatusPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface VoucherRepository extends JpaRepository<PhieuGiamGia, String> {


}
