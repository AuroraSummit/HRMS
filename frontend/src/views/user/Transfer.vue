<template>
  <div class="transfer-page">
    <el-card shadow="never">
      <template #header><span>我的异动记录</span></template>
      <el-table :data="records" stripe border v-loading="loading" style="width:100%">
        <el-table-column label="异动类型" width="100">
          <template #default="{ row }">{{ typeLabel(row.transferType) }}</template>
        </el-table-column>
        <el-table-column label="原部门→新部门" min-width="160">
          <template #default="{ row }">{{ row.fromDeptId }} → {{ row.toDeptId }}</template>
        </el-table-column>
        <el-table-column label="原岗位→新岗位" min-width="160">
          <template #default="{ row }">{{ row.fromPositionId }} → {{ row.toPositionId }}</template>
        </el-table-column>
        <el-table-column prop="transferDate" label="异动日期" width="110" />
        <el-table-column prop="reason" label="原因" min-width="150" show-overflow-tooltip />
        <el-table-column label="状态" width="90">
          <template #default="{ row }"><el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag></template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getTransferPage, type TransferData } from '@/api/transfer'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const records = ref<TransferData[]>([])
const loading = ref(false)

onMounted(() => { loadData() })
async function loadData() {
  loading.value = true
  try {
    const empId = userStore.userInfo?.empId || 0
    const res = await getTransferPage({ page: 1, pageSize: 100, empId })
    if (res.data.code === 200) records.value = res.data.data.records || []
  } catch { } finally { loading.value = false }
}
function typeLabel(v: number) { const m: Record<number, string> = { 1: '晋升', 2: '平调', 3: '降职', 4: '转岗', 5: '其他' }; return m[v] || '未知' }
function statusTag(v: number) { const m: Record<number, string> = { 0: 'warning', 1: 'success', 2: 'danger' }; return m[v] || 'info' }
function statusLabel(v: number) { const m: Record<number, string> = { 0: '待审批', 1: '已通过', 2: '已驳回' }; return m[v] || '未知' }
</script>
