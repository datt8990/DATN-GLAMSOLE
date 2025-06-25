package com.be.server.core.admin.kichthuoc.repository;

import com.be.server.entity.KichCo;
import com.be.server.repository.SizeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ADKichThuocRepository extends SizeRepository {

    Page<KichCo> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);

}
