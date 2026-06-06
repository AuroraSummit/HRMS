<template>
  <div class="attendance-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>我的考勤</span>
          <el-date-picker
            v-model="month"
            type="month"
            placeholder="选择月份"
            value-format="YYYY-MM"
            style="width: 160px"
            @change="loadData"
          />
        </div>
      </template>

      <el-table :data="records" stripe border style="width: 100%">
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="checkIn" label="上班打卡" width="120" />
        <el-table-column prop="checkOut" label="下班打卡" width="120" />
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const month = ref('2025-05')

const records = [
  { date: '2025-05-26', checkIn: '08:55', checkOut: '18:05', status: '正常', remark: '' },
  { date: '2025-05-23', checkIn: '08:58', checkOut: '18:02', status: '正常', remark: '' },
  { date: '2025-05-22', checkIn: '08:50', checkOut: '17:58', status: '正常', remark: '' },
  { date: '2025-05-21', checkIn: '09:15', checkOut: '18:10', status: '迟到', remark: '交通拥堵' },
  { date: '2025-05-20', checkIn: '08:45', checkOut: '17:30', status: '早退', remark: '个人事务' },
  { date: '2025-05-19', checkIn: '08:55', checkOut: '18:00', status: '正常', remark: '' },
  { date: '2025-05-16', checkIn: '08:50', checkOut: '18:05', status: '正常', remark: '' },
  { date: '2025-05-15', checkIn: '--:--', checkOut: '--:--', status: '缺勤', remark: '病假' },
  { date: '2025-05-14', checkIn: '08:55', checkOut: '18:00', status: '正常', remark: '' },
  { date: '2025-05-13', checkIn: '08:48', checkOut: '18:02', status: '正常', remark: '' },
]

function statusTag(status: string) {
  const map: Record<string, string> = { '正常': 'success', '迟到': 'warning', '早退': 'warning', '缺勤': 'danger' }
  return map[status] || 'info'
}

function loadData() {
  // In real app, fetch from API with month filter
}
</script>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  font-weight: 600;
}
</style>
