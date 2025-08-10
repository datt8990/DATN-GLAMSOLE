import type { AxiosResponse } from 'axios'
import request from '@/services/request'
import { PREFIX_API_PROFILE_ORDER_HISTORY} from '@/constants/url'
import type {
  PaginationParams,
  DefaultResponse,
  ResponseList,
  PaginationResponse
} from '@/types/api.common'

export type DonMuaResponse = ResponseList & {
  q: string,
  ten: string,
  status: string,
}

export interface ParamsGetHoaDonCT extends PaginationParams {
  maHoaDon?: string | ''
}

export interface DonMuaRequest {
  q?: string
}

export const getDonMua = async (params: DonMuaRequest) => {
  const res = (await request({
    url: `${PREFIX_API_PROFILE_ORDER_HISTORY}`,
    method: 'GET',
    params: params
  })) as AxiosResponse<DefaultResponse<DonMuaResponse>>

  return res.data
}

export const getHoaDonChiTiets = async (params: ParamsGetHoaDonCT) => {
  const res = (await request({
    url: `${PREFIX_API_PROFILE_ORDER_HISTORY}/all`,
    method: 'GET',
    params: params
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<DonMuaResponse>>>>

  return res.data
}

export const GetLSTTHD = async (id: string) => {
  const res = (await request({
    url: `${PREFIX_API_PROFILE_ORDER_HISTORY}/${id}`,
    method: 'GET',
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<DonMuaResponse>>>>

  return res.data
}