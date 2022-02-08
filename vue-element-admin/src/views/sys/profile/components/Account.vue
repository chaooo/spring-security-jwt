<template>
  <el-form ref="sysUserForm" :model="user" :rules="rules">
    <el-form-item label="用户头像">
      <single-upload v-model="user.avatar" />
    </el-form-item>
    <el-form-item label="登录名：" prop="username">
      <el-input v-model.trim="user.username" />
    </el-form-item>
    <el-form-item label="姓名：" prop="fullName">
      <el-input v-model.trim="user.fullName" />
    </el-form-item>
    <el-form-item label="手机：">
      <el-input v-model.trim="user.phone" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit('sysUserForm')">保存修改</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateProfile } from '@/api/sys/user'
import SingleUpload from '@/components/Upload/singleUpload'
export default {
  components: { SingleUpload },
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          userId: 0,
          username: '',
          fullName: '',
          phone: '',
          avatar: ''
        }
      }
    },
    rules: {
      type: Object,
      default: () => {
        return {
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
    }
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm('是否提交数据', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            updateProfile(this.user).then(response => {
              this.$message({
                message: '修改成功',
                type: 'success',
                duration: 1000
              })
              this.$router.back()
            })
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
    }
  }
}
</script>
