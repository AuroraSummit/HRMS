<template>
  <div class="tags-view" v-if="appStore.visitedViews.length > 0">
    <el-tag
      v-for="view in appStore.visitedViews"
      :key="view.path"
      :type="isActive(view.path) ? 'primary' : 'info'"
      :closable="view.path !== '/dashboard'"
      :effect="isActive(view.path) ? 'dark' : 'plain'"
      class="tags-view-item"
      @click="handleClick(view.path)"
      @close="handleClose(view)"
    >
      {{ view.title }}
    </el-tag>
  </div>
</template>

<script setup lang="ts">
import { watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

// Watch route changes to add visited views
watch(
  () => route.path,
  () => {
    const { meta, name, path } = route
    if (meta?.title && !meta?.hidden) {
      appStore.addVisitedView({
        path,
        title: meta.title as string,
        name: name as string,
      })
    }
  },
  { immediate: true }
)

function isActive(path: string): boolean {
  return route.path === path
}

function handleClick(path: string) {
  router.push(path)
}

function handleClose(view: { path: string; title: string; name?: string }) {
  appStore.removeVisitedView(view.path)
  if (isActive(view.path)) {
    const remaining = appStore.visitedViews
    if (remaining.length > 0) {
      router.push(remaining[remaining.length - 1].path)
    } else {
      router.push('/dashboard')
    }
  }
}
</script>

<style scoped lang="scss">
.tags-view {
  height: 34px;
  display: flex;
  align-items: center;
  padding: 0 8px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  overflow-x: auto;
  gap: 4px;

  &::-webkit-scrollbar {
    height: 0;
  }

  .tags-view-item {
    cursor: pointer;
    flex-shrink: 0;
    border-radius: 4px;
    transition: all 0.2s;

    &:hover {
      opacity: 0.8;
    }
  }
}
</style>
