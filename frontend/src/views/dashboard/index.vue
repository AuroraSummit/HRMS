<template>
  <div class="dashboard">
    <el-row :gutter="16" class="mb-20">
      <el-col :xs="12" :sm="6" v-for="card in statCards" :key="card.title">
        <el-card shadow="never" class="stat-card">
          <div class="stat-card-content">
            <div class="stat-info">
              <div class="stat-label">{{ card.title }}</div>
              <div class="stat-value">{{ card.value }}</div>
              <div class="stat-trend" :class="card.trend > 0 ? 'up' : 'down'">
                <el-icon>
                  <Top v-if="card.trend > 0" />
                  <Bottom v-else />
                </el-icon>
                {{ Math.abs(card.trend) }}%
              </div>
            </div>
            <div class="stat-icon" :style="{ background: card.bgColor }">
              <el-icon :size="28" color="#fff">
                <component :is="card.icon" />
              </el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mb-20">
      <el-col :xs="24" :lg="16" class="mb-16">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>月度入职/离职统计</span>
            </div>
          </template>
          <div class="chart-placeholder">
            <div class="bar-chart">
              <div
                v-for="(item, idx) in monthlyData"
                :key="idx"
                class="bar-group"
              >
                <div class="bar-label">{{ item.month }}</div>
                <div class="bars">
                  <div
                    class="bar bar-in"
                    :style="{ height: item.incoming * 3 + 'px' }"
                    :title="'入职: ' + item.incoming"
                  ></div>
                  <div
                    class="bar bar-out"
                    :style="{ height: item.outgoing * 3 + 'px' }"
                    :title="'离职: ' + item.outgoing"
                  ></div>
                </div>
                <div class="bar-values">
                  <span class="val-in">{{ item.incoming }}</span>
                  <span class="val-out">{{ item.outgoing }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>待办事项</span>
            </div>
          </template>
          <div class="todo-list">
            <div v-for="(todo, idx) in todoList" :key="idx" class="todo-item">
              <el-tag :type="todo.tagType" size="small">{{ todo.type }}</el-tag>
              <span class="todo-text">{{ todo.text }}</span>
              <span class="todo-date">{{ todo.date }}</span>
            </div>
            <el-empty v-if="todoList.length === 0" description="暂无待办" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="12" class="mb-16">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>最近入职员工</span>
            </div>
          </template>
          <el-table :data="recentEmployees" stripe style="width: 100%" size="small">
            <el-table-column prop="name" label="姓名" width="80" />
            <el-table-column prop="deptName" label="部门" width="100" />
            <el-table-column prop="positionName" label="岗位" width="100" />
            <el-table-column prop="hireDate" label="入职日期" width="100" />
          </el-table>
          <el-empty v-if="recentEmployees.length === 0" description="暂无数据" />
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span>部门人数分布</span>
            </div>
          </template>
          <div class="dept-dist">
            <div v-for="(dept, idx) in deptDistribution" :key="idx" class="dept-row">
              <span class="dept-name">{{ dept.name }}</span>
              <el-progress
                :percentage="dept.percentage"
                :color="dept.color"
                :stroke-width="14"
              />
              <span class="dept-count">{{ dept.count }}人</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  UserFilled,
  OfficeBuilding,
  Clock,
  Document,
  Top,
  Bottom,
} from '@element-plus/icons-vue'

const statCards = ref([
  { title: '员工总数', value: 256, icon: UserFilled, trend: 5.2, bgColor: '#5B8FF9' },
  { title: '部门数量', value: 12, icon: OfficeBuilding, trend: 0, bgColor: '#67C23A' },
  { title: '今日考勤', value: '94%', icon: Clock, trend: -1.5, bgColor: '#E6A23C' },
  { title: '待审批', value: 8, icon: Document, trend: 3.1, bgColor: '#F56C6C' },
])

const monthlyData = ref([
  { month: '1月', incoming: 12, outgoing: 3 },
  { month: '2月', incoming: 8, outgoing: 5 },
  { month: '3月', incoming: 15, outgoing: 2 },
  { month: '4月', incoming: 10, outgoing: 4 },
  { month: '5月', incoming: 18, outgoing: 1 },
  { month: '6月', incoming: 14, outgoing: 3 },
])

const todoList = ref([
  { type: '审批', tagType: 'warning', text: '李明的请假申请', date: '今天' },
  { type: '面试', tagType: 'primary', text: '前端开发工程师面试', date: '明天' },
  { type: '合同', tagType: 'success', text: '王芳合同续签', date: '3天后' },
  { type: '培训', tagType: 'info', text: '新员工入职培训', date: '下周' },
])

const recentEmployees = ref([
  { name: '张三', deptName: '技术部', positionName: '前端工程师', hireDate: '2025-05-20' },
  { name: '李四', deptName: '市场部', positionName: '市场专员', hireDate: '2025-05-18' },
  { name: '王五', deptName: '人事部', positionName: 'HR专员', hireDate: '2025-05-15' },
])

const deptDistribution = ref([
  { name: '技术部', percentage: 35, count: 42, color: '#5B8FF9' },
  { name: '市场部', percentage: 20, count: 24, color: '#67C23A' },
  { name: '人事部', percentage: 12, count: 15, color: '#E6A23C' },
  { name: '财务部', percentage: 10, count: 12, color: '#F56C6C' },
  { name: '运营部', percentage: 15, count: 18, color: '#909399' },
  { name: '行政部', percentage: 8, count: 10, color: '#409EFF' },
])
</script>

<style scoped lang="scss">
.dashboard {
  .stat-card {
    :deep(.el-card__body) {
      padding: 18px;
    }

    .stat-card-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .stat-info {
      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }
      .stat-value {
        font-size: 28px;
        font-weight: 700;
        color: #303133;
        margin-bottom: 4px;
      }
      .stat-trend {
        font-size: 12px;
        display: flex;
        align-items: center;
        gap: 2px;
        &.up {
          color: #67c23a;
        }
        &.down {
          color: #f56c6c;
        }
      }
    }

    .stat-icon {
      width: 56px;
      height: 56px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .card-header {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
  }

  .chart-placeholder {
    height: 240px;
    display: flex;
    align-items: flex-end;
  }

  .bar-chart {
    display: flex;
    justify-content: space-around;
    width: 100%;
    height: 100%;
    align-items: flex-end;
    padding-bottom: 20px;

    .bar-group {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 4px;

      .bar-label {
        font-size: 12px;
        color: #909399;
      }

      .bars {
        display: flex;
        gap: 4px;
        align-items: flex-end;

        .bar {
          width: 20px;
          min-height: 4px;
          border-radius: 4px 4px 0 0;
          transition: height 0.5s ease;
        }
        .bar-in {
          background: #5B8FF9;
        }
        .bar-out {
          background: #f56c6c;
        }
      }

      .bar-values {
        display: flex;
        gap: 8px;
        font-size: 11px;

        .val-in {
          color: #5B8FF9;
        }
        .val-out {
          color: #f56c6c;
        }
      }
    }
  }

  .todo-list {
    .todo-item {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 0;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .todo-text {
        flex: 1;
        font-size: 13px;
        color: #303133;
      }

      .todo-date {
        font-size: 12px;
        color: #909399;
        white-space: nowrap;
      }
    }
  }

  .dept-dist {
    .dept-row {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 16px;

      .dept-name {
        width: 60px;
        font-size: 13px;
        color: #606266;
        white-space: nowrap;
      }

      .el-progress {
        flex: 1;
      }

      .dept-count {
        width: 40px;
        font-size: 12px;
        color: #909399;
        text-align: right;
      }
    }
  }

  .mb-16 {
    margin-bottom: 16px;
  }
}
</style>
