<template>
  <div class="role-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增角色
          </el-button>
        </div>
      </template>

      <el-table :data="roleList" v-loading="loading" stripe border style="width: 100%">
        <el-table-column prop="name" label="角色名称" min-width="140" />
        <el-table-column prop="code" label="角色编码" min-width="140" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small">
              {{ row.status === 'active' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleAssignMenu(row)">分配菜单</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确认删除此角色？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑角色' : '新增角色'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="如: admin" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="active">启用</el-radio>
            <el-radio label="disabled">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- Assign Menu Dialog -->
    <el-dialog v-model="menuDialogVisible" title="分配菜单权限" width="400px">
      <div class="menu-dialog-body">
        <el-tree
          ref="menuTreeRef"
          :data="menuTree"
          :props="{ label: 'title', children: 'children' }"
          node-key="id"
          show-checkbox
          default-expand-all
          :default-checked-keys="checkedMenuIds"
        />
      </div>
      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="menuSubmitLoading" @click="handleAssignSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import type { ElTree } from 'element-plus'
import { getRolePage, createRole, updateRole, deleteRole, assignRoleMenus, getMenuTree, type RoleData, type MenuData } from '@/api/system'

const loading = ref(false)
const roleList = ref<RoleData[]>([])
const total = ref(0)
const queryParams = reactive({ page: 1, pageSize: 10 })

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const defaultForm: RoleData = { name: '', code: '', description: '', sort: 0, status: 'active' }
const form = reactive<RoleData>({ ...defaultForm })
const rules: FormRules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
}

// Menu assignment
const menuDialogVisible = ref(false)
const menuSubmitLoading = ref(false)
const menuTree = ref<MenuData[]>([])
const menuTreeRef = ref<InstanceType<typeof ElTree>>()
const currentRoleId = ref<number>(0)
const checkedMenuIds = ref<number[]>([])

onMounted(() => {
  loadData()
  loadMenuTree()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getRolePage(queryParams)
    if (res.data.code === 200) {
      roleList.value = res.data.data?.records || []
      total.value = res.data.data?.total || 0
    }
  } catch { } finally {
    loading.value = false
  }
}

async function loadMenuTree() {
  try {
    const res = await getMenuTree()
    if (res.data.code === 200) {
      menuTree.value = res.data.data || []
    }
  } catch { }
}

function handleAdd() {
  isEdit.value = false
  Object.assign(form, { ...defaultForm })
  dialogVisible.value = true
}

function handleEdit(row: RoleData) {
  isEdit.value = true
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

async function handleDelete(row: RoleData) {
  try {
    const res = await deleteRole(row.id!)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    }
  } catch { }
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const res = isEdit.value ? await updateRole(form) : await createRole(form)
    if (res.data.code === 200) {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      loadData()
    }
  } catch { } finally {
    submitLoading.value = false
  }
}

function handleAssignMenu(row: RoleData) {
  currentRoleId.value = row.id!
  checkedMenuIds.value = []
  menuDialogVisible.value = true
  // Load assigned menus on next tick after dialog opens
  setTimeout(() => {
    menuTreeRef.value?.setCheckedKeys([])
  }, 100)
}

async function handleAssignSubmit() {
  const checkedKeys = menuTreeRef.value?.getCheckedKeys(false) || []
  const halfCheckedKeys = menuTreeRef.value?.getHalfCheckedKeys() || []
  const allKeys = [...checkedKeys, ...halfCheckedKeys]

  menuSubmitLoading.value = true
  try {
    const res = await assignRoleMenus(currentRoleId.value, allKeys as number[])
    if (res.data.code === 200) {
      ElMessage.success('分配菜单成功')
      menuDialogVisible.value = false
    }
  } catch { } finally {
    menuSubmitLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.role-page {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: 600;
  }
  .pagination-wrap {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;
  }
  .menu-dialog-body {
    max-height: 400px;
    overflow-y: auto;
  }
}
</style>
