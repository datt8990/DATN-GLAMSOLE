package com.be.server.infrastructure.security.user;

import com.be.server.entity.KhachHang;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class UserPrincipal implements OAuth2User, UserDetails {

    @Getter
    private final String id;

    @Getter
    private final String email;

    private final String password;

    @Setter
    private Map<String, Object> attributes;

    public UserPrincipal(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static UserPrincipal create(KhachHang khachHang) {
        return new UserPrincipal(
                khachHang.getId(),
                khachHang.getEmail(),
                khachHang.getMat_khau()
        );
    }

    public static UserPrincipal create(KhachHang khachHang, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = create(khachHang);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getPassword() {
        log.info("Mật khẩu từ DB (đã băm): {}", password);
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Không có phân quyền nên trả về danh sách trống.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
