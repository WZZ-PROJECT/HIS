<template>
  <!-- 草药处方 -->
  <div>
  <el-container>
   <!-- <div :width="mainwidth">
      <el-button type="primary" @click="print">打印</el-button>
    </div>-->
  <el-aside :width="mainwidth" style="background:white;padding:0 10px 0 0">
    <el-table
      id="historyCprescription1"
    ref="multipleTable"
    :data="prescriptionList"
    tooltip-effect="dark"
    style="width: 100%"
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
        addmodel(val){
      val.amount = Math.floor((val.amount+0.5)*100)/100
      val.status =-1
      this.prescriptionList.push(val)
    },
    selectmodel(val){

      this.model = deepClone(val)
      this.model.amount = Math.floor((this.model.amount+0.5)*100)/100
      this.modelvisivle = true
    },
    listModel(){
      let data = {}
      data.scope = 0
      data.ownId = this.$store.getters.id
      data.type = 2
      data.pageSize = 1000
      data.pageNum = 1
      data.isAdmin = 0
      listModel(data).then(res=>{
        this.models = res.data.list
        this.models.forEach(model=>{
          model.druglist = []
          model.amount = 0
          model.dmsMedicineModelItemList.forEach(item=>{
            let data = {}
            selectById(item.id).then(res=>{
              data = res.data
              data.days = item.days
              data.frequency = item.frequency
              data.medicalAdvice = item.medicalAdvice
              data.medicineUsage = item.medicineUsage
              data.usageMeans = item.usageMeans
              data.usageNum = item.usageNum
              data.usageNumUnit = item.usageNumUnit
              model.amount += (data.price*item.num)
              model.druglist.push(deepClone(data))
            })
          })
          model.amount = Math.floor((model.amount+0.5)*100)/100
        })
      })
    },
    saveDrugPrescription(){
      saveDrugPrescription(this.refs,this.patient.registrationId,5).then(res=>{
        this.$notify({
          title: '成功',
          message: '已暂存选中的草药处方',
          type: 'success',
          duration: 2000
        })
      })
    },
    addfreitem(val){
      this.selectCheck(val)
    },
    getfreqList(){
      let data = {}
      data.staffId = this.$store.getters.id
      data.selectType = 6
      selectByType(data).then(res=>{
        this.freqlist = res.data.drugList.filter(item=>{
          if(item.typeId === 103)
            return true
        })
      })
    },
    refresh(){
      this.$confirm('未开立的处方都将清除,确认刷新?', '刷新', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          this.listByReg()
        })
    },
    addfreitem(val){
      this.selectCheck(val)
    },
    getfreqList(){
      let data = {}
      data.staffId = this.$store.getters.id
      data.selectType = 6
      selectByType(data).then(res=>{
        this.freqlist = res.data.drugList.filter(item=>{
          if(item.typeId === 103)
            return true
        })

      })
    },
    deletepre(){
      let data = this.refs[0].name
      this.prescriptionList=this.prescriptionList.filter(item=>{
        if(item.name===data)
          return false
        return true
      })
    },
    invalid(){
      let data = this.refs[0]

      invalid(data.dmsHerbalItemRecordResultList[0].prescriptionId).then(res=>{
        this.$notify({
          title: '成功',
          message: res.message,
          type: 'success',
          duration: 2000
        })
        this.listByReg()
      })
    },
    async listByReg(){
      listByReg(this.patient.registrationId).then(res=>{
        this.prescriptionList=res.data
      })
    },
    handleSelectionChange(val){
      this.refs = val
    },
    async apply(){
      let data = this.refs[0]
      data.createStaffId = this.$store.getters.id
      data.registrationId = this.patient.registrationId
      data.dmsHerbalItemRecordList = data.druglist
      data.dmsHerbalItemRecordList.forEach(item=>{
        item.drugId = item.id
        item.currentNum = item.num
        item.totalNum = item.num
      })
      apply(data).then(res=>{
         this.$notify({
          title: '成功',
          message: res.message,
          type: 'success',
          duration: 2000
        })
        this.listByReg()
      })
    },
    createpre(){
      this.edit = false

      this.prescriptionList.push(this.oneprescription)
      this.dialogTableVisible = false
    },
    changepre(){
      this.dialogTableVisible =false
    },
    changenum(val){
      this.oneprescription.amount=0
      this.oneprescription.druglist.forEach(item=>{
        this.oneprescription.amount+=item.price*item.num
      })
      this.oneprescription.amount = Math.floor((this.oneprescription.amount+0.5)*100)/100
    },
    deldrug(row){
      this.oneprescription.druglist = this.oneprescription.druglist.filter(item=>{
        if(item.id===row.id)
          return false
        return true
      })
    },
    choosedrug(val){
      let flag = 1
      this.oneprescription.druglist.forEach(item=>{
        if(item.id===val.id){
          item.num+=1
          flag=0
        }
      })
      if(flag){
        this.oneprescription.amount +=val.price
        this.oneprescription.amount = Math.floor((this.oneprescription.amount+0.5)*100)/100
        this.oneprescription.druglist.push(val)
        this.oneprescription.druglist.forEach(item=>{
          if(item.num===undefined)
            item.num=1
        })
      }
    },
    async getdrugList() {
      let data = {}
      data.pageSize = this.page.pageSize
      data.pageNum = this.page.pageNum
      data.typeId = 103
      data.status = 1
      data.name = this.searchdrug
      const response = await getdrugList(data)
      this.drugList = response.data.list
      this.total = response.data.total
    },
    addpre(){
      this.getfreqList()
      this.oneprescription = deepClone(defaultpre)
      this.getdrugList()
      this.dialogTableVisible = true
    },
    showDetail(row){
      this.edit = true
      this.getfreqList()
      this.getdrugList()
      this.oneprescription = row
      this.dialogTableVisible=true
    },
    controlfast(){
      this.isclose=!this.isclose
      if(this.mainwidth==="65%")
        this.mainwidth="80%"
      else
        this.mainwidth="65%"
    },
    print(e){
      const subOutputRankPrint = document.getElementById('historyCprescription1')
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

