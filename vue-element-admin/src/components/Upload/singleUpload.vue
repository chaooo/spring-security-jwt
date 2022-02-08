<template>
  <div>
    <el-upload :action="uploadUrl" list-type="picture" :multiple="false" :show-file-list="showFileList" :file-list="fileList" :before-upload="beforeUpload" :on-remove="handleRemove" :on-success="handleUploadSuccess" :on-preview="handlePreview">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png/gif文件，且不超过1MB</div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="fileList[0].url" alt="">
    </el-dialog>
  </div>
</template>
<script>
// import { policy } from '@/api/oss'

export default {
  name: 'SingleUpload',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    value: String
  },
  data() {
    return {
      dialogVisible: false,
      uploadUrl: process.env.VUE_APP_CORE_API + '/tools/image/upload/multi'
    }
  },
  computed: {
    imageUrl() {
      return this.value
    },
    imageName() {
      if (this.value != null && this.value !== '') {
        return this.value.substr(this.value.lastIndexOf('/') + 1)
      } else {
        return null
      }
    },
    fileList() {
      return [
        {
          name: this.imageName,
          url: this.imageUrl
        }
      ]
    },
    showFileList: {
      get: function() {
        return this.value !== null && this.value !== '' && this.value !== undefined
      },
      set: function(newValue) {}
    }
  },
  methods: {
    emitInput(val) {
      this.$emit('input', val)
    },
    handleRemove(file, fileList) {
      this.emitInput('')
    },
    handlePreview(file) {
      this.dialogVisible = true
    },
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif'
      const isLt1M = file.size / 1024 / 1024 < 1
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt1M) {
        this.$message.error('上传头像图片大小不能超过 1MB!')
      }
      return isJPG && isLt1M
    },
    handleUploadSuccess(res, file) {
      this.showFileList = true
      this.fileList.pop()
      const url = res.data
      this.fileList.push({ name: file.name, url: url })
      this.emitInput(this.fileList[0].url)
    }
  }
}
</script>
<style>
</style>

