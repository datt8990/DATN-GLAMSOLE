package com.be.server.core.auth.controller;

import com.be.server.core.auth.dto.request.LoginRequest;
import com.be.server.core.auth.dto.request.RegisterRequest;
import com.be.server.core.auth.service.AuthService;
import com.be.server.core.common.base.AuthTokens;
import com.be.server.core.common.base.ResponseObject;
import com.be.server.infrastructure.constant.MappingConstants;
import com.be.server.infrastructure.security.service.CustomUserDetailsService;
import com.be.server.infrastructure.security.service.TokenProvider;
import com.be.server.infrastructure.security.user.UserPrincipal;
import com.be.server.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(MappingConstants.API_AUTH_PREFIX)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("tài khoản"+loginRequest.getEmail()+"/"+loginRequest.getPassword());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String accessToken = tokenProvider.createRefreshTokenForKhachHang(authentication);
            String refreshToken = tokenProvider.createRefreshTokenForKhachHang(authentication);
            return Helper.createResponseEntity(
             new ResponseObject<>(new AuthTokens(accessToken, refreshToken), HttpStatus.OK, "Lấy token thành công")

//            new ResponseObject<>(token, HttpStatus.OK, "Lấy token thành công")
            );

        } catch (BadCredentialsException ex) {
            System.out.println(ex.getMessage());
            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không đúng")
            );

        } catch (DisabledException ex) {
            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Tài khoản đã bị vô hiệu hóa")
            );

        } catch (LockedException ex) {
            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Tài khoản đã bị khóa")
            );

        } catch (CredentialsExpiredException ex) {
            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Thông tin xác thực đã hết hạn")
            );

        } catch (Exception ex) {
            // Ghi log đầy đủ lỗi (stack trace)
            ex.printStackTrace();
//            log.error("Đăng nhập thất bại: ", ex);

            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống: " + ex.getClass().getSimpleName() + " - " + ex.getMessage())
            );
        }
    }

    @PostMapping("/login-admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("chua implement chuc nang này");
//            System.out.println("tài khoản"+loginRequest.getEmail()+"/"+loginRequest.getPassword());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String accessToken = tokenProvider.createTokenForAdmin(authentication);
            String refreshToken = tokenProvider.createTokenForAdmin(authentication);
            return Helper.createResponseEntity(
                    new ResponseObject<>(new AuthTokens(accessToken, refreshToken), HttpStatus.OK, "Lấy token thành công")

//            new ResponseObject<>(token, HttpStatus.OK, "Lấy token thành công")
            );

        } catch (BadCredentialsException ex) {
            System.out.println(ex.getMessage());
            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không đúng")
            );

        } catch (DisabledException ex) {
            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Tài khoản đã bị vô hiệu hóa")
            );

        } catch (LockedException ex) {
            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Tài khoản đã bị khóa")
            );

        } catch (CredentialsExpiredException ex) {
            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.UNAUTHORIZED, "Thông tin xác thực đã hết hạn")
            );

        } catch (Exception ex) {
            // Ghi log đầy đủ lỗi (stack trace)
            ex.printStackTrace();
//            log.error("Đăng nhập thất bại: ", ex);

            return Helper.createResponseEntity(
                    new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống: " + ex.getClass().getSimpleName() + " - " + ex.getMessage())
            );
        }
    }


    @PutMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return Helper.createResponseEntity(authService.register(request));
    }

}
