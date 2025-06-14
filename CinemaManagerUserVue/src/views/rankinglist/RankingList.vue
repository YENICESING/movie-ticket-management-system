<template>
  <div class="ranking-container">

    <!-- 三列布局 -->
    <div class="columns-wrapper">
      <!-- 总票房 -->
      <div class="rank-column">
        <h2 class="column-header">
          <i class="icon-trophy"></i>
          总票房榜
        </h2>
        <RankBox :list="totalList" />
      </div>

      <!-- 国内票房 -->
      <div class="rank-column">
        <h2 class="column-header">
          <i class="icon-china"></i>
          国内票房榜
        </h2>
        <RankBox :list="domesticList" />
      </div>

      <!-- 国外票房 -->
      <div class="rank-column">
        <h2 class="column-header">
          <i class="icon-globe"></i>
          国外票房榜
        </h2>
        <RankBox :list="foreignList" />
      </div>
    </div>
  </div>
</template>

<script>
import RankBox from '@/components/RankBox'

export default {
  name: 'RankingList',
  components: { RankBox },
  data() {
    return {
      totalList: [],
      domesticList: [],
      foreignList: []
    }
  },
  async created() {
    await this.loadAllData()
  },
  methods: {
    async loadAllData() {
      try {
        const requests = [
          this.$axios.get('sysMovie/find/rankingList/1'),
          this.$axios.get('sysMovie/find/rankingList/2'),
          this.$axios.get('sysMovie/find/rankingList/3')
        ]

        const [total, domestic, foreign] = await Promise.all(requests)

        this.totalList = total.data.data || []
        this.domesticList = domestic.data.data || []
        this.foreignList = foreign.data.data || []
      } catch (error) {
        this.$message.error('数据加载失败')
        console.error('API Error:', error)
      }
    }
  }
}
</script>

<style scoped>
.ranking-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.columns-wrapper {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 25px;
  margin-top: 30px;
}

.rank-column {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  padding: 20px;
}

.column-header {
  font-size: 20px;
  color: #303133;
  margin: 0 0 15px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #eee;
}

.column-header i {
  margin-right: 8px;
  font-size: 24px;
}

.icon-trophy { color: #ffd700; }
.icon-china { color: #d42323; }
.icon-globe { color: #409eff; }

@media (max-width: 992px) {
  .columns-wrapper {
    grid-template-columns: 1fr;
  }

  .rank-column {
    margin-bottom: 20px;
  }
}
</style>
