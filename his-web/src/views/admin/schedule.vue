<template>
  <div>
    <div v-if="!showdetail" class="app-container" style="text-align:center">
      <div class="demo-input-suffix">
        科室选择:
        <el-select placeholder="科室选择" v-model="depart" clearable style="width: 130px" class="filter-item" filterable @change="getRuleList">
          <el-option v-for="item in alldepart" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-button class="filter-item" size="small" type="danger" icon="el-icon-delete" @click="delrule">删除</el-button>
        <el-button class="filter-item" size="small" type="primary" @click="jumpadd();addState = true;editbutton=false"><i class="el-icon-edit-outline" />新增排班规则</el-button>
      </div>
      <el-table v-loading="listLoading" :data="ruleList" style="width: 100%;margin-top:30px;"  stripe @selection-change="changerule">
        <el-table-column type="selection" width="55" @selection-change="changerule"></el-table-column>
        <el-table-column prop="ruleName" align="center" label="规则名称" width="150">
          <template slot-scope="scope">
            {{ scope.row.ruleName }}
          </template>
        </el-table-column>
        <el-table-column prop="description" align="center" label="规则描述">
          <template slot-scope=" scope">
            {{ scope.row.description }}
          </template>
        </el-table-column>
        <el-table-column prop="deptName" align="center" label="科室" width="120">
          <template slot-scope="scope">
            {{ scope.row.deptName }}
          </template>
        </el-table-column>
        <el-table-column prop="operatorId" align="center" label="创建人">
          <template slot-scope="scope">
            {{ scope.row.operatorName }}
          </template>
        </el-table-column>
        <el-table-column prop="operateTime" align="center" label="创建时间">
          <template slot-scope="scope">
            {{ scope.row.operateTime }}
          </template>
        </el-table-column>
        <el-table-column width="250px">
          <template slot-scope="scope">
            <el-button type="warning" size="mini" @click="getRuleDetail(scope.row.id);editbutton=true">修改</el-button>
            <el-button class="filter-item" size="mini" type="primary" @click="dialogVisible=true;ruleid=scope.row.id"><i class="el-icon-document" />
              生成排班计划表
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getRuleList" />
    </div>

  <!-- 新增或修改排班规则 -->
    <div v-if="showdetail" class="app-container" style="text-align:center">
      <div class="demo-input-suffix" v-if="addState">
        <el-form :model="addRule" inline>
          <el-form-item label="科室选择:">
            <el-select placeholder="科室选择" v-model="addRule.deptId" clearable style="width: 130px" class="filter-item" filterable @change="getDepDoctor">
              <el-option v-for="item in alldepart" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button class="filter-item" type="primary" icon="el-icon-delete" @click="empty">清空</el-button>
          </el-form-item>

        </el-form>
      </div>
      <el-table v-loading="listLoading" :data="scheduleList" style="width: 100%;margin-top:10px;" border @selection-change="selectrule">
        <el-table-column type="selection" width="55" @selection-change="selectrule"></el-table-column>
        <el-table-column prop="code" align="center" sortable label="医生名称">
          <template slot-scope="scope">
            {{ scope.row.staffName }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期一上午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked1"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期一下午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked2"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期二上午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked3"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期二下午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked4"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期三上午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked5"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期三下午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked6"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期四上午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked7"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期四下午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked8"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期五上午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked9"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期五下午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked10"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期六上午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked11"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期六下午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked12"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期日上午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked13"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column align="center" label="星期日下午" width="70px">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.checked14"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column label="排班限额" align="center" width="200px">
          <template slot-scope="scope">
            <!--<el-input v-model="scope.row.limit"></el-input>-->
            <el-input-number size="mini" v-model="scope.row.limit"></el-input-number>
          </template>
        </el-table-column>
      </el-table>
      <div>
        <el-form :model="addRule" ref="addRule" inline style="margin-top:20px" :rules="rules">
          <el-form-item label="规则名称" prop="ruleName">
            <el-input placeholder="规则名称" v-model="addRule.ruleName" class="filter-item" @keyup.enter.native="handleFilter" />
          </el-form-item>
          <el-form-item label="规则描述" prop="description">
            <el-input placeholder="规则描述" v-model="addRule.description" class="filter-item" @keyup.enter.native="handleFilter" />
          </el-form-item>
          <el-button class="filter-item" type="danger" icon="el-icon-download" @click="jumpback">
            取消
          </el-button>
          <el-button v-if="!editbutton" class="filter-item" type="primary" icon="el-icon-download" @click="createRule('addRule')">
            新增排班规则
          </el-button>
          <el-button v-if="editbutton" class="filter-item" type="primary" icon="el-icon-download" @click="updateRule">
            保存修改
          </el-button>
        </el-form>

      </div>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="670px" title="生成排班计划表" @close="getDeplist">
      <div>
        <!--default-value="2019-06-01"-->
        <span class="demonstration">开始时间:</span>
        <el-date-picker
                v-model="value1"
                align="right"
                type="date"
                placeholder="选择日期"
                :picker-options="pickerOptions">
        </el-date-picker>
        <span style="margin-left:30px" class="demonstration">结束时间:</span>
        <el-date-picker
                v-model="value2"
                align="right"
                type="date"
                placeholder="选择日期"
                :picker-options="pickerOptions">
        </el-date-picker>
      </div>
      <div>

      </div>
      <div style="text-align:right;margin-top:30px">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="generateSkd">生成排班表</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getAllDep, getDeplist} from '@/api/department'
import {getRulelist,generateSkd,createRule,getRuleDetail,updateRule,deleteRule} from '@/api/schedule'
import {getUserList} from '@/api/user'
import Pagination from '@/components/Pagination'
import {getAllReg} from '@/api/admin'
import {parseTime,formatTime} from '@/utils'
export default {
  components: {Pagination},
  data() {
    return {
      editbutton:false,
      addState: false,
      rulelistid:'',
      rulecurrent: [],
      addRule:{},
      ruleid:'',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() <= Date.now()-3600*1000*24;
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '一周后',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }, {
          text: '一月后',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() + 3600 * 1000 * 24 * 30);
            picker.$emit('pick', date);
          }
        }]
      },
      value1: '',
      value2:'',
      allreg:[],
      dialogVisible:false,
      listLoading: false,
      total: 0,
      depart:'',
      alldepart:[],
      listQuery:{
        deptId:'',
        pageSize:10,
        pageNum:1,
      },
      ruleList:[],
      showdetail:false,
      schedule:0,
      scheduleList: [],
      rulelistref:[],
      rules:{
        ruleName:[{required: true, message: '请输入规则名称',trigger: 'blur'}],
        description:[{required: true, message: '请输入规则描述',trigger: 'blur'}]
      }
    }
  },
  created(){
    this.getAlldep()
    this.getAllreg()
  },
  methods: {
    change(e){
      this.$forceUpdate()
    },
    getDepDoctor(){
      let data = {}
      this.scheduleList = []
      data.deptId = this.addRule.deptId
      data.pageSize = 10000
      data.pageNum = 1
      getUserList(data).then(res=>{
        res.data.list.forEach(item=>{
          let temp = {}
          temp.staffName = item.name
          temp.id = item.id
          this.scheduleList.push(temp)
        })
      })
    },
    updateRule(){

      if(this.addRule.deptId === null || this.addRule.deptId === undefined) {
        this.$notify({
          title: '提示',
          message: "保存排班规则前请选择对应科室！",
          type: 'warning',
          duration: 2000
        })
        return;
      }
      if(this.rulecurrent.length === 0) {
        this.$notify({
          title: '提示',
          message: "请选择医生！",
          type: 'warning',
          duration: 2000
        })
        return;
      }
      if (!this.addRule.ruleName) {
        this.$notify({
          title: '提示',
          message: "请填写规则名称！",
          type: 'warning',
          duration: 2000
        })
        return;
      }
      if (!this.addRule.description) {
        this.$notify({
          title: '提示',
          message: "请填写规则描述！",
          type: 'warning',
          duration: 2000
        })
        return;
      }
      let docArr = [];
      this.rulecurrent.forEach(element => {
        let daysOfWeek=''
        let num = 0;
        if(element.checked1){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked2){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked3){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked4){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked5){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked6){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked7){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked8){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked9){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked10){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked11){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked12){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked13){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        if(element.checked14){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
        element.daysOfWeek = daysOfWeek
        element.status = 1
        element.staffId = element.staffId
        element.skLimit = element.limit
        if (!element.limit || element.limit != num) {
          docArr.push(element.staffName)
        }
      });
      let data = {}
      data.smsSkdRuleItemParamList = this.rulecurrent
      data.deptId = this.addRule.deptId
      data.description = this.addRule.description
      data.operatorId = this.$store.getters.id
      data.status = 1
      data.ruleName = this.addRule.ruleName
      data.ruleid = this.rulelistid
      updateRule(data).then(res=>{
        this.showdetail=false
        this.$notify({
          title: '成功',
          message: res.message,
          type: 'success',
          duration: 2000
        })
        this.getRuleList()
      })
    },
    async getRuleDetail(id){
      this.rulelistid = id
      let res = await getRuleDetail(id)
      this.addRule = res.data

      this.scheduleList = res.data.smsSkdRuleItemResultList
      let i =0
      this.scheduleList.forEach(item=>{
        item.limit = item.skLimit
        if(item.daysOfWeek.charAt(0)==='1'){item.checked1=true}else{item.checked1=false}
        if(item.daysOfWeek.charAt(1)==='1'){item.checked2=true}else{item.checked2=false}
        if(item.daysOfWeek.charAt(2)==='1'){item.checked3=true}else{item.checked3=false}
        if(item.daysOfWeek.charAt(3)==='1'){item.checked4=true}else{item.checked4=false}
        if(item.daysOfWeek.charAt(4)==='1'){item.checked5=true}else{item.checked5=false}
        if(item.daysOfWeek.charAt(5)==='1'){item.checked6=true}else{item.checked6=false}
        if(item.daysOfWeek.charAt(6)==='1'){item.checked7=true}else{item.checked7=false}
        if(item.daysOfWeek.charAt(7)==='1'){item.checked8=true}else{item.checked8=false}
        if(item.daysOfWeek.charAt(8)==='1'){item.checked9=true}else{item.checked9=false}
        if(item.daysOfWeek.charAt(9)==='1'){item.checked10=true}else{item.checked10=false}
        if(item.daysOfWeek.charAt(10)==='1'){item.checked11=true}else{item.checked11=false}
        if(item.daysOfWeek.charAt(11)==='1'){item.checked12=true}else{item.checked12=false}
        if(item.daysOfWeek.charAt(12)==='1'){item.checked13=true}else{item.checked13=false}
        if(item.daysOfWeek.charAt(13)==='1'){item.checked14=true}else{item.checked14=false}
      })
      this.getRuleList()
      this.showdetail=true;
    },
    createRule(addRule){
      let sch = []
      this.rulecurrent.forEach(limit =>{
        if (limit.limit === undefined || limit.limit === '') {
          sch.push(limit.staffName)
        }
      })

      if (sch.join(',') !== "") {
        this.$notify({
          title: '提示',
          message: "请填写排班限额！",
          type: 'warning',
          duration: 2000
        })
        return;
      }
      this.$refs[addRule].validate((valid)=>{
        if (valid) {
          // "addRule.ruleName" class="filter-item" @keyup.enter.native="handleFilter" />
          //     </el-form-item>
          //     <el-form-item label="规则描述">
          //       <el-input placeholder="规则描述" v-model="addRule.description
          if(this.addRule.deptId === null || this.addRule.deptId === undefined) {
            this.$notify({
              title: '提示',
              message: "保存排班规则前请选择对应科室！",
              type: 'warning',
              duration: 2000
            })
            return;
          }
          if(this.rulecurrent.length === 0) {
            this.$notify({
              title: '提示',
              message: "请选择医生！",
              type: 'warning',
              duration: 2000
            })
            return;
          }
          if (!this.addRule.ruleName) {
            this.$notify({
              title: '提示',
              message: "请填写规则名称！",
              type: 'warning',
              duration: 2000
            })
            return;
          }
          if (!this.addRule.description) {
            this.$notify({
              title: '提示',
              message: "请填写规则描述！",
              type: 'warning',
              duration: 2000
            })
            return;
          }
          /*
           console.log(this.rulecurrent.limit)
           if(this.rulecurrent.limit===undefined){
             this.$notify({
               title: '提示',
               message: "请输入排班限额！",
               type: 'warning',
               duration: 2000
             })
             return;
           }*/
          let docArr = [];
          this.rulecurrent.forEach(element => {
            let daysOfWeek=''
            let num = 0;
            if(element.checked1){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked2){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked3){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked4){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked5){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked6){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked7){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked8){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked9){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked10){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked11){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked12){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked13){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            if(element.checked14){daysOfWeek+='1';num += 1}else{daysOfWeek+='0'}
            element.daysOfWeek = daysOfWeek
            element.status = 1
            element.staffId = element.id
            element.skLimit = element.limit
            if (!element.limit || element.limit != num) {
              docArr.push(element.staffName)
            }
          });
          let data = {}
          data.smsSkdRuleItemParamList = this.rulecurrent
          data.deptId = this.addRule.deptId
          data.description = this.addRule.description
          data.operatorId = this.$store.getters.id
          data.status = 1
          data.ruleName = this.addRule.ruleName
          createRule(data).then(res=>{
            this.getRuleList()
            this.showdetail=false
            this.addState = false;
            this.$notify({
              title: '成功',
              message: res.message,
              type: 'success',
              duration: 2000
            })
          })
        }else {
          return
        }
      })
    },
    selectrule(val){

      this.rulecurrent = val
    },
    async getAllreg(){
      const res = await getAllReg()
      this.allreg = res.data
    },
    generateSkd(){
      let data = {}
      data.ruleIds=this.ruleid
      if (!this.value1) {
        this.$confirm('请选择排班表开始时间', '提示', {
          type: 'warning'
        });
        return;
      }
      if (!this.value2) {
        this.$confirm('请选择排班表结束时间', '提示', {
          type: 'warning'
        });
        return;
      }
      data.startDate = parseTime(this.value1).substr(0,10)
      data.endDate = parseTime(this.value2).substr(0,10)
      generateSkd(data).then(res=>{
        if(res.data===1){
          this.$notify({
            title: '提示',
            message: res.message,
            type: 'success',
            duration: 2000
          })
        }else {
          this.$notify({
            title: '提示',
            message: res.message,
            type: 'warning',
            duration: 2000
          })
        }
      })
      this.dialogVisible = false
    },
    changerule(val){
      this.rulelistref=val
    },
    delrule(){
      if(this.rulelistref.length===0) {
        this.$notify({
          title: '提示',
          message: '请先选中要删除的规则',
          type: 'warning',
          duration: 3500
        })
        return
      }
      this.$confirm('确认删除选中的排班规则?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        let data = ''
        this.rulelistref.forEach(item=>{
          data+=item.id+','
        })
        data = data.substr(0, data.length - 1);
        deleteRule(data).then(res=>{
          this.$notify({
            title: '成功',
            message: res.message,
            type: 'success',
            duration: 2000
          })
          this.getRuleList()
        })
      })
    },
    confirmdate(){

    },
    async getAlldep(){
      const res = await getAllDep()
      this.alldepart=res.data
    },
    async getDeplist() {
      this.listLoading = true
      const response = await getDeplist(this.listQuery)
      this.depList = response.data.list
      this.total = response.data.total
      this.listLoading = false
    },
    getRuleList(){
      if(this.depart!==''){
        this.ruleList = []
        this.rulecurrent=[];
        this.listQuery.deptId=this.depart
        getRulelist(this.listQuery).then(res=>{
          if(res.data === undefined || res.data === null) {
            this.ruleList = []
            return
          }
          this.ruleList=res.data.list
          this.ruleList.forEach(item=>{
            item.operateTime = parseTime(item.operateTime)
          })
          this.total=res.data.total
        })
      }else {
        this.ruleList=[]
        this.total=0
      }
    },
    jumpadd(){
      this.showdetail=!this.showdetail
      this.addRule={}
      /*this.getDepDoctor()*/
    },
    showRow(row){
//赋值给radio
      this.radio = this.tableData.indexOf(row);
    },
    handleCurrentChange(val,index) {
      this.currentRow = val;
      this.$emit('data',val.pkg);
    },

    getCurrentRow(val){

    },
    choose(){
      this.schedule = !this.schedule;
    },
    jumpback(){
      this.showdetail=!this.showdetail
      this.addState = false;
    },
    empty(){
      this.$confirm('确认清空', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.addRule.deptId = '';
      })
      // this.showdetail = false;
      // this.ruleList=[];
      // this.rulecurrent=[];
      // this.rulelistid ='';
      // this.scheduleList = [];
    },
    handleClearDept() {
      this.$confirm('确认清空所选科室', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.depart = '';
        this.ruleList=[]
        this.total=0
      })
    }
  }
}
</script>
<style>
  body .el-table th.gutter{
display: table-cell!important;
}
</style>
