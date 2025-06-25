package com.be.server.core.admin.thuonghieu.repository;

import com.be.server.entity.ThuongHieu;
import com.be.server.repository.ThuongHieuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ADThuongHieuRepository extends ThuongHieuRepository {

    Page<ThuongHieu> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);

}
