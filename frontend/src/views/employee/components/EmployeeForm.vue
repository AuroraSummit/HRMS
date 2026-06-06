<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑员工' : '新增员工'"
    width="680px"
    :close-on-click-modal="false"
    @update:model-value="$emit('update:visible', $event)"
    @open="handleOpen"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
      size="default"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工号" prop="empNo">
            <el-input v-model="form.empNo" :disabled="isEdit" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select v-model="form.gender" placeholder="请选择" style="width: 100%">
              <el-option label="男" :value="0" />
              <el-option label="女" :value="1" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机" prop="phone">
            <el-input v-model="form.phone" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门" prop="deptId">
            <el-tree-select
              v-model="form.deptId"
              :data="deptTree"
              :props="{ label: 'name', value: 'id', children: 'children' }"
              placeholder="选择部门"
              clearable
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="岗位" prop="positionId">
            <el-select v-model="form.positionId" placeholder="选择岗位" style="width: 100%" clearable>
              <el-option
                v-for="pos in positionOptions"
                :key="pos.id"
                :label="pos.name"
                :value="pos.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入职日期" prop="hireDate">
            <el-date-picker
              v-model="form.hireDate"
              type="date"
              placeholder="选择日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="form.idCard" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="选择状态" style="width: 100%">
              <el-option label="在职" value="active" />
              <el-option label="试用期" value="probation" />
              <el-option label="离职" value="resigned" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="紧急联系人" prop="emergencyContact">
            <el-input v-model="form.emergencyContact" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="紧急电话" prop="emergencyPhone">
            <el-input v-model="form.emergencyPhone" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { create, update, type EmployeeData } from '@/api/employee'
import { getDeptTree, getPositionList, type DeptData, type PositionData } from '@/api/org'

interface Props {
  visible: boolean
  formData: EmployeeData | null
  isEdit: boolean
}

const props = withDefaults(defineProps<Props>(), {
  visible: false,
  formData: null,
  isEdit: false,
})

const emit = defineEmits<{
  'update:visible': [value: boolean]
  success: []
}>()

const formRef = ref<FormInstance>()
const submitLoading = ref(false)
const deptTree = ref<DeptData[]>([])
const positionOptions = ref<PositionData[]>([])

const defaultForm: EmployeeData = {
  empNo: '',
  name: '',
  gender: 0,
  deptId: 0,
  positionId: 0,
  phone: '',
  email: '',
  hireDate: '',
  status: 'active',
  idCard: '',
  emergencyContact: '',
  emergencyPhone: '',
}

const form = reactive<EmployeeData>({ ...defaultForm })

const rules: FormRules = {
  empNo: [{ required: true, message: '请输入工号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  positionId: [{ required: true, message: '请选择岗位', trigger: 'change' }],
  hireDate: [{ required: true, message: '请选择入职日期', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
}

onMounted(() => {
  loadDeptTree()
  loadPositions()
})

async function loadDeptTree() {
  try {
    const res = await getDeptTree()
    if (res.data.code === 200) {
      deptTree.value = res.data.data || []
    }
  } catch {
    // silent
  }
}

async function loadPositions() {
  try {
    const res = await getPositionList()
    if (res.data.code === 200) {
      positionOptions.value = res.data.data || []
    }
  } catch {
    // silent
  }
}

function handleOpen() {
  if (props.formData && props.isEdit) {
    Object.assign(form, props.formData)
  } else {
    Object.assign(form, { ...defaultForm })
  }
}

function handleClose() {
  formRef.value?.resetFields()
}

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const res = props.isEdit ? await update(form) : await create(form)
    if (res.data.code === 200) {
      ElMessage.success(props.isEdit ? '编辑成功' : '新增成功')
      emit('success')
      emit('update:visible', false)
    }
  } catch {
    // handled by interceptor
  } finally {
    submitLoading.value = false
  }
}
</script>
