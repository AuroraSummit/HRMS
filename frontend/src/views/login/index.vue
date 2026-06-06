<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-left">
        <div class="login-brand">
          <div class="brand-icon">
            <el-icon :size="48"><Avatar /></el-icon>
          </div>
          <h1 class="brand-title">HRMS</h1>
          <p class="brand-subtitle">人力资源管理系统</p>
          <p class="brand-desc">高效管理 · 智能决策 · 卓越体验</p>
        </div>
      </div>
      <div class="login-right">
        <h2 class="login-title">欢迎登录</h2>
        <p class="login-desc">请输入您的账号和密码</p>
        <el-form
          ref="formRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          size="large"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              class="login-btn"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { User, Lock, Avatar } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)
const rememberMe = ref(false)

interface LoginForm {
  username: string
  password: string
}

const loginForm = reactive<LoginForm>({
  username: '',
  password: '',
})

const loginRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, message: '密码长度至少5位', trigger: 'blur' },
  ],
}

onMounted(() => {
  const savedUsername = localStorage.getItem('remembered_username')
  const savedPassword = localStorage.getItem('remembered_password')
  if (savedUsername) {
    loginForm.username = savedUsername
    loginForm.password = savedPassword || ''
    rememberMe.value = true
  }
})

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  if (rememberMe.value) {
    localStorage.setItem('remembered_username', loginForm.username)
    localStorage.setItem('remembered_password', loginForm.password)
  } else {
    localStorage.removeItem('remembered_username')
    localStorage.removeItem('remembered_password')
  }

  loading.value = true
  try {
    await userStore.login({
      username: loginForm.username,
      password: loginForm.password,
    })
  } catch {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
}

.login-card {
  display: flex;
  width: 860px;
  height: 520px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  overflow: hidden;
}

.login-left {
  width: 420px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #5B8FF9 0%, #667eea 50%, #764ba2 100%);
  padding: 40px;
}

.login-brand {
  text-align: center;
  color: #fff;

  .brand-icon {
    margin-bottom: 20px;
    .el-icon {
      color: #fff;
      opacity: 0.9;
    }
  }

  .brand-title {
    font-size: 36px;
    font-weight: 700;
    margin-bottom: 8px;
  }

  .brand-subtitle {
    font-size: 18px;
    opacity: 0.9;
    margin-bottom: 16px;
  }

  .brand-desc {
    font-size: 14px;
    opacity: 0.7;
    letter-spacing: 2px;
  }
}

.login-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 50px;
}

.login-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.login-desc {
  font-size: 14px;
  color: #909399;
  margin-bottom: 32px;
}

.login-form {
  width: 100%;
  max-width: 360px;
}

.login-btn {
  width: 100%;
  font-size: 16px;
  height: 44px;
}
</style>
