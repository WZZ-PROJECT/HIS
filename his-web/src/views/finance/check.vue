<template>
  <div>
    <el-container>
      <el-header style="padding:0 0 0 0">
        <aside style="margin:0 0 0 0">
          <el-button disabled style="color:black">门诊日流水</el-button>
        </aside>
        <div style="margin-left:30px;margin-top:15px">
          <el-form inline>
            <el-form-item label="起始日期">
               <el-date-picker
                type="datetime"
                v-model="starttime"
                placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="截止日期">
               <el-date-picker
                v-model="endtime"
                type="datetime"
                placeholder="截止日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="listDailySettleListRecord">查询</el-button>
              <el-button type="primary" @click="print">打印</el-button>
              <el-button type="danger" @click="verifySettle">核对</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-header>
      <el-container style="margin-top:58px;border-style: dotted;border-width: 1px 0px 0px 0px;border-color:#C0C0C0;">
      <el-aside width="25%" style="border-style: dotted;border-width: 0px 1px 0px 0px;border-color:#C0C0C0;background:white">
        <el-tabs value="first">
          <el-tab-pane label="日流水信息列表" name="first">
            <el-table :data="reportList" style="margin-top:-15px" highlight-current-row @row-click="selectreport">
              <el-table-column label="收款员" prop="operatorName"></el-table-column>
              <el-table-column label="创建时间" width="150px" prop="createDatetme"></el-table-column>
              <el-table-column label="日结状态">
                <template slot-scope="scope">
                  <el-tag type="success" v-if="scope.row.verifyStatus===1">已核对</el-tag>
                  <el-tag type="warning" v-if="scope.row.verifyStatus!==1">未核对</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-aside>
      <el-main style="padding-top:0;">

        <el-tabs v-model="activeName">
          <el-tab-pane label="日结单" name="first" style="text-align:center;">
            <div id="table" v-if="rijiedan" style="width:1000px;text-align:center;">
              <span>济南慈光诊所日流水</span>
              <table cellpadding="5" border="1" style="border:#DEDEDE;padding:1px;width:100%;height:100%" cellspacing="0">
                <tr>
                  <td style="width:200px">日结时间范围</td>
                  <td colspan="8">{{reportdetail.startDatetime}} —— {{reportdetail.endDatetime}}</td>
                </tr>
                <tr>
                  <td >报账人名称</td>
                  <td>{{reportdetail.cashierName}}</td>
                  <td>制表时间</td>
                  <td colspan="6">{{reportdetail.createDatetime}}</td>
                </tr>
                <tr>
                  <td style="width:30%" rowspan="1">消费金额</td>
                  <td style="width:8%">检查金额</td>
                  <td style="width:10%">{{reportdetail.checkAllAmount}}</td>
                  <td style="width:8%">挂号金额</td>
                  <td style="width:10%">{{reportdetail.registrationAmount}}</td>
                  <td style="width:8%"></td>
                  <td style="width:10%" colspan="2"></td>
                </tr>
                <tr>
                  <!--                  <td>成药金额</td>-->
                  <!--                  <td>{{reportdetail.medicineAmount}}</td>-->
                  <!--                  <td>草药金额</td>-->
                  <!--                  <td>{{reportdetail.herbalAmount}}</td>-->
                  <!--                  <td>处置金额</td>-->
                  <!--                  <td colspan="6">{{reportdetail.dispositionAmount}}</td>-->
                  <td style="width:30%">总消费金额</td>
                  <td colspan="7">{{reportdetail.totalConsumptionAmount}}</td>
                </tr>
                <tr>
                  <td style="width:30%" rowspan="1">充值方式</td>
                  <td>微信金额</td>
                  <td>{{reportdetail.wechatAmount}}</td>
                  <td>信用卡金额</td>
                  <td>{{reportdetail.creditCardAmount}}</td>
                  <td>现金金额</td>
                  <td colspan="2">{{reportdetail.cashAmount}}</td>
                </tr>
                <tr>
                  <td style="width:30%">总充值金额</td>
                  <td colspan="7">{{reportdetail.totalRechargeAmount}}</td>
                </tr>
                <tr>
                  <td>退款金额</td>
                  <td colspan="7">{{reportdetail.otherAmount}}</td>
                </tr>
              </table>
              <!--              <img :src="img" alt style="width:35%;height:35%;margin-left: 65%;margin-top:-75%">-->
            </div>
          </el-tab-pane>
<!--          <avue-sign ref="sign" v-if="hide" v-show="printtable"></avue-sign>-->
          <el-tab-pane label="发票信息" name="second">
            <el-table :data="InvoiceInfoList" stripe>
              <el-table-column label="发票号" prop="invoiceNo"></el-table-column>
              <el-table-column label="发票金额" prop="amount"></el-table-column>
              <el-table-column label="发票时间" prop="createTime"></el-table-column>
<!--              <el-table-column label="账单号" prop="billId"></el-table-column>-->
              <el-table-column label="支付类型" prop="settlementCatName"></el-table-column>
              <el-table-column label="收费员" prop="operatorName"></el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
import {listDailySettleListRecord,querySettleDetailById,queryInvoiceInfoBySettleId,verifySettle} from '@/api/dayreport'
import {parseTime,formatTime} from '@/utils'
import { selectAll } from '@/api/signature'
import printJS from "print-js";
export default {
  data(){
    return{
      hide:true,
      printtable:false,
      reportdetail:{},
      InvoiceInfoList:[],
      reportList:[],
      starttime: new Date(new Date().setHours(0, 0, 0, 0)),
      endtime:'',
      activeName:'first',
      test:[
        {
          id:111,
          name:222,
        },
        {
          id:111,
          name:222,
        },
        {
          id:111,
          name:222,
        },
        {
          id:111,
          name:222,
        },
      ],
      nowreport:{}
    };
  },
  created() {
    this.handleSubmit();
  },
  methods:{
    handleClick(){
      this.$notify({
        title: '暂无数据',
        message: '暂无数据！',
        type: 'warning',
        duration: 2000
      })
      return false;
    },
    verifySettle(){
      let data = {}
      data.verifyOperatorId = this.$store.getters.id
      data.settleRecordId = this.nowreport.settleRecordId
      verifySettle(data).then(res=>{
        this.$notify({
          title: '成功',
          message: '已核对！',
          type: 'success',
          duration: 2000
        })
        this.listDailySettleListRecord()
      })
    },
    print(e) {
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
        printable: "table",
        type: "html",
        style,
        scanStyles: false,
      });
    },
    // print(e){
    //   const subOutputRankPrint = document.getElementById('table')
    //   const newContent = subOutputRankPrint.innerHTML
    //   const oldContent = document.body.innerHTML
    //   document.body.innerHTML = newContent
    //   window.print()
    //   window.location.reload()
    //   document.body.innerHTML = oldContent
    //   return false
    // },
    listDailySettleListRecord(){
      this.printtable = false
      let data = {}
      data.cashierId = this.$store.getters.id
      data.startDatetime = parseTime(this.starttime)
      data.endDatetime = parseTime(this.endtime)
      listDailySettleListRecord(data).then(res=>{
        this.reportList = res.data
        if (res.data.size<=0) {
          this.handleClick()
        }
          this.reportList.forEach(item=>{
            item.createDatetme = parseTime(item.createDatetme)
          })
      })
    },
    selectreport(val){
      this.nowreport = val
      this.printtable = true
      querySettleDetailById(val.settleRecordId).then(res=>{
        this.reportdetail = res.data
        // 总消费金额
        this.reportdetail.totalConsumptionAmount
          = res.data.checkAmount
          + res.data.testAmount
          + res.data.registrationAmount
          + res.data.medicineAmount
          + res.data.herbalAmount
          + res.data.dispositionAmount
        // 总充值金额
        this.reportdetail.totalRechargeAmount
          = res.data.insuranceAmount
          + res.data.bankCardAmount
          + res.data.alipayAmount
          + res.data.wechatAmount
          + res.data.creditCardAmount
          + res.data.cashAmount
        this.reportdetail.startDatetime = parseTime(this.reportdetail.startDatetime)
        this.reportdetail.endDatetime = parseTime(this.reportdetail.endDatetime)
        this.reportdetail.createDatetime = parseTime(this.reportdetail.createDatetime)
      })
      queryInvoiceInfoBySettleId(val.settleRecordId).then(res=>{
        this.InvoiceInfoList = res.data
        this.InvoiceInfoList.forEach(item=>{
          item.createTime = parseTime(item.createTime)
        })
        this.handleSubmit();
        this.hide = false;
      })
    },
    handleSubmit() {
      selectAll().then(res=>{
        this.$refs.sign.getStar(res.data.typeName,res.data.name,res.data.code)
        this.img = this.$refs.sign.submit(80, 50);
      })

    }
  }
}
</script>
