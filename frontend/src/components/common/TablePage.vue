<template>
  <div class="table-page">
    <el-table
      :data="data"
      v-loading="loading"
      stripe
      style="width: 100%"
      v-bind="$attrs"
    >
      <slot />
    </el-table>
    <div v-if="showPagination" class="pagination-wrapper">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="pageSizes"
        :total="total"
        :layout="layout"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  data: any[]
  loading?: boolean
  total?: number
  currentPage?: number
  pageSize?: number
  pageSizes?: number[]
  layout?: string
  showPagination?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  total: 0,
  currentPage: 1,
  pageSize: 10,
  pageSizes: () => [10, 20, 50, 100],
  layout: 'total, sizes, prev, pager, next, jumper',
  showPagination: true,
})

const emit = defineEmits<{
  'update:currentPage': [value: number]
  'update:pageSize': [value: number]
  'page-change': []
}>()

const currentPage = computed({
  get: () => props.currentPage,
  set: (val: number) => emit('update:currentPage', val),
})

const pageSize = computed({
  get: () => props.pageSize,
  set: (val: number) => emit('update:pageSize', val),
})

function handleSizeChange(_val: number) {
  emit('page-change')
}

function handleCurrentChange(_val: number) {
  emit('page-change')
}
</script>

<style scoped lang="scss">
.table-page {
  .pagination-wrapper {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;
    padding-top: 8px;
  }
}
</style>
