package com.be.server.core.admin.khachhang.repository;

import com.be.server.entity.KhachHang;
import com.be.server.repository.KhachHangRepository;
import com.be.server.repository.SizeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ADKhachHangRepository extends KhachHangRepository {

    Page<KhachHang> findByMaContainingOrTenContainingOrSdtContainingOrDiaChiContaining(String ma, String ten, String sdt, String diaChi, Pageable pageable);

}
