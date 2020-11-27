<template>
  <div>
    <!-- 病历首页 -->
    <el-container>
      <!--<div :width="mainwidth">
        <el-button type="primary" @click="print">打印</el-button>
      </div>-->
      <el-aside id="record" :width="mainwidth" style="background:white;" v-show="!this.patientfalg">
        <el-tag style="margin-bottom:20px">病史内容:</el-tag>
        <el-form :model="record" label-width="100px">
          <el-form-item label="主诉"><el-input disabled v-model="priliminaryDise.chiefComplaint" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="主述" style="width:80%"></el-input></el-form-item>
          <el-form-item label="现病史"><el-input disabled v-model="priliminaryDise.historyOfPresentIllness" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="现病史" style="width:80%" ></el-input></el-form-item>
          <el-form-item label="现病治疗情况"><el-input disabled v-model="priliminaryDise.historyOfTreatment" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="现病治疗情况" style="width:80%"></el-input></el-form-item>
          <el-form-item label="既往史"><el-input disabled v-model="priliminaryDise.pastHistory" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="既往史" style="width:80%"></el-input></el-form-item>
          <el-form-item label="过敏史"><el-input disabled v-model="priliminaryDise.allergies" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="过敏史" style="width:80%"></el-input></el-form-item>
          <el-form-item label="体格检查"><el-input disabled v-model="priliminaryDise.healthCheckup" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="体格检查" style="width:80%"></el-input></el-form-item>
          <el-form-item label="发病时间">
            <el-date-picker disabled
              v-model="priliminaryDise.startDate"
              align="right"
              type="date"
              placeholder="选择日期"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
        </el-form>
        <el-tag>评估诊断:</el-tag>
        <el-card style="width:85%">
          <el-table :data="record">
            <el-table-column label="ICD编码" prop="icd"></el-table-column>
            <el-table-column label="名称" prop="name"></el-table-column>
            <el-table-column label="编码" prop="code" ></el-table-column>
          </el-table>
        </el-card>
      </el-aside>

    </el-container>
  </div>
</template>
<script>
  import {getDmsDislist,parseList} from '@/api/diagnosis'
  import {submitPriliminaryDise,selectEndCaseHistory} from '@/api/outpatient/dmscase'
  import {getAllStaffModel} from '@/api/outpatient/dmscasemodel'
  import Pagination from '@/components/Pagination'
  import {selectByType,addfre,delfre} from '@/api/outpatient/frequentuse'
  import {parseTime,deepClone} from '@/utils'
  import {saveCasePage,getCasePage} from '@/api/outpatient/save'
  export default {
    props:['patient'],
    name:'Record',
    components: {Pagination},
    data(){
      return{
        history:[],
        historyitem:{},
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
          shortcuts: [{
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date());
            }
          }, {
            text: '昨天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          }, {
            text: '一周前',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
        },
        dialogTableVisible:false,
        activeName:'first',
        isclose:false,
        record:[],
        medicineDiseIdList:[],//常用诊断
        priliminaryDise:{
          chiefComplaint:'',//主述
          historyOfPresentIllness:'',//现病史
          historyOfTreatment:'',//现治疗情况
          pastHistory:'',//既往史
          allergies:'',//过敏史
          healthCheckup:'',//体格检查
          registrationId:'',//
          priliminaryDiseStrList:'',
          priliminaryDiseIdList:'',
          startDate:'',
          name:'',
          gender:'',
          ageStr:''
        },
        mainwidth:"80%",
        activeNames: ['1'],
        total:0,
        disQuery: {
          catId: '',
          code: '',
          name: '',
          icd: '',
          status: '',
          pageSize: 8,
          pageNum: 1
        },
        models:[],
        model:{},
        disList:[]
      };
    },
    created(){
      this.getmedicineDiseIdList()
      this.getAllStaffModel()
    },
    watch:{
      'patient' : function(newVal, oldVal){
        this.patient = newVal
        this.selectEndCaseHistoryByReg()
        this.getCasePage()
      },
    },
    methods:{
      addmodel(val){
        this.$confirm('是否加载病历模板 '+val.name+' ?', '加载病历模板', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(()=>{
          this.priliminaryDise = deepClone(val)
          let diss = ''
          this.priliminaryDise.priliminaryDiseIdList.forEach(item=>{
            diss += (item+',')
          })
          diss = diss.substr(0,diss.length-1)
          parseList(diss).then(res=>{
            this.record = res.data
          })
        })
      },
      selectmodel(val){
        this.model = val
      },
      getAllStaffModel(){
        getAllStaffModel(this.$store.getters.id).then(res=>{
          this.models = res.data.staffList
          this.models.forEach(item=>{
            item.dis = ''
            item.priliminaryDiseStrList.forEach(dis=>{
              item.dis+=(dis+',')
            })
            item.dis = item.dis.substr(0,item.dis.length-1)
          })
        })
      },
      saveCasePage(){
        let data  =this.priliminaryDise
        data.registrationId = this.patient.registrationId
        saveCasePage(this.priliminaryDise).then(res=>{
          this.$notify({
            title: '成功',
            message: '已暂存病历首页',
            type: 'success',
            duration: 2000
          })
        })
      },
      getCasePage(){
        getCasePage(this.patient.registrationId).then(res=>{
          if(res.data!==null){
            this.priliminaryDise = res.data
            this.$notify({
              title: '成功',
              message: '已加载暂存病历首页',
              type: 'success',
              duration: 2000
            })
          }
        })
      },
      selecthistory(val){
        this.historyitem = val
      },
      selectEndCaseHistoryByReg(){
        selectEndCaseHistory(this.patient.registrationId,3).then(res=>{
          this.history = res.data.dmsCaseHistoryList
          this.priliminaryDise.chiefComplaint=this.history[0].chiefComplaint
          this.priliminaryDise.historyOfPresentIllness=this.history[0].historyOfPresentIllness
          this.priliminaryDise.historyOfTreatment =this.history[0].historyOfTreatment
          this.priliminaryDise.pastHistory=this.history[0].pastHistory
          this.priliminaryDise.allergies=this.history[0].allergies
          this.priliminaryDise.healthCheckup=this.history[0].healthCheckup
          this.priliminaryDise.startDate=this.history[0].startDate

          this.priliminaryDise.priliminaryDiseIdList=this.history[0].priliminaryDiseIdList
          parseList(this.priliminaryDise.priliminaryDiseIdList).then(res=>{
            this.record=res.data
          })
          this.history.forEach(item=>{
            item.createTime = parseTime(item.createTime)
            item.startDate = parseTime(item.startDate)
          })
        })
      },
      getmedicineDiseIdList(){
        selectByType({staffId:this.$store.getters.id,selectType:2}).then(res=>{
          this.medicineDiseIdList = res.data.medicineDiseList
        })
      },
      submitPriliminaryDise(){
        this.priliminaryDise.registrationId = this.patient.registrationId
        this.record.forEach(item=>{
          this.priliminaryDise.priliminaryDiseStrList+=(item.name+',')
          this.priliminaryDise.priliminaryDiseIdList+=(item.id+',')
        })
        this.priliminaryDise.priliminaryDiseStrList = this.priliminaryDise.priliminaryDiseStrList.substr(0, this.priliminaryDise.priliminaryDiseStrList.length - 1);
        this.priliminaryDise.priliminaryDiseIdList = this.priliminaryDise.priliminaryDiseIdList.substr(0, this.priliminaryDise.priliminaryDiseIdList.length - 1);
        this.priliminaryDise.name = this.patient.patientName
        this.priliminaryDise.gender = this.patient.patientGender
        this.priliminaryDise.startDate = parseTime(this.priliminaryDise.startDate).substr(0,10)
        this.priliminaryDise.ageStr = this.patient.patientAge
        submitPriliminaryDise(this.priliminaryDise).then(res=>{
          this.$notify({
            title: '成功',
            message: '成功提交初诊病历',
            type: 'success',
            duration: 2000
          })
          this.$emit('priliminary')
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
          let flag = 1
          if(flag)
            this.record.push(val)
          else
            this.$notify({
              title: '提示',
              message: '已存在该项目',
              type: 'warning',
              duration: 2000
            })
          this.dialogTableVisible = false
        })
      },
      async getDis(){
        const res = await getDmsDislist(this.disQuery)
        this.disList = res.data.list
        this.total = res.data.total
      },
      loadpatient(){
      },
      addDis(){
        this.dialogTableVisible=true
        this.getDis()
      },
      controlfast(){
        this.isclose=!this.isclose
        if(this.mainwidth==="60%")
          this.mainwidth="80%"
        else
          this.mainwidth="60%"
      },
      print(e){
        const subOutputRankPrint = document.getElementById('record')
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
<style>
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
</style>
