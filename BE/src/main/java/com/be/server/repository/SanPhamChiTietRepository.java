package com.be.server.repository;

import com.be.server.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, String> {


    @Query(value = """
            SELECT spct.* FROM datn_v2.san_pham_chi_tiet spct JOIN datn_v2.san_pham sp ON sp.id = spct.id_san_pham WHERE sp.id = :idSP AND spct.status = 0""", nativeQuery = true)
    List<SanPhamChiTiet> findBySanPham(String idSP);


    @Query(value = """
            SELECT
                spct.*
            FROM
                db_datn.san_pham_chi_tiet spct
            JOIN
                db_datn.san_pham sp ON sp.id = spct.id_san_pham
            LEFT JOIN
                db_datn.dot_giam_gia_chi_tiet_san_pham dggctsp ON dggctsp.id_chi_tiet_san_pham = spct.id
            LEFT JOIN
                db_datn.dot_giam_gia dgg ON dgg.id = dggctsp.id_dot_giam_gia
            WHERE
                sp.id = :idSP
                AND spct.status = 0
                AND (dgg.id IS NULL OR dgg.trang_thai != 'DANG_KICH_HOAT');
            """, nativeQuery = true)
    List<SanPhamChiTiet> detailSPCTByDot(String idSP);
}
