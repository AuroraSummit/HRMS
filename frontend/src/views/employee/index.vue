<template>
  <div class="employee-page">
    <!-- Search Bar -->
    <el-card shadow="never" class="mb-20">
      <el-form :model="queryParams" inline size="default">
        <el-form-item label="姓名">
          <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="queryParams.deptId" placeholder="选择部门" clearable style="width: 160px">
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="在职" value="active" />
            <el-option label="离职" value="resigned" />
            <el-option label="试用期" value="probation" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Toolbar -->
    <el-card shadow="never" class="mb-20">
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增员工
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>导出Excel
        </el-button>
      </div>
    </el-card>

    <!-- Table -->
    <el-card shadow="never">
      <el-table
        :data="employeeList"
        stripe
        v-loading="loading"
        style="width: 100%"
        @sort-change="() => {}"
      >
        <el-table-column prop="empNo" label="工号" width="100" sortable="custom" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="positionName" label="岗位" width="120" />
        <el-table-column prop="phone" label="手机" width="130" />
        <el-table-column prop="hireDate" label="入职日期" width="110" sortable="custom" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small">
              {{ statusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleView(row)">
              查看
            </el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-popconfirm title="确认删除该员工吗？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="getList"
          @current-change="getList"
        />
      </div>
    </el-card>

    <!-- Employee Form Dialog -->
    <EmployeeForm
      v-model:visible="dialogVisible"
      :form-data="currentFormData"
      :is-edit="isEdit"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, h } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Plus, Download } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getPage, remove, exportExcel, type EmployeeData } from '@/api/employee'
import { getDeptTree, type DeptData } from '@/api/org'
import EmployeeForm from './components/EmployeeForm.vue'

const router = useRouter()
const loading = ref(false)
const employeeList = ref<EmployeeData[]>([])
const total = ref(0)
const deptOptions = ref<DeptData[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentFormData = ref<EmployeeData | null>(null)

const queryParams = reactive({
  page: 1,
  pageSize: 10,
  name: '',
  deptId: undefined as number | undefined,
  status: '',
  orderBy: '',
  orderDir: '',
})

onMounted(() => {
  getList()
  loadDeptOptions()
})

async function getList() {
  loading.value = true
  try {
    // Remove empty params to avoid backend filtering on empty strings
    const params = { ...queryParams }
    for (const key of Object.keys(params)) {
      if (params[key] === '' || params[key] === undefined) {
        delete params[key]
      }
    }
    const res = await getPage(params)
    if (res.data.code === 200) {
      employeeList.value = res.data.data.records || res.data.data
      total.value = res.data.data.total || 0
    }
  } catch {
    // Error handled by interceptor
  } finally {
    loading.value = false
  }
}

async function loadDeptOptions() {
  try {
    const res = await getDeptTree()
    if (res.data.code === 200) {
      deptOptions.value = flattenDeptTree(res.data.data || [])
    }
  } catch {
    // silent
  }
}

function flattenDeptTree(tree: DeptData[]): DeptData[] {
  const result: DeptData[] = []
  function traverse(nodes: DeptData[]) {
    for (const node of nodes) {
      result.push(node)
      if (node.children && node.children.length > 0) {
        traverse(node.children)
      }
    }
  }
  traverse(tree)
  return result
}

function handleSearch() {
  queryParams.page = 1
  getList()
}

function handleReset() {
  queryParams.name = ''
  queryParams.deptId = undefined
  queryParams.status = ''
  queryParams.page = 1
  getList()
}

function handleAdd() {
  isEdit.value = false
  currentFormData.value = null
  dialogVisible.value = true
}

function handleEdit(row: EmployeeData) {
  isEdit.value = true
  currentFormData.value = { ...row }
  dialogVisible.value = true
}

function handleView(row: EmployeeData) {
  router.push(`/employee/detail/${row.id}`)
}

async function handleDelete(row: EmployeeData) {
  try {
    const res = await remove(row.id!)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      getList()
    }
  } catch {
    // handled by interceptor
  }
}

function handleFormSuccess() {
  dialogVisible.value = false
  getList()
}

function handleExport() {
  ElMessage.success('导出功能需后端接口支持')
}

function handleSortChange(sort: { prop: string; order: string }) {
  if (sort.prop && sort.order) {
    queryParams.orderBy = sort.prop
    queryParams.orderDir = sort.order === 'ascending' ? 'asc' : 'desc'
  } else {
    queryParams.orderBy = ''
    queryParams.orderDir = ''
  }
  getList()
}

function statusTag(status: string) {
  const map: Record<string, string> = {
    active: 'success',
    resigned: 'danger',
    probation: 'warning',
  }
  return map[status] || 'info'
}

function statusLabel(status: string) {
  const map: Record<string, string> = {
    active: '在职',
    resigned: '离职',
    probation: '试用期',
  }
  return map[status] || status
}
</script>

<style scoped lang="scss">
.employee-page {
  .toolbar {
    display: flex;
    gap: 8px;
  }

  .pagination-wrapper {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;
    padding-top: 8px;
  }
}
</style>
