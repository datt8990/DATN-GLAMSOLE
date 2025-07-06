package com.be.server.core.admin.banhang.repository;
import com.be.server.core.admin.banhang.model.response.ListHoaDon;
import com.be.server.entity.HoaDon;
import com.be.server.repository.HoaDonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ADTaoHoaDonRepository extends HoaDonRepository {

    @Query(value= """
    select hd.id as id,
            hd.ma as ma,
            sum(hdct.soLuong) as soLuong
     from HoaDon hd 
    left join HoaDonChiTiet hdct on hd.id = hdct.hoaDon.id
    where hd.trangThaiHoaDon = 0
    GROUP BY hd.id,hd.ma
    ORDER BY hd.id ASC
    
""")
    List<ListHoaDon> getAll();


}
