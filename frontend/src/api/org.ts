import request from '@/utils/request'
import type { ApiResponse } from './types'

export interface DeptData {
  id?: number
  parentId: number
  name: string
  leader?: string
  phone?: string
  email?: string
  sort: number
  status: number
  children?: DeptData[]
}

export interface PositionData {
  id?: number
  deptId: number
  name: string
  code: string
  description?: string
  sort: number
  status: number
}

export function getDeptTree() {
  return request.get<ApiResponse<DeptData[]>>('/org/dept/tree')
}

export function createDept(data: DeptData) {
  return request.post<ApiResponse>('/org/dept', data)
}

export function updateDept(data: DeptData) {
  return request.put<ApiResponse>('/org/dept', data)
}

export function getDeptById(id: number) {
  return request.get<ApiResponse<DeptData>>(`/org/dept/${id}`)
}

export function deleteDept(id: number) {
  return request.delete<ApiResponse>(`/org/dept/${id}`)
}

export function getPositionList(params?: any) {
  return request.get<ApiResponse<PositionData[]>>('/org/position/list', { params })
}

export function createPosition(data: PositionData) {
  return request.post<ApiResponse>('/org/position', data)
}

export function updatePosition(data: PositionData) {
  return request.put<ApiResponse>('/org/position', data)
}

export function deletePosition(id: number) {
  return request.delete<ApiResponse>(`/org/position/${id}`)
}
