<template>
  <div class="movie-container">
    <div class="header">
      <div class="header-inner">
        <!-- 横向布局容器 -->
        <div class="movie-info-main">
          <!-- 电影海报 -->
          <div class="movie-poster">
            <img class="avatar" :src="movieInfo.moviePoster">
          </div>

          <!-- 右侧内容区 -->
          <div class="movie-info-right-content">
            <!-- 电影标题和基础信息 -->
            <div class="movie-basic-info">
              <h1 class="movie-name">{{movieInfo.movieName}}</h1>
              <div class="movie-category">{{movieInfo.movieCategoryList}}</div>
              <div class="movie-meta">
                {{movieInfo.movieArea}} / {{movieInfo.movieLength}}分钟 |
                {{movieInfo.releaseDate}} {{movieInfo.movieArea}} 上映
              </div>
            </div>

            <!-- 剧情简介 -->
            <div class="plot-summary">
              <p>{{movieInfo.movieIntroduction}}</p>
            </div>

            <!-- 底部操作区 -->
            <div class="action-area">
              <!-- 购票按钮 -->
              <div class="buy-ticket-btn">
                <el-button class="buy-btn" type="primary" @click="toChooseSession">
                  ✔ 特惠购票
                </el-button>
              </div>

              <!-- 票房信息 -->
              <div class="movie-box-office">
                <div class="movie-index-title">累计票房</div>
                <div class="box-office-container">
                  <span class="box-office">{{movieInfo.movieBoxOffice}}</span>
                  <span class="unit">元</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 面包屑导航 -->
        <div class="crumbs-nav-container">
          <a href="/welcome">首页</a> > <a href="/movie">电影</a> > {{movieInfo.movieName}}
        </div>

        <!-- 图集选项卡 -->
        <el-tabs v-model="activeName">
          <el-tab-pane label="图集" name="pictures">
            <div class="tab-body">
              <div class="pictures-list">
                <div class="picture-grid">
                  <div v-if="movieInfo.moviePictures.length === 0" class="empty-tips">
                    暂无图片资源
                  </div>
                  <el-image
                    v-for="(item, index) in movieInfo.moviePictures"
                    :key="index"
                    class="gallery-img"
                    :src="item"
                    :preview-src-list="movieInfo.moviePictures"
                    fit="cover"
                  />
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MovieInfo",
  data() {
    return {
      movieInfo: {
        moviePictures: [],
        movieBoxOffice: 0
      },
      movieId: this.$route.params.movieId,
      activeName: 'pictures',
      httpURL: this.global.base
    }
  },
  created() {
    this.getMovieInfo()
  },
  methods: {
    async getMovieInfo(){
      try {
        const {data : res} = await axios.get('sysMovie/find/' + this.movieId)
        if(res.code !== 200) throw new Error('数据查询失败')

        // 处理图片路径
        this.movieInfo = {
          ...res.data,
          moviePoster: this.httpURL + JSON.parse(res.data.moviePoster)[0],
          moviePictures: JSON.parse(res.data.moviePictures).map(img => this.httpURL + img),
          movieCategoryList: res.data.movieCategoryList
            .map(c => c.movieCategoryName)
            .join(" / ")
        }
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    toChooseSession() {
      this.$router.push(`/chooseSession/1`)
    }
  }
}
</script>

<style scoped>
/* 基础容器 */
.movie-container {
  background: linear-gradient(
    135deg,
    #ffffff 0%,  /* 纯白 */
    #f8f9fa 100%  /* 浅灰 */
  );
  min-height: 100vh;
  padding: 30px 0;
}

/* 主内容区 */
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  background: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.05);
  padding: 24px;
}

/* 横向布局容器 */
.movie-info-main {
  display: flex;
  gap: 32px;
  position: relative;
}

/* 海报样式 */
.movie-poster {
  width: 280px;
  height: 420px;
  flex-shrink: 0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  background: #f8f9fa;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.avatar:hover {
  transform: scale(1.03);
}

/* 右侧内容区 */
.movie-info-right-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-height: 420px;
}

/* 基本信息样式 */
.movie-name {
  font-size: 32px;
  margin: 0 0 12px;
  color: #2c3e50;
  line-height: 1.2;
}

.movie-category {
  color: #409EFF;
  font-weight: 500;
  margin-bottom: 8px;
  font-size: 16px;
}

.movie-meta {
  color: #666;
  font-size: 14px;
  margin-bottom: 20px;
}

/* 剧情简介 */
.plot-summary {
  flex: 1;
  line-height: 1.8;
  color: #4a5568;
  font-size: 15px;
  overflow-y: auto;
  padding-right: 15px;
  margin: 15px 0;
}

/* 底部操作区 */
.action-area {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: 20px;
}

.buy-ticket-btn .buy-btn {
  width: 180px;
  height: 48px;
  font-size: 18px !important;
  border-radius: 24px !important;
  transition: all 0.3s;
  background: linear-gradient(to right, #409EFF, #36c2cf) !important;
  border: none !important;
  color: white !important;
}

.movie-box-office {
  text-align: right;
}

.box-office {
  font-size: 28px;
  color: #ff9100;
  font-weight: bold;
}

.unit {
  font-size: 14px;
  color: #666;
}

/* 图集样式 */
.picture-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  padding: 20px 0;
}

.gallery-img {
  height: 150px;
  border-radius: 8px;
  transition: transform 0.3s;
  cursor: pointer;
  border: 1px solid #eee;
}

.gallery-img:hover {
  transform: translateY(-3px);
}

/* 面包屑导航 */
.crumbs-nav-container {
  color: #666;
  margin: 24px 0 16px;
  font-size: 14px;
}

.crumbs-nav-container a {
  color: #409EFF;
  transition: color 0.3s;
  text-decoration: none;
}

.crumbs-nav-container a:hover {
  color: #297acc;
}

/* 响应式处理 */
@media (max-width: 768px) {
  .movie-info-main {
    flex-direction: column;
  }

  .movie-poster {
    width: 100%;
    height: auto;
    max-width: 400px;
    margin: 0 auto;
  }

  .movie-info-right-content {
    min-height: auto;
  }

  .action-area {
    flex-direction: column;
    gap: 20px;
    align-items: flex-start;
  }

  .movie-name {
    font-size: 24px;
  }

  .gallery-img {
    height: 120px;
  }

  .buy-ticket-btn .buy-btn {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .header-inner {
    padding: 16px;
  }

  .movie-name {
    font-size: 22px;
  }

  .movie-category {
    font-size: 14px;
  }

  .movie-meta {
    font-size: 13px;
  }

  .plot-summary {
    font-size: 14px;
  }

  .box-office {
    font-size: 24px;
  }
}
</style>
