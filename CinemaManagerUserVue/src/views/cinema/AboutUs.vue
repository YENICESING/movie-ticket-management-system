/*该组件通过模板展示影院信息，使用脚本获取并处理影院数据，利用样式美化页面布局。组件的主要功能包括显示影院基本信息、放映厅类别和图片，
以及提供选座购票的跳转功能。*/
<template>
  <div class="cinema-container">
    <!-- 头部标题 -->
    <div class="cinema-header">
      <h1 class="cinema-title">
        <i class="iconfont icon-r-building"></i>
        影院信息
      </h1>
    </div>

    <!-- 主体内容 -->
    <div class="cinema-body">
      <!-- 左侧信息区 -->
      <div class="cinema-info">
        <!-- 基本信息 -->
        <div class="info-card">
          <h2 class="cinema-name">{{ cinemaInfo.cinemaName || '未知影院' }}</h2>
          <div class="info-item" v-if="cinemaInfo.cinemaAddress">
            <i class="el-icon-location"></i>
            <span>{{ cinemaInfo.cinemaAddress }}</span>
          </div>
          <div class="info-item" v-if="cinemaInfo.cinemaPhone">
            <i class="el-icon-phone"></i>
            <span>{{ cinemaInfo.cinemaPhone }}</span>
          </div>
        </div>

        <!-- 影厅标签 -->
        <div class="hall-tags">
          <el-tag
            v-for="hall in halls"
            :key="hall"
            type="warning"
            effect="dark"
            class="hall-tag"
          >
            {{ hall }}
          </el-tag>
        </div>

        <!-- 操作按钮 -->
        <el-button
          @click="toChooseSession(cinemaInfo.cinemaId)"
          type="danger"
          class="purchase-btn"
          :disabled="!cinemaInfo.cinemaId"
        >
          <i class="el-icon-tickets"></i>
          立即选座购票
        </el-button>
      </div>

      <!-- 右侧图片展示 -->
      <div class="cinema-gallery">
        <div class="gallery-grid">
          <div
            v-for="(item, index) in pics"
            :key="index"
          >
            <img :src="item.url" class="popover-img" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "Cinema",
  data() {
    return {
      cinemaInfo: {},
      halls: [],
      pics: [],
      sorry: false
    }
  },
  created() {
    this.getCinemaInfo()
  },
  methods: {
    async getCinemaInfo(){
      try {
        const { data } = await axios.get('sysCinema');
        this.cinemaInfo = data.data;
        this.pics = JSON.parse(this.cinemaInfo.cinemaPicture).map(item => ({
          name: '',
          url: this.global.base + item
        }));
        this.halls = JSON.parse(this.cinemaInfo.hallCategoryList);
      } catch (error) {
        console.error('获取影院信息失败:', error);
        this.$message.error('获取影院信息失败，请稍后重试');
      }
    },
    toChooseSession(id){
      this.$router.push('/chooseSession/' + id)
    }
  }
}
</script>

<style scoped>
/* 整体容器 */
.cinema-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 2rem 1rem;
  box-sizing: border-box;
}

/* 头部样式 */
.cinema-header {
  text-align: center;
  margin-bottom: 3rem;
  border-bottom: 2px solid #eee;
  padding-bottom: 1.5rem;
}

.cinema-title {
  font-size: 2.2rem;
  color: #2c3e50;
  display: inline-flex;
  align-items: center;
  gap: 0.8rem;
}

.icon-r-building {
  font-size: 2.8rem;
  color: #409EFF;
}

/* 主体布局 */
.cinema-body {
  display: grid;
  gap: 2.5rem;
  grid-template-columns: 1fr 1.2fr;
}

/* 信息卡片样式 */
.info-card {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  margin-bottom: 2rem;
  transition: transform 0.3s;
}

.info-card:hover {
  transform: translateY(-5px);
}

.cinema-name {
  font-size: 1.8rem;
  color: #1a1a1a;
  margin-bottom: 1.5rem;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin: 1.2rem 0;
  font-size: 1.1rem;
  color: #666;
}

.info-item i {
  font-size: 1.4rem;
  color: #409EFF;
}

/* 影厅标签 */
.hall-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin: 2rem 0;
}

.hall-tag {
  font-size: 1rem;
  padding: 0.3rem 1.2rem 2rem 1.2rem;
  border-radius: 20px;
  transition: all 0.3s;
  background: #f9a825 !important;
  color: white !important;
}

.hall-tag:hover {
  transform: scale(1.05);
}

/* 购票按钮 */
.purchase-btn {
  width: 100%;
  padding: 1.2rem;
  font-size: 1.1rem;
  letter-spacing: 1px;
  transition: transform 0.3s;
  border-radius: 8px;
  background: #ee4040 !important;
  border: 2px solid #ee4040 !important;
}

.purchase-btn:hover {
  transform: translateY(-3px);
  background: #ff6b6b !important;
  border-color: #ff6b6b !important;
}

.purchase-btn:disabled {
  background: #ccc !important;
  border-color: #ccc !important;
  cursor: not-allowed;
}

/* 图片展示区 */
.gallery-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.2rem;
  border-radius: 12px;
  overflow: hidden;
}

.gallery-thumb {
  width: 100%;
  height: 400px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}


.popover-img {
  width: 100%;
  height: auto;
  max-height: 80vh;
  object-fit: contain;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .cinema-body {
    grid-template-columns: 1fr;
  }

  .cinema-gallery {
    order: -1;
  }

  .cinema-title {
    font-size: 1.8rem;
  }

  .info-card {
    padding: 1.5rem;
  }
}

@media (max-width: 576px) {
  .gallery-grid {
    grid-template-columns: 1fr;
  }

  .gallery-thumb {
    height: 180px;
  }

  .cinema-container {
    padding: 1.5rem 0.8rem;
  }

  .purchase-btn {
    padding: 1rem;
  }
}
</style>
