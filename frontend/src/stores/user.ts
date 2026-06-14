import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, getUserInfo } from '@/api/auth'
import { setToken, setRefreshToken, removeToken, getToken } from '@/utils/auth'
import router from '@/router'

interface UserInfoData {
  id: number
  username: string
  realName: string
  email: string
  phone: string
  avatar: string
  empId: number | null
  roles: string[]
  permissions: string[]
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(getToken())
  const userInfo = ref<UserInfoData | null>(null)
  const permissions = ref<string[]>([])
  const roles = ref<string[]>([])

  async function login(loginData: { username: string; password: string }) {
    const res = await loginApi(loginData)
    if (res.data.code === 200) {
      const { accessToken, refreshToken: rToken } = res.data.data
      token.value = accessToken
      setToken(accessToken)
      if (rToken) setRefreshToken(rToken)
      await getUserInfoAction()
      // Role-based redirect (fire-and-forget — don't block the login response)
      if (roles.value.includes('employee')) {
        router.push('/user/dashboard')
      } else {
        router.push('/dashboard')
      }
      return res.data
    }
    return Promise.reject(new Error(res.data.message || '登录失败'))
  }

  async function getUserInfoAction() {
    try {
      const res = await getUserInfo()
      if (res.data.code === 200) {
        const info = res.data.data
        userInfo.value = info
        roles.value = info.roles || []
        permissions.value = info.permissions || []
      }
    } catch (error) {
      throw error
    }
  }

  function logout() {
    token.value = null
    userInfo.value = null
    permissions.value = []
    roles.value = []
    removeToken()
    router.push('/login')
  }

  return {
    token,
    userInfo,
    permissions,
    roles,
    login,
    getUserInfoAction,
    logout,
  }
}, {
  persist: {
    key: 'hrms-user',
    storage: localStorage,
    paths: ['token'],
  },
})
