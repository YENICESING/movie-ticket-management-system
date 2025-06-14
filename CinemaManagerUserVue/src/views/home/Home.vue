<template>
  <div class="wp">
    <el-carousel indicator-position="outside" height="350px" style="width: 90%; margin: 0 auto;">
      <el-carousel-item v-for="item in carouselItems" :key="item">
        <img class="carousel-image" :src="item.image"  @click="handleCarouselClick(item.link)"/>
      </el-carousel-item>
    </el-carousel>
    <div class="whole">
      <div class="left">
        <div class="panel">
          <div class="panel-content">
            <movie-item
              :movieItem="item"
              v-for="(item, index) in classicMovieList"
              :key="index"
            ></movie-item>
          </div>
        </div>
      </div>
    </div>
    <footer class="site-footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3>关于我们</h3>
          <p>提供最新最全的电影资讯，打造最好的观影体验</p>
        </div>
        <div class="footer-section">
          <h3>快速链接</h3>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/movie">电影</router-link></li>
            <li><router-link to="/rankingList">榜单</router-link></li>
            <li><router-link to="/aboutUs">关于</router-link></li>
          </ul>
        </div>
        <div class="footer-section">
          <h3>联系我们</h3>
          <p>邮箱: contact@moviehub.com</p>
          <p>电话: 400-123-4567</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2025 MovieHub 电影网. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script>
import movieItem from "../../components/movie/movie-item";
import moment from "moment";
export default {
  name: "Home",
  components: {
    movieItem,
  },
  data() {
    return {
      queryInfo3: {
        total: 0,
        pageSize: 20,
        pageNum: 1,
      },
      carouselItems: [
        {
          image: require("@/assets/daomu.jpg"),link: '/movieInfo/49'
        },
        {
          image: require("@/assets/podiyu.jpg"),link: '/movieInfo/8'
        },
        {
          image: require("@/assets/wuzhu.jpg"),link: '/movieInfo/48'
        }
      ],
      classicMovieList: [],
    };
  },
  created() {
    this.getClassicMovieList();
    this.getHeight();
  },
  methods: {
    handleCarouselClick(link) {
      this.$router.push(link);
    },
    async getClassicMovieList() {
      const { data: res } = await axios.get("sysMovie/find", {
        params: this.queryInfo3,
      });
      if (res == undefined) {
        return;
      }
      this.classicMovieList = res.data;
      this.total = res.total;
    },
    getHeight() {
      let clientWidth = `${document.documentElement.clientWidth}`;
      clientWidth *= 0.8;
      this.carouselHeight = (clientWidth / 1700) * 520 + "px";
    },
  },
};
</script>

<style scoped>
.el-carousel {
  margin-bottom: 30px;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  cursor: pointer;
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.wp {
  background-color: #1f2935;
}

.whole {
  width: 100%;
  max-width: 1250px;
  margin: 0px auto;
  display: flex;
  gap: 30px;
}

.left {
  width: 100%;
}

.panel {
  background-color: #1f2935;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  padding: 10px;
}

.panel-content {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding-left: 70px;
}

.site-footer {
  background-color: #0f1721;
  color: #a0aec0;
  padding: 40px 0 0;
  margin-top: 50px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 40px;
  padding: 0 20px;
}

.footer-section h3 {
  color: #fff;
  font-size: 18px;
  margin-bottom: 20px;
}

.footer-section p, .footer-section li {
  margin-bottom: 10px;
  line-height: 1.6;
}

.footer-section a {
  color: #a0aec0;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #fff;
}

.footer-bottom {
  text-align: center;
  padding: 20px 0;
  margin-top: 40px;
  border-top: 1px solid #2d3748;
}
</style>
