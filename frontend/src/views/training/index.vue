<template>
  <div class="training-page">
    <el-card shadow="never">
      <template #header><div class="card-header"><span>培训管理</span><el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增培训</el-button></div></template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="name" label="培训主题" min-width="160" />
        <el-table-column prop="instructor" label="讲师" width="100" />
        <el-table-column prop="startDate" label="开始日期" width="110" />
        <el-table-column prop="endDate" label="结束日期" width="110" />
        <el-table-column prop="location" label="地点" width="120" />
        <el-table-column prop="maxAttendees" label="参训人数" width="90" />
        <el-table-column prop="status" label="状态" width="80"><template #default="{row}"><el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{row}"><el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button><el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm></template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑培训':'新增培训'" width="650px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="培训主题" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="讲师" prop="instructor"><el-input v-model="form.instructor" /></el-form-item>
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="结束日期" prop="endDate"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col></el-row>
        <el-form-item label="地点" prop="location"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="培训内容" prop="description"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="状态" prop="status"><el-radio-group v-model="form.status"><el-radio :label="0">待开始</el-radio><el-radio :label="1">进行中</el-radio><el-radio :label="2">已完成</el-radio><el-radio :label="3">已取消</el-radio></el-radio-group></el-form-item>
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
import { getTrainingPage, createTraining, updateTraining, deleteTraining, type TrainingData } from '@/api/training'

const list = ref<TrainingData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const submitLoading = ref(false); const formRef = ref<FormInstance>()
const defaultForm: TrainingData = { name: '', instructor: '', startDate: '', endDate: '', location: '', description: '', maxAttendees: 0, status: 0 }
const form = reactive<TrainingData>({ ...defaultForm })
const rules: FormRules = { name: [{ required: true, message: '请输入培训主题', trigger: 'blur' }], instructor: [{ required: true, message: '请输入讲师', trigger: 'blur' }] }

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getTrainingPage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || res.data.data || [] } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: TrainingData) { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true }
async function handleDelete(row: TrainingData) { try { const res = await deleteTraining(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return; submitLoading.value = true
  try { const res = isEdit.value ? await updateTraining(form) : await createTraining(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { } finally { submitLoading.value = false }
}
function statusTag(v: number) { const m: Record<number, string> = { 0: 'info', 1: 'primary', 2: 'success', 3: 'danger' }; return m[v] ?? 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待开始', 1: '进行中', 2: '已完成', 3: '已取消' }; return m[v] ?? '未知' }
</script>
<style scoped lang="scss">
.training-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
