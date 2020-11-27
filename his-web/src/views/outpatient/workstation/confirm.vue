<template>
  <div>
  <!-- 确诊 -->
  <el-container>
  <el-aside id="confirm1" :width="mainwidth" style="background:white;">
    <el-tag style="margin-bottom:20px;margin-left:-20px" type="info">初诊内容:</el-tag>
    <!--<el-button type="primary" @click="print">打印</el-button>-->
    <el-button style="float:right" @click="controlfast"><i v-show="!isclose" class="el-icon-caret-right" /><i v-show="isclose" class="el-icon-caret-left" />  快捷操作</el-button>
    <el-form :model="prerecord"  ref="prerecord" style="color:black" :rules="rules">
      <el-form-item label="主诉" prop="chiefComplaint"><el-input  v-model="prerecord.chiefComplaint" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="主述" style="width:80%;float:right;"></el-input></el-form-item>
      <el-form-item label="现病史" prop="historyOfPresentIllness"><el-input  v-model="prerecord.historyOfPresentIllness" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="现病史"  style="width:80%;float:right;"></el-input></el-form-item>
      <el-form-item label="现病治疗情况" prop="historyOfTreatment"><el-input  v-model="prerecord.historyOfTreatment" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="现病治疗情况" style="width:80%;float:right"></el-input></el-form-item>
      <el-form-item label="既往史" prop="pastHistory"><el-input  v-model="prerecord.pastHistory" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="既往史" style="width:80%;float:right"></el-input></el-form-item>
      <el-form-item label="过敏史" prop="allergies"><el-input  v-model="prerecord.allergies" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="过敏史" style="width:80%;float:right"></el-input></el-form-item>
      <el-form-item label="体格检查" prop="healthCheckup"><el-input  v-model="prerecord.healthCheckup" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="体格检查" style="width:80%;float:right"></el-input></el-form-item>
    </el-form>
    <el-tag style="margin-bottom:20px;margin-left:-20px">检查检验结果:</el-tag>
    <el-form :model="prerecord" ref="prerecord" :rules="rules">
      <el-form-item label="检查结果" prop="checkResult"><el-input v-model="prerecord.checkResult" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="检查结果" style="width:40%"></el-input></el-form-item>
      <el-form-item label="检验结果" prop="testResult"><el-input v-model="prerecord.testResult" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="检验结果" style="width:40%"></el-input></el-form-item>
      <el-form-item label="医嘱" prop="testAdvice"><el-input v-model="prerecord.testAdvice" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" placeholder="医嘱" style="width:40%;margin-left:3%"></el-input></el-form-item>
    </el-form>
    <div style="margin-left:-20px;margin-bottom:30px">
      <el-tag>评估诊断:</el-tag>
      <el-card style="width:85%">
        <el-button type="text" style="float:right" @click="addDis">添加诊断</el-button>
        <el-table :data="record">
          <el-table-column width="80">
            <template slot-scope="scope">
              <el-button type="text" @click="deleteDis(scope.row)">删除</el-button>
            </template>
          </el-table-column>
          <el-table-column label="ICD编码" prop="icd"></el-table-column>
          <el-table-column label="名称" prop="name"></el-table-column>
          <el-table-column label="编码" prop="code" ></el-table-column>
        </el-table>
      </el-card>
    </div>
    <div>
      <el-button type="primary" style="float:right;margin-right:30px" @click="submitdefinite('prerecord')">提交</el-button>
    </div>
  </el-aside>
  <transition name="el-zoom-in-left">
    <el-main width="70%" v-show="isclose" style="border-style: dotted;border-width: 0px 0px 0px 1px;border-color:#C0C0C0;margin-top:-12px">
       <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="常用诊断" name="first">
          <el-table :data="medicineDiseIdList" @row-click="selectDis">
            <el-table-column label="ICD编码" prop="icd"></el-table-column>
            <el-table-column label="名称" prop="name"></el-table-column>
          </el-table>
        </el-tab-pane>
       </el-tabs>
    </el-main>
  </transition>
  </el-container>
  <el-dialog title="诊断目录" :visible.sync="dialogTableVisible" top="50px">
    <div style="height:65%">
      <span>搜索诊断</span>
      <el-input style="width:200px" placeholder="搜索诊断" v-model="disQuery.name"></el-input>
      <el-button type="primary" @click="getDis1">搜索</el-button>
    <el-table highlight-current-row @row-click="selectDis" :data="disList " style=" margin-top:20px">
      <el-table-column property="icd" label="ICD编码" width="150"></el-table-column>
      <el-table-column property="name" label="名称" width="350"></el-table-column>
      <el-table-column property="code" label="编码" width="200"></el-table-column>
    </el-table>
    <pagination style="margin-top:0px" v-show="total>0" :total="total" page-sizes="[]" :page.sync="disQuery.pageNum" :limit.sync="disQuery.pageSize" @pagination="getDis" />
    </div>
  </el-dialog>
  </div>
</template>
<script>
import {getnonend,submitdefinite,selectEndCaseHistory} from '@/api/outpatient/dmscase'
import {getDmsDislist,parseList} from '@/api/diagnosis'
import Pagination from '@/components/Pagination'
import {selectByType} from '@/api/outpatient/frequentuse'
import {parseTime} from '@/utils'
export default {
  props:['patient','activeName'],
  components: {Pagination},
  name:'Comfirm',
  data(){
    return{
      rules: {
        chiefComplaint: [
          { required: true, message: '请输入主诉', trigger: 'blur' },
          { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
        ],
        historyOfPresentIllness: [
          { required: true, message: '请输入现病史', trigger: 'blur' },
          { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
        ],
        historyOfTreatment: [
          { required: true, message: '请输入现病治疗情况', trigger: 'blur' },
          { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
        ],
        pastHistory: [
          { required: true, message: '请输入既往史', trigger: 'blur' },
          { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
        ],
        allergies: [
          { required: true, message: '请输入过敏史', trigger: 'blur' },
          { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
        ],
        healthCheckup: [
          { required: true, message: '请输入体格检验', trigger: 'blur' },
          { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
        ],
        checkResult: [
          { required: true, message: '请输入检查结果', trigger: 'blur' },
          { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
        ],
        testResult: [
          { required: true, message: '请输入检验结果', trigger: 'blur' },
          { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
        ],
        testAdvice: [
          { required: true, message: '请输入医嘱', trigger: 'blur' },
          { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
        ],
      },
      medicineDiseIdList:[],
      dialogTableVisible:false,
      total:0,
      record:[],
      disQuery: {
        catId: '',
        code: '',
        name: '',
        icd: '',
        status: '',
        pageSize: 10,
        pageNum: 1
      },
      disList:[],
      prerecord:{},
      tabName:'first',
      isclose:true,
      mainwidth:"60%",
      activeNames: ['1'],
    };
  },
  created(){
    this.getmedicineDiseIdList()
  },
  watch:{
    'patient' : function(newVal, oldVal){
      this.patient = newVal
      this.getnonend()
    },
    activeName(n,o){
      if("fourth" === n){
        if (this.patient.registrationStatus === 1) {
          this.getnonend()
        }else{
          this.selectEndCaseHistory()
        }
      }
    }
  },
  methods: {
    getmedicineDiseIdList() {
      selectByType({staffId: this.$store.getters.id, selectType: 2}).then(res => {
        this.medicineDiseIdList = res.data.medicineDiseList
      })
    },
    submitdefinite(prerecord) {
      console.log(this.prerecord)
      this.$refs[prerecord].validate((valid)=>{
        if (valid) {
          this.prerecord.definiteDiseStrList = ''
          if (this.record !== null) {
            this.record.forEach(item => {
              this.prerecord.definiteDiseStrList += (item.id + ',')
            })
            this.prerecord.definiteDiseStrList = this.prerecord.definiteDiseStrList.substr(0, this.prerecord.definiteDiseStrList.length - 1);
          }
          this.prerecord.startDate = parseTime(this.prerecord.startDate, '{y}-{m}-{d}')
          this.prerecord.createTime = parseTime(this.prerecord.createTime, '{y}-{m}-{d}')
          /*this.prerecord.testAdvice=*/
          console.log(this.prerecord)
          submitdefinite(this.prerecord).then(res => {
            this.$notify({
              title: '成功',
              message: '已确诊!',
              type: 'success',
              duration: 2000
            })
            // this.$emit('comfirmdms', 2)
          })
        }else {
          return
        }
      })
    },
    submitdefinite2(prerecord) {

          this.prerecord.definiteDiseStrList = ''
          this.record.forEach(item => {
            this.prerecord.definiteDiseStrList += (item.id + ',')
          })
          this.prerecord.startDate = parseTime(this.prerecord.startDate, '{y}-{m}-{d}')
          this.prerecord.createTime = parseTime(this.prerecord.createTime, '{y}-{m}-{d}')
          this.prerecord.definiteDiseStrList = this.prerecord.definiteDiseStrList.substr(0, this.prerecord.definiteDiseStrList.length - 1);
          /*this.prerecord.testAdvice=*/
          submitdefinite(this.prerecord).then(res => {
            this.$notify({
              title: '成功',
              message: '已确诊!',
              type: 'success',
              duration: 2000
            })
          })
    },
    deleteDis(row) {
      this.record = this.record.filter(item => {
        if (item.id === row.id)
          return false
        return true
      })

    },
    selectDis(val) {
      this.$confirm('是否添加 ' + val.name + ' 到该患者?', '添加诊断', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'success'
      }).then(() => {
        let flag = true
        if (this.record == null) {
          this.record = []
        }
        if (this.record.length == 0) {
          this.record.push(val)
        } else {
          this.record.forEach(item=>{
            if(val.id===item.id){
              flag=0
            }
          })
          if (flag) {
            this.record.push(val)
          } else {
            this.$notify({
              title: '提示',
              message: '已存在该诊断',
              type: 'warning',
              duration: 2000
            })
          }
        }
        this.dialogTableVisible = false
      })
    },
    addDis() {
      this.dialogTableVisible = true
      this.getDis()
    },
    async getDis() {
      const res = await getDmsDislist(this.disQuery)
      this.disList = res.data.list
      this.total = res.data.total
    },
    async getDis1() {
      this.disQuery.pageSize = 10
      this.disQuery.pageNum = 1
      const res = await getDmsDislist(this.disQuery)
      this.disList = res.data.list
      this.total = res.data.total
    },
    getnonend() {
      getnonend(this.patient.registrationId).then(res => {
        const list = res.data.dmsCaseHistoryList[0];
        if(list!=undefined && list!=null && list!=""){
          this.prerecord = list
          parseList(this.prerecord.priliminaryDiseIdList).then(res => {
            this.record = res.data
          })
        }
      })
    },
    controlfast() {
      this.isclose = !this.isclose
      if (this.mainwidth === "60%")
        this.mainwidth = "80%"
      else
        this.mainwidth = "60%"
    },
    print(e) {
      const subOutputRankPrint = document.getElementById('confirm1')
      const newContent = subOutputRankPrint.innerHTML
      const oldContent = document.body.innerHTML
      document.body.innerHTML = newContent
      window.print()
      window.location.reload()
      document.body.innerHTML = oldContent
      return false
    },
    selectEndCaseHistory() {
      selectEndCaseHistory(this.patient.registrationId).then(res => {
        const list = res.data.dmsCaseHistoryList[0];
        console.log(list)
        if(list!=undefined && list!=null && list!=""){
          this.prerecord = list
          parseList(this.prerecord.definiteDiseStrList).then(res => {
            this.record = res.data
          })
        }
      })
    },
  }
}
</script>
