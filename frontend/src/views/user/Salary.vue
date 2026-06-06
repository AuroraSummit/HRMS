<template>
  <div class="salary-page">
    <el-card shadow="never">
      <template #header>
        <span>我的工资</span>
      </template>

      <el-table :data="records" stripe border style="width: 100%">
        <el-table-column prop="month" label="月份" width="100" />
        <el-table-column prop="basicSalary" label="基本工资" width="120" align="right">
          <template #default="{ row }">{{ formatMoney(row.basicSalary) }}</template>
        </el-table-column>
        <el-table-column prop="bonus" label="奖金" width="120" align="right">
          <template #default="{ row }">{{ formatMoney(row.bonus) }}</template>
        </el-table-column>
        <el-table-column prop="deduction" label="扣款" width="120" align="right">
          <template #default="{ row }">{{ formatMoney(row.deduction) }}</template>
        </el-table-column>
        <el-table-column prop="netPay" label="实发工资" width="140" align="right">
          <template #default="{ row }">
            <span style="font-weight: 600; color: #409eff">{{ formatMoney(row.netPay) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '已发放' ? 'success' : 'info'" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="viewDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'

const records = [
  { month: '2025-05', basicSalary: 12000, bonus: 3000, deduction: 1200, netPay: 13800, status: '待发放' },
  { month: '2025-04', basicSalary: 12000, bonus: 2500, deduction: 1100, netPay: 13400, status: '已发放' },
  { month: '2025-03', basicSalary: 12000, bonus: 2000, deduction: 1050, netPay: 12950, status: '已发放' },
  { month: '2025-02', basicSalary: 12000, bonus: 1500, deduction: 1000, netPay: 12500, status: '已发放' },
  { month: '2025-01', basicSalary: 12000, bonus: 5000, deduction: 1300, netPay: 15700, status: '已发放' },
]

function formatMoney(val: number) {
  return '¥' + val.toLocaleString()
}

function viewDetail(row: any) {
  ElMessage.info(`查看 ${row.month} 工资详情`)
}
</script>
