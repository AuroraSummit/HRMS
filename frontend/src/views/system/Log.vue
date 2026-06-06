<template>
  <div class="log-page">
    <el-card shadow="never" class="mb-20">
      <el-form :model="queryParams" inline>
        <el-form-item label="模块"><el-input v-model="queryParams.module" placeholder="请输入" clearable style="width:140px" /></el-form-item>
        <el-form-item label="操作人"><el-input v-model="queryParams.operator" placeholder="请输入" clearable style="width:140px" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="queryParams.status" placeholder="选择" clearable style="width:120px"><el-option label="成功" :value="1" /><el-option label="失败" :value="0" /></el-select></el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker v-model="dateRange" type="datetimerange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="YYYY-MM-DD HH:mm:ss" style="width:320px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="danger" @click="handleClean">清空日志</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <el-table :data="list" stripe v-loading="loading" style="width:100%">
        <el-table-column prop="module" label="模块" width="100" />
        <el-table-column prop="action" label="操作" width="100" />
        <el-table-column prop="operator" label="操作人" width="90" />
        <el-table-column prop="ip" label="IP" width="130" />
        <el-table-column prop="requestUrl" label="请求地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="requestMethod" label="请求方式" width="80" />
        <el-table-column prop="duration" label="耗时(ms)" width="80" />
        <el-table-column prop="status" label="状态" width="60"><template #default="{row}"><el-tag :type="row.status===1?'success':'danger'" size="small">{{ row.status===1?'成功':'失败' }}</el-tag></template></el-table-column>
        <el-table-column prop="createdAt" label="操作时间" width="170" />
      </el-table>
      <div class="pagination-wrapper">
        <el-pagination v-model:current-page="queryParams.page" v-model:page-size="queryParams.pageSize" :total="total" layout="total,sizes,prev,pager,next" @current-change="loadData" @size-change="loadData" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getLogPage, cleanLog } from '@/api/system'

const list = ref<any[]>([]); const total = ref(0); const loading = ref(false)
const dateRange = ref<string[]>([])
const queryParams = reactive({ page: 1, pageSize: 10, module: '', operator: '', status: undefined as number | undefined, startTime: '', endTime: '' })

onMounted(() => { loadData() })

async function loadData() {
  loading.value = true
  if (dateRange.value && dateRange.value.length === 2) {
    queryParams.startTime = dateRange.value[0]
    queryParams.endTime = dateRange.value[1]
  } else { queryParams.startTime = ''; queryParams.endTime = '' }
  try {
    const res = await getLogPage(queryParams)
    if (res.data.code === 200) { const d = res.data.data; list.value = d.records || d || []; total.value = d.total || 0 }
  } catch { } finally { loading.value = false }
}

function handleReset() { queryParams.module = ''; queryParams.operator = ''; queryParams.status = undefined; dateRange.value = []; loadData() }

async function handleClean() {
  try { const res = await cleanLog(); if (res.data.code === 200) { ElMessage.success('日志已清空'); loadData() } } catch { }
}
</script>

<style scoped lang="scss">
.log-page {
  .pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 16px; }
}
</style>
