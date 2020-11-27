<template>
  <!-- 门诊医生工作台 -->
  <div class="div1">
    <el-container>
      <!-- 左侧患者列表 -->
      <transition name="el-zoom-in-left">
        <el-aside width="350px" v-show="isaside" style="padding:0">
          <div>
            <el-card style="width:350px">
              <span style="font-family:'微软雅黑'">选择患者:</span>
              <el-input v-model="search" placeholder="患者名" style="width: 180px;" class="filter-item input1" />
              <el-button style="margin-left:10px" type="primary"  circle icon="el-icon-refresh" @click="getPatientList"></el-button>
              <el-tabs type="card" v-model="activeName" style="margin-top:15px">
                <el-tab-pane  label="本人" name="first">
                  <el-tag style="width:100%">待诊患者</el-tag>
                  <el-table @row-click="handleCurrentChange" highlight-current-row stripe :data="personalWaitList.filter(data => !search || data.patientName.toLowerCase().includes(search.toLowerCase())||data.patientMedicalRecordNo.toLowerCase().includes(search.toLowerCase()))" height="255">
                    <el-table-column align="center" label="看诊时间" width="150px">
                      <template slot-scope="scope">
                        {{scope.row.attendanceDate}}
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="名字" width="90px">
                      <template slot-scope="scope">
                        {{scope.row.patientName}}
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="年龄" width="65px">
                      <template slot-scope="scope">
                        {{scope.row.patientAge}}
                      </template>
                    </el-table-column>
                  </el-table>
                  <el-tag type="success" style="width:100%">诊中患者</el-tag>
                  <el-table @row-click="continuing" highlight-current-row stripe :data="personalDuringList.filter(data => !search || data.patientName.toLowerCase().includes(search.toLowerCase())||data.patientMedicalRecordNo.toLowerCase().includes(search.toLowerCase()))" height="255">
                    <el-table-column align="center" label="看诊时间" width="150px">
                      <template slot-scope="scope">
                        {{scope.row.attendanceDate}}
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="名字" width="90px">
                      <template slot-scope="scope">
                        {{scope.row.patientName}}
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="年龄" width="65px">
                      <template slot-scope="scope">
                        {{scope.row.patientAge}}
                      </template>
                    </el-table-column>
                  </el-table>
                  <el-tag type="warning" style="width:100%">已诊患者</el-tag>
                  <el-table @row-click="continuingEnd" stripe :data="personalEndList.filter(data => !search || data.patientName.toLowerCase().includes(search.toLowerCase())||data.patientMedicalRecordNo.toLowerCase().includes(search.toLowerCase()))" height="255">
                    <el-table-column align="center" label="看诊时间" width="150px">
                      <template slot-scope="scope">
                        {{scope.row.attendanceDate}}
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="名字" width="90px">
                      <template slot-scope="scope">
                        {{scope.row.patientName}}
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="年龄" width="65px">
                      <template slot-scope="scope">
                        {{scope.row.patientAge}}
                      </template>
                    </el-table-column>
                  </el-table>
                </el-tab-pane>
                <el-tab-pane label="科室" name="second">
                  <el-tag style="width:100%">待诊患者</el-tag>
                  <el-table v-loading="loaddepp" @row-click="bindPatient" highlight-current-row stripe :data="deptWaitList.filter(data => !search || data.patientName.toLowerCase().includes(search.toLowerCase())||data.patientMedicalRecordNo.toLowerCase().includes(search.toLowerCase()))" height="820">
                    <el-table-column align="center" label="病历号" width="150px">
                      <template slot-scope="scope">
                        {{scope.row.patientMedicalRecordNo}}
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="名字" width="90px">
                      <template slot-scope="scope">
                        {{scope.row.patientName}}
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="年龄" width="65px">
                      <template slot-scope="scope">
                        {{scope.row.patientAge}}
                      </template>
                    </el-table-column>
                  </el-table>
                </el-tab-pane>
              </el-tabs>
            </el-card>
          </div>
        </el-aside>
      </transition>
      <!-- 医生工作台 -->
      <el-main>
        <div class='popContainer' v-if="mask"></div>
        <div style="border-style: dotted;border-width: 0px 0px 1px 0px;border-color:#C0C0C0;padding: 0 0 10px 0;margin-bottom:-10px">
          <el-button class="toggle-button" size="mini" circle type="primary" @click="showaside"><i v-show="isaside" class="el-icon-arrow-left" /><i v-show="!isaside" class="el-icon-arrow-right" /></el-button>
          <span style="margin-left:15px;font-size:14px;font-family: '微软雅黑';">当前病人：</span>
          <el-tag class="painfo" color="white" style="width:15%;font-size:15px">姓名: <span style="color:black;font-size: 14px;font-family:'微软雅黑';">{{patient.patientName}}</span></el-tag>
          <el-tag class="painfo" color="white" style="width:20%;font-size:15px">就诊号: <span style="color:black;font-size: 14px;font-family:'微软雅黑';">{{patient.patientMedicalRecordNo}}</span></el-tag>
          <el-tag class="painfo" color="white" style="width:10%;font-size:15px">性别: <span style="color:black;font-size: 14px;font-family:'微软雅黑';" v-if="patient.patientGender===1">女</span><span style="color:black" v-if="patient.patientGender===0">男</span></el-tag>
          <el-tag class="painfo" color="white" style="width:10%;font-size:15px">年龄: <span style="color:black;font-size: 14px;font-family:'微软雅黑';">{{patient.patientAge}}</span></el-tag>
          <el-button type="primary" style="margin-left:30px" @click="endDiagnosis" v-show="confirmed">诊毕</el-button>  <!--<i class="el-icon-success" />-->
          <el-button type="primary" style="margin-left:30px" @click="prints">打印</el-button>
        </div>
        <div style="margin-top=-30px" v-show="flag">
          <el-tabs v-model="activeName2" type="card" style="margin-top:20px">
           <!-- <el-tab-pane label="病历首页" :disabled="!firstdisabled" name="first"><Record @priliminary="priliminary" :activeName="activeName2" ref="record" v-bind:patient="patient"></Record></el-tab-pane>
            <el-tab-pane label="检查申请" :disabled="firstdisabled" name="second"><Inspection ref="inspection" :activeName="activeName2" v-bind:patient="patient"></Inspection></el-tab-pane>
            <el-tab-pane label="检验申请" :disabled="firstdisabled" name="third"><Examine ref="examine" :activeName="activeName2" v-bind:patient="patient"></Examine></el-tab-pane>
            <el-tab-pane label="门诊确诊" :disabled="firstdisabled||!comfirmdisabled" name="fourth"><Comfirm @comfirmdms="comfirmdms" ref="comfirm" :activeName="activeName2" v-bind:patient="patient" ></Comfirm></el-tab-pane>
            <el-tab-pane label="成药处方" :disabled="firstdisabled||comfirmdisabled" name="fiveth"><Prescription ref="prescription" :activeName="activeName2" v-bind:patient="patient"></Prescription></el-tab-pane>
            <el-tab-pane label="草药处方" :disabled="firstdisabled||comfirmdisabled" name="eightth"><Cprescription ref="cprescription" :activeName="activeName2" v-bind:patient="patient"></Cprescription></el-tab-pane>
            <el-tab-pane label="处置申请" :disabled="firstdisabled||comfirmdisabled" name="sixth"><Handle ref="cprescription" :activeName="activeName2" v-bind:patient="patient"></Handle></el-tab-pane>
            <el-tab-pane label="患者账单" :disabled="firstdisabled" name="seventh"><Bill ref="bill" :activeName="activeName2" v-bind:patient="patient"></Bill></el-tab-pane>
            <el-tab-pane label="知情告知"  name="noticeInform"><noticeInform ref="noticeInform" :activeName="activeName2" v-bind:patient="patient"></noticeInform></el-tab-pane>-->

            <el-tab-pane label="病历首页"  name="first"><Record @priliminary="priliminary" :activeName="activeName2" ref="Record" v-bind:patient="patient"></Record></el-tab-pane>
            <el-tab-pane label="检查申请"  name="second"><Inspection ref="Inspection" :activeName="activeName2" v-bind:patient="patient"></Inspection></el-tab-pane>
            <el-tab-pane label="检验申请"  name="third"><Examine ref="Examine" :activeName="activeName2" v-bind:patient="patient"></Examine></el-tab-pane>
            <el-tab-pane label="门诊确诊"  name="fourth"><Comfirm @comfirmdms="comfirmdms" ref="Comfirm" :activeName="activeName2" v-bind:patient="patient" ></Comfirm></el-tab-pane>
            <el-tab-pane label="成药处方"  name="fiveth"><Prescription ref="Prescription" :activeName="activeName2" v-bind:patient="patient"></Prescription></el-tab-pane>
            <el-tab-pane label="草药处方"  name="eightth"><Cprescription ref="Cprescription" :activeName="activeName2" v-bind:patient="patient"></Cprescription></el-tab-pane>
            <el-tab-pane label="处置申请"  name="sixth"><Handle ref="Handle" :activeName="activeName2" v-bind:patient="patient"></Handle></el-tab-pane>
            <el-tab-pane label="患者账单"  name="seventh"><Bill ref="Bill" :activeName="activeName2" v-bind:patient="patient"></Bill></el-tab-pane>
            <el-tab-pane label="知情告知"  name="noticeInform"><noticeInform ref="noticeInform" :activeName="activeName2" v-bind:patient="patient"></noticeInform></el-tab-pane>
          </el-tabs>
        </div>
        <div style="margin-top=-30px" v-show="!flag">
          <el-tabs v-model="activeName2" type="card" style="margin-top:20px">
            <el-tab-pane label="病历首页"  name="first"><historyRecord @priliminary="priliminary" ref="record" v-bind:patient="patient"></historyRecord></el-tab-pane>
            <el-tab-pane label="检查申请"  name="second"><historyInspection ref="inspection" v-bind:patient="patient"></historyInspection></el-tab-pane>
            <el-tab-pane label="检验申请"  name="third"><historyExamine ref="examine" v-bind:patient="patient"></historyExamine></el-tab-pane>
            <el-tab-pane label="门诊确诊"  name="fourth"><historyComfirm @comfirmdms="comfirmdms" ref="comfirm" v-bind:patient="patient" ></historyComfirm></el-tab-pane>
            <el-tab-pane label="成药处方"  name="fiveth"><historyPrescription ref="prescription" v-bind:patient="patient"></historyPrescription></el-tab-pane>
            <el-tab-pane label="草药处方"  name="eightth"><historyCprescription ref="cprescription" v-bind:patient="patient"></historyCprescription></el-tab-pane>
            <el-tab-pane label="处置申请"  name="sixth"><historyHandle ref="cprescription" v-bind:patient="patient"></historyHandle></el-tab-pane>
            <el-tab-pane label="患者账单"  name="seventh"><historyBill ref="bill" v-bind:patient="patient"></historyBill></el-tab-pane>
            <el-tab-pane label="知情告知"  name="historyNoticeInform"><historyNoticeInform ref="historyNoticeInform" v-bind:patient="patient"></historyNoticeInform></el-tab-pane>
          </el-tabs>
        </div>
      </el-main>

      <el-dialog :visible.sync="print" title="病例信息">
        <medicalRecords ref="medicalRecords" :print="print" :activeName="medicalRecords" v-bind:patient="patient"></medicalRecords>
      </el-dialog>
    </el-container>
  </div>
</template>

<script>
  import Record from './workstation/record'
  import Inspection from './workstation/inspection'
  import Examine from './workstation/examine'
  import Prescription from './workstation/prescription'
  import Handle from './workstation/handle'
  import Comfirm from './workstation/confirm'
  import Cprescription from './workstation/Cprescription'
  import Bill from './workstation/bill'
  import noticeInform from './workstation/noticeInform'

  import historyRecord from './workstation/historyRecord'
  import historyInspection from './workstation/historyInspection'
  import historyExamine from './workstation/historyExamine'
  import historyPrescription from './workstation/historyPrescription'
  import historyHandle from './workstation/historyHandle'
  import historyComfirm from './workstation/historyComfirm'
  import historyCprescription from './workstation/historyCprescription'
  import historyBill from './workstation/historyBill'
  import historyNoticeInform from './workstation/historyNoticeInform'

  import medicalRecords from '@/views/historyRecords/workstation/medicalRecords'





  import {getPatientList,bindPatient,startDiagnosis} from '@/api/outpatient/patient'
  import {endDiagnosis,getnonend,endgetnonend,Clinical,selectEndCaseHistory} from '@/api/outpatient/dmscase'
  import { parseTime,deepClone } from '@/utils'
  import { truncate } from 'fs';
  export default{
    components: {Record,Inspection,Examine,Prescription,Handle,Comfirm,Cprescription,Bill,
      historyRecord,historyInspection,historyExamine,historyPrescription,historyHandle,historyComfirm,
      historyCprescription,historyBill,noticeInform,historyNoticeInform,medicalRecords},
    data(){
      return{
        medicalRecords:'medicalRecords',
        confirmed:true,
        comfirmdisabled:true,
        firstdisabled:false,
        mask:true,
        loaddepp:true,
        patient: {},
        deptWaitList:[],
        personalDuringList:[],
        personalEndList:[],
        personalWaitList:[],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        mouldname:'',
        dialog1: false,
        dialog2: false,
        search:'',
        isaside: true,
        activeName: 'first',
        activeName2: 'first',
        flag:true,
        print:false
      };
    },
    created(){
      this.getPatientList()
    },
    methods: {
      comfirmdms(val){
        if(val==2){
          this.endcomfirmdms()
        }else {
          getnonend(this.patient.registrationId).then(res=>{
            this.comfirmdisabled = true
            if(res.data.dmsCaseHistoryList.length!==0){
              this.activeName2 = 'second'
              if(res.data.dmsCaseHistoryList[0].status===1){
                this.comfirmdisabled = false
                this.activeName2 = 'fiveth'
              }
              this.firstdisabled = false
            }
            else{
              this.firstdisabled = true
              this.activeName2 = 'first'
            }
            this.patient = val
            this.$refs.record.controlfast()
          })
        }
      },
      //确诊之后调用此方法，获得已确诊信息
      endcomfirmdms(){
        endgetnonend(this.patient.registrationId).then(res=>{
          this.comfirmdisabled = true
          if(res.data.dmsCaseHistoryList.length!==0){
            this.activeName2 = 'second'
            if(res.data.dmsCaseHistoryList[0].status===2){
              this.comfirmdisabled = false
              this.activeName2 = 'fiveth'
            }
            this.firstdisabled = false
          }
          else{
            this.firstdisabled = true
            this.activeName2 = 'first'
          }
          this.patient = val
          this.$refs.record.controlfast()
        })
      },

      priliminary(){
        getnonend(this.patient.registrationId).then(res=>{
          this.comfirmdisabled = true
          if(res.data.dmsCaseHistoryList.length!==0){
            if(res.data.dmsCaseHistoryList[0].status===2){
              this.comfirmdisabled = false
              this.activeName2 = 'fiveth'
            }
            this.firstdisabled = false
            this.activeName2 = 'second'
          }
          else{
            this.firstdisabled = true
            this.activeName2 = 'first'
          }
          this.patient = val
          this.$refs.record.controlfast()
        })
      },
      endDiagnosis(){
        endDiagnosis(this.patient.registrationId).then(res=>{
          if(res.data===1){
            this.$notify({
              title: '成功',
              message: '已诊毕！',
              type: 'success',
              duration: 2000
            })
          }else {
            this.$notify({
              title: '失败',
              message: '诊毕失败！',
              type: 'warning',
              duration: 2000
            })
          }
          this.showaside()
          this.getPatientList()
        })
      },
      continuing(val){
        this.flag = true
        this.confirmed=true
        this.$confirm('确认继续诊断患者 '+val.patientName+'?', '就诊', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(()=>{
          Clinical(val.registrationId).then(res=>{
            this.comfirmdisabled = true
            if(res.data.dmsCaseHistoryList.length!==0){
              this.activeName2 = 'second'
              if(res.data.dmsCaseHistoryList[0].status===2){
                this.comfirmdisabled = false
                this.activeName2 = 'fiveth'
              }
              this.firstdisabled = false

            }
            else{
              this.firstdisabled = true
              this.activeName2 = 'first'
            }
            this.patient = val
            this.showaside()
            this.$refs.record.controlfast()
          })
        })
      },
      continuingEnd(val){
        this.confirmed=false
        this.$confirm('确认查看患者 '+val.patientName+'的就诊信息?', '信息', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(()=>{
          selectEndCaseHistory(val.registrationId).then(res=>{
            this.flag = false
            this.patient = val
            this.showaside()
            this.$refs.record.controlfast()
          })
        })
      },

      bindPatient(val){
        this.$confirm('确认绑定患者 '+val.patientName+'?', '就诊', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(()=>{
          bindPatient(val.registrationId,this.$store.getters.id).then(res=>{
            this.getPatientList()
            this.$notify({
              title: '成功',
              message: '成功绑定该患者!',
              type: 'success',
              duration: 2000
            })
          })
        })
      },
      async handleCurrentChange(val){
        this.flag = true
        this.confirmed=true
        this.$confirm('确认开始诊断患者 '+val.patientName+'?', '就诊', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(()=>{
          startDiagnosis(val.registrationId).then(res=>{
            this.$notify({
              title: '成功',
              message: '开始诊断',
              type: 'success',
              duration: 2000
            })
            this.patient = val
            this.showaside()
            this.$refs.record.controlfast()
          })

        })

      },
      getPatientList: async function () {
        this.loaddepp = true
        await getPatientList(this.$store.getters.id).then(res => {
          this.deptWaitList = res.data.deptWaitList
          this.deptWaitList.forEach(item => {
            item.attendanceDate = parseTime(item.attendanceDate).toString().substring(0,16)
          })
          this.personalDuringList = res.data.personalDuringList
          this.personalDuringList.forEach(item => {
            item.attendanceDate = parseTime(item.attendanceDate).toString().substring(0,16)
          })
          this.personalEndList = res.data.personalEndList
          this.personalEndList.forEach(item => {
            item.attendanceDate = parseTime(item.attendanceDate).toString().substring(0,16)
          })
          this.personalWaitList = res.data.personalWaitList
          this.personalWaitList.forEach(item => {
            item.attendanceDate = parseTime(item.attendanceDate).toString().substring(0,16)
          })
          this.loaddepp = false
        }).catch(err => {
          this.loaddepp = false
        })
      },
      showaside(){
        this.getPatientList()
        if(this.isaside===false){
          this.patient = {}
          this.deptWaitList = []
          this.personalDuringList = []
          this.personalEndList = []
          this.personalWaitList = []
          this.isaside=true;
          this.mask = true
        }
        else{
          this.isaside = false
          this.mask = false
        }
      },
      storeRecord(){
        this.dialog1=true;
      },
      prints(){
        this.print=!this.print
      },
    },
    watch: {
      activeName2(newV, oldV) {
        console.log(newV+":"+oldV)
        if(oldV==="first"){
          this.$refs.Record.submitPriliminaryDise2()
        }else if(oldV==="fourth"){
          console.log("确诊")
          this.$refs.Comfirm.submitdefinite2()
        }
      }
    }
  }
</script>
<style>
  /* .toggle-button {
    position: relative;
    z-index: 199;
  } */
  div.popContainer{
    position: absolute;
    top: 0;
    left: 350px;
    right: 0;
    bottom: 0;
    z-index: 100;
    background: rgba(0,0,0,0.3);
  }

  .div1{
    font-family:  "微软雅黑";
  }
  .card1{
    font-size: 14px;
    font-family: "微软雅黑";
    line-height: 20px;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }
  .input1{
    width:200px;
  }
  .box-card {
    width: 480px;
  }
  ::-webkit-scrollbar {
    width: 8px;
    height: 8px;
    background-color: #fff;
  }
  ::-webkit-scrollbar-thumb {
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, .3);
    background-color:#F0F8FF
  }
</style>


