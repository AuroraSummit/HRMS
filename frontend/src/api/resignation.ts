import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface ResignationData {
  id?: number
  empId: number
  applyDate: string
  resignDate?: string
  actualResignDate?: string
  resignType?: number
  reason?: string
  handoverTo?: string
  handoverStatus?: number
  assetReturn?: number
  settlement?: number
  status: number
  approveComment?: string
}

export function getResignationPage(params: any) {
  return request.get<ApiResponse<PageResponse<ResignationData>>>('/resignation/page', { params })
}

export function createResignation(data: ResignationData) {
  return request.post<ApiResponse>('/resignation', data)
}

export function updateResignation(data: ResignationData) {
  return request.put<ApiResponse>('/resignation', data)
}

export function approveResignation(id: number, status: number, comment?: string) {
  return request.put<ApiResponse>(`/resignation/approve/${id}`, null, { params: { status, comment } })
}

export function updateHandover(id: number, handoverTo: string, handoverStatus: number) {
  return request.put<ApiResponse>(`/resignation/handover/${id}`, null, { params: { handoverTo, handoverStatus } })
}

export function deleteResignation(id: number) {
  return request.delete<ApiResponse>(`/resignation/${id}`)
}
