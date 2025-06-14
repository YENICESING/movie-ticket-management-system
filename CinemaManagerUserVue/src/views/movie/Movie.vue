<template>
  <div class="movie-container">
    <!-- 顶部导航栏 -->
    <div class="app-header">
      <div class="header-content">
        <div class="logo">
          <i class="el-icon-film" style="color: #00dc82; font-size: 36px;"></i>
          <span style="font-size: 24px; margin-left: 10px; font-weight: 600;">MovieHub</span>
        </div>
        <div class="nav-menu">
          <div
            v-for="item in menuList"
            :key="item.id"
            class="nav-item"
            :class="{ active: $route.path === item.path }"
            @click="$router.push(item.path)"
          >
            <i :class="item.iconClass"></i>
            <span>{{ item.name }}</span>
            <div class="nav-indicator"></div>
          </div>
        </div>
        <div class="user-actions">
        </div>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 类别筛选 -->
      <div class="filter-section">
        <div class="filter-card">
          <div class="filter-header">
            <i class="iconfont icon-r-find" style="color: #00dc82; font-size: 24px;"></i>
            <h3>电影分类</h3>
          </div>
          <div class="category-tags">
            <el-radio-group v-model="categoryRadio" class="category-group">
              <el-radio-button
                v-for="item in [{movieCategoryId: 0, movieCategoryName: '全部'}, ...categoryList]"
                :key="item.movieCategoryId"
                :label="item.movieCategoryId"
                class="category-tag"
              >
                {{ item.movieCategoryName }}
              </el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </div>

      <!-- 路由视图 -->
      <div class="movie-view">
        <router-view
          :movieCategoryId="categoryRadio"
          :orderByColumn="orderByColumn"
        ></router-view>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Movie",
  data() {
    return {
      menuList: [
        {
          id: "1",
          name: "正在热映",
          path: "/movie/movieOngoing",
          iconClass: "el-icon-video-camera"
        },
        {
          id: "2",
          name: "即将上映",
          path: "/movie/movieUpcoming",
          iconClass: "el-icon-time"
        },
        {
          id: "3",
          name: "热播影片",
          path: "/movie/movieClassics",
          iconClass: "el-icon-star-on"
        }
      ],
      categoryRadio: 0,
      categoryList: [],
      orderByColumn: "releaseDate"
    };
  },
  created() {
    this.getCategoryList();
  },
  methods: {
    async getCategoryList() {
      const { data: res } = await axios.get("sysMovieCategory/find");
      if (res.code !== 200)
        return this.$message.error("获取服务器信息失败");
      this.categoryList = res.data;
    }
  },
  watch: {
    $route() {
      this.categoryRadio = 0;
      this.orderByColumn = "releaseDate";
    }
  }
};
</script>

<style scoped>
.movie-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f7fa;
}

/* 顶部导航栏样式 */
.app-header {
  background: linear-gradient(135deg, #1a1e2b, #2d3447);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 0 40px;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  display: flex;
  align-items: center;
  color: #fff;
  font-family: 'Poppins', sans-serif;
}

.nav-menu {
  display: flex;
  gap: 30px;
  margin-right: 170px;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  padding: 10px 0;
  position: relative;
  transition: all 0.3s ease;
}

.nav-item i {
  font-size: 22px;
  margin-bottom: 5px;
}

.nav-item span {
  font-size: 16px;
  font-weight: 500;
}

.nav-item:hover {
  color: #fff;
}

.nav-item.active {
  color: #00dc82;
}

.nav-item.active .nav-indicator {
  width: 30px;
  height: 3px;
  background: #00dc82;
  border-radius: 3px;
  position: absolute;
  bottom: 0;
}

.user-actions {
  display: flex;
  gap: 15px;
}

.user-actions .el-button {
  font-size: 20px;
}

/* 主内容区样式 */
.main-content {
  flex: 1;
  padding: 30px 40px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

/* 筛选区域样式 */
.filter-section {
  margin-bottom: 30px;
}

.filter-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.05);
  padding: 20px;
}

.filter-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.filter-header h3 {
  margin-left: 10px;
  font-size: 18px;
  color: #333;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.category-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.category-tag >>> .el-radio-button__inner {
  border: none !important;
  border-radius: 20px !important;
  background: #f0f2f5 !important;
  color: #666;
  padding: 8px 20px;
  transition: all 0.3s ease;
  font-size: 14px;
}

.category-tag.is-active >>> .el-radio-button__inner {
  background: linear-gradient(135deg, #00dc82, #00b674) !important;
  color: #fff !important;
  box-shadow: 0 4px 12px rgba(0, 220, 130, 0.3) !important;
}

.category-tag:not(.is-active):hover >>> .el-radio-button__inner {
  background: #e1e6eb !important;
  color: #333 !important;
}

/* 电影视图区域 */
.movie-view {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.05);
  padding: 25px;
}
</style>
