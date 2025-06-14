<template>
  <el-container>
    <el-header>
      <div class="header-inner">
        <a href="/welcome" style="text-decoration: none">
          <div class="logo-text">
            <img
              style="height: 30px; margin-bottom: -5px"
              src="../assets/homeLogo.jpg"
            />
            影院网
          </div>
        </a>
        <el-menu
          :default-active="activeUrl"
          class="nav-menu"
          mode="horizontal"
          :router="true"
          active-text-color="red"
          text-color="#000000"
        >
          <el-menu-item
            :index="item.path"
            v-for="item in menuList"
            :key="item.id"
            :style="{ color: 'white' }"
            :class="{ 'active-menu-item': activeUrl === item.path }"
          >{{ item.name }}</el-menu-item>
        </el-menu>
        <div class="searchContainer">
          <el-input
            v-model="kw"
            class="searchBar"
            placeholder="搜索电影"
          ></el-input>
          <el-button
            id="searchBtn"
            type="danger"
            circle
            @click="search"
          >
            <i class="iconfont icon-r-find" style="font-size: 18px;"></i>
          </el-button>
        </div>
        <el-dropdown @command="handleCommand">
                    <span class="el-dropdown-link">
                        <el-avatar :src="url == '' ? require('@/assets/avatar.jpg') : url" :size="50">{{
                            url === "" || url === null ? "user" : ""
                          }}</el-avatar>
                        <i
                          class="el-icon-arrow-down el-icon--right icon-arrow"
                        ></i>
                    </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="userMenu" v-if="isToken"
            >个人中心</el-dropdown-item>
            <el-dropdown-item command="logout" v-if="isToken"
            >退出</el-dropdown-item>
            <el-dropdown-item command="login" v-if="!isToken"
            >登录</el-dropdown-item>
            <el-dropdown-item command="register" v-if="!isToken"
            >注册账号</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "Welcome",
  data() {
    return {
      isToken: "",
      cinemaInfo: "",
      url: "",
      activeUrl: this.$route.path.substring(
        0,
        this.$route.path.indexOf("/", 1) === -1
          ? this.$route.path.length
          : this.$route.path.indexOf("/", 1)
      ),
      menuList: [
        {
          id: 1,
          name: "首页",
          path: "/home",
        },
        {
          id: 2,
          name: "影片",
          path: "/movie",
        },
        {
          id: 3,
          name: "榜单",
          path: "/rankingList",
        },
        {
          id: 4,
          name: "信息",
          path: "/aboutUs",
        },
      ],
      //搜索关键字
      kw: "",
    };
  },
  created() {
    this.getCinemaInfo();
    this.isToken = window.sessionStorage.getItem("token");
    var res = JSON.parse(
      window.sessionStorage.getItem("loginUser")
    );
    if (res == undefined) {
      return;
    }
    const userPicture = res.userPicture;
    const picture = JSON.parse(userPicture);
    if (picture === null || picture.length === 0) return;
    this.url = this.global.base + picture[0];
  },
  watch: {
    $route() {
      this.activeUrl = this.$route.path.substring(
        0,
        this.$route.path.indexOf("/", 1) === -1
          ? this.$route.path.length
          : this.$route.path.indexOf("/", 1)
      );
    },
  },
  methods: {
    async getCinemaInfo() {
      const _this = this;
      await axios
        .get("sysCinema")
        .then((resp) => {
          if (resp == undefined) {
            return;
          }
          _this.cinemaInfo = resp.data.data;
        })
        .catch((e) => {
          console.log(e);
          if (
            e.response == undefined ||
            e.response.data == undefined
          ) {
            this.$message({
              showClose: true,
              message: e,
              type: "error",
              duration: 5000,
            });
          } else {
            this.$message({
              showClose: true,
              message: e.response.data,
              type: "error",
              duration: 5000,
            });
          }
        });
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    search() {
      let tmp = this.kw;
      this.$router.push("/search/searchMovie?kw=" + tmp);
      this.kw = "";
    },
    handleCommand(command) {
      if (command === "logout") {
        window.sessionStorage.clear();
        return this.$router.push("/login");
      }
      this.$router.push("/" + command);
    },
    toWelcome() {
      this.$router.push("/welcome");
    },
  },
};
</script>

<style scoped>
.active-menu-item {
  background-color: #1f2935 !important;
}
.el-header {
  height: 80px !important;
  border-bottom: 1px solid #1f2935;
  background-color:#1f2935;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-inner {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-text {
  color: white;
  font-size: 26px;
}

.nav-menu {
  border-bottom: 0px solid #1f2935 !important;
  height: 60px;
  margin: 9px auto;
}
.el-menu{
  margin-right: 160px;
}
.el-menu-item {
  font-size: 20px;
  background-color: #1f2935;
}

.el-menu-item:hover {
  color: red !important;
  background-color: #1f2935 !important;

}

.searchContainer {
  padding: 20px 0;
  display: flex;
  align-items: center;
}

.searchBar >>> input {
  border-radius: 50px;
  border: 1px solid #ccc;
  padding: 0 15px;
}

#searchBtn {
  transform: translate(-100%, 0);
}

.el-dropdown-link {
  width: 70px;
  margin: 15px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.icon-arrow {
  transition: all 0.2s ease-in-out;
  -webkit-transition: all 0.2s ease-in-out;
  -moz-transition: all 0.2s ease-in-out;
  -o-transition: all 0.2s ease-in-out;
}

.el-dropdown-link:hover .icon-arrow {
  /* 定义动画的状态 */
  -webkit-transform: rotate(180deg) scale(1);
  -moz-transform: rotate(180deg) scale(1);
  -o-transform: rotate(180deg) scale(1);
  -transform: rotate(180deg) scale(1);
}

.footer-mini {
  border-top: 1px solid #eee;
  padding-top: 20px;
  text-align: center;
}

.footer {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.el-main {
  padding: 0px;
}
</style>
