<template>
  <div>
    <!--<el-container>
      <el-button type="primary" @click="print">打印</el-button>
    </el-container>-->
    <div>
      <el-tag type="primary">项目金额总计:</el-tag>
      <el-tag type="warning">{{totalprice}} 元</el-tag>
      <el-table :data="bill" id="historyBill1" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="项目名" prop="itemName"></el-table-column>
        <el-table-column label="规格" prop="format"></el-table-column>
        <el-table-column label="数量" prop="currentNum"></el-table-column>
        <el-table-column label="单价" prop="price"></el-table-column>
        <el-table-column label="总金额" prop="totalprice"></el-table-column>
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
  </div>
</template>
<script>
import {listByRegistration} from '@/api/outpatient/feequery'
export default {
  name: 'Bill',
  props:['patient'],
  watch:{
    'patient' : function(newVal, oldVal){
      this.queryBill(newVal)
    },
  },
  data(){
    return{
      bill: [],
      bill1: [],
      multipleSelection: [],
      totalprice: 0.000,
    };
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.totalprice = 0.00
      this.multipleSelection.forEach(item => {
        this.totalprice += item.price
      })
      this.totalprice = this.totalprice.toFixed(2)
    },
    queryBill(){
      this.bill=[],
      this.bill1=[],
      this.multipleSelection=[],
      listByRegistration(this.patient.registrationId).then(res=>{
        this.bill1 = res.data
        this.bill1.forEach(item=>{
          if((item.status===2 || item.status===3 || item.status===4) && item.invoice===0){
            item.totalprice = item.price*item.currentNum
            this.bill.push(item)
          }
        })
        /*this.sum()*/
      })

    },
    sum(){
      if(this.bill!=null && this.bill != undefined && this.bill.length>0){
        this.bill.forEach(item=>{
          if(item.status==1){
            this.alreadyPay=this.alreadyPay+item.totalprice
          }else if(item.status==2 || item.status==3 || item.status==4) {
            this.didNotPay=this.didNotPay+item.totalprice
          }
        })
        this.amount=this.alreadyPay+this.didNotPay;
        let money1={
          itemName:'',
          format:'',
          currentNum:'',
          price:'',
          totalprice:'',
          type:''
        }
        let money2={
          itemName:'',
          format:'',
          currentNum:'',
          price:'',
          totalprice:'',
          type:''
        }
        let money3={
          itemName:'',
          format:'',
          currentNum:'',
          price:'',
          totalprice:'',
          type:''
        }
        money1.itemName="已缴费"
        money1.totalprice=this.alreadyPay
        money2.itemName="未缴费"
        money2.totalprice=this.didNotPay
        money3.itemName="总费用"
        money3.totalprice=this.amount
        this.bill.push(money1)
        this.bill.push(money2)
        this.bill.push(money3)
      }
    },
    print(e){
      const subOutputRankPrint = document.getElementById('historyBill1')
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
