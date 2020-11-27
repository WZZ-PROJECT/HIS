<template>
  <div>
  <!-- 确诊 -->
  <el-container>
   <!-- <div :width="mainwidth">
      <el-button type="primary" @click="print">打印</el-button>
    </div>-->
  <el-aside id="historyComfirm1" :width="mainwidth" style="background:white;">
    <el-tag style="margin-bottom:20px;margin-left:-20px" type="info">初诊内容:</el-tag>
    <el-form :model="prerecord" disabled style="color:black">
      <el-form-item label="主诉"><el-input readonly v-model="prerecord.chiefComplaint" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="主述" style="width:80%;float:right;"></el-input></el-form-item>
      <el-form-item label="现病史"><el-input readonly v-model="prerecord.historyOfPresentIllness" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="现病史"  style="width:80%;float:right;"></el-input></el-form-item>
      <el-form-item label="现病治疗情况"><el-input readonly v-model="prerecord.historyOfTreatment" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="现病治疗情况" style="width:80%;float:right"></el-input></el-form-item>
      <el-form-item label="既往史"><el-input readonly v-model="prerecord.pastHistory" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="既往史" style="width:80%;float:right"></el-input></el-form-item>
      <el-form-item label="过敏史"><el-input readonly v-model="prerecord.allergies" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="过敏史" style="width:80%;float:right"></el-input></el-form-item>
      <el-form-item label="体格检查"><el-input readonly v-model="prerecord.healthCheckup" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="体格检查" style="width:80%;float:right"></el-input></el-form-item>
    </el-form>
    <el-tag style="margin-bottom:20px;margin-left:-20px">检查检验结果:</el-tag>
    <el-form :model="prerecord">
      <el-form-item label="检查结果"><el-input disabled v-model="prerecord.checkResult" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="检查结果" style="width:40%"></el-input></el-form-item>
      <el-form-item label="检验结果"><el-input disabled v-model="prerecord.testResult" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="检验结果" style="width:40%"></el-input></el-form-item>
      <el-form-item label="医嘱"><el-input disabled v-model="prerecord.testAdvice" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="医嘱" style="width:40%;margin-left:3%"></el-input></el-form-item>
    </el-form>
    <div style="margin-left:-20px;margin-bottom:30px">
      <el-tag>评估诊断:</el-tag>
      <el-card style="width:85%">
        <el-table :data="records">
          <el-table-column label="ICD编码" prop="icd"></el-table-column>
          <el-table-column label="名称" prop="name"></el-table-column>
          <el-table-column label="编码" prop="code" ></el-table-column>
        </el-table>
      </el-card>
    </div>
  </el-aside>
  </el-container>
  </div>
</template>
<script>
import {getnonend,submitdefinite,Clinical,selectEndCaseHistory} from '@/api/outpatient/dmscase'
import {getDmsDislist,parseList} from '@/api/diagnosis'
import Pagination from '@/components/Pagination'
import {selectByType} from '@/api/outpatient/frequentuse'
import {parseTime} from '@/utils'
export default {
  props:['patient'],
  components: {Pagination},
  name:'Comfirm',
  data(){
    return{
      registrationId:'',
      medicineDiseIdList:[],
      dialogTableVisible:false,
      total:0,
      record:[],
      records:[],
      disQuery: {
        catId: '',
        code: '',
        name: '',
        icd: '',
        status: '',
        pageSize: 8,
        pageNum: 1
      },
      disList:[],
      prerecord:{
        chiefComplaint:'',
        historyOfPresentIllness:'',
        historyOfTreatment:'',
        pastHistory:'',
        allergies:'',
        healthCheckup:'',
        checkResult:'',
        testResult:'',
        priliminaryDiseIdList:'',
        testAdvice:''
      },
      activeName:'first',
      isclose:true,
      mainwidth:"60%",
      activeNames: ['1'],
    };
  },
  created(){
    this.getmedicineDiseIdList()
    this.registrationId=this.patient.registrationId

  /*  this.Clinical();*/
  },
  watch:{
    'patient' : function(newVal, oldVal){
      this.patient = newVal
      /*this.getnonend()*/
      this.Clinical();
    },
  },
  methods:{
    getmedicineDiseIdList(){
      selectByType({staffId:this.$store.getters.id,selectType:2}).then(res=>{
        this.medicineDiseIdList = res.data.medicineDiseList
      })
    },
    submitdefinite(){
      this.prerecord.definiteDiseStrList = ''
      this.record.forEach(item=>{
        this.prerecord.definiteDiseStrList += (item.id+',')
      })
      this.prerecord.startDate = parseTime(this.prerecord.startDate,'{y}-{m}-{d}')
      this.prerecord.createTime = parseTime(this.prerecord.createTime,'{y}-{m}-{d}')
      this.prerecord.definiteDiseStrList = this.prerecord.definiteDiseStrList.substr(0, this.prerecord.definiteDiseStrList.length - 1);
      submitdefinite(this.prerecord).then(res=>{
          this.$notify({
          title: '成功',
          message: '已确诊!',
          type: 'success',
          duration: 2000
        })
        this.$emit('comfirmdms',2)

      })
    },
    deleteDis(row){
      this.record=this.record.filter(item=>{
        if(item.id===row.id)
          return false
        return true
      })

    },
    selectDis(val){
      this.$confirm('是否添加 '+val.name+' 到该患者?', '添加诊断', {
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
    },
    addDis(){
      this.dialogTableVisible=true
      this.getDis()
    },
    async getDis(){
      const res = await getDmsDislist(this.disQuery)
      this.disList = res.data.list
      this.total = res.data.total
    },
    getnonend(){
      getnonend(this.patient.registrationId).then(res=>{

        this.prerecord = res.data.dmsCaseHistoryList[0]

        parseList(this.prerecord.priliminaryDiseIdList).then(res=>{
          this.record = res.data

        })
      })
    },
    Clinical(){
      selectEndCaseHistory(this.patient.registrationId,3).then(res=>{
        this.prerecord.chiefComplaint=res.data.dmsCaseHistoryList[0].chiefComplaint
        this.prerecord.historyOfPresentIllness=res.data.dmsCaseHistoryList[0].historyOfPresentIllness
        this.prerecord.historyOfTreatment=res.data.dmsCaseHistoryList[0].historyOfTreatment
        this.prerecord.pastHistory=res.data.dmsCaseHistoryList[0].pastHistory
        this.prerecord.allergies=res.data.dmsCaseHistoryList[0].allergies
        this.prerecord.healthCheckup=res.data.dmsCaseHistoryList[0].healthCheckup
        this.prerecord.checkResult=res.data.dmsCaseHistoryList[0].checkResult
        this.prerecord.testResult=res.data.dmsCaseHistoryList[0].testResult
        this.prerecord.definiteDiseStrList=res.data.dmsCaseHistoryList[0].definiteDiseStrList
        this.prerecord.testAdvice=res.data.dmsCaseHistoryList[0].testAdvice
        parseList(this.prerecord.definiteDiseStrList).then(pars=>{
          this.records=pars.data
        })
      })
    },
    controlfast(){
      this.isclose=!this.isclose
      if(this.mainwidth==="60%")
        this.mainwidth="80%"
      else
        this.mainwidth="60%"
    },
    print(e){
      const subOutputRankPrint = document.getElementById('historyComfirm1')
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
