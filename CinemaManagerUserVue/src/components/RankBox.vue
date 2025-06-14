<template>
  <div class="rank-box">
    <div v-for="(item, index) in list" :key="item.movieId" class="movie-item">
      <div class="rank-number">#{{ index + 1 }}</div>

      <router-link
          :to="`/movieInfo/${item.movieId}`"
          class="movie-link"
      >
        <div class="poster-wrapper">
          <img
              :src="getPoster(item.moviePoster)"
              :alt="item.movieName"
              class="movie-poster"
          />
        </div>

        <div class="movie-meta">
          <h3 class="title">{{ item.movieName }}</h3>
          <div class="release">
            <i class="el-icon-date"></i>
            {{ formatDate(item.releaseDate) }}
          </div>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RankBox',
  props: {
    list: {
      type: Array,
      required: true
    }
  },
  methods: {
    getPoster(posters) {
      try {
        const arr = JSON.parse(posters)
        return this.global.base + (arr[0] || '')
      } catch {
        return require('@/assets/default_poster.jpg')
      }
    },
    formatDate(dateStr) {
      return dateStr ? dateStr.split(' ')[0] : '未知日期'
    }
  }
}
</script>

<style scoped>
.rank-box {
  display: grid;
  gap: 18px;
}

.movie-item {
  position: relative;
  background: #f8f9fa;
  border-radius: 6px;
  transition: transform 0.3s ease;
}

.movie-item:hover {
  transform: translateX(5px);
}

.rank-number {
  position: absolute;
  left: -15px;
  top: 15px;
  background: #409eff;
  color: white;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.movie-link {
  display: flex;
  padding: 15px;
  text-decoration: none;
  color: inherit;
}

.poster-wrapper {
  flex-shrink: 0;
  width: 80px;
  height: 110px;
  border-radius: 4px;
  overflow: hidden;
}

.movie-poster {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.movie-meta {
  flex: 1;
  margin-left: 15px;
}

.title {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #303133;
  line-height: 1.4;
}

.release {
  font-size: 13px;
  color: #909399;
  display: flex;
  align-items: center;
}

.release i {
  margin-right: 5px;
  font-size: 14px;
}
</style>