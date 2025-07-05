package com.be.server.core.admin.loaigiay.repository;

import com.be.server.entity.DanhMuc;
import com.be.server.repository.DanhMucRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ADLoaiGiayRepository extends DanhMucRepository {

    Page<DanhMuc> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);


    List<DanhMuc> findByTenContaining(String ten);


}
