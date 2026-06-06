import request from '@/utils/request'
import type { ApiResponse } from './types'

export interface UserInfo {
  id: number
  username: string
  realName: string
  email: string
  phone: string
  avatar: string
  roles: string[]
  permissions: string[]
}

export function login(data: { username: string; password: string }) {
  return request.post<ApiResponse>('/auth/login', data)
}

export function register(data: any) {
  return request.post<ApiResponse>('/auth/register', data)
}

export function getUserInfo() {
  return request.get<ApiResponse<UserInfo>>('/auth/userinfo')
}

export function refreshToken(data: { refreshToken: string }) {
  return request.post<ApiResponse>('/auth/refresh', data)
}
