<template>
  <div class="dict-page">
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>字典类型</span>
              <el-button type="primary" size="small" @click="handleAddType"><el-icon><Plus /></el-icon>新增</el-button>
            </div>
          </template>
          <el-table :data="typeList" stripe highlight-current-row style="width:100%" @row-click="handleTypeClick">
            <el-table-column prop="name" label="字典名称" min-width="120" />
            <el-table-column prop="code" label="编码" width="100" />
            <el-table-column label="操作" width="100">
              <template #default="{row}">
                <el-button type="primary" link size="small" @click.stop="handleEditType(row)">编辑</el-button>
                <el-popconfirm title="确认删除？" @confirm.stop="handleDeleteType(row)">
                  <template #reference><el-button type="danger" link size="small" @click.stop>删除</el-button></template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>{{ selectedType ? selectedType.name + ' - 字典数据' : '字典数据' }}</span>
              <el-button type="primary" size="small" :disabled="!selectedType" @click="handleAddData">
                <el-icon><Plus /></el-icon>新增
              </el-button>
            </div>
          </template>
          <el-table :data="dataList" stripe style="width:100%">
            <el-table-column prop="label" label="标签" width="120" />
            <el-table-column prop="value" label="值" width="120" />
            <el-table-column prop="sort" label="排序" width="60" />
            <el-table-column prop="status" label="状态" width="70"><template #default="{row}"><el-tag :type="(row.status===0||row.status==='active')?'success':'info'" size="small">{{ (row.status===0||row.status==='active')?'正常':'停用' }}</el-tag></template></el-table-column>
            <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
            <el-table-column label="操作" width="120">
              <template #default="{row}"><el-button type="primary" link size="small" @click="handleEditData(row)">编辑</el-button><el-popconfirm title="确认删除？" @confirm="handleDeleteData(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm></template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- Dict Type Dialog -->
    <el-dialog v-model="typeDialogVisible" :title="isEditType?'编辑字典类型':'新增字典类型'" width="450px">
      <el-form ref="typeFormRef" :model="typeForm" :rules="typeRules" label-width="90px">
        <el-form-item label="字典名称" prop="name"><el-input v-model="typeForm.name" /></el-form-item>
        <el-form-item label="字典编码" prop="code"><el-input v-model="typeForm.code" /></el-form-item>
        <el-form-item label="描述" prop="description"><el-input v-model="typeForm.description" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="状态" prop="status"><el-radio-group v-model="typeForm.status"><el-radio label="active">正常</el-radio><el-radio label="disabled">停用</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="typeDialogVisible=false">取消</el-button><el-button type="primary" :loading="submitTypeLoading" @click="handleSubmitType">确定</el-button></template>
    </el-dialog>

    <!-- Dict Data Dialog -->
    <el-dialog v-model="dataDialogVisible" :title="isEditData?'编辑字典数据':'新增字典数据'" width="450px">
      <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="90px">
        <el-form-item label="标签" prop="label"><el-input v-model="dataForm.label" /></el-form-item>
        <el-form-item label="值" prop="value"><el-input v-model="dataForm.value" /></el-form-item>
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="排序" prop="sort"><el-input-number v-model="dataForm.sort" :min="0" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="状态" prop="status"><el-radio-group v-model="dataForm.status"><el-radio label="active">正常</el-radio><el-radio label="disabled">停用</el-radio></el-radio-group></el-form-item></el-col></el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="dataForm.remark" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dataDialogVisible=false">取消</el-button><el-button type="primary" :loading="submitDataLoading" @click="handleSubmitData">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getDictTypeList, createDictType, updateDictType, deleteDictType, getDictDataList, createDictData, updateDictData, deleteDictData, type DictTypeData, type DictDataItem } from '@/api/system'

const typeList = ref<DictTypeData[]>([]); const dataList = ref<DictDataItem[]>([]); const selectedType = ref<DictTypeData | null>(null)
const typeDialogVisible = ref(false); const isEditType = ref(false); const submitTypeLoading = ref(false); const typeFormRef = ref<FormInstance>()
const dataDialogVisible = ref(false); const isEditData = ref(false); const submitDataLoading = ref(false); const dataFormRef = ref<FormInstance>()

const typeForm = reactive<DictTypeData>({ name: '', code: '', description: '', status: 'active' })
const typeRules: FormRules = { name: [{ required: true, message: '请输入字典名称', trigger: 'blur' }], code: [{ required: true, message: '请输入字典编码', trigger: 'blur' }] }
const dataForm = reactive<DictDataItem>({ typeId: 0, label: '', value: '', sort: 0, status: 'active', remark: '' })
const dataRules: FormRules = { label: [{ required: true, message: '请输入标签', trigger: 'blur' }], value: [{ required: true, message: '请输入值', trigger: 'blur' }] }

onMounted(() => { loadTypes() })

async function loadTypes() { try { const res = await getDictTypeList(); if (res.data.code === 200) typeList.value = res.data.data || [] } catch { } }
async function loadDataList(typeId: number) { try { const res = await getDictDataList({ dictTypeId: typeId }); if (res.data.code === 200) dataList.value = res.data.data || [] } catch { } }

function handleTypeClick(row: DictTypeData) { selectedType.value = row; loadDataList(row.id!) }

function handleAddType() { isEditType.value = false; Object.assign(typeForm, { name: '', code: '', description: '', status: 'active' }); typeDialogVisible.value = true }
function handleEditType(row: DictTypeData) { isEditType.value = true; Object.assign(typeForm, row); typeDialogVisible.value = true }
async function handleDeleteType(row: DictTypeData) { try { const res = await deleteDictType(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadTypes(); if (selectedType.value?.id === row.id) { selectedType.value = null; dataList.value = [] } } } catch { } }
async function handleSubmitType() {
  const valid = await typeFormRef.value?.validate().catch(() => false)
  if (!valid) return; submitTypeLoading.value = true
  try {
    const payload = { ...typeForm, status: typeForm.status === 'active' ? 0 : 1 }
    const res = isEditType.value ? await updateDictType(payload) : await createDictType(payload)
    if (res.data.code === 200) { ElMessage.success('操作成功'); typeDialogVisible.value = false; loadTypes() }
  } catch { } finally { submitTypeLoading.value = false }
}

function handleAddData() { if (!selectedType.value) return; isEditData.value = false; Object.assign(dataForm, { dictTypeId: selectedType.value.id!, label: '', value: '', sort: 0, status: 'active', remark: '' }); dataDialogVisible.value = true }
function handleEditData(row: DictDataItem) { isEditData.value = true; Object.assign(dataForm, { ...row, status: (row.status === 0 || row.status === 'active') ? 'active' : 'disabled' }); dataDialogVisible.value = true }
async function handleDeleteData(row: DictDataItem) { try { const res = await deleteDictData(row.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); if (selectedType.value) loadDataList(selectedType.value.id!) } } catch { } }
async function handleSubmitData() {
  const valid = await dataFormRef.value?.validate().catch(() => false)
  if (!valid) return; submitDataLoading.value = true
  try {
    const payload = { ...dataForm, status: dataForm.status === 'active' ? 0 : 1 }
    const res = isEditData.value ? await updateDictData(payload) : await createDictData(payload)
    if (res.data.code === 200) { ElMessage.success('操作成功'); dataDialogVisible.value = false; if (selectedType.value) loadDataList(selectedType.value.id!) }
  } catch { } finally { submitDataLoading.value = false }
}
</script>

<style scoped lang="scss">
.dict-page {
  .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; }
  :deep(.el-table__row) { cursor: pointer; }
}
</style>
