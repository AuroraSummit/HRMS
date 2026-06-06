<template>
  <router-view />
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getToken, removeToken } from '@/utils/auth'
import { getUserInfo } from '@/api/auth'

const router = useRouter()

onMounted(async () => {
  const token = getToken()
  if (!token) return

  // 验证 token 是否有效
  try {
    const res = await getUserInfo()
    if (res.data.code !== 200) {
      // Token 无效，清除
      clearAndRedirect()
    }
  } catch {
    // 网络错误或 401，清除 token
    clearAndRedirect()
  }
})

function clearAndRedirect() {
  removeToken()
  localStorage.removeItem('hrms-user')
  localStorage.removeItem('hrms_access_token')
  localStorage.removeItem('hrms_refresh_token')
  router.replace('/login')
}
</script>

<style scoped>
</style>
