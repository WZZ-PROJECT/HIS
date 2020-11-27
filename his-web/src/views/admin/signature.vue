<template>
  <div class="app-container">
    <!--搜索功能 -->
    <div class="filter-container">
      <el-input v-model="listQuery.code" placeholder="签章编码" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.name" placeholder="签章名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.type" placeholder="签章类型" clearable style="width: 130px" class="filter-item" filterable>
        <el-option v-for="item in allSignature" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button  class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleAdd">
        新增签章
      </el-button>
      <el-button  :loading="downloadLoading" class="filter-item" type="danger" icon="el-icon-download" @click="handleSomeDelete">
        批量删除
      </el-button>
    </div>
    <!--科室列表 -->
    <el-table v-loading="listLoading" :data="signatureList" style="width: 100%;margin-top:30px;" border @selection-change="changedep">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="code" align="center" label="签章编码" width="220">
        <template slot-scope="scope">
          {{ scope.row.code }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="签章名称" width="220">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="签章类型" width="220">
        <template slot-scope="scope">
          {{ scope.row.typeName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">修改</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="primary" v-if="scope.row.state===2" size="small" @click="handleEnable(scope.row)">启用</el-button>
          <el-button type="danger" v-if="scope.row.state===1" size="small" @click="handleDisable(scope.row)">禁用</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="dialogVisible" width="500px" :title="dialogType==='edit'?'修改科室信息':'新增科室'" @close="getSignatureList">
      <el-form ref="depart" :model="depart" label-width="80px"  label-position="left" :rules="rules">
        <el-form-item label="签章编码" prop="code">
          <el-input v-model="depart.code" placeholder="签章编码" />
        </el-form-item>
        <el-form-item label="签章名称" prop="name">
          <el-input v-model="depart.name" placeholder="签章名称" />
        </el-form-item>
        <el-form-item label="签章类型" prop="type">
          <el-select v-model="depart.type" placeholder="签章类型" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in allSignature" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmDep('depart')">确认</el-button>
      </div>
    </el-dialog>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getSignatureList" />
  </div>
</template>

<script>
import path from 'path'
import { deepClone } from '@/utils'
import { getSignatureList,selectType,createSignature,deleteSignature,updateSignature } from '@/api/signature'
import UploadExcelComponent from '@/components/UploadExcel/index.vue'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { Promise } from 'q';

const defaultDepart = {
  id:'',
  code: '',
  name: '',
  type: '',
  state:''
}

export default {
  components: {Pagination,UploadExcelComponent},

  data() {
    return {
      tableData: [],
      tableHeader: [],
      uploadVisible:false,
      depart: Object.assign({},defaultDepart),
      //签章返回集
      signatureList: [],
      dialogVisible: false,
      dialogType: 'new',
      checkStrictly: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      total: 0,
      listLoading: true,
      allSignature:[],
      // 查询所需参数
      listQuery: {
        code: '',
        name: '',
        type: '',
        state:'',
        page: 1,
        limit: 10
      },
      downloadLoading: false,
      rules:{
        code:[
          {required: true, message: '请输入签章编码',trigger: 'blur'}
        ],
        name:[
          {required: true, message: '请输入签章名称',trigger: 'blur'}
        ],
        type:[
          {required: true, message: '请选择签章类别',trigger: 'blur'}
        ]
      },
      deplistref:[]
    }
  },
  computed: {

  },
  created() {
    Promise.all([
    this.selectType(),
    this.getSignatureList()
    ])
  },
  methods: {
    beforeUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1
      if (isLt1M) {
        return true
      }
      this.$message({
        message: 'Please do not upload files larger than 1m in size.',
        type: 'warning'
      })
      return false
    },
    handleSuccess({ results, header }) {
      this.tableData = results
      this.tableHeader = header
      this.tableData.forEach(item=>{
        createSignature(item).then(res=>{
          this.getSignatureList()
          this.$notify({
          title: '成功',
          message: '成功从Excel导入科室数据',
          type: 'success',
          duration: 2000
        })
          this.uploadVisible = false
        })
      })

    },
    async selectType(){
      const response = await selectType()
      this.allSignature = response.data
    },

    // 查询列表
    async getSignatureList() {
      this.listLoading = true
      const response = await getSignatureList(this.listQuery)
      this.signatureList = response.data.list
      this.total = response.data.total
      this.listLoading = false
    },
    // 添加签章
    handleAdd() {
      this.resetTemp()
      this.dialogType = 'new'
      this.dialogVisible = true
      this.checkStrictly = true
      this.$nextTick(() => {
        this.$refs['depart'].clearValidate()
      })
    },
    // 修改签章
    handleEdit(row) {
      this.resetTemp()
      this.depart = Object.assign({}, row) // copy obj
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.checkStrictly = true
      this.$nextTick(() => {
        this.$refs['depart'].clearValidate()
      })
    },
    // 添加签章或修改签章
    async confirmDep(formName){
      this.$refs[formName].validate((valid)=>{
        if(valid){
          const isEdit = this.dialogType === 'edit'
          this.listLoading=true
          if(isEdit){
            updateSignature(this.depart).then(res=>{
              this.getSignatureList()
              this.$notify({
                title: '成功',
                message: '已修改该签章信息',
                type: 'success',
                duration: 2000
              })
            })
            this.dialogVisible=false
          }else{
            // 添加签章
            this.depart.state = 2;
            createSignature(this.depart).then(res=>{
              this.getSignatureList()
              this.$notify({
                title: '成功',
                message: '新增签章成功！',
                type: 'success',
                duration: 2000
              })
              this.dialogVisible=false
            })
          }
        }
      })
    },
    //删除指定签章
    handleDelete(row) {
      this.$confirm('确认删除当前签章?', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        deleteSignature(row.id).then(res=>{
          this.$notify({
            title: '成功',
            message: '已删除该签章',
            type: 'success',
            duration: 2000
          })
          this.getSignatureList()
        })
      })
    },
    // 批量删除签章
    handleSomeDelete(){
      this.$confirm('确认删除选中科室?', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        let delsom =''
        for(let i=0;i<this.deplistref.length;i++){
          if(i!==this.deplistref.length-1)
            delsom=delsom+(this.deplistref[i].id)+','
          else
            delsom=delsom+(this.deplistref[i].id)
        }
        deleteSignature(delsom).then(res=>{
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getSignatureList()
          }
        )
      })
    },

    //启用 禁用
    handleEnable(row){


      let status = []
      this.signatureList.forEach(item =>{
        status.push(item.state)
      })
      if (status.join(',').includes(1)) {
        this.$notify({
          title: '提示',
          message: "已存在启用状态的印章！",
          type: 'warning',
          duration: 2000
        })
        return;
      }else {
        this.depart = Object.assign({}, row);
        this.depart.state = 1;
        this.handleDisableOrEnable(this.depart)
      }
    },
    handleDisable(row){
      this.depart = Object.assign({}, row);
      this.depart.state = 2;
      this.handleDisableOrEnable(this.depart)
    },
    handleDisableOrEnable(data){
      this.$confirm('确认启用或禁用当前签章?', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        updateSignature(data).then(res=>{
          this.getSignatureList()
          this.$notify({
            title: '成功',
            message: '已启用或已禁用该签章',
            type: 'success',
            duration: 2000
          })
          this.getSignatureList()
        })
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.listQuery.limit = 10
      this.getSignatureList()
    },
    resetTemp() {
      this.depart.id=''
      this.depart.name=''
      this.depart.code=''
      this.depart.type=''
      this.depart.state=''
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    changedep(val){
      this.deplistref=val;
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }
  .permission-tree {
    margin-bottom: 30px;
  }
}
body .el-table th.gutter{
display: table-cell!important;
}
</style>

