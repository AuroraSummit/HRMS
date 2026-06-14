import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface TransferData {
  id?: number
  empId: number
  transferType: number
  fromDeptId?: number
  toDeptId?: number
  fromPositionId?: number
  toPositionId?: number
  fromSalary?: number
  toSalary?: number
  transferDate: string
  reason?: string
  remark?: string
  status: number
}

export function getTransferPage(params: any) {
  return request.get<ApiResponse<PageResponse<TransferData>>>('/transfer/page', { params })
}

export function createTransfer(data: TransferData) {
  return request.post<ApiResponse>('/transfer', data)
}

export function updateTransfer(data: TransferData) {
  return request.put<ApiResponse>('/transfer', data)
}

export function approveTransfer(id: number, status: number) {
  return request.put<ApiResponse>(`/transfer/approve/${id}`, null, { params: { status } })
}

export function deleteTransfer(id: number) {
  return request.delete<ApiResponse>(`/transfer/${id}`)
}
