import type { AxiosResponse } from 'axios'
import request from '@/services/request'
import { PREFIX_API_BAN_HANG_ADMIN } from '@/constants/url'
import type {
  PaginationParams,
  DefaultResponse,
  ResponseList,
  PaginationResponse
} from '@/types/api.common'

export interface ParamsGetHoaDon extends PaginationParams {
  q?: string | ''
  status?: number | null
}

export interface ParamsXoaSP {
  idHD?: string
  idSP?: string
}

export interface ParamsPTTT {
  idHD?: string
  pttt: string
}

export interface ParamsThanhCong {
  idHD?: string
  tongTien: string
}


export type KhachHangResponse = ResponseList & {
  id: string
  ten: string,
  sdt: string
}

export type thanhToanResponse = ResponseList & {
  id: string
  ten: string,
  sdt: string
}


export type PhuongThucThanhToanResponse = ResponseList & {
  id: string
  soTien: number,
  phuongThucThanhToan: string
}

export type tongTienResponse = ResponseList & {
  tongTien: number,

}

export interface ADThemSanPhamRequest {
  id?: string,
  code: string,
  name: string,
}

export type BanHangResponse = ResponseList & {
  ma: string,
  ten: string,
  status: string,
}

export type themKHResponse = {
  idHD: string
  idKH: string
}

export type XoaSPResponse = {
  idHD: string
  idSP: string
}


export const GetHoaDons = async (params: ParamsGetHoaDon) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/list-hoa-don`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<BanHangResponse>>>>

  return res.data
}

export const getCreateHoaDon = async (maHoaDon: string) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/create-hoa-don`,
    method: 'POST'
  })) as AxiosResponse<DefaultResponse<BanHangResponse>>
  return res.data
}


export const themSanPham = async (data: ADThemSanPhamRequest) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/them-san-pham`,
    method: 'POST',
    data: data
  })) as AxiosResponse<DefaultResponse<BanHangResponse>>

  return res.data
}

export const xoaSP = async (data: ParamsXoaSP) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/xoa-san-pham `,
    method: 'POST',
    data: data
  })) as AxiosResponse<DefaultResponse<XoaSPResponse>>
}

export const GetGioHang = async (id: string) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/list-gio-hang/${id}`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<BanHangResponse>>>>

  return res.data
}

export const themSL = async (data: ParamsXoaSP) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/them-so-luong `,
    method: 'POST',
    data: data
  })) as AxiosResponse<DefaultResponse<XoaSPResponse>>
}

export const xoaSL = async (data: ParamsXoaSP) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/xoa-so-luong `,
    method: 'POST',
    data: data
  })) as AxiosResponse<DefaultResponse<XoaSPResponse>>
}


export const GetKhachHang = async () => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/list-khach-hang`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<KhachHangResponse>>>>

  return res.data
}

export const GeOneKhachHang = async (id: string) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/khach-hang/${id}`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<KhachHangResponse>>>>

  return res.data
}

export const getThanhToan = async (id: string) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/thanh-toan/${id}`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<thanhToanResponse>>>>

  return res.data
}

export const themKhachHang = async (data: themKHResponse) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/them-khach-hang`,
    method: 'POST',
    data: data
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<KhachHangResponse>>>>

  return res.data
}


export const getPhuongThucThanhToan = async (id: string) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/phuong-thuc-thanh-toan/${id}`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<thanhToanResponse>>>>

  return res.data
}

export const themPTTT = async (data: ParamsPTTT) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/cap-nhat-phuong-thuc-thanh-toan`,
    method: 'POST',
    data: data
  })) as AxiosResponse<DefaultResponse<XoaSPResponse>>
}

export const thanhToanThanhCong = async (data :ParamsThanhCong) => {
  const res = (await request({
    url: `${PREFIX_API_BAN_HANG_ADMIN}/thanh-toan-thanh-cong`,
    method: 'POST',
    data: data
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<KhachHangResponse>>>>

  return res.data
}