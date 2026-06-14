<template>
  <div class="performance-page">
    <el-card shadow="never">
      <template #header><div class="card-header"><span>绩效考核</span><el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增考核</el-button></div></template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="empId" label="员工ID" width="70" />
        <el-table-column prop="planId" label="计划ID" width="70" />
        <el-table-column prop="selfScore" label="自评" width="60" />
        <el-table-column prop="managerScore" label="主管评分" width="80" />
        <el-table-column prop="finalScore" label="最终得分" width="80"><template #default="{row}"><span style="color:#f56c6c;font-weight:600">{{ row.finalScore }}</span></template></el-table-column>
        <el-table-column prop="result" label="等级" width="70"><template #default="{row}"><el-tag :type="resultTag(row.result)" size="small">{{ resultLabel(row.result) }}</el-tag></template></el-table-column>
        <el-table-column prop="managerComment" label="主管评语" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{row}"><el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button><el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm></template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑考核':'新增考核'" width="550px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="员工ID" prop="empId"><el-input-number v-model="form.empId" :min="0" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="计划ID" prop="planId"><el-input-number v-model="form.planId" :min="0" style="width:100%" /></el-form-item></el-col></el-row>
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="自评分数" prop="selfScore"><el-input-number v-model="form.selfScore" :min="0" :max="100" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="主管评分" prop="managerScore"><el-input-number v-model="form.managerScore" :min="0" :max="100" style="width:100%" /></el-form-item></el-col></el-row>
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="最终得分" prop="finalScore"><el-input-number v-model="form.finalScore" :min="0" :max="100" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="结果" prop="result"><el-select v-model="form.result" style="width:100%"><el-option label="优秀" :value="1" /><el-option label="良好" :value="2" /><el-option label="合格" :value="3" /><el-option label="待改进" :value="4" /><el-option label="不合格" :value="5" /></el-select></el-form-item></el-col></el-row>
        <el-form-item label="自评意见" prop="selfComment"><el-input v-model="form.selfComment" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="主管评语" prop="managerComment"><el-input v-model="form.managerComment" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getPerformancePage, createPerformance, updatePerformance, deletePerformance, type PerformanceData } from '@/api/performance'

const list = ref<PerformanceData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const submitLoading = ref(false); const formRef = ref<FormInstance>()
const defaultForm: PerformanceData = { empId: 0, planId: 0, selfScore: 0, selfComment: '', managerScore: 0, managerComment: '', finalScore: 0, result: 3 }
const form = reactive<PerformanceData>({ ...defaultForm })
const rules: FormRules = { planId: [{ required: true, message: '请选择计划ID', trigger: 'change' }] }

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getPerformancePage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || res.data.data || [] } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: PerformanceData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: PerformanceData) { try { const res = await deletePerformance(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return; submitLoading.value = true
  try { const res = isEdit.value ? await updatePerformance(form) : await createPerformance(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { } finally { submitLoading.value = false }
}
function resultTag(v: number) { const m: Record<number, string> = {1:'success',2:'primary',3:'warning',4:'danger',5:'danger'}; return m[v]||'info' }
function resultLabel(v: number) { const m: Record<number, string> = {1:'优秀',2:'良好',3:'合格',4:'待改进',5:'不合格'}; return m[v]||'未知' }
</script>
<style scoped lang="scss">
.performance-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
