import type { AxiosResponse } from 'axios'
import request from '@/services/request'
import { PREFIX_API_SANPHAM_PERMITALL } from '@/constants/url'
import type { DefaultResponse, PaginationParams, PaginationResponse } from '@/types/api.common'

export interface MauSacDTO {
  ten: string
  maMau: string
}

export interface KichCoDTO {
  ten: string
  soLuong: number
}

export interface DotGiamGiaInfo {
  tenDotGiamGia: string
  phanTramGiam: number
  giaTruoc: number
  giaSau: number
  ngayBatDau: number
  ngayKetThuc: number
}

export interface SanPhamMoiResponse {
  id: string
  tenSanPham: string
  hinhAnhDaiDien: string
  thuongHieu: string
  danhMuc: string
  chatLieu: string
  xuatXu: string | null
  moTa: string
  giaBan: number
  ngayTao: number
  giaSauGiam: number | null
  kichCo: KichCoDTO[]
  mauSac: MauSacDTO[]
  dotGiamGia: DotGiamGiaInfo | null
  dsAnh: string[]
}

export interface ParamsGetSanPhamMoi extends PaginationParams {}

export const GetSanPhamMoiTrangChu = async (
  params: ParamsGetSanPhamMoi
) => {
  const res = (await request({
    url: `${PREFIX_API_SANPHAM_PERMITALL}/get-all/san-pham-moi`,
    method: 'GET',
    params
  })) as AxiosResponse<DefaultResponse<PaginationResponse<SanPhamMoiResponse[]>>>
  return res.data
}

export const GetSanPhamGiamGiaTrangChu = async (
  params: ParamsGetSanPhamMoi
) => {
  const res = (await request({
    url: `${PREFIX_API_SANPHAM_PERMITALL}/get-all/san-pham-giam-gia`,
    method: 'GET',
    params
  })) as AxiosResponse<DefaultResponse<PaginationResponse<SanPhamMoiResponse[]>>>
  return res.data
}
