<template>
  <div class="profile-page">
    <el-card shadow="never">
      <template #header>
        <span>个人信息</span>
      </template>

      <div class="profile-avatar-section">
        <el-avatar :size="80" icon="UserFilled" />
        <div class="profile-name">
          <h3>{{ profile.name }}</h3>
          <p>{{ profile.position }}</p>
        </div>
      </div>

      <el-descriptions :column="2" border class="profile-desc">
        <el-descriptions-item label="姓名" :span="1">{{ profile.name }}</el-descriptions-item>
        <el-descriptions-item label="工号" :span="1">{{ profile.empNo }}</el-descriptions-item>
        <el-descriptions-item label="部门" :span="1">{{ profile.department }}</el-descriptions-item>
        <el-descriptions-item label="职位" :span="1">{{ profile.position }}</el-descriptions-item>
        <el-descriptions-item label="手机号" :span="1">{{ profile.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱" :span="1">{{ profile.email }}</el-descriptions-item>
        <el-descriptions-item label="入职日期" :span="1">{{ profile.hireDate }}</el-descriptions-item>
        <el-descriptions-item label="学历" :span="1">{{ profile.education }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { UserFilled } from '@element-plus/icons-vue'

const profile = {
  name: '张三',
  empNo: 'EMP2023001',
  department: '技术部',
  position: '高级前端工程师',
  phone: '138****8888',
  email: 'zhangsan@company.com',
  hireDate: '2023-03-15',
  education: '本科',
}
</script>

<style scoped lang="scss">
.profile-page {
  .profile-avatar-section {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 24px;
    padding-bottom: 20px;
    border-bottom: 1px solid #ebeef5;

    h3 {
      margin: 0 0 4px;
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }

    p {
      margin: 0;
      font-size: 14px;
      color: #909399;
    }
  }
}
</style>
