<template>
  <div class="menu-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" @click="handleAddRoot">
            <el-icon><Plus /></el-icon>新增根菜单
          </el-button>
        </div>
      </template>

      <el-tree
        :data="menuTree"
        :props="{ label: 'title', children: 'children' }"
        node-key="id"
        :default-expand-all="true"
        highlight-current
      >
        <template #default="{ node, data }">
          <span class="tree-node">
            <el-icon v-if="data.icon"><component :is="data.icon" /></el-icon>
            <span class="tree-label">{{ node.label }}</span>
            <el-tag size="small" type="info" class="tree-permission">{{ data.permission }}</el-tag>
            <el-tag size="small" :type="data.status === 'active' ? 'success' : 'info'" class="tree-status">
              {{ data.status === 'active' ? '启用' : '停用' }}
            </el-tag>
            <span class="tree-actions">
              <el-button link type="primary" size="small" @click.stop="handleAddChild(data)">新增</el-button>
              <el-button link type="primary" size="small" @click.stop="handleEdit(data)">编辑</el-button>
              <el-popconfirm title="确认删除此菜单？" @confirm.stop="handleDelete(data)">
                <template #reference><el-button link type="danger" size="small" @click.stop>删除</el-button></template>
              </el-popconfirm>
            </span>
          </span>
        </template>
      </el-tree>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑菜单' : '新增菜单'" width="550px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="菜单名称" prop="title"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="路由名称" prop="name"><el-input v-model="form.name" placeholder="如: SystemMenu" /></el-form-item>
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="路由路径" prop="path"><el-input v-model="form.path" placeholder="如: /system/menu" /></el-form-item></el-col><el-col :span="12"><el-form-item label="组件路径" prop="component"><el-input v-model="form.component" placeholder="如: /system/Menu.vue" /></el-form-item></el-col></el-row>
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="上级菜单" prop="parentId"><el-tree-select v-model="form.parentId" :data="menuTree" :props="{label:'title',value:'id',children:'children'}" placeholder="无(根菜单)" clearable check-strictly /></el-form-item></el-col><el-col :span="12"><el-form-item label="图标" prop="icon"><el-select v-model="form.icon" placeholder="选择图标" filterable style="width:100%"><el-option v-for="icon in iconList" :key="icon" :label="icon" :value="icon" /></el-select></el-form-item></el-col></el-row>
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="排序" prop="sort"><el-input-number v-model="form.sort" :min="0" :max="999" style="width:100%" /></el-form-item></el-col><el-col :span="12"><el-form-item label="类型" prop="type"><el-select v-model="form.type" style="width:100%"><el-option label="目录" value="directory" /><el-option label="菜单" value="menu" /><el-option label="按钮" value="button" /></el-select></el-form-item></el-col></el-row>
        <el-form-item label="权限标识" prop="permission"><el-input v-model="form.permission" placeholder="如: system:menu:list" /></el-form-item>
        <el-form-item label="状态" prop="status"><el-radio-group v-model="form.status"><el-radio label="active">正常</el-radio><el-radio label="disabled">停用</el-radio></el-radio-group></el-form-item>
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
import { getMenuTree, createMenu, updateMenu, deleteMenu, type MenuData } from '@/api/system'

const menuTree = ref<MenuData[]>([]); const dialogVisible = ref(false); const isEdit = ref(false); const submitLoading = ref(false); const formRef = ref<FormInstance>()

const iconList = ['HomeFilled', 'OfficeBuilding', 'UserFilled', 'Clock', 'Money', 'DataAnalysis', 'Reading', 'Document', 'Tools', 'Menu', 'Collection', 'List', 'Avatar', 'Briefcase', 'Setting', 'Search', 'Plus', 'EditPen', 'Delete', 'Download', 'Upload', 'Message', 'ChatDotSquare', 'Tickets', 'Coin', 'CopyDocument', 'DataBoard', 'DocumentAdd', 'DocumentCopy', 'Folder', 'Files']

const defaultForm: MenuData = { parentId: 0, name: '', path: '', component: '', icon: '', title: '', sort: 0, permission: '', type: 'menu', visible: 1, status: 'active' }
const form = reactive<MenuData>({ ...defaultForm })
const rules: FormRules = { title: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }] }

onMounted(() => { loadData() })
async function loadData() {
  try { const res = await getMenuTree(); if (res.data.code === 200) menuTree.value = res.data.data || [] } catch { }
}
function handleAddRoot() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleAddChild(data: MenuData) { isEdit.value = false; Object.assign(form, { ...defaultForm, parentId: data.id || 0 }); dialogVisible.value = true }
function handleEdit(data: MenuData) { isEdit.value = true; Object.assign(form, data); dialogVisible.value = true }
async function handleDelete(data: MenuData) { try { const res = await deleteMenu(data.id!); if (res.data.code === 200) { ElMessage.success('删除成功'); loadData() } } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return; submitLoading.value = true
  try { const res = isEdit.value ? await updateMenu(form) : await createMenu(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { } finally { submitLoading.value = false }
}
</script>

<style scoped lang="scss">
.menu-page {
  .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; }
  .tree-node { display: flex; align-items: center; gap: 8px; width: 100%; padding-right: 16px;
    .tree-label { font-size: 14px; font-weight: 500; }
    .tree-permission { font-size: 11px; }
    .tree-status { font-size: 11px; }
    .tree-actions { margin-left: auto; display: none; gap: 4px; white-space: nowrap; }
    &:hover .tree-actions { display: inline-flex; }
  }
}
</style>
