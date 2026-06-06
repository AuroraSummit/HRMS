<template>
  <div class="contract-page">
    <!-- Renewal Alert -->
    <el-alert
      v-if="showRenewalAlert"
      title="合同即将到期提醒"
      :description="renewalAlertText"
      type="warning"
      show-icon
      :closable="true"
      class="renewal-alert"
    />

    <!-- Renewal Dialog -->
    <el-dialog v-model="renewDialogVisible" title="合同续签" width="420px" :close-on-click-modal="false">
      <el-form :model="renewForm" label-width="80px" :rules="renewRules" ref="renewFormRef">
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="renewForm.startDate" type="date" placeholder="选择续签开始日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="renewForm.endDate" type="date" placeholder="选择续签结束日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="续签说明" prop="remark">
          <el-input v-model="renewForm.remark" type="textarea" :rows="3" placeholder="可选" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="renewDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="renewSubmitting" @click="submitRenew">确认续签</el-button>
      </template>
    </el-dialog>

    <el-card shadow="never">
      <template #header>
        <span>我的合同</span>
      </template>

      <el-table :data="records" stripe border v-loading="loading" style="width: 100%">
        <el-table-column label="合同类型" width="120">
          <template #default="{ row }">
            {{ contractTypeLabel(row.contractType) }}
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="signDate" label="签订日期" width="120" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="140" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="viewContract(row)">查看合同</el-button>
            <el-button v-if="row.status === 1" link type="warning" size="small" @click="handleRenew(row)">续签</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>

  <!-- Contract Detail Dialog -->
  <el-dialog v-model="detailDialogVisible" title="合同详情" width="500px" :close-on-click-modal="false">
    <el-descriptions :column="1" border v-if="contractDetail">
      <el-descriptions-item label="合同类型">{{ contractTypeLabel(contractDetail.contractType) }}</el-descriptions-item>
      <el-descriptions-item label="开始日期">{{ contractDetail.startDate }}</el-descriptions-item>
      <el-descriptions-item label="结束日期">{{ contractDetail.endDate }}</el-descriptions-item>
      <el-descriptions-item label="签订日期">{{ contractDetail.signDate }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="statusTag(contractDetail.status)" size="small">{{ statusLabel(contractDetail.status) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="备注">{{ contractDetail.remark || '无' }}</el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button @click="detailDialogVisible=false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElForm } from 'element-plus'
import { getContractPage, renewContract } from '@/api/contract'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const records = ref<any[]>([])
const loading = ref(false)
const contractDetail = ref<any>(null)
const detailDialogVisible = ref(false)

// --- Renew dialog state ---
const renewDialogVisible = ref(false)
const renewSubmitting = ref(false)
const renewFormRef = ref<InstanceType<typeof ElForm>>()
const renewTarget = ref<any>(null)

const renewForm = ref({
  startDate: '',
  endDate: '',
  remark: '',
})

const renewRules = {
  startDate: [{ required: true, message: '请选择续签开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择续签结束日期', trigger: 'change' }],
}

const showRenewalAlert = computed(() => {
  return records.value.some((r) => r.status === 1)
})

const renewalAlertText = computed(() => {
  const nearExpiry = records.value
    .filter((r) => r.status === 1 && r.endDate)
    .sort((a, b) => (a.endDate || '').localeCompare(b.endDate || ''))
  if (nearExpiry.length > 0) {
    const first = nearExpiry[0]
    return `您的劳动合同（${contractTypeLabel(first.contractType)}）将于 ${first.endDate} 到期，请及时联系人力资源部办理续签手续。`
  }
  return '您有生效中的合同。'
})

function contractTypeLabel(v: number): string {
  const m: Record<number, string> = { 1: '正式', 2: '试用', 3: '实习', 4: '劳务', 5: '兼职' }
  return m[v] || '未知'
}

function statusTag(v: number): string {
  const m: Record<number, string> = { 0: 'info', 1: 'success', 2: 'danger', 3: 'warning' }
  return m[v] || 'info'
}

function statusLabel(v: number): string {
  const m: Record<number, string> = { 0: '待签', 1: '生效', 2: '到期', 3: '解约' }
  return m[v] || '未知'
}

async function loadData() {
  loading.value = true
  try {
    const empId = userStore.userInfo?.empId || 0
    const res = await getContractPage({ page: 1, pageSize: 100, empId })
    if (res.data.code === 200) {
      records.value = res.data.data.records || res.data.data || []
    }
  } catch {
    // handled by request interceptor
  } finally {
    loading.value = false
  }
}

function viewContract(row: any) {
  if (row.fileUrl) {
    window.open(row.fileUrl, '_blank')
  } else {
    // Show contract detail dialog instead
    contractDetail.value = row
    detailDialogVisible.value = true
  }
}

function handleRenew(row: any) {
  renewTarget.value = row
  renewForm.value = { startDate: '', endDate: '', remark: '' }
  renewDialogVisible.value = true
}

async function submitRenew() {
  if (!renewTarget.value) return
  const valid = await renewFormRef.value?.validate().catch(() => false)
  if (!valid) return

  renewSubmitting.value = true
  try {
    const res = await renewContract(renewTarget.value.id, {
      startDate: renewForm.value.startDate,
      endDate: renewForm.value.endDate,
      remark: renewForm.value.remark,
    })
    if (res.data.code === 200) {
      ElMessage.success('续签申请已提交')
      renewDialogVisible.value = false
      await loadData()
    } else {
      ElMessage.error(res.data.message || '续签失败')
    }
  } catch {
    ElMessage.error('续签请求失败，请稍后重试')
  } finally {
    renewSubmitting.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.renewal-alert {
  margin-bottom: 16px;
}
</style>
