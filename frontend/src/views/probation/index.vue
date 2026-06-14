<template>
  <div class="probation-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>转正管理</span>
          <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增转正记录</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="empId" label="员工ID" width="80" />
        <el-table-column prop="probationStart" label="试用开始" width="110" />
        <el-table-column prop="probationEnd" label="试用结束" width="110" />
        <el-table-column prop="actualEnd" label="实际转正" width="110" />
        <el-table-column prop="mentorName" label="导师" width="100" />
        <el-table-column label="综合评分" width="90">
          <template #default="{ row }">{{ row.score ?? '-' }}</template>
        </el-table-column>
        <el-table-column label="转正结果" width="90">
          <template #default="{ row }"><el-tag :type="resultTag(row.result)" size="small">{{ resultLabel(row.result) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }"><el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status===1" type="success" link size="small" @click="approve(row.id,2)">通过</el-button>
            <el-button v-if="row.status===1" type="warning" link size="small" @click="approve(row.id,3)">驳回</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="员工ID" prop="empId"><el-input-number v-model="form.empId" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="导师姓名"><el-input v-model="form.mentorName" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="试用开始" prop="probationStart"><el-date-picker v-model="form.probationStart" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="试用结束" prop="probationEnd"><el-date-picker v-model="form.probationEnd" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="自评总结"><el-input v-model="form.selfEvaluation" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="导师评价"><el-input v-model="form.mentorEvaluation" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="主管评价"><el-input v-model="form.managerEvaluation" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="综合评分"><el-input-number v-model="form.score" :min="0" :max="100" :precision="1" style="width:200px" /></el-form-item>
        <el-form-item label="转正结果"><el-radio-group v-model="form.result"><el-radio :label="1">通过</el-radio><el-radio :label="2">延期</el-radio><el-radio :label="3">不通过</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" /></el-form-item>
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
import { getProbationPage, createProbation, updateProbation, approveProbation, deleteProbation, type ProbationData } from '@/api/probation'

const list = ref<ProbationData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const formRef = ref<FormInstance>()
const defaultForm: ProbationData = { empId: 0, probationStart: '', probationEnd: '', status: 0 }
const form = reactive<ProbationData>({ ...defaultForm })
const rules: FormRules = { empId: [{ required: true, message: '请输入员工ID', trigger: 'blur' }], probationStart: [{ required: true, message: '请选择日期', trigger: 'change' }], probationEnd: [{ required: true, message: '请选择日期', trigger: 'change' }] }
const dialogTitle = computed(() => isEdit.value ? '编辑转正记录' : '新增转正记录')

onMounted(() => { loadData() })
async function loadData() { loading.value = true; try { const res = await getProbationPage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || [] } catch { } finally { loading.value = false } }
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: ProbationData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: ProbationData) { try { await deleteProbation(row.id!); ElMessage.success('删除成功'); loadData() } catch { } }
async function handleSubmit() { const valid = await formRef.value?.validate().catch(() => false); if (!valid) return; try { const res = isEdit.value ? await updateProbation(form) : await createProbation(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { } }
async function approve(id: number, status: number) { try { await approveProbation(id, status); ElMessage.success('操作成功'); loadData() } catch { } }
function resultTag(v: number) { const m: Record<number, string> = { 1: 'success', 2: 'warning', 3: 'danger' }; return m[v] || 'info' }
function resultLabel(v: number) { const m: Record<number, string> = { 1: '通过', 2: '延期', 3: '不通过' }; return m[v] || '未知' }
function statusTag(v: number) { const m: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }; return m[v] || 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待评估', 1: '已提交', 2: '已通过', 3: '已驳回' }; return m[v] || '未知' }
</script>
<style scoped lang="scss">
.probation-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
