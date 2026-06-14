<template>
  <div class="notice-page">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>通知公告</span>
          <el-radio-group v-model="filterType" size="small" @change="filterNotices">
            <el-radio-button label="all">全部</el-radio-button>
            <el-radio-button label="1">公司公告</el-radio-button>
            <el-radio-button label="2">制度通知</el-radio-button>
            <el-radio-button label="3">活动通知</el-radio-button>
            <el-radio-button label="4">系统通知</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <div v-loading="loading">
        <div v-if="filteredNotices.length === 0" class="empty-state">
          <el-empty description="暂无通知公告" />
        </div>

        <!-- Timeline Layout -->
        <div v-else class="notice-timeline">
          <!-- Timeline vertical line -->
          <div class="timeline-track" />

          <div
            v-for="item in filteredNotices"
            :key="item.id"
            class="timeline-item"
            :class="{
              'is-important': item.priority === 1,
              'is-urgent': item.priority === 2,
            }"
            @click="viewDetail(item)"
          >
            <!-- Timeline dot & date -->
            <div class="timeline-marker">
              <div class="timeline-dot">
                <el-icon v-if="item.priority === 2" :size="14"><WarningFilled /></el-icon>
                <el-icon v-else-if="item.priority === 1" :size="14"><InfoFilled /></el-icon>
                <div v-else class="dot-inner" />
              </div>
              <div class="timeline-date">
                <span class="date-day">{{ formatDay(item.publishDate) }}</span>
                <span class="date-month">{{ formatMonth(item.publishDate) }}</span>
              </div>
            </div>

            <!-- Timeline card -->
            <div class="timeline-card">
              <div class="card-meta">
                <el-tag
                  :type="noticeTypeTag(item.noticeType)"
                  size="small"
                  effect="plain"
                >
                  {{ noticeTypeLabel(item.noticeType) }}
                </el-tag>
                <el-tag
                  v-if="item.priority === 1"
                  type="warning"
                  size="small"
                  effect="plain"
                >
                  重要
                </el-tag>
                <el-tag
                  v-if="item.priority === 2"
                  type="danger"
                  size="small"
                  effect="plain"
                >
                  紧急
                </el-tag>
              </div>

              <h3 class="card-title">{{ item.title }}</h3>

              <p class="card-excerpt" v-if="item.content">
                {{ excerpt(item.content, 120) }}
              </p>

              <div class="card-footer">
                <span class="footer-item">
                  <el-icon><View /></el-icon>
                  {{ item.viewCount || 0 }} 阅读
                </span>
                <span class="footer-item">
                  <el-icon><User /></el-icon>
                  {{ item.publisher || '系统管理员' }}
                </span>
                <span class="footer-read-more">查看详情 <el-icon><ArrowRight /></el-icon></span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- Notice Detail Dialog -->
    <el-dialog
      v-model="detailVisible"
      :title="detail?.title"
      width="720px"
      :close-on-click-modal="false"
    >
      <div v-if="detail" class="notice-detail">
        <div class="detail-meta">
          <div class="meta-left">
            <el-tag :type="noticeTypeTag(detail.noticeType)" size="small">
              {{ noticeTypeLabel(detail.noticeType) }}
            </el-tag>
            <el-tag
              v-if="detail.priority === 1"
              type="warning"
              size="small"
            >
              重要
            </el-tag>
            <el-tag
              v-if="detail.priority === 2"
              type="danger"
              size="small"
            >
              紧急
            </el-tag>
          </div>
          <div class="meta-right">
            <span>{{ detail.publishDate }}</span>
            <el-divider direction="vertical" />
            <span>{{ detail.publisher || '系统管理员' }}</span>
            <el-divider direction="vertical" />
            <span><el-icon><View /></el-icon> {{ detail.viewCount || 0 }}</span>
          </div>
        </div>
        <el-divider />
        <div class="detail-content">{{ detail.content || '暂无内容' }}</div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import {
  WarningFilled,
  InfoFilled,
  View,
  User,
  ArrowRight,
} from '@element-plus/icons-vue'
import { getNoticeList, getNoticeDetail, type NoticeData } from '@/api/notice'

const notices = ref<NoticeData[]>([])
const loading = ref(false)
const detailVisible = ref(false)
const detail = ref<NoticeData | null>(null)
const filterType = ref('all')

const filteredNotices = computed(() => {
  if (filterType.value === 'all') return notices.value
  return notices.value.filter((n) => n.noticeType === Number(filterType.value))
})

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getNoticeList()
    if (res.data.code === 200) {
      notices.value = (res.data.data || []).sort(
        (a: NoticeData, b: NoticeData) =>
          (b.publishDate || '').localeCompare(a.publishDate || '') ||
          (b.priority || 0) - (a.priority || 0)
      )
    }
  } catch {
    // handled by interceptor
  } finally {
    loading.value = false
  }
}

function filterNotices() {
  // computed already handles filtering
}

async function viewDetail(notice: NoticeData) {
  try {
    const res = await getNoticeDetail(notice.id!)
    if (res.data.code === 200) {
      detail.value = res.data.data
      detailVisible.value = true
      // Locally increment view count
      const found = notices.value.find((n) => n.id === notice.id)
      if (found) found.viewCount = (found.viewCount || 0) + 1
    }
  } catch {
    // handled by interceptor
  }
}

function formatDay(dateStr?: string): string {
  if (!dateStr) return '--'
  // Expects YYYY-MM-DD
  const parts = dateStr.split('-')
  return parts[2] || '--'
}

function formatMonth(dateStr?: string): string {
  if (!dateStr) return '--'
  const parts = dateStr.split('-')
  const months = [
    'JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN',
    'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC',
  ]
  const idx = parseInt(parts[1] || '0') - 1
  return months[idx] || `${parts[1]}月`
}

function excerpt(text: string, maxLen: number): string {
  if (!text) return ''
  return text.length > maxLen ? text.slice(0, maxLen) + '...' : text
}

function noticeTypeLabel(v: number): string {
  const m: Record<number, string> = {
    1: '公司公告', 2: '制度通知', 3: '活动通知', 4: '系统通知',
  }
  return m[v] || '未知'
}

function noticeTypeTag(v: number): string {
  const m: Record<number, string> = {
    1: '', 2: 'info', 3: 'success', 4: 'warning',
  }
  return m[v] || 'info'
}
</script>

<style scoped lang="scss">
// ===== Color variables =====
$primary: #5b8ff9;
$timeline-line: #e4e7ed;
$dot-normal: #c0c4cc;
$dot-important: #e6a23c;
$dot-urgent: #f56c6c;

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

// ===== Timeline =====
.notice-timeline {
  position: relative;
  padding: 8px 0 8px 0;
}

.timeline-track {
  position: absolute;
  left: 68px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: $timeline-line;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -3px;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: $primary;
  }

  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: -3px;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: $timeline-line;
  }
}

// ===== Timeline Item =====
.timeline-item {
  display: flex;
  align-items: flex-start;
  gap: 24px;
  padding: 20px 0;
  cursor: pointer;
  position: relative;

  &:first-child {
    padding-top: 4px;
  }

  &:last-child {
    padding-bottom: 4px;
  }

  &:hover .timeline-card {
    box-shadow: 0 4px 16px rgba(91, 143, 249, 0.15);
    transform: translateX(6px);
    border-color: $primary;
  }
}

// ===== Timeline Marker =====
.timeline-marker {
  flex-shrink: 0;
  width: 56px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.timeline-dot {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #fff;
  border: 2px solid $dot-normal;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
  transition: all 0.3s;

  .dot-inner {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: $dot-normal;
  }

  .timeline-item:hover & {
    border-color: $primary;
    box-shadow: 0 0 0 4px rgba(91, 143, 249, 0.15);

    .dot-inner {
      background: $primary;
    }
  }
}

.timeline-item.is-important .timeline-dot {
  border-color: $dot-important;
  box-shadow: 0 0 0 4px rgba(230, 162, 60, 0.12);

  .el-icon { color: $dot-important; }
}

.timeline-item.is-urgent .timeline-dot {
  border-color: $dot-urgent;
  box-shadow: 0 0 0 4px rgba(245, 108, 108, 0.12);
  animation: pulse-urgent 2s ease-in-out infinite;

  .el-icon { color: $dot-urgent; }
}

@keyframes pulse-urgent {
  0%, 100% { box-shadow: 0 0 0 4px rgba(245, 108, 108, 0.12); }
  50% { box-shadow: 0 0 0 8px rgba(245, 108, 108, 0.06); }
}

.timeline-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  line-height: 1;

  .date-day {
    font-size: 18px;
    font-weight: 700;
    color: #303133;
  }

  .date-month {
    font-size: 11px;
    color: #909399;
    text-transform: uppercase;
    letter-spacing: 1px;
  }
}

// ===== Timeline Card =====
.timeline-card {
  flex: 1;
  min-width: 0;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 10px;
  padding: 16px 20px;
  transition: all 0.3s ease;
}

.card-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.card-title {
  margin: 0 0 10px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  line-height: 1.4;
}

.card-excerpt {
  margin: 0 0 12px;
  font-size: 13px;
  color: #909399;
  line-height: 1.6;
}

.card-footer {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 12px;
  color: #c0c4cc;

  .footer-item {
    display: inline-flex;
    align-items: center;
    gap: 4px;

    .el-icon { font-size: 14px; }
  }

  .footer-read-more {
    margin-left: auto;
    color: $primary;
    font-weight: 500;
    display: inline-flex;
    align-items: center;
    gap: 4px;
    opacity: 0;
    transition: opacity 0.3s;

    .timeline-item:hover & {
      opacity: 1;
    }
  }
}

// ===== Detail Dialog =====
.notice-detail {
  .detail-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 12px;
    font-size: 13px;
    color: #909399;

    .meta-left, .meta-right {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  .detail-content {
    font-size: 14px;
    line-height: 1.9;
    color: #303133;
    white-space: pre-wrap;
  }
}

// ===== Empty =====
.empty-state {
  padding: 40px 0;
}

// ===== Responsive =====
@media (max-width: 768px) {
  .timeline-marker {
    width: 44px;
  }
  .timeline-track {
    left: 56px;
  }
  .timeline-date .date-day {
    font-size: 15px;
  }
  .timeline-card {
    padding: 12px 14px;
  }
  .card-title {
    font-size: 14px;
  }
}
</style>
