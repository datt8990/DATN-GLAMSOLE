package com.be.server.core.admin.loaigiay.repository;

import com.be.server.entity.DanhMuc;
import com.be.server.repository.DanhMucRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ADLoaiGiayRepository extends DanhMucRepository {

    Page<DanhMuc> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);

}
