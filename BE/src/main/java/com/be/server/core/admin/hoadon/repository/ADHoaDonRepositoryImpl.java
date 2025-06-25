package com.be.server.core.admin.hoadon.repository;

import com.be.server.core.admin.hoadon.model.request.ADHoaDonSearchRequest;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponse;
import com.be.server.core.admin.hoadon.model.response.ADHoaDonResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ADHoaDonRepositoryImpl implements ADHoaDonRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ADHoaDonResponse> getAllHoaDonResponse(ADHoaDonSearchRequest request, Pageable pageable) {
        String hql = """
        SELECT new com.be.server.core.admin.hoadon.model.response.ADHoaDonResponse(
            hd.ma,
            kh.ten,
            kh.sdt,
            nv.ten,
            hd.tongTien,
            hd.loaiHoaDon,
            hd.createdDate,
            hd.status
        )
        FROM HoaDon hd
        LEFT JOIN hd.khachHang kh
        LEFT JOIN hd.nhanVien nv
        WHERE (:q IS NULL OR :q = '' 
            OR LOWER(kh.ten) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(kh.sdt) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(nv.ten) LIKE LOWER(CONCAT('%', :q, '%')))
        ORDER BY hd.createdDate DESC
    """;

        Long totalRecords = (Long) entityManager.createQuery("""
        SELECT COUNT(hd)
        FROM HoaDon hd
        LEFT JOIN hd.khachHang kh
        LEFT JOIN hd.nhanVien nv
        WHERE (:q IS NULL OR :q = '' 
            OR LOWER(kh.ten) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(kh.sdt) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(nv.ten) LIKE LOWER(CONCAT('%', :q, '%')))
    """)
                .setParameter("q", request.getQ() == null ? "" : request.getQ().trim())
                .getSingleResult();

        List<ADHoaDonResponse> hoaDonResponses = entityManager.createQuery(hql, ADHoaDonResponse.class)
                .setParameter("q", request.getQ() == null ? "" : request.getQ().trim())
                .setFirstResult((int) pageable.getOffset())  // Chỉ định vị trí bắt đầu (offset)
                .setMaxResults(pageable.getPageSize())     // Chỉ định số lượng bản ghi trên mỗi trang
                .getResultList();

        return new PageImpl<>(hoaDonResponses, pageable, totalRecords);
    }


    @Override
    public List<ADHoaDonChiTietResponse> getAllHoaDonChiTietResponse(String maHoaDon) {
        String hql = """
                SELECT new com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponse(
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

        return entityManager.createQuery(hql, ADHoaDonChiTietResponse.class)
                .setParameter("maHoaDon", maHoaDon)
                .getResultList();
    }


}
