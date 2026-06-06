<template>
  <div class="profile-page">
    <el-card shadow="never" class="mb-20">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>
      <div class="profile-avatar-section">
        <el-avatar :size="80" icon="UserFilled" />
        <div class="profile-name">
          <h3>{{ userInfo.realName }}</h3>
          <p>{{ userInfo.roles?.includes('admin') ? '系统管理员' : '员工' }}</p>
        </div>
      </div>
      <el-descriptions :column="2" border class="profile-desc">
        <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ userInfo.realName }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userInfo.email || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ userInfo.phone || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ userInfo.roles?.join(', ') || '无' }}</el-descriptions-item>
        <el-descriptions-item label="权限">{{ userInfo.permissions?.includes('*:*:*') ? '全部权限' : (userInfo.permissions?.join(', ') || '无') }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { UserFilled } from '@element-plus/icons-vue'

const userStore = useUserStore()
const userInfo = userStore.userInfo || { username: '', realName: '', email: '', phone: '', roles: [] as string[], permissions: [] as string[] }
</script>

<style scoped lang="scss">
.profile-page {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: 600;
  }
  .profile-avatar-section {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 24px;
    padding-bottom: 20px;
    border-bottom: 1px solid #ebeef5;
    h3 { margin: 0 0 4px; font-size: 18px; font-weight: 600; color: #303133; }
    p { margin: 0; font-size: 14px; color: #909399; }
  }
}
</style>
