<template>
  <div class="bill-container">
    <div class="page-header">
      <div class="header-title">
        <i class="iconfont icon-r-shield"></i>
        <h1>我的订单</h1>
      </div>
      <div class="header-actions">
        <el-input
          placeholder="搜索订单..."
          prefix-icon="el-icon-search"
          v-model="searchQuery"
          clearable
          @clear="handleSearch"
          @keyup.enter.native="handleSearch"
        ></el-input>
      </div>
    </div>

    <div class="order-list">
      <div class="order-card" v-for="item in filteredBills" :key="item.id">
        <div class="order-header">
          <div class="order-meta">
            <div class="order-date">
              <i class="el-icon-time"></i>
              {{ item.createTime }}
            </div>
            <div class="order-id">
              <i class="el-icon-document"></i>
              订单号: {{ item.billId }}
            </div>
          </div>
          <div class="order-status" :class="getStatusClass(item.payState)">
            {{ item.payState ? "已完成" : "未完成" }}
          </div>
        </div>

        <div class="order-content">
          <div class="movie-poster">
            <img :src="item.sysSession.sysMovie.moviePoster" alt="电影海报" />
          </div>
          <div class="movie-info">
            <h3 class="movie-title">
              《{{ item.sysSession.sysMovie.movieName }}》
            </h3>
            <div class="cinema-info">
              <i class="el-icon-location-outline"></i>
              {{ item.sysSession.sysHall.sysCinema.cinemaName }}
            </div>
            <div class="hall-info">
              <i class="el-icon-video-camera"></i>
              {{ item.sysSession.sysHall.hallName }} ({{ item.sysSession.sysHall.hallCategory }})
            </div>
            <div class="time-info">
              <i class="el-icon-alarm-clock"></i>
              {{ item.sysSession.sessionDate }} {{ item.sysSession.playTime }} - {{ item.sysSession.endTime }}
            </div>
            <div class="seat-info">
              <i class="el-icon-user"></i>
              座位: {{ formatSeats(item.seats) }}
            </div>
          </div>
          <div class="order-summary">
            <div class="price-info">
              <div class="total-price">
                ￥{{ calculateTotalPrice(item) }}
              </div>
              <div class="price-detail">
                ({{ JSON.parse(item.seats).length }} × ￥{{ item.sysSession.sessionPrice }})
              </div>
            </div>
            <div class="order-actions">
              <el-button
                type="primary"
                plain
                @click="toBillDetail(item.billId)"
                class="detail-btn"
              >
                查看详情
              </el-button>
              <el-button
                type="danger"
                plain
                @click="deleteBill(item.billId)"
                class="delete-btn"
              >
                删除订单
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" v-if="filteredBills.length === 0">
        <i class="el-icon-document-delete"></i>
        <p>暂无订单记录</p>
        <el-button type="primary" @click="$router.push('/movie')">去购票</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import moment from "moment";

export default {
  name: "BillInfo",
  data() {
    return {
      queryInfo: {
        userId: "",
      },
      billList: [],
      searchQuery: ""
    };
  },
  computed: {
    filteredBills() {
      if (!this.searchQuery) return this.billList;
      const query = this.searchQuery.toLowerCase();
      return this.billList.filter(bill =>
        bill.billId.toLowerCase().includes(query) ||
        bill.sysSession.sysMovie.movieName.toLowerCase().includes(query) ||
        bill.sysSession.sysHall.sysCinema.cinemaName.toLowerCase().includes(query)
      );
    }
  },
  created() {
    this.getUser();
    this.getBillList();
  },
  methods: {
    getUser() {
      this.queryInfo.userId = JSON.parse(
        window.sessionStorage.getItem("loginUser")
      ).userId;
    },
    async getBillList() {
      try {
        const { data } = await axios.get("sysBill", { params: this.queryInfo });
        this.billList = data.data.map(item => ({
          ...item,
          sysSession: {
            ...item.sysSession,
            sessionDate: moment(item.sysSession.sessionDate).format("YYYY年MM月DD日"),
            sysMovie: {
              ...item.sysSession.sysMovie,
              moviePoster: this.global.base + JSON.parse(item.sysSession.sysMovie.moviePoster)[0]
            }
          }
        }));
      } catch (error) {
        console.error("获取订单列表失败:", error);
        this.$message.error("获取订单列表失败");
      }
    },
    calculateTotalPrice(item) {
      return (item.sysSession.sessionPrice * JSON.parse(item.seats).length).toFixed(2);
    },
    formatSeats(seats) {
      return JSON.parse(seats).join(", ");
    },
    getStatusClass(isPaid) {
      return isPaid ? "completed" : "pending";
    },
    toBillDetail(id) {
      this.$router.push("/billDetail/" + id);
    },
    async deleteBill(id) {
      try {
        await this.$confirm("确定要删除此订单吗?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        });

        await axios.delete("sysBill/" + id);
        this.$message.success("订单删除成功");
        this.getBillList();
      } catch (error) {
        if (error !== "cancel") {
          this.$message.error("删除订单失败");
        }
      }
    },
    handleSearch() {
      // 搜索功能已通过计算属性实现
    }
  }
};
</script>

<style scoped>
.bill-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.header-title {
  display: flex;
  align-items: center;
}

.header-title i {
  font-size: 28px;
  color: #409EFF;
  margin-right: 15px;
}

.header-title h1 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.header-actions {
  width: 300px;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.order-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #f8f9fa;
  border-bottom: 1px solid #eee;
}

.order-meta {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;
}

.order-meta i {
  margin-right: 5px;
  color: #999;
}

.order-status {
  padding: 5px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
}

.order-status.completed {
  background-color: #e6f7e6;
  color: #67C23A;
}

.order-status.pending {
  background-color: #fef0f0;
  color: #F56C6C;
}

.order-content {
  display: flex;
  padding: 20px;
}

.movie-poster {
  width: 100px;
  height: 140px;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 20px;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.movie-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.movie-info {
  flex: 1;
  padding-right: 20px;
}

.movie-title {
  font-size: 18px;
  color: #333;
  margin: 0 0 10px 0;
}

.cinema-info,
.hall-info,
.time-info,
.seat-info {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}

.movie-info i {
  margin-right: 8px;
  color: #999;
}

.order-summary {
  width: 200px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
}

.price-info {
  text-align: right;
}

.total-price {
  font-size: 20px;
  font-weight: bold;
  color: #F56C6C;
}

.price-detail {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.order-actions {
  display: flex;
  gap: 10px;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.empty-state i {
  font-size: 60px;
  color: #ddd;
  margin-bottom: 20px;
}

.empty-state p {
  font-size: 16px;
  color: #999;
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .header-actions {
    width: 100%;
  }

  .order-content {
    flex-direction: column;
  }

  .movie-info {
    padding-right: 0;
    margin: 15px 0;
  }

  .order-summary {
    width: 100%;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }

  .price-info {
    text-align: left;
  }

  .order-actions {
    gap: 5px;
  }
}
</style>
