package com.be.server.core.admin.mausac.repository;

import com.be.server.entity.MauSac;
import com.be.server.repository.MauSacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ADMauSacRepository extends MauSacRepository {

    Page<MauSac> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);

}
