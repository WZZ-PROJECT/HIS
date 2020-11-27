<template>
  <!-- 草药处方 -->
  <div>
  <el-container>
    <!--<div :width="mainwidth">
      <el-button type="primary" @click="print">打印</el-button>
    </div>-->
  <el-aside id="cparescription" :width="mainwidth" style="background:white;padding:0 10px 0 0">
    <el-table
    ref="multipleTable"
    :data="prescriptionList"
    tooltip-effect="dark"
    style="width: 100%"
    cell-style="text-align:center"
    header-cell-style="text-align:center"
    @selection-change="handleSelectionChange">
    <el-table-column
    align="center"
      label="处方名称">
      <template slot-scope="scope">{{ scope.row.name }}</template>
    </el-table-column>
    <el-table-column
     align="center"
      prop="amount"
      label="总金额(元)">
    </el-table-column>
    <el-table-column label="状态">
      <template slot-scope="scope">
        <el-tag type="success" v-if="scope.row.status===-1">未开立</el-tag>
        <el-tag type="success" v-if="scope.row.status===0">已作废</el-tag>
        <el-tag type="success" v-if="scope.row.status===1">未缴费</el-tag>
        <el-tag type="success" v-if="scope.row.status===2">未发药</el-tag>
        <el-tag type="success" v-if="scope.row.status===3">已发药</el-tag>
        <el-tag type="success" v-if="scope.row.status===4">已过期</el-tag>
      </template>
    </el-table-column>
  </el-table>
  </el-aside>
  </el-container>
  </div>
</template>
<script>
import {getdrugList,selectById} from '@/api/drug'
import Pagination from '@/components/Pagination'
import {apply,listByReg,invalid} from '@/api/outpatient/cprescription'
import {deepClone} from '@/utils'
import {selectByType} from '@/api/outpatient/frequentuse'
import {getDrugPrescription,saveDrugPrescription} from '@/api/outpatient/save'
import {listModel} from '@/api/drugmodel'
const defaultpre={
  name:'',
  druglist:[],
  amount:0,
  status:-1,
}
export default {
   props:['patient'],
   components: {Pagination},
   name:'Prescription',

  data(){
    return{
      modelvisivle:false,
      model:{},
      models:[],
      freqlist:[],
      refs:[],
      prescriptionList:[],
      oneprescription:{
        name:'',
        druglist:[],
        amount:0,
        status:-1,
      },
      edit:false,
      test:'',
      num:0,
      dialogTableVisible:false,
      activeName:'first',
      isclose:true,
      record:{
        main:'test'
      },
      page:{
        pageNum:1,
        pageSize:10,
      },
      searchdrug:'',
      drugList:[],
      total:0,
      mainwidth:"65%",
      activeNames: ['1'],
      data2:[
        {
          date: '0001',
          name: '王小虎1',
          address: '38'
        },
        {
          date: '0002',
          name: '王小虎2',
          address: '39'
        }
      ]
    };
  },
  watch:{
    'patient' : function(newVal, oldVal){
      this.patient = newVal
      this.listByReg()
    },
  },
  created(){
    this.listModel()
  },
  methods:{

    async listByReg(){
      listByReg(this.patient.registrationId).then(res=>{
        this.prescriptionList=res.data
      })
    },


    controlfast(){
      this.isclose=!this.isclose
      if(this.mainwidth==="65%")
        this.mainwidth="80%"
      else
        this.mainwidth="65%"
    },
    print(e){
      const subOutputRankPrint = document.getElementById('cparescription')
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

