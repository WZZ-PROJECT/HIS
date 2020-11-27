<template>
  <!-- 成药处方 -->
  <div>
    <el-container>
      <el-aside :width="mainwidth" style="background:white;padding:0 10px 0 0">
        <aside style="margin:0 0 0 0">
          <el-button type="text" size="medium" @click="addpre"><i class="el-icon-circle-plus-outline"/>新增处方</el-button>
          <el-button type="text" size="medium" @click="deletepre"><i class="el-icon-remove-outline"/>删除处方</el-button>
          <el-button type="text" size="medium" @click="apply"><i class="el-icon-circle-check"/>开立处方</el-button>
          <el-button type="text" size="medium" @click="invalid"><i class="el-icon-circle-close"/>作废处方</el-button>
          <el-button type="text" size="medium" @click="saveDrugPrescription"><i class="el-icon-upload2"/>暂存</el-button>
          <el-button type="text" size="medium" @click="getDrugPrescription"><i class="el-icon-download"/>取出暂存项
          </el-button>
          <el-button type="text" size="medium" @click="refresh"><i class="el-icon-refresh"/>刷新</el-button>
          <el-button style="float:right" @click="controlfast"><i v-show="!isclose" class="el-icon-caret-right"/><i
            v-show="isclose" class="el-icon-caret-left"/></el-button>
          <!-- <el-button type="primary" @click="print">打印</el-button>-->
        </aside>
        <el-table
          id="prescription1"
          ref="multipleTable"
          :data="prescriptionList"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange">
          <el-table-column
            align="center"
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            align="center"
            label="处方名称">
            <template slot-scope="scope">{{ scope.row.name }}</template>
          </el-table-column>
          <el-table-column
            align="center"
            prop="amount"
            label="总金额(元)">
          </el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
              <el-tag type="warning" v-if="scope.row.status===-1">未开立</el-tag>
              <el-tag type="danger" v-if="scope.row.status===0">已作废</el-tag>
              <el-tag type="info" v-if="scope.row.status===1">未缴费</el-tag>
              <el-tag type="info" v-if="scope.row.status===2">未发药</el-tag>
              <el-tag type="success" v-if="scope.row.status===3">已发药</el-tag>
              <el-tag type="danger" v-if="scope.row.status===4">已过期</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label=""
            show-overflow-tooltip>
            <template slot-scope="scope">
              <el-button type="text" v-if="scope.row.status===-1" @click="showDetail(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-aside>
      <transition name="el-zoom-in-left">
        <el-main width="50%" v-show="isclose"
                 style="border-style: dotted;border-width: 0px 0px 0px 1px;border-color:#C0C0C0;margin-top:-12px">
          <el-tabs v-model="tabName">
            <el-tab-pane label="成药模板" name="first">
              <el-table :data="models" height="230" @row-click="selectmodel" @row-dblclick="addmodel">
                <el-table-column label="模板名">
                  <template slot-scope="scope">
                    {{scope.row.name}}
                  </template>
                </el-table-column>
                <el-table-column label="目的">
                  <template slot-scope="scope">
                    {{scope.row.aim}}
                  </template>
                </el-table-column>
              </el-table>
              <el-card v-show="model.name!==undefined" class="box-card" shadow="never"
                       body-style="font-size: 14px;font-family:'微软雅黑';width:350px">
                <div slot="header" class="clearfix">
                  <span>{{model.name}}</span>
                </div>
                <p><b>模板目的：</b>{{model.aim}}</p>
                <p><b>模板总金额: </b>{{model.amount}}</p>
                <p><b>模板项目：</b></p>
                <p v-for="(drug,index) in model.druglist" :key="index"><b></b> {{drug.name}}</p>
              </el-card>
            </el-tab-pane>
          </el-tabs>
        </el-main>
      </transition>
    </el-container>
    <el-dialog title="处方详细" :visible.sync="dialogTableVisible" width="1500px" top="20px">
      <el-container>
        <el-aside width="30%" style="padding:0 0 0 0;margin:0 0 0 0">
          <div>
            <el-tag type="primary" style="width:100%">常用药品</el-tag>
            <el-table :data="freqlist" height="200px" @row-click="choosedrug">
              <el-table-column label="药品名" prop="name">
              </el-table-column>
              <el-table-column label="价格(元)" prop="price" width="100px"></el-table-column>
            </el-table>
          </div>
          <el-tag type="primary" style="width:100%;height:30px">药品目录

            <el-button type="primary" size="mini" style="width: 20px;;float:right">
              <svg-icon icon-class="search" style="margin-left:-6px"/>
            </el-button>
            <el-input size="mini" placeholder="药品名称" v-model="searchdrug" style="width:30%;margin-left:65px;"
                      @change="getdrugList"></el-input>
            <el-input size="mini" placeholder="拼音助记码" v-model="mnemonicCode" style="width:30%;margin-left:10px"
                      @change="getdrugList"></el-input>
          </el-tag>

          <el-table :data="drugList" height="300px" @row-click="choosedrug">
            <el-table-column label="药品名" prop="name"></el-table-column>
            <el-table-column label="价格(元)" prop="price" width="100px"></el-table-column>
            <el-table-column label="拼音助记码" prop="mnemonicCode"></el-table-column>
          </el-table>
          <pagination layout="prev, pager, next" auto-scroll="false" style="margin-top:0px" page-sizes="[]"
                      v-show="total>0" :total="total" :page.sync="page.pageNum" :limit.sync="page.pageSize"
                      @pagination="getdrugList"/>
          <div>
          </div>
        </el-aside>
        <el-main>
          <el-tag type="primary">项目金额总计:</el-tag>
          <el-tag type="warning">{{oneprescription.amount}}元</el-tag>
          <el-button type="primary" style="float:right" @click="createpre" v-if="!edit">增加处方</el-button>
          <el-button type="primary" style="float:right" @click="changepre" v-if="edit">修改处方</el-button>
          <el-input style="width:200px;margin-right:20px;float:right" v-model="oneprescription.name"
                    placeholder="处方名"></el-input>
          <el-table height="500px" :data="oneprescription.druglist" cell-style="text-align:center"
                    header-cell-style="text-align:center">
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
                <el-input-number controls-position="right" style="width:100px" :min="1" :max="100" size="mini"
                                 @change="changenum(scope.row)" v-model="scope.row.num"></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="剂型" width="130px" prop="dosage.name">
            </el-table-column>
            <el-table-column label="使用方法" width="130">
              <template slot-scope="scope">
                <el-input placeholder="用法" v-model="scope.row.medicalAdvice"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="频次" width="130px">
              <template slot-scope="scope">
                <el-select v-model="scope.row.frequency" placeholder="" style="width:120px">
                  <el-option v-for="item in [{key:1,label:'一天一次'},{key:2,label:'一天三次'}]" :key="item.key"
                             :label="item.label" :value="item.key"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="天数" width="100px">
              <template slot-scope="scope">
                <el-input-number controls-position="right" style="width:100px" :min="1" :max="100"
                                 v-model="scope.row.days" placeholder=""></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="用量" width="100px">
              <template slot-scope="scope">
                <el-input-number controls-position="right" style="width:100px" :min="1" :max="10000000" type="number"
                                 v-model="scope.row.usageNum" placeholder=""></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="单位" width="130px">
              <template slot-scope="scope">
                <el-select v-model="scope.row.usageNumUnit" placeholder="" style="width:120px">
                  <el-option v-for="item in [{key:1,label:'片'},{key:2,label:'支'},{key:3,label:'瓶'},{key:2,label:'克'}]"
                             :key="item.key" :label="item.label" :value="item.key"></el-option>
                </el-select>
              </template>
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
    </el-dialog>
  </div>
</template>
<script>
  import {getdrugList, selectById} from '@/api/drug'
  import Pagination from '@/components/Pagination'
  import {apply, listByReg, invalid} from '@/api/outpatient/prescription'
  import {deepClone} from '@/utils'
  import {selectByType} from '@/api/outpatient/frequentuse'
  import {getDrugPrescription, saveDrugPrescription} from '@/api/outpatient/save'
  import {listModel} from '@/api/drugmodel'

  const defaultpre = {
    name: '',
    druglist: [],
    amount: 0,
    status: -1,
  }
  export default {
    props: ['patient', 'activeName'],
    components: {Pagination},
    name: 'Prescription',
    data() {
      return {
        modelvisivle: false,
        model: {},
        models: [],
        freqlist: [],
        refs: [],
        prescriptionList: [],
        oneprescription: {
          name: '',
          druglist: [],
          amount: 0,
          status: -1,
        },
        edit: false,
        test: '',
        num: 0,
        dialogTableVisible: false,
        tabName: 'first',
        isclose: true,
        record: {
          main: 'test'
        },
        page: {
          pageNum: 1,
          pageSize: 10,
        },
        searchdrug: '',
        mnemonicCode:'',
        drugList: [],
        total: 0,
        mainwidth: "65%",
        activeNames: ['1'],
      };
    },
    watch: {
      'patient': function (newVal, oldVal) {
        this.patient = newVal
        this.listByReg()
      },
      activeName(n, o) {
        if ("fiveth" != n) {
          // this.listRecord()
        }
      }
    },
    created() {
      this.listModel()
    },
    methods: {
      addmodel(val) {
        // val.amount = Math.floor((val.amount+0.5)*100)/100
        val.amount = Math.floor((val.amount) * 100) / 100
        val.status = -1
        this.prescriptionList.push(val)
      },
      selectmodel(val) {
        this.model = deepClone(val)
        // this.model.amount = Math.floor((this.model.amount+0.5)*100)/100
        this.model.amount = Math.floor((this.model.amount) * 100) / 100
        this.modelvisivle = true
      },
      listModel() {
        let data = {}
        data.scope = 0
        data.ownId = this.$store.getters.id
        data.type = 1
        data.pageSize = 1000
        data.pageNum = 1
        data.isAdmin = 0
        listModel(data).then(res => {
          this.models = res.data.list
          this.models.forEach(model => {
            model.druglist = []
            model.amount = 0
            model.dmsMedicineModelItemList.forEach(item => {
              let data = {}
              selectById(item.id).then(res => {
                data = res.data
                data.days = item.days
                data.frequency = item.frequency
                data.medicalAdvice = item.medicalAdvice
                data.medicineUsage = item.medicineUsage
                data.usageMeans = item.usageMeans
                data.num = item.num
                data.usageNum = item.usageNum
                data.usageNumUnit = item.usageNumUnit
                model.amount += (data.price * item.num)
                model.druglist.push(deepClone(data))
              })
            })
            // model.amount = Math.floor((model.amount+0.5)*100)/100
            model.amount = Math.floor((model.amount) * 100) / 100
          })
        })
      },
      saveDrugPrescription() {
        if (this.refs.length>0){
          console.log(this.refs)
          saveDrugPrescription(this.refs,this.patient.registrationId, 4).then(res => {
            this.$notify({
              title: '成功',
              message: '已暂存选中的成药处方',
              type: 'success',
              duration: 2000
            })
          })
        }else {
          this.$notify({
            title: '提示',
            message: '未选择暂存项',
            type: 'warning',
            duration: 2000
          })
        }
      },
      getDrugPrescription() {
        let data = {}
        data.registrationId = this.patient.registrationId
        data.type = 4

        getDrugPrescription(data).then(res => {
          res.data.forEach(item => {
            this.prescriptionList.push(item)

          })
        })
        this.$notify({
          title: '成功',
          message: '已取出暂存的成药处方',
          type: 'success',
          duration: 2000
        })
      },

      /* getNonDrug(){
         let data = {}
         data.registrationId = this.patient.registrationId
         data.type = 0
         getNonDrug(data).then(res=>{
           res.data.dmsNonDrugItemRecordParamList.forEach(item=>{
             this.selectCheckred(item)
           })
           this.$notify({
             title: '成功',
             message: '已取出暂存的检查项',
             type: 'success',
             duration: 2000
           })
         })
       },*/
      addfreitem(val) {
        this.selectCheck(val)
      },
      getfreqList() {
        let data = {}
        data.staffId = this.$store.getters.id
        data.selectType = 6
        selectByType(data).then(res => {
          this.freqlist = res.data.drugList.filter(item => {
            if (item.typeId === 101)
              return true
          })
        })
      },
      refresh() {
        this.$confirm('未开立的处方都将清除,确认刷新?', '刷新', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.listByReg()
        })
      },
      /*deletepre() {
        if (this.prescriptionList.length <= 0) {
          this.$notify({
            title: '警告',
            message: '暂无数据',
            type: 'warning',
            duration: 2000
          })
          return
        }
        if (this.refs[0].name === undefined) {
          this.$notify({
            title: '警告',
            message: '请勾选数据',
            type: 'warning',
            duration: 2000
          })
          return
        }
        let data = this.refs[0].name
        this.$confirm('是否删除选中数据', '删除', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          this.prescriptionList = this.prescriptionList.filter(item => {
            if (item.name === data)
              return false
            return true
          })
        })
      },*/

      deletepre() {
        if (this.prescriptionList.length <= 0) {
          this.$notify({
            title: '提示',
            message: '暂无数据',
            type: 'warning',
            duration: 2000
          })
          return
        }
        if (this.refs.length <= 0 ) {
          this.$notify({
            title: '提示',
            message: '请勾选数据',
            type: 'warning',
            duration: 2000
          })
          return
        }
        this.$confirm('是否删除选中数据', '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          let status = []
          let ids = []
          this.refs.forEach(item =>{
            if (item.status === -1) {
              status.push(item.status)
            }else {
               ids.push(item.id)
            }
          })
          let data = ids.join(",")
          invalid(data).then(res => {
            this.$notify({
              title: '提示',
              message: "删除成功",
              type: 'success',
              duration: 2000
            })
          })
          this.listByReg()
        })
      },

      invalid() {
        if (this.prescriptionList.length <= 0) {
          this.$notify({
            title: '提示',
            message: '暂无数据',
            type: 'warning',
            duration: 2000
          })
          return
        }
        if (this.refs.length <= 0 ) {
          this.$notify({
            title: '提示',
            message: '请勾选数据',
            type: 'warning',
            duration: 2000
          })
          return
        }
        let ids = []
        let status = []
        this.refs.forEach(item =>{
          if (item.status === -1) {
            status.push(item.status)
          }else {
            ids.push(item.id)
          }
        })
        let data = ids.join(",")

        if (status.join(",") !== "") {
          this.$notify({
            title: '提示',
            message: '请勿作废未开立项目',
            type: 'warning',
            duration: 2000
          })
          return
        }
        this.$confirm('是否作废选中数据', '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          invalid(data).then(res => {
            this.$notify({
              title: '提示',
              message: res.message,
              type: 'success',
              duration: 2000
            })
            this.listByReg()
          })
        })
      },
      async listByReg() {
        listByReg(this.patient.registrationId).then(res => {
          this.prescriptionList = res.data
        })
      },
      handleSelectionChange(val) {
        this.refs = val
      },
      handleClick() {
        this.$notify({
          title: '提示',
          message: '您开立的项目中包含已开立的项目，请检查后重新提交！',
          type: 'warning',
          duration: 2000
        })
        return false;
      },
      async apply() {
        if (this.refs.length <= 0) {
          this.$notify({
            title: '提示',
            message: '未选中数据',
            type: 'warning',
            duration: 2000
          })
          return
        }else if(this.refs.length > 1){
          this.$notify({
            title: '提示',
            message: '一次操作最多可开立一条',
            type: 'warning',
            duration: 2000
          })
          return
        }
        let flags = 0
        this.refs.forEach(item => {
          if (item.status != -1) {
            flags = 1;
          }
        })
        if (flags === 1) {
          this.handleClick()
        }
        let data = this.refs[0]
        data.type = 5
        data.createStaffId = this.$store.getters.id
        data.registrationId = this.patient.registrationId
        data.dmsMedicineItemRecordList = data.druglist
        data.dmsMedicineItemRecordList.forEach(item => {
          item.drugId = item.id
        })
        apply(data).then(res => {
          if (res.data === 1) {
            this.$notify({
              title: '提示',
              message: "开立成功",
              type: 'success',
              duration: 2000
            })
          } else {
            this.$notify({
              title: '提示',
              message: "开立失败",
              type: 'warning',
              duration: 2000
            })
          }
          this.listByReg()
        })
      },
      createpre() {

        let data=0;
        if (this.oneprescription.name === undefined || this.oneprescription.name === "" || this.oneprescription.name === null) {
          this.$notify({
            title: "信息不完整",
            message: "请输入处方名！",
            type: "warning",
            duration: 2000,
          });
          return;
        }
        this.oneprescription.druglist.forEach(item=>{
          if (data===0 && (item.num === undefined || item.num === '' || item.num === null)) {
            this.$notify({
              title: "信息不完整",
              message: "请输入数量！",
              type: "warning",
              duration: 2000,
            });
            data=1;
            return;
          }else if (data===0 && (item.medicalAdvice === undefined || item.medicalAdvice === '' || item.medicalAdvice === null)) {
            this.$notify({
              title: "信息不完整",
              message: "请输入使用方法！",
              type: "warning",
              duration: 2000,
            });
            data=1;
            return;
          }else if ( data===0 && (item. frequency === undefined || item. frequency === '' || item. frequency === null)) {
            this.$notify({
              title: "信息不完整",
              message: "请输入频次！",
              type: "warning",
              duration: 2000,
            });
            data=1;
            return;
          }else if (data===0 && (item.days === undefined || item.days === '' || item.days === null)) {
            this.$notify({
              title: "信息不完整",
              message: "请输入天数！",
              type: "warning",
              duration: 2000,
            });
            data=1;
            return;
          }else if ( data===0 && (item.usageNum === undefined || item.usageNum === '' || item.usageNum === null)) {
            this.$notify({
              title: "信息不完整",
              message: "请输入用量！",
              type: "warning",
              duration: 2000,
            });
            data=1;
            return;
          }else if ( data===0 && (item. usageNumUnit === undefined || item. usageNumUnit === '' || item. usageNumUnit === null)) {
            this.$notify({
              title: "信息不完整",
              message: "请输入单位！",
              type: "warning",
              duration: 2000,
            });
            data=1;
            return;
          }
        })
        if(data===0){
          this.edit = false
          const status = []
          this.prescriptionList.forEach(item =>{
            status.push(item.status)
          })
          if (status.join(",").includes(-1)) {
            this.$notify({
              title: "提示",
              message: "已包含未开立项目,请先将项目开立再进行添加",
              type: "warning",
              duration: 2000,
            });
            return;
          }
          this.prescriptionList.push(this.oneprescription)
          this.dialogTableVisible = false
        }
      },
      changepre() {
        if (this.oneprescription.name === undefined || this.oneprescription.name === "" || this.oneprescription.name === null) {
          this.$notify({
            title: "信息不完整",
            message: "请输入处方名！",
            type: "warning",
            duration: 2000,
          });
          return;
        }
        this.dialogTableVisible = false
      },
      changenum(val) {
        this.oneprescription.amount = 0
        this.oneprescription.druglist.forEach(item => {
          this.oneprescription.amount += item.price * item.num
        })
        // this.oneprescription.amount = Math.floor((this.oneprescription.amount+0.5)*100)/100
        this.oneprescription.amount = Math.floor((this.oneprescription.amount) * 100) / 100
      },
      deldrug(row) {
        this.oneprescription.druglist = this.oneprescription.druglist.filter(item => {
          if (item.id === row.id) {
            this.oneprescription.amount = Math.floor(this.oneprescription.amount - (row.price * row.num))
            return false
          } else {
            return true
          }
        })
      },
      choosedrug(val) {


        let flag = 1
        this.oneprescription.druglist.forEach(item => {
          if (item.id === val.id) {
            item.num += 1
            flag = 0
          }
        })
        if (flag) {
          this.oneprescription.amount += val.price
          // this.oneprescription.amount = Math.floor((this.oneprescription.amount+0.5)*100)/100
          this.oneprescription.amount = Math.floor((this.oneprescription.amount) * 100) / 100
          this.oneprescription.druglist.push(val)
          this.oneprescription.druglist.forEach(item => {
            if (item.num === undefined)
              item.num = 1
          })
        }
      },
      async getdrugList() {
        let data = {}
        data.pageSize = this.page.pageSize
        data.pageNum = this.page.pageNum
        data.status = 1
        data.typeId = 101
        data.name = this.searchdrug
        data.mnemonicCode = this.mnemonicCode
        const response = await getdrugList(data)
        this.drugList = response.data.list
        this.total = response.data.total
      },
      addpre() {
        this.getfreqList()
        this.edit = false
        this.oneprescription = deepClone(defaultpre)
        this.getdrugList()
        this.dialogTableVisible = true
      },
      showDetail(row) {
        this.edit = true
        this.oneprescription = row
        this.dialogTableVisible = true
      },
      controlfast() {
        this.isclose = !this.isclose
        if (this.mainwidth === "65%")
          this.mainwidth = "80%"
        else
          this.mainwidth = "65%"
      },
      print(e) {
        const subOutputRankPrint = document.getElementById('prescription1')
        const newContent = subOutputRankPrint.innerHTML
        const oldContent = document.body.innerHTML
        document.body.innerHTML = newContent
        window.print()
        window.location.reload()
        document.body.innerHTML = oldContent
        return false
      }
    }
  }
</script>

