<template>
  <div class="transfer-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>员工异动管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增异动
          </el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="empId" label="员工ID" width="80" />
        <el-table-column label="异动类型" width="100">
          <template #default="{ row }">{{ typeLabel(row.transferType) }}</template>
        </el-table-column>
        <el-table-column prop="fromDeptId" label="原部门" width="100" />
        <el-table-column prop="toDeptId" label="新部门" width="100" />
        <el-table-column prop="fromPositionId" label="原岗位" width="100" />
        <el-table-column prop="toPositionId" label="新岗位" width="100" />
        <el-table-column prop="transferDate" label="异动日期" width="110" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="原因" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status===0" type="success" link size="small" @click="approveTransfer(row.id,1)">通过</el-button>
            <el-button v-if="row.status===0" type="warning" link size="small" @click="approveTransfer(row.id,2)">驳回</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="员工ID" prop="empId"><el-input-number v-model="form.empId" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="异动类型" prop="transferType"><el-select v-model="form.transferType" style="width:100%"><el-option label="晋升" :value="1" /><el-option label="平调" :value="2" /><el-option label="降职" :value="3" /><el-option label="转岗" :value="4" /><el-option label="其他" :value="5" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="原部门" prop="fromDeptId"><el-input-number v-model="form.fromDeptId" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="新部门" prop="toDeptId"><el-input-number v-model="form.toDeptId" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="原岗位" prop="fromPositionId"><el-input-number v-model="form.fromPositionId" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="新岗位" prop="toPositionId"><el-input-number v-model="form.toPositionId" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="异动日期" prop="transferDate"><el-date-picker v-model="form.transferDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
        <el-form-item label="原因"><el-input v-model="form.reason" type="textarea" :rows="2" /></el-form-item>
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
import { getTransferPage, createTransfer, updateTransfer, approveTransfer as approveTransferApi, deleteTransfer, type TransferData } from '@/api/transfer'

const list = ref<TransferData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const formRef = ref<FormInstance>()
const defaultForm: TransferData = { empId: 0, transferType: 1, transferDate: '', reason: '', remark: '', status: 0 }
const form = reactive<TransferData>({ ...defaultForm })
const rules: FormRules = { empId: [{ required: true, message: '请输入员工ID', trigger: 'blur' }], transferType: [{ required: true, message: '请选择异动类型', trigger: 'change' }], transferDate: [{ required: true, message: '请选择异动日期', trigger: 'change' }] }
const dialogTitle = computed(() => isEdit.value ? '编辑异动' : '新增异动')

onMounted(() => { loadData() })
async function loadData() { loading.value = true; try { const res = await getTransferPage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || [] } catch { } finally { loading.value = false } }
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: TransferData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: TransferData) { try { await deleteTransfer(row.id!); ElMessage.success('删除成功'); loadData() } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  try {
    const res = isEdit.value ? await updateTransfer(form) : await createTransfer(form)
    if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
  } catch { }
}
async function approveTransfer(id: number, status: number) {
  try { await approveTransferApi(id, status); ElMessage.success(status === 1 ? '已通过' : '已驳回'); loadData() } catch { }
}
function typeLabel(v: number) { const m: Record<number, string> = { 1: '晋升', 2: '平调', 3: '降职', 4: '转岗', 5: '其他' }; return m[v] || '未知' }
function statusTag(v: number) { const m: Record<number, string> = { 0: 'warning', 1: 'success', 2: 'danger' }; return m[v] || 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待审批', 1: '已通过', 2: '已驳回' }; return m[v] || '未知' }
</script>
<style scoped lang="scss">
.transfer-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
