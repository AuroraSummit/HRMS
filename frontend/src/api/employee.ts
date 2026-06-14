import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface EmployeeQuery {
  page: number
  pageSize: number
  name?: string
  deptId?: number
  status?: string
  [key: string]: any
}

export interface EmployeeData {
  id?: number
  empNo: string
  name: string
  gender: number
  deptId: number
  positionId: number
  phone: string
  email: string
  hireDate: string
  status: string
  idCard?: string
  emergencyContact?: string
  emergencyPhone?: string
  birthday?: string
  employmentType?: number
  source?: number
  deptName?: string
  positionName?: string
  educationList?: EducationData[]
  workExperienceList?: WorkExperienceData[]
  [key: string]: any
}

export interface EducationData {
  id?: number
  empId: number
  school: string
  major: string
  degree: number
  startDate: string
  endDate: string
}

export interface WorkExperienceData {
  id?: number
  empId: number
  company: string
  position: string
  startDate: string
  endDate: string
  description: string
}

export function getPage(params: EmployeeQuery) {
  return request.get<ApiResponse<PageResponse<EmployeeData>>>('/employee/page', { params })
}

export function getDetail(id: number) {
  return request.get<ApiResponse<EmployeeData>>(`/employee/${id}`)
}

export function create(data: EmployeeData) {
  return request.post<ApiResponse>('/employee', data)
}

export function update(data: EmployeeData) {
  return request.put<ApiResponse>('/employee', data)
}

export function remove(id: number) {
  return request.delete<ApiResponse>(`/employee/${id}`)
}

export function exportExcel(params: EmployeeQuery) {
  return request.get('/employee/export', { params, responseType: 'blob' })
}
