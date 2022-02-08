<template>
  <div class="app-container">
    <el-page-header v-if="parentTitle" :content="parentTitle + ' 下的子菜单'" @back="goBack" />
    <div class="filter-container">
      <el-select v-model="listQuery.status" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in statusOptions" :key="item.key" :label="item.label" :value="item.key" />
      </el-select>
      <el-button v-if="checkPermission('sys:menu:edit')" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        添加菜单
      </el-button>
    </div>
    <el-table :key="tableKey" v-loading="listLoading" :data="list" border fit highlight-current-row style="width:100%">
      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="菜单ID" align="center">
        <template slot-scope="{row}">{{ row.id }}</template>
      </el-table-column>
      <el-table-column label="菜单名称" align="center">
        <template slot-scope="{row}">{{ row.title }}</template>
      </el-table-column>
      <el-table-column label="路由名称" align="center">
        <template slot-scope="{row}">{{ row.name }}</template>
      </el-table-column>
      <el-table-column label="前端图标" width="80" align="center">
        <template slot-scope="{row}">
          <i v-if="isElementIcon(row.icon)" :class="row.icon" />
          <svg-icon v-else-if="row.icon" :icon-class="row.icon" />
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template slot-scope="{row}">
          <el-switch v-model="row.status" :active-value="0" :inactive-value="1" active-color="#1890ff" inactive-color="#DCDFE6" @change="handleHiddenChange($index, row)" />
        </template>
      </el-table-column>
      <el-table-column label="排序" width="80" align="center">
        <template slot-scope="{row}">
          <el-input v-model="row.sort" maxlength="4" minlength="1" @blur="handleOnInputBlur(row)" />
        </template>
      </el-table-column>
      <el-table-column label="设置" width="250" align="center">
        <template slot-scope="{row}">
          <el-button plain size="mini" :disabled="row.childCount | disableNextLevel" @click="handleShowNextLevel(row)">查看子菜单</el-button>
          <el-button v-if="checkPermission('sys:menu:edit')" plain size="mini" @click="handleCreateNextLevel(row)">添加子菜单</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="{row}">
          <el-button v-if="checkPermission('sys:menu:edit')" size="mini" type="primary" @click="handleUpdate(row)">编辑
          </el-button>
          <el-button v-permission="'sys:menu:del'" size="mini" type="danger" @click="handleDelete(row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

  </div>
</template>

<script>
import { fetchList, deleteMenu, updateSelective } from '@/api/sys/menu'
import Pagination from '@/components/Pagination'
import checkPermission from '@/utils/permission' // 权限判断函数
// 当然你也可以为了方便使用，将它注册到全局
import permission from '@/directive/permission/index.js' // 权限判断指令
export default {
  name: 'SysMenuList',
  directives: { permission },
  components: { Pagination },
  filters: {
    disableNextLevel(value) {
      if (value === 0) {
        return true
      } else {
        return false
      }
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 5,
      listLoading: true,
      parentTitle: '',
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        parentId: '',
        status: ''
      },
      statusOptions: [
        { label: '状态', key: '' },
        { label: '启用', key: '0' },
        { label: '停用', key: '1' }
      ]
    }
  },
  watch: {
    $route(route) {
      this.resetParentId()
      this.getList()
    }
  },
  created() {
    this.resetParentId()
    this.getList()
  },
  methods: {
    checkPermission,
    isElementIcon(value) {
      return value && value.substr(0, 7) === 'el-icon'
    },
    resetParentId() {
      this.listQuery.pageNum = 1
      if (this.$route.query.parentId) {
        this.listQuery.parentId = this.$route.query.parentId
        this.parentTitle = this.$route.query.title
      } else {
        this.listQuery.parentId = ''
        this.parentTitle = ''
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.listLoading = false
        this.list = response.data.results
        this.total = response.data.totalRecord
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    goBack() {
      // 调用全局挂载的方法,关闭当前标签页
      this.$store.dispatch('tagsView/delView', this.$route)
      // 返回上一步路由，返回上一个标签页
      this.$router.go(-1)
    },
    handleCreate() {
      this.$router.push({ path: '/sys/menus/add', query: { parentId: this.listQuery.parentId, title: this.parentTitle }})
    },
    handleUpdate(row) {
      this.$router.push({ path: '/sys/menus/edit', query: { id: row.id, parentId: this.listQuery.parentId, title: this.parentTitle }})
    },
    handleShowNextLevel(row) {
      this.$router.push({ path: '/sys/menus/list', query: { parentId: row.id, title: row.title }})
    },
    handleCreateNextLevel(row) {
      this.$router.push({ path: '/sys/menus/add', query: { parentId: row.id, title: row.title }})
    },
    handleDelete(row) {
      this.$confirm('是否要删除该菜单', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenu(row.id).then(response => {
          this.$message({
            message: '删除成功',
            type: 'success',
            duration: 1000
          })
          this.getList()
        })
      })
    },
    handleOnInputBlur(row) {
      // 更新排序
      updateSelective(row.id, { sort: row.sort }).then(response => {
        this.$message({
          message: '操作成功',
          type: 'success',
          duration: 1000
        })
        this.getList()
      })
    },
    handleHiddenChange(index, row) {
      // 操作开关
      updateSelective(row.id, { status: row.status }).then(response => {
        this.$message({
          message: '操作成功',
          type: 'success',
          duration: 1000
        })
        this.getList()
      })
    },
    handleFilterStatus(status) {
      console.log('status', status)
      const defObj = { msg: '', status: 0 }
      if (status === 0) {
        defObj.msg = '是否要停用该菜单?'
        defObj.status = 1
      } else {
        defObj.msg = '是否要启用该菜单?'
        defObj.status = 0
      }
      return defObj
    }
  }
}
</script>

<style scoped>
</style>
