<template>
  <div class="performance-page">
    <el-card shadow="never">
      <template #header>
        <span>我的绩效</span>
      </template>

      <el-table :data="records" stripe border style="width: 100%">
        <el-table-column prop="planName" label="考核计划" min-width="160" />
        <el-table-column prop="period" label="考核周期" width="160" />
        <el-table-column prop="score" label="评分" width="80" align="center" />
        <el-table-column prop="grade" label="等级" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="gradeTag(row.grade)" size="small">{{ row.grade }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '已完成' ? 'success' : 'warning'" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comment" label="评语" min-width="200" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
const records = [
  { planName: '2025年Q1绩效考核', period: '2025-01 至 2025-03', score: 92, grade: 'A', status: '已完成', comment: '表现优秀，项目交付及时' },
  { planName: '2024年度绩效考核', period: '2024-01 至 2024-12', score: 88, grade: 'A', status: '已完成', comment: '年度表现优异，团队贡献突出' },
  { planName: '2024年Q3绩效考核', period: '2024-07 至 2024-09', score: 85, grade: 'B', status: '已完成', comment: '工作认真负责，技术能力持续提升' },
  { planName: '2024年Q2绩效考核', period: '2024-04 至 2024-06', score: 78, grade: 'B', status: '已完成', comment: '基本完成目标，部分项目需改进' },
  { planName: '2024年Q1绩效考核', period: '2024-01 至 2024-03', score: 82, grade: 'B', status: '已完成', comment: '工作态度积极，团队协作良好' },
]

function gradeTag(grade: string) {
  const map: Record<string, string> = { S: 'danger', A: 'success', B: 'primary', C: 'warning', D: 'info' }
  return map[grade] || 'info'
}
</script>
