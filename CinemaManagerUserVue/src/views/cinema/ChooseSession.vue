<template>
  <div class="cinema-page">
    <!-- 影院头部信息 -->
    <div class="cinema-header">
      <div class="header-content">
        <div class="cinema-avatar">
          <img :src="cinemaInfo.cinemaPicture[0]" alt="影院图片">
        </div>
        <div class="cinema-info">
          <h1 class="cinema-name">{{ cinemaInfo.cinemaName }}</h1>
          <div class="info-item">
            <i class="el-icon-location"></i>
            <span>{{ cinemaInfo.cinemaAddress }}</span>
          </div>
          <div class="info-item">
            <i class="el-icon-phone"></i>
            <span>{{ cinemaInfo.cinemaPhone }}</span>
          </div>
          <div class="service-tags">
            <el-tag v-for="service in cinemaInfo.services" :key="service" type="info">{{ service }}</el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区 -->
    <div class="main-container">
      <!-- 面包屑导航 -->
      <div class="crumbs-nav-container">
        <el-breadcrumb separator=">">
          <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ cinemaInfo.cinemaName }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 正在上映电影列表 -->
      <div class="movie-list-container" v-if="cinemaInfo.sysMovieList && cinemaInfo.sysMovieList.length">
        <div class="scroll-prev scroll-btn" @click="prevPage">
          <i class="el-icon-arrow-left"></i>
        </div>
        <div class="scroll-next scroll-btn" @click="nextPage">
          <i class="el-icon-arrow-right"></i>
        </div>

        <div class="movie-list" ref="movieListRef" :style="{ transform: `translateX(${translateX}px)` }">
          <div
            class="movie"
            v-for="item in cinemaInfo.sysMovieList"
            :key="item.movieId"
            :class="{ active: item.movieId === activeMovie }"
            @click="activeMovie = item.movieId"
          >
            <img :src="global.base + JSON.parse(item.moviePoster)[0]" :alt="item.movieName">
            <div class="movie-title">{{ item.movieName }}</div>
          </div>
        </div>
      </div>

      <!-- 当前选中电影信息 -->
      <div class="current-movie" v-if="movieDict[activeMovie]">
        <div class="movie-basic">
          <h2 class="movie-name">{{ movieDict[activeMovie].movieName }}</h2>
          <div class="movie-meta">
            <span>{{ movieDict[activeMovie].movieLength }}分钟</span>
            <span>|</span>
            <span>{{ movieDict[activeMovie].movieCategoryList.join(' / ') }}</span>
          </div>
        </div>

        <!-- 日期选择 -->
        <div class="date-picker" v-if="sessions && sessions.length">
          <div
            class="date-item"
            v-for="(item, key) in sessionDict"
            :key="key"
            :class="{ active: activeDate === key }"
            @click="activeDate = key"
          >
            <div class="date-week">{{ calculateWeekday(key) }}</div>
            <div class="date-day">{{ calculateDay(key) }}</div>
          </div>
        </div>

        <!-- 场次列表 -->
        <div class="session-list" v-if="sessionDict[activeDate]">
          <div class="session-card" v-for="session in sessionDict[activeDate]" :key="session.sessionId">
            <div class="session-time">
              <span class="begin-time">{{ session.playTime }}</span>
              <span class="end-time">{{ session.endTime }}散场</span>
            </div>
            <div class="session-detail">
              <div class="detail-item">
                <div class="detail-label">语言版本</div>
                <div class="detail-value">{{ session.languageVersion }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">放映厅</div>
                <div class="detail-value">{{ session.sysHall.hallName }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">影厅提示</div>
                <div class="detail-value">{{ session.sessionTips || '无' }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">售价</div>
                <div class="detail-value price">{{ session.sessionPrice }}元</div>
              </div>
            </div>
            <el-button
              type="danger"
              round
              @click="toChooseSeat(session.sessionId)"
              class="book-btn"
            >
              选座购票
            </el-button>
          </div>
        </div>

        <div class="no-session" v-else>
          <el-empty description="暂无场次信息"></el-empty>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment';
export default {
  name: "CinemaInfo",
  data() {
    return {
      cinemaId: this.$route.params.cinemaId,
      cinemaInfo: {
        cinemaName: "",
        cinemaAddress: "",
        cinemaPhone: "",
        cinemaPicture: [],
        services: ["退", "改签", "折扣卡", "IMAX厅"],
        sysMovieList: [],
      },
      movieDict: {},
      sessionDict: {},
      sessions: [],
      activeMovie: 0,
      activeDate: "",
      translateX: 0,
      movieItemWidth: 180,
      visibleMovies: 6
    };
  },
  watch: {
    activeMovie() {
      this.getNewSessionList();
    },
  },
  created() {
    this.getCinemaMovieList();
  },
  methods: {
    async getCinemaMovieList() {
      try {
        const { data: res } = await axios.get("sysCinema/find/" + this.cinemaId);
        this.cinemaInfo = {
          ...this.cinemaInfo,
          ...res.data.cinema,
          cinemaPicture: JSON.parse(res.data.cinema.cinemaPicture).map(obj => this.global.base + obj)
        };

        this.sessions = res.data.sessions || [];

        // 处理电影数据
        this.cinemaInfo.sysMovieList.forEach(movie => {
          this.movieDict[movie.movieId] = {
            ...movie,
            movieCategoryList: movie.movieCategoryList.map(cat => cat.movieCategoryName)
          };
        });

        // 设置默认选中的电影
        if (this.sessions.length > 0) {
          this.activeMovie = this.sessions[0].movieId;
        } else if (this.cinemaInfo.sysMovieList.length > 0) {
          this.activeMovie = this.cinemaInfo.sysMovieList[0].movieId;
        }

        // 处理场次数据，按日期分组
        this.processSessions();
      } catch (error) {
        this.$message.error("获取影院信息失败");
        console.error(error);
      }
    },

    async getNewSessionList() {
      try {
        const { data: res } = await axios.get(
          "sysCinema/find/" + this.cinemaId + "/" + this.activeMovie
        );
        this.sessions = res.data.sessions || [];
        this.processSessions();
      } catch (error) {
        this.$message.error("获取场次信息失败");
        console.error(error);
      }
    },

    processSessions() {
      this.sessionDict = {};
      this.sessions.forEach(session => {
        if (!this.sessionDict[session.sessionDate]) {
          this.sessionDict[session.sessionDate] = [];
        }
        this.sessionDict[session.sessionDate].push(session);
      });

      // 设置默认选中的日期
      if (this.sessions.length > 0) {
        this.activeDate = this.sessions[0].sessionDate;
      } else {
        this.activeDate = "";
      }
    },

    prevPage() {
      const maxTranslateX = 0;
      this.translateX = Math.min(this.translateX + this.movieItemWidth * 3, maxTranslateX);
    },

    nextPage() {
      const movieListWidth = this.cinemaInfo.sysMovieList.length * this.movieItemWidth;
      const containerWidth = this.visibleMovies * this.movieItemWidth;
      const minTranslateX = containerWidth - movieListWidth;
      this.translateX = Math.max(this.translateX - this.movieItemWidth * 3, minTranslateX);
    },

    toChooseSeat(sessionId) {
      this.$router.push("/chooseSeat/" + sessionId);
    },

    calculateDate(date) {
      return moment(date).format("MM月DD日");
    },

    calculateDay(date) {
      return moment(date).format("DD");
    },

    calculateWeekday(date) {
      const weekdays = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
      return weekdays[moment(date).day()];
    }
  }
};
</script>

<style scoped>
/* 基础样式 */
.cinema-page {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", Arial, sans-serif;
  color: #333;
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 影院头部样式 */
.cinema-header {
  background: linear-gradient(135deg, #2c3e50, #4a6491);
  color: white;
  padding: 2rem 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.cinema-avatar {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  flex-shrink: 0;
}

.cinema-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cinema-info {
  flex-grow: 1;
}

.cinema-name {
  font-size: 28px;
  font-weight: 500;
  margin-bottom: 10px;
}

.info-item {
  display: flex;
  align-items: center;
  margin: 8px 0;
  font-size: 16px;
}

.info-item i {
  margin-right: 8px;
  font-size: 18px;
}

.service-tags {
  margin-top: 15px;
}

.service-tags .el-tag {
  margin-right: 10px;
  border-radius: 4px;
}

/* 主要内容区 */
.main-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transform: translateY(-30px);
  position: relative;
  z-index: 1;
}

.crumbs-nav-container {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

/* 电影列表样式 */
.movie-list-container {
  position: relative;
  margin: 30px 0;
  overflow: hidden;
}

.scroll-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  background-color: white;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: all 0.3s;
}

.scroll-btn:hover {
  background-color: #f5f5f5;
  transform: translateY(-50%) scale(1.1);
}

.scroll-btn i {
  font-size: 20px;
  color: #666;
}

.scroll-prev {
  left: -20px;
}

.scroll-next {
  right: -20px;
}

.movie-list {
  display: flex;
  gap: 20px;
  padding: 15px 0;
  transition: transform 0.3s ease;
  will-change: transform;
}

.movie {
  flex: 0 0 160px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.movie img {
  width: 100%;
  height: 220px;
  border-radius: 8px;
  object-fit: cover;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.movie-title {
  margin-top: 10px;
  font-size: 14px;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.movie.active img {
  box-shadow: 0 4px 16px rgba(231, 76, 60, 0.3);
  transform: scale(1.05);
}

.movie.active::after {
  content: "";
  position: absolute;
  bottom: -5px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 3px;
  background-color: #e74c3c;
  border-radius: 3px;
}

/* 当前电影信息 */
.current-movie {
  margin-top: 40px;
}

.movie-basic {
  margin-bottom: 30px;
}

.movie-name {
  font-size: 24px;
  font-weight: 500;
  margin-bottom: 10px;
}

.movie-meta {
  color: #666;
  font-size: 14px;
}

.movie-meta span {
  margin-right: 10px;
}

/* 日期选择器 */
.date-picker {
  display: flex;
  gap: 15px;
  margin: 30px 0;
  overflow-x: auto;
  padding-bottom: 10px;
}

.date-item {
  flex: 0 0 80px;
  text-align: center;
  padding: 12px 0;
  border-radius: 8px;
  background-color: #f8f8f8;
  cursor: pointer;
  transition: all 0.3s;
}

.date-item:hover {
  background-color: #eee;
}

.date-item.active {
  background-color: #e74c3c;
  color: white;
}

.date-week {
  font-size: 14px;
  margin-bottom: 5px;
}

.date-day {
  font-size: 18px;
  font-weight: bold;
}

/* 场次列表 */
.session-list {
  display: grid;
  gap: 20px;
}

.session-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s, box-shadow 0.3s;
}

.session-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.session-time {
  margin-bottom: 15px;
}

.begin-time {
  font-size: 20px;
  font-weight: bold;
  color: #e74c3c;
  margin-right: 10px;
}

.end-time {
  font-size: 14px;
  color: #999;
}

.session-detail {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
  margin: 15px 0;
}

.detail-item {
  margin-bottom: 10px;
}

.detail-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 5px;
}

.detail-value {
  font-size: 16px;
  color: #333;
}

.price {
  color: #e74c3c;
  font-weight: bold;
  font-size: 18px;
}

.book-btn {
  width: 100%;
  padding: 12px 0 !important;
  font-size: 16px !important;
  margin-top: 10px;
}

.no-session {
  padding: 50px 0;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    text-align: center;
  }

  .cinema-avatar {
    margin-right: 0;
    margin-bottom: 20px;
  }

  .movie {
    flex: 0 0 120px;
  }

  .movie img {
    height: 180px;
  }

  .session-detail {
    grid-template-columns: 1fr 1fr;
  }

  .scroll-btn {
    width: 30px;
    height: 30px;
  }

  .scroll-prev {
    left: -15px;
  }

  .scroll-next {
    right: -15px;
  }
}

@media (max-width: 480px) {
  .session-detail {
    grid-template-columns: 1fr;
  }

  .date-item {
    flex: 0 0 70px;
  }
}
</style>
