import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

export interface NoticeData {
  id?: number
  title: string
  content?: string
  noticeType: number
  priority: number
  publisher?: string
  publishDate?: string
  expireDate?: string
  targetRole: string
  status: number
  viewCount?: number
  createTime?: string
}

export function getNoticePage(params: any) {
  return request.get<ApiResponse<PageResponse<NoticeData>>>('/notice/page', { params })
}

export function getNoticeList() {
  return request.get<ApiResponse<NoticeData[]>>('/notice/list')
}

export function getAdminNoticeList() {
  return request.get<ApiResponse<NoticeData[]>>('/notice/admin/list')
}

export function createNotice(data: NoticeData) {
  return request.post<ApiResponse>('/notice', data)
}

export function updateNotice(data: NoticeData) {
  return request.put<ApiResponse>('/notice', data)
}

export function publishNotice(id: number, status: number) {
  return request.put<ApiResponse>(`/notice/publish/${id}`, null, { params: { status } })
}

export function getNoticeDetail(id: number) {
  return request.get<ApiResponse<NoticeData>>(`/notice/${id}`)
}

export function deleteNotice(id: number) {
  return request.delete<ApiResponse>(`/notice/${id}`)
}
