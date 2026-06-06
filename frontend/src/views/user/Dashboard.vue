<template>
  <div class="dashboard-page">
    <!-- Welcome Card -->
    <el-card shadow="never" class="welcome-card">
      <div class="welcome-content">
        <div class="welcome-avatar">
          <el-avatar :size="64" icon="UserFilled" />
        </div>
        <div class="welcome-info">
          <h2 class="welcome-title">欢迎回来，{{ employee.name }}</h2>
          <p class="welcome-desc">工号：{{ employee.empNo }} | 部门：{{ employee.department }} | 职位：{{ employee.position }}</p>
        </div>
      </div>
    </el-card>

    <!-- Stat Cards -->
    <el-row :gutter="16" class="stat-row">
      <el-col :xs="12" :sm="6" v-for="stat in stats" :key="stat.label">
        <el-card shadow="never" class="stat-card">
          <div class="stat-value" :style="{ color: stat.color }">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
          <el-icon class="stat-icon" :style="{ color: stat.color }" :size="32">
            <component :is="stat.icon" />
          </el-icon>
        </el-card>
      </el-col>
    </el-row>

    <!-- Recent Activities -->
    <el-card shadow="never">
      <template #header>
        <span>最近动态</span>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="(item, index) in activities"
          :key="index"
          :timestamp="item.time"
          :type="item.type"
        >
          {{ item.content }}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { UserFilled, Clock, EditPen, Money, Reading } from '@element-plus/icons-vue'

const employee = {
  name: '张三',
  empNo: 'EMP2023001',
  department: '技术部',
  position: '高级工程师',
}

const stats = [
  { label: '本月出勤', value: '22天', color: '#67c23a', icon: Clock },
  { label: '待审批请假', value: '1条', color: '#e6a23c', icon: EditPen },
  { label: '本月工资', value: '¥15,800', color: '#409eff', icon: Money },
  { label: '即将培训', value: '2项', color: '#909399', icon: Reading },
]

const activities = [
  { content: '提交了5月份请假申请', time: '2025-05-27 09:30', type: 'primary' },
  { content: '完成4月份绩效评估', time: '2025-05-20 14:00', type: 'success' },
  { content: '查看4月份工资单', time: '2025-05-15 10:00', type: 'info' },
  { content: '参加了"Vue.js 3进阶"培训', time: '2025-05-10 16:30', type: 'warning' },
  { content: '续签劳动合同(2025-2028)', time: '2025-05-01 09:00', type: 'danger' },
]
</script>

<style scoped lang="scss">
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.welcome-card {
  .welcome-content {
    display: flex;
    align-items: center;
    gap: 20px;
  }

  .welcome-title {
    margin: 0 0 4px;
    font-size: 20px;
    font-weight: 600;
    color: #303133;
  }

  .welcome-desc {
    margin: 0;
    font-size: 14px;
    color: #909399;
  }
}

.stat-row {
  .stat-card {
    position: relative;
    overflow: hidden;

    .stat-value {
      font-size: 28px;
      font-weight: 700;
    }

    .stat-label {
      margin-top: 4px;
      font-size: 13px;
      color: #909399;
    }

    .stat-icon {
      position: absolute;
      right: 16px;
      top: 16px;
      opacity: 0.3;
    }
  }
}
</style>
