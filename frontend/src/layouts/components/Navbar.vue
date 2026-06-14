<template>
  <div class="navbar">
    <div class="navbar-left">
      <el-icon
        class="navbar-collapse-btn"
        :size="20"
        @click="appStore.toggleSidebar()"
      >
        <Fold v-if="!appStore.sidebarCollapsed" />
        <Expand v-else />
      </el-icon>
      <el-breadcrumb class="navbar-breadcrumb" separator="/">
        <el-breadcrumb-item
          v-for="item in breadcrumbs"
          :key="item.path"
          :to="item.path"
        >
          {{ item.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="navbar-right">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="navbar-user">
          <el-avatar
            :size="32"
            :src="userStore.userInfo?.avatar || undefined"
            icon="UserFilled"
          />
          <span class="navbar-user-name">
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
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import type { RouteRecordRaw } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { changePassword } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

const breadcrumbs = computed(() => {
  const matched = route.matched.filter((r: RouteRecordRaw) => r.meta?.title)
  return matched.map((r: RouteRecordRaw) => ({
    path: r.path || '/',
    title: (r.meta?.title as string) || '',
  }))
})

function handleCommand(command: string) {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'password':
      pwdDialogVisible.value = true
      break
    case 'logout':
      userStore.logout()
      break
  }
}

// --- Password Change Dialog ---
const pwdDialogVisible = ref(false)
const pwdLoading = ref(false)
const pwdForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })
const formRef = ref<FormInstance>()
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
.navbar {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  z-index: 10;

  .navbar-left {
    display: flex;
    align-items: center;
    gap: 12px;

    .navbar-collapse-btn {
      cursor: pointer;
      color: #606266;
      &:hover {
        color: var(--el-color-primary);
      }
    }

    .navbar-breadcrumb {
      :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
        font-weight: 500;
        color: #303133;
      }
    }
  }

  .navbar-right {
    .navbar-user {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;

      .navbar-user-name {
        font-size: 14px;
        color: #303133;
      }

      .el-icon {
        color: #909399;
      }
    }
  }
}
</style>
