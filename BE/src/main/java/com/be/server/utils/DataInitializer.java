package com.be.server.utils;

import com.be.server.entity.NhanVien;
import com.be.server.entity.Role;
import com.be.server.infrastructure.constant.EntityRole;
import com.be.server.repository.NhanVienRepository;
import com.be.server.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final AppConfig appConfig;
    private final NhanVienRepository nhanVienRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void init() {
        if (!appConfig.isGenerateDb()) {
            System.out.println("⚠ Không tạo dữ liệu mặc định vì GENERATE_DB = false");
            return;
        }

        // ===== 1. Đảm bảo tồn tại role ADMIN =====
        Role adminRole = roleRepository.findByCode("ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setCode("ADMIN");
            adminRole.setName("Administrator");
            roleRepository.save(adminRole);
            System.out.println(" Đã tạo role ADMIN");
        } else {
            System.out.println("ℹ Role ADMIN đã tồn tại");
        }

        // ===== 2. Đảm bảo tồn tại role STAFF =====
        Role staffRole = roleRepository.findByCode("STAFF");
        if (staffRole == null) {
            staffRole = new Role();
            staffRole.setCode("STAFF");
            staffRole.setName("Staff");
            roleRepository.save(staffRole);
            System.out.println(" Đã tạo role STAFF");
        } else {
            System.out.println("ℹ Role STAFF đã tồn tại");
        }

        // ===== 3. Tạo nhân viên mặc định nếu chưa tồn tại =====
        if (!nhanVienRepository.existsByEmail(appConfig.getUserEmail())) {
            NhanVien nv = new NhanVien();
            nv.setMa(appConfig.getUserCode());
            nv.setTen(appConfig.getUserName());
            nv.setEmail(appConfig.getUserEmail());
            nv.setSdt(appConfig.getUserPhone());
            nv.setNgaySinh(new Date());
            nv.setChucVu("ADMIN".equalsIgnoreCase(appConfig.getRole()) ? EntityRole.ADMIN : EntityRole.STAFF);
            nv.setRole("ADMIN".equalsIgnoreCase(appConfig.getRole()) ? adminRole : staffRole);
            nv.setMatKhau(passwordEncoder.encode("123456"));

            nhanVienRepository.save(nv);
            System.out.println("Đã tạo nhân viên mặc định: " + nv.getEmail());
        } else {
            System.out.println("ℹ Nhân viên với email " + appConfig.getUserEmail() + " đã tồn tại.");
        }
    }
}
