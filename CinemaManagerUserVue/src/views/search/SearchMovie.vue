<!--组件属性：通过 props 接收父组件传递的 keyword 作为搜索关键词。
数据定义：data 中定义了 movieList 数组，初始包含一个空的电影对象。
生命周期钩子：created 钩子中调用 getSearchMovieList 方法，在组件创建时自动发起搜索请求。
方法定义：getSearchMovieList 是一个异步方法，它使用 axios 发送 GET 请求到 sysMovie/find 接口，携带搜索关键词 movieName。
若请求成功（响应码为 200），将响应数据赋值给 movieList，并对数据进行处理，包括拼接海报图片的完整路径和提取电影类型名称。
路由监听：使用 watch 监听 $route 的变化，当路由发生改变时，重新调用 getSearchMovieList 方法进行搜索。-->
<template>
  <div class="search-container">
    <!-- 搜索的一部电影 -->
    <div class="search-result" v-for="item in movieList" :key="item.movieId">
      <!-- 电影海报 -->
      <div class="img-container">
        <a :href="'/movieInfo/' + item.movieId">
          <img :src="item.moviePoster">
        </a>
      </div>
      <!-- 电影信息 -->
      <div class="movie-info">
        <div class="movie-name">{{item.movieName}}</div>
        <br/>
        <div class="movie-category">{{item.movieCategoryList.join('/')}}</div>
        <br/>
        <div class="movie-releaseDate">{{item.releaseDate}}上映</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SearchMovie",
  props:{
    keyword: {
      type: String,
      default: ''
    }
  },
  data() {
    return{
      movieList: [
        {
          movieCategoryList: [],
          movieId: 0,
          movieLength: 0,
          movieName: '',
          moviePoster: '',
          releaseDate: ''
        }
      ]
    }
  },
  created() {
    this.getSearchMovieList()
  },
  methods:{
    async getSearchMovieList(){
      let queryInfo = {
        movieName: this.keyword
      }
      const { data : res } = await axios.get('sysMovie/find', {params : queryInfo})
      if(res.code !== 200) return this.$message.error('获取信息失败')
      this.movieList = res.data
      //处理数据
      for(let movie of this.movieList){
        movie.moviePoster = this.global.base + JSON.parse(movie.moviePoster)[0]
        movie.movieCategoryList = movie.movieCategoryList.map((obj, index) => {
          return obj.movieCategoryName
        })
      }
    }
  },
  watch:{
    '$route'(){
      this.getSearchMovieList()
    }
  }
}
</script>

<style scoped>
.search-container{
  width: 980px;
  margin: 0px auto;
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
}

.search-result{
  width: 460px;
  margin: 30px 0 0 30px;
  display: flex;
}

.img-container{
  width: 160px;
  height: 220px;
  margin-right: 20px;
}

.img-container img{
  width: 100%;
  height: 100%;
}

a{
  text-decoration: none;
  cursor:pointer;
}

.movie-info{
  display: flex;
  flex-direction: column;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-top: 5px;
  font-size: 16px;
  width: 280px;
  border-bottom: solid #eee 1px;
}

.movie-name{
  font-size: 26px;
  margin-top: 27px;
}

.movie-ename{
  font-size: 14px;
  color: #999;
  margin-top: 3px;
}

.movie-score{
  font-size: 26px;
  margin-bottom: 13px;
  color: #ffb400;
  height: 31px;
  line-height: 31px;
}

.movie-category{
  color: #333;

}

.movie-releaseDate{
  color: #999;
}
</style>
