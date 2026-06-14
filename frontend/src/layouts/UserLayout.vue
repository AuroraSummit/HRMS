<template>
  <div class="user-layout">
    <header class="user-header">
      <div class="header-inner">
        <div class="header-left">
          <div class="header-logo">
            <span class="logo-text">HRMS</span>
            <span class="logo-subtitle">员工门户</span>
          </div>
        </div>
        <div class="header-center">
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            class="header-menu"
            router
            @select="handleMenuSelect"
          >
            <el-menu-item index="/user/dashboard">
              <el-icon><HomeFilled /></el-icon>我的首页
            </el-menu-item>
            <el-menu-item index="/user/profile">
              <el-icon><User /></el-icon>个人信息
            </el-menu-item>
            <el-menu-item index="/user/attendance">
              <el-icon><Clock /></el-icon>我的考勤
            </el-menu-item>
            <el-menu-item index="/user/leave">
              <el-icon><EditPen /></el-icon>我的请假
            </el-menu-item>
            <el-menu-item index="/user/salary">
              <el-icon><Money /></el-icon>我的工资
            </el-menu-item>
            <el-menu-item index="/user/performance">
              <el-icon><DataAnalysis /></el-icon>我的绩效
            </el-menu-item>
            <el-menu-item index="/user/training">
              <el-icon><Reading /></el-icon>我的培训
            </el-menu-item>
            <el-menu-item index="/user/contract">
              <el-icon><Document /></el-icon>我的合同
            </el-menu-item>
            <el-menu-item index="/user/transfer">
              <el-icon><Sort /></el-icon>我的异动
            </el-menu-item>
            <el-menu-item index="/user/probation">
              <el-icon><Medal /></el-icon>我的转正
            </el-menu-item>
            <el-menu-item index="/user/notice">
              <el-icon><Bell /></el-icon>通知公告
            </el-menu-item>
          </el-menu>
        </div>
        <div class="header-right">
          <el-dropdown trigger="click" @command="handleCommand">
            <span class="header-user">
              <el-avatar :size="32" icon="UserFilled" />
              <span class="header-user-name">
                {{ userStore.userInfo?.realName || userStore.userInfo?.username || '用户' }}
              </span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>个人信息
                </el-dropdown-item>
                <el-dropdown-item command="password">
                  <el-icon><Lock /></el-icon>修改密码
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>
    <main class="user-main">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" :key="$route.fullPath" />
        </transition>
      </router-view>
    </main>
  </div>

  <!-- Password Change Dialog -->
  <el-dialog v-model="pwdDialogVisible" title="修改密码" width="420px" :close-on-click-modal="false">
    <el-form ref="formRef" :model="pwdForm" :rules="pwdRules" label-width="100px">
      <el-form-item label="当前密码" prop="oldPassword">
        <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入当前密码" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入新密码（至少6位）" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="pwdDialogVisible=false">取消</el-button>
      <el-button type="primary" :loading="pwdLoading" @click="handlePwdSubmit">确定修改</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { changePassword } from '@/api/auth'
import {
  HomeFilled, User, Clock, EditPen, Money, DataAnalysis,
  Reading, Document, ArrowDown, Lock, SwitchButton,
  Sort, Medal, Bell,
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const pwdDialogVisible = ref(false)
const pwdLoading = ref(false)
const pwdForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })
const pwdRules: FormRules = {
  oldPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_rule: any, value: string, callback: any) => {
        if (value !== pwdForm.value.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

const activeMenu = computed(() => {
  return route.path
})

function handleMenuSelect(index: string) {
  router.push(index)
}

function handleCommand(command: string) {
  switch (command) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'password':
      pwdForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
      pwdDialogVisible.value = true
      break
    case 'logout':
      userStore.logout()
      break
  }
}

async function handlePwdSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  pwdLoading.value = true
  try {
    const res = await changePassword({
      oldPassword: pwdForm.value.oldPassword,
      newPassword: pwdForm.value.newPassword,
    })
    if (res.data.code === 200) {
      ElMessage.success('密码修改成功，请重新登录')
      pwdDialogVisible.value = false
      userStore.logout()
    }
  } catch {
    ElMessage.error('修改失败')
  } finally {
    pwdLoading.value = false
  }
}
</script>

<style scoped lang="scss">
.user-layout {
  min-height: 100vh;
  background-color: #f0f2f5;
  display: flex;
  flex-direction: column;
}

.user-header {
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 60px;
  padding: 0 16px;
}

.header-left {
  flex-shrink: 0;
  margin-right: 24px;
}

.header-logo {
  display: flex;
  align-items: center;
  gap: 8px;

  .logo-text {
    font-size: 22px;
    font-weight: 700;
    color: var(--el-color-primary);
  }

  .logo-subtitle {
    font-size: 13px;
    color: #909399;
    padding-left: 8px;
    border-left: 1px solid #dcdfe6;
  }
}

.header-center {
  flex: 1;
  overflow-x: auto;
}

.header-menu {
  border-bottom: none !important;

  .el-menu-item {
    height: 60px;
    line-height: 60px;
    font-size: 13px;
    padding: 0 12px;
  }
}

.header-right {
  flex-shrink: 0;
  margin-left: 16px;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.2s;

  &:hover {
    background: #f5f7fa;
  }

  .header-user-name {
    font-size: 14px;
    color: #303133;
  }

  .el-icon {
    color: #909399;
  }
}

.user-main {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 16px auto;
  padding: 0 16px;
  box-sizing: border-box;
}
</style>
