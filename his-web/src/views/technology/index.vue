<template>
  <!-- 门诊医技工作台 -->
  <div class="div1">
      <aside>
        <el-button disabled style="color:black">门诊医技工作台</el-button>
        <el-input  placeholder="姓名" v-model="name" style="width: 180px" class="filter-item input1"/>
        <el-button @click="searchMechlist()"><svg-icon icon-class="search" /></el-button>
      </aside>

      <el-table border stripe highlight-current-row :data="MechList" style="margin-left:10px">
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
        <el-table-column align="center" label="开立项目">
          <template slot-scope="scope">
            {{scope.row.itemName}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="检查部位">
          <template slot-scope="scope">
            {{scope.row.checkParts}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="状态">
          <template slot-scope="scope">
            <el-tag v-show="scope.row.status===2" type="info">未登记</el-tag>
            <el-tag v-show="scope.row.status===3" type="primary">已登记</el-tag>
            <el-tag v-show="scope.row.status===4" type="success">已完成</el-tag>
            <el-tag v-show="scope.row.status===1" type="warning">未缴费</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status===3" size="small" type="primary" @click="upload(scope.row)">上传结果</el-button>
            <el-button v-if="scope.row.status===4" size="small" disabled>已完成</el-button>
            <el-button v-if="scope.row.status===1" size="small" type="danger"  @click="charge(scope.row)">缴费</el-button>
            <el-button v-if="scope.row.status===2" size="small" type="primary" @click="register(scope.row)">登记</el-button>
<!--            <el-button v-if="scope.row.status===2" type="danger" size="small" @click="addExtra(scope.row)">补录</el-button>-->
          </template>
        </el-table-column>
      </el-table>
    <pagination :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getMechlist"/>
      <el-dialog title="药材补录" :visible.sync="dialogTableVisible" width="1500px" top="10px">
        <el-container>
        <el-aside width="30%" style="padding:0 0 0 0;margin:0 0 0 0">
            <el-tag type="primary" style="width:100%;height:30px">药品目录
            <el-button type="primary" size="mini"  style="width: 20px;;float:right"><svg-icon icon-class="search" style="margin-left:-6px"/></el-button>
            <el-input  size="mini" placeholder="药品名称" v-model="searchdrug" style="width:60%;float:right" @change="getdrugList(0)"></el-input>
            </el-tag>
            <el-table :data="drugList" height="500px" @row-click="choosedrug">
              <el-table-column label="药品名" prop="name"></el-table-column>
              <el-table-column label="价格(元)" prop="price" width="100px"></el-table-column>
            </el-table>
            <pagination layout="prev, pager, next" auto-scroll="false" style="margin-top:0px" page-sizes="[]"  v-show="total>0" :total="total" :page.sync="page.pageNum" :limit.sync="page.pageSize" @pagination="getdrugList(0)" />
          <div>
          </div>
        </el-aside>
        <el-main>
          <el-form label-width="100px" :model="extraname" inline>
            <el-form-item label="补录处方名" >
              <el-input v-model="extraname.name" style="width:200px" placeholder=""></el-input>
            </el-form-item>
            <el-form-item label="补录处方类型">
              <el-select v-model="extraname.type" placeholder="选择类型" clearable style="width: 130px" class="filter-item" @change="changeextratype">
                <el-option v-for="item in [{key:1,value:'成药处方'},{key:2,value:'草药处方'}]" :key="item.key" :label="item.value" :value="item.key" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="float:right" @click="comfirmExtra">补录处方开立</el-button>
            </el-form-item>
          </el-form>
          <el-tag type="primary">项目金额总计:</el-tag>
          <el-tag type="warning">{{oneprescription.amount}}元</el-tag>

          <el-table  height="500px" :data="oneprescription.druglist" cell-style="text-align:center" header-cell-style="text-align:center">
              <el-table-column width="50px">
                <template slot-scope="scope">
                  <el-button type="text" @click="deldrug(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            <el-table-column property="name" label="药品名" width="150"></el-table-column>
            <el-table-column property="format" label="规格" width="200"></el-table-column>
            <el-table-column property="price" label="单价"></el-table-column>
            <el-table-column label="数量" width="130px">
              <template slot-scope="scope">
                <el-input-number controls-position="right" style="width:100px" :min="1" :max="100" size="mini" @change="changenum(scope.row)" v-model="scope.row.num"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="剂型" width="130px" prop="dosage.name">
            </el-table-column>
            <el-table-column label="使用建议" width="130">
              <template slot-scope="scope">
                <el-input placeholder="用法" v-model="scope.row.medicalAdvice"></el-input>
              </template>
            </el-table-column>
          </el-table>
        </el-main>
        </el-container>
      </el-dialog>
      <el-dialog title="结果" :visible.sync="uploadvisible" v-bind:patient="patient" width="600px">
        <Upload ref="upload" :uploadpatient="uploadpatient" @reflect="reflect" />
      </el-dialog>
  </div>


</template>

<script>
  import Upload from '../technology/upload'
  import {getMechlist,log,uploadResult} from '@/api/technology'
  import {getdrugList} from '@/api/drug'
  import Pagination from '@/components/Pagination'
  import {apply} from '@/api/outpatient/prescription'
  import {charge} from '@/api/regist'
  export default{
    components:{Upload,Pagination},
    data(){
      return{
        total:0,
        uploadpatient:'',
        name:'',
        listQuery:{
          name:'',
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
        refs:{
          chargeItemId:'',
          invoiceNo:'',
          operatorId:'',
          settlementCatId:7,
          amount:'',
          type:''
        },
      };
    },
    created(){
      this.getMechlist()
    },
    methods: {
      reflect(res){
        this.$notify({
            title: '提示',
            message: res.message,
            type: 'success',
            duration: 2000
          })
        this.uploadvisible = false
        this.getMechlist()
      },
      comfirmExtra(){
        let data = {}
        data = this.oneprescription
        data.registrationId = this.patient.registrationId
        data.name = this.extraname.name
        data.createStaffId = this.$store.getters.id
        data.status = 1
        data.amount = this.oneprescription.amount
        data.dmsMedicineItemRecordList = data.druglist
        data.dmsMedicineItemRecordList.forEach(item=>{
          item.drugId = item.id
        })
        apply(data).then(res=>{
          this.$notify({
            title: '成功',
            message: res.message,
            type: 'success',
            duration: 2000
          })
          this.oneprescription.druglist = []
          this.getMechlist()
        })
        this.dialogTableVisible = false
      },
      deldrug(row){
        this.oneprescription.druglist = this.oneprescription.druglist.filter(item=>{
          if(item.id===row.id)
            return false
          return true
        })
        this.oneprescription.amount = 0
        this.oneprescription.druglist.forEach(item=>{
          this.oneprescription.amount += item.price
        })
        this.oneprescription.amount = Math.floor((this.oneprescription.amount+0.5)*100)/100
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
      changeextratype(val){
        if(val===1){
          this.getdrugList(101)
        }
        else{
          this.getdrugList(103)
        }
      },
      async getdrugList(type) {
        let data = {}
        data.pageSize = this.page.pageSize
        data.pageNum = this.page.pageNum
        if(type!==0)
          data.typeId = type
        data.name = this.searchdrug
        const response = await getdrugList(data)
        this.drugList = response.data.list
        this.total = response.data.total
      },
      upload(val){
        this.uploadpatient = val.itemRecordId
        this.uploadvisible = true
      },
      searchMechlist(){
        this.listQuery.pageNum = 1
        this.listQuery.pageSize = 10
        this.listQuery.name = this.name
        this.getMechlist()
      },
      getMechlist(){
        this.listQuery.deptId=this.$store.getters.deptId;
        getMechlist(this.listQuery).then(res=>{
          this.total=res.data.total
          this.MechList = res.data.list
        })
      },
      addExtra(row){
        this.patient = row
        this.dialogTableVisible= true
        this.getdrugList(101)
      },
      showaside(){
        this.isaside = !this.isaside
      },
      register(row){
        this.$confirm('确定为 '+row.patientName+' 登记项目 '+row.itemName+' 吗?', '项目登记', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        let data = {}
        data.id = row.itemRecordId
        data.logStaffId = this.$store.getters.id
        log(data).then(res=>{
          this.$notify({
            title: '成功',
            message: res.message,
            type: 'success',
            duration: 2000
          })
          this.getMechlist()
        })
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


