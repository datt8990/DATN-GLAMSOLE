package com.be.server.core.admin.banhang.model.request;

import com.be.server.core.common.base.PageableRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListSanPhamRequest extends PageableRequest {
    private String q; // Từ khóa tìm kiếm
    private String status; // Trạng thái sản phẩm
    private Long idMauSac; // ID màu sắc
    private Long idKichThuoc; // ID kích thước
    private Long idDanhMuc; // ID danh mục
    private Long idChatLieu; // ID chất liệu
    private Long idThuongHieu; // ID thương hiệu
    private Long idLoaiDe; // ID loại đế
}