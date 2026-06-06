import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

// Recruitment Demand
export interface DemandData {
  id?: number
  title: string
  deptId: number
  positionId: number
  headcount: number
  description: string
  requirement: string
  salaryMin: number
  salaryMax: number
  status: number
  deptName?: string
  positionName?: string
}

export function getDemandPage(params: any) {
  return request.get<ApiResponse<PageResponse<DemandData>>>('/recruitment/demand/page', { params })
}

export function createDemand(data: DemandData) {
  return request.post<ApiResponse>('/recruitment/demand', data)
}

export function updateDemand(data: DemandData) {
  return request.put<ApiResponse>('/recruitment/demand', data)
}

export function deleteDemand(id: number) {
  return request.delete<ApiResponse>(`/recruitment/demand/${id}`)
}

// Interview
export interface InterviewData {
  id?: number
  candidateName: string
  phone: string
  email: string
  position: string
  demandId: number
  interviewDate: string
  interviewer: string
  round: number
  result: number
  comment: string
}

export function getInterviewPage(params: any) {
  return request.get<ApiResponse<PageResponse<InterviewData>>>('/recruitment/interview/page', { params })
}

export function createInterview(data: InterviewData) {
  return request.post<ApiResponse>('/recruitment/interview', data)
}

export function updateInterview(data: InterviewData) {
  return request.put<ApiResponse>('/recruitment/interview', data)
}

export function deleteInterview(id: number) {
  return request.delete<ApiResponse>(`/recruitment/interview/${id}`)
}

// Offer
export interface OfferData {
  id?: number
  candidateName: string
  demandId: number
  offerSalary: number
  offerDate: string
  expiryDate: string
  status: number
}

export function getOfferPage(params: any) {
  return request.get<ApiResponse<PageResponse<OfferData>>>('/recruitment/offer/page', { params })
}

export function createOffer(data: OfferData) {
  return request.post<ApiResponse>('/recruitment/offer', data)
}

export function updateOffer(data: OfferData) {
  return request.put<ApiResponse>('/recruitment/offer', data)
}

export function deleteOffer(id: number) {
  return request.delete<ApiResponse>(`/recruitment/offer/${id}`)
}
