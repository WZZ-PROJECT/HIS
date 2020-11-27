<template>
  <!-- 检查申请 -->
  <el-container>
   <!-- <div :width="mainwidth">
      <el-button type="primary" @click="print">打印</el-button>
    </div>-->
  <el-aside :width="mainwidth" style="background:white;padding:0 10px 0 0">
    <el-table
      id="historyInspection1"
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
        <el-button type="text" v-if="scope.row.status===-1" @click="demand(scope.row)">检查要求</el-button>
        <el-button type="text" v-if="scope.row.status===4" @click="showresult(scope.row)">查看结果</el-button>
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

    saveNonDrug(){
      let data = {}
      data.dmsNonDrugItemRecordParamList = this.ref
      data.registrationId = this.patient.registrationId
      data.type = 0
      saveNonDrug(data).then(res=>{
        this.$notify({
          title: '成功',
          message: '已暂存选中的检查项',
          type: 'success',
          duration: 2000
        })
      })
    },
    getNonDrug(){
      let data = {}
      data.registrationId = this.patient.registrationId
      data.type = 0
      getNonDrug(data).then(res=>{
        res.data.dmsNonDrugItemRecordParamList.forEach(item=>{
          this.selectCheckred(item)
        })
        this.$notify({
          title: '成功',
          message: '已取出暂存的检查项',
          type: 'success',
          duration: 2000
        })
      })
    },
    showresult(row){
      this.checkresult = row.checkResult
      row.resultImgUrlList.split(',').forEach(item=>{
        this.checkresultimg.push(item)
      })
      this.resultvisible = true
    },
    addfreitem(val){
      this.selectCheck(val)
    },
    getfreqList(){
      let data = {}
      data.staffId = this.$store.getters.id
      data.selectType = 1
      selectByType(data).then(res=>{
        this.freqlist = res.data.checkList
      })
    },
    addModel(val){
     /* console.log(val)
      this.$confirm('是否确定将 模板:'+val.name+' 中的内容导入该患者的检查中', '导入模板', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          val.nondruglist.forEach(item=>{
            item.status = -1
            let flag = 1
            this.record.forEach(com=>{
              if(com.id===item.id){
                flag = 0
              }
            })
            if(flag)
              this.record.push(item)
          })
        })*/
      this.model = deepClone(val)
      this.model.amount = Math.floor((this.model.amount+0.5)*100)/100
      this.modelvisivle = true
    },
    selectModel(val){
      this.onemodel = val
    },
    async getmodel(){
      let data = {}
      data.scope = 0
      data.ownId = this.$store.getters.id
      data.type = 0
      data.pageSize =10000
      data.pageNum = 1
      data.isAdmin = 0
      await getNondrugModelList(data).then(res=>{
        this.checkmodels = res.data.list
        this.checkmodels.forEach(onemodel=>{
          onemodel.nondruglist = []
          onemodel.totalprice =0.00
          this.checkList.filter(item=>{
            if(onemodel.nonDrugIdList.includes(item.id)){
              onemodel.nondruglist.push(item)
              onemodel.totalprice += item.price
            }
          })
        })
      })
    },
    refresh(){
      this.$confirm('未开立的项目都将清除,确认刷新?', '刷新', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          this.listRecord()
        })
    },
    deleteTS(){
      this.$confirm('所选项目都将清除,确认刷新?', '刷新', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.listRecord()
      })
    },
    invalid(){
      let data = ''
      this.ref.forEach(item=>{
        data +=(item.id+',')
      })
      data = data.substr(0,data.length-1)
      invalid(data).then(res=>{
        this.$notify({
          title: '成功',
          message: res.message,
          type: 'success',
          duration: 2000
        })
        this.refresh()
      })

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
    handleSelectionChange(val){
      this.ref = val
      this.totalprice = 0.00
      this.ref.forEach(item=>{
        this.totalprice+=item.price
      })
      this.totalprice = this.totalprice.toFixed(2)
    },
    handleClick(){
      this.$notify({
        title: '检查',
        message: '您开立的项目中包含已开立的项目，请检查后重新提交！',
        type: 'warning',
        duration: 2000
      })
      return false;
    },
    apply(){
      let flags=0
      this.ref.forEach(item=>{
        if(item.status!=-1){
          this.handleClick()
          flags=1;
        }
      })
      if(flags===0){
        let data = {}
        data.dmsNonDrugItemRecordParamList = this.ref
        data.createStaffId = this.$store.getters.id
        data.registrationId = this.patient.registrationId
        data.dmsNonDrugItemRecordParamList.forEach(item=>{
          item.amount = item.price
          item.excuteDeptId = item.deptId
          item.noDrugId = item.id
        })
        data.type = 0
        apply(data).then(res=>{
          this.$notify({
            title: '成功',
            message: res.message,
            type: 'success',
            duration: 2000
          })
          this.listRecord()
        })
      }
    },
    submitDemand(){
      this.record.forEach(item=>{
        if(item.id===this.check.id){
          item.aim = this.check.aim
          item.demand = this.check.demand
          item.clinicalImpression = this.check.clinicalImpression
          item.clinicalDiagnosis = this.check.clinicalDiagnosis
          item.checkParts = this.check.checkParts
        }
      })
      this.demandVisible = false
    },
    demand(row){
      this.demandVisible = true
      this.check = deepClone(row)
    },
    async getNondrugList() {
      const response = await getNondrugList(this.listQuery)

      this.checkList = response.data.list
      this.total = response.data.total
    },
    selectCheckred(val){

      let flag = 1
      val.status = -1
      this.record.forEach(item=>{
        if(item.id===val.id){
          flag=0
        }
      })
      if(flag)
        this.record.push(val)
      this.dialogTableVisible = false
    },
   /* selectCheck(val){
      this.$confirm('是否添加 '+val.name+' 到该患者?', '添加检查', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(()=>{
        let flag = true
        if(this.record==null){
          this.record=[]
        }
        if(this.record.length==0){
          this.record.push(val)
        }else {
          this.record.forEach(item=>{
            if(item.icd===val.icd){
              flag=false
            }
          })
          if(flag) {
            this.record.push(val)
          }else {
            this.$notify({
              title: '提示',
              message: '已存在该项目',
              type: 'warning',
              duration: 2000
            })('已存在该诊断！')
          }
        }
        this.dialogTableVisible = false
      })
    },*/
    selectCheck(val){

      val.status=0
      this.$confirm('是否添加 '+val.name+' 到该患者?', '添加检查', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'success'
      }).then(()=>{
        let flag = 1
        val.status = -1
        this.record.forEach(item=>{
          if(item.id===val.id){
            flag=0
          }
        })
        if(flag)
          this.record.push(val)
        else
          this.$notify({
              title: '提示',
              message: '已存在该项目',
              type: 'warning',
              duration: 2000
            })('已存在该检验项目！')
        this.dialogTableVisible = false
      })
    },
    addcheck(){
      this.dialogTableVisible = true
    },
    delcheck(){
      let data = ''
      this.ref.forEach(item=>{
        data +=(item.id+',')
      })
      data = data.substr(0,data.length-1)
      deleteById(data).then(res=>{
        this.$notify({
          title: '成功',
          message: res.message,
          type: 'success',
          duration: 2000
        })
        this.deleteTS()
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
      const subOutputRankPrint = document.getElementById('historyInspection1')
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

