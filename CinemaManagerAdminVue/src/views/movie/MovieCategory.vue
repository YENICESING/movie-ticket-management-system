<!--getMovieCategoryList：发送 GET 请求获取电影类别列表数据，并更新页面显示。
handleSizeChange 和 handleCurrentChange：处理分页组件的每页显示记录数和页码变化事件，重新获取数据。
addMovieCategory：验证添加表单数据，若验证通过则发送 POST 请求添加新的电影类别，添加成功后隐藏对话框并刷新列表。
showEditDialog：根据传入的类别 ID 发送 GET 请求获取类别详细信息，填充到修改表单中并显示修改对话框。
editMovieCategoryInfo：验证修改表单数据，若验证通过则发送 PUT 请求更新电影类别信息，更新成功后隐藏对话框并刷新列表。
handleSelectionChange：监听表格中多选框的变化，更新 multipleSelection 数组。
multipleDelete：询问用户是否确认批量删除，若确认则发送 DELETE 请求删除选中的类别，删除成功后刷新列表。
deleteMovieCategoryById：询问用户是否确认删除单个类别，若确认则发送 DELETE 请求删除该类别，删除成功后刷新列表。-->
<template>
<div>
 <!--面包屑导航类别-->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>电影管理</el-breadcrumb-item>
        <el-breadcrumb-item>电影类别</el-breadcrumb-item>
      </el-breadcrumb>
    </div>


    <!--卡片视图-->
    <el-card class="box-card">
      <el-row :gutter="20">
        <el-col :span="20">
          <el-button type="primary" @click="addDialogVisible = true" style="font-size: 18px;">
             添加类别
          </el-button>
          <el-button type="danger" @click="multipleDelete" style="font-size: 18px;">
             批量删除类别
          </el-button>
        </el-col>
      </el-row>

      <!--类别分类列表-->
      <el-table :data="movieCategoryList" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="movieCategoryId" label="类别编号" width="145"></el-table-column>
        <el-table-column prop="movieCategoryName" label="电影类别"></el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template slot-scope="scope">
            <el-tooltip effect="dark" content="修改电影类别" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" @click="showEditDialog(scope.row.movieCategoryId)"  style="font-size: 18px;">
                 编辑
              </el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="删除类别" placement="top" :enterable="false" :open-delay="500">
              <el-button type="danger" @click="deleteMovieCategoryById(scope.row.movieCategoryId)"  style="font-size: 18px;">
                 删除
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!--分页类别-->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryInfo.pageNum"
          :page-sizes="[5, 7, 9]"
          :page-size="queryInfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-card>

    <!--添加类别对话框-->
    <el-dialog title="添加类别" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
      <!--内容主题区-->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <!--prop：在addFormRules中定义校验规则， v-model：双向绑定数据-->
        <el-form-item label="电影类别" prop="movieCategoryName">
          <el-input v-model="addForm.movieCategoryName"></el-input>
        </el-form-item>
      </el-form>
      <!--底部区域-->
      <span slot="footer" class="dialog-footer">
      <el-button @click="addDialogVisible = false" style="font-size: 18px;">  取 消</el-button>
      <el-button type="primary" @click="addMovieCategory" style="font-size: 18px;">  确 定</el-button>
      </span>
    </el-dialog>

    <!--修改类别对话框-->
    <el-dialog title="修改类别" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <el-form-item label="类别编号">
          <el-input v-model="editForm.movieCategoryId" disabled></el-input>
        </el-form-item>
        <el-form-item label="电影类别" prop="movieCategoryName">
          <el-input v-model="editForm.movieCategoryName"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false" style="font-size: 18px;">  取 消</el-button>
        <el-button type="primary" @click="editMovieCategoryInfo" style="font-size: 18px;">  确 定</el-button>
      </span>
    </el-dialog>

  </div>

</template>

<script>
export default {
  name: "MovieCategory",
  // this.$message和this.$confirm都属于原型挂载, 在element.js中配置
  // Vue.prototype.$message = Message
  // Vue.prototype.$confirm = MessageBox.confirm
  data() {
    return {
      queryInfo: {
        query: '',
        pageNum: 1,
        pageSize: 7
      },
      movieCategoryList: [],
      total: 0,
      //控制对话框的显示与隐藏
      addDialogVisible: false,
      //添加类别的表单数据
      addForm: {
        movieCategoryName: ''
      },
      //验证表单规则对象
      addFormRules: {
        movieCategoryName: [
          { required: true, message: '请输入电影类别', trigger: 'blur' }
        ]
      },
      editDialogVisible: false,
      editForm: {},
      editFormRules: {
        movieCategoryName: [
          { required: true, message: '请输入电影类别', trigger: 'blur' }
        ]
      },
      multipleSelection: []
    }
  },
  created() {
    this.getMovieCategoryList()
  },
  methods: {
    getMovieCategoryList() {
      const _this = this;
      axios.get('sysMovieCategory/find', {params: _this.queryInfo}).then(resp => {
        console.log(resp)
        _this.movieCategoryList = resp.data.data;
        _this.total = resp.data.total;
        _this.queryInfo.pageSize = resp.data.pageSize;
        _this.queryInfo.pageNum = resp.data.pageNum;
      })
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getMovieCategoryList()
      console.log(newSize)
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getMovieCategoryList()
      console.log(newPage)
    },
    // 监听添加对话框的关闭事件
    addDialogClosed(){
      this.$refs.addFormRef.resetFields()
    },
    // 监听添加按钮
    addMovieCategory(){
      const _this = this;
      this.$refs.addFormRef.validate(async valid => {
        console.log(valid)
        if (!valid) return
        //预校验成功，发网络请求
        axios.defaults.headers.post['Content-Type'] = 'application/json'
        await axios.post('sysMovieCategory', JSON.stringify(_this.addForm)).then(resp => {
          console.log(resp)
          if (resp.data.code !== 200){
            this.$message.error('添加电影类别失败！')
          }
        })
        //隐藏添加对话框
        this.addDialogVisible = false
        //重新加载列表
        this.getMovieCategoryList()
        this.$message.success('添加电影类别成功！')
      })
    },
    // 显示修改对话框，回显数据
    showEditDialog(id){
      const _this = this
      axios.get('sysMovieCategory/' + id ).then(resp => {
        console.log(resp)
        _this.editForm = resp.data.data
      })
      this.editDialogVisible = true
    },
    // 监听修改对话框的关闭事件
    editDialogClosed(){
      this.$refs.editFormRef.resetFields()
    },
    // 修改类别分类信息并提交
    editMovieCategoryInfo(){
      this.$refs.editFormRef.validate(async valid => {
        const _this = this
        if (!valid) return
        axios.defaults.headers.put['Content-Type'] = 'application/json'
        await axios.put('sysMovieCategory', JSON.stringify(_this.editForm)).then(resp => {
          if (resp.data.code !== 200){
            this.$message.error('修改电影类别失败！')
          }
        })
        this.editDialogVisible = false
        this.getMovieCategoryList()
        this.$message.success('修改电影类别成功！')
      })
    },
    // 监听多选框变化
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    async multipleDelete(){
      const _this = this
      //询问用户是否确认删除
      const resp = await this.$confirm('此操作将永久删除这些条目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err)

      // 用户确认删除, resp为字符串"confirm"
      // 用户取消删除，resp为字符串"cancel"
      if (resp === 'cancel'){
        return _this.$message.info('已取消删除')
      }

      let ids = []
      this.multipleSelection.forEach(data => {
        ids.push(data.movieCategoryId)
      })
      await axios.delete('sysMovieCategory/' + ids).then(resp => {
        if (resp.data.code !== 200){
          this.$message.success('批量删除电影类别失败！')
        }
      })
      this.getMovieCategoryList()
      this.$message.success('批量删除电影类别成功！')
    },
    //根据id删除对应的类别分类
    async deleteMovieCategoryById(id){
      const _this = this
      //询问用户是否确认删除
      const resp = await this.$confirm('此操作将永久删除该条目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err)

      // 用户确认删除, resp为字符串"confirm"
      // 用户取消删除，resp为字符串"cancel"
      console.log(resp)
      if (resp === 'cancel'){
        return _this.$message.info('已取消删除')
      }

      await axios.delete('sysMovieCategory/' + id).then(resp => {
        if (resp.data.code !== 200){
          _this.$message.success('删除电影类别失败！')
        }
      })
      this.getMovieCategoryList()
      this.$message.success('删除电影类别成功！')
    }
  }
}
</script>

<style scoped>
/* 全局卡片样式增强 */
.box-card {
  margin: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: box-shadow 0.3s ease;
}

.box-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

/* 操作按钮组样式 */
.el-row {
  margin-bottom: 20px;
}

.el-button {
  font-size: 14px;
  padding: 10px 20px;
  border-radius: 6px;
  transition: all 0.2s;
}

.el-button--primary {
  background: linear-gradient(135deg, #409eff, #2979ff);
  border: none;
}

.el-button--danger {
  background: linear-gradient(135deg, #ff4d4f, #f5222d);
  border: none;
}

/* 表格样式优化 */
.el-table {
  margin: 20px 0;
  --el-table-header-bg-color: #f8f9fa;
  --el-table-row-hover-bg-color: #f8f9fa;
}

.el-table__body td {
  padding: 12px 0;
}

/* 分页组件紧凑化 */
.el-pagination {
  margin-top: 20px;
  padding: 12px 16px;
  background: #fff;
  border-radius: 8px;
}

/* 对话框表单统一间距 */
.el-dialog__body {
  padding: 20px 30px;
}

.el-form-item {
  margin-bottom: 22px;
}

/* 表单标签统一宽度 */
.el-form-item__label {
  width: 120px !important;
  justify-content: flex-end;
}

/* 操作列按钮间距 */
.el-table-column--fixed-right .cell {
  display: flex;
  gap: 8px;
}

/* 响应式处理 */
@media (max-width: 768px) {
  .el-col {
    width: 100%;
    margin-bottom: 10px;
  }

  .el-table-column--fixed-right .cell {
    flex-direction: column;
  }

  .el-button {
    width: 100%;
    margin-bottom: 10px;
  }
}

/* 工具提示样式优化 */
.el-tooltip {
  margin: 0 5px;
}

/* 对话框按钮统一风格 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

/* 输入框现代样式 */
.el-input {
  border-radius: 6px;
}

/* 删除确认对话框样式 */
.el-message-box {
  border-radius: 12px;
}

/* 批量操作按钮组 */
.batch-actions {
  margin-bottom: 20px;
  display: flex;
  gap: 15px;
}

/* 表格操作按钮紧凑布局 */
.table-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

/* 分页组件现代样式 */
.el-pagination.is-background .btn-next,
.el-pagination.is-background .btn-prev,
.el-pagination.is-background .el-pager li {
  border-radius: 6px;
  margin: 0 4px;
}

/* 对话框标题样式 */
.el-dialog__title {
  font-weight: 600;
  color: #303133;
}

/* 表单输入框聚焦效果 */
.el-input__inner:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 表格斑马纹样式 */
.el-table--striped .el-table__body tr.el-table__row--striped td {
  background: #fafafa;
}

/* 操作按钮hover效果 */
.el-button:not(.is-disabled):hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
}
</style>
