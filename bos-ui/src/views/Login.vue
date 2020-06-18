<template>
  <el-form :model="loginForm" :rules="fieldRules" ref="loginForm" label-position="left" label-width="0px" class="demo-ruleForm login-container">
    <h3 class="title">系统登录</h3>
    <el-form-item prop="account">
      <el-input type="text" v-model="loginForm.name" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>

    <el-form-item prop="password">
      <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>

    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:48%;" @click.native.prevent="reset">重 置</el-button>
      <el-button type="primary" style="width:48%;" @click.native.prevent="login" :loading="logining">登 录</el-button>
    </el-form-item>

  </el-form>
</template>

<script>
  import Cookies from "js-cookie";
  export default {
    name: 'Login',
    data() {
      return {
        logining: false,
        loginForm: {
          name: 'admin',
          password: 'admin'
        },
        fieldRules: {
          name: [
            { required: true, message: '请输入账号', trigger: 'blur' },
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ]
        },
        checked: true
      };
    },
    methods: {
      login() {
        let userInfo = {name: this.loginForm.name, password: this.loginForm.password}
        console.debug(userInfo);
        this.$api.login(userInfo).then((res) => {
          console.debug(res);
          console.debug(res.token)
          Cookies.set('token', res.token) // 放置token到Cookie
          sessionStorage.setItem('user', res.loginUser) // 保存用户到本地会话
          this.$router.push('/home')  // 登录成功，跳转到主页
        }).catch(function(res) {
          alert(res);
        });
      },
      reset() {
        this.$refs.loginForm.resetFields();
      },

     /* handleLogin(){
        this.$refs.loginForm.validate(valid =>{
            if(valid) {
              this.loading = true
              this.$store.dispatch('user/login', this.loginForm)
                .then(() => {
                  this.$router.push({path: this.redirect || '/', query: this.otherQuery})
                  this.loading = false
                }).catch(() => {
                this.loading = false
              })
            }else {
              console.log('error submit!')
              return false
            }
        })
       }*/

    }
  }
</script>

<style lang="scss" scoped>
  .login-container {
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 100px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    .title {
      margin: 0px auto 30px auto;
      text-align: center;
      color: #505458;
    }
    .remember {
      margin: 0px 0px 35px 0px;
    }
  }
</style>
