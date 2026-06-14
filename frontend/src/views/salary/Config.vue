<template>
  <div class="config-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>薪资配置</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增配置
          </el-button>
        </div>
      </template>
      <el-table :data="list" stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="name" label="配置名称" width="150" />
        <el-table-column prop="basicSalary" label="基本薪资" width="100" />
        <el-table-column
          prop="housingFundRate"
          label="公积金比例(%)"
          width="120"
        >
          <template #default="{ row }">{{ row.housingFundRate }}%</template>
        </el-table-column>
        <el-table-column prop="taxExemption" label="个税起征点" width="120" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)"
              >编辑</el-button
            >
            <el-popconfirm title="确认删除？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑配置' : '新增配置'"
      width="600px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="配置名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="基本薪资" prop="basicSalary">
          <el-input-number
            v-model="form.basicSalary"
            :min="0"
            :step="1000"
            style="width: 100%"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公积金比例(%)" prop="housingFundRate">
              <el-input-number
                v-model="form.housingFundRate"
                :min="0"
                :max="100"
                :step="0.5"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="养老比例(%)" prop="pensionRate">
              <el-input-number
                v-model="form.pensionRate"
                :min="0"
                :max="100"
                :step="0.5"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="医疗比例(%)" prop="medicalRate">
              <el-input-number
                v-model="form.medicalRate"
                :min="0"
                :max="100"
                :step="0.5"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失业比例(%)" prop="unemploymentRate">
              <el-input-number
                v-model="form.unemploymentRate"
                :min="0"
                :max="100"
                :step="0.5"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工伤比例(%)" prop="injuryRate">
              <el-input-number
                v-model="form.injuryRate"
                :min="0"
                :max="100"
                :step="0.5"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生育比例(%)" prop="maternityRate">
              <el-input-number
                v-model="form.maternityRate"
                :min="0"
                :max="100"
                :step="0.5"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="个税起征点" prop="taxExemption">
          <el-input-number
            v-model="form.taxExemption"
            :min="0"
            :step="500"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit"
          >确定</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { Plus } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import type { FormInstance, FormRules } from "element-plus";
import {
  getSalaryConfigPage,
  createSalaryConfig,
  updateSalaryConfig,
  deleteSalaryConfig,
  type SalaryConfigData,
} from "@/api/salary";

const list = ref<SalaryConfigData[]>([]);
const loading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const submitLoading = ref(false);
const formRef = ref<FormInstance>();

const defaultForm: SalaryConfigData = {
  name: "",
  basicSalary: 0,
  housingFundRate: 0,
  pensionRate: 0,
  medicalRate: 0,
  unemploymentRate: 0,
  injuryRate: 0,
  maternityRate: 0,
  taxExemption: 5000,
};
const form = reactive<SalaryConfigData>({ ...defaultForm });
const rules: FormRules = {
  name: [{ required: true, message: "请输入配置名称", trigger: "blur" }],
};

onMounted(() => {
  loadData();
});
async function loadData() {
  loading.value = true;
  try {
    const res = await getSalaryConfigPage({ page: 1, pageSize: 100 });
    if (res.data.code === 200)
      list.value = res.data.data.records || res.data.data || [];
  } catch {
  } finally {
    loading.value = false;
  }
}
function handleAdd() {
  isEdit.value = false;
  Object.assign(form, { ...defaultForm });
  dialogVisible.value = true;
}
function handleEdit(row: SalaryConfigData) {
  isEdit.value = true;
  Object.assign(form, JSON.parse(JSON.stringify(row)));
  dialogVisible.value = true;
}
async function handleDelete(row: SalaryConfigData) {
  try {
    const res = await deleteSalaryConfig(row.id!);
    if (res.data.code === 200) {
      ElMessage.success("删除成功");
      loadData();
    }
  } catch {}
}
async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false);
  if (!valid) return;
  submitLoading.value = true;
  try {
    const res = isEdit.value
      ? await updateSalaryConfig(form)
      : await createSalaryConfig(form);
    if (res.data.code === 200) {
      ElMessage.success("操作成功");
      dialogVisible.value = false;
      loadData();
    }
  } catch {
  } finally {
    submitLoading.value = false;
  }
}
</script>
<style scoped lang="scss">
.config-page {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: 600;
  }
}
</style>
