package com.be.server.repository;

import com.be.server.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, String> {

    @Query(value = """
            SELECT spct.* FROM db_datn.san_pham_chi_tiet spct JOIN db_datn.san_pham sp ON sp.id = spct.id_san_pham WHERE sp.id = :idSP AND spct.status = 0""", nativeQuery = true)
    List<SanPhamChiTiet> findBySanPham(String idSP);
}
