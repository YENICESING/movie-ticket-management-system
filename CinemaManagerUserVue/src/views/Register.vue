<template>
  <div class="register-container">
    <div class="register-header">
            <div class="register-top">
              <b> 注册影院会员</b>
            </div>
    </div>
    <div class="register-body">

      <el-form class="register-form" ref="registerFormRef" :model="registerForm" :rules="registerFormRules" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="registerForm.userName" placeholder="请输入用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="用户密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请输入确认密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="phoneNumber">
          <el-input v-model="registerForm.phoneNumber" type="text" aria-placeholder="请输入手机号" clearable></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio v-model="gender" label="1">男</el-radio>
          <el-radio v-model="gender" label="0">女</el-radio>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" style="font-size: 18px;">
             同意以下协议并注册</el-button>
        </el-form-item>
        <el-form-item id="protocal">
          <el-link type="primary" style="line-height: 14px;"  @click="showProtocol = true">《影院注册协议》</el-link>
        </el-form-item>
      </el-form>
      <el-dialog :visible.sync="showProtocol" title="影院注册协议">
        <!-- 默认插槽直接放置内容 -->
        <div>
          <p>这是影院注册协议的详细内容，包括注册的相关条款、权利义务等。</p>
          <p>条款 1：影院需提供真实有效的信息进行注册。</p>
          <p>条款 2：注册成功后，影院需遵守平台的运营规则。</p>
        </div>
        <div slot="footer">
          <el-button @click="showProtocol = false">关闭</el-button>
        </div>
      </el-dialog>
    </div>
    <div class="footer-mini"></div>
    <div class="footer">
      <!-- Copyright© -->
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
        // password 是表单上绑定的字段
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    let checkMobile = (rule, value, cb) => {
      const regMobile = /^(0|86|17951)?(13[0-9]|15[0123456789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
      if (regMobile.test(value)){
        return cb()
      }
      cb(new Error('请输入合法的手机号码'))
    }
    return {
      gender: '1',
      showProtocol: false,
      registerForm: {
        userName: '',
        password: '',
        confirmPassword: '',
        phoneNumber: '',
        sex: ''
      },
      registerFormRules: {
        userName: [
          { required: true, message: "请输入用户名称", trigger: "blur"},
          { min: 2, max: 20, message: "用户名称长度在2到20个字符之间", trigger: "blur"}
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur"},
          { min:6, max: 16, message: "长度在6到16个字符之间", trigger: "blur"}
        ],
        confirmPassword: [
          { required: true, validator: validatePass, message: "两次密码输入不一致", trigger: "blur"},
        ],
        phoneNumber: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { validator: checkMobile, trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    register() {
      this.$refs.registerFormRef.validate(async valid => {
        if(!valid) return
        this.registerForm.sex = this.gender === '1'
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        const { data: res} = await axios.post('sysUser/register', JSON.stringify(this.registerForm));
        if(res.code !== 200) return this.$message.error(res.msg);

        this.$message.success("注册完成！");
        this.$router.push('/login')
        this.$refs.registerFormRef.resetFields()
      })
    },
    toWelcome() {
      this.$router.push('/welcome')
    }
  }
}
</script>

<style scoped>
.register-container{
  width: 100%;
  height: 100%;
}

.header{
  height: 60px;
  border-bottom: deepskyblue solid 2px;
  padding-top: 6px;
  padding-left: 500px;
  padding-bottom: 0;
  cursor: pointer;
}

.register-body{
  /* text-align: center; */
  padding: 0 400px 0 400px;
}

.register-form{
  /* width: 40%;
  margin-left: 25%;
  margin-right: 5%; */
}

.footer-mini{
  border-top: 1px solid #EEE;
  padding-top: 20px;
  text-align: center;
}

.register-top {
  font-size: 28px;
}

.register-header {
  width: 100%; text-align: center;margin: 20px 0 20px 0
}

.footer{
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>
