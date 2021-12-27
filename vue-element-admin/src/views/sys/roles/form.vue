<template>
  <div class="app-container">
    <el-page-header :content="isEdit?'编辑角色':'添加角色'" @back="goBack" />
    <el-card class="form-container" shadow="never">
      <el-form ref="roleFrom" :model="role" :rules="rules" label-width="150px">
        <el-form-item label="角色名称：" prop="roleName">
          <el-input v-model="role.roleName" maxlength="20" />
        </el-form-item>
        <el-form-item label="角色描述：" prop="roleDesc">
          <el-input v-model="role.roleDesc" maxlength="20" />
        </el-form-item>
        <el-form-item label="角色授权：">
          <el-tree ref="tree" :data="menuTree" node-key="id" :props="menuProps" show-checkbox default-expand-all check-strictly="true" @check-change="handleTreeChange" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit('roleFrom')">提交</el-button>
          <el-button v-if="!isEdit" @click="resetForm('roleFrom')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getRole, updateRole, createRole } from '@/api/role'
import { sysMenuTree } from '@/api/menu'

const defaultRole = {
  id: '',
  roleName: '',
  roleDesc: '',
  menuIds: ''
}
export default {
  name: 'RolesForm',
  data() {
    return {
      role: Object.assign({}, defaultRole),
      isEdit: false,
      menuTree: [],
      menuProps: {
        children: 'subMenus',
        label: 'title'
      },
      rules: {
        roleName: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }]
      }
    }
  },
  watch: {
    $route(route) {
      this.getFormData()
      this.getMenuTree()
    }
  },
  created() {
    this.getFormData()
    this.getMenuTree()
  },
  methods: {
    goBack() {
      window.history.go(-1)
    },
    getFormData() {
      if (this.$route.query.id) {
        this.role.id = this.$route.query.id
        this.isEdit = true
        getRole(this.role.id).then(response => {
          this.role.roleName = response.data.roleName
          this.role.roleDesc = response.data.roleDesc
          this.role.menuIds = response.data.menuIds
          if (response.data.menuIds) {
            const keys = []
            response.data.menuIds.split(',').forEach(element => {
              keys.push(element * 1)
            })
            console.log(keys)
            this.$refs.tree.setCheckedKeys(keys)
          }
        })
      } else {
        this.role.id = ''
        this.isEdit = false
        this.role = Object.assign({}, defaultRole)
      }
    },
    getMenuTree() {
      // 属性菜单
      sysMenuTree().then(response => {
        this.menuTree = response.data
      })
    },
    getCheckedNodes() {
      const menuIdArr = []
      const checkedNodes = this.$refs.tree.getCheckedNodes()
      for (let i = 0; i < checkedNodes.length; i++) {
        console.log(checkedNodes[i].id)
        menuIdArr.push(checkedNodes[i].id)
      }
      this.role.menuIds = menuIdArr.join(',')
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
      this.role = Object.assign({}, defaultRole)
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
            this.getCheckedNodes()
            if (this.isEdit) {
              updateRole(this.$route.query.id, this.role).then(response => {
                this.$message({
                  message: '修改成功',
                  type: 'success',
                  duration: 1000
                })
                this.$router.back()
              })
            } else {
              createRole(this.role).then(response => {
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
