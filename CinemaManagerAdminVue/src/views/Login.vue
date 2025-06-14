<template>
  <div class="login_container">
    <div class="login_box">
      <div class="title_box">
        <div class="logo">
          <i class="iconfont icon-r-setting"></i>
        </div>
        <h1>影院后台管理系统</h1>
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
            prefix-icon="el-icon-user"
            clearable
          >
          </el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
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
      //登录表单数据对象
      loginForm: {
        userName: "",
        password: "",
      },
      //表单验证规则
      loginFormRules: {
        //验证用户名
        userName: [
          {
            required: true,
            message: "请输入用户名称",
            trigger: "blur",
          },
          {
            min: 2,
            max: 20,
            message: "长度在2到20个字符之间",
            trigger: "blur",
          },
        ],
        //验证密码
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 6,
            max: 16,
            message: "长度在6到16个字符之间",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    success(params) {
      this.login();
    },
    async login() {
      try {
        this.loading = true;
        const valid = await this.$refs.loginFormRef.validate();
        if (!valid) return;

        axios.defaults.headers.post["Content-Type"] = "application/json";
        const resData = await axios.post(
          "sysUser/login",
          JSON.stringify(this.loginForm)
        );

        const { data: res } = resData;
        if (res.code !== 200) {
          this.$message.error(res.msg);
          return;
        }

        //控制登录权限
        if (
          res.data.sysUser.sysRole.children === null ||
          res.data.sysUser.sysRole.children[0] === null
        ) {
          this.$message.error(
            "抱歉，您没有权限登录，请联系管理员获取权限"
          );
          return;
        }

        this.$message.success("登录成功");
        //保存token
        window.sessionStorage.setItem("token", res.data.token);
        window.sessionStorage.setItem(
          "loginUser",
          JSON.stringify({
            sysUser: res.data.sysUser,
            cinemaId: res.data.cinemaId,
            cinemaName: res.data.cinemaName,
          })
        );
        window.sessionStorage.setItem(
          "btnPermission",
          res.data.sysUser.sysRole.roleId === 1 ? "admin" : "admin"
        );

        //导航跳转到首页
        await this.$router.push("/welcome");
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

  .el-button {
    width: 100%;
    height: 48px;
    font-size: 16px;
    border-radius: 4px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    transition: all 0.3s;

    &:hover {
      opacity: 0.9;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
    }

    &:active {
      transform: translateY(0);
    }
  }
}
</style>
