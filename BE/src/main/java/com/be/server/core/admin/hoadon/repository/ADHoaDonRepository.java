package com.be.server.core.admin.hoadon.repository;

import com.be.server.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ADHoaDonRepository extends JpaRepository<HoaDon, String>, ADHoaDonRepositoryCustom {

}
