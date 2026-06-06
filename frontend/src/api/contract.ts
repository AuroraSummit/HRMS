import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface ContractData {
  id?: number
  empId: number
  contractType: number
  startDate: string
  endDate: string
  signDate?: string
  fileUrl?: string
  remark?: string
  status: number
}

export function getContractPage(params: any) {
  return request.get<ApiResponse<PageResponse<ContractData>>>('/contract/page', { params })
}

export function createContract(data: ContractData) {
  return request.post<ApiResponse>('/contract', data)
}

export function updateContract(data: ContractData) {
  return request.put<ApiResponse>('/contract', data)
}

export function deleteContract(id: number) {
  return request.delete<ApiResponse>(`/contract/${id}`)
}

export function renewContract(id: number, data: any) {
  return request.put<ApiResponse>(`/contract/renew/${id}`, data)
}
