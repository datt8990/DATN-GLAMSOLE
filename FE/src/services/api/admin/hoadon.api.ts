import type { AxiosResponse } from 'axios'
import request from '@/services/request'
import { PREFIX_API_HOA_DON_ADMIN, PREFIX_API_KHACH_HANG_ADMIN} from '@/constants/url'
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

export type HoaDonResponse = ResponseList & {
  ma: string,
  ten: string,
  status: string,
}

export const GetHoaDons = async (params: ParamsGetHoaDon) => {
  const res = (await request({
    url: `${PREFIX_API_HOA_DON_ADMIN}`,
    method: 'GET',
    params: params
  })) as AxiosResponse<DefaultResponse<PaginationResponse<Array<HoaDonResponse>>>>

  return res.data
}

export const getHoaDonChiTiets = async (maHoaDon: string) => {
  const res = (await request({
    url: `${PREFIX_API_HOA_DON_ADMIN}/all/${maHoaDon}`,
    method: 'GET'
  })) as AxiosResponse<DefaultResponse<HoaDonResponse>>

  return res.data
}
