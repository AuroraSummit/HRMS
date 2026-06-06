import request from '@/utils/request'
import type { ApiResponse, PageResponse } from './types'

// Menu
export interface MenuData {
  id?: number
  parentId: number
  name: string
  path: string
  component: string
  icon: string
  title: string
  sort: number
  permission: string
  type: string
  visible: number
  status: string
  children?: MenuData[]
}

export function getMenuTree() {
  return request.get<ApiResponse<MenuData[]>>('/system/menu/tree')
}

export function createMenu(data: MenuData) {
  return request.post<ApiResponse>('/system/menu', data)
}

export function updateMenu(data: MenuData) {
  return request.put<ApiResponse>('/system/menu', data)
}

export function deleteMenu(id: number) {
  return request.delete<ApiResponse>(`/system/menu/${id}`)
}

// Dict
export interface DictTypeData {
  id?: number
  name: string
  code: string
  description: string
  status: string | number
}

export interface DictDataItem {
  id?: number
  dictTypeId: number
  label: string
  value: string
  sort: number
  status: string | number
  remark: string
}

export function getDictTypeList(params?: any) {
  return request.get<ApiResponse<DictTypeData[]>>('/system/dict/type/list', { params })
}

export function createDictType(data: DictTypeData) {
  return request.post<ApiResponse>('/system/dict/type', data)
}

export function updateDictType(data: DictTypeData) {
  return request.put<ApiResponse>('/system/dict/type', data)
}

export function deleteDictType(id: number) {
  return request.delete<ApiResponse>(`/system/dict/type/${id}`)
}

export function getDictDataList(params: { dictTypeId: number; [key: string]: any }) {
  return request.get<ApiResponse<DictDataItem[]>>('/system/dict/data/list', { params })
}

export function createDictData(data: DictDataItem) {
  return request.post<ApiResponse>('/system/dict/data', data)
}

export function updateDictData(data: DictDataItem) {
  return request.put<ApiResponse>('/system/dict/data', data)
}

export function deleteDictData(id: number) {
  return request.delete<ApiResponse>(`/system/dict/data/${id}`)
}

// Log
export function getLogPage(params: any) {
  return request.get<ApiResponse<{ records: any[]; total: number }>>('/system/log/page', { params })
}

export function deleteLog(id: number) {
  return request.delete<ApiResponse>(`/system/log/${id}`)
}

export function cleanLog() {
  return request.delete<ApiResponse>('/system/log/clean')
}

// Role
export interface RoleData {
  id?: number
  name: string
  code: string
  description: string
  sort: number
  status: string
}

export function getRolePage(params: any) {
  return request.get<ApiResponse<PageResponse<RoleData>>>('/system/role/page', { params })
}

export function getRoleDetail(id: number) {
  return request.get<ApiResponse<RoleData>>(`/system/role/${id}`)
}

export function createRole(data: RoleData) {
  return request.post<ApiResponse>('/system/role', data)
}

export function updateRole(data: RoleData) {
  return request.put<ApiResponse>('/system/role', data)
}

export function deleteRole(id: number) {
  return request.delete<ApiResponse>(`/system/role/${id}`)
}

export function assignRoleMenus(roleId: number, menuIds: number[]) {
  return request.put<ApiResponse>(`/system/role/${roleId}/menus`, { menuIds })
}
