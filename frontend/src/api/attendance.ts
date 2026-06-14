import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface AttendanceData {
  id?: number
  empId: number
  attendanceDate: string
  clockInTime: string
  clockOutTime: string
  status: number
  remark: string
}

export function getAttendancePage(params: any) {
  return request.get<ApiResponse<PageResponse<AttendanceData>>>('/attendance/page', { params })
}

export function createAttendance(data: AttendanceData) {
  return request.post<ApiResponse>('/attendance', data)
}

export function updateAttendance(data: AttendanceData) {
  return request.put<ApiResponse>('/attendance', data)
}

export function deleteAttendance(id: number) {
  return request.delete<ApiResponse>(`/attendance/${id}`)
}

// Leave
export interface LeaveData {
  id?: number
  empId: number
  leaveType: number
  startDate: string
  endDate: string
  duration: number
  reason: string
  status: number
}

export function getLeavePage(params: any) {
  return request.get<ApiResponse<PageResponse<LeaveData>>>('/attendance/leave/page', { params })
}

export function createLeave(data: LeaveData) {
  return request.post<ApiResponse>('/attendance/leave', data)
}

export function updateLeave(data: LeaveData) {
  return request.put<ApiResponse>('/attendance/leave', data)
}

export function deleteLeave(id: number) {
  return request.delete<ApiResponse>(`/attendance/leave/${id}`)
}

export function approveLeave(id: number, status: number) {
  return request.put<ApiResponse>(`/attendance/leave/approve/${id}`, { status })
}
