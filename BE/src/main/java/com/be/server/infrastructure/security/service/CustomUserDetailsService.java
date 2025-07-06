package com.be.server.infrastructure.security.service;

import com.be.server.entity.KhachHang;
import com.be.server.infrastructure.constant.EntityStatus;
import com.be.server.infrastructure.security.repository.KhachHangAuthRepository;
import com.be.server.infrastructure.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final KhachHangAuthRepository userAuthRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("EMAIL: " + email);

        Optional<KhachHang> existingUser = userAuthRepository.findByEmailAndStatus(email, EntityStatus.ACTIVE);
        if (existingUser.isPresent()) {
            KhachHang user = existingUser.get();
            System.out.println("loadUserByUsername: " + user);
            return UserPrincipal.create(user);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }


}
