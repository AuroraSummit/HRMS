<template>
  <el-aside
    :width="appStore.sidebarCollapsed ? '64px' : '220px'"
    class="sidebar-container"
  >
    <div class="sidebar-logo">
      <img src="@/assets/logo.svg" alt="Logo" class="sidebar-logo-img" />
      <span v-show="!appStore.sidebarCollapsed" class="sidebar-logo-text">HRMS</span>
    </div>
    <el-menu
      :default-active="activeMenu"
      :collapse="appStore.sidebarCollapsed"
      :background-color="menuBg"
      :text-color="menuText"
      :active-text-color="menuActiveText"
      :unique-opened="true"
      :collapse-transition="false"
      mode="vertical"
      router
    >
      <SidebarItem
        v-for="route in menuRoutes"
        :key="route.path"
        :route="route"
        :base-path="route.path"
      />
    </el-menu>
  </el-aside>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useRouter } from 'vue-router'
import SidebarItem from './SidebarItem.vue'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

const menuBg = '#1f2d3d'
const menuText = '#bfcbd9'
const menuActiveText = '#5B8FF9'

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu as string
  }
  return path
})

const menuRoutes = computed(() => {
  return router
    .getRoutes()
    .filter((r) => r.meta && !r.meta.hidden && r.children && r.children.length > 0)
})
</script>

<style scoped lang="scss">
.sidebar-container {
  background-color: #1f2d3d;
  transition: width 0.3s ease;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  .sidebar-logo {
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 16px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);

    .sidebar-logo-img {
      width: 32px;
      height: 32px;
      flex-shrink: 0;
    }

    .sidebar-logo-text {
      margin-left: 10px;
      font-size: 18px;
      font-weight: 600;
      color: #ffffff;
      white-space: nowrap;
    }
  }

  .el-menu {
    border-right: none;
    flex: 1;
    overflow-y: auto;
    overflow-x: hidden;

    &:not(.el-menu--collapse) {
      width: 220px;
    }
  }
}
</style>
