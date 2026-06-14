<template>
  <div class="notice-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>通知公告管理</span>
          <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>发布公告</el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">{{ noticeTypeLabel(row.noticeType) }}</template>
        </el-table-column>
        <el-table-column label="优先级" width="80">
          <template #default="{ row }"><el-tag :type="priorityTag(row.priority)" size="small">{{ priorityLabel(row.priority) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="publishDate" label="发布日期" width="110" />
        <el-table-column label="目标角色" width="100">
          <template #default="{ row }">{{ targetLabel(row.targetRole) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }"><el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="70" align="center" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status!==1" type="success" link size="small" @click="publishNotice(row.id,1)">发布</el-button>
            <el-button v-if="row.status===1" type="warning" link size="small" @click="publishNotice(row.id,2)">撤回</el-button>
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)"><template #reference><el-button type="danger" link size="small">删除</el-button></template></el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="title"><el-input v-model="form.title" placeholder="公告标题" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="类型" prop="noticeType"><el-select v-model="form.noticeType" style="width:100%"><el-option label="公司公告" :value="1" /><el-option label="制度通知" :value="2" /><el-option label="活动通知" :value="3" /><el-option label="系统通知" :value="4" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="优先级" prop="priority"><el-select v-model="form.priority" style="width:100%"><el-option label="普通" :value="0" /><el-option label="重要" :value="1" /><el-option label="紧急" :value="2" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="发布日期"><el-date-picker v-model="form.publishDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="失效日期"><el-date-picker v-model="form.expireDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="目标角色"><el-radio-group v-model="form.targetRole"><el-radio label="all">全部</el-radio><el-radio label="admin">管理员</el-radio><el-radio label="employee">员工</el-radio></el-radio-group></el-form-item>
        <el-form-item label="内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="6" placeholder="公告内容..." /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getAdminNoticeList, createNotice, updateNotice, publishNotice as publishNoticeApi, deleteNotice, type NoticeData } from '@/api/notice'

const list = ref<NoticeData[]>([]); const loading = ref(false); const dialogVisible = ref(false); const isEdit = ref(false); const formRef = ref<FormInstance>()
const defaultForm: NoticeData = { title: '', noticeType: 1, priority: 0, targetRole: 'all', status: 0 }
const form = reactive<NoticeData>({ ...defaultForm })
const rules: FormRules = { title: [{ required: true, message: '请输入标题', trigger: 'blur' }] }
const dialogTitle = computed(() => isEdit.value ? '编辑公告' : '发布公告')

onMounted(() => { loadData() })
async function loadData() { loading.value = true; try { const res = await getAdminNoticeList(); if (res.data.code === 200) list.value = res.data.data || [] } catch { } finally { loading.value = false } }
function handleAdd() { isEdit.value = false; Object.assign(form, { ...defaultForm }); dialogVisible.value = true }
function handleEdit(row: NoticeData) { isEdit.value = true; Object.assign(form, JSON.parse(JSON.stringify(row))); dialogVisible.value = true }
async function handleDelete(row: NoticeData) { try { await deleteNotice(row.id!); ElMessage.success('删除成功'); loadData() } catch { } }
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  try { const res = isEdit.value ? await updateNotice(form) : await createNotice(form); if (res.data.code === 200) { ElMessage.success('操作成功'); dialogVisible.value = false; loadData() } } catch { }
}
async function publishNotice(id: number, status: number) { try { await publishNoticeApi(id, status); ElMessage.success(status === 1 ? '已发布' : '已撤回'); loadData() } catch { } }
function noticeTypeLabel(v: number) { const m: Record<number, string> = { 1: '公司公告', 2: '制度通知', 3: '活动通知', 4: '系统通知' }; return m[v] || '未知' }
function priorityTag(v: number) { const m: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'danger' }; return m[v] || 'info' }
function priorityLabel(v: number) { const m: Record<number, string> = { 0: '普通', 1: '重要', 2: '紧急' }; return m[v] || '未知' }
function targetLabel(v: string) { const m: Record<string, string> = { all: '全部', admin: '管理员', employee: '员工' }; return m[v] || v }
function statusTag(v: number) { const m: Record<number, string> = { 0: 'info', 1: 'success', 2: 'warning' }; return m[v] || 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '草稿', 1: '已发布', 2: '已撤回' }; return m[v] || '未知' }
</script>
<style scoped lang="scss">
.notice-page { .card-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; } }
</style>
