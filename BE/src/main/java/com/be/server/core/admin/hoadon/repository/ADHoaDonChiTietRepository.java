package com.be.server.core.admin.hoadon.repository;

import com.be.server.core.admin.hoadon.model.response.ADHoaDonChiTietResponseDetail;
import com.be.server.repository.HoaDonChiTietRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ADHoaDonChiTietRepository extends HoaDonChiTietRepository {

    @Query(value = """
            SELECT
                                                                        hd.ma_hoa_don AS maHoaDon,
                                                                        hd.ten_hoa_don AS tenHoaDon,
                                                                        hdct.ma_hoa_don_chi_tiet AS maHoaDonChiTiet,
                                                                        sp.ten_san_pham AS tenSanPham,
                                                                        spct.anh_san_pham AS anhSanPham,
                                                                        thuong_hieu.ten_thuong_hieu AS thuongHieu,
                                                                        xuat_su.ten_xuat_su AS xuatSu,
                                                                        mau_sac.ten_mau_sac AS mauSac,
                                                                        kich_co.ten_kich_co AS size,
                                                                        hdct.so_luong AS soLuong,
                                                                        spct.gia_ban AS giaBan,
                                                                        (hdct.so_luong * hdct.tong_tien) AS thanhTien,
                                                                        kh.ten_khach_hang AS tenKhachHang,
                                                                        kh.so_dien_thoai AS sdtKH,
                                                                        kh.email AS email,
                                                                        kh.dia_chi AS diaChi,
                                                                        hd.loai_hoa_don AS loaiHoaDon,
                                                                        hd.trang_thai_hoa_don AS trangThaiHoaDon,
                                                                        hd.created_date AS ngayTao,
                                                                        hd.phi_van_chuyen AS phiVanChuyen,
                                                                        pgg.ma_phieu_giam_gia AS maVoucher,
                                                                        hd.tong_tien_sau_giam AS tongTienSauGiam
                                                                    FROM db_datn.hoa_don_chi_tiet hdct
                                                                    JOIN db_datn.hoa_don hd ON hdct.id_hoa_don = hd.id
                                                                    JOIN db_datn.phieu_giam_gia pgg ON hd.id_voucher = pgg.id
                                                                    JOIN db_datn.khach_hang kh ON hd.id_khach_hang = kh.id
                                                                    JOIN db_datn.san_pham_chi_tiet spct ON hdct.id_spct = spct.id
                                                                    JOIN db_datn.san_pham sp ON spct.id_san_pham = sp.id
                                                                    JOIN db_datn.thuong_hieu ON sp.id_thuong_hieu = thuong_hieu.id
                                                                    JOIN db_datn.xuat_su ON sp.id_xuat_su = xuat_su.id
                                                                    JOIN db_datn.kich_co ON spct.id_kich_co = kich_co.id
                                                                    JOIN db_datn.mau_sac ON spct.id_mau_sac = mau_sac.id
                                                                    WHERE db_datn.hd.ma_hoa_don = :maHoaDon
            """, nativeQuery = true)
    List<ADHoaDonChiTietResponseDetail> getAllHoaDonChiTietResponse(@Param("maHoaDon") String maHoaDon);
}
