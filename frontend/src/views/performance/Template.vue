<template>
  <div class="template-page">
    <el-card shadow="never">
      <template #header><div class="card-header"><span>绩效模板</span><el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增模板</el-button></div></template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="name" label="模板名称" width="150" />
        <el-table-column prop="description" label="描述" min-width="300" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="70"><template #default="{row}"><el-tag :type="row.status===0?'success':'info'" size="small">{{ row.status===0?'启用':'停用' }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{row}"><el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button><el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm></template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogVisible" :title="isEdit?'编辑模板':'新增模板'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="模板名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="描述" prop="description"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="状态" prop="status"><el-radio-group v-model="form.status"><el-radio :label="0">启用</el-radio><el-radio :label="1">停用</el-radio></el-radio-group></el-form-item>
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
import { getTemplatePage, createTemplate, updateTemplate, deleteTemplate, type TemplateData } from '@/api/performance'

const list = ref<TemplateData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const submitLoading = ref(false); const formRef = ref<FormInstance>()
const defaultForm: TemplateData = { name: '', description: '', status: 0 }
const form = reactive<TemplateData>({ ...defaultForm })
const rules: FormRules = { name: [{ required: true, message: '请输入模板名称', trigger: 'blur' }] }

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try { const res = await getTemplatePage({ page: 1, pageSize: 100 }); if (res.data.code === 200) list.value = res.data.data.records || res.data.data || [] } catch { } finally { loading.value = false }
}
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: TemplateData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: TemplateData) { try { const res = await deleteTemplate(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return; submitLoading.value = true
  try { const res = isEdit.value ? await updateTemplate(form) : await createTemplate(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { } finally { submitLoading.value = false }
}
</script>
<style scoped lang="scss">
.template-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
