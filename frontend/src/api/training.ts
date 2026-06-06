import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface TrainingData {
  id?: number
  name: string
  instructor: string
  startDate: string
  endDate: string
  location: string
  description: string
  maxAttendees: number
  status: number
}

export function getTrainingPage(params: any) {
  return request.get<ApiResponse<PageResponse<TrainingData>>>('/training/page', { params })
}

export function createTraining(data: TrainingData) {
  return request.post<ApiResponse>('/training', data)
}

export function updateTraining(data: TrainingData) {
  return request.put<ApiResponse>('/training', data)
}

export function deleteTraining(id: number) {
  return request.delete<ApiResponse>(`/training/${id}`)
}
