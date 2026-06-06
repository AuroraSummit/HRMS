<template>
  <div class="salary-page">
    <el-card shadow="never" class="mb-20">
      <el-form :model="queryParams" inline>
        <el-form-item label="月份"><el-date-picker v-model="queryParams.month" type="month" value-format="YYYY-MM" style="width:160px" /></el-form-item>
        <el-form-item label="姓名"><el-input v-model="queryParams.name" placeholder="请输入" clearable /></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">搜索</el-button><el-button @click="queryParams={page:1,pageSize:10,month:'',name:''};loadData()">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never">
      <template #header><div class="card-header"><span>薪资记录</span><el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增薪资</el-button></div></template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="month" label="月份" width="80" />
        <el-table-column prop="basicSalary" label="基本工资" width="100" /><el-table-column prop="bonus" label="绩效奖金" width="100" />
        <el-table-column prop="overtimePay" label="加班费" width="80" /><el-table-column prop="allowance" label="补贴" width="80" />
        <el-table-column prop="housingFund" label="住房公积金" width="80" /><el-table-column prop="tax" label="个税" width="70" />
        <el-table-column prop="actualSalary" label="实发工资" width="100"><template #default="{row}"><span style="color:#f56c6c;font-weight:600">{{ row.actualSalary }}</span></template></el-table-column>
        <el-table-column prop="status" label="状态" width="70"><template #default="{row}"><el-tag :type="row.status===1?'success':row.status===2?'info':'warning'" size="small">{{ row.status===1?'已发放':row.status===2?'已撤回':'未发放' }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{row}"><el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button><el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm></template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrapper"><el-pagination v-model:current-page="queryParams.page" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next" @current-change="loadData" @size-change="loadData" /></div>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑薪资':'新增薪资'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="月份" prop="month"><el-date-picker v-model="form.month" type="month" value-format="YYYY-MM" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="基本工资" prop="basicSalary"><el-input-number v-model="form.basicSalary" :min="0" :step="1000" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="绩效奖金" prop="bonus"><el-input-number v-model="form.bonus" :min="0" :step="500" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="加班费" prop="overtimePay"><el-input-number v-model="form.overtimePay" :min="0" :step="100" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="补贴" prop="allowance"><el-input-number v-model="form.allowance" :min="0" :step="100" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="住房公积金" prop="housingFund"><el-input-number v-model="form.housingFund" :min="0" :step="100" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="个税" prop="tax"><el-input-number v-model="form.tax" :min="0" :step="10" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="实发工资" prop="actualSalary"><el-input-number v-model="form.actualSalary" :min="0" :step="1000" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="状态" prop="status"><el-radio-group v-model="form.status"><el-radio :label="0">未发放</el-radio><el-radio :label="1">已发放</el-radio><el-radio :label="2">已撤回</el-radio></el-radio-group></el-form-item>
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
import { getSalaryPage, createSalary, updateSalary, deleteSalary, type SalaryData } from '@/api/salary'

const list = ref<SalaryData[]>([]); const total = ref(0); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const submitLoading = ref(false); const formRef = ref<FormInstance>()
const queryParams = reactive({ page: 1, pageSize: 10, month: '', name: '' })
const defaultForm: SalaryData = { empId: 0, month: '', basicSalary: 0, bonus: 0, overtimePay: 0, allowance: 0, housingFund: 0, pension: 0, medical: 0, unemployment: 0, tax: 0, deduction: 0, actualSalary: 0, status: 0 }
const form = reactive<SalaryData>({ ...defaultForm })
const rules: FormRules = { month: [{ required: true, message: '请选择月份', trigger: 'change' }], basicSalary: [{ required: true, message: '请输入基本工资', trigger: 'blur' }] }

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getSalaryPage(queryParams); if (res.data.code === 200) { const d = res.data.data; list.value = d.records || d || []; total.value = d.total || 0 } } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: SalaryData) { isEdit.value = true; Object.assign(form, row); dialogVisible.value = true }
async function handleDelete(row: SalaryData) { try { const res = await deleteSalary(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return; submitLoading.value = true
  try { const res = isEdit.value ? await updateSalary(form) : await createSalary(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { } finally { submitLoading.value = false }
}
</script>
<style scoped lang="scss">
.salary-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } .pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 16px; } }
</style>
