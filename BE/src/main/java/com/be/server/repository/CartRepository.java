package com.be.server.repository;

import com.be.server.entity.Cart;
import com.be.server.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByKhachHang(KhachHang khachHang);


}
