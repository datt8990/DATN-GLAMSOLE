package com.be.server.repository;

import com.be.server.entity.CartDetail;
import com.be.server.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail,String> {

    @Query(value = """

                        select
            cd.*  from
            	cart_detail cd
            join cart c on
            	cd.id_cart = c.id
            join san_pham_chi_tiet spct on
            	cd.id_san_pham_chi_tiet = spct.id
                 where c.id = :idCart
            """,nativeQuery = true)
    List<CartDetail> getAllCart(@Param("idCart") String idCart);

}
