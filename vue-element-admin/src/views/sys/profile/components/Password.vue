<template>
  <el-form ref="passwordForm" :model="user" :rules="rules">
    <el-form-item label="旧密码：" prop="oldPassword">
      <el-input v-model.trim="user.oldPassword" />
    </el-form-item>
    <el-form-item label="新密码：" prop="password">
      <el-input v-model.trim="user.password" />
    </el-form-item>
    <el-form-item label="确认新密码：" prop="confirmPassword">
      <el-input v-model.trim="user.confirmPassword" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit('passwordForm')">保存修改</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updatePassword } from '@/api/sys/user'
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          userId: 0,
          oldPassword: '',
          password: '',
          confirmPassword: ''
        }
      }
    },
    rules: {
      type: Object,
      default: () => {
        return {
          oldPassword: [
            { required: true, message: '请输入旧密码', trigger: 'blur' },
            { min: 6, max: 30, message: '长度在 6 到 30 个字符', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '请输入新密码', trigger: 'blur' },
            { min: 6, max: 30, message: '长度在 6 到 30 个字符', trigger: 'blur' }
          ],
          confirmPassword: [
            { required: true, message: '请确认新密码', trigger: 'blur' },
            { min: 6, max: 30, message: '长度在 6 到 30 个字符', trigger: 'blur' }
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
            updatePassword(this.user).then(response => {
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
