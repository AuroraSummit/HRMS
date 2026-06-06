<template>
  <div class="contract-page">
    <el-card shadow="never">
      <template #header><div class="card-header"><span>合同管理</span><el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增合同</el-button></div></template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="empId" label="员工编号" width="100" />
        <el-table-column label="合同类型" width="110"><template #default="{row}">{{ contractTypeLabel(row.contractType) }}</template></el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="110" />
        <el-table-column prop="endDate" label="结束日期" width="110" />
        <el-table-column prop="signDate" label="签订日期" width="110" />
        <el-table-column label="状态" width="80"><template #default="{row}"><el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{row}">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status===1" type="success" link size="small" @click="handleRenew(row)">续签</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="合同类型" prop="contractType"><el-select v-model="form.contractType" style="width:100%"><el-option :label="'正式合同'" :value="1" /><el-option :label="'试用期合同'" :value="2" /><el-option :label="'实习协议'" :value="3" /><el-option :label="'劳务合同'" :value="4" /><el-option :label="'兼职合同'" :value="5" /></el-select></el-form-item></el-col><el-col :span="12"><el-form-item label="签订日期" prop="signDate"><el-date-picker v-model="form.signDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col></el-row>
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="结束日期" prop="endDate"><el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col></el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="状态" prop="status"><el-radio-group v-model="form.status"><el-radio :label="0">待签</el-radio><el-radio :label="1">生效</el-radio><el-radio :label="2">到期</el-radio><el-radio :label="3">解约</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getContractPage, createContract, updateContract, deleteContract, renewContract, type ContractData } from '@/api/contract'

const list = ref<ContractData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const isRenew = ref(false); const submitLoading = ref(false); const formRef = ref<FormInstance>()
const defaultForm: ContractData = { empId: 0, contractType: 1, startDate: '', endDate: '', signDate: '', fileUrl: '', remark: '', status: 0 }
const form = reactive<ContractData>({ ...defaultForm })
const rules: FormRules = { contractType: [{ required: true, message: '请选择合同类型', trigger: 'change' }], startDate: [{ required: true, message: '请选择日期', trigger: 'change' }] }
const dialogTitle = computed(() => { if (isRenew.value) return '续签合同'; return isEdit.value ? '编辑合同' : '新增合同' })

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getContractPage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || res.data.data || [] } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; isRenew.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: ContractData) { isEdit.value = true; isRenew.value = false; Object.assign(form, row); dialogVisible.value = true }
function handleRenew(row: ContractData) { isEdit.value = false; isRenew.value = true; Object.assign(form, { ...defaultForm, empId: row.empId }); dialogVisible.value = true }
async function handleDelete(row: ContractData) { try { const res = await deleteContract(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return; submitLoading.value = true
  try {
    let res
    if (isRenew.value) { res = await renewContract(form.empId, form) }
    else { res = isEdit.value ? await updateContract(form) : await createContract(form) }
    if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() }
  } catch { } finally { submitLoading.value = false }
}
function contractTypeLabel(v: number) { const m: Record<number, string> = { 1: '正式合同', 2: '试用期合同', 3: '实习协议', 4: '劳务合同', 5: '兼职合同' }; return m[v] || '未知' }
function statusTag(v: number) { const m: Record<number, string> = { 0: 'info', 1: 'success', 2: 'danger', 3: 'warning' }; return m[v] || 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待签', 1: '生效', 2: '到期', 3: '解约' }; return m[v] || '未知' }
</script>
<style scoped lang="scss">
.contract-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
