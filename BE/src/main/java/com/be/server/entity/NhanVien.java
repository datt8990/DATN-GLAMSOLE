package com.be.server.entity;

import com.be.server.entity.base.PrimaryEntity;
import com.be.server.infrastructure.constant.EntityRole;
import com.be.server.infrastructure.listener.CreateMauSacEntityListener;
import com.be.server.infrastructure.listener.CreateNhanVIenEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "nhan_vien")
@DynamicUpdate
@EntityListeners(CreateNhanVIenEntityListener.class)
public class NhanVien extends PrimaryEntity implements Serializable {

    @Column(name = "ma_nhan_vien")
    private String ma;

    @Column(name = "ten_nhan_vien")
    private String ten;

    @Column(name = "so_dien_thoai")
    private String sdt;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "email")
    private String email;

    @Column(name = "gioi_timh")
    private Boolean gioiTimh;

    @Column(name = "chuc_vu")
    private EntityRole chucVu;

    @Column(name = "luong")
    private Double luong;

    @Column(name = "mat_khau")
    private String matKhau;
}
