package com.be.server.repository;

import com.be.server.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanPhamRepository extends JpaRepository<SanPham, String> {
}
