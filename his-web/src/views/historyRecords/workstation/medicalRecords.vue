<template>
  <div>
    <div id="records">
      <div class="records-print-wrap" style="overflow: hidden;padding-bottom: 30px">
        <!-- <t1r>
          <td style="width:100px">病例首页</td>
        </t1r> -->
        <!-- <p class="section-title"><span>病例首页：</span></p>
      <ul>
        <li>
          <p><span>主诉：</span>{{ dmsCaseHistory.chiefComplaint }}</p>
        </li>
        <li>
          <p>
            <span>现病史：</span>{{ dmsCaseHistory.historyOfPresentIllness }}
          </p>
        </li>
        <li>
          <p>
            <span>现病治疗情况：</span>{{ dmsCaseHistory.historyOfTreatment }}
          </p>
        </li>
        <li>
          <p><span>既往史：</span>{{ dmsCaseHistory.pastHistory }}</p>
        </li>
        <li>
          <p><span>过敏史：</span>{{ dmsCaseHistory.allergies }}</p>
        </li>
        <li>
          <p><span>体格检查：</span>{{ dmsCaseHistory.healthCheckup }}</p>
        </li>
        <li>
          <p>
            <span>发病时间：</span
            >{{
              dmsCaseHistory.startDate &&
              dmsCaseHistory.startDate.substring(0, 10)
            }}
          </p>
        </li>
      </ul>
      <p class="section-title"><span>评估诊断：</span></p>
      <table>
        <tr>
          <th>ICD编码</th>
          <th>名称</th>
          <th>编码</th>
        </tr>
        <tr v-for="(i, index) in 5" :key="index">
          <td>{{ i.icd }}</td>
          <td>{{ i.name }}</td>
          <td>{{ i.code }}</td>
        </tr>
      </table>
      <p class="section-title"><span>检查申请：</span></p>
      <table>
        <tr>
          <th>项目编码</th>
          <th>项目名称</th>
          <th>执行科室</th>
          <th>单位</th>
          <th>单价</th>
        </tr>
        <tr v-for="(i, index) in record" :key="index">
          <td>{{ i.code }}</td>
          <td>{{ i.name }}</td>
          <td>{{ i.deptName }}</td>
          <td>{{ i.format }}</td>
          <td>{{ i.price }}</td>
        </tr>
        <tr>
          <th>结果</th>
          <th>图片</th>
        </tr>
        <tr v-for="(i, index) in record" :key="index">
          <td>{{ i.checkResult }}</td>
          <td><img :src="i.resultImgUrlList" /></td>
        </tr>
      </table>
      <p class="section-title"><span>检验申请：</span></p>
      <table>
        <tr>
          <th>项目编码</th>
          <th>项目名称</th>
          <th>执行科室</th>
          <th>单位</th>
          <th>单价</th>
        </tr>
        <tr v-for="(i, index) in record1" :key="index">
          <td>{{ i.code }}</td>
          <td>{{ i.name }}</td>
          <td>{{ i.deptName }}</td>
          <td>{{ i.format }}</td>
          <td>{{ i.price }}</td>
        </tr>
        <tr>
          <th>结果</th>
          <th>图片</th>
        </tr>
        <tr v-for="(i, index) in record1" :key="index">
          <td>{{ i.checkResult }}</td>
          <td><img :src="i.resultImgUrlList" /></td>
        </tr>
      </table> -->
        <div class="print-content-title">
          <h3>山东省神经疾病协同创新中心</h3>
          <ul class="print-table-section print-dash-table">
            <li>
              <span>姓名：{{ patient.patientName }}</span>
              <span>性别：{{ patient.patientGender==0?'男':'女' }}</span>
              <span>年龄：{{ patient.patientAge }}</span>
            </li>
            <li>
              <span rowspan=2>身份证号：{{ patient.identificationNo }}</span>
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

        <div v-if="dmsCaseHistory != null && dmsCaseHistory!='' && dmsCaseHistory!= undefined">
          <p class="section-title">
            <span>门诊确诊：</span>
          </p>
          <ul class="print-result-section">
            <li>
              <p><span>主诉：</span>{{ dmsCaseHistory.chiefComplaint}}</p>
            </li>
            <li>
              <p><span>现病史：</span>{{ dmsCaseHistory.historyOfPresentIllness }}</p>
            </li>
            <li>
              <p><span>现病治疗情况：</span>{{ dmsCaseHistory.historyOfTreatment }}</p>
            </li>

            <li>
              <p><span>既往史：</span>{{ dmsCaseHistory.pastHistory }}</p>
            </li>
            <li>
              <p><span>过敏史：</span>{{ dmsCaseHistory.allergies }}</p>
            </li>
            <li>
              <p><span>体格检查：</span>{{ dmsCaseHistory.healthCheckup }}</p>
            </li>

          </ul>
        </div>

        <div v-if="dmsCaseHistory.checkResult!=null && dmsCaseHistory.checkResult!=''">
          <ul class="print-result-section">
            <li>
              <p><span>检查结果：</span>{{ dmsCaseHistory.checkResult }}</p>
            </li>
            <li>
              <p><span>检验结果：</span>{{ dmsCaseHistory.testResult }}</p>
            </li>
            <li>
              <p><span>医嘱：</span>{{ dmsCaseHistory.testAdvice }}</p>
            </li>
          </ul>
        </div>

        <div v-if="this.records != null && this.records!='' && this.records!= undefined">
          <p class="section-title">
            <span>评估诊断：</span>
          </p>
          <ul class="print-table-section">
            <li>
              <span>ICD编码</span>
              <span>名称</span>
              <span>编码</span>
            </li>
            <li v-for="(i, index) in this.records" :key="index">
              <span>{{ i.icd }}</span>
              <span>{{ i.name }}</span>
              <span>{{ i.code }}</span>
            </li>
          </ul>
        </div>

        <!-- <p class="section-title"><span>成药处方：</span></p>
      <table>
        <tr>
          <th>处方名</th>
          <th>总金额</th>
        </tr>
        <tr v-for="(i, index) in prescriptionList1" :key="index">
          <td>{{ i.name }}</td>
          <td>{{ i.amount }}</td>
        </tr>
      </table>
      <p class="section-title"><span>草药处方：</span></p>
      <table>
        <tr>
          <th>处方名</th>
          <th>总金额</th>
        </tr>
        <tr v-for="(i, index) in prescriptionList2" :key="index">
          <td>{{ i.name }}</td>
          <td>{{ i.amount }}</td>
        </tr>
      </table>
      <p class="section-title"><span>处置申请：</span></p>
      <table>
        <tr>
          <th>项目编码</th>
          <th>项目名称</th>
          <th>执行科室</th>
          <th>单位</th>
          <th>单价</th>
        </tr>
        <tr v-for="(i, index) in record2" :key="index">
          <td>{{ i.code }}</td>
          <td>{{ i.name }}</td>
          <td>{{ i.deptName }}</td>
          <td>{{ i.format }}</td>
          <td>{{ i.price }}</td>
        </tr>
      </table> -->
        <template v-if="bill1 && bill1.length">
          <p class="section-title"><span>患者账单：</span></p>
          <ul class="print-table-section">
            <li>
              <span>项目名</span>
              <span>规格</span>
              <span>数量</span>
              <span>单价</span>
              <span>总金额</span>
            </li>
            <li v-for="(i, index) in bill1" :key="index">
              <span>{{ i.itemName }}</span>
              <span>{{ i.format }}</span>
              <span>{{ i.currentNum }}</span>
              <span>{{ i.price }}</span>
              <span>{{ i.totalprice }}</span>
            </li>
          </ul>
        </template>
        <template v-if="record && record.length">
          <p class="section-title"><span>知情告知：</span></p>
          <ul class="print-table-section">
            <li>
              <span>知情告知名字</span>
              <span>知情告知内容</span>
            </li>
            <li v-for="(i, index) in record" :key="index">
              <span class="print-table-text">{{ i.name }}</span>
              <span class="print-table-text">{{ i.content }}</span>
            </li>
          </ul>
        </template>

        <div style="float:right;margin-right: 150px">签字：</div>
        <!-- <img
          :src="img"
          alt
          style="width: 35%; height: 35%; margin-left: 65%; margin-top: -75%"
        /> -->
      </div>
    </div>
    <div style="text-align: right">
      <el-button type="primary" @click="print1">打印</el-button>
    </div>
  </div>
</template>
<script>
  import { getNondrugList } from "@/api/non_drug";
  import { Clinical } from "@/api/outpatient/dmscase"; /*病例首页*/
  import { parseList } from "@/api/diagnosis"; /*评估诊断*/
  import {selectEndCaseHistory} from '@/api/outpatient/dmscase'
  import { list } from "@/api/outpatient/nondrugapply"; /*检查申请*/
  import { listByReg1 } from "@/api/outpatient/prescription";
  import { listByRegistration } from "@/api/outpatient/feequery";
  import { Promise } from "q"; /*检查检验*/
  import { selectFamiliarInform } from "@/api/outpatient/dmscase"; /*加载知情告知增删改查*/
  import printJS from "print-js";

  export default {
    props: ["patient", "activeName", "print"],
    name: "medicalRecords",

    data() {
      return {
        dmsCaseHistory: {}, //病例首页
        records: [], //诊断
        qrecord: [], //检查
        record: [], //检查已缴费的
        checkList: [],
        qrecord1: [], //检验
        record1: [], //检验已缴费的
        checkList1: [],

        qrecord2: [], //处置
        record2: [], //处置已缴费的
        checkList2: [],

        prescriptionList1: [],
        prescriptionList2: [],

        bill: [],
        bill1: [],
        amount: 0,
        alreadyPay: 0,
        didNotPay: 0,

        listQuery: {
          code: null,
          name: null,
          format: null,
          price: null,
          expClassId: null,
          deptId: null,
          mnemonicCode: null,
          recordType: 1,
          createDate: null,
          status: 1,
          pageSize: 1000,
          pageNum: 1,
        },

        listQuery1: {
          code: null,
          name: null,
          format: null,
          price: null,
          expClassId: null,
          deptId: null,
          mnemonicCode: null,
          recordType: 2,
          createDate: null,
          status: 1,
          pageSize: 1000,
          pageNum: 1,
        },

        listQuery2: {
          code: null,
          name: null,
          format: null,
          price: null,
          expClassId: null,
          deptId: null,
          mnemonicCode: null,
          recordType: 3,
          createDate: null,
          status: 1,
          pageSize: 1000,
          pageNum: 1,
        },
        record: {},
      };
    },
    created() {
      /*Promise.all([
          this.getNondrugList().then(()=>{
            this.getmodel()
          }),
          this.getNondrugList1().then(()=>{
            this.getmodel()
          }),
          this.getNondrugList2().then(()=>{
            this.getmodel()
          })
        ]),
          this.getfreqList()*/
      this.listRecord();
      this.listRecord1();
      this.selectEndCaseHistory();
      this.listByReg1()
      this.listByReg2()
      this.listRecord2()
      this.queryBill()
      this.selectFamiliarInform()
    },
    watch: {
      patient: {
        deep: true,
        handler(newVal, oldVal) {
          console.log(newVal)
          this.patient = newVal;
          this.listRecord();
          this.listRecord1();
          this.selectEndCaseHistory();
          this.listByReg1();
          this.listByReg2();
          this.listRecord2();
          this.queryBill();
          this.selectFamiliarInform();
        }
      },
      print(n) {
        if (n) {
          this.$nextTick(() => {
            this.listRecord();
            this.listRecord1();
            this.selectEndCaseHistory();
            this.listByReg1();
            this.listByReg2();
            this.listRecord2();
            this.queryBill();
            this.selectFamiliarInform();
          })
        }
      }
    },
    activated() {
      console.log(1)
    },

    methods: {
      /*    Clinical() {
            this.dmsCaseHistory={}
            this.records=[]
            Clinical(this.patient.registrationId).then((res) => {
              this.dmsCaseHistory = res.data.dmsCaseHistoryList[0];
              parseList(this.dmsCaseHistory.priliminaryDiseIdList).then((pars) => {
                this.records = pars.data;
              });
            });
          },*/

      selectEndCaseHistory() {
        this.dmsCaseHistory={}
        this.records=[]
        selectEndCaseHistory(this.patient.registrationId).then(res => {
          this.dmsCaseHistory= res.data.dmsCaseHistoryList[0];
          parseList(this.dmsCaseHistory.definiteDiseStrList).then(res => {
            this.records = res.data;
          })
        })
      },

      async listRecord() {
        this.qrecord=[]
        this.checkList=[]
        list(this.patient.registrationId, 0).then((res) => {
          this.qrecord = res.data;
          this.qrecord.forEach((item) => {
            this.checkList.filter((check) => {
              if (check.id === item.noDrugId) {
                item.code = check.code;
                item.name = check.name;
                item.format = check.format;
                item.price = check.price;
              }
            });
            item.deptName = item.excuteDeptName;
          });

          this.qrecord.forEach((item) => {
            if (item.status == 2 || item.status == 3 || item.status == 4) {
              this.record.push(item);
            }
          });
        });
      },

      async getNondrugList() {
        this.checkList=[]
        const response = await getNondrugList(this.listQuery);
        this.checkList = response.data.list;
      },

      async listRecord1() {
        this.qrecord1=[]
        this.record1=[]
        list(this.patient.registrationId, 1).then((res) => {
          this.qrecord1 = res.data;
          this.qrecord1.forEach((item) => {
            this.checkList1.filter((check) => {
              if (check.id === item.noDrugId) {
                item.code = check.code;
                item.name = check.name;
                item.format = check.format;
                item.price = check.price;
              }
            });
            item.deptName = item.excuteDeptName;
          });
          this.qrecord1.forEach((item) => {
            if (item.status == 2 || item.status == 3 || item.status == 4) {
              this.record1.push(item);
            }
          });
        });
      },

      async getNondrugList1() {
        this.checkList1=[]
        const response = await getNondrugList(this.listQuery1);
        this.checkList1 = response.data.list;
      },

      async listByReg1() {
        this.prescriptionList1=[]
        listByReg1(this.patient.registrationId).then((res) => {
          this.prescriptionList1 = res.data;
        });
      },

      async listByReg2() {
        this.prescriptionList2=[]
        listByReg1(this.patient.registrationId).then((res) => {
          this.prescriptionList2 = res.data;
        });
      },

      async listRecord2() {
        this.qrecord2=[]
        list(this.patient.registrationId, 2).then((res) => {
          this.qrecord2 = res.data;
          this.qrecord2.forEach((item) => {
            this.checkList2.filter((check) => {
              if (check.id === item.noDrugId) {
                item.code = check.code;
                item.name = check.name;
                item.format = check.format;
                item.price = check.price;
              }
            });
            item.deptName = item.excuteDeptName;
          });
          this.qrecord2.forEach((item) => {
            if (item.status == 2 || item.status == 3 || item.status == 4) {
              this.record2.push(item);
            }
          });
        });
      },

      async getNondrugList2() {
        const response = await getNondrugList(this.listQuery2);
        this.checkList2 = response.data.list;
      },

      queryBill() {
        this.bill=[]
        this.bill1=[]
        listByRegistration(this.patient.registrationId).then((res) => {
          this.bill = res.data;
          this.bill.forEach((item) => {
            item.totalprice = item.price * item.currentNum;
          });
          this.bill.forEach((item) => {
            if (item.status == 2 || item.status == 3 || item.status == 4) {
              this.bill1.push(item);
            }
          });
          /*this.sum();*/
        });
      },

      sum() {
        this.bill1.forEach((item) => {
          if (item.status == 2 || item.status == 3 || item.status == 4) {
            this.alreadyPay = this.alreadyPay + item.totalprice;
          } else if (item.status == 1) {
            this.didNotPay = this.didNotPay + item.totalprice;
          }
        });
        if(this.alreadyPay!=0 || this.didNotPay!=0){
          this.amount = this.alreadyPay + this.didNotPay;
          let money1 = {
            itemName: "",
            format: "",
            currentNum: "",
            price: "",
            totalprice: "",
            type: "",
          };
          let money2 = {
            itemName: "",
            format: "",
            currentNum: "",
            price: "",
            totalprice: "",
            type: "",
          };
          let money3 = {
            itemName: "",
            format: "",
            currentNum: "",
            price: "",
            totalprice: "",
            type: "",
          };
          money1.itemName = "已缴费";
          money1.totalprice = this.alreadyPay;
          money1.status = 3;
          money2.itemName = "未缴费";
          money2.totalprice = this.didNotPay;
          money3.itemName = "总费用";
          money3.totalprice = this.amount;
          this.bill1.push(money1);
          this.bill1.push(money2);
          this.bill1.push(money3);
        }
      },

      //知情告知
      selectFamiliarInform() {
        this.record = {};
        let data = {
          registrationId: "",
          id: "",
          name: "",
          content: "",
        };
        data.registrationId = this.patient.registrationId;
        selectFamiliarInform(data).then((res) => {
          this.record = res.data;

        });
      },
      print1(e) {
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
          .print-table-section li:first-child {
            font-weight: bold;
          }
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
          printable: "records",
          type: "html",
          style,
          scanStyles: false,
        });
      },
    },
  };
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
