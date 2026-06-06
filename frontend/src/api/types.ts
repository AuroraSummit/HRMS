export interface ApiResponse<T = any> {
  code: number
  data: T
  message: string
}

export interface PageResponse<T = any> {
  records: T[]
  total: number
  page: number
  pageSize: number
}
