import { ROUTES_CONSTANTS } from '@/constants/path'
import {
  ACCESS_TOKEN_STORAGE_KEY,
  REFRESH_TOKEN_STORAGE_KEY,
  USER_INFO_STORAGE_KEY
} from '@/constants/storageKey'
import { API_URL, PREFIX_API_REFRESH } from '@/constants/url'
import type { DefaultResponse } from '@/utils/types/api.common'
import { localStorageAction } from '@/utils/storage'
import axios from 'axios'
import type { AxiosResponse } from 'axios';


const request = axios.create({
  baseURL: `${API_URL}/api/`
})

request.interceptors.request.use((config) => {
  const accessToken = localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY)
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`
  }
  return config
})


export default request
