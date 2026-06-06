<template>
  <el-upload
    v-bind="$attrs"
    :action="action"
    :headers="headers"
    :on-success="handleSuccess"
    :on-error="handleError"
    :before-upload="handleBeforeUpload"
  >
    <slot>
      <el-button type="primary">
        <el-icon><Upload /></el-icon>点击上传
      </el-button>
    </slot>
    <template #tip>
      <div v-if="tip" class="el-upload__tip">{{ tip }}</div>
    </template>
  </el-upload>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload } from '@element-plus/icons-vue'
import { getToken } from '@/utils/auth'

interface Props {
  action?: string
  accept?: string
  maxSize?: number
  tip?: string
}

const props = withDefaults(defineProps<Props>(), {
  action: '/api/v1/hr/common/upload',
  accept: '.jpg,.jpeg,.png,.gif,.doc,.docx,.pdf',
  maxSize: 10,
  tip: '',
})

const emit = defineEmits<{
  success: [url: string]
  error: [err: Error]
}>()

const headers = computed(() => ({
  Authorization: `Bearer ${getToken()}`,
}))

function handleSuccess(response: any) {
  if (response.code === 200) {
    const url = response.data?.url || response.data
    ElMessage.success('上传成功')
    emit('success', url)
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

function handleError(err: Error) {
  ElMessage.error('上传失败')
  emit('error', err)
}

function handleBeforeUpload(file: File) {
  const isLt = file.size / 1024 / 1024 < props.maxSize
  if (!isLt) {
    ElMessage.error(`文件大小不能超过 ${props.maxSize}MB`)
    return false
  }
  return true
}
</script>
