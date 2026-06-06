<template>
  <template v-if="hasChildren">
    <el-sub-menu :index="resolvePath(route.path)">
      <template #title>
        <el-icon v-if="route.meta?.icon">
          <component :is="route.meta.icon" />
        </el-icon>
        <span>{{ route.meta?.title }}</span>
      </template>
      <SidebarItem
        v-for="child in route.children"
        :key="child.path"
        :route="child"
        :base-path="resolvePath(route.path)"
      />
    </el-sub-menu>
  </template>
  <template v-else>
    <el-menu-item :index="resolvePath(route.path)">
      <el-icon v-if="route.meta?.icon">
        <component :is="route.meta.icon" />
      </el-icon>
      <template #title>
        <span>{{ route.meta?.title }}</span>
      </template>
    </el-menu-item>
  </template>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { RouteRecordRaw } from 'vue-router'

interface Props {
  route: RouteRecordRaw
  basePath: string
}

const props = defineProps<Props>()

const hasChildren = computed(() => {
  return (
    props.route.children &&
    props.route.children.length > 0 &&
    !props.route.children.every((child) => child.meta?.hidden)
  )
})

function resolvePath(path: string): string {
  if (/^https?:\/\//.test(path)) {
    return path
  }
  if (path.startsWith('/')) {
    return path
  }
  return props.basePath ? `${props.basePath}/${path}` : `/${path}`
}
</script>
