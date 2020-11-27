<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item" />

        <error-log class="errLog-container right-menu-item hover-effect" />

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="字体大小调节" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div>
          <span>{{$store.getters.name}}</span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">注销</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="beforeUpdatePassword()">
            <span style="display:block;">修改密码</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div>
      <el-dialog title="重置密码" :visible.sync="resetPassword" width="400px">
        <el-form :model="user"  ref="user" :rules="rules">
          <el-form-item label="原始密码" label-width="100px" prop="oldPassword">
            <el-input
              v-model="user.oldPassword"
              style="width: 200px"
              placeholder="请填写原始密码"
              @change="checkPassword()"
            ></el-input>
          </el-form-item>
          <el-form-item label="新密码" label-width="100px" prop="newPassword">
            <el-input
              v-model="user.newPassword"
              style="width: 200px"
              type="password"
              :key="passwordType"
              :type="passwordType"
              ref="password"
              :disabled="isOff"
              placeholder="请填写新密码"
            ></el-input>
            <span class="show-pwd" @click="showPwd">
              <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
          </el-form-item>
          <el-form-item>
            <el-button
              style="float: right; margin-left: 10px"
              type="danger"
              @click="resetPassword = false;user={};isOff=true"
            >取消
            </el-button
            >
            <el-button style="float: right" type="primary" @click="updatePassword('user')"
            >确定
            </el-button
            >
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import { checkPassword,updatePassword} from '@/api/user'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    SizeSelect,
    Search
  },
  data() {
    return{
      user:{
        oldPassword:'',
        newPassword:'',
        staffId:''
      },
      resetPassword:false,
      passwordType: 'password',
      isOff:true,
      rules:{
        oldPassword:[{required: true, message: '请输入登录密码',trigger: 'blur'},
          { min: 4, max: 16,  message: '长度在 4 到 16 个字符', trigger: 'blur' }],
        newPassword:[{required: true, message: '请输入登录密码',trigger: 'blur'},
          { min: 4, max: 16,pattern: /^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{4,16}$/, message: '密码必须由字母、数字组成，区分大小写 长度在 4 到 16 个字符', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      await this.$store.dispatch('tagsView/delAllViews')
      // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
      this.$router.push('/login');
    },
    beforeUpdatePassword(){
      this.resetPassword = true
    },
    checkPassword(){
     this.user.staffId = this.$store.getters.id
      checkPassword(this.user).then(res=>{
          if (res.data === 0) {
            this.$notify({
              title: '提示',
              message: res.message,
              type: 'warning',
              duration: 2000
            })
            this.isOff = true;
            this.user = {};
          } else {
           this.isOff = false;
          }
      })
    },
    updatePassword(user){
      this.$refs[user].validate((valid) => {
        if (valid) {
          this.user.staffId = this.$store.getters.id
          const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          updatePassword(this.user).then(res=>{
            setTimeout(() => {
              loading.close();
            }, 1);
            if (res.data > 0) {
              this.$notify({
                title: '提示',
                message: res.message,
                type: 'success',
                duration: 2000
              })
              this.user = {};
              this.isOff = true;
              this.resetPassword = false;
            } else {
              this.$notify({
                title: '提示',
                message: res.message,
                type: 'warning',
                duration: 2000
              })
            }
          })
        }else {
         return
        }
      })
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
