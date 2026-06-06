import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { RouteRecordRaw } from 'vue-router'

export const useAppStore = defineStore('app', () => {
  const sidebarCollapsed = ref(false)
  const theme = ref('light')
  const visitedViews = ref<Array<{ path: string; title: string; name?: string }>>([])
  const breadcrumbList = ref<Array<{ path: string; title: string }>>([])

  const sidebarWidth = computed(() =>
    sidebarCollapsed.value ? 'var(--sidebar-collapsed-width)' : 'var(--sidebar-width)'
  )

  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  function addVisitedView(view: { path: string; title: string; name?: string }) {
    const exists = visitedViews.value.some((v) => v.path === view.path)
    if (!exists) {
      visitedViews.value.push(view)
    }
  }

  function removeVisitedView(path: string) {
    visitedViews.value = visitedViews.value.filter((v) => v.path !== path)
  }

  function setBreadcrumb(list: Array<{ path: string; title: string }>) {
    breadcrumbList.value = list
  }

  return {
    sidebarCollapsed,
    theme,
    visitedViews,
    breadcrumbList,
    sidebarWidth,
    toggleSidebar,
    addVisitedView,
    removeVisitedView,
    setBreadcrumb,
  }
}, {
  persist: {
    key: 'hrms-app',
    storage: localStorage,
    paths: ['sidebarCollapsed', 'visitedViews'],
  },
})
