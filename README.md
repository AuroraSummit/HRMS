# HRMS 人力资源管理系统

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.5
- **JDK**: 17
- **构建工具**: Maven 3.8+
- **ORM**: MyBatis-Plus 3.5.7
- **数据库**: MySQL 8.0+
- **缓存**: Redis
- **安全**: Spring Security + JWT (jjwt 0.12.3)
- **文件存储**: MinIO 8.5.10
- **API文档**: Knife4j 4.5.0
- **工具库**: Hutool 5.8.28, EasyExcel 3.3.4

### 前端
- **框架**: Vue 3.4 + TypeScript 5.4
- **UI 库**: Element Plus 2.6
- **构建工具**: Vite 5.2
- **状态管理**: Pinia 2.1 (持久化插件)
- **HTTP 客户端**: Axios 1.7
- **富文本**: wangEditor 5.1

## 快速启动

### 前置依赖
1. MySQL 8.0+ (创建数据库并执行初始化脚本)
2. Redis 6.0+
3. JDK 17+
4. Node.js 18+
5. MinIO (可选，用于文件存储)

### 1. 初始化数据库

```sql
-- 执行建表脚本（包含全部 25 张表）
SOURCE database/init-schema.sql;
-- 执行初始数据脚本（包含角色、部门、岗位、示例数据等）
SOURCE database/init.sql;
```

默认账号:
| 账号 | 密码 | 角色 |
|------|------|------|
| `admin` | `admin123` | 超级管理员 |
| `employee` | `employee123` | 普通员工 |

### 2. 启动后端

```bash
cd backend
mvn clean package -DskipTests
java -jar target/hrms-backend-1.0.0.jar
```

API 文档地址: `http://localhost:8080/doc.html`

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

访问地址: `http://localhost:5173`

## 项目结构

```
HRMS/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/hrms/
│   │   ├── common/             # 公共模块 (实体基类、异常、结果封装、工具类)
│   │   ├── config/             # 配置 (CORS、Jackson、Knife4j、MinIO、MyBatis-Plus)
│   │   ├── security/           # 安全 (SecurityConfig、JWT过滤器、SecurityUser)
│   │   └── module/             # 业务模块
│   │       ├── auth/           # 认证模块
│   │       ├── employee/       # 员工管理
│   │       ├── org/            # 组织架构 (部门、岗位)
│   │       ├── attendance/     # 考勤管理 (考勤、请假)
│   │       ├── salary/         # 薪酬管理
│   │       ├── performance/    # 绩效管理
│   │       ├── recruitment/    # 招聘管理
│   │       ├── training/       # 培训管理
│   │       ├── contract/       # 合同管理
│   │       ├── transfer/       # 员工异动管理
│   │       ├── resignation/    # 离职管理
│   │       ├── probation/      # 转正管理
│   │       └── notice/         # 通知公告
│   └── src/main/resources/
│       ├── application.yml     # 全局配置
│       └── mapper/             # MyBatis XML 映射文件
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── api/                # API 接口
│   │   ├── views/              # 页面视图 (15+ 页面)
│   │   ├── layouts/            # 布局组件
│   │   ├── components/         # 通用组件
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── router/             # 路由配置
│   │   ├── utils/              # 工具函数
│   │   └── styles/             # 样式
│   └── dist/                   # 构建产物
└── database/                   # 数据库脚本
    ├── init-schema.sql         # 建表脚本 (25张表)
    └── init.sql                # 初始数据 + 示例数据
```

## 系统模块

### 管理端（管理员角色）

1. **首页面板** — 统计卡片、入职/离职趋势、待办事项、部门人数分布
2. **组织架构** — 部门管理(树形)、岗位管理
3. **员工管理** — 员工增删改查、详情(含教育/工作经历)、Excel 导出
4. **招聘管理** — 招聘需求、面试记录、Offer 管理
5. **考勤管理** — 考勤明细、请假审批
6. **薪酬管理** — 薪酬记录、薪资配置(含五险一金比例)
7. **绩效管理** — 绩效考核(自评/主管/HR)、绩效模板
8. **培训管理** — 培训计划、培训记录
9. **合同管理** — 合同全生命周期管理（新增/续签/到期处理）
10. **异动管理** — 员工晋升、平调、降职、转岗记录与审批
11. **离职管理** — 离职申请、审批、工作交接、资产归还跟踪
12. **转正管理** — 试用期评估、导师/主管评价、转正审批
13. **通知公告** — 公司公告、制度通知、活动通知发布管理

### 员工端（员工角色）

1. **我的首页** — 个人信息概览、待办提醒
2. **个人信息** — 查看/编辑个人资料
3. **我的考勤** — 考勤记录、签到签退
4. **我的请假** — 请假申请与记录查询
5. **我的工资** — 工资条查询
6. **我的绩效** — 绩效考核自评与结果查看
7. **我的培训** — 培训计划查看与报名
8. **我的合同** — 合同查看、续签申请
9. **我的异动** — 个人异动记录查看
10. **我的转正** — 转正进度与评估详情
11. **通知公告** — 公司通知公告（时间轴展示）
