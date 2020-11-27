<template>
  <!-- 门诊挂号 -->
  <div>
    <aside>
      <span style="font-family: '微软雅黑'; font-size: 14px">门诊挂号</span>
      <!-- <el-input
         size="mini"
         v-show="isshow"
         v-model="dmsRegistrationParam.invoiceNo"
         placeholder="发票号"
         style="width: 200px; margin-left: 30px"
         class="filter-item"
       ></el-input>-->
      <el-input
        size="mini"
        v-show="dialogFormVisible || refundVisible"
        v-model="invoiceNo"
        placeholder="发票号"
        style="width: 200px; margin-left: 30px"
        class="filter-item"
      ></el-input>
      <!--<el-button
        style="float: right; margin-left: 20px"
        type="text"
        size="medium"
        @click="reprint"
        ><i class="el-icon-circle-plus" />重打</el-button>-->
      <el-button
        style="float: right; margin-left: 20px"
        type="text"
        size="medium"
        @click="selectPatientByIdNo"
      ><i class="el-icon-upload"/>读卡
      </el-button
      >
      <el-button
        style="float: right; margin-left: 20px"
        type="text"
        size="medium"
        @click="guahao"
      ><i class="el-icon-circle-plus"/>挂号
      </el-button
      >
      <el-button
        style="float: right; margin-left: 20px"
        type="text"
        size="medium"
        @click="qingkong"
      ><i class="el-icon-circle-plus"/>清空
      </el-button
      >
    </aside>
    <div>
      <transition name=".el-fade-in-linear">
        <el-form
          v-if="isshow"
          label-position="left"
          :model="dmsRegistrationParam"
          ref="dmsRegistrationParam"
          label-width="80px"
          :inline="true"
          class="demo-form-inline"
          style="
            padding: 0 0 0 100px;
            border-style: solid;
            border-width: 0px 0px 1px 0px;
            border-color: #c0c0c0;
          "
          :rules="rules"
        >
          <el-form-item
            style="width: 300px"
            label="身份证号"
            prop="identificationNo"
          >
            <el-input
              placeholder="身份证号"
              v-model="dmsRegistrationParam.identificationNo"
              @blur="doChange()"
            ></el-input>
          </el-form-item>
          <el-form-item
            style="width: 300px"
            :inline="true"
            label="姓名"
            prop="name"
          >
            <el-input
              v-model="dmsRegistrationParam.name"
              placeholder="姓名"
            ></el-input>
          </el-form-item>
          <el-form-item
            style="width: 300px"
            :inline="true"
            label="出生日期"
            prop="dateOfBirth"
          >
            <el-input
              v-model="dmsRegistrationParam.dateOfBirth"
              placeholder="出生日期"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item style="width: 300px" label="性别" prop="gender">
            <el-select
              placeholder="性别"
              v-model="dmsRegistrationParam.gender"
              style="width: 100px"
            >
              <el-option
                v-for="item in [
                  { label: '男', value: 0 },
                  { label: '女', value: 1 },
                ]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            style="width: 300px"
            label="家庭住址"
            prop="homeAddress"
          >
            <el-input
              placeholder="家庭住址"
              v-model="dmsRegistrationParam.homeAddress"
            ></el-input>
          </el-form-item>
          <el-form-item style="width: 300px" label="联系方式" prop="phoneNo">
            <el-input
              placeholder="联系方式"
              v-model="dmsRegistrationParam.phoneNo"
            ></el-input>
          </el-form-item>
          <el-form-item style="width: 300px" label="挂号科室" prop="deptId">
            <el-select
              placeholder="挂号科室"
              filterable
              @change="chooseRegister"
              v-model="dmsRegistrationParam.deptId"
              style="width: 180px"
            >
              <el-option
                v-for="item in alldept"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            style="width: 300px"
            label="挂号级别"
            prop="RegisterRnak"
          >
<!--            <el-select-->
<!--              placeholder="挂号级别"-->
<!--              filterable-->
<!--              v-model="dmsRegistrationParam.RegisterRnak"-->
<!--              @change="chooseRegister"-->
<!--              style="width: 180px"-->
<!--            >-->
            <el-select
                placeholder="挂号级别"
                filterable
                v-model="dmsRegistrationParam.registeredLevel"
                style="width: 180px"
              >
              <el-option
                v-for="item in RegisterRank"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item
            style="width: 300px"
            label="挂号日期"
            prop="attendanceDate"
          >
            <el-date-picker
              style="width: 180px"
              @change="chooseRegister"
              v-model="dmsRegistrationParam.attendanceDate"
              align="right"
              type="date"
              placeholder="选择日期"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item style="width: 300px" label="午别" prop="noon">
            <el-select
              placeholder="默认"
              :disabled="regdisabled"
              style="width: 100px"
              @change="choosenoon"
              v-model="dmsRegistrationParam.noon"
            >
              <el-option label="上午" value="0"></el-option>
              <el-option label="下午" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item style="width: 300px" label="看诊医生"><!--prop="skdId"-->
            <el-select
              style="width: 180px"
              placeholder="默认"
              v-model="dmsRegistrationParam.skdId"
              :disabled="regdisabled2"
              @change="listTime"
            >
              <el-option
                v-for="item in doctList"
                :key="item.skdId"
                :label="item.name"
                :value="item.skdId"
              />
            </el-select>
          </el-form-item>

          <el-form-item style="width: 300px" label="预约时间"> <!--prop="time"-->
            <el-select
              style="width: 180px"
              placeholder="默认"
              v-model="dmsRegistrationParam.time"
              :disabled="regdisabled2"
            >
              <el-option
                v-for="item in timeList"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
          </el-form-item>

          <el-form-item style="width: 300px" label="应收金额" prop="amount">
            <el-input
              placeholder="应收金额"
              style="width: 180px; color: black"
              v-model="dmsRegistrationParam.amount"
            ></el-input>
          </el-form-item>
          <el-form-item
            style="width: 300px"
            label="支付方式"
            prop="settlementCatId"
          >
            <el-select
              style="width: 150px"
              v-model="dmsRegistrationParam.settlementCatId"
              @change="WXpay"
              placeholder="请选择支付方式"
              :clearable="true"
            >
              <el-option
                v-for="item in paytype"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :disabled="item.disabled"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item style="width: 300px" label="付款码" v-show="WXPayState"><!--:disabled="isAble"-->
            <el-input
              placeholder="付款码"
              style="width: 180px; color: black"
              ref="editTask"
              v-model="dmsRegistrationParam.barCode"
              @keyup.enter.native="searchEnterFun"
              :disabled="isAble"
            ></el-input>
          </el-form-item>

          <!--<el-form-item style="width: 300px" label="病历本" prop="needBook" hidden>
            <el-radio
              v-model="dmsRegistrationParam.needBook"
              @change="changeradio1"
              label="1">是
            </el-radio>
            <el-radio
              v-model="dmsRegistrationParam.needBook"
              @change="changeradio2"
              label="0">否
            </el-radio>
          </el-form-item>-->

          <el-form-item
            style="width: 300px"
            :inline="true"
            label="总金额"
            prop="summery">
            <el-input
              v-model="dmsRegistrationParam.summery"
              placeholder="总金额"
              disabled></el-input>
          </el-form-item>

          <el-form-item
            style="width: 300px"
            :inline="true"
            label="可用金额"
            prop="blance">
            <el-input
              v-model="dmsRegistrationParam.blance"
              placeholder="可用金额"
              disabled></el-input>
          </el-form-item>

          <!-- <el-form-item
             style="width: 300px"
             :inline="true"
             label="冻结金额"
             prop="frozen"
           >
             <el-input
               v-model="dmsRegistrationParam.frozen"
               placeholder="冻结金额"
               disabled
             ></el-input>
           </el-form-item>-->

          <el-dialog :visible.sync="dialogVisible" title="充值" width="500px">
            <el-form ref="user" :model="dmsRegistrationParam" label-position="left" :rules="rules">
              <el-form-item style="width: 320px" :inline="true" label="充值金额" prop="topUp">
                <el-input type="number" v-model="dmsRegistrationParam.topUp" placeholder="充值金额"
                          style="width:200px;"></el-input>
              </el-form-item>
              <el-form-item style="width: 320px" :inline="true" label="支付方式" prop="topUp">
                <el-select
                  style="width: 200px"
                  v-model="payType"
                  @change="WXpay1"
                  placeholder="请选择支付方式"
                  :clearable="true">
                  <el-option
                    v-for="item in paytypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    :disabled="item.disabled">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item v-if="payType == 6 && dmsRegistrationParam.topUp != ''" style="width: 320px" :inline="true"
                            label="付款码" prop="topUp">
                <!-- <el-input v-model="dmsRegistrationParam.topUp" placeholder="付款码" style="margin-left:7%;width:200px;"></el-input> -->
                <el-input
                  placeholder="付款码"
                  style="wcolor: black;margin-left:7%;width:200px;"
                  ref="editTask2"
                  v-model="payCode"
                  @keyup.enter.native="searchEnterFun2"
                  :disabled="isAble2">
                </el-input>
              </el-form-item>
            </el-form>
            <div style="text-align:right;">
              <el-button type="danger" @click="dialogVisible=false">取消</el-button>
              <el-button type="primary" @click="recharge()">确认</el-button>
            </div>
          </el-dialog>

          <el-dialog :visible.sync="collBackrefund" title="退費" width="600px">
            <!--<el-form-item style="width: 300px" :inline="true" label="退费金额" prop="refund">
              <el-input v-model="dmsRegistrationParam.refund" placeholder="退费金额"></el-input>
              <div style="color:red"><label>仅支持现金退款！</label></div>
            </el-form-item>-->
            <el-form-item style="width: 400px" :inline="true" label="现金金额">
              <el-input
                placeholder="现金金额"
                disabled
                v-model="refundResultsParam.cash"
              ></el-input>
            </el-form-item>
            <el-form-item style="width: 400px" :inline="true" label="银行卡金额" >
              <el-input
                placeholder="银行卡金额"
                disabled
                v-model="refundResultsParam.bankCard"
              ></el-input>
            </el-form-item>
            <el-form-item style="width: 400px" :inline="true" label="微信金额" >
              <el-input
                placeholder="微信金额"
                disabled
                v-model="refundResultsParam.weChat"
              ></el-input>
            </el-form-item>
            <div style="text-align:right;">
              <el-button type="danger" @click="collBackrefund=false">取消</el-button>
              <el-button type="primary" @click="rollback()">确认</el-button>
            </div>
          </el-dialog>

          <el-form-item style="align-content: center">
            <el-button
              style="align-content: center"
              type="primary"
              @click="createRegistration('dmsRegistrationParam')"
            >挂号
            </el-button
            >
          </el-form-item>

          <el-form-item style="align-content: center">
            <el-button
              style="align-content: center"
              type="primary"
              @click="charge()"
            >充值
            </el-button
            >
          </el-form-item>

          <el-form-item style="align-content: center">
            <el-button
              style="align-content: center"
              type="primary"
              @click="refund()"
            >退费
            </el-button
            >
          </el-form-item>

          <el-form-item style="align-content: center">
            <el-button
              style="align-content: center"
              type="primary"
              @click="updateInformation('dmsRegistrationParam')"
            >修改信息
            </el-button
            >
          </el-form-item>

        </el-form>
      </transition>
    </div>

    <div v-if="!dialogFormVisible && !isshow && !refundVisible">
      <el-button
        style="
          margin-left: 20px;
          font-family: '微软雅黑';
          font-size: 14px;
          color: black;
        "
        disabled
      >日挂号信息列表
      </el-button
      >
      <el-input
        placeholder="病历号/姓名"
        v-model="queryRegister.medicalRecordNo"
        style="margin-left: 20px; width: 180px"
      ></el-input>
      <!-- <el-button @click="listRegisteredPatient"><svg-icon icon-class="search" /></el-button>-->
      <el-button
        style="float: right; margin-right: 30px"
        type="text"
        @click="refresh"
        size="medium"
      ><i class="el-icon-refresh"/>刷新
      </el-button
      >
      <el-table
        border
        :data="
          RegisterList.filter(
            (data) =>
              !queryRegister.medicalRecordNo ||
              data.patientName
                .toLowerCase()
                .includes(queryRegister.medicalRecordNo.toLowerCase()) ||
              data.medicalRecordNo
                .toLowerCase()
                .includes(queryRegister.medicalRecordNo.toLowerCase())
          )
        "
        highlight-current-row
        style="width: 100%; margin-top: 20px"
        height="400"
      >
        <el-table-column fixed="left" label="操作" width="300" align="center">
          <template slot-scope="scope">
            <el-button
              plain
              size="mini"
              v-if="scope.row.registrationStatus != '4'"
              @click="supprint(scope.row)"
            >打发票
            </el-button
            >
            <el-button
              plain
              size="mini"
              v-if="scope.row.registrationStatus != '4'"
              @click="printHistory(scope.row)"
            >打印病例
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '1'"
              plain
              size="mini"
              @click="tuihao(scope.row)"
            >退号
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '2'"
              plain
              size="mini"
              @click="handlepay(scope.row)"
            >查看
            </el-button
            >
            <!--            <el-button v-if="scope.row.registrationStatus=='2'" plain size="mini" @click="refundRegistrationCharge(scope.row)">退费</el-button>-->
            <el-button
              v-if="scope.row.registrationStatus == '0'"
              plain
              size="mini"
            >已退号
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '4'"
              plain
              size="mini"
              disabled
            >无
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '3'"
              plain
              size="mini"
              disabled
            >无
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '6'"
              plain
              size="mini"
              disabled
            >无
            </el-button
            >
          </template>
        </el-table-column>
        <el-table-column align="center" label="状态" width="100px">
          <!--<template slot-scope="scope">
            <el-tag v-if="scope.row.registrationStatus === 1" type="primary"
              >未看诊</el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 4" type="danger"
              >已退号</el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 2" type="warning"
              >待收费</el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 3" type="success"
              >诊毕</el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 6" type="success"
              >已收费</el-tag
            >
          </template>-->
          <template slot-scope="scope">

            <el-tag v-if="scope.row.registrationStatus === 1" type="primary"
            >未看诊
            </el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 4" type="danger"
            >已退号
            </el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 2" type="warning"
            >未诊毕
            </el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 3" type="success"
            >诊毕
            </el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 6" type="success"
            >已收费
            </el-tag
            >
          </template>
        </el-table-column>
        <el-table-column align="center" label="病历号" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.medicalRecordNo }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="身份证号" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.identificationNo}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="姓名">
          <template slot-scope="scope">
            <span>{{ scope.row.patientName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="性别" width="70px">
          <template slot-scope="scope">
            <span v-if="scope.row.patientGender === 0">男</span>
            <span v-if="scope.row.patientGender === 1">女</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="出生日期">
          <template slot-scope="scope">
            <span>{{ scope.row.patientDateOfBirth }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="看诊日期">
          <template slot-scope="scope">
            <span>{{ scope.row.attendanceDate }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="看诊科目">
          <template slot-scope="scope">
            <span>{{ scope.row.deptName }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="total>0" :total="total" :page.sync="queryRegister.pageNum" :limit.sync="queryRegister.pageSize" @pagination="listRegisteredPatient()" />
    </div>
    <div
      v-show="dialogFormVisible && !refundVisible"
      style="text-align: center"
    >
      <el-form
        :model="onepatient"
        label-width="80px"
        label-position="left"
        :inline="true"
      >
        <el-form-item label="病历号" prop="id" label-width="60px">
          <el-tag
            style="width: 200px"
            type="info"
            size="large"
            placeholder="病历号"
          >{{ onepatient.medicalRecordNo }}
          </el-tag
          >
        </el-form-item>
        <el-form-item label="患者姓名" prop="name" label-width="70px">
          <el-tag
            style="width: 140px"
            type="info"
            size="large"
            placeholder="病历号"
          >{{ onepatient.patientName }}
          </el-tag
          >
        </el-form-item>

        <el-form-item label="看诊日期" prop="birth" label-width="70px">
          <el-tag
            style="width: 140px"
            type="info"
            size="large"
            placeholder="病历号"
          >{{ onepatient.attendanceDate }}
          </el-tag
          >
        </el-form-item>
      </el-form>
      <el-table stripe border :data="paylist" @selection-change="handlechange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="项目名称">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="总金额(元)" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.amount }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="开立时间" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="项目类型" width="200">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1">检查</el-tag>
            <el-tag v-if="scope.row.type === 2">检验</el-tag>
            <el-tag v-if="scope.row.type === 3">处置</el-tag>
            <el-tag v-if="scope.row.type === 4">草药处方</el-tag>
            <el-tag v-if="scope.row.type === 5">成药处方</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="状态" width="200">
          <el-tag type="primary">未缴费</el-tag>
        </el-table-column>
      </el-table>
      <p></p>
      <!--<span style="font-family: '微软雅黑'">应付: </span>
      <el-tag size="large" type="info">{{ totalamount }}</el-tag>
      <el-select
        style="width: 150px"
        v-model="settlementCatId"
        placeholder="请选择支付方式"
        @change="WXpay1"
      >
        <el-option
          v-for="item in paytype"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>-->

      <!--开药付款的地方
      :disabled="isAble"
      v-show="WXPayState1"
       v-model="dmsRegistrationParam.barCode"
      @keyup.enter.native="searchEnterFun" :disabled="isAble"-->
      <el-input
        placeholder="付款码"
        style="width: 180px; color: black"
        ref="editTask"
        v-show="WXPayState1"
        @keyup.enter.native="searchEnterFun"
        :readonly="isAble"
        v-model="xbarCode"
      ></el-input>

      <p></p>
      <!--<el-button type="primary" @click="charge">缴费</el-button>-->
      <el-button type="danger" @click="dialogFormVisible = false">返回</el-button>
    </div>
    <div v-show="refundVisible" style="text-align: center">
      <el-form
        :model="onepatient"
        label-width="80px"
        label-position="left"
        :inline="true"
      >
        <el-form-item label="病历号" prop="id" label-width="60px">
          <el-tag
            style="width: 200px"
            type="info"
            size="large"
            placeholder="病历号"
          >{{ onepatient.medicalRecordNo }}
          </el-tag
          >
        </el-form-item>
        <el-form-item label="患者姓名" prop="name" label-width="70px">
          <el-tag
            style="width: 140px"
            type="info"
            size="large"
            placeholder="病历号"
          >{{ onepatient.patientName }}
          </el-tag
          >
        </el-form-item>

        <el-form-item label="看诊日期" prop="birth" label-width="70px">
          <el-tag
            style="width: 140px"
            type="info"
            size="large"
            placeholder="病历号"
          >{{ onepatient.attendanceDate }}
          </el-tag
          >
        </el-form-item>
      </el-form>
      <el-table stripe border :data="paylist" @selection-change="handlechange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="发票号" width="250px">
          <template slot-scope="scope">
            {{ scope.row.invoiceIdfNo }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="项目名称">
          <template slot-scope="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="总金额(元)" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.amount }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="开立时间" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="项目类型" width="200">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1">检查</el-tag>
            <el-tag v-if="scope.row.type === 2">检验</el-tag>
            <el-tag v-if="scope.row.type === 3">处置</el-tag>
            <el-tag v-if="scope.row.type === 4">草药处方</el-tag>
            <el-tag v-if="scope.row.type === 5">成药处方</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="状态" width="200">
          <el-tag type="success">可退费</el-tag>
        </el-table-column>
      </el-table>
      <p></p>
      <span style="font-family: '微软雅黑'">应退费: </span>
      <el-tag size="large" type="info">{{ totalamount }}</el-tag>
      <el-button type="primary" @click="refundmoney">退费</el-button>
      <el-button type="danger" @click="refundVisible = false">取消</el-button>
    </div>
    <el-dialog title="退号" :visible.sync="refundregistVisible" width="400px">
      <el-form :model="refundregist" >
        <el-form-item label="发票号" label-width="100px">
          <el-input
            v-model="invoiceNo"
            style="width: 200px"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item label="冲红发票号" label-width="100px">
          <el-input
            v-model="redinvoiceNo"
            style="width: 200px"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            style="float: right; margin-left: 10px"
            type="danger"
            @click="refundregistVisible = false"
          >取消
          </el-button
          >
          <el-button style="float: right" type="primary" @click="comfirmtuihao(scope.row)"
          >退号
          </el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog title="重打" :visible.sync="reprintregistVisible" width="400px">
      <el-form>
        <el-form-item label="新发票号" label-width="100px">
          <el-input
            v-model="re1"
            style="width: 200px"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item label="原发票号" label-width="100px">
          <el-input
            v-model="re2"
            style="width: 200px"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            style="float: right; margin-left: 10px"
            type="danger"
            @click="reprintregistVisible = false"
          >取消
          </el-button
          >
          <el-button style="float: right" type="primary" @click="reprint2"
          >重打发票
          </el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- <el-dialog title="补打" :visible.sync="suppregistVisible" width="400px">
      <el-form :model="refundregist">
        <el-form-item label="新发票号" label-width="100px">
          <el-input
            v-model="re2"
            style="width: 200px"
            placeholder=""
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            style="float: right; margin-left: 10px"
            type="danger"
            @click="suppregistVisible = false"
            >取消</el-button
          >
          <el-button style="float: right" type="primary" @click="supprint2">确定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog> -->

    <el-dialog title="打发票" :visible.sync="suppregistVisible" width="50%">
      <historyBill2 ref="bill" v-bind:patient="patient"></historyBill2>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="updateInvoice">打发票</el-button>
      </span>
    </el-dialog>

    <div v-if="isshow">
      <el-button
        style="
        margin-left: 20px;
        font-family: '微软雅黑';
        font-size: 14px;
        color: black;
        top: 20px"
        disabled
      >历史挂号信息列表
      </el-button
      >
      <el-table
        border
        :data="RegisterList1"
        highlight-current-row
        style="width: 100%; margin-top: 20px"
        height="400">
        <el-table-column fixed="left" label="操作" width="300" align="center">
          <template slot-scope="scope">
            <el-button
              plain
              size="mini"
              v-if="scope.row.registrationStatus != '4'"
              @click="supprint(scope.row)"
            >打发票
            </el-button
            >
            <el-button
              plain
              size="mini"
              v-if="scope.row.registrationStatus != '4'"
              @click="printHistory(scope.row)"
            >打印病例
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '1'"
              plain
              size="mini"
              @click="tuihao(scope.row)"
            >退号
            </el-button
            >
           <!-- <el-button
              v-if="scope.row.registrationStatus == '2'"
              plain
              size="mini"
              @click="handlepay(scope.row)"
            >查看
            </el-button
            >-->
            <!--            <el-button v-if="scope.row.registrationStatus=='2'" plain size="mini" @click="refundRegistrationCharge(scope.row)">退费</el-button>-->
            <el-button
              v-if="scope.row.registrationStatus == '0'"
              plain
              size="mini"
            >已退号
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '4'"
              plain
              size="mini"
              disabled
            >无
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '3'"
              plain
              size="mini"
              disabled
            >无
            </el-button
            >
            <el-button
              v-if="scope.row.registrationStatus == '6'"
              plain
              size="mini"
              disabled
            >无
            </el-button>
          </template>
        </el-table-column>

        <el-table-column align="center" label="状态" width="100px">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.registrationStatus === 1" type="primary"
            >未看诊
            </el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 4" type="danger"
            >已退号
            </el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 2" type="warning"
            >未诊毕
            </el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 3" type="success"
            >诊毕
            </el-tag
            >
            <el-tag v-if="scope.row.registrationStatus === 6" type="success"
            >已收费
            </el-tag
            >
          </template>
        </el-table-column>
        <el-table-column align="center" label="病历号" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.medicalRecordNo }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="身份证号" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.identificationNo}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="姓名">
          <template slot-scope="scope">
            <span>{{ scope.row.patientName }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="性别" width="70px">
          <template slot-scope="scope">
            <span v-if="scope.row.patientGender === 0">男</span>
            <span v-if="scope.row.patientGender === 1">女</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="出生日期">
          <template slot-scope="scope">
            <span>{{ scope.row.patientDateOfBirth }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="看诊医生">
          <template slot-scope="scope">
            <span>{{ scope.row.bindDoctorName}}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="看诊日期">
          <template slot-scope="scope">
            <span>{{ scope.row.attendanceDate }}</span>
          </template>
        </el-table-column>

        <el-table-column align="center" label="看诊科目">
          <template slot-scope="scope">
            <span>{{ scope.row.deptName }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="registerByCardIdTotal>0" :total="registerByCardIdTotal" :page.sync="listRegisterByCardId.pageNum" :limit.sync="listRegisterByCardId.pageSize" @pagination="listPatientByCardId()" />
    </div>

<!--    <el-dialog title="打印病历" :visible.sync="medicalRecords" width="50%">
      <medicalRecords ref="medicalRecords" v-bind:patient="patient"></medicalRecords>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="updateInvoice">打印</el-button>
      </span>
    </el-dialog>-->

    <el-dialog :visible.sync="medicalRecords" title="病例信息">
      <medicalRecords ref="medicalRecords" v-bind:patient="patient"></medicalRecords>
    </el-dialog>
  </div>

</template>
<style>
  body .el-table th.gutter {
    display: table-cell !important;
  }

  ::-webkit-scrollbar {
    width: 8px;
    height: 8px;
    background-color: #fff;
  }

  ::-webkit-scrollbar-thumb {
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
    background-color: #f0f8ff;
  }
</style>

<script>
  import {
    reprintInvoice,
    supplementPrintInvoice,
    listRegisteredPatient,
    createRegistration,
    listDocBySkd,
    refundRegistrationCharge,
    listChargeByRegistrationId,
    charge,
    refundCharge,
    listRefundByRegistrationId,
    listTime,
    recharge,
    rollback,
    isAccount,
    updateInformation,
    listPatientByCardId,
    selectRefundResultsParam
  } from "@/api/regist";
  import {getAllDep} from "@/api/department";
  import {selectPatientByIdNo,selectPeopleByRegistrationId} from "@/api/outpatient/patient";
  import {getAllReg, getSettleCatlist} from "@/api/admin";
  import {parseTime, formatTime, deepClone} from "@/utils";
  import {connect, readCert} from "@/utils/ajax";
  import Pagination from '@/components/Pagination'
  import {updateInvoice} from "@/api/outpatient/feequery";
  import historyBill2 from '../outpatient/workstation/historyBill2'
  import medicalRecords from '../historyRecords/workstation/medicalRecords'
  import moment from 'moment';

  const defaultpatient = {
    id: "",
    name: "",
    male: "",
    birth: "",
    card: "",
    fapiao: "",
    type: "",
    type2: "",
    data1: "",
    data2: "",
    isdone: "",
    isget: "",
    status: "",
    money: "",
    kemu: "",
  };
  function compareTime(h, m, now) {
    return new Date().setHours(h, m) >= now;
  }
  export default {
    components: {
      historyBill2,medicalRecords,Pagination
    },
    data() {
      return {
        isAble: false,
        isAble2: false,
        dialogVisible: false,
        collBackrefund: false,
        medicalRecords:false,
        rules: {
          // needBook: [
          //   { required: true, message: "请选择病历本", trigger: "blur" },
          // ],
          settlementCatId: [
            {required: true, message: "请选择支付方式", trigger: "blur"},
          ],
          /* skdId: [{required: true, message: "请输入看诊医生", trigger: "blur"}],
           time: [{required: true, message: "请输入预约时间", trigger: "blur"}],*/
          amount: [
            {required: true, message: "请输入应收金额", trigger: "blur"},
          ],
          identificationNo: [
            {required: true, message: "请输入身份证号", trigger: "blur"},
            {
              pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
              message: "请输入正确的身份证号",
              trigger: "blur",
            },
          ],
          name: [{required: true, message: "请输入姓名", trigger: "blur"}],
          dateOfBirth: [
            {required: true, message: "请输入出生日期", trigger: "blur"},
          ],
          gender: [{required: true, message: "请输入性别", trigger: "blur"}],
          homeAddress: [
            {required: true, message: "请输入家庭住址", trigger: "blur"},
          ],
          phoneNo: [
            {required: true, message: "请输入联系方式", trigger: "blur"},
          ],
          deptId: [
            {required: true, message: "请输入挂号科室", trigger: "blur"},
          ],
         /* RegisterRnak: [
            {required: true, message: "请输入挂号级别", trigger: "blur"},
          ],*/
          attendanceDate: [
            {required: true, message: "请输入挂号日期", trigger: "blur"},
          ],
          noon: [{required: true, message: "请输入午别", trigger: "blur"}],
        },
        re1: "",
        re2: "",
        patient: null,
        xbarCode: "",
        WXPayState: false,
        WXPayState1: false,
        suppregistVisible: false,
        reprintregistVisible: false,
        isbin: 0,
        redinvoiceNo: "",
        refundregistVisible: false,
        refundregist: {},
        refundVisible: false,
        settlementCatId: "",
        invoiceNo: "",
        refs: [],
        paymoney: 0,
        totalamount: 0,
        paylist: [],
        doctList: [],
        timeList: [], //预约时间
        regdisabled: true,
        regdisabled2: true,
        alldept: [],
        dmsRegistrationParam: {
          attendanceDate: "",
          deptId: "",
          noon: "",
          amount: "",
          skdId: "",
          needBook: "",
          name: "",
          dateOfBirth: "",
          age: "",
          homeAddress: "",
          gender: "",
          phoneNo: "",
          identificationNo: "",
          barCode: "",
          time: "",
          summery: "", //总金额
          blance: "", //可用金额
          frozen: "", //冻结金额
          topUp: "", //充值金额
          refund: "", //退费金额
          settlementCatId: "",
          registeredLevel:''
        },
        RegisterList: [],
        RegisterList1: [],
        RegisterRnak: [],
        itemtype: ["检查检验", "处方", "处置"],
        payT: "",
        paytype: [
          /* {
              value:'1',
              label:'现金'
            },
            {
              value:'2',
              label:'银行卡'
            },
            {
              value:'3',
              label:'医保'
            },
            {
              value:'4',
              label:'信用卡'
            },
            {
              value:'5',
              label:'支付宝'
            },
            {
              value:'6',
              label:'微信'
            },
            {
              value:'7',
              label:'其他'
            }*/
        ],
        onepatient: Object.assign({}, defaultpatient),
        radio: 1,
        dialogFormVisible: false,
        isshow: false,
        show: false,
        list: [],
        refundRegisterId: "",
        queryRegister: {
          medicalRecordNo: null,
          queryDate: null,
          pageSize: 10,
          pageNum: 1,
        },
        listRegisterByCardId:{
          medicalRecordNo:'',
          pageSize:10,
          pageNum:1
        },
        registerByCardIdTotal:0,
        total: 0,
        payType: "",
        payCode: "",
        paytypes: [],
        refundResultsParam:{
          cash:0.000,
          bankCard:0.000,
          weChat:0.000,
        }
      };
    },

    created() {
      const that = this;
      this.listRegisteredPatient();
      this.getAllDep();
      this.getAllReg();
      this.getSettleCatlist();
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
      };
      handleConncet();
    },
    methods: {
      makeInvoice() {
      },

      updateInvoice() {
        let data = this.$refs.bill.multipleSelection
        updateInvoice(data).then(res => {
          if (res.data === 1) {
            this.$notify({
              title: "提示",
              message: "操作成功！",
              type: "success",
              duration: 2000,
            });
          } else {
            this.$notify({
              title: "提示",
              message: "操作失败！",
              type: "warning",
              duration: 2000,
            });
          }
          this.suppregistVisible != this.suppregistVisible
        })
      },
      close() {
        this.suppregistVisible = false
      },
      async doChange() {
        const idcard = this.dmsRegistrationParam.identificationNo;
        await this.requestPersonById(idcard)
        if (idcard.length === 18) {
          this.dmsRegistrationParam.dateOfBirth =
            idcard.substring(6, 10) +
            "-" +
            idcard.substring(10, 12) +
            "-" +
            idcard.substring(12, 14);
        }
      },
      supprint(val) {
        this.suppregistVisible = true;
        this.re1 = val.registrationId;
        this.re2 = "";
        this.$nextTick(() => {
          this.patient = val;
        })
      },
      supprint2() {
        if (this.r2 === null || this.r2 === '' || this.r2 === undefined) {
          this.$notify({
            title: "信息不完整",
            message: "请输入新发票号！",
            type: "warning",
            duration: 2000,
          });
          return;
        }
        let data = {};
        data.newInvoiceNo = this.re2;
        data.registrationId = this.re1;
        supplementPrintInvoice(data).then((res) => {
          this.$notify({
            title: "成功",
            message: "补打发票成功",
            type: "success",
            duration: 2000,
          });
        });
        this.re2 = ''
        this.suppregistVisible = false;
      },
      reprint2() {
        let data = {};
        if (!this.re1) {
          this.$notify({
            title: '提示',
            message: '请输入新发票号',
            type: 'warning',
            duration: 3500
          })
          return
        }
        if (!this.re2) {
          this.$notify({
            title: '提示',
            message: '请输入原发票号',
            type: 'warning',
            duration: 3500
          })
          return
        }
        data.newInvoiceNo = this.re2;
        data.oldInvoiceNo = this.re1;
        reprintInvoice(data).then((res) => {
          this.$notify({
            title: "成功",
            message: "重打发票成功",
            type: "success",
            duration: 2000,
          });
        });
      },
      reprint() {
        this.re1 = "";
        this.re2 = "";
        this.reprintregistVisible = true;
      },
      changeradio1() {
        if (this.isbin === 0) {
          this.dmsRegistrationParam.amount += 1;
          this.isbin = 1;
        }
      },
      changeradio2() {
        if (this.isbin === 1) {
          this.dmsRegistrationParam.amount -= 1;
          this.isbin = 0;
        }
      },
      refresh() {
        this.listRegisteredPatient();
      },
      selectPatientByIdNo() {
        this.isshow = true;
        // this.dmsRegistrationParam.res;
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
            this.requestPersonById(person.certNumber, person);
          }
        });
      },
      requestPersonById(id, person) {
        return new Promise(resolve => {
          selectPatientByIdNo(id).then((res) => {
            this.WXPayState = false;
            /*this.doChange()*/
            //清数据
            this.qingkong();
            this.dmsRegistrationParam.identificationNo = id;
            if (res.data != null) {
              //信息回显
              this.dmsRegistrationParam.name = res.data.name;
              /*this.dmsRegistrationParam.dateOfBirth = res.data.dateOfBirth*/
              this.dmsRegistrationParam.dateOfBirth = res.data.dateOfBirth.substring(
                0,
                10
              );
              this.dmsRegistrationParam.age = res.data.age;
              this.dmsRegistrationParam.homeAddress = res.data.homeAddress;
              this.dmsRegistrationParam.gender = res.data.gender;
              this.dmsRegistrationParam.phoneNo = res.data.phoneNo;
              this.dmsRegistrationParam.summery = res.data.summery;
              this.dmsRegistrationParam.blance = res.data.blance;
              this.dmsRegistrationParam.frozen = res.data.frozen;
              this.listRegisterByCardId.medicalRecordNo = res.data.medicalRecordNo;
              this.$message({
                type: "success",
                message: "成功读取患者: " + this.dmsRegistrationParam.name,
              });
            } else if (person) {
              //信息回显
              this.dmsRegistrationParam.name = person.partyName;
              this.dmsRegistrationParam.dateOfBirth = person.bornDay.substring(0, 4) + "-" + person.bornDay.substring(4, 6) + "-" + person.bornDay.substring(6, 8)
              this.dmsRegistrationParam.homeAddress = person.certAddress;
              if (person.gender == "男") {
                this.dmsRegistrationParam.gender = 0
              } else {
                this.dmsRegistrationParam.gender = 1;
              }
            } else {
              /* this.$message({
                 type: "error",
                 message: "该患者没有历史信息",
               });*/
            }
            /*this.listRegisterByCardId.medicalRecordNo = res.data.medicalRecordNo;*/
            this.listPatientByCardId()
            resolve();
          });
        })
      },
      comfirmtuihao(val) {
        let data = {};
        // if (!this.invoiceNo && !this.redinvoiceNo) {
        //   this.$notify({
        //     title: "提示",
        //     message: "请填写发票号或冲红发票号",
        //     type: "warning",
        //     duration: 4500,
        //   });
        //   return;
        // }

        data.oldInvoiceNo = this.invoiceNo;
        data.redInvoiceNo = this.redinvoiceNo;
        data.registrationId = this.refundRegisterId;
        data.operatorId = this.$store.getters.id;
        const loading = this.$loading({
          lock: true,
          text: '退号中请等待！！！！',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        refundRegistrationCharge(data).then((res) => {
          setTimeout(() => {
            loading.close();
          }, 1);
          if (res.data === 1) {
            this.$notify({
              title: "提示",
              message: res.message,
              type: "success",
              duration: 2000,
            });
          }else {
            this.$notify({
              title: "提示",
              message: res.message,
              type: "warning",
              duration: 2000,
            });
          }
          this.refundregistVisible = false;
          this.listPatientByCardId();
        });
      },
      tuihao(row) {
        // this.refundregistVisible = true;
        this.refundRegisterId = row.registrationId;
        this.$confirm("确定进行退号操作吗?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
            this.comfirmtuihao()
          })
          /*.then(() => {
            this.$message({
              type: "success",
              message: "操作成功!"
            });
          });*/
      },
      refundmoney() {
        if (this.invoiceNo === "" || this.invoiceNo === undefined) {
          this.$notify({
            title: "警告",
            message: "发票号不能为空！",
            type: "warning",
            duration: 2000,
          });
          return;
        }
        let flag = 1;
        let inv = "";
        this.refs.forEach((item) => {
          item.settlementCatId = 1;
          item.chargeItemId = item.id;
          if (inv === "") item.invoiceNo = item.invoiceIdfNo;
          else {
            if (item.invoiceIdfNo === inv) {
              item.invoiceNo = item.invoiceIdfNo;
            } else flag = 0;
          }
          if (flag === 0) {
            this.$notify({
              title: "警告",
              message: "不能同时退不同发票号的项目！",
              type: "warning",
              duration: 2000,
            });
            return;
          }
          item.newInvoiceNo = this.invoiceNo + 1;
          item.redInvoiceNo = this.invoiceNo;
          item.operatorId = this.$store.getters.id;
          item.refundAmount = item.amount;
        });

        refundCharge(this.refs).then((res) => {
          this.$notify({
            title: "成功",
            message: "退费成功",
            type: "success",
            duration: 2000,
          });
          this.refundVisible = false;
          this.listRegisteredPatient();
        });
        this.refundregistVisible = false;
        this.listRegisteredPatient();
      },
      charge() {
        if (
          this.invoiceNo === "" ||
          this.invoiceNo === undefined ||
          this.settlementCatId === "" ||
          this.settlementCatId === undefined
        ) {
          this.$notify({
            title: "信息不完整",
            message: "请填写发票号或选择缴费类型！",
            type: "danger",
            duration: 2000,
          });
          return;
        }
        let data = this.refs;
        data.forEach((item) => {
          item.chargeItemId = item.id;
          item.invoiceNo = this.invoiceNo;
          item.opratorId = this.$store.getters.id;
          item.settlementCatId = this.settlementCatId;
          item.xbarCode = this.xbarCode;
        });
        charge(data).then((res) => {
          this.$notify({
            title: "成功",
            message: "缴费成功",
            type: "success",
            duration: 2000,
          });
          this.dialogFormVisible = false;
          this.listRegisteredPatient();
        });
      },
      handlechange(val) {
        this.refs = val;
        this.totalamount = 0;
        this.refs.forEach((item) => {
          this.totalamount += item.amount;
        });
        this.totalamount = this.totalamount.toFixed(2);
      },
      refundRegistrationCharge(row) {
        this.onepatient = deepClone(row);
        listRefundByRegistrationId(row.registrationId).then((res) => {
          this.paylist = res.data;
          this.paylist.forEach((item) => {
            if (item.createTime !== null) {
              item.createTime = parseTime(item.createTime);
            }
          });
          this.refundVisible = true;
        });
      },
      createRegistration(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (
              this.dmsRegistrationParam.settlementCatId === "" ||
              this.dmsRegistrationParam.settlementCatId === undefined
            ) {
              this.$notify({
                title: "信息不完整",
                message: "请选择支付方式！",
                type: "warning",
                duration: 2000,
              });
              return;
            }
            this.dmsRegistrationParam.attendanceDate = parseTime(
              this.dmsRegistrationParam.attendanceDate
            ).substr(0, 10);
            this.dmsRegistrationParam.dateOfBirth = parseTime(
              this.dmsRegistrationParam.dateOfBirth
            ).substr(0, 10);
            this.dmsRegistrationParam.opratorId = this.$store.getters.id;
            createRegistration(this.dmsRegistrationParam).then((res) => {
              if (res.data === 0) {
                this.$notify({
                  title: "提示",
                  message: "挂号失败",
                  type: "warning",
                  duration: 2000,
                });
              } else if (res.data === 1) {
                this.$notify({
                  title: "提示",
                  message: "挂号成功",
                  type: "success",
                  duration: 2000,
                });
              } else if (res.data === 2) {
                this.$notify({
                  title: "提示",
                  message: "余额不足",
                  type: "warning",
                  duration: 2000,
                });
              } else if (res.data === 3) {
                this.$notify({
                  title: "提示",
                  message: "请勿同一天重复挂号",
                  type: "warning",
                  duration: 2000,
                });
                return;
              }

              this.isshow = false;
              this.listRegisteredPatient();
              // this.dmsRegistrationParam = {};
              this.qingkong();
            });
          }
        });

      },
      charge() {
        this.dialogVisible = true
      },
      refund() {
        this.selectRefundResultsParam()
        this.collBackrefund = true
      },
      updateInformation() {

        updateInformation(this.dmsRegistrationParam).then(res => {
          if (res.data === 1) {
            this.$notify({
              title: "成功",
              message: "修改成功",
              type: "success",
              duration: 2000,
            });
          } else {
            this.$notify({
              title: "失败",
              message: "修改失败",
              type: "warning",
              duration: 2000,
            });
          }
        })
      },
      recharge() {
        this.dmsRegistrationParam.settlementCatId = this.payType
        this.dmsRegistrationParam.barCode = this.payCode
        this.dmsRegistrationParam.opratorId = this.$store.getters.id;
        const loading = this.$loading({
          lock: true,
          text: '充值中请等待！！！',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        recharge(this.dmsRegistrationParam).then((res) => {
          setTimeout(() => {
            loading.close();
          }, 1);
          if (res.data === 1) {
            this.$notify({
              title: "成功",
              message: "充值成功",
              type: "success",
              duration: 2000,
            });
          } else {
            this.$notify({
              title: "失败",
              message: "充值失败",
              type: "warning",
              duration: 2000,
            });
          }
          this.payType = ''
          this.payCode = ''
          this.isshow = false;
          this.dialogVisible = false
          this.listRegisteredPatient();
          const idcard = this.dmsRegistrationParam.identificationNo;
          this.requestPersonById(idcard)
        });
      },
      rollback() {
        this.dmsRegistrationParam.opratorId = this.$store.getters.id;
        const loading = this.$loading({
          lock: true,
          text: '退费中请等到！！！',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        rollback(this.dmsRegistrationParam).then((res) => {
          setTimeout(() => {
            loading.close();
          }, 1);
          if (res.data === 1) {
            this.$notify({
              title: "成功",
              message: "退费成功",
              type: "success",
              duration: 2000,
            });
          } else {
            this.$notify({
              title: "失败",
              message: "退费失败",
              type: "warning",
              duration: 2000,
            });
          }
          this.isshow = false;
          this.collBackrefund = false;
          this.listRegisteredPatient();
          const idcard = this.dmsRegistrationParam.identificationNo;
          this.requestPersonById(idcard)
        });
      },

      choosenoon(val) {
        if (val !== "") {
          this.dmsRegistrationParam.skdId=""
          this.listDocBySkd();
        }
      },
      listDocBySkd() {
        let data = {};
        data.date = parseTime(this.dmsRegistrationParam.attendanceDate).substr(
          0,
          10
        );
        data.deptId = this.dmsRegistrationParam.deptId;
        data.noon = this.dmsRegistrationParam.noon;
        data.registrationRankId = this.dmsRegistrationParam.registeredLevel;
        listDocBySkd(data).then((res) => {
          this.doctList = res.data;
          this.regdisabled2 = false;
        });
      },

      listTime() {
        let data = {};
        data.skdId = this.dmsRegistrationParam.skdId;
        let time = this.dmsRegistrationParam.attendanceDate;
        let year = time.getFullYear();
        let month = time.getMonth() + 1;
        let date1 = time.getDate();
        if (month < 10) {
          month = "0" + month;
        }
        if (date1 < 10) {
          date1 = "0" + date1;
        }
        data.ruletime = year + "-" + month + "-" + date1;
        data.noon = this.dmsRegistrationParam.noon;
        listTime(data).then((res) => {
          if (moment().format('YYYY-MM-DD') === data.ruletime) {
            this.timeList = res.data.filter(item => {
              const [h, m] = item.split(':');
              return compareTime(h, m, new Date().getTime())
            });
          } else {
            this.timeList = res.data;
          }
        });
        this.doctList.forEach((item) => {
          if (data.skdId === item.skdId) {
            this.dmsRegistrationParam.amount = item.amount;
          }
        })
      },
      chooseRegister(val) {
        if (
          this.dmsRegistrationParam.registeredLevel === 1 &&
          this.dmsRegistrationParam.attendanceDate !== "" &&
          this.dmsRegistrationParam.deptId !== ""
        ) {
          this.regdisabled = false;
          this.regdisabled2 = false;
        } else {
          this.regdisabled = true;
          this.dmsRegistrationParam.noon = "";
          this.dmsRegistrationParam.skdId = "";
        }
        this.RegisterRank.forEach((item) => {
          if (item.id === this.dmsRegistrationParam.registeredLevel) {
            if (this.dmsRegistrationParam.needBook)
              this.dmsRegistrationParam.amount += 1;
          }
        });
      },
      getAllReg() {
        getAllReg().then((res) => {
          this.RegisterRank = res.data;
          this.dmsRegistrationParam.registeredLevel=this.RegisterRank[0].id
        });
      },
      getAllDep() {
        getAllDep().then((res) => {
          this.alldept = res.data;
        });
      },
      listRegisteredPatient() {
        listRegisteredPatient(this.queryRegister).then((res) => {
          this.RegisterList = res.data.list;
          this.RegisterList.forEach((item) => {
            item.patientDateOfBirth = item.patientDateOfBirth.substr(0, 10);
            item.attendanceDate = item.attendanceDate.substr(0, 10);
          });
          this.total = res.data.total;
        });
      },
      guahao() {
        this.isshow = !this.isshow;
      },
      handlepay(row) {
        this.onepatient = deepClone(row);
        listChargeByRegistrationId(row.registrationId).then((res) => {
          this.paylist = res.data;
          this.paylist.forEach((item) => {
            if (item.createTime !== null) {
              item.createTime = parseTime(item.createTime);
            }
          });
        });
        this.dialogFormVisible = true;
      },
      WXpay() {
        if (this.dmsRegistrationParam.settlementCatId === 7) {
          this.isAccount();
        } else if (this.dmsRegistrationParam.settlementCatId === 6) {
          this.WXPayState = true;
          this.$nextTick(() => this.$refs.editTask.focus());
        } else {
          this.WXPayState = false;
        }

      },
      WXpay1() {
        if (this.settlementCatId == 6) {
          this.WXPayState1 = true;
          this.$nextTick(() => this.$refs.editTask.focus());
        } else {
          this.WXPayState1 = false;
        }
      },
      searchEnterFun: function (e) {
        var keyCode = window.event ? e.keyCode : e.which;
        //  console.log('回车搜索',keyCode,e);
        if (keyCode == 13) {
          this.isAble = true;
        }
      },
      searchEnterFun2: function (e) {
        var keyCode = window.event ? e.keyCode : e.which;
        //  console.log('回车搜索',keyCode,e);
        if (keyCode == 13) {
          this.isAble2 = true;
        }
      },
      qingkong() {
        (this.dmsRegistrationParam.attendanceDate = ""),
          (this.dmsRegistrationParam.deptId = ""),
          (this.dmsRegistrationParam.noon = ""),
          (this.dmsRegistrationParam.amount = ""),
          (this.dmsRegistrationParam.skdId = ""),
          (this.dmsRegistrationParam.needBook = ""),
          (this.dmsRegistrationParam.name = ""),
          (this.dmsRegistrationParam.dateOfBirth = ""),
          (this.dmsRegistrationParam.age = ""),
          (this.dmsRegistrationParam.homeAddress = ""),
          (this.dmsRegistrationParam.gender = ""),
          (this.dmsRegistrationParam.phoneNo = ""),
          (this.dmsRegistrationParam.identificationNo = ""),
          (this.dmsRegistrationParam.barCode = ""),
          (this.dmsRegistrationParam.time = ""),
          (this.dmsRegistrationParam.summery = ""), //总金额
          (this.dmsRegistrationParam.blance = ""), //可用金额
          (this.dmsRegistrationParam.frozen = ""), //冻结金额
          (this.dmsRegistrationParam.topUp = ""), //充值金额
          (this.dmsRegistrationParam.refund = ""), //退费金额
          /*this.dmsRegistrationParam.settlementCatId=''*/
          (this.dmsRegistrationParam.settlementCatId = "");
        (this.RegisterList1 = []);

      },

      getSettleCatlist() {
        this.paytype = []
        this.paytypes = []
        let data = {
          pageNum: 1,
          pageSize: 20,
        };
        getSettleCatlist(data).then((res) => {
          res.data.list.forEach((item) => {
            let data = {
              value: "",
              label: "",
            };
            data.value = item.id;
            data.label = item.name;
            this.paytype.push(data);
            if (item.id != 7) {
              this.paytypes.push(data);
            }
          });
        });
      },

      isAccount() {
        let data = {
          identificationNo: ''
        }
        data.identificationNo = this.dmsRegistrationParam.identificationNo
        isAccount(data).then(res => {
          if (res.data === 0) {
            this.$notify({
              title: "失败",
              message: "用户不存在",
              type: "warning",
              duration: 2000,
            });
          }
        })
      },
      listPatientByCardId() {
        listPatientByCardId(this.listRegisterByCardId).then(res => {
          this.RegisterList1 = res.data.list;
          this.registerByCardIdTotal = res.data.total;
          if (this.RegisterList1 != null || this.RegisterList1 != undefined) {
            this.RegisterList1.forEach((item) => {
              item.patientDateOfBirth = item.patientDateOfBirth.substr(0, 10);
              item.attendanceDate = parseTime(item.attendanceDate).substr(0, 16)
            });
          }
        })
      },
      printHistory(data){

        this.medicalRecords=true
        selectPeopleByRegistrationId(data.registrationId).then(res=>{
          this.$nextTick(() => {
            this.patient = res.data;
          })
        })
      },
      selectRefundResultsParam(){
        selectRefundResultsParam(this.dmsRegistrationParam).then(res=>{
          this.refundResultsParam=res.data
        })
      }
    },
  };
</script>
