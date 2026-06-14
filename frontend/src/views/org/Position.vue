<template>
  <div class="position-page">
    <el-card shadow="never" class="mb-20">
      <el-form :model="queryParams" inline size="default">
        <el-form-item label="岗位名称">
          <el-input v-model="queryParams.name" placeholder="请输入" clearable />
        </el-form-item>
        <el-form-item label="所属部门">
          <el-select v-model="queryParams.deptId" placeholder="选择部门" clearable style="width: 160px">
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="选择状态" clearable style="width: 120px">
            <el-option label="正常" value="active" />
            <el-option label="停用" value="disabled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>岗位列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增岗位
          </el-button>
        </div>
      </template>

      <el-table :data="positionList" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="name" label="岗位名称" width="150" />
        <el-table-column prop="code" label="岗位编码" width="120" />
        <el-table-column prop="deptName" label="所属部门" width="150" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="70" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small">
              {{ row.status === 'active' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确认删除此岗位？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑岗位' : '新增岗位'"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="岗位名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="岗位编码" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-tree-select
            v-model="form.deptId"
            :data="deptOptions"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="选择部门"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="active">正常</el-radio>
            <el-radio label="disabled">停用</el-radio>
          </el-radio-group>
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
import { getDeptTree, getPositionList, createPosition, updatePosition, deletePosition, type PositionData, type DeptData } from '@/api/org'

const positionList = ref<PositionData[]>([])
const deptOptions = ref<DeptData[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const queryParams = reactive({
  name: '',
  deptId: undefined as number | undefined,
  status: '',
})

const defaultForm: PositionData = {
  deptId: 0,
  name: '',
  code: '',
  description: '',
  sort: 0,
  status: 0 as number,
}

const form = reactive<PositionData>({ ...defaultForm })

const rules: FormRules = {
  name: [{ required: true, message: '请输入岗位名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入岗位编码', trigger: 'blur' }],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
}

onMounted(() => {
  loadData()
  loadDeptOptions()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getPositionList(queryParams)
    if (res.data.code === 200) {
      positionList.value = res.data.data || []
    }
  } catch {
    // silent
  } finally {
    loading.value = false
  }
}

async function loadDeptOptions() {
  try {
    const res = await getDeptTree()
    if (res.data.code === 200) {
      deptOptions.value = res.data.data || []
    }
  } catch {
    // silent
  }
}

function handleSearch() { loadData() }
function handleReset() {
  queryParams.name = ''
  queryParams.deptId = undefined
  queryParams.status = ''
  loadData()
}

function handleAdd() {
  isEdit.value = false
  Object.assign(form, { ...defaultForm })
  dialogVisible.value = true
}

function handleEdit(row: PositionData) {
  isEdit.value = true
  Object.assign(form, JSON.parse(JSON.stringify(row)))
  dialogVisible.value = true
}

async function handleDelete(row: PositionData) {
  try {
    const res = await deletePosition(row.id!)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    }
  } catch {
    // handled
  }
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const res = isEdit.value ? await updatePosition(form) : await createPosition(form)
    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      loadData()
    }
  } catch {
    // handled
  } finally {
    submitLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.position-page {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: 600;
  }
}
</style>
