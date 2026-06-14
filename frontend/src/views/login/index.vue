<template>
  <div class="login-container">
    <!-- Background decorative elements -->
    <div class="login-bg">
      <div class="bg-circle bg-circle-1" />
      <div class="bg-circle bg-circle-2" />
      <div class="bg-circle bg-circle-3" />
      <div class="bg-grid" />
    </div>

    <div class="login-card">
      <!-- Left: Brand & Illustration -->
      <div class="login-left">
        <div class="login-brand">
          <div class="brand-logo">
            <svg viewBox="0 0 64 64" class="brand-svg">
              <defs>
                <linearGradient
                  id="logoGrad"
                  x1="0%"
                  y1="0%"
                  x2="100%"
                  y2="100%"
                >
                  <stop
                    offset="0%"
                    style="stop-color: #3b7cbf; stop-opacity: 1"
                  />
                  <stop
                    offset="100%"
                    style="stop-color: #1a5276; stop-opacity: 1"
                  />
                </linearGradient>
              </defs>
              <!-- Abstract building/shield representing org structure -->
              <rect
                x="8"
                y="28"
                width="12"
                height="28"
                rx="2"
                fill="url(#logoGrad)"
              />
              <rect
                x="24"
                y="16"
                width="12"
                height="40"
                rx="2"
                fill="url(#logoGrad)"
              />
              <rect
                x="40"
                y="24"
                width="12"
                height="32"
                rx="2"
                fill="url(#logoGrad)"
              />
              <rect
                x="4"
                y="56"
                width="52"
                height="4"
                rx="2"
                fill="url(#logoGrad)"
                opacity="0.6"
              />
              <!-- People dots -->
              <circle cx="14" cy="14" r="4" fill="#5B8FF9" opacity="0.8" />
              <circle cx="30" cy="6" r="4" fill="#5B8FF9" opacity="0.8" />
              <circle cx="46" cy="12" r="4" fill="#5B8FF9" opacity="0.8" />
            </svg>
          </div>
          <h1 class="brand-title">HRMS</h1>
          <p class="brand-subtitle">Human Resource Management System</p>
          <div class="brand-features">
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>组织架构管理</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>全生命周期员工管理</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>智能化数据分析</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right: Login Form -->
      <div class="login-right">
        <div class="login-header">
          <h2 class="login-title">欢迎登录</h2>
          <p class="login-desc">企业人力资源数字化管理平台</p>
        </div>

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
              class="login-input"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
              class="login-input"
            />
          </el-form-item>
          <div class="login-options">
            <el-checkbox v-model="rememberMe">记住密码</el-checkbox>
          </div>
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              class="login-btn"
              @click="handleLogin"
            >
              {{ loading ? "登录中..." : "登 录" }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- Footer -->
    <div class="login-copyright">© 2026 HRMS · 人力资源管理系统</div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { User, Lock, Check } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
const formRef = ref<FormInstance>();
const loading = ref(false);
const rememberMe = ref(false);

interface LoginForm {
  username: string;
  password: string;
}

const loginForm = reactive<LoginForm>({
  username: "",
  password: "",
});

const loginRules: FormRules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 5, message: "密码长度至少5位", trigger: "blur" },
  ],
};

onMounted(() => {
  const savedUsername = localStorage.getItem("remembered_username");
  const savedPassword = localStorage.getItem("remembered_password");
  if (savedUsername && savedPassword) {
    loginForm.username = savedUsername;
    try {
      loginForm.password = atob(savedPassword);
    } catch {
      loginForm.password = "";
    }
    rememberMe.value = true;
  }
});

async function handleLogin() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;

  if (rememberMe.value) {
    localStorage.setItem("remembered_username", loginForm.username);
    localStorage.setItem("remembered_password", btoa(loginForm.password));
  } else {
    localStorage.removeItem("remembered_username");
    localStorage.removeItem("remembered_password");
  }

  loading.value = true;
  try {
    await userStore.login({
      username: loginForm.username,
      password: loginForm.password,
    });
  } catch {
    loading.value = false;
  }
}
</script>

<style scoped lang="scss">
// ===== Corporate Color Palette =====
$primary: #3b7cbf;
$primary-dark: #1a5276;
$primary-light: #5b8ff9;
$accent: #2e86c1;
$bg-dark: #0d1b2a;
$bg-mid: #1b2838;
$text-primary: #1a1a2e;
$text-secondary: #606266;

.login-container {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(
    160deg,
    #0d1b2a 0%,
    #1b2838 30%,
    #162235 60%,
    #0f1e30 100%
  );
  overflow: hidden;
  font-family: "PingFang SC", "Microsoft YaHei", "Helvetica Neue", sans-serif;
}

// ===== Background Decorative Elements =====
.login-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.06;
  background: #5b8ff9;
}

.bg-circle-1 {
  width: 700px;
  height: 700px;
  top: -320px;
  right: -200px;
  animation: float 20s ease-in-out infinite;
}

.bg-circle-2 {
  width: 500px;
  height: 500px;
  bottom: -220px;
  left: -140px;
  animation: float 25s ease-in-out infinite reverse;
}

.bg-circle-3 {
  width: 350px;
  height: 350px;
  top: 40%;
  left: 55%;
  opacity: 0.04;
  animation: float 18s ease-in-out infinite;
}

@keyframes float {
  0%,
  100% {
    transform: translate(0, 0) scale(1);
  }
  33% {
    transform: translate(30px, -20px) scale(1.03);
  }
  66% {
    transform: translate(-15px, 15px) scale(0.97);
  }
}

// Subtle grid pattern
.bg-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(91, 143, 249, 0.04) 1px, transparent 1px),
    linear-gradient(90deg, rgba(91, 143, 249, 0.04) 1px, transparent 1px);
  background-size: 60px 60px;
}

// ===== Login Card =====
.login-card {
  position: relative;
  z-index: 1;
  display: flex;
  width: 920px;
  min-height: 560px;
  background: rgba(255, 255, 255, 0.97);
  border-radius: 20px;
  box-shadow:
    0 25px 80px rgba(0, 0, 0, 0.4),
    0 0 1px rgba(91, 143, 249, 0.2);
  overflow: hidden;
  backdrop-filter: blur(10px);
}

// ===== Left Brand Panel =====
.login-left {
  width: 440px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(
    155deg,
    #1a5276 0%,
    #2e86c1 40%,
    #3b7cbf 70%,
    #5b8ff9 100%
  );
  padding: 50px 40px;
  position: relative;
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    inset: 0;
    background:
      radial-gradient(
        circle at 20% 80%,
        rgba(255, 255, 255, 0.08) 0%,
        transparent 50%
      ),
      radial-gradient(
        circle at 80% 20%,
        rgba(255, 255, 255, 0.06) 0%,
        transparent 50%
      );
  }

  &::after {
    content: "";
    position: absolute;
    top: -60px;
    right: -60px;
    width: 200px;
    height: 200px;
    border: 2px solid rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    pointer-events: none;
  }
}

.login-brand {
  position: relative;
  z-index: 1;
  text-align: center;
  color: #fff;
}

.brand-logo {
  margin-bottom: 20px;
}

.brand-svg {
  width: 80px;
  height: 80px;
  filter: drop-shadow(0 4px 12px rgba(0, 0, 0, 0.2));
}

.brand-title {
  font-size: 38px;
  font-weight: 800;
  letter-spacing: 4px;
  margin-bottom: 10px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.15);
}

.brand-subtitle {
  font-size: 15px;
  font-weight: 300;
  letter-spacing: 3px;
  opacity: 0.85;
  margin-bottom: 32px;
  text-transform: uppercase;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 14px;
  text-align: left;
  padding: 0 20px;

  .feature-item {
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 14px;
    opacity: 0.85;
    letter-spacing: 1px;

    .el-icon {
      color: #7fdbff;
      font-size: 16px;
      flex-shrink: 0;
    }
  }
}

// ===== Right Login Panel =====
.login-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px 55px;
  background: #fff;
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
  width: 100%;
}

.login-title {
  font-size: 28px;
  font-weight: 700;
  color: $text-primary;
  margin-bottom: 8px;
  letter-spacing: 2px;
}

.login-desc {
  font-size: 14px;
  color: $text-secondary;
  letter-spacing: 1px;
}

.login-form {
  width: 100%;
  max-width: 340px;
}

.login-input {
  :deep(.el-input__wrapper) {
    border-radius: 10px;
    box-shadow: 0 0 0 1px #e4e7ed inset;
    transition: all 0.3s;
    padding: 2px 12px;

    &:hover {
      box-shadow: 0 0 0 1px #c0c4cc inset;
    }
  }

  :deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 2px rgba(59, 124, 191, 0.25) inset;
  }

  :deep(.el-input__prefix) {
    color: #a8abb2;
    margin-right: 8px;
  }
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding: 0 4px;

  :deep(.el-checkbox__label) {
    font-size: 13px;
    color: #909399;
  }
}

.login-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 4px;
  border-radius: 10px;
  background: linear-gradient(135deg, #3b7cbf 0%, #2e86c1 100%);
  border: none;
  box-shadow: 0 4px 15px rgba(59, 124, 191, 0.35);
  transition: all 0.3s;

  &:hover {
    background: linear-gradient(135deg, #2e86c1 0%, #1a5276 100%);
    box-shadow: 0 6px 20px rgba(59, 124, 191, 0.5);
    transform: translateY(-1px);
  }

  &:active {
    transform: translateY(0);
    box-shadow: 0 2px 8px rgba(59, 124, 191, 0.3);
  }
}

// ===== Copyright Footer =====
.login-copyright {
  position: absolute;
  bottom: 20px;
  left: 0;
  right: 0;
  text-align: center;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.3);
  letter-spacing: 1px;
  z-index: 1;
}

// ===== Responsive =====
@media (max-width: 960px) {
  .login-card {
    width: 420px;
    min-height: auto;
    flex-direction: column;
    margin: 20px;
  }

  .login-left {
    width: 100%;
    padding: 40px 30px;

    .brand-features {
      display: none;
    }

    .brand-title {
      font-size: 28px;
    }

    .brand-subtitle {
      font-size: 13px;
    }
  }

  .login-right {
    padding: 40px 30px;
  }
}
</style>
