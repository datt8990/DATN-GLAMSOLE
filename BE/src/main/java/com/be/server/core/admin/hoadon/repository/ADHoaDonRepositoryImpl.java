package com.be.server.core.admin.hoadon.repository;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponseDetail;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonResponse;
import com.be.server.core.admin.hoadon.model.response.HoaDonPageResponse;
import com.be.server.entity.HoaDon;
import com.be.server.infrastructure.constant.EntityTrangThaiHoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ADHoaDonRepositoryImpl implements ADHoaDonRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public HoaDonPageResponse getAllHoaDonResponse(ADHoaDonSearchRequest request, Pageable pageable) {
        String hql = """
        SELECT new com.be.server.core.admin.hoadon.model.response.ADHoaDonResponse(
                        hd.id,
                        hd.ma,
                        kh.ten,
                        kh.sdt,
                        nv.ten,
                        hd.tongTien,
                        hd.loaiHoaDon,
                        hd.createdDate,
                        hd.trangThaiHoaDon
                    )
                    FROM HoaDon hd
                    LEFT JOIN hd.khachHang kh
                    LEFT JOIN hd.nhanVien nv
                    WHERE (:q IS NULL OR :q = '' 
                        OR LOWER(kh.ten) LIKE LOWER(CONCAT('%', :q, '%'))
                        OR LOWER(kh.sdt) LIKE LOWER(CONCAT('%', :q, '%'))
                        OR LOWER(nv.ten) LIKE LOWER(CONCAT('%', :q, '%')))
                      AND (:trangThai IS NULL OR hd.trangThaiHoaDon = :trangThai)
                      AND (:startDate IS NULL OR CAST(hd.createdDate AS BIGINTEGER) >= :startDate)
                      AND (:endDate IS NULL OR CAST(hd.createdDate AS BIGINTEGER) <= :endDate)
                    ORDER BY hd.createdDate DESC
                """;

        String countByStatusHql = """
                    SELECT hd.trangThaiHoaDon, COUNT(hd)
                    FROM HoaDon hd
                    LEFT JOIN hd.khachHang kh
                    LEFT JOIN hd.nhanVien nv
                    WHERE (:q IS NULL OR :q = '' 
                        OR LOWER(kh.ten) LIKE LOWER(CONCAT('%', :q, '%'))
                        OR LOWER(kh.sdt) LIKE LOWER(CONCAT('%', :q, '%'))
                        OR LOWER(nv.ten) LIKE LOWER(CONCAT('%', :q, '%')))
                      AND (:startDate IS NULL OR CAST(hd.createdDate AS BIGINTEGER) >= :startDate)
                      AND (:endDate IS NULL OR CAST(hd.createdDate AS BIGINTEGER) <= :endDate)
                    GROUP BY hd.trangThaiHoaDon
                """;

        String totalCountHql = """
                    SELECT COUNT(hd)
                    FROM HoaDon hd
                    LEFT JOIN hd.khachHang kh
                    LEFT JOIN hd.nhanVien nv
                    WHERE (:q IS NULL OR :q = '' 
                        OR LOWER(kh.ten) LIKE LOWER(CONCAT('%', :q, '%'))
                        OR LOWER(kh.sdt) LIKE LOWER(CONCAT('%', :q, '%'))
                        OR LOWER(nv.ten) LIKE LOWER(CONCAT('%', :q, '%')))
                      AND (:trangThai IS NULL OR hd.trangThaiHoaDon = :trangThai)
                      AND (:startDate IS NULL OR CAST(hd.createdDate AS BIGINTEGER) >= :startDate)
                      AND (:endDate IS NULL OR CAST(hd.createdDate AS BIGINTEGER) <= :endDate)
                """;

        List<Object[]> countByStatusList = entityManager.createQuery(countByStatusHql)
                .setParameter("q", request.getQ() == null ? "" : request.getQ().trim())
                .setParameter("startDate", request.getStartDate())
                .setParameter("endDate", request.getEndDate())
                .getResultList();

        Map<EntityTrangThaiHoaDon, Long> countByStatusMap = new HashMap<>();
        for (Object[] row : countByStatusList) {
            EntityTrangThaiHoaDon status = (EntityTrangThaiHoaDon) row[0];
            Long count = (Long) row[1];
            countByStatusMap.put(status, count);
        }

        Long totalRecords = (Long) entityManager.createQuery(totalCountHql)
                .setParameter("q", request.getQ() == null ? "" : request.getQ().trim())
                .setParameter("trangThai", request.getStatus())
                .setParameter("startDate", request.getStartDate())
                .setParameter("endDate", request.getEndDate())
                .getSingleResult();

        List<ADHoaDonResponse> hoaDonResponses = entityManager.createQuery(hql, ADHoaDonResponse.class)
                .setParameter("q", request.getQ() == null ? "" : request.getQ().trim())
                .setParameter("trangThai", request.getStatus())
                .setParameter("startDate", request.getStartDate())
                .setParameter("endDate", request.getEndDate())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        Page<ADHoaDonResponse> pageResult = new PageImpl<>(hoaDonResponses, pageable, totalRecords);
        return new HoaDonPageResponse(pageResult, countByStatusMap);
    }


    @Override
    public List<ADHoaDonChiTietResponseDetail> getAllHoaDonChiTietResponse(String maHoaDon) {
        String hql = """
                SELECT new com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponseDetail(
                    hd.ten,
                    hdct.ma,
                    sp.ten,
                    hdct.spct.anh,
                    sp.thuongHieu.ten,
                    sp.xuatSu.ten,
                    spct.mauSac.ten,
                    spct.kichCo.ten,
                    hdct.soLuong,
                    hdct.tongTien,
                    (hdct.soLuong * hdct.tongTien),
                    kh.ten,
                    kh.sdt,
                    hd.loaiHoaDon,
                    hd.status,
                    hd.createdDate
                )
                FROM HoaDonChiTiet hdct
                JOIN hdct.hoaDon hd
                JOIN hd.khachHang kh
                JOIN hdct.spct spct
                JOIN spct.sanPham sp
                WHERE hdct.hoaDon.ma = :maHoaDon
                """;

        return entityManager.createQuery(hql, ADHoaDonChiTietResponseDetail.class)
                .setParameter("maHoaDon", maHoaDon)
                .getResultList();
    }

    @Override
    @Transactional
    public HoaDon updateTrangThaiHoaDon(String maHoaDon, EntityTrangThaiHoaDon newTrangThai) {
        String hql = """
                    UPDATE HoaDon hd
                    SET hd.trangThaiHoaDon = :newTrangThai
                    WHERE hd.ma = :maHoaDon
                """;

        int updatedRows = entityManager.createQuery(hql)
                .setParameter("newTrangThai", newTrangThai)
                .setParameter("maHoaDon", maHoaDon)
                .executeUpdate();

        if (updatedRows == 0) {
            throw new EntityNotFoundException("Không tìm thấy hóa đơn với mã: " + maHoaDon);
        }

        // Retrieve the updated HoaDon object
        return entityManager.createQuery("SELECT hd FROM HoaDon hd WHERE hd.ma = :maHoaDon", HoaDon.class)
                .setParameter("maHoaDon", maHoaDon)
                .getSingleResult();
    }

}
