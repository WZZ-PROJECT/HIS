<template>
  <div>
    <div id="bill12">
      <aside style="margin:0 0 0 0">
        <el-button type="text" @click="isprint(1)"><i class="el-icon-circle-check"/>创新中心打印</el-button>
        <el-button type="text" @click="isprint(0)"><i class="el-icon-circle-check"/>非创新中心检查打印</el-button>
        <el-button type="text" @click="isprint(2)"><i class="el-icon-circle-check"/>非创新中心药品打印</el-button>
      </aside>
      <el-table style="width: 100%" :data="bill" height="530px">
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

    <el-dialog title="打印患者账单" :visible.sync="isprintmark">
      <!--<el-button type="text" @click="printmark">打印</el-button>
      <el-table :data="printdata"
                style="margin-top:20px;width: 600px"
                id="billmark">
        <el-table-column style="width: 300px" property="itemName" label="项目名"></el-table-column>
        <el-table-column style="width: 50px" property="format" label="规格"></el-table-column>
        <el-table-column style="width: 50px" property="currentNum" label="数量"></el-table-column>
        <el-table-column style="width: 50px" property="price" label="单价"></el-table-column>
        <el-table-column style="width: 50px" property="totalprice" label="总金额"></el-table-column>
        <el-table-column style="width: 100px" property="type" label="类型"></el-table-column>
      </el-table>
      {{$store.getters.name}}-->
      <div>
        <div id="billmark">
          <div class="records-print-wrap" style="overflow: hidden;padding-bottom: 30px">
            <div class="print-content-title">
              <h3>山东省神经疾病协同创新中心</h3>
              <ul class="print-table-section print-dash-table">
                <li>
                  <span>姓名：{{ patient.patientName }}</span>
                  <span>性别：{{ patient.patientGender==0?'男':'女' }}</span>
                  <span>年龄：{{ patient.patientAge }}</span>
                </li>
                <li>
                  <span>身份证号：{{ patient.identificationNo }}</span>
                  <span>就诊号：{{ patient.patientMedicalRecordNo }}</span>
                  <span>手机号：{{ patient.phoneNo }}</span>
                </li>
                <li>
                  <span>家庭住址：{{ patient.patientHomeAdress }}</span>
                  <!-- <span></span>
                  <span></span> -->
                </li>
              </ul>
              <p></p>
            </div>
            <template v-if="printdata && printdata.length">
              <ul class="print-table-section">
                <li>
                  <span>项目名</span>
                  <span>规格</span>
                  <!-- <span v-show="isDrug">药品用法</span> -->
                  <span>数量</span>
                  <span v-if="noDrug != 1">单价</span>
                  <span v-if="noDrug == 1">单价(折)</span>
                  <span>总金额</span>
                  <span>类型</span>
                  <span v-if="noDrug == 2">使用方法</span>
                </li>
                <li v-for="(i, index) in printdata" :key="index">
                  <span>{{ i.itemName }}</span>
                  <span>{{ i.format }}</span>
                  <!-- <span v-show="isDrug">{{ i.medicalAdvice }}</span> -->
                  <span>{{ i.currentNum }}</span>
                  <span>{{ i.price }}</span>
                  <span>{{ i.totalprice }}</span>
                  <span>{{ i.type }}</span>
                  <span v-if="noDrug == 2">{{i.frequency == 1 ? '一天一次':'一天三次'}}
                      {{i.usageNum}}{{i.usageNumUnit == 1 ?'片':i.usageNumUnit == 2 ?'支':i.usageNumUnit == 2 ?'瓶':'克'}}/次</span>
                </li>
              </ul>
            </template>


            <div style="float:left;left: 150px">总金额：{{this.amount}} 元</div>
            <div style="float:right;margin-right: 150px">开方人：{{$store.getters.name}}</div>
          </div>
        </div>
        <div style="text-align: right">
          <el-button type="primary" @click="printmark">打印</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {listByRegistration} from '@/api/outpatient/feequery'
  import {deepClone, parseTime} from '@/utils'
  import printJS from "print-js";

  export default {
    name: 'Bill',
    props: ['patient', 'activeName'],
    watch: {
      'patient': function (newVal, oldVal) {
        this.patient = newVal
        console.log(this.patient)
        this.queryBill()
      },
      activeName(n, o) {
        if ("seventh" == n) {
          this.queryBill()
        }
      }
    },
    // inject: ['reload'],
    data() {
      return {
        noDrug:0,
        bill: [],
        isprintmark: false,  //创新中心打印;非创新中心打印
        printdata: [],
        amount:0.000,
        isDrug: false,  //是否是药品
      };
    },
    created() {

    },
    methods: {
      queryBill() {
        listByRegistration(this.patient.registrationId).then(res => {
          this.bill = res.data
          this.bill.forEach(item => {
            item.totalprice = item.price * item.currentNum
          })
        })
        this.sum()
      },
      sum() {
        this.bill.forEach(item => {
          if (item.status == 1) {
            this.alreadyPay = this.alreadyPay + item.totalprice
          } else if (item.status == 2 || item.status == 3 || item.status == 4) {
            this.didNotPay = this.didNotPay + item.totalprice
          }
        })
        this.amount = this.alreadyPay + this.didNotPay;
        let money1 = {
          itemName: '',
          format: '',
          currentNum: '',
          price: '',
          totalprice: '',
          type: ''
        }
        let money2 = {
          itemName: '',
          format: '',
          currentNum: '',
          price: '',
          totalprice: '',
          type: ''
        }
        let money3 = {
          itemName: '',
          format: '',
          currentNum: '',
          price: '',
          totalprice: '',
          type: ''
        }
        money1.itemName = "已缴费"
        money1.totalprice = this.alreadyPay
        money2.itemName = "未缴费"
        money2.totalprice = this.didNotPay
        money3.itemName = "总费用"
        money3.totalprice = this.amount

        this.bill.push(money1)
        this.bill.push(money2)
        this.bill.push(money3)
      },

      print(e) {
        const subOutputRankPrint = document.getElementById('bill12')
        const newContent = subOutputRankPrint.innerHTML
        const oldContent = document.body.innerHTML
        document.body.innerHTML = newContent
        window.print()
        // this.reload()
        window.location.reload()
        document.body.innerHTML = oldContent
        return false
      },

      //创新中心打印;非创新中心打印

      isprint(data) {
        this.noDrug = data
        let sumdata={
          itemName:'总金额',
          format:'',
          currentNum:'',
          price:'',
          totalprice:'',
          type:'',
        }
        let sum=0;
        this.printdata=[]
        this.printdata1 = deepClone(this.bill);
        if (data === 1) {
          this.isDrug=false
          this.printdata1.forEach(itme => {
            if (itme.price > 0) {
              this.printdata.push(itme)
            }
          })
        } else if (data === 0) {  //非创新中心检查打印
          this.isDrug=false
          this.printdata1.forEach(itme => {
            if (itme.price === 0 && (itme.type===0 || itme.type===1 || itme.type===2)) {
              this.printdata.push(itme)
            }
          })
        }else if (data === 2) {
          this.isDrug=true
          this.printdata1.forEach(itme => { //非创新中心药品打印
            if (itme.price === 0 && (itme.type===4 || itme.type===5)) {
              this.printdata.push(itme)
            }
          })
        }
        this.printdata.forEach(item => {
          if (item.type === 0) {
            item.type = '检查'
          } else if (item.type === 1) {
            item.type = '检验'
          } else if (item.type === 2) {
            item.type = '处置'
          } else if (item.type === 4) {
            item.type = '草药处方'
          } else if (item.type === 5) {
            item.type = '成药处方'
          }
        })
        this.printdata.forEach(item=>{
          sum+=item.totalprice;
        })
        sumdata.totalprice=sum
        /* this.printdata.push(sumdata)*/
        debugger
        console.log(this.printdata)
        this.amount=sum
        this.isprintmark = true
      },

      printmark(e) {
        const style = `@page {
          margin-top: 0;
          margin-bottom: 0;
        }
        @media print {
          ul, li, span, p, table, th, tr, td {
            padding: 0;
            margin: 0;
            list-style: none;
          }
          .print-dash-table {
            text-align: left;
            border: 1px dashed transparent !important;
            border-bottom: none !important;
          }
          .print-dash-table li {
            border-bottom: 1px dashed transparent !important;
          }
          .print-dash-table li span {
            justify-content: flex-start !important;
            border-right: 1px dashed transparent !important;
          }
          .print-dash-table li span:last-child {
            border-right: none !important;
          }
          .records-print-wrap {
            font-family: "宋体";
            color: #000;
            font-size: 16px;
          }
          .records-print-wrap .print-result-section {
            margin-bottom: 20px;
          }
          .records-print-wrap .print-result-section li {
            margin-bottom: 10px;
          }
          .records-print-wrap .print-result-section p {
            line-height: 1.5em;
          }
          .records-print-wrap .print-result-section p span {
            text-indent: 2em;
            display: inline-block;
            padding-right: 1em;
            text-align: left;
            font-weight: bold;
          }
          .records-print-wrap .section-title {
            margin-bottom: 20px;
            line-height: 1.5em;
          }
          .print-table-section {
            margin-bottom: 20px;
            text-align:center;
            line-height: 1.5em;
            border: 1px solid #000;
            border-bottom: none;
          }
          .print-table-section li {
            display: flex;
            min-height: 24px;
            border-bottom: 1px solid #000;
          }
          .print-table-section li:first-child {
            font-weight: bold;
          }
          .print-table-section span {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            border-right: 1px solid #000;
          }
          .print-table-section span:last-child {
            flex: 1;
            border: none;
          }
          .records-print-wrap .section-title span {
            text-indent: 2em;
            font-weight: bold;
          }
          .records-print-wrap .print-content-title {
            padding-bottom: 30px;
          }
          .records-print-wrap .print-content-title h3 {
            text-align: center;
            font-size: 20px;
            font-weight: bold;
            padding: 30px 0 50px;
          }
          .records-print-wrap .print-content-title p {
            line-height: 1.5em;
            border-bottom: 1px solid #000;
            display: flex;
          }
          .records-print-wrap .print-content-title span:nth-child(2) {
            flex: 1;
          }
        }`;
        printJS({
          printable: "billmark",
          type: "html",
          style,
          scanStyles: false,
        });
      },
    },
  }
</script>
<style>
  ul,
  li,
  span,
  p,table,th,tr,td {
    padding: 0;
    margin: 0;
    list-style: none;
  }
  .records-print-wrap {
    font-family: "宋体";
    color: #000;
    font-size: 16px;
  }
  .records-print-wrap .print-result-section {
    margin-bottom: 20px;
  }
  .records-print-wrap .print-result-section li {
    margin-bottom: 10px;
  }
  .records-print-wrap .print-result-section p {
    line-height: 1.5em;
  }
  .records-print-wrap .print-result-section p span {
    text-indent: 2em;
    display: inline-block;
    padding-right: 1em;
    text-align: left;
    font-weight: bold;
  }
  .print-table-section {
    margin-bottom: 20px;
    text-align:center;
    line-height: 1.5em;
    border: 1px solid #000;
    border-bottom: none;
  }
  .print-table-text {
    justify-content: start !important;
    text-align: left;
    text-indent: 2em;
  }
  .print-table-section li {
    display: flex;
    min-height: 24px;
    border-bottom: 1px solid #000;
  }
  /* .print-table-section li:first-child {
    font-weight: bold;
  } */
  .print-table-section li > span {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    border-right: 1px solid #000;
    white-space: normal;
    word-break: break-all;
  }
  .print-table-section li > span:last-child {
    flex: 1;
    border: none;
  }
  .print-table-section li > span span {
    font-weight: bold;
    white-space: nowrap;
  }
  .records-print-wrap .section-title {
    margin-bottom: 20px;
    line-height: 1.5em;
  }
  .records-print-wrap .section-title span {
    text-indent: 2em;
    font-weight: bold;
  }
  .records-print-wrap .print-content-title {
    padding-bottom: 30px;
  }
  .records-print-wrap .print-content-title h3 {
    text-align: center;
    font-size: 20px;
    padding: 30px 0 50px;
  }
  .records-print-wrap .print-content-title p {
    line-height: 1.5em;
    border-bottom: 1px solid #000;
    display: flex;
  }
  .records-print-wrap .print-content-title span:nth-child(2) {
    flex: 1;
  }

  .print-dash-table {
    text-align: left;
    border: 1px dashed transparent !important;
    border-bottom: none !important;
  }
  .print-dash-table li {
    border-bottom: 1px dashed transparent !important;
  }
  .print-dash-table li span {
    justify-content: flex-start !important;
    border-right: 1px dashed transparent !important;
  }
  .print-dash-table li span:last-child {
    border-right: none !important;
  }
</style>
