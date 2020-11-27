<template>
  <!-- 收费处 -->
  <div class="div1">
    <div class="filter-container">
      <el-input  placeholder="身份证号" style="width: 200px;margin-left:10px;margin-top: 10px" v-model="listQuery.identificationNo" @blur="searchMechlist()" class="filter-item"/>
      <el-button  class="filter-item" style="margin-left:72%;margin-top: 10px" type="danger" @click="duka()">
        读卡
      </el-button>
      <el-button  class="filter-item" style="margin-top: 10px" type="danger" @click="moueny()">
        缴费
      </el-button>
      <el-button  class="filter-item" type="success" style="margin-top: 10px"  @click="refoundMoueny()">
        退费
      </el-button>
    </div>

    <el-table border stripe highlight-current-row :data="MechList" style="margin-left:10px" @selection-change="handlechange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="挂号编码">
        <template slot-scope="scope">
          {{scope.row.registrationId}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="姓名">
        <template slot-scope="scope">
          {{scope.row.patientName}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="年龄">
        <template slot-scope="scope">
          {{scope.row.patientAgeStr}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="性别">
        <template slot-scope="scope">
          <span v-if="scope.row.patientGender===0">男</span>
          <span v-if="scope.row.patientGender===1">女</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="金额(元)" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.amount }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="项目名称">
        <template slot-scope="scope">
          {{scope.row.itemName}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="检查部位">
        <template slot-scope="scope">
          {{scope.row.checkParts}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="开立医生">
        <template slot-scope="scope">
          {{scope.row.logStaffName}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="开立时间" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="项目类型" width="200">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0">检查</el-tag>
          <el-tag v-if="scope.row.type === 1">检验</el-tag>
          <el-tag v-if="scope.row.type === 2">处置</el-tag>
          <el-tag v-if="scope.row.type === 3">草药处方</el-tag>
          <el-tag v-if="scope.row.type === 4">成药处方</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态">
        <template slot-scope="scope">
          <el-tag v-show="scope.row.status===5" type="success">已退费</el-tag>
          <el-tag v-show="scope.row.status===2" type="info">未登记</el-tag>
          <el-tag v-show="scope.row.status===3" type="primary">已登记</el-tag>
          <el-tag v-show="scope.row.status===4" type="success">已完成</el-tag>
          <el-tag v-show="scope.row.status===1" type="warning">未缴费</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status===1" size="small" type="danger"  @click="charge(scope.row)">缴费</el-button>
          <el-button v-if="scope.row.status !==1 && scope.row.status !==5" size="small" type="success"  @click="refunds(scope.row)">退费</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getMechlist"/>
  </div>


</template>

<script>
  import {listByDeptChange,log,refundCharge} from '@/api/technology'
  import {connect, readCert} from "@/utils/ajax";
  import {getdrugList} from '@/api/drug'
  import Pagination from '@/components/Pagination'
  import {apply} from '@/api/outpatient/prescription'
  import {charge} from '@/api/regist'
  import {parseTime} from "@/utils";
  export default{
    components:{Pagination},
    data(){
      return{
        total:0,
        uploadpatient:'',
        name:'',
        listQuery:{
          identificationNo:'',
          deptId:'',
          pageSize: 10,
          pageNum: 1,
        },
        extraname:{
          name:'',
          type:1
        },
        page:{
          pageNum:1,
          pageSize:10,
        },
        oneprescription:{
          name:'',
          druglist:[],
          amount:0,
          status:0,
        },
        patient:{},
        uploadvisible:false,
        MechList:[],
        drugList:[],
        searchdrug:'',
        dialogTableVisible:false,
        isaside: true,
        activeName: 'first',
        activeName2: 'first',
        charges:[],
        refs:{},
      };
    },
    created(){
      this.getMechlist()
      const handleConncet = () => {
        connect((data) => {
          const res = JSON.parse(data);
          if (res.resultFlag !== 0) {
            this.$confirm(res.errorMsg, "提示", {
              confirmButtonText: "重试",
              cancelButtonText: "取消",
              type: "warning",
            }).then(() => {
              handleConncet();
            });
          } else {
            this.$notify({
              title: "提示",
              message: '读卡器' + res.errorMsg,
              type: "success",
              duration: 2000,
            });
          }
        });
      }
      handleConncet()
    },
    methods: {
      duka(){
        readCert((data) => {
          const person = JSON.parse(data);
          if (person.resultFlag !== 0) {
            this.$notify({
              title: "提示",
              message: person.resultFlag == -11 ? person.errorMsg + ",未放置身份证！" : "读卡失败，请重试",
              type: "warning",
              duration: 3500,
            });
          } else {
            this.listQuery.identificationNo = person.certNumber
            this.searchMechlist()
          }
        });
      },
      searchMechlist(){
        this.listQuery.pageNum = 1
        this.listQuery.pageSize = 10
        this.getMechlist()
      },
      getMechlist(){
        this.listQuery.deptId=this.$store.getters.deptId;
        listByDeptChange(this.listQuery).then(res=>{
          this.total=res.data.total
          this.MechList = res.data.list
          this.MechList.forEach((item) => {
            if (item.createTime !== null) {
              item.createTime = parseTime(item.createTime);
            }
          });
        })
      },
      // 缴费
      charge(val){
        this.$confirm(' 确定为 '+ val.itemType +' 项目 '+val.itemName+'  缴纳费用  '+val.amount+'  元吗?', val.itemType +'  项目', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          let data = [];
            this.refs.chargeItemId = val.itemRecordId
            this.refs.amount = val.amount
            this.refs.invoiceNo = this.orderCode()
            this.refs.type = val.type+1
            this.refs.operatorId = this.$store.getters.id
            data.push(this.refs);
          charge(data).then(res=>{
            if (res.data === 2) {
              this.$notify({
                title: '缴费失败',
                message: '余额不足,请充值',
                type: 'warning',
                duration: 2000
              })
              return
            }
            this.$notify({
              title: '成功',
              message: '缴费成功',
              type: 'success',
              duration: 2000
            })
            this.getMechlist()
          })
        })
      },
      // 生成发票号
      orderCode() {
        let orderCode = '';
        for (let i = 0; i < 6; i++) //6位随机数，用以加在时间戳后面。
        {
          orderCode += Math.floor(Math.random() * 10);
        }
        orderCode = new Date().getTime() + orderCode;  //时间戳，用来生成订单号。
        return orderCode;
      },
      handlechange(val){
        this.charges=val
      },
      moueny(){
        if (this.charges.length <= 0) {
          this.$notify({
            title: '提示',
            message: '未选中数据',
            type: 'warning',
            duration: 2000
          })
          return
        }
        let flags = []
        this.charges.forEach(item => {
          if (item.status != 1) {
            flags.push(1)
          }
        })
        if (flags.join(",").includes(1)) {
          this.$notify({
            title: '提示',
            message: '您选择的项目中包含已缴费的项目，请检查后重新缴费！',
            type: 'warning',
            duration: 2000
          })
          return false;
        }
        let amount = 0
        let itemName = []
        this.charges.forEach((item) => {
          amount += Number(item.amount)
          itemName.push(item.itemName)

        })
        this.$confirm(' 确定为 ' + itemName.join(",") + '  缴纳费用  '+amount+'  元吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          let data = [];
          this.charges.forEach(item => {
            let ref = {}
            ref.chargeItemId = item.itemRecordId
            ref.amount = item.amount
            ref.invoiceNo = this.orderCode()
            ref.type = item.type+1
            ref.operatorId = this.$store.getters.id
            data.push(ref);
          })
          charge(data).then(res=>{
            if (res.data === 2) {
              this.$notify({
                title: '缴费失败',
                message: '余额不足,请充值',
                type: 'warning',
                duration: 2000
              })
              return
            }
            this.$notify({
              title: '成功',
              message: '缴费成功',
              type: 'success',
              duration: 2000
            })
            this.getMechlist()
          })
        })
      },
      // 退费
      refunds(val) {
        this.$confirm(' 确定为 '+ val.itemType +' 项目 '+val.itemName+'  退费  '+val.amount+'  元吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          let data = [];
          this.refs.chargeItemId = val.itemRecordId
          this.refs.refundAmount = val.amount
          this.refs.invoiceNo = this.orderCode()
          this.refs.type = val.type+1
          this.refs.operatorId = this.$store.getters.id
          this.refs.patientId = val.patientId
          data.push(this.refs);
          refundCharge(data).then(res=>{
            if (res.data === 2) {
              this.$notify({
                title: '提示',
                message: '退费失败',
                type: 'warning',
                duration: 2000
              })
              return
            }
            this.$notify({
              title: '提示',
              message: '退费成功',
              type: 'success',
              duration: 2000
            })
            this.getMechlist()
          })
      })
    },
      refoundMoueny() {
        if (this.charges.length <= 0) {
          this.$notify({
            title: '提示',
            message: '未选中数据',
            type: 'warning',
            duration: 2000
          })
          return
        }
        let flags = []
        this.charges.forEach(item => {
          if (item.status != 5) {
            flags.push(1)
          }else if(item === 1){
            flags.push(2)
          }else {
            flags.push(5)
          }
        })
        if (flags.join(",").includes(2)) {
          this.$notify({
            title: '提示',
            message: '您选择的项目中包含未缴费的项目，请检查后重新退费！',
            type: 'warning',
            duration: 2000
          })
          return false;
        }
        if (flags.join(",").includes(5)) {
          this.$notify({
            title: '提示',
            message: '您选择的项目中包含已退费的项目，请检查后重新退费！',
            type: 'warning',
            duration: 2000
          })
          return false;
        }
        let amount = 0
        let itemName = []
        this.charges.forEach((item) => {
          amount += Number(item.amount)
          itemName.push(item.itemName)

        })
        this.$confirm(' 确定为 '+itemName.join(",")+'  退费  '+amount+'  元吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          debugger
          let data = [];
          this.charges.forEach((item)=>{
            let ref = {}
            ref.chargeItemId = item.itemRecordId
            ref.refundAmount = item.amount
            ref.invoiceNo = this.orderCode()
            ref.type = item.type+1
            ref.operatorId = this.$store.getters.id
            ref.patientId = item.patientId
            data.push(ref);
          })
          refundCharge(data).then(res=>{
            if (res.data === 2) {
              this.$notify({
                title: '提示',
                message: '退费失败',
                type: 'warning',
                duration: 2000
              })
              return
            }
            this.$notify({
              title: '提示',
              message: '退费成功',
              type: 'success',
              duration: 2000
            })
            this.getMechlist()
          })
        })
      }
    }
  }
</script>
<style>
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


