<!--这段代码定义了一个名为 MovieOngoing 的 Vue 组件，其主要功能是展示正在热映的电影列表，支持按电影类别筛选、排序以及分页功能
getMovieList：构建查询参数 queryInfo，包含电影类别 ID、排序字段、每页数量、当前页码、日期范围和排序方式，
通过 axios 发送 GET 请求到 sysMovie/find 接口获取电影数据。将返回的数据赋值给 movieList，更新总记录数 total，
并根据 movieList 的长度更新 sorry 变量。
handleCurrentChange：当用户切换页码时，更新 pageNum 的值，并调用 getMovieList 方法重新获取当前页的电影列表数据。-->
<template>
  <div class="movie-container">
    <!-- 电影列表 -->
    <div v-if="!sorry" class="movie-grid">
      <movie-item
        v-for="item in movieList"
        :key="item.movieId"
        :movieItem="item"
        class="movie-card"
      />
    </div>

    <!-- 空状态提示 -->
    <div v-else class="empty-state">
      <el-icon :size="60" class="empty-icon">
        <svg viewBox="0 0 1024 1024"><path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z"/><path d="M512 304m-48 0a48 48 0 1 0 96 0 48 48 0 1 0-96 0ZM512 768c-17.7 0-32-14.3-32-32V448c0-17.7 14.3-32 32-32s32 14.3 32 32v288c0 17.7-14.3 32-32 32z"/></svg>
      </el-icon>
      <h3 class="empty-title">没有找到相关电影</h3>
      <p class="empty-tip">尝试调整筛选条件或查看其他分类</p>
    </div>

    <!-- 分页 -->
    <el-pagination
      v-if="total > pageSize"
      class="smart-pagination"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-size="pageSize"
      background
      layout="prev, pager, next, jumper"
      :total="total"
      :pager-count="5"
    >
    </el-pagination>
  </div>
</template>
<script>
import movieItem from '../../components/movie/movie-item'
import moment from 'moment'
export default {
  name: "MovieOngoing",
  components: {
    movieItem
  },
  props: {
    movieCategoryId: Number,
    orderByColumn: String
  },
  data() {
    return {
      total: 0,
      pageSize: 30,
      pageNum: 1,
      startDate: moment().subtract(365, "days").format("YYYY-MM-DD"),
      endDate: moment().format('YYYY-MM-DD'),
      movieList: [],
      sorry: false
    }
  },
  computed: {
    listenChange() {
      const {movieCategoryId, orderByColumn} = this
      return {movieCategoryId, orderByColumn}
    }
  },
  created() {
    this.getMovieList()
  },
  watch: {
    listenChange() {
      this.getMovieList()
    }
  },
  methods: {
    async getMovieList(){
      let queryInfo = {
        movieCategoryId: this.movieCategoryId,
        orderByColumn: this.orderByColumn,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        startDate: this.startDate,
        endDate: this.endDate,
        isAsc: 'desc'
      }
      const { data : res } = await axios.get('sysMovie/find', {params: queryInfo})
      this.movieList = res.data
      this.total = res.total
      this.sorry = this.movieList.length === 0;
    },
    handleCurrentChange(newPage) {
      this.pageNum = newPage
      this.getMovieList()
    }
  }
}
</script>

<style scoped>
.movie-list{
  display: flex;
  flex-wrap: wrap;
}

.pageHelper{
  display: flex;
  justify-content: center;
  margin: 40px 0px;
}

.no-cinema{
  display: flex;
  justify-content: center;
  margin-top: 40px;
  margin-bottom: 40px;
  font-size: 16px;
  color: #333;
}
</style>
