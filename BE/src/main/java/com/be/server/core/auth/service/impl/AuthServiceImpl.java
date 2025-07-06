package com.be.server.core.auth.service.impl;

import com.be.server.core.auth.dto.request.RegisterRequest;
import com.be.server.core.auth.repository.AuthUserRepository;
import com.be.server.core.auth.service.AuthService;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.entity.KhachHang;
import com.be.server.infrastructure.constant.EntityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseObject<?> register(RegisterRequest request) {
        Optional<KhachHang> existingUser = authUserRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT, "Email đã tồn tại");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        KhachHang newUser = new KhachHang();
        newUser.setTen(request.getUserName());
        newUser.setEmail(request.getEmail());
        newUser.setMat_khau(encodedPassword);
        newUser.setStatus(EntityStatus.ACTIVE);

        authUserRepository.save(newUser);

        return new ResponseObject<>(null, HttpStatus.OK, "Đăng ký thành công");
    }

}
