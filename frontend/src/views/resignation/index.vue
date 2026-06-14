<template>
  <div class="resignation-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>离职管理</span>
          <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增离职申请</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="empId" label="员工ID" width="80" />
        <el-table-column prop="applyDate" label="申请日期" width="110" />
        <el-table-column prop="resignDate" label="预计离职日" width="110" />
        <el-table-column label="离职类型" width="100">
          <template #default="{ row }">{{ resignTypeLabel(row.resignType) }}</template>
        </el-table-column>
        <el-table-column label="交接状态" width="90">
          <template #default="{ row }">{{ handoverLabel(row.handoverStatus) }}</template>
        </el-table-column>
        <el-table-column label="资产归还" width="90">
          <template #default="{ row }"><el-tag :type="row.assetReturn ? 'success' : 'danger'" size="small">{{ row.assetReturn ? '已归还' : '未归还' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }"><el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="reason" label="离职原因" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status===0" type="success" link size="small" @click="approve(row.id,1)">通过</el-button>
            <el-button v-if="row.status===0" type="warning" link size="small" @click="approve(row.id,2)">驳回</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="员工ID" prop="empId"><el-input-number v-model="form.empId" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="离职类型" prop="resignType"><el-select v-model="form.resignType" style="width:100%"><el-option label="主动离职" :value="1" /><el-option label="被动离职" :value="2" /><el-option label="退休" :value="3" /><el-option label="其他" :value="4" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="申请日期" prop="applyDate"><el-date-picker v-model="form.applyDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预计离职日期"><el-date-picker v-model="form.resignDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="离职原因"><el-input v-model="form.reason" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="交接人"><el-input v-model="form.handoverTo" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getResignationPage, createResignation, updateResignation, approveResignation, deleteResignation, type ResignationData } from '@/api/resignation'

const list = ref<ResignationData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const formRef = ref<FormInstance>()
const defaultForm: ResignationData = { empId: 0, applyDate: '', resignType: 1, reason: '', status: 0 }
const form = reactive<ResignationData>({ ...defaultForm })
const rules: FormRules = { empId: [{ required: true, message: '请输入员工ID', trigger: 'blur' }], applyDate: [{ required: true, message: '请选择申请日期', trigger: 'change' }] }
const dialogTitle = computed(() => isEdit.value ? '编辑离职申请' : '新增离职申请')

onMounted(() => { loadData() })
async function loadData() { loading.value = true; try { const res = await getResignationPage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || [] } catch { } finally { loading.value = false } }
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: ResignationData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: ResignationData) { try { await deleteResignation(row.id!); ElMessage.success('删除成功'); loadData() } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  try { const res = isEdit.value ? await updateResignation(form) : await createResignation(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { }
}
async function approve(id: number, status: number) { try { await approveResignation(id, status); ElMessage.success(status === 1 ? '已通过' : '已驳回'); loadData() } catch { } }
function resignTypeLabel(v: number) { const m: Record<number, string> = { 1: '主动离职', 2: '被动离职', 3: '退休', 4: '其他' }; return m[v] || '未知' }
function handoverLabel(v: number) { const m: Record<number, string> = { 0: '未交接', 1: '交接中', 2: '已完成' }; return m[v] || '未知' }
function statusTag(v: number) { const m: Record<number, string> = { 0: 'warning', 1: 'success', 2: 'danger', 3: 'info' }; return m[v] || 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待审批', 1: '已通过', 2: '已驳回', 3: '已撤回' }; return m[v] || '未知' }
</script>
<style scoped lang="scss">
.resignation-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
