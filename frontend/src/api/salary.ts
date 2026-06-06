import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface SalaryData {
  id?: number
  empId: number
  month: string
  basicSalary: number
  bonus: number
  overtimePay: number
  allowance: number
  housingFund: number
  pension: number
  medical: number
  unemployment: number
  tax: number
  deduction: number
  actualSalary: number
  status: number
  [key: string]: any
}

export function getSalaryPage(params: any) {
  return request.get<ApiResponse<PageResponse<SalaryData>>>('/salary/page', { params })
}

export function createSalary(data: SalaryData) {
  return request.post<ApiResponse>('/salary', data)
}

export function updateSalary(data: SalaryData) {
  return request.put<ApiResponse>('/salary', data)
}

export function deleteSalary(id: number) {
  return request.delete<ApiResponse>(`/salary/${id}`)
}

// Salary Config
export interface SalaryConfigData {
  id?: number
  name: string
  basicSalary: number
  housingFundRate: number
  pensionRate: number
  medicalRate: number
  unemploymentRate: number
  injuryRate: number
  maternityRate: number
  taxExemption: number
  [key: string]: any
}

export function getSalaryConfigPage(params: any) {
  return request.get<ApiResponse<PageResponse<SalaryConfigData>>>('/salary/config/page', { params })
}

export function createSalaryConfig(data: SalaryConfigData) {
  return request.post<ApiResponse>('/salary/config', data)
}

export function updateSalaryConfig(data: SalaryConfigData) {
  return request.put<ApiResponse>('/salary/config', data)
}

export function deleteSalaryConfig(id: number) {
  return request.delete<ApiResponse>(`/salary/config/${id}`)
}
