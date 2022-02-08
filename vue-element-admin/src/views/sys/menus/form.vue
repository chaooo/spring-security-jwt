<template>
  <div class="app-container">
    <el-page-header :content="isEdit?'编辑菜单':'添加菜单'" @back="goBack" />
    <el-card class="form-container" shadow="never">
      <el-form ref="menuFrom" :model="menu" :rules="rules" label-width="150px">
        <el-form-item label="上级菜单:" prop="parentId">
          <el-input v-model="parentTitle" readonly />
          <input v-model="menu.parentId" type="hidden">
        </el-form-item>
        <el-form-item label="菜单名称:" prop="title">
          <el-input v-model="menu.title" />
        </el-form-item>
        <el-form-item label="前端名称:" prop="name">
          <el-input v-model="menu.name" />
        </el-form-item>
        <el-form-item label="显示隐藏:" prop="hidden">
          <el-radio-group v-model="menu.hidden">
            <el-radio :label="0">显示</el-radio>
            <el-radio :label="1">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="前端图标:" prop="icon">
          <el-input v-model="menu.icon" style="width: 80%" placeholder="系统图标里拷贝Class" />
          <template>
            <i v-if="isElementIcon(menu.icon)" :class="menu.icon" />
            <svg-icon v-else-if="menu.icon" :icon-class="menu.icon" />
          </template>
        </el-form-item>
        <el-form-item label="状态:">
          <el-radio-group v-model="menu.status">
            <el-radio :label="0">启用</el-radio>
            <el-radio :label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序:">
          <el-input v-model="menu.sort" />
        </el-form-item>
        <el-form-item v-if="null === menu.childCount || menu.childCount === 0" label="按钮权限:" prop="permissionJson">
          <el-table row-key="title" :data="permissions" border highlight-current-row>
            <el-table-column label="按钮标题" prop="title" align="center">
              <template slot-scope="{row}">
                <el-input v-model="row.title" />
              </template>
            </el-table-column>
            <el-table-column label="权限标识" prop="name" align="center">
              <template slot-scope="{row}">
                <el-input v-model="row.name" />
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="{row}">
                <el-button size="mini" type="danger" @click="handlePermissionDelete(row)">删除按钮</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="permission-btn">
            <el-button type="primary" @click="handlePermissionCreate">添加按钮</el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit('menuFrom')">提交</el-button>
          <el-button v-if="!isEdit" type="info" @click="resetForm('menuFrom')">重置</el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { getMenu, updateMenu, createMenu } from '@/api/sys/menu'
const defaultMenu = {
  id: '',
  title: '',
  name: '',
  icon: '',
  hidden: 0,
  status: 0,
  sort: 0,
  childCount: 0,
  permissionJson: '',
  parentId: 0
}
const defaultPermission = {
  id: 0,
  name: '',
  title: ''
}
export default {
  name: 'SysMenuEdit',
  data() {
    return {
      rules: {
        title: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '前端名称不能为空', trigger: 'blur' }]
      },
      parentTitle: '',
      menu: Object.assign({}, defaultMenu),
      permissions: [],
      disableNextLevel: false,
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
      // 调用全局挂载的方法,关闭当前标签页
      this.$store.dispatch('tagsView/delView', this.$route)
      // 返回上一步路由，返回上一个标签页
      this.$router.go(-1)
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
          this.menu.childCount = response.data.childCount
          if (response.data.permissions) {
            this.permissions = response.data.permissions
          }
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
      this.menu.permissionJson = JSON.stringify(this.permissions)
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$confirm('是否提交数据', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            const self = this
            if (this.isEdit) {
              updateMenu(this.$route.query.id, this.menu).then(response => {
                this.$message({
                  message: '修改成功',
                  type: 'success',
                  duration: 1000
                })
                self.goBack()
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
                self.goBack()
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
    handlePermissionDelete(row) {
      const temp = this.permissions
      const newList = []
      temp.forEach(item => {
        if (item.id !== row.id) {
          newList.push(item)
        }
      })
      this.permissions = newList
    },
    handlePermissionCreate() {
      this.permissions.push(Object.assign({}, defaultPermission))
    }
  }
}
</script>

<style scoped>
.permission-btn{
  text-align: center;
  padding:15px 0;
  border-bottom: 1px solid #e6ebf5;
}
</style>
