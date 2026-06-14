<template>
  <div class="resignation-page">
    <el-card shadow="never">
      <template #header><div class="card-header"><span>我的离职申请</span><el-button type="primary" size="small" @click="handleApply">提交离职申请</el-button></div></template>
      <el-table :data="records" stripe border v-loading="loading" style="width:100%">
        <el-table-column prop="applyDate" label="申请日期" width="110" />
        <el-table-column prop="resignDate" label="预计离职日" width="110" />
        <el-table-column label="离职类型" width="100">
          <template #default="{ row }">{{ resignTypeLabel(row.resignType) }}</template>
        </el-table-column>
        <el-table-column label="交接状态" width="90">
          <template #default="{ row }">{{ handoverLabel(row.handoverStatus) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }"><el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="reason" label="原因" min-width="180" show-overflow-tooltip />
        <el-table-column prop="approveComment" label="审批意见" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="80">
          <template #default="{ row }">
            <el-button v-if="row.status===0" link type="danger" size="small" @click="withdraw(row)">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="提交离职申请" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="离职类型" prop="resignType"><el-select v-model="form.resignType" style="width:100%"><el-option label="主动离职" :value="1" /><el-option label="被动离职" :value="2" /><el-option label="退休" :value="3" /><el-option label="其他" :value="4" /></el-select></el-form-item>
        <el-form-item label="预计离职日期"><el-date-picker v-model="form.resignDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="离职原因" prop="reason"><el-input v-model="form.reason" type="textarea" :rows="4" placeholder="请说明离职原因..." /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit">提交申请</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getResignationPage, createResignation, updateResignation, type ResignationData } from '@/api/resignation'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const records = ref<ResignationData[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const formRef = ref<FormInstance>()
const form = reactive<ResignationData>({ empId: 0, applyDate: new Date().toISOString().slice(0, 10), resignType: 1, reason: '', status: 0 })
const rules: FormRules = { reason: [{ required: true, message: '请填写离职原因', trigger: 'blur' }] }

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try {
    const empId = userStore.userInfo?.empId || 0
    const res = await getResignationPage({ page: 1, pageSize: 100, empId })
    if (res.data.code === 200) records.value = res.data.data.records || []
  } catch { } finally { loading.value = false }
}
function handleApply() { form.empId = userStore.userInfo?.empId || 0; form.applyDate = new Date().toISOString().slice(0, 10); dialogVisible.value = true }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  try { const res = await createResignation(form); if (res.data.code === 200) { ElMessage.success('申请已提交'); dialogVisible.value = false; loadData() } } catch { }
}
async function withdraw(row: ResignationData) {
  try { await updateResignation({ ...row, status: 3 }); ElMessage.success('已撤回'); loadData() } catch { }
}
function resignTypeLabel(v: number) { const m: Record<number, string> = { 1: '主动离职', 2: '被动离职', 3: '退休', 4: '其他' }; return m[v] || '未知' }
function handoverLabel(v: number) { const m: Record<number, string> = { 0: '未交接', 1: '交接中', 2: '已完成' }; return m[v] || '未知' }
function statusTag(v: number) { const m: Record<number, string> = { 0: 'warning', 1: 'success', 2: 'danger', 3: 'info' }; return m[v] || 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待审批', 1: '已通过', 2: '已驳回', 3: '已撤回' }; return m[v] || '未知' }
</script>
<style scoped lang="scss">
.resignation-page { .card-header { display: flex; justify-content: space-between; align-items: center; } }
</style>
