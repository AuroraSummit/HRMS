<template>
  <div class="probation-page">
    <el-card shadow="never">
      <template #header><span>我的转正信息</span></template>
      <el-table :data="records" stripe border v-loading="loading" style="width:100%">
        <el-table-column prop="probationStart" label="试用开始" width="110" />
        <el-table-column prop="probationEnd" label="试用结束" width="110" />
        <el-table-column prop="actualEnd" label="实际转正" width="110" />
        <el-table-column prop="mentorName" label="导师" width="100" />
        <el-table-column label="综合评分" width="90">
          <template #default="{ row }">{{ row.score ?? '-' }}</template>
        </el-table-column>
        <el-table-column label="转正结果" width="90">
          <template #default="{ row }"><el-tag :type="resultTag(row.result)" size="small">{{ resultLabel(row.result) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template #default="{ row }"><el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="detailVisible" title="转正详情" width="600px">
      <el-descriptions :column="2" border v-if="detail">
        <el-descriptions-item label="试用开始">{{ detail.probationStart }}</el-descriptions-item>
        <el-descriptions-item label="试用结束">{{ detail.probationEnd }}</el-descriptions-item>
        <el-descriptions-item label="实际转正">{{ detail.actualEnd || '-' }}</el-descriptions-item>
        <el-descriptions-item label="导师">{{ detail.mentorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="综合评分">{{ detail.score ?? '-' }}</el-descriptions-item>
        <el-descriptions-item label="转正结果"><el-tag :type="resultTag(detail.result)" size="small">{{ resultLabel(detail.result) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="自评总结" :span="2">{{ detail.selfEvaluation || '-' }}</el-descriptions-item>
        <el-descriptions-item label="导师评价" :span="2">{{ detail.mentorEvaluation || '-' }}</el-descriptions-item>
        <el-descriptions-item label="主管评价" :span="2">{{ detail.managerEvaluation || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailVisible=false">关闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getProbationPage, type ProbationData } from '@/api/probation'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const records = ref<ProbationData[]>([])
const loading = ref(false)
const detailVisible = ref(false)
const detail = ref<ProbationData | null>(null)

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try {
    const empId = userStore.userInfo?.empId || 0
    const res = await getProbationPage({ page: 1, pageSize: 100, empId })
    if (res.data.code === 200) records.value = res.data.data.records || []
  } catch { } finally { loading.value = false }
}
function viewDetail(row: ProbationData) { detail.value = row; detailVisible.value = true }
function resultTag(v: number) { const m: Record<number, string> = { 1: 'success', 2: 'warning', 3: 'danger' }; return m[v] || 'info' }
function resultLabel(v: number) { const m: Record<number, string> = { 1: '通过', 2: '延期', 3: '不通过' }; return m[v] || '未知' }
function statusTag(v: number) { const m: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }; return m[v] || 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待评估', 1: '已提交', 2: '已通过', 3: '已驳回' }; return m[v] || '未知' }
</script>
