<template>
  <div class="employee-detail">
    <el-card shadow="never" class="mb-20">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>
      <div class="basic-info" v-if="employee">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="工号">{{ employee.empNo }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ employee.name }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ {0:'男',1:'女',2:'未知'}[employee.gender] || employee.gender }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ employee.deptName }}</el-descriptions-item>
          <el-descriptions-item label="岗位">{{ employee.positionName }}</el-descriptions-item>
          <el-descriptions-item label="手机">{{ employee.phone }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ employee.email }}</el-descriptions-item>
          <el-descriptions-item label="入职日期">{{ employee.hireDate }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="employee.status === 'active' ? 'success' : employee.status === 'probation' ? 'warning' : 'danger'" size="small">
              {{ employee.status === 'active' ? '在职' : employee.status === 'probation' ? '试用期' : '离职' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <el-empty v-else description="暂无数据" />
    </el-card>

    <!-- Tab Content -->
    <el-card shadow="never">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="教育经历" name="education">
          <div class="tab-placeholder">教育经历信息</div>
        </el-tab-pane>
        <el-tab-pane label="工作经历" name="work">
          <div class="tab-placeholder">工作经历信息</div>
        </el-tab-pane>
        <el-tab-pane label="合同信息" name="contract">
          <div class="tab-placeholder">合同信息</div>
        </el-tab-pane>
        <el-tab-pane label="考勤记录" name="attendance">
          <div class="tab-placeholder">考勤记录</div>
        </el-tab-pane>
        <el-tab-pane label="薪酬记录" name="salary">
          <div class="tab-placeholder">薪酬记录</div>
        </el-tab-pane>
        <el-tab-pane label="绩效记录" name="performance">
          <div class="tab-placeholder">绩效记录</div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getDetail, type EmployeeData } from '@/api/employee'

const route = useRoute()
const activeTab = ref('education')
const employee = ref<EmployeeData | null>(null)

onMounted(async () => {
  const id = Number(route.params.id)
  if (id && id > 0) {
    try {
      const res = await getDetail(id)
      if (res.data.code === 200) {
        employee.value = res.data.data
      }
    } catch {
      // handled by interceptor
    }
  }
})
</script>

<style scoped lang="scss">
.employee-detail {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: 600;
  }

  .tab-placeholder {
    height: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #909399;
    font-size: 14px;
  }
}
</style>
