package com.be.server.repository;

import com.be.server.entity.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<KichCo, String> {
}
