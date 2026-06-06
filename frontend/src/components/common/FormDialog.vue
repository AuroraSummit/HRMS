<template>
  <el-dialog
    :model-value="visible"
    :title="title"
    :width="width"
    :close-on-click-modal="false"
    @update:model-value="$emit('update:visible', $event)"
    @open="handleOpen"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formModel"
      :rules="formRules"
      :label-width="labelWidth"
      size="default"
    >
      <slot :form="formModel" />
    </el-form>
    <template #footer>
      <slot name="footer">
        <el-button @click="$emit('update:visible', false)">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleConfirm">
          确定
        </el-button>
      </slot>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'

interface Props {
  visible: boolean
  title?: string
  width?: string
  labelWidth?: string
  formModel: Record<string, any>
  formRules?: FormRules
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: '弹窗',
  width: '500px',
  labelWidth: '100px',
  formRules: () => ({}),
  loading: false,
})

const emit = defineEmits<{
  'update:visible': [value: boolean]
  confirm: []
  open: []
  close: []
}>()

const formRef = ref<FormInstance>()

function handleOpen() {
  emit('open')
}

function handleClose() {
  formRef.value?.resetFields()
  emit('close')
}

async function handleConfirm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  emit('confirm')
}
</script>
