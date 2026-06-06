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
-- 执行建表脚本
SOURCE database/init-schema.sql;
-- 执行初始数据脚本
SOURCE database/init-data.sql;
```

默认管理员账号: `admin` / `admin123`

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
│   │       └── system/         # 系统管理 (菜单、字典、日志)
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
    ├── init-schema.sql         # 建表脚本 (22张表)
    └── init-data.sql           # 初始数据脚本
```

## 系统模块

1. **首页面板** - 统计卡片、入职/离职趋势、待办事项、部门人数分布
2. **组织架构** - 部门管理(树形)、岗位管理
3. **员工管理** - 员工增删改查、详情(含教育/工作经历)、Excel 导出
4. **招聘管理** - 招聘需求、面试记录、Offer 管理
5. **考勤管理** - 考勤明细、请假申请
6. **薪酬管理** - 薪酬记录、薪资配置(含五险一金比例)
7. **绩效管理** - 绩效考核(自评/主管/HR)、绩效模板
8. **培训管理** - 培训计划、培训记录
9. **合同管理** - 合同全生命周期管理
10. **系统管理** - 菜单管理、字典管理、操作日志
