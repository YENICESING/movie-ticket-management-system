<!--数据获取方法
getResourceList：根据查询条件向服务器发送请求，获取资源列表数据，并更新页面显示。
getAllResource：向服务器请求获取所有资源列表，用于添加和修改资源时选择父资源。
分页处理方法
handleSizeChange：当用户更改每页显示的记录数量时，更新查询条件中的 pageSize，并重新调用 getResourceList 方法获取数据。
handleCurrentChange：当用户切换页码时，更新查询条件中的 pageNum，并重新调用 getResourceList 方法获取数据。
对话框操作方法
addDialogClosed：在添加资源对话框关闭时，重置表单字段，清除之前输入的信息。
addResource：处理添加资源的逻辑，包括表单验证、设置父资源 ID 为默认值（若未选择）、发送添加请求、更新资源列表和显示提示信息。
showEditDialog：根据资源 ID 获取资源详细信息，并显示修改资源对话框，回显资源信息，同时处理父资源 ID 为 0 的情况。
editDialogClosed：在修改资源对话框关闭时，重置表单字段。
editResource：处理修改资源信息的逻辑，包括表单验证、处理父资源 ID 为空的情况、发送更新请求、更新资源列表和显示提示信息。
删除操作方法
multipleDelete：处理批量删除资源的逻辑，先弹出确认删除的提示框，若用户确认则发送删除请求，更新资源列表并显示提示信息。
deleteResourceById：处理单个资源删除的逻辑，先弹出确认删除的提示框，若用户确认则发送删除请求，更新资源列表并显示提示信息。
辅助方法
handleSelectionChange：监听表格中多选框的选择变化，更新 multipleSelection 数组，用于批量删除操作。-->
<template>
  <div>
    <!--面包屑导航区域-->
    <div class="board">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>权限管理</el-breadcrumb-item>
        <el-breadcrumb-item>资源信息管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>


    <!-- 卡片视图 -->
    <el-card>
      <el-row :gutter="24">
        <el-col :span="24">
          <el-button type="primary" @click="addDialogVisible = true" style="font-size: 18px;">
             添加资源
          </el-button>
          <el-button type="danger" @click="multipleDelete" style="font-size: 18px;">
             批量删除
          </el-button>
        </el-col>
      </el-row>

      <el-table :data="resourceList" border stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="资源编号" width="145"></el-table-column>
        <el-table-column prop="name" label="资源名称"></el-table-column>
        <el-table-column prop="path" label="资源路径"></el-table-column>
        <el-table-column prop="level" label="权限等级" width="180">
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.level === 1">一级</el-tag>
            <el-tag type="warning" v-else-if="scope.row.level === 2">二级</el-tag>
            <el-tag v-else>三级</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="parent.name" label="父资源名称" width="180"></el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template slot-scope="scope">
            <el-tooltip effect="dark" style="font-size: 18px;" content="修改资源信息" placement="top" :enterable="false" :open-delay="500">
              <el-button type="primary" @click="showEditDialog(scope.row.id)">
                 编辑
              </el-button>
            </el-tooltip>
            <el-tooltip effect="dark" content="删除资源" placement="top" style="font-size: 18px;" :enterable="false" :open-delay="500">
              <el-button type="danger" @click="deleteResourceById(scope.row.id)">
                 删除
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!--分页区域-->
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

    <!--添加资源对话框-->
    <el-dialog title="添加资源" :visible.sync="addDialogVisible" width="50%" @close="addDialogClosed">
      <!--内容主题区-->
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="100px">
        <!--prop：在addFormRules中定义校验规则， v-model：双向绑定数据-->

        <el-form-item label="资源编号" prop="id">
          <el-input v-model="addForm.id"></el-input>
        </el-form-item>
        <el-form-item label="资源名称" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="资源路径" prop="path">
          <el-input v-model="addForm.path"></el-input>
        </el-form-item>
        <el-form-item label="父资源名称" prop="parentId">
          <el-select v-model="addForm.parentId" placeholder="请选择父资源" clearable >
            <el-option
                v-for="item in allResources"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!--底部区域-->
      <span slot="footer" class="dialog-footer">
      <el-button @click="addDialogVisible = false" style="font-size: 18px;">  取 消</el-button>
      <el-button type="primary" @click="addResource" style="font-size: 18px;">  确 定</el-button>
      </span>
    </el-dialog>

    <!--修改影厅对话框-->
    <el-dialog title="修改资源" :visible.sync="editDialogVisible" width="50%" @close="editDialogClosed">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <el-form-item label="资源编号" prop="id">
          <el-input v-model="editForm.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="资源名称" prop="name">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="资源路径" prop="path">
          <el-input v-model="editForm.path"></el-input>
        </el-form-item>
        <el-form-item label="父资源名称" prop="parentId">
          <el-select v-model="editForm.parentId" placeholder="请选择父资源" clearable >
            <el-option
                v-for="item in allResources"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false" style="font-size: 18px;">  取 消</el-button>
        <el-button type="primary" @click="editResource" style="font-size: 18px;">  确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "ResourceInfo",
  data() {
    return {
      queryInfo: {
        query: '',
        pageNum: 1,
        pageSize: 7
      },
      resourceList: [],
      allResources: [],
      total: 0,
      addDialogVisible: false,
      //添加资源的表单数据
      addForm: {
        name: '',
        path: '',
        level: 0,
        parentId: null
      },
      //验证表单规则对象
      addFormRules: {
        name: [
          { required: true, message: '请输入资源名', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入资源路径', trigger: 'blur'}
        ]
      },
      editDialogVisible: false,
      editForm: {},
      editFormRules: {
        name: [
          { required: true, message: '请输入资源名', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入资源路径', trigger: 'blur'}
        ]
      },
      multipleSelection: []
    }
  },
  created() {
    this.getResourceList()
    this.getAllResource()
  },
  methods: {
    async getResourceList(){
      const {data : res} = await axios.get('sysResource', {params: this.queryInfo})
      this.resourceList = res.data;
      this.total = res.total
      this.queryInfo.pageNum = res.pageNum
      this.queryInfo.pageSize = res.pageSize
    },
    async getAllResource(){
      const {data : res} = await axios.get('sysResource')
      this.allResources = res.data;
    },
    handleSizeChange(newSize) {
      this.queryInfo.pageSize = newSize
      this.getResourceList()
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNum = newPage
      this.getResourceList()
    },
    // 监听添加对话框的关闭事件
    addDialogClosed(){
      this.$refs.addFormRef.resetFields()
    },
    // 监听添加按钮
    addResource(){
      const _this = this;
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //预校验成功，发网络请求
        if(_this.addForm.parentId === null) _this.addForm.parentId = 0

        axios.defaults.headers.post['Content-Type'] = 'application/json'
        const { data : res} = await axios.post('sysResource', JSON.stringify(_this.addForm))
        if(res.code !== 200) return this.$message.error(res.msg)
        //隐藏添加对话框
        this.addDialogVisible = false
        //重新加载列表
        await this.getResourceList()
        this.$message.success('添加资源信息成功！')
      })
    },
    // 显示修改对话框，回显数据
    async showEditDialog(id){
      const _this = this
      await axios.get('sysResource/' + id ).then(resp => {
        console.log(resp)
        _this.editForm = resp.data.data
      })
      if(this.editForm.parentId === 0) this.editForm.parentId = ''
      this.editDialogVisible = true
    },
    // 监听修改对话框的关闭事件
    editDialogClosed(){
      this.$refs.editFormRef.resetFields()
    },
    // 修改影厅分类信息并提交
    editResource(){
      this.$refs.editFormRef.validate(async valid => {
        const _this = this
        if (!valid) return
        console.log(this.editForm.parentId)
        if(this.editForm.parentId === '') this.editForm.parentId = 0
        console.log(this.editForm.parentId)
        axios.defaults.headers.put['Content-Type'] = 'application/json'
        const {data : res} = await axios.put('sysResource/', JSON.stringify(_this.editForm))
        if(res.code !== 200) return this.$message.error(res.msg)

        this.editDialogVisible = false
        await this.getResourceList()
        this.$message.success('修改资源信息成功！')
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
        ids.push(data.id)
      })
      await axios.delete('sysResource/' + ids).then(resp => {
        if (resp.data.code !== 200){
          this.$message.success('批量删除资源信息失败！')
        }
      })
      await this.getResourceList()
      this.$message.success('批量删除资源信息成功！')
    },
    //根据id删除对应的资源信息
    async deleteResourceById(id){
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

      await axios.delete('sysResource/' + id).then(resp => {
        if (resp.data.code !== 200){
          _this.$message.success('删除资源信息失败！')
        }
      })
      await this.getResourceList()
      this.$message.success('删除资源信息成功！')
    }
  }
}
</script>

<style scoped>
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
/* 操作按钮hover效果 */
.el-button:not(.is-disabled):hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
}
</style>
