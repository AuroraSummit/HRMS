<template>
  <div class="leave-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>请假管理</span>
          <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增请假</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="employeeName" label="申请人" width="100" />
        <el-table-column label="请假类型" width="100">
          <template #default="{row}">{{ ['年假','事假','病假','婚假','产假','丧假','其他'][row.leaveType-1] || '未知' }}</template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="110" />
        <el-table-column prop="endDate" label="结束日期" width="110" />
        <el-table-column prop="duration" label="天数" width="60" />
        <el-table-column prop="reason" label="原因" min-width="160" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{row}"><el-tag :type="['warning','success','danger','info'][row.status]" size="small">{{ ['待审批','已通过','已驳回','已取消'][row.status] }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{row}">
            <el-button v-if="row.status===0" type="success" link size="small" @click="handleApprove(row)">通过</el-button>
            <el-button v-if="row.status===0" type="danger" link size="small" @click="handleReject(row)">驳回</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)">
              <template #reference><el-button type="danger" link size="small">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑请假':'新增请假'" width="550px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="请假类型" prop="leaveType"><el-select v-model="form.leaveType" style="width:100%"><el-option :value="2" label="事假" /><el-option :value="3" label="病假" /><el-option :value="1" label="年假" /><el-option :value="4" label="婚假" /><el-option :value="5" label="产假" /><el-option :value="6" label="丧假" /><el-option :value="7" label="其他" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="天数" prop="duration"><el-input-number v-model="form.duration" :min="0.5" :step="0.5" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结束日期" prop="endDate"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="请假原因" prop="reason"><el-input v-model="form.reason" type="textarea" :rows="3" /></el-form-item>
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
import { getLeavePage, createLeave, updateLeave, deleteLeave, approveLeave, type LeaveData } from '@/api/attendance'

const list = ref<LeaveData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const submitLoading = ref(false); const formRef = ref<FormInstance>()
const defaultForm: LeaveData = { empId: 0, leaveType: 2, startDate: '', endDate: '', duration: 1, reason: '', status: 0 }
const form = reactive<LeaveData>({ ...defaultForm })
const rules: FormRules = { leaveType: [{ required: true, message: '请选择类型', trigger: 'change' }], startDate: [{ required: true, message: '请选择日期', trigger: 'change' }], endDate: [{ required: true, message: '请选择日期', trigger: 'change' }] }

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getLeavePage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || res.data.data || [] } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: LeaveData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: LeaveData) { try { const res = await deleteLeave(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { } }
async function handleApprove(row: LeaveData) { try { const res = await approveLeave(row.id!, 1); if (res.data.code === 200) { ElMessage.success('已通过'); loadData() } } catch { } }
async function handleReject(row: LeaveData) { try { const res = await approveLeave(row.id!, 2); if (res.data.code === 200) { ElMessage.success('已驳回'); loadData() } } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return; submitLoading.value = true
  try { const res = isEdit.value ? await updateLeave(form) : await createLeave(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { } finally { submitLoading.value = false }
}
</script>
<style scoped lang="scss">
.leave-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
