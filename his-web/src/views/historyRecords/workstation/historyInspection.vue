<template>
  <!-- 检查申请 -->
  <el-container>
  <!--  <div :width="mainwidth">
      <el-button type="primary" @click="print">打印</el-button>
    </div>-->
  <el-aside id="inspection" :width="mainwidth" style="background:white;padding:0 10px 0 0">
    <el-table
    ref="multipleTable"
    :data="record"
    border
    tooltip-effect="dark"
    style="width: 100%"
    @selection-change="handleSelectionChange">
    <el-table-column
    align="center"
      label="项目编码"
      width="120">
      <template slot-scope="scope">{{ scope.row.code }}</template>
    </el-table-column>
    <el-table-column
    align="center"
      prop="name"
      label="项目名称"
      width="200">
    </el-table-column>
    <el-table-column
    align="center"
      prop="deptName"
      label="执行科室"
      width="120">
    </el-table-column>
    <el-table-column
    align="center"
      prop="format"
      label="单位"
      show-overflow-tooltip>
    </el-table-column>
    <el-table-column
    align="center"
      prop="price"
      label="单价"
      show-overflow-tooltip>
    </el-table-column>
    <el-table-column
      align="center"
      label="状态"
      show-overflow-tooltip>
    <template slot-scope="scope">
      <el-tag type="warning" v-if="scope.row.status===-1">未开立</el-tag>
      <el-tag type="danger" v-if="scope.row.status===0">已作废</el-tag>
      <el-tag type="info" v-if="scope.row.status===1">未缴费</el-tag>
      <el-tag type="info" v-if="scope.row.status===2">未登记</el-tag>
      <el-tag type="info" v-if="scope.row.status===3">已登记</el-tag>
      <el-tag type="success" v-if="scope.row.status===4">已执行</el-tag>
      <el-tag type="danger" v-if="scope.row.status===5">已退费</el-tag>
    </template>
    </el-table-column>
    <el-table-column
    align="center"
      label="操作"
      show-overflow-tooltip>
      <template slot-scope="scope">
        <el-button type="text" v-if="scope.row.status!==0" @click="showresult(scope.row)">查看结果</el-button>
      </template>
    </el-table-column>
  </el-table>


  </el-aside>
  <el-dialog title="检查结果" :visible.sync="resultvisible" top="10px">
    <div class="block" style="height:100%;width:100%">
      <span>检查结果：{{checkresult}}</span>
      <el-carousel trigger="click" height="500px" style="margin-top:30px">
        <el-carousel-item v-for="item in checkresultimg" :key="item">
           <img :src="item" style="height:100%">
        </el-carousel-item>
      </el-carousel>
    </div>
  </el-dialog>
  </el-container>
</template>
<script>
import {getNondrugList} from '@/api/non_drug'
import {apply,list,invalid,deleteById} from '@/api/outpatient/nondrugapply'
import {getNondrugModelList} from '@/api/nondrugmodel'
import {deepClone} from '@/utils'
import {selectByType} from '@/api/outpatient/frequentuse'
import {saveNonDrug,getNonDrug} from '@/api/outpatient/save'
import { Promise, all } from 'q';
export default {
  props:['patient'],
  name:'Inspection',
  data(){
    return{
      resultvisible:false,
      checkresult:'',
      checkresultimg:[],
      freqlist:[],
      onemodel:{},
      totalprice:0.000,
      ref:[],
      checkmodels:[],
      check:{},
      demandVisible:false,
      dialogTableVisible:false,
      activeName:'first',
      isclose:true,
      checkList:[],
      mainwidth:"65%",
      activeNames: ['1'],
      total:0,
      search:'',
      listQuery: {
        code:null,
        name:null,
        format:null,
        price:null,
        expClassId: null,
        deptId: null,
        mnemonicCode:null,
        recordType: 1,
        createDate: null,
        status:1,
        pageSize:1000,
        pageNum:1
      },
      record:[]
    };
  },
   watch:{
    'patient' : function(newVal, oldVal){
      this.patient = newVal
      this.listRecord()
    },
  },
  created(){
    Promise.all([
      this.getNondrugList().then(()=>{
        this.getmodel()
      })
    ])
    this.getfreqList()
  },

  methods:{


    showresult(row){
      this.checkresult = row.checkResult
      if (this.checkresult === null) {
        this.$notify({
          title: '警告',
          message: '暂无检查结果查看',
          type: 'warning',
          duration: 2000
        })
        return
      }
      row.resultImgUrlList.split(',').forEach(item=>{
        this.checkresultimg.push(item)
      })
      this.resultvisible = true
    },

    async listRecord(){
      list(this.patient.registrationId,0).then(res=>{

        this.record = res.data

        this.record.forEach(item=>{
          this.checkList.filter(check=>{
            if(check.id===item.noDrugId){
              item.code = check.code
              item.name = check.name
              item.format = check.format
              item.price = check.price
            }
          })
          item.deptName = item.excuteDeptName
        })
      })
    },

    async getNondrugList() {
      const response = await getNondrugList(this.listQuery)

      this.checkList = response.data.list
      this.total = response.data.total
    },

    controlfast(){
      this.isclose=!this.isclose
      if(this.mainwidth==="65%")
        this.mainwidth="80%"
      else
        this.mainwidth="65%"
    },
    print(e){
      const subOutputRankPrint = document.getElementById('inspection')
      const newContent = subOutputRankPrint.innerHTML
      const oldContent = document.body.innerHTML
      document.body.innerHTML = newContent
      window.print()
      window.location.reload()
      document.body.innerHTML = oldContent
      return false
    }
  }
}
</script>

