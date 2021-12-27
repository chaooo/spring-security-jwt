<template>
  <div class="app-container">
    <el-page-header :content="isEdit?'编辑菜单':'添加菜单'" @back="goBack" />
    <el-card class="form-container" shadow="never">
      <el-form ref="menuFrom" :model="menu" :rules="rules" label-width="150px">
        <el-form-item label="上级菜单：" prop="parentId">
          <el-input v-model="parentTitle" readonly />
          <input v-model="menu.parentId" type="hidden">
        </el-form-item>
        <el-form-item label="菜单名称：" prop="title">
          <el-input v-model="menu.title" />
        </el-form-item>
        <el-form-item label="前端名称：" prop="name">
          <el-input v-model="menu.name" />
        </el-form-item>
        <el-form-item label="显示隐藏：" prop="hidden">
          <el-radio-group v-model="menu.hidden">
            <el-radio :label="0">显示</el-radio>
            <el-radio :label="1">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="前端图标：" prop="icon">
          <el-input v-model="menu.icon" style="width: 80%" placeholder="系统图标里拷贝Class" />
          <template>
            <i v-if="isElementIcon(menu.icon)" :class="menu.icon" />
            <svg-icon v-else-if="menu.icon" :icon-class="menu.icon" />
          </template>
        </el-form-item>
        <el-form-item label="状态：">
          <el-radio-group v-model="menu.status">
            <el-radio :label="0">启用</el-radio>
            <el-radio :label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序：">
          <el-input v-model="menu.sort" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit('menuFrom')">提交</el-button>
          <el-button v-if="!isEdit" @click="resetForm('menuFrom')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { getMenu, updateMenu, createMenu } from '@/api/menu'
const defaultMenu = {
  id: '',
  title: '',
  name: '',
  icon: '',
  hidden: 0,
  status: 0,
  sort: 0,
  parentId: 0
}
export default {
  name: 'MenuForm',
  data() {
    return {
      rules: {
        title: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '前端名称不能为空', trigger: 'blur' }]
      },
      parentTitle: '',
      menu: Object.assign({}, defaultMenu),
      isEdit: false
    }
  },
  watch: {
    $route(route) {
      this.getFormData()
    }
  },
  created() {
    this.getFormData()
  },
  methods: {
    goBack() {
      window.history.go(-1)
    },
    isElementIcon(value) {
      return value && value.substr(0, 7) === 'el-icon'
    },
    getFormData() {
      if (this.$route.query.id) {
        this.menu.id = this.$route.query.id
        this.isEdit = true
        getMenu(this.menu.id).then(response => {
          this.menu.title = response.data.title
          this.menu.name = response.data.name
          this.menu.icon = response.data.icon
          this.menu.hidden = response.data.hidden
          this.menu.status = response.data.status
          this.menu.sort = response.data.sort
          this.menu.parentId = response.data.parentId
        })
      } else {
        this.menu.id = ''
        this.isEdit = false
        this.menu = Object.assign({}, defaultMenu)
      }
      if (this.$route.query.parentId) {
        this.menu.parentId = this.$route.query.parentId
        this.parentTitle = this.$route.query.title
      } else {
        this.menu.parentId = 0
        this.parentTitle = '无'
      }
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.menu = Object.assign({}, defaultMenu)
      this.getFormData()
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
              updateMenu(this.$route.query.id, this.menu).then(response => {
                this.$message({
                  message: '修改成功',
                  type: 'success',
                  duration: 1000
                })
                this.$router.back()
              })
            } else {
              createMenu(this.menu).then(response => {
                this.$refs[formName].resetFields()
                this.resetForm(formName)
                this.$message({
                  message: '提交成功',
                  type: 'success',
                  duration: 1000
                })
                this.$router.back()
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
    }
  }
}
</script>

<style scoped>
</style>
