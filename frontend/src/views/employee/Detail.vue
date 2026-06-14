<template>
  <div class="employee-detail">
    <el-card shadow="never" class="mb-20">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>
      <div class="basic-info" v-if="employee">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="工号">{{ employee.empNo }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ employee.name }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ genderLabel(employee.gender) }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ employee.deptName }}</el-descriptions-item>
          <el-descriptions-item label="岗位">{{ employee.positionName }}</el-descriptions-item>
          <el-descriptions-item label="手机">{{ employee.phone }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ employee.email }}</el-descriptions-item>
          <el-descriptions-item label="入职日期">{{ employee.hireDate }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="employee.status === 'active' ? 'success' : employee.status === 'probation' ? 'warning' : 'danger'" size="small">
              {{ employee.status === 'active' ? '在职' : employee.status === 'probation' ? '试用期' : '离职' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <el-empty v-else description="暂无数据" />
    </el-card>

    <!-- Tab Content -->
    <el-card shadow="never">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="教育经历" name="education">
          <el-table :data="employee?.educationList || []" stripe v-loading="false" empty-text="暂无教育经历">
            <el-table-column prop="school" label="学校" min-width="150" />
            <el-table-column prop="major" label="专业" width="150" />
            <el-table-column prop="degree" label="学历" width="100">
              <template #default="{ row }">{{ degreeLabel(row.degree) }}</template>
            </el-table-column>
            <el-table-column prop="startDate" label="开始日期" width="120" />
            <el-table-column prop="endDate" label="结束日期" width="120" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="工作经历" name="work">
          <el-table :data="employee?.workExperienceList || []" stripe empty-text="暂无工作经历">
            <el-table-column prop="company" label="公司" min-width="150" />
            <el-table-column prop="position" label="职位" width="150" />
            <el-table-column prop="startDate" label="开始日期" width="120" />
            <el-table-column prop="endDate" label="结束日期" width="120" />
            <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="合同信息" name="contract">
          <el-table :data="contractList" stripe v-loading="contractLoading" empty-text="暂无合同信息">
            <el-table-column prop="contractType" label="合同类型" width="100">
              <template #default="{ row }">{{ contractTypeLabel(row.contractType) }}</template>
            </el-table-column>
            <el-table-column prop="startDate" label="开始日期" width="120" />
            <el-table-column prop="endDate" label="结束日期" width="120" />
            <el-table-column prop="signDate" label="签订日期" width="120" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="contractStatusTag(row.status)" size="small">{{ contractStatusLabel(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" show-overflow-tooltip />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="考勤记录" name="attendance">
          <el-table :data="attendanceList" stripe v-loading="attendanceLoading" empty-text="暂无考勤记录">
            <el-table-column prop="attendanceDate" label="日期" width="120" />
            <el-table-column prop="clockInTime" label="签到" width="160" />
            <el-table-column prop="clockOutTime" label="签退" width="160" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="attTag(row.status)" size="small">{{ attLabel(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" show-overflow-tooltip />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="薪酬记录" name="salary">
          <el-table :data="salaryList" stripe v-loading="salaryLoading" empty-text="暂无薪酬记录">
            <el-table-column prop="month" label="月份" width="100" />
            <el-table-column prop="basicSalary" label="基本工资" width="120" />
            <el-table-column prop="bonus" label="奖金" width="100" />
            <el-table-column prop="allowance" label="津贴" width="100" />
            <el-table-column prop="overtimePay" label="加班费" width="100" />
            <el-table-column prop="deduction" label="扣款" width="100" />
            <el-table-column prop="actualSalary" label="实发工资" width="120" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="绩效记录" name="performance">
          <el-table :data="performanceList" stripe v-loading="performanceLoading" empty-text="暂无绩效记录">
            <el-table-column prop="planId" label="计划ID" width="80" />
            <el-table-column prop="selfScore" label="自评分" width="80" />
            <el-table-column prop="managerScore" label="主管评分" width="100" />
            <el-table-column prop="finalScore" label="最终得分" width="100" />
            <el-table-column prop="result" label="结果" width="100">
              <template #default="{ row }">{{ performanceResultLabel(row.result) }}</template>
            </el-table-column>
            <el-table-column prop="selfComment" label="自评意见" show-overflow-tooltip />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getDetail, type EmployeeData } from '@/api/employee'
import { getContractPage, type ContractData } from '@/api/contract'
import { getAttendancePage, type AttendanceData } from '@/api/attendance'
import { getSalaryPage, type SalaryData } from '@/api/salary'
import { getPerformancePage, type PerformanceData } from '@/api/performance'

const route = useRoute()
const activeTab = ref('education')
const employee = ref<EmployeeData | null>(null)

// Tab data
const contractList = ref<ContractData[]>([])
const contractLoading = ref(false)
const attendanceList = ref<AttendanceData[]>([])
const attendanceLoading = ref(false)
const salaryList = ref<SalaryData[]>([])
const salaryLoading = ref(false)
const performanceList = ref<PerformanceData[]>([])
const performanceLoading = ref(false)

const empId = Number(route.params.id)

onMounted(async () => {
  if (empId && empId > 0) {
    try {
      const res = await getDetail(empId)
      if (res.data.code === 200) {
        employee.value = res.data.data
      }
    } catch {
      // handled by interceptor
    }
  }
})

async function handleTabChange(name: string) {
  switch (name) {
    case 'contract': await loadContracts(); break
    case 'attendance': await loadAttendances(); break
    case 'salary': await loadSalaries(); break
    case 'performance': await loadPerformances(); break
  }
}

async function loadContracts() {
  if (contractList.value.length > 0) return
  contractLoading.value = true
  try {
    const res = await getContractPage({ empId, page: 1, pageSize: 100 })
    if (res.data.code === 200) contractList.value = res.data.data.records || []
  } catch { } finally { contractLoading.value = false }
}

async function loadAttendances() {
  if (attendanceList.value.length > 0) return
  attendanceLoading.value = true
  try {
    const res = await getAttendancePage({ empId, page: 1, pageSize: 100 })
    if (res.data.code === 200) attendanceList.value = res.data.data.records || []
  } catch { } finally { attendanceLoading.value = false }
}

async function loadSalaries() {
  if (salaryList.value.length > 0) return
  salaryLoading.value = true
  try {
    const res = await getSalaryPage({ empId, page: 1, pageSize: 100 })
    if (res.data.code === 200) salaryList.value = res.data.data.records || []
  } catch { } finally { salaryLoading.value = false }
}

async function loadPerformances() {
  if (performanceList.value.length > 0) return
  performanceLoading.value = true
  try {
    const res = await getPerformancePage({ empId, page: 1, pageSize: 100 })
    if (res.data.code === 200) performanceList.value = res.data.data.records || []
  } catch { } finally { performanceLoading.value = false }
}

// Label helpers
function genderLabel(v: number) { const m: Record<number, string> = { 0: '男', 1: '女', 2: '未知' }; return m[v] || v }
function degreeLabel(v: number) { const m: Record<number, string> = { 1: '高中', 2: '大专', 3: '本科', 4: '硕士', 5: '博士' }; return m[v] || '未知' }
function contractTypeLabel(v: number) { const m: Record<number, string> = { 1: '正式', 2: '试用', 3: '实习', 4: '劳务', 5: '兼职' }; return m[v] || '未知' }
function contractStatusTag(v: number) { return v === 1 ? 'success' : v === 2 ? 'warning' : v === 3 ? 'danger' : v === 0 ? 'info' : 'info' }
function contractStatusLabel(v: number) { const m: Record<number, string> = { 0: '待签', 1: '生效', 2: '到期', 3: '解约' }; return m[v] || '未知' }
function attTag(v: number) { return v === 0 ? 'success' : v === 1 ? 'warning' : v === 2 ? 'warning' : v === 3 ? 'danger' : 'danger' }
function attLabel(v: number) { const m: Record<number, string> = { 0: '正常', 1: '迟到', 2: '早退', 3: '缺勤', 4: '异常' }; return m[v] || '未知' }
function performanceResultLabel(v: number) { const m: Record<number, string> = { 1: '优秀', 2: '良好', 3: '合格', 4: '待改进', 5: '不合格' }; return m[v] || '未知' }
</script>

<style scoped lang="scss">
.employee-detail {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: 600;
  }
}
</style>
