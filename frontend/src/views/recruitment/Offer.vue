<template>
  <div class="offer-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>Offer管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增Offer
          </el-button>
        </div>
      </template>

      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="candidateName" label="候选人" width="100" />
        <el-table-column prop="offerSalary" label="薪资" width="100">
          <template #default="{ row }">{{ row.offerSalary }} 元</template>
        </el-table-column>
        <el-table-column prop="offerDate" label="发放日期" width="110" />
        <el-table-column prop="expiryDate" label="有效期至" width="110" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)">
              <template #reference><el-button type="danger" link size="small">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑Offer' : '新增Offer'" width="550px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="候选人" prop="candidateName"><el-input v-model="form.candidateName" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资(元)" prop="offerSalary"><el-input-number v-model="form.offerSalary" :min="0" :step="1000" style="width:100%" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" style="width:100%">
                <el-option :value="0" label="待发送" />
                <el-option :value="1" label="已发送" />
                <el-option :value="2" label="已接受" />
                <el-option :value="3" label="已拒绝" />
                <el-option :value="4" label="已撤回" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发放日期" prop="offerDate">
              <el-date-picker v-model="form.offerDate" type="date" placeholder="选择" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效期至" prop="expiryDate">
              <el-date-picker v-model="form.expiryDate" type="date" placeholder="选择" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
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
import { getOfferPage, createOffer, updateOffer, deleteOffer, type OfferData } from '@/api/recruitment'

const list = ref<OfferData[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const defaultForm: OfferData = {
  candidateName: '', demandId: 0, offerSalary: 0,
  offerDate: '', expiryDate: '', status: 0,
}
const form = reactive<OfferData>({ ...defaultForm })
const rules: FormRules = {
  candidateName: [{ required: true, message: '请输入候选人名称', trigger: 'blur' }],
}

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getOfferPage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || res.data.data || [] } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: OfferData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: OfferData) {
  try { const res = await deleteOffer(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { }
}
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const res = isEdit.value ? await updateOffer(form) : await createOffer(form)
    if (res.data.code === 200) { ElMessage.success(isEdit.value ? '编辑成功' : '新增成功'); dialogVisible.value = false; loadData() }
  } catch { } finally { submitLoading.value = false }
}
function statusTag(v: number) { return v === 2 ? 'success' : v === 3 ? 'danger' : v === 1 ? 'primary' : 'warning' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待发送', 1: '已发送', 2: '已接受', 3: '已拒绝', 4: '已撤回' }; return m[v] ?? '未知' }
</script>

<style scoped lang="scss">
.offer-page {
  .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; }
}
</style>
