<template>
  <div class="app-container">
    <!--搜索功能 -->
    <div class="filter-container">
      <el-input v-model="listQuery.code" placeholder="项目编码" style="width: 160px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.name" placeholder="项目名称" style="width: 160px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.mnemonicCode" placeholder="拼音助记码" style="width: 160px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.recordType" placeholder="项目类型" clearable style="width: 130px" class="filter-item" filterable>
        <el-option v-for="item in [{name:'检查',id:'1'},{name:'检验',id:'2'},{name:'处置',id:'3'}]" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-select v-model="listQuery.deptId" placeholder="执行科室" clearable class="filter-item" style="width: 130px" filterable>
        <el-option v-for="item in Alldept" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button :size="small" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
      </el-button>
      <el-button class="filter-item"  style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleAdd">
        新增项目
      </el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
        导出项目
      </el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="danger" icon="el-icon-download" @click="handleSomeDelete">
        批量删除
      </el-button>
    </div>
    <!--项目列表 -->
    <el-table :data="nondrugList" style="width: 100%;margin-top:30px;" border @selection-change="changedep">  <!--v-loading="listLoading"-->
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="code" align="center" label="项目编码" width="100">
        <template slot-scope="scope">
          {{ scope.row.code }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="项目名称">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="规格" width="60">
        <template slot-scope="scope">
          {{ scope.row.format }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="价格" width="60">
        <template slot-scope="scope">
          {{ scope.row.price }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="执行科室" width="150">
        <template slot-scope="scope">
          {{ scope.row.deptName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="拼音助记码">
        <template slot-scope="scope">
          {{ scope.row.mnemonicCode }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="项目类型" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.recordType===1">检查</span>
          <span v-if="scope.row.recordType===2">检验</span>
          <span v-if="scope.row.recordType===3">处置</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="地点" width="150">
        <template slot-scope="scope">
          {{ scope.row.place }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间">
        <template slot-scope="scope">
          {{ scope.row.createDate }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">修改</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  <!--  <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getAllNondrug" />-->

    <el-dialog :visible.sync="dialogVisible" width="550px" :title="dialogType==='edit'?'修改项目信息':'新增项目'"> <!--@close="getDeplist"-->
      <el-form ref="depart" :model="nondrug" style="width:400px" label-width="120px"  label-position="left" :rules="rules">
        <el-form-item label="项目编码" prop="code">
          <el-input v-model="nondrug.code" placeholder="项目编码" />
        </el-form-item>
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="nondrug.name" placeholder="项目名称" />
        </el-form-item>
        <el-form-item label="规格" prop="format">
          <el-input v-model="nondrug.format" placeholder="规格" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number controls-position="right" style="width:100px" :min="1" v-model="nondrug.price" placeholder="价格" />
        </el-form-item>
        <el-form-item label="执行科室">
          <el-select v-model="nondrug.deptId" placeholder="执行科室" clearable filterable>
              <el-option v-for="item in Alldept" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="拼音助记码" prop="mnemonicCode">
          <el-input v-model="nondrug.mnemonicCode" placeholder="拼音助记码" />
        </el-form-item>
        <el-form-item label="项目类型">
          <el-select v-model="nondrug.recordType" placeholder="项目类型" clearable style="width: 130px" class="filter-item">
            <el-option v-for="item in [{name:'检查',id:1},{name:'检验',id:2},{name:'处置',id:3}]" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="地点" >
          <el-input v-model="nondrug.place" placeholder="地点" />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmDep('depart')">确认</el-button>
      </div>
    </el-dialog>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getNondrugList" />
  </div>
</template>

<script>
import path from 'path'
import { deepClone,parseTime } from '@/utils'
import {getNondrugList,getAllNondrug,updateNondrug,createNondrug,deleteNondrug} from '@/api/non_drug'
import {getAllDep} from '@/api/department'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { Promise } from 'q';

const defaultNondrug = {
  code:'',
  name:'',
  format:'',
  price:'',
  expClassId: '',
  deptId: '',
  mnemonicCode:'',
  recordType: '',
  createDate: '',
  place:''
}

export default {
  components: {Pagination},
  data() {
    return {
      nondrug: Object.assign({},defaultNondrug),
      nondrugList: [],
      Alldept:[],
      dialogVisible: false,
      dialogType: 'new',
      checkStrictly: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      deptype:[1,2,3,4],
      total: 0,
      listLoading: true,
      allCatId:[],
      allNondrug:[],
      allType:[],
      listQuery: {
        code:null,
        name:null,
        format:null,
        price:null,
        expClassId: null,
        deptId: null,
        mnemonicCode:null,
        recordType: null,
        createDate: null,
        status:1,
        pageSize:10,
        pageNum:1
      },
      listNull:{
      },
      downloadLoading: false,
      rules:{
        code:[
          {required: true, message: '请输入项目编码',trigger: 'blur'}
        ],
        name:[
          {required: true, message: '请输入项目名称',trigger: 'blur'}
        ],
        format:[
          {required: true, message: '请输入规格',trigger: 'blur'}
        ],
        price:[
          {required: true,trigger: 'blur', validator: (rule, value, callback) => {
            if (!value) {
              return callback(new Error('请输入价格'));
            }
            if (isNaN(value) || value < 0) {
              callback(new Error('请输入正确的价格'));
            } else {
              callback();
            }
          }}
        ],
        expClassId:[
          {required:true , message:'请输入费用所属科目',trigger:'blur'}
        ],
        recordType:[
          {required:true,message:'请选择项目类型',trigger:'blur'}
        ]
      },
      deplistref:[],
    }
  },
  computed: {

  },
  created() {
    // Mock: get all routes and roles list from server
    Promise.all([
    this.getAllNondrug(),
    this.getNondrugList()
    ])
    this.getAllDep()
  },
  methods: {
    getAllDep(){
      getAllDep().then(res=>{
        this.Alldept=res.data
      })
    },
    async getAllNondrug(){
      this.listLoading = true
      const response = await getNondrugList(this.listNull)
      this.allNondrug = response.data.list
      this.total=response.data.total
      this.listLoading = false
    },
    async getNondrugList() {
      this.listLoading = true
      const response = await getNondrugList(this.listQuery)
      this.nondrugList = response.data.list
      this.allNondrug=response.data.list
      if(this.nondrugList!=null && this.nondrugList.length!=0){
        this.nondrugList.forEach(item=>{
          if(item.createDate!=null){
            item.createDate = item.createDate.substr(0,10)
          }
        })
      }
      this.total = response.data.total
      this.listLoading = false
    },
    handleFilter() {
      this.listQuery.pagesize = 10
      this.listQuery.pageNum=1
      this.getNondrugList()
    },
    handleAdd() {
      this.resetTemp()
      this.dialogType = 'new'
      this.dialogVisible = true
      this.checkStrictly = true
      this.$nextTick(() => {
        this.$refs['depart'].clearValidate()
      })
    },
    handleEdit(row) {
      this.resetTemp()
      this.nondrug = Object.assign({}, row) // copy obj
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.checkStrictly = true
      this.$nextTick(() => {
        this.$refs['depart'].clearValidate()
      })
    },
    resetTemp() {
      this.nondrug = Object.assign({},defaultNondrug)
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
    },
    handleSomeDelete(){
      if (this.deplistref.length === 0) {
        this.$notify({
          title: '提示',
          message: "请勾选数据！",
          type: 'warning',
          duration: 2000
        })
        return;
      }
      this.$confirm('确认删除选中项目?', '警告', {
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

      deleteNondrug(delsom).then(res=>{
      this.$notify({
        title: '成功',
        message: res.message,
        type: 'success',
        duration: 2000
      })
        this.getNondrugList()
      }
      )
      })
    }
    ,
    handleDelete(row) {
      this.$confirm('确认删除当前项目?', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
      deleteNondrug(row.id).then(res=>{
        this.$notify({
        title: '成功',
        message: res.message,
        type: 'success',
        duration: 2000
      })
      this.getNondrugList()
      })
      })
    },
    handleDownload() {
      this.downloadLoading = true
      /*this.getAllNondrug()*/

      this.alldrug = deepClone(this.allNondrug);

      this.alldrug.forEach(item=>{
        if(item.recordType===1){
          item.recordType='检查'
        }else if(item.recordType===2){
          item.recordType='检验'
        }else if(item.recordType===3){
          item.recordType='处置'
        }
      })
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['项目编码', '项目名称','规格','价格','执行科室','拼音助记码','项目类型','创建时间']
        const filterVal = ['code', 'name','format','price','deptName','mnemonicCode','recordType','createDate']
        const data = this.formatJson(filterVal, this.alldrug)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: '项目目录'
        })
        this.downloadLoading = false
      })
    },
    async confirmDep(formName){
      this.$refs[formName].validate((valid)=>{
        if(valid){
          const isEdit = this.dialogType === 'edit'
          this.listLoading=true
          if(isEdit){
            updateNondrug(this.nondrug).then(res=>{
                this.getNondrugList()
                this.$notify({
                title: '成功',
                message: '已修改该项目',
                type: 'success',
                duration: 2000
              })
            })
            this.dialogVisible=false
          }else{
            this.nondrug.expClassId = 1
            this.nondrug.status = 1
            createNondrug(this.nondrug).then(res=>{
              this.getNondrugList()
              this.$notify({
              title: '成功',
              message: res.message,
              type: 'success',
              duration: 2000
            })
            this.dialogVisible=false
            })
          }
        }
      })
    },
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

