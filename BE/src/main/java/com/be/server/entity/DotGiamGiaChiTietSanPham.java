package com.be.server.entity;

import com.be.server.entity.base.PrimaryEntity;
import com.be.server.infrastructure.listener.CreateDotGiamGIaEntityListener;
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
@Table(name = "dot_giam_gia_chi_tiet_san_pham")
@DynamicUpdate
@EntityListeners(CreateDotGiamGIaEntityListener.class)
public class DotGiamGiaChiTietSanPham extends PrimaryEntity implements Serializable {

    @Column(name = "ma_dot_giam_gia_chi_tiet_san_pham")
    private String ma;

    @Column(name = "gia_truoc_khi_giam")
    private Double giaTruoc;

    @Column(name = "gia_sau_khi_giam")
    private Double giaSau;

    @ManyToOne
    @JoinColumn(name = "id_chi_tiet_san_pham", referencedColumnName = "id")
    private SanPhamChiTiet sanPhamChiTiet;

    @ManyToOne
    @JoinColumn(name = "id_dot_giam_gia", referencedColumnName = "id")
    private DotGiamGia dotGiamGia;
}
