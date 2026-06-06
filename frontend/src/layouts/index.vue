<template>
  <el-container class="layout-container">
    <Sidebar />
    <el-container class="layout-right">
      <Navbar />
      <TagsView />
      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="page" mode="out-in">
            <component :is="Component" :key="$route.fullPath" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import Sidebar from './components/Sidebar.vue'
import Navbar from './components/Navbar.vue'
import TagsView from './components/TagsView.vue'
</script>

<style scoped lang="scss">
.layout-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.layout-right {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
  background-color: #f0f2f5;
}

.layout-main {
  padding: 16px;
  overflow-y: auto;
  overflow-x: hidden;
  flex: 1;
  background-color: #f0f2f5;
}
</style>
