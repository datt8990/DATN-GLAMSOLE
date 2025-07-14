package com.be.server.repository;

import com.be.server.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, String> {

    @Query("""
    SELECT spct
    FROM SanPhamChiTiet spct
    WHERE spct.sanPham.id = :idSP
    AND spct.status = 0
    """
    )
    List<SanPhamChiTiet> findBySanPham(@Param("idSP") String idSP);

}
