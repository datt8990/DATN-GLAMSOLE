package com.be.server.core.admin.voucher.repository;


import com.be.server.core.admin.SanPhamChiTiet.model.response.ADThemSanPhamChiTietResponse;
import com.be.server.core.admin.voucher.model.response.ADPhieuGiamGiaResponse;

import com.be.server.entity.PhieuGiamGia;
import com.be.server.entity.ThuongHieu;
import com.be.server.repository.VoucherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;


@Repository
public interface ADVoucherRepository extends VoucherRepository {

    Page<PhieuGiamGia> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);


    @Query(value = """
        SELECT 
          pgg.id as id,      
          pgg.ma as ma, 
          pgg.ten as ten,
          pgg.dieuKien as dieuKien,
          pgg.giaGiam as giaGiam,
          pgg.kieuGiam as kieuGiam,
          pgg.loaiGiam as loaiGiam,
          pgg.phanTramGiam as phanTramGiam,
          pgg.soLuongPhieu  as soLuongPhieu,
          pgg.ngayBatDau as ngayBatDau,
          pgg.ngayKetThuc as ngayKetThuc                                                                    
        FROM 
            PhieuGiamGia  pgg
        WHERE 
            pgg.id LIKE CONCAT('%', :#{#rep}, '%')           
        """ )
    Optional<ADPhieuGiamGiaResponse> getNhanVienID(@Param("rep") String id);

    @Query("""
    SELECT DISTINCT pggct.khachHang.id 
    FROM PhieuGiamGiaChiTiet pggct
    WHERE pggct.phieuGiamGia.id = :id
""")
    List<String> getDanhSachKhachHang(@Param("id") String id);


}
