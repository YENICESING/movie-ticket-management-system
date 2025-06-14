<template>
  <div class="login_container">
    <div class="login_box">
      <div class="title_box">
        <div class="logo">
          <i class="iconfont icon-r-team"></i>
        </div>
        <h1>影院用户端登录</h1>
      </div>
      <!-- 登录表单区域 -->
      <el-form
        class="login_form"
        :model="loginForm"
        :rules="loginFormRules"
        ref="loginFormRef"
      >
        <!-- 用户名 -->
        <el-form-item prop="userName">
          <el-input
            v-model="loginForm.userName"
            placeholder="请输入用户名"
            prefix-icon="iconfont icon-r-user1"
            clearable
          ></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="iconfont icon-r-lock"
            show-password
          ></el-input>
        </el-form-item>
        <!-- 按扭区域 -->
        <el-form-item class="btns">
          <el-button
            type="primary"
            @click="login"
            :loading="loading"
          >
            登 录
          </el-button>
          <el-button
            type="info"
            @click="registerAccount"
          >
            注册新用户
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      loading: false,
      // 登录表单数据对象
      loginForm: {
        userName: "",
        password: "",
      },
      // 登录表单验证规则
      loginFormRules: {
        // 验证用户名格式
        userName: [
          {
            required: true,
            message: "请输入用户名称",
            trigger: "blur",
          },
          {
            min: 2,
            max: 20,
            message: "用户名称长度在2到20个字符之间",
            trigger: "blur",
          },
        ],
        // 验证密码格式
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 6,
            max: 16,
            message: "登录密码长度在6到16个字符之间",
            trigger: "blur",
          },
        ],
      },
      sessionId: 0,
    };
  },
  created() {
    this.sessionId = window.sessionStorage.getItem("sessionId");
    console.log("this sessionId is : " + this.sessionId);
    window.sessionStorage.setItem("sessionId", 0);
  },
  methods: {
    async login() {
      try {
        this.loading = true;
        const valid = await this.$refs.loginFormRef.validate();
        if (!valid) return;

        axios.defaults.headers.post["Content-Type"] = "application/json";
        const { data: res } = await axios.post(
          "sysUser/login",
          JSON.stringify(this.loginForm)
        );

        if (res.code !== 200) {
          this.$message.error(res.msg);
          return;
        }

        this.$message.success({ message: "登录成功", duration: 1000 });
        console.log(res.data);
        // 保存token
        window.sessionStorage.setItem("token", res.data.token);
        res.data.sysUser.sysRole = null;
        window.sessionStorage.setItem(
          "loginUser",
          JSON.stringify(res.data.sysUser)
        );

        if (
          this.sessionId !== 0 &&
          this.sessionId !== "0" &&
          this.sessionId !== null
        ) {
          this.$router.push("/chooseSeat/" + this.sessionId);
          return;
        }
        // 导航跳转到首页
        this.$router.push("/welcome");
      } catch (e) {
        console.error(e);
        let errorMsg = "Network Error";
        if (e.response && e.response.data) {
          errorMsg = typeof e.response.data === 'string'
            ? e.response.data
            : (e.response.data.msg || '请求错误');
        }
        this.$message({
          showClose: true,
          message: errorMsg,
          type: "error",
          duration: 5000,
        });
      } finally {
        this.loading = false;
      }
    },
    registerAccount() {
      this.$router.push("/register");
    },
  },
};
</script>

<style lang="scss" scoped>
.login_container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url("../assets/login-background.jpg");
  background-size: cover;
}

.login_box {
  width: 420px;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  }
}

.title_box {
  text-align: center;
  margin-bottom: 30px;

  .logo {
    margin: 0 auto 15px;
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 50%;

    i {
      font-size: 32px;
      color: white;
    }
  }

  h1 {
    font-size: 24px;
    color: #333;
    margin: 0;
    font-weight: 500;
  }
}

.login_form {
  .el-form-item {
    margin-bottom: 25px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .el-input {
    ::v-deep .el-input__inner {
      height: 48px;
      line-height: 48px;
      padding-left: 40px;
      border-radius: 4px;
      border: 1px solid #dcdfe6;
      transition: border-color 0.3s;

      &:focus {
        border-color: #667eea;
      }
    }

    ::v-deep .el-input__prefix {
      left: 10px;
      i {
        font-size: 18px;
        color: #999;
      }
    }
  }
}

.btns {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;

  .el-button {
    width: 48%;
    height: 48px;
    font-size: 16px;
    border-radius: 4px;
    transition: all 0.3s;

    &:first-child {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;

      &:hover {
        opacity: 0.9;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
      }
    }

    &:last-child {
      background: #f0f2f5;
      color: #666;
      border: none;

      &:hover {
        background: #e6e8eb;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
    }

    &:active {
      transform: translateY(0);
    }
  }
}
</style>
