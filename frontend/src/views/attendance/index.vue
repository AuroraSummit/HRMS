<template>
  <div class="attendance-page">
    <el-card shadow="never" class="mb-20">
      <el-form :model="queryParams" inline size="default">
        <el-form-item label="月份">
          <el-date-picker v-model="queryParams.month" type="month" placeholder="选择月份" value-format="YYYY-MM" style="width:160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="选择" clearable style="width:120px">
            <el-option label="正常" value="normal" />
            <el-option label="迟到" value="late" />
            <el-option label="早退" value="early" />
            <el-option label="缺勤" value="absent" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="queryParams = { page: 1, pageSize: 10, month: '', status: '' }; loadData()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>考勤明细</span>
          <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增记录</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="empId" label="员工ID" width="100" />
        <el-table-column prop="attendanceDate" label="日期" width="110" />
        <el-table-column prop="clockInTime" label="签到" width="100" />
        <el-table-column prop="clockOutTime" label="签退" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{row}"><el-tag :type="attTag(row.status)" size="small">{{ attLabel(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{row}">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)">
              <template #reference><el-button type="danger" link size="small">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑考勤':'新增考勤'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="日期" prop="attendanceDate"><el-date-picker v-model="form.attendanceDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="form.status" style="width:100%"><el-option :value="0" label="正常" /><el-option :value="1" label="迟到" /><el-option :value="2" label="早退" /><el-option :value="3" label="缺勤" /><el-option :value="4" label="异常" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="签到" prop="clockInTime"><el-time-picker v-model="form.clockInTime" value-format="HH:mm:ss" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="签退" prop="clockOutTime"><el-time-picker v-model="form.clockOutTime" value-format="HH:mm:ss" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
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
import { getAttendancePage, createAttendance, updateAttendance, deleteAttendance, type AttendanceData } from '@/api/attendance'

const list = ref<AttendanceData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const submitLoading = ref(false); const formRef = ref<FormInstance>()
const queryParams = reactive({ page: 1, pageSize: 10, month: '', status: '' })
const defaultForm: AttendanceData = { empId: 0, attendanceDate: '', clockInTime: '', clockOutTime: '', status: 0, remark: '' }
const form = reactive<AttendanceData>({ ...defaultForm })
const rules: FormRules = { attendanceDate: [{ required: true, message: '请选择日期', trigger: 'change' }], status: [{ required: true, message: '请选择状态', trigger: 'change' }] }

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getAttendancePage(queryParams); if (res.data.code === 200) list.value = res.data.data.records || res.data.data || [] } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: AttendanceData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: AttendanceData) { try { const res = await deleteAttendance(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return; submitLoading.value = true
  try { const res = isEdit.value ? await updateAttendance(form) : await createAttendance(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { } finally { submitLoading.value = false }
}
function attTag(v: number) { return v === 0 ? 'success' : v === 1 ? 'warning' : v === 2 ? 'warning' : v === 3 ? 'danger' : 'danger' }
function attLabel(v: number) { const m: Record<number, string> = { 0: '正常', 1: '迟到', 2: '早退', 3: '缺勤', 4: '异常' }; return m[v] || '未知' }
</script>
<style scoped lang="scss">
.attendance-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
