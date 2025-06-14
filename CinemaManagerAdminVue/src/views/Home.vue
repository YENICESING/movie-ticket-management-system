<template>
  <el-container class="home-container">
    <!-- 顶部导航栏 -->
    <el-header class="main-header">
      <div class="header-left" @click="toWelcome">
        <img src="../assets/logo02.jpg" alt="Logo" class="logo-img">
        <h1 class="system-title">影院管理系统</h1>
      </div>
      <el-dropdown class="user-dropdown" @command="handleCommand" trigger="click">
        <div class="user-avatar">
          <el-avatar :src="require('../assets/default.jpg')" :size="40"></el-avatar>
          <i class="el-icon-arrow-down el-icon--right icon-arrow"></i>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="logout" v-if="!isToken">
            <i class="el-icon-switch-button"></i>退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-header>

    <el-container class="main-container">
      <!-- 侧边栏菜单 -->
      <el-aside class="main-sidebar">
        <el-menu
          class="sidebar-menu"
          background-color="#ffffff"
          text-color="#333333"
          active-text-color="#667eea"
          :unique-opened="true"
          :collapse-transition="false"
          :router="true"
          :default-active="$route.path">
          <!-- 一级菜单 -->
          <el-submenu
            v-for="item in menuList"
            :index="String(item.id)"
            :key="item.id">
            <template slot="title">
              <i :class="['menu-icon', iconList[item.id]]"></i>
              <span class="menu-title">{{item.name}}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item
              v-for="subItem in item.children"
              :index="'/' + subItem.path"
              :key="subItem.id">
              <i class="el-icon-right"></i>
              <span>{{subItem.name}}</span>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      // 左侧菜单数据
      menuList: [],
      // 菜单图标映射
      iconList: {
        '1': 'el-icon-s-home',  // 首页
        '2': 'el-icon-document', // 文档
        '3': 'el-icon-office-building', // 影院
        '4': 'el-icon-lock',     // 权限
        '5': 'el-icon-user',     // 用户
        '6': 'el-icon-edit',    // 编辑
      },
      isToken: false,
    }
  },
  created() {
    this.getMenuList()
  },
  methods: {
    logout() {
      window.sessionStorage.clear();
      this.$router.push('/login')
    },
    async getMenuList() {
      try {
        const loginUser = JSON.parse(window.sessionStorage.getItem("loginUser"));
        if (loginUser) {
          this.menuList = loginUser.sysUser.sysRole.children;
        }
        this.$router.push('/welcome');
      } catch (error) {
        console.error('解析 sessionStorage 中的 loginUser 时出错:', error);
      }
    },
    toWelcome() {
      this.$router.push('/welcome')
    },
    handleCommand(command) {
      if (command === "logout") {
        this.logout();
      } else {
        this.$router.push("/" + command);
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.home-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏样式 */
.main-header {
  height: 60px !important;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 10;

  .header-left {
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      opacity: 0.9;
    }

    .logo-img {
      height: 40px;
      width: 40px;
      border-radius: 50%;
      margin-right: 15px;
      border: 2px solid rgba(255, 255, 255, 0.3);
    }

    .system-title {
      color: white;
      font-size: 22px;
      font-weight: 600;
      margin: 0;
      letter-spacing: 1px;
    }
  }
}

/* 用户下拉菜单样式 */
.user-dropdown {
  .user-avatar {
    display: flex;
    align-items: center;
    cursor: pointer;
    padding: 5px 10px;
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.2);
    transition: all 0.3s;

    &:hover {
      background: rgba(255, 255, 255, 0.3);
    }

    .icon-arrow {
      color: white;
      margin-left: 5px;
      transition: transform 0.3s;
    }
  }

  &:hover .icon-arrow {
    transform: rotate(180deg);
  }
}

/* 主容器样式 */
.main-container {
  flex: 1;
  overflow: hidden;
}

/* 侧边栏样式 */
.main-sidebar {
  width: 220px !important;
  background-color: #ffffff;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.05);
  transition: width 0.3s;

  .sidebar-menu {
    border-right: none;
    height: 100%;

    .menu-icon {
      font-size: 18px;
      margin-right: 10px;
      color: #667eea;
    }

    .menu-title {
      font-size: 16px;
      font-weight: 500;
    }

    .el-submenu__title, .el-menu-item {
      height: 50px;
      line-height: 50px;
      transition: all 0.3s;

      &:hover {
        background-color: #f5f7fa !important;
      }
    }

    .el-menu-item {
      padding-left: 50px !important;
      font-size: 14px;

      &.is-active {
        background-color: #f0f5ff !important;
        color: #667eea !important;
        font-weight: 500;
      }
    }
  }
}

/* 主内容区样式 */
.main-content {
  background-color: #f5f7fa;
  padding: 20px;
  overflow-y: auto;

  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: rgba(144, 147, 153, 0.3);
    border-radius: 3px;
  }
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .main-sidebar {
    width: 64px !important;

    .menu-title {
      display: none;
    }

    .el-menu-item {
      padding-left: 20px !important;
      span {
        display: none;
      }
    }
  }
}
</style>
