package com.be.server.entity;

import com.be.server.entity.base.PrimaryEntity;
import com.be.server.infrastructure.constant.EntityPhuongThucThanhToan;
import com.be.server.infrastructure.constant.EntityTrangThaiHoaDon;
import com.be.server.infrastructure.listener.CreateHoaDonEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "hoa_don")
@DynamicUpdate
@EntityListeners(CreateHoaDonEntityListener.class)
public class HoaDon extends PrimaryEntity implements Serializable {

    @Column(name = "ma_hoa_don")
    private String ma;

    @Column(name = "ten_hoa_don")
    private String ten;



    @Column(name = "so_dien_thoai_khach_hang")
    private String sdt;

    @Column(name = "ten_khach_hang")
    private String tenKH;

    @Column(name = "phi_van_chuyen")
    private Double phiVanChuyen;

    @Column(name = "dia_chi_gia_hang")
    private Double diaChi;

    @Column(name = "tong_tien_sau_giam")
    private Double tongTienSauGiam;

    @Column(name = "tong_tien")
    private Double tongTien;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang",referencedColumnName = "id")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "id_voucher",referencedColumnName = "id")
    private PhieuGiamGia voucher;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien",referencedColumnName = "id")
    private NhanVien nhanVien;



    @Column(name = "trang_thai_hoa_don")
    private EntityTrangThaiHoaDon trangThaiHoaDon;




}
