<template>
  <div class="interview-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>面试记录</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增面试
          </el-button>
        </div>
      </template>

      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="candidateName" label="候选人" width="100" />
        <el-table-column prop="phone" label="电话" width="120" />
        <el-table-column prop="position" label="应聘岗位" width="120" />
        <el-table-column prop="interviewDate" label="面试时间" width="110" />
        <el-table-column prop="interviewer" label="面试官" width="100" />
        <el-table-column prop="round" label="轮次" width="60" />
        <el-table-column prop="result" label="结果" width="80">
          <template #default="{ row }">
            <el-tag :type="resultTag(row.result)" size="small">{{ resultLabel(row.result) }}</el-tag>
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑面试' : '新增面试'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="候选人" prop="candidateName"><el-input v-model="form.candidateName" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="phone"><el-input v-model="form.phone" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email"><el-input v-model="form.email" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面试岗位" prop="position">
              <el-input v-model="form.position" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="面试时间" prop="interviewDate">
              <el-date-picker v-model="form.interviewDate" type="datetime" placeholder="选择" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="面试官" prop="interviewer"><el-input v-model="form.interviewer" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="轮次" prop="round"><el-input-number v-model="form.round" :min="1" style="width:100%" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结果" prop="result">
              <el-select v-model="form.result" placeholder="选择" style="width:100%">
                <el-option :value="1" label="通过" /><el-option :value="2" label="不通过" /><el-option :value="3" label="待定" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="评价" prop="comment">
          <el-input v-model="form.comment" type="textarea" :rows="3" />
        </el-form-item>
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
import { getInterviewPage, createInterview, updateInterview, deleteInterview, type InterviewData } from '@/api/recruitment'

const list = ref<InterviewData[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const defaultForm: InterviewData = {
  candidateName: '', phone: '', email: '', position: '', demandId: 0,
  interviewDate: '', interviewer: '', round: 1, result: 3, comment: '',
}
const form = reactive<InterviewData>({ ...defaultForm })
const rules: FormRules = {
  candidateName: [{ required: true, message: '请输入候选人姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
  interviewDate: [{ required: true, message: '请选择面试时间', trigger: 'change' }],
}

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getInterviewPage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || res.data.data || [] } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: InterviewData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: InterviewData) {
  try { const res = await deleteInterview(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { }
}
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const res = isEdit.value ? await updateInterview(form) : await createInterview(form)
    if (res.data.code === 200) { ElMessage.success(isEdit.value ? '编辑成功' : '新增成功'); dialogVisible.value = false; loadData() }
  } catch { } finally { submitLoading.value = false }
}
function resultTag(v: number) { return v === 1 ? 'success' : v === 2 ? 'danger' : 'warning' }
function resultLabel(v: number) { const m: Record<number, string> = { 0: '未开始', 1: '通过', 2: '未通过', 3: '待定' }; return m[v] ?? '待定' }
</script>

<style scoped lang="scss">
.interview-page {
  .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; }
}
</style>
