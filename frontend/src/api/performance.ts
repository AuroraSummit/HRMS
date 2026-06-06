import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface PerformanceData {
  id?: number
  empId: number
  planId: number
  selfScore: number
  selfComment: string
  managerScore: number
  managerComment: string
  finalScore: number
  result: number
}

export function getPerformancePage(params: any) {
  return request.get<ApiResponse<PageResponse<PerformanceData>>>('/performance/page', { params })
}

export function createPerformance(data: PerformanceData) {
  return request.post<ApiResponse>('/performance', data)
}

export function updatePerformance(data: PerformanceData) {
  return request.put<ApiResponse>('/performance', data)
}

export function deletePerformance(id: number) {
  return request.delete<ApiResponse>(`/performance/${id}`)
}

// Performance Template
export interface TemplateData {
  id?: number
  name: string
  description: string
  status: number
}

export function getTemplatePage(params: any) {
  return request.get<ApiResponse<PageResponse<TemplateData>>>('/performance/template/page', { params })
}

export function createTemplate(data: TemplateData) {
  return request.post<ApiResponse>('/performance/template', data)
}

export function updateTemplate(data: TemplateData) {
  return request.put<ApiResponse>('/performance/template', data)
}

export function deleteTemplate(id: number) {
  return request.delete<ApiResponse>(`/performance/template/${id}`)
}
