package com.be.server.core.admin.mausac.repository;
import com.be.server.entity.DanhMuc;
import com.be.server.entity.MauSac;
import com.be.server.repository.MauSacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ADMauSacRepository extends MauSacRepository {

    Page<MauSac> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);

    List<MauSac> findByTen(String ten);


}
