import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface ProbationData {
  id?: number
  empId: number
  probationStart?: string
  probationEnd?: string
  actualEnd?: string
  selfEvaluation?: string
  mentorName?: string
  mentorEvaluation?: string
  managerEvaluation?: string
  score?: number
  result?: number
  status: number
  remark?: string
}

export function getProbationPage(params: any) {
  return request.get<ApiResponse<PageResponse<ProbationData>>>('/probation/page', { params })
}

export function createProbation(data: ProbationData) {
  return request.post<ApiResponse>('/probation', data)
}

export function updateProbation(data: ProbationData) {
  return request.put<ApiResponse>('/probation', data)
}

export function evaluateProbation(id: number, data: ProbationData) {
  return request.put<ApiResponse>(`/probation/evaluate/${id}`, data)
}

export function approveProbation(id: number, status: number) {
  return request.put<ApiResponse>(`/probation/approve/${id}`, null, { params: { status } })
}

export function deleteProbation(id: number) {
  return request.delete<ApiResponse>(`/probation/${id}`)
}
