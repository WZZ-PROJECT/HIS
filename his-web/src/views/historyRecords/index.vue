<template>
  <!-- 门诊医生工作台 -->
  <div class="div1">
    <el-container>
      <!-- 左侧患者列表 -->
      <transition name="el-zoom-in-left">
      <el-aside width="350px" v-show="isaside" style="padding:0">
        <div>
          <el-card style="width:350px">
            <!--<span style="font-family:'微软雅黑'">选择患者:</span>
            <el-input v-model="search" placeholder="患者名" style="width: 180px;" class="filter-item input1" />-->

            <el-form label-width="100px">
              <el-form-item label="开始日期">
                <el-date-picker
                  v-model="startDate"
                  placeholder="选择开始日期"
                  type="date">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="结束日期">
                <el-date-picker
                  v-model="endDate"
                  placeholder="选择结束日期"
                  type="date">
                </el-date-picker>
              </el-form-item>

              <el-form-item style="width:300px" label="科室" prop="deptId">
                <el-select placeholder="科室" clearable  v-model="conditions.deptId" style="width:220px">
                  <el-option v-for="item in deptIdList" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>

              <el-form-item label="患者姓名">
                <el-input v-model="conditions.name" placeholder="患者姓名" style="width: 220px;" class="filter-item input1" />
              </el-form-item>
              <el-form-item label="身份证号">
                <el-input v-model="conditions.cardId" placeholder="患者身份证号" style="width: 220px;" class="filter-item input1" />
              </el-form-item>
              <div style="margin-left:81%"><el-button type="primary" @click="queryPeople">查询</el-button></div>

            </el-form>
            <!--<el-button style="margin-left:10px" type="primary"  circle icon="el-icon-refresh" @click="getPatientList"></el-button>-->
            <el-tabs type="card" v-model="activeName" style="margin-top:15px">
              <el-tag type="warning" style="width:100%">已诊患者</el-tag>
              <el-table @row-click="continuingEnd" stripe :data="personalEndList.filter(data => !search || data.patientName.toLowerCase().includes(search.toLowerCase())||data.patientMedicalRecordNo.toLowerCase().includes(search.toLowerCase()))" height="255">
                <el-table-column align="center" label="身份证号" width="160px">
                  <template slot-scope="scope">
                    {{scope.row.identificationNo}}
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
            </el-tabs>
          </el-card>
        </div>
      </el-aside>
    </transition>
      <!-- 医生工作台 -->
      <el-main>
        <div class='popContainer' v-if="mask"></div>
        <div style="border-style: dotted;border-width: 0px 0px 1px 0px;border-color:#C0C0C0;padding: 0 0 10px 0;margin-bottom:-10px">
          <el-button size="mini" circle type="primary" @click="showaside"><i v-show="isaside" class="el-icon-arrow-left" /><i v-show="!isaside" class="el-icon-arrow-right" /></el-button>
          <span style="margin-left:15px;font-size:14px;font-family: '微软雅黑';">当前病人：</span>
          <el-tag class="painfo" color="white" style="width:15%;font-size:15px">姓名: <span style="color:black;font-size: 14px;font-family:'微软雅黑';">{{patient.patientName}}</span></el-tag>
          <el-tag class="painfo" color="white" style="width:20%;font-size:15px">就诊号: <span style="color:black;font-size: 14px;font-family:'微软雅黑';">{{patient.patientMedicalRecordNo}}</span></el-tag>
          <el-tag class="painfo" color="white" style="width:10%;font-size:15px">性别: <span style="color:black;font-size: 14px;font-family:'微软雅黑';" v-if="patient.patientGender===1">女</span><span style="color:black" v-if="patient.patientGender===0">男</span></el-tag>
          <el-tag class="painfo" color="white" style="width:10%;font-size:15px">年龄: <span style="color:black;font-size: 14px;font-family:'微软雅黑';">{{patient.patientAge}}</span></el-tag>
          <el-button type="primary" style="margin-left:30px" @click="prints">打印</el-button>
        </div>
        <div style="margin-top=-30px" v-show="!falg">
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
            <!--<el-tab-pane label="病历"  name="medicalRecords"><medicalRecords ref="medicalRecords" v-bind:patient="patient"></medicalRecords></el-tab-pane>-->
          </el-tabs>
        </div>
      </el-main>

      <el-dialog :visible.sync="print" title="病例信息">
        <medicalRecords ref="medicalRecords" :print="print"  :activeName="medicalRecords" v-bind:patient="patient"></medicalRecords>
      </el-dialog>
    </el-container>
  </div>
</template>

<script>
  import historyRecord from './workstation/historyRecord'
  import historyInspection from './workstation/historyInspection'
  import historyExamine from './workstation/historyExamine'
  import historyPrescription from './workstation/historyPrescription'
  import historyHandle from './workstation/historyHandle'
  import historyComfirm from './workstation/historyComfirm'
  import historyCprescription from './workstation/historyCprescription'
  import historyBill from './workstation/historyBill'
  import noticeInform from './workstation/noticeInform'
  import medicalRecords from './workstation/medicalRecords'
  import historyNoticeInform from './workstation/historyNoticeInform'

  import { getAllDep } from "@/api/department";
  import {getPatientList,bindPatient,startDiagnosis,queryPeople} from '@/api/outpatient/patient'
  import {endDiagnosis,getnonend,endgetnonend,Clinical} from '@/api/outpatient/dmscase'
  import { deepClone } from '@/utils'
  import { truncate } from 'fs';
  import {parseTime} from "../../utils";
  export default{
    components: {historyRecord,historyInspection,historyExamine,historyPrescription,historyHandle,
      historyComfirm,historyCprescription,historyBill,noticeInform,medicalRecords,historyNoticeInform},
    data(){
      return{
        confirmed:true,
        comfirmdisabled:true,
        firstdisabled:true,
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
        falg:true,
        startEndDate:[],
        deptIdList:[],
        startDate: '',
        endDate: '',
        conditions:{
          startDate:'',
          endDate:'',
          name:'',
          cardId:'',
          deptId:''
        },
        print:false
      };
    },
    created(){
      /*this.getPatientList()*/
      this.getAllDep()
      this.queryPeople();
    },
    methods: {
      endDiagnosis,
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
      continuingEnd(val){
        this.confirmed=false
        this.$confirm('确认查看患者 '+val.patientName+'的就诊信息?', '信息', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(()=>{
          Clinical(val.registrationId).then(res=>{
            this.falg = false
            this.patient = val
            this.showaside()
            this.$refs.record.controlfast()
          })
        })
      },

      getAllDep(){
        getAllDep().then(res=>{
          this.deptIdList = res.data
        })
      },

      async queryPeople(){
        this.loaddepp = true

        this.conditions.startDate=parseTime(this.startDate)
        this.conditions.endDate=parseTime(this.endDate)

        await queryPeople(this.conditions).then(res=>{
          this.deptWaitList = res.data.deptWaitList
          this.personalDuringList = res.data.personalDuringList
          this.personalEndList = res.data.personalEndList
          this.personalWaitList = res.data.personalWaitList
          this.loaddepp = false
        }).catch(err=>{
          this.loaddepp = false
        })
      },
      showaside(){
        this.queryPeople()
        if(this.isaside===false){
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
    }
  }
</script>
<style>
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


