package com.be.server.core.admin.loaide.repository;

import com.be.server.entity.LoaiDe;
import com.be.server.repository.LoaiDeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ADLoaiDeRepository extends LoaiDeRepository {

    Page<LoaiDe> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);

    List<LoaiDe> findByTenContaining(String ten);

}
