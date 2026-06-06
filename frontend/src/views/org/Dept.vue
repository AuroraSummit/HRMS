<template>
  <div class="dept-page">
    <el-card shadow="never" class="mb-20">
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <el-button type="primary" size="default" @click="handleAddRoot">
            <el-icon><Plus /></el-icon>新增根部门
          </el-button>
        </div>
      </template>

      <div class="dept-content">
        <div class="dept-tree">
          <el-tree
            :data="deptTree"
            :props="{ label: 'label', children: 'children' }"
            node-key="id"
            :default-expand-all="true"
            :highlight-current="true"
            :expand-on-click-node="false"
            @node-click="handleNodeClick"
          >
            <template #default="{ node, data }">
              <span class="tree-node">
                <span class="tree-label">{{ node.label }}</span>
                <span class="tree-actions">
                  <el-button link type="primary" size="small" @click.stop="handleAddChild(data)">
                    新增
                  </el-button>
                  <el-button link type="primary" size="small" @click.stop="handleEdit(data)">
                    编辑
                  </el-button>
                  <el-popconfirm
                    title="确认删除此部门及子部门？"
                    @confirm.stop="handleDelete(data)"
                  >
                    <template #reference>
                      <el-button link type="danger" size="small" @click.stop>
                        删除
                      </el-button>
                    </template>
                  </el-popconfirm>
                </span>
              </span>
            </template>
          </el-tree>
        </div>
      </div>
    </el-card>

    <!-- Dept Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑部门' : '新增部门'"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="deptTree"
            :props="{ label: 'label', value: 'id', children: 'children' }"
            placeholder="选择上级部门"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="form.leader" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="0">正常</el-radio>
            <el-radio :value="1">停用</el-radio>
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
import { getDeptTree, getDeptById, createDept, updateDept, deleteDept, type DeptData } from '@/api/org'

const deptTree = ref<DeptData[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const defaultForm: DeptData = {
  parentId: 0,
  name: '',
  leader: '',
  phone: '',
  email: '',
  sort: 0,
  status: 0,
}

const form = reactive<DeptData>({ ...defaultForm })

const rules: FormRules = {
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
}

onMounted(() => {
  loadData()
})

async function loadData() {
  try {
    const res = await getDeptTree()
    if (res.data.code === 200) {
      deptTree.value = res.data.data || []
    }
  } catch {
    // silent
  }
}

function handleAddRoot() {
  isEdit.value = false
  Object.assign(form, { ...defaultForm })
  dialogVisible.value = true
}

function handleAddChild(data: DeptData) {
  isEdit.value = false
  Object.assign(form, { ...defaultForm, parentId: data.id || 0 })
  dialogVisible.value = true
}

async function handleEdit(data: DeptData) {
  isEdit.value = true
  try {
    const res = await getDeptById(data.id!)
    if (res.data.code === 200 && res.data.data) {
      Object.assign(form, res.data.data)
    }
  } catch {
    Object.assign(form, data)
  }
  dialogVisible.value = true
}

async function handleDelete(data: DeptData) {
  try {
    const res = await deleteDept(data.id!)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    }
  } catch {
    // handled
  }
}

function handleNodeClick(_data: DeptData) {
  // optional: show dept detail
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const res = isEdit.value ? await updateDept(form) : await createDept(form)
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
.dept-page {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: 600;
  }

  .dept-content {
    .tree-node {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      padding-right: 8px;

      .tree-label {
        font-size: 14px;
      }

      .tree-actions {
        display: none;
        gap: 4px;
      }

      &:hover .tree-actions {
        display: inline-flex;
      }
    }
  }
}
</style>
