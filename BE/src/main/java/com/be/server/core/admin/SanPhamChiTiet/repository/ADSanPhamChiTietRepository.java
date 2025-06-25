package com.be.server.core.admin.SanPhamChiTiet.repository;

import com.be.server.core.admin.SanPhamChiTiet.model.request.ADSPCTSearchRequest;
import com.be.server.core.admin.SanPhamChiTiet.model.response.ADListThuocTinh;
import com.be.server.core.admin.SanPhamChiTiet.model.response.ADSanPhamChiTietDetail;
import com.be.server.core.admin.SanPhamChiTiet.model.response.ADSanPhamChiTietResponse;
import com.be.server.core.admin.SanPhamChiTiet.model.response.ADThemSanPhamChiTietResponse;
import com.be.server.repository.SanPhamChiTietRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ADSanPhamChiTietRepository extends SanPhamChiTietRepository {

    @Query(value = """
    SELECT 
        ROW_NUMBER() OVER (ORDER BY sp.id DESC) AS stt,
        spct.id AS id,    
        sp.ten AS ten,
        spct.soLuong as soLuong,
       th.ten AS tenThuongHieu,
       ld.ten AS tenLoaiDe,
        cl.ten AS tenChatLieu,
        dm.ten AS tenDanhMuc,
        spct.giaBan as giaBan,
        s.ten as kichThuoc,
        ms.mau as mau,
        spct.anh as anh,
        spct.status as status
    FROM 
        SanPhamChiTiet spct
        LEFT JOIN SanPham AS sp ON spct.sanPham.id = sp.id
            LEFT JOIN ThuongHieu AS th ON th.id = sp.thuongHieu.id
            LEFT JOIN XuatSu AS xx ON xx.id = sp.xuatSu.id
            LEFT JOIN KichCo AS kc ON kc.id = spct.kichCo.id
            LEFT JOIN LoaiDe AS ld ON ld.id = sp.loaiDe.id
            LEFT JOIN DanhMuc AS dm ON dm.id = sp.danhMuc.id
            LEFT JOIN ChatLieu AS cl ON cl.id = sp.chatLieu.id  
        LEFT JOIN MauSac AS ms ON ms.id = spct.mauSac.id
        LEFT JOIN KichCo AS s ON s.id = spct.kichCo.id
    WHERE 
        ( :#{#rep.idSP} IS NULL OR spct.sanPham.id = :#{#rep.idSP} ) 
        AND ( :#{#rep.q} IS NULL OR sp.ten LIKE CONCAT('%', :#{#rep.q}, '%') OR spct.ma LIKE CONCAT('%', :#{#rep.q}, '%') )
    ORDER BY spct.createdDate DESC
    """, countQuery = """
    SELECT 
        COUNT(d.id)
    FROM 
        SanPhamChiTiet d
    WHERE 
        ( :#{#rep.q} IS NULL OR d.ma LIKE CONCAT('%', :#{#rep.q}, '%') )
        AND ( :#{#rep.idSP} IS NULL OR d.sanPham.id = :#{#rep.idSP} )
    """)
    Page<ADSanPhamChiTietResponse> getAllSanPhamChiTietByFilter(Pageable pageable, @Param("rep") ADSPCTSearchRequest req);


    @Query(value = """
        SELECT 
            ROW_NUMBER() OVER (ORDER BY sp.id DESC) AS stt,
            sp.id AS id, 
            sp.ten AS ten, 
            sp.moTa AS moTa,
            th.ten AS tenThuongHieu,
            th.id AS idThuongHieu,
            xx.ten AS tenXuatXu,
            xx.id AS idXuatXu, 
            ld.ten AS tenLoaiDe,
            ld.id AS idLoaiDe, 
            dm.ten AS tenDanhMuc,
            dm.id AS idDanhMuc,   
            cl.ten AS tenChatLieu,
            cl.id AS idChatLieu
                    
        FROM 
            SanPham sp
            LEFT JOIN ThuongHieu AS th ON th.id = sp.thuongHieu.id
            LEFT JOIN XuatSu AS xx ON xx.id = sp.xuatSu.id
            LEFT JOIN LoaiDe AS ld ON ld.id = sp.loaiDe.id
            LEFT JOIN DanhMuc AS dm ON dm.id = sp.danhMuc.id
            LEFT JOIN ChatLieu AS cl ON cl.id = sp.chatLieu.id  
        WHERE 
            sp.id LIKE CONCAT('%', :#{#rep}, '%')           
        """ )
    Optional<ADThemSanPhamChiTietResponse> getSanPhamID(@Param("rep") String id);

    @Query(value = """
        SELECT 
            ROW_NUMBER() OVER (ORDER BY sp.id DESC) AS stt,
            spct.id AS id, 
            sp.ten AS ten, 
            sp.moTa AS moTa,
            th.ten AS tenThuongHieu,
            th.id AS idThuongHieu,
            xx.ten AS tenXuatXu,
            xx.id AS idXuatXu, 
            ld.ten AS tenLoaiDe,
            ld.id AS idLoaiDe, 
            dm.ten AS tenDanhMuc,
            dm.id AS idDanhMuc,   
            cl.ten AS tenChatLieu,
            cl.id AS idChatLieu,
            ms.ten AS tenMauSac,
            ms.id AS idMauSac,
            kc.ten AS tenKichThuoc,
            kc.id AS idKichThuoc,
            spct.soLuong as soLuong,
            spct.giaBan as giaBan                     
        FROM 
            SanPhamChiTiet spct
             LEFT JOIN SanPham AS sp ON sp.id = spct.sanPham.id       
            LEFT JOIN ThuongHieu AS th ON th.id = sp.thuongHieu.id
            LEFT JOIN XuatSu AS xx ON xx.id = sp.xuatSu.id
                        LEFT JOIN MauSac AS ms ON ms.id = spct.mauSac.id      
                                      LEFT JOIN KichCo AS kc ON kc.id = spct.kichCo.id
            LEFT JOIN LoaiDe AS ld ON ld.id = sp.loaiDe.id
            LEFT JOIN DanhMuc AS dm ON dm.id = sp.danhMuc.id
            LEFT JOIN ChatLieu AS cl ON cl.id = sp.chatLieu.id  
        WHERE 
            spct.id LIKE CONCAT('%', :#{#rep}, '%')           
        """ )
    Optional<ADSanPhamChiTietDetail> getSanPhamChiTietID(@Param("rep") String id);

    @Query(value=
            """
                    select 
                           th.ten as ten,
                           th.id as id
                          from KichCo th 
                          where th.status = 0
                         ORDER BY th.createdDate DESC
                    """
    )
    List<ADListThuocTinh> getListSize();

    @Query(value=
            """
                    select 
                           th.ten as ten,
                           th.id as id,
                           th.mau as mau
                          from MauSac th 
                          where th.status = 0
                         ORDER BY th.createdDate DESC
                    """
    )
    List<ADListThuocTinh> getListColor();

}
