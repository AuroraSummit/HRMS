<template>
  <div class="not-found">
    <div class="not-found-content">
      <h1 class="not-found-code">404</h1>
      <p class="not-found-desc">抱歉，您访问的页面不存在</p>
      <el-button type="primary" @click="$router.push('/dashboard')">返回首页</el-button>
    </div>
  </div>
</template>

<style scoped lang="scss">
.not-found {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: #f0f2f5;
}

.not-found-content {
  text-align: center;

  .not-found-code {
    font-size: 96px;
    font-weight: 700;
    color: #5B8FF9;
    margin: 0;
    line-height: 1.2;
  }

  .not-found-desc {
    font-size: 18px;
    color: #909399;
    margin: 16px 0 32px;
  }
}
</style>
