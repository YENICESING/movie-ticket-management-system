/*这段代码实现了一个电影展示组件，接收父组件传递的电影信息对象 movieItem，展示电影的海报、标题。当鼠标悬停在海报上时，会显示一个包含更多电
影信息（如电影类型、上映时间）的层。同时，点击海报或标题可以跳转到电影详情页。代码通过 Vue 的响应式数据绑定和生命周期钩子函数，
实现了数据的处理和视图的更新。样式部分使用 CSS 对组件的外观进行了设计。*/
<template>
  <!-- 一部电影 -->
  <div class="movie-item">
    <div v-on:mouseover="isHover = true" v-on:mouseleave="isHover = false">
      <!-- 海报 -->
      <a :href="movieInfoUrl">
        <img class="poster" :src="this.global.base + movieItem.moviePoster">
      </a>
      <!-- 鼠标划过时的效果 -->
      <div class="movie-item-hover" v-if="isHover">
        <!-- 海报 -->
        <a :href="movieInfoUrl">
          <img class="poster-hover" :src="this.global.base + movieItem.moviePoster">
          <!-- 鼠标划过时显示的信息 -->
          <div class="movie-hover-info">
            <div class="title-hover">
              <span class="name-tags">{{movieItem.movieName}}</span>
            </div>
            <div class="title-hover">
              <span class="name-tags">类型: </span>{{movieItem.movieCategoryList}}
            </div>
            <div class="title-hover">
              <span class="name-tags">上映时间: </span>{{movieItem.releaseDate}}
            </div>
          </div>
        </a>
      </div>
    </div>
    <!-- 标题 -->
    <div class="title-style">
      {{movieItem.movieName}}
    </div>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  name: "movie-item",
  props: {
    movieItem: Object
  },
  data() {
    return {
      isHover: false,
      movieInfoUrl: ''
    }
  },
  created(){
    this.movieItem.moviePoster = JSON.parse(this.movieItem.moviePoster)[0]
    this.movieItem.moviePictures = JSON.parse(this.movieItem.moviePictures)
    this.movieItem.releaseDate = moment(this.movieItem.releaseDate).format('YYYY-MM-DD')
    this.movieItem.movieCategoryList = this.movieItem.movieCategoryList.map((obj,index) => {
      return obj.movieCategoryName;
    }).join("/")

    //赋值目标链接
    this.movieInfoUrl = '/movieInfo/' + this.movieItem.movieId
  },
  watch: {
    'movieItem'() {
      this.movieItem.moviePoster = JSON.parse(this.movieItem.moviePoster)[0]
      this.movieItem.moviePictures = JSON.parse(this.movieItem.moviePictures)
      this.movieItem.releaseDate = moment(this.movieItem.releaseDate).format('YYYY-MM-DD')
      this.movieItem.movieCategoryList = this.movieItem.movieCategoryList.map((obj,index) => {
        return obj.movieCategoryName;
      }).join("/")

      //赋值目标链接
      this.movieInfoUrl = '/movieInfo/' + this.movieItem.movieId
    }
  }
}
</script>

<style scoped>
.movie-item{
  margin-top: 30px;
  margin-left: 30px;
  display: inline-block;
  vertical-align: top;
  position: relative;
}

.poster{
  width: 223px;
  height: 322px;
}

.title-style{
  width: 223px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
  font-size: 16px;
  color: #FFFFFF;
  margin-top: 10px;
}


a{
  text-decoration: none;
  color: #333;
}

.movie-item-hover{
  position: absolute;
  width: 250px;
  height: 360px;
  z-index: 10;
  top: 0px;
  left: 0px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 0 16px #fff, 0 0 6px rgba(0,0,0,.2);
}

.poster-hover{
  width: 250px;
  height: 350px;
}

.movie-hover-info{
  padding-left: 50px;
  height: 150px;
  box-sizing: border-box;
  position: absolute;
  z-index: 11;
  top: 250px;
  background: #fff;
  width: 250px;
  color: #999;
}

.title-hover:first-child{
  font-size: 20px;
  line-height: 20px;
  margin-top: 6px;
}

.title-hover{
  font-size: 16px;
  line-height: 16px;
  margin-top: 12px;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.name-tags{
  color: #333;
}

</style>