<template>
  <div class="demand-page">
    <el-card shadow="never" class="mb-20">
      <template #header>
        <div class="card-header">
          <span>招聘需求</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增需求
          </el-button>
        </div>
      </template>

      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="title" label="需求标题" min-width="160" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="positionName" label="岗位" width="120" />
        <el-table-column prop="headcount" label="招聘人数" width="90" />
        <el-table-column prop="description" label="需求描述" min-width="160" show-overflow-tooltip />
        <el-table-column prop="requirement" label="任职要求" min-width="160" show-overflow-tooltip />
        <el-table-column label="薪资范围" width="140">
          <template #default="{ row }">
            {{ row.salaryMin }}~{{ row.salaryMax }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @current-change="loadData"
          @size-change="loadData"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑需求' : '新增需求'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="需求标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门" prop="deptId">
              <el-tree-select v-model="form.deptId" :data="deptTree" :props="deptProps" placeholder="选择" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位" prop="positionId">
              <el-select v-model="form.positionId" placeholder="选择" clearable style="width:100%">
                <el-option v-for="p in positions" :key="p.id" :label="p.name" :value="p.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="招聘人数" prop="headcount">
              <el-input-number v-model="form.headcount" :min="1" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最低薪资" prop="salaryMin">
              <el-input-number v-model="form.salaryMin" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最高薪资" prop="salaryMax">
              <el-input-number v-model="form.salaryMax" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="选择" style="width:100%">
                <el-option :value="0" label="待审批" />
                <el-option :value="1" label="招聘中" />
                <el-option :value="2" label="已关闭" />
                <el-option :value="3" label="已完成" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="需求描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="任职要求" prop="requirement">
          <el-input v-model="form.requirement" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getDemandPage, createDemand, updateDemand, deleteDemand, type DemandData } from '@/api/recruitment'
import { getDeptTree, getPositionList, type DeptData, type PositionData } from '@/api/org'

const list = ref<DemandData[]>([])
const total = ref(0)
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const deptTree = ref<DeptData[]>([])
const positions = ref<PositionData[]>([])

const queryParams = reactive({ page: 1, pageSize: 10 })
const deptProps = { label: 'name', value: 'id', children: 'children' }

const defaultForm: DemandData = {
  title: '', deptId: 0, positionId: 0, headcount: 1,
  description: '', requirement: '', salaryMin: 0, salaryMax: 0, status: 0,
}
const form = reactive<DemandData>({ ...defaultForm })
const rules: FormRules = {
  title: [{ required: true, message: '请输入需求标题', trigger: 'blur' }],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  positionId: [{ required: true, message: '请选择岗位', trigger: 'change' }],
}

onMounted(() => { loadData(); loadDeptTree(); loadPositions() })

async function loadData() {
  loading.value = true
  try {
    const res = await getDemandPage(queryParams)
    if (res.data.code === 200) {
      list.value = res.data.data.records || res.data.data
      total.value = res.data.data.total || 0
    }
  } catch { } finally { loading.value = false }
}

async function loadDeptTree() {
  try { const res = await getDeptTree(); if (res.data.code === 200) deptTree.value = res.data.data || [] } catch { }
}
async function loadPositions() {
  try { const res = await getPositionList(); if (res.data.code === 200) positions.value = res.data.data || [] } catch { }
}

function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: DemandData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: DemandData) {
  try { const res = await deleteDemand(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { }
}
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const res = isEdit.value ? await updateDemand(form) : await createDemand(form)
    if (res.data.code === 200) { ElMessage.success(isEdit.value ? '编辑成功' : '新增成功'); dialogVisible.value = false; loadData() }
  } catch { } finally { submitLoading.value = false }
}

function statusTag(v: number) {
  const map: Record<number, string> = { 0: 'warning', 1: 'success', 2: 'info', 3: 'primary' }
  return map[v] || 'info'
}
function statusLabel(v: number) {
  const map: Record<number, string> = { 0: '待审批', 1: '招聘中', 2: '已关闭', 3: '已完成' }
  return map[v] || '未知'
}
</script>

<style scoped lang="scss">
.demand-page {
  .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; }
  .pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 16px; }
}
</style>
