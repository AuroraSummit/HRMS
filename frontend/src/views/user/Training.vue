<template>
  <div class="training-page">
    <el-card shadow="never">
      <template #header>
        <span>我的培训</span>
      </template>

      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :md="8" v-for="item in trainingList" :key="item.title" class="training-col">
          <el-card shadow="hover" class="training-card">
            <div class="training-card-header">
              <h4>{{ item.title }}</h4>
              <el-tag :type="statusTag(item.status)" size="small">{{ item.status }}</el-tag>
            </div>
            <p class="training-date">
              <el-icon><Clock /></el-icon>
              {{ item.date }}
            </p>
            <p class="training-desc">{{ item.description }}</p>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { Clock } from '@element-plus/icons-vue'

const trainingList = [
  { title: 'Vue.js 3 进阶实战', date: '2025-06-15 ~ 2025-06-16', status: '未开始', description: '深入学习Vue3组合式API、响应式原理及性能优化技巧。' },
  { title: 'TypeScript 高级用法', date: '2025-05-20 ~ 2025-05-21', status: '进行中', description: '掌握TypeScript泛型、装饰器、类型体操等高级特性。' },
  { title: '团队管理与领导力', date: '2025-04-10 ~ 2025-04-11', status: '已完成', description: '提升团队管理能力，学习高效沟通与目标管理方法。' },
  { title: '数据库优化与设计', date: '2025-03-25 ~ 2025-03-26', status: '已完成', description: 'MySQL性能优化、索引设计及SQL调优实战。' },
  { title: '微服务架构入门', date: '2025-02-18 ~ 2025-02-19', status: '已完成', description: '了解微服务架构设计思想及Spring Cloud基础组件使用。' },
  { title: '安全编程规范', date: '2025-01-08 ~ 2025-01-09', status: '已完成', description: '学习OWASP Top 10安全漏洞及防御措施。' },
]

function statusTag(status: string) {
  const map: Record<string, string> = { '未开始': 'info', '进行中': 'warning', '已完成': 'success' }
  return map[status] || 'info'
}
</script>

<style scoped lang="scss">
.training-col {
  margin-bottom: 16px;
}

.training-card {
  height: 100%;

  .training-card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 8px;
    margin-bottom: 12px;

    h4 {
      margin: 0;
      font-size: 15px;
      font-weight: 600;
      color: #303133;
      line-height: 1.4;
    }
  }

  .training-date {
    font-size: 13px;
    color: #909399;
    margin: 0 0 8px;
    display: flex;
    align-items: center;
    gap: 4px;
  }

  .training-desc {
    font-size: 13px;
    color: #606266;
    margin: 0;
    line-height: 1.6;
  }
}
</style>
