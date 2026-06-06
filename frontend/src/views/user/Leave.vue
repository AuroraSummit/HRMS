<template>
  <div class="leave-page">
    <!-- Submit Leave Form -->
    <el-card shadow="never" class="leave-form-card">
      <template #header>
        <span>提交请假</span>
      </template>
      <el-form :model="leaveForm" label-width="100px" size="default">
        <el-form-item label="请假类型">
          <el-select v-model="leaveForm.type" placeholder="请选择请假类型" style="width: 100%">
            <el-option label="年假" value="年假" />
            <el-option label="事假" value="事假" />
            <el-option label="病假" value="病假" />
            <el-option label="婚假" value="婚假" />
            <el-option label="产假" value="产假" />
            <el-option label="丧假" value="丧假" />
          </el-select>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="开始日期">
              <el-date-picker v-model="leaveForm.startDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期">
              <el-date-picker v-model="leaveForm.endDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="请假原因">
          <el-input v-model="leaveForm.reason" type="textarea" :rows="3" placeholder="请输入请假原因" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitLeave">提交申请</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Leave History -->
    <el-card shadow="never" class="leave-history-card">
      <template #header>
        <span>请假记录</span>
      </template>
      <el-table :data="leaveHistory" stripe border style="width: 100%">
        <el-table-column prop="type" label="请假类型" width="100" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="days" label="天数" width="70" align="center" />
        <el-table-column prop="reason" label="原因" min-width="160" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const leaveForm = reactive({
  type: '',
  startDate: '',
  endDate: '',
  reason: '',
})

const leaveHistory = ref([
  { type: '年假', startDate: '2025-04-10', endDate: '2025-04-12', days: 3, reason: '年度休假', status: '已通过' },
  { type: '事假', startDate: '2025-03-05', endDate: '2025-03-05', days: 1, reason: '个人事务', status: '已通过' },
  { type: '病假', startDate: '2025-02-20', endDate: '2025-02-21', days: 2, reason: '身体不适', status: '待审批' },
  { type: '事假', startDate: '2025-01-15', endDate: '2025-01-16', days: 2, reason: '家庭事务', status: '已拒绝' },
])

function statusTag(status: string) {
  const map: Record<string, string> = { '待审批': 'warning', '已通过': 'success', '已拒绝': 'danger' }
  return map[status] || 'info'
}

function submitLeave() {
  if (!leaveForm.type || !leaveForm.startDate || !leaveForm.endDate) {
    ElMessage.warning('请填写完整请假信息')
    return
  }
  ElMessage.success('请假申请已提交')
  leaveHistory.value.unshift({
    type: leaveForm.type,
    startDate: leaveForm.startDate,
    endDate: leaveForm.endDate,
    days: 1,
    reason: leaveForm.reason || '-',
    status: '待审批',
  })
  resetForm()
}

function resetForm() {
  leaveForm.type = ''
  leaveForm.startDate = ''
  leaveForm.endDate = ''
  leaveForm.reason = ''
}
</script>

<style scoped lang="scss">
.leave-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>
