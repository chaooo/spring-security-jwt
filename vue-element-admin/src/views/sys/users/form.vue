/* eslint-disable vue/require-v-for-key */
<template>
  <div class="app-container">
    <el-page-header :content="isEdit?'编辑用户':'添加用户'" @back="goBack" />
    <el-card class="form-container" shadow="never">
      <el-form ref="sysUserForm" :model="sysUser" :rules="rules" label-width="150px">
        <el-form-item label="登录名：" prop="username">
          <el-input v-model="sysUser.username" />
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input v-model="sysUser.password" />
        </el-form-item>
        <el-form-item label="姓名：" prop="fullName">
          <el-input v-model="sysUser.fullName" />
        </el-form-item>
        <el-form-item label="手机：">
          <el-input v-model="sysUser.phone" />
        </el-form-item>
        <el-form-item label="是否阻止登录：">
          <el-radio-group v-model="sysUser.loginFlag">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户角色：">
          <el-checkbox-group v-model="roleChecked" @change="handleCheckedRoles">
            <el-checkbox v-for="item in roleSelect" :key="item.id" :label="item.id" class="littleMarginLeft">{{ item.roleDesc }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="用户头像">
          <single-upload v-model="sysUser.avatar" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="medium" @click="onSubmit('sysUserForm')">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import SingleUpload from '@/components/Upload/singleUpload'
import { getUser, updateUser, createUser } from '@/api/user'
import { fetchList as fetchRoleList } from '@/api/role'

const defaultSysUser = {
  id: 0,
  username: '',
  password: '',
  fullName: '',
  phone: '',
  avatar: '',
  roleIds: '',
  loginFlag: 0
}
export default {
  name: 'SysUsersForm',
  components: { SingleUpload },
  filters: {},
  data() {
    return {
      sysUser: Object.assign({}, defaultSysUser),
      roleSelect: [],
      roleChecked: [],
      rules: {
        username: [
          { required: true, message: '请输入登录名', trigger: 'blur' },
          { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 30, message: '长度在 6 到 30 个字符', trigger: 'blur' }
        ],
        fullName: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    $route(route) {
      this.getFormData()
      this.getRoleList()
    }
  },
  created() {
    this.getFormData()
    this.getRoleList()
  },
  methods: {
    getFormData() {
      if (this.$route.query.id) {
        this.sysUser.id = this.$route.query.id
        this.isEdit = true
        getUser(this.sysUser.id).then(response => {
          this.sysUser.username = response.data.username
          this.sysUser.password = response.data.password
          this.sysUser.fullName = response.data.fullName
          this.sysUser.phone = response.data.phone
          this.sysUser.avatar = response.data.avatar
          this.sysUser.roleIds = response.data.roleIds
          this.sysUser.loginFlag = response.data.loginFlag
          if (response.data.roleIds) {
            this.roleChecked = []
            response.data.roleIds.split(',').forEach(element => {
              this.roleChecked.push(element * 1)
              console.log(this.roleChecked)
            })
          }
        })
      } else {
        this.sysUser.id = ''
        this.isEdit = false
        this.sysUser = Object.assign({}, defaultSysUser)
      }
    },
    getRoleList() {
      fetchRoleList({ pageNum: 1, pageSize: 100 }).then(response => {
        this.roleSelect = response.data.results
      })
    },
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm('是否提交数据', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            if (this.isEdit) {
              updateUser(this.$route.query.id, this.sysUser).then(response => {
                this.$message({
                  message: '修改成功',
                  type: 'success',
                  duration: 1000
                })
                this.$router.back()
              })
            } else {
              createUser(this.sysUser).then(response => {
                this.$refs[formName].resetFields()
                this.resetForm(formName)
                this.$message({
                  message: '提交成功',
                  type: 'success',
                  duration: 1000
                })
              })
            }
          })
        } else {
          this.$message({
            message: '验证失败',
            type: 'error',
            duration: 1000
          })
          return false
        }
      })
    },
    handleCheckedRoles() {
      // 选择角色
      this.sysUser.roleIds = this.roleChecked.join(',')
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.sysUser = Object.assign({}, defaultSysUser)
      this.getSelectsysUserList()
      this.filterProductAttrList = [
        {
          value: []
        }
      ]
    },
    goBack() {
      window.history.go(-1)
    }
  }
}
</script>

<style scoped>
.form-container {
  width: 800px;
}
</style>
