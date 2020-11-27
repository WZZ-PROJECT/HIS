<template>
  <!-- 处置申请 -->
  <el-container>
    <!--<div :width="mainwidth">
      <el-button type="primary" @click="print">打印</el-button>
    </div>-->
  <el-aside :width="mainwidth" style="background:white;padding:0 10px 0 0">
    <el-table
      id="handle"
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
    </template>
    </el-table-column>
  </el-table>
  </el-aside>
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
        recordType: 3,
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

    async listRecord(){
      list(this.patient.registrationId,2).then(res=>{
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
      const subOutputRankPrint = document.getElementById('handle')
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

