import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from "vue-router";
import { getToken, removeToken } from "@/utils/auth";
import { getUserInfo } from "@/api/auth";
import Layout from "@/layouts/index.vue";
import UserLayout from "@/layouts/UserLayout.vue";

const routes: RouteRecordRaw[] = [
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/login/index.vue"),
    meta: { title: "登录", hidden: true },
  },
  {
    path: "/",
    component: Layout,
    redirect: "/dashboard",
    meta: { title: "首页", icon: "HomeFilled" },
    children: [
      {
        path: "dashboard",
        name: "Dashboard",
        component: () => import("@/views/dashboard/index.vue"),
        meta: { title: "首页面板", icon: "HomeFilled" },
      },
      {
        path: "profile",
        name: "Profile",
        component: () => import("@/views/profile/index.vue"),
        meta: { title: "个人信息", icon: "User", hidden: true },
      },
    ],
  },
  {
    path: "/org",
    component: Layout,
    redirect: "/org/dept",
    meta: { title: "组织架构", icon: "OfficeBuilding" },
    children: [
      {
        path: "dept",
        name: "Dept",
        component: () => import("@/views/org/Dept.vue"),
        meta: { title: "部门管理", icon: "List" },
      },
      {
        path: "position",
        name: "Position",
        component: () => import("@/views/org/Position.vue"),
        meta: { title: "岗位管理", icon: "Avatar" },
      },
    ],
  },
  {
    path: "/employee",
    component: Layout,
    meta: { title: "员工管理", icon: "UserFilled" },
    children: [
      {
        path: "",
        name: "Employee",
        component: () => import("@/views/employee/index.vue"),
        meta: { title: "员工管理", icon: "UserFilled" },
      },
      {
        path: "detail/:id",
        name: "EmployeeDetail",
        component: () => import("@/views/employee/Detail.vue"),
        meta: { title: "员工详情", hidden: true, activeMenu: "/employee" },
      },
    ],
  },
  {
    path: "/recruitment",
    component: Layout,
    redirect: "/recruitment/demand",
    meta: { title: "招聘管理", icon: "Briefcase" },
    children: [
      {
        path: "demand",
        name: "RecruitmentDemand",
        component: () => import("@/views/recruitment/Demand.vue"),
        meta: { title: "招聘需求", icon: "DocumentAdd" },
      },
      {
        path: "interview",
        name: "RecruitmentInterview",
        component: () => import("@/views/recruitment/Interview.vue"),
        meta: { title: "面试记录", icon: "ChatDotSquare" },
      },
      {
        path: "offer",
        name: "RecruitmentOffer",
        component: () => import("@/views/recruitment/Offer.vue"),
        meta: { title: "Offer管理", icon: "Tickets" },
      },
    ],
  },
  {
    path: "/attendance",
    component: Layout,
    meta: { title: "考勤管理", icon: "Clock" },
    children: [
      {
        path: "",
        name: "Attendance",
        component: () => import("@/views/attendance/index.vue"),
        meta: { title: "考勤明细", icon: "DataBoard" },
      },
      {
        path: "leave",
        name: "AttendanceLeave",
        component: () => import("@/views/attendance/Leave.vue"),
        meta: { title: "请假管理", icon: "EditPen" },
      },
    ],
  },
  {
    path: "/salary",
    component: Layout,
    meta: { title: "薪酬管理", icon: "Money" },
    children: [
      {
        path: "",
        name: "Salary",
        component: () => import("@/views/salary/index.vue"),
        meta: { title: "薪酬管理", icon: "Coin" },
      },
      {
        path: "config",
        name: "SalaryConfig",
        component: () => import("@/views/salary/Config.vue"),
        meta: { title: "薪资配置", icon: "Setting" },
      },
    ],
  },
  {
    path: "/performance",
    component: Layout,
    meta: { title: "绩效管理", icon: "DataAnalysis" },
    children: [
      {
        path: "",
        name: "Performance",
        component: () => import("@/views/performance/index.vue"),
        meta: { title: "绩效考核", icon: "DataBoard" },
      },
      {
        path: "template",
        name: "PerformanceTemplate",
        component: () => import("@/views/performance/Template.vue"),
        meta: { title: "绩效模板", icon: "CopyDocument" },
      },
    ],
  },
  {
    path: "/training",
    component: Layout,
    meta: { title: "培训管理", icon: "Reading" },
    children: [
      {
        path: "",
        name: "Training",
        component: () => import("@/views/training/index.vue"),
        meta: { title: "培训管理", icon: "Reading" },
      },
    ],
  },
  {
    path: "/contract",
    component: Layout,
    meta: { title: "合同管理", icon: "Document" },
    children: [
      {
        path: "",
        name: "Contract",
        component: () => import("@/views/contract/index.vue"),
        meta: { title: "合同管理", icon: "Document" },
      },
    ],
  },

  {
    path: "/transfer",
    component: Layout,
    meta: { title: "异动管理", icon: "Sort" },
    children: [
      {
        path: "",
        name: "Transfer",
        component: () => import("@/views/transfer/index.vue"),
        meta: { title: "异动管理", icon: "Sort" },
      },
    ],
  },
  {
    path: "/resignation",
    component: Layout,
    meta: { title: "离职管理", icon: "SwitchButton" },
    children: [
      {
        path: "",
        name: "Resignation",
        component: () => import("@/views/resignation/index.vue"),
        meta: { title: "离职管理", icon: "SwitchButton" },
      },
    ],
  },
  {
    path: "/probation",
    component: Layout,
    meta: { title: "转正管理", icon: "Medal" },
    children: [
      {
        path: "",
        name: "Probation",
        component: () => import("@/views/probation/index.vue"),
        meta: { title: "转正管理", icon: "Medal" },
      },
    ],
  },
  {
    path: "/notice",
    component: Layout,
    meta: { title: "通知公告", icon: "Bell" },
    children: [
      {
        path: "",
        name: "Notice",
        component: () => import("@/views/notice/index.vue"),
        meta: { title: "通知公告", icon: "Bell" },
      },
    ],
  },

  {
    path: "/user",
    component: UserLayout,
    meta: { title: "员工门户", hidden: true },
    children: [
      {
        path: "dashboard",
        component: () => import("@/views/user/Dashboard.vue"),
        meta: { title: "我的首页" },
      },
      {
        path: "profile",
        component: () => import("@/views/user/Profile.vue"),
        meta: { title: "个人信息" },
      },
      {
        path: "attendance",
        component: () => import("@/views/user/Attendance.vue"),
        meta: { title: "我的考勤" },
      },
      {
        path: "leave",
        component: () => import("@/views/user/Leave.vue"),
        meta: { title: "我的请假" },
      },
      {
        path: "salary",
        component: () => import("@/views/user/Salary.vue"),
        meta: { title: "我的工资" },
      },
      {
        path: "performance",
        component: () => import("@/views/user/Performance.vue"),
        meta: { title: "我的绩效" },
      },
      {
        path: "training",
        component: () => import("@/views/user/Training.vue"),
        meta: { title: "我的培训" },
      },
      {
        path: "contract",
        component: () => import("@/views/user/Contract.vue"),
        meta: { title: "我的合同" },
      },
      {
        path: "transfer",
        component: () => import("@/views/user/Transfer.vue"),
        meta: { title: "我的异动" },
      },
      {
        path: "probation",
        component: () => import("@/views/user/Probation.vue"),
        meta: { title: "我的转正" },
      },
      {
        path: "notice",
        component: () => import("@/views/user/Notice.vue"),
        meta: { title: "通知公告" },
      },
      {
        path: "resignation",
        component: () => import("@/views/user/Resignation.vue"),
        meta: { title: "离职申请" },
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("@/views/error/NotFound.vue"),
    meta: { title: "404", hidden: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Cached user info to avoid repeated API calls on every navigation
let userInfoCache: { roles: string[] } | null = null;
let userInfoPromise: Promise<any> | null = null;

// Navigation guard
router.beforeEach(async (to, _from, next) => {
  const token = getToken();

  // 如果要去登录页
  if (to.path === "/login") {
    if (token) {
      // 有 token 但尝试去 login，验证 token 是否有效
      try {
        const res = await getUserInfo();
        if (res.data.code === 200) {
          // 有效 token，根据角色重定向
          const roles: string[] = res.data.data?.roles || [];
          next(roles.includes("employee") ? "/user/dashboard" : "/dashboard");
          return;
        }
      } catch {
        // token 无效，清除并进入登录页
        clearToken();
      }
    }
    next();
    return;
  }

  // 没有 token → 去登录
  if (!token) {
    clearToken();
    next("/login");
    return;
  }

  // 有 token，验证是否有效（首次加载时验证，后续使用缓存）
  if (!userInfoCache) {
    // Use a shared promise to avoid concurrent API calls
    if (!userInfoPromise) {
      userInfoPromise = getUserInfo().finally(() => {
        userInfoPromise = null;
      });
    }
    try {
      const res = await userInfoPromise;
      if (res.data.code !== 200) {
        // Token 无效
        clearToken();
        next("/login");
        return;
      }
      userInfoCache = { roles: res.data.data?.roles || [] };
    } catch {
      // Token 过期或网络错误
      clearToken();
      next("/login");
      return;
    }
  }

  next();
});

function clearToken() {
  removeToken();
  localStorage.removeItem("hrms-user");
  localStorage.removeItem("hrms_access_token");
  localStorage.removeItem("hrms_refresh_token");
  userInfoCache = null;
}

export default router;
