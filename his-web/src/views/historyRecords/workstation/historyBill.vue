<template>
  <div>
    <!--<el-container>
      <el-button type="primary" @click="print">打印</el-button>
    </el-container>-->
    <div id="bill">
      <el-table  :data="bill">
        <el-table-column label="项目名" prop="itemName"></el-table-column>
        <el-table-column label="规格" prop="format"></el-table-column>
        <el-table-column label="数量" prop="currentNum"></el-table-column>
        <el-table-column label="单价（元）" prop="price"></el-table-column>
        <el-table-column label="总金额（元）" prop="totalprice"></el-table-column>
        <el-table-column label="类型" prop="type">
          <template slot-scope="scope">
            <span v-if="scope.row.type===0">检查</span>
            <span v-if="scope.row.type===1">检验</span>
            <span v-if="scope.row.type===2">处置</span>
            <span v-if="scope.row.type===4">草药处方</span>
            <span v-if="scope.row.type===5">成药处方</span>
          </template>
        </el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <el-tag type="warning" v-if="scope.row.status===1">未缴费</el-tag>
            <el-tag type="danger" v-if="scope.row.status===0">作废</el-tag>
            <el-tag type="success" v-if="scope.row.status===2">已缴费</el-tag>
            <el-tag type="success" v-if="scope.row.status===3">已登记</el-tag>
            <el-tag type="success" v-if="scope.row.status===4">已执行</el-tag>
            <el-tag type="danger" v-if="scope.row.status===5">已退费</el-tag>
            <el-tag type="danger" v-if="scope.row.status===6">已过期</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div >
      <el-row :gutter="20" >
        <el-col :span="6"><div class="grid-content bg-purple">未缴费：{{ this.didNotPay }} 元</div></el-col>
        <el-col :span="6"><div class="grid-content bg-purple">已缴费：{{ this.alreadyPay }} 元</div></el-col>
        <el-col :span="6"><div class="grid-content bg-purple">总费用：{{ this.amount }} 元</div></el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import {listByRegistration} from '@/api/outpatient/feequery'
export default {
  name: 'Bill',
  props:['patient'],
  watch:{
    'patient' : function(newVal, oldVal){
      this.patient = newVal
      this.queryBill()
    },
  },
  data(){
    return{
      bill:[],
      amount:0,
      alreadyPay:0,
      didNotPay:0,
    };
  },
  created(){

  },
  methods: {
    queryBill(){
      listByRegistration(this.patient.registrationId).then(res=>{
        this.bill = res.data
        this.bill.forEach(item=>{
          item.totalprice = item.price*item.currentNum
        })

        this.sum();
      })
    },

    sum(){
      this.bill.forEach(item=>{
        if(item.status==2 || item.status==3 || item.status==4){
          this.alreadyPay=this.alreadyPay+item.totalprice
        }else if(item.status==1) {
          this.didNotPay=this.didNotPay+item.totalprice
        }
      })
      this.amount=this.alreadyPay+this.didNotPay;
    },
    print(e){
      const subOutputRankPrint = document.getElementById('bill')
      const newContent = subOutputRankPrint.innerHTML
      const oldContent = document.body.innerHTML
      document.body.innerHTML = newContent
      window.print()
      window.location.reload()
      document.body.innerHTML = oldContent
      return false
    }
  },
}
</script>
