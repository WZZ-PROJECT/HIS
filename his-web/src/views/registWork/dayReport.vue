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
                :picker-options="pickerOptions"
                placeholder="截止日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="queryInvoiceInfo">搜索</el-button>
              <el-button type="primary" @click="dailySettle">结算报账</el-button>
              <el-button type="primary" @click="print">打印</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-header>
      <el-main style="padding-top:0;margin-top:58px">
        <el-tabs v-model="activeName">
          <el-tab-pane label="发票信息" name="first">
            <el-table :data="InvoiceInfoList" stripe>
              <el-table-column label="发票号" prop="invoiceNo"></el-table-column>
              <el-table-column label="发票金额" prop="amount"></el-table-column>
              <el-table-column label="发票时间" prop="createTime"></el-table-column>
<!--              <el-table-column label="账单号" prop="billId"></el-table-column>-->
              <el-table-column label="支付类型" prop="settlementCatName"></el-table-column>
              <el-table-column label="收费员" prop="operatorName"></el-table-column>
            </el-table>
            <pagination :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="queryInvoiceInfo" />
          </el-tab-pane>
          <el-tab-pane label="日结单" name="second" style="text-align:center;">
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
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import {parseTime,formatTime} from '@/utils'
import {queryRecentEndDatetime,queryInvoiceInfo,dailySettle,querySettleDetailById} from '@/api/dayreport'
import { selectAll } from '@/api/signature'
import Pagination from '@/components/Pagination'
import { parse } from 'path';
import printJS from "print-js";
export default {
  components: {Pagination},
  data(){
    return{
      //分页信息
      total:0,
      // 查询所需参数
      listQuery: {
        cashierId:'',
        startDatetime:'',
        endDatetime:'',
        pageSize: 10,
        pageNum: 1
      },
      hide:true,
      printtable:false,
      rijiedan:false,
      reportdetail:[],
      InvoiceInfoList:[],
      starttime:new Date(),
      endtime:'',
      activeName:'first',
       pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          }
      }
    };
  },
  created(){
    this.handleSubmit();
    queryRecentEndDatetime(this.$store.getters.id).then(res=>{
      this.starttime = parseTime(res.data)
    })
  },
  methods:{
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
            justify-content: flex-start !important;
            border-bottom: 1px dashed transparent !important;
          }
          .print-dash-table li span {
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
    //   document.body.innerHTML = oldContent
    //   return false
    // },
    querySettleDetailById(data){
      querySettleDetailById(data).then(res=>{
        this.reportdetail = res.data
        // 检查金额
        this.reportdetail.checkAllAmount = res.data.checkAmount
        + res.data.testAmount
        + res.data.dispositionAmount
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
    },
    queryInvoiceInfo(){
      if (this.check() === false) {
        return
      }
      this.listQuery.cashierId = this.$store.getters.id
      this.listQuery.startDatetime = parseTime(this.starttime)
      this.listQuery.endDatetime = parseTime(this.endtime)
      queryInvoiceInfo(this.listQuery).then(res=>{
        this.InvoiceInfoList = res.data.list
        this.total = res.data.total
        this.InvoiceInfoList.forEach(item=>{
          item.createTime = parseTime(item.createTime)
        })
      })
    },
    dailySettle(){
      if (this.InvoiceInfoList.length <= 0) {
        this.$notify({
          title: '警告',
          message: '暂无数据，无法生成日结单',
          type: 'warning',
          duration: 2000
        })
        return false
      }
      let data = {}
      data.cashierId =this.$store.getters.id
      data.startDatetime = parseTime(this.starttime)
      data.endDatetime = parseTime(this.endtime)
      dailySettle(data).then(res=>{
        this.$notify({
          title: '成功',
          message: '已生成日结单',
          type: 'success',
          duration: 2000
        })

        this.rijiedan = true
        this.querySettleDetailById(res.data)
        this.printtable = true
        this.handleSubmit();
        this.hide = false;
      })

    },
    handleSubmit() {
      selectAll().then(res=>{
        this.$refs.sign.getStar(res.data.typeName,res.data.name,res.data.code)
        this.img = this.$refs.sign.submit(80, 50);
      })
    },
    //检查校验
    check () {
      if (this.starttime === null || this.starttime === '') {
        this.$notify({
          title: '警告',
          message: '请选择起始时间进行查询',
          type: 'warning',
          duration: 2000
        })
        return false
      }
      if (this.endtime === null || this.endtime === '') {
        this.$notify({
          title: '警告',
          message: '请选择截止时间时间进行查询',
          type: 'warning',
          duration: 2000
        })
        return false;
      }
    }
  }
}
</script>
