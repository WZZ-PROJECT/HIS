<template>
  <!-- 成药处方 -->
  <div>
    <el-container>
      <el-header style="padding:0 0 0 0">
        <div style="margin-left:1700px;margin-top:15px">
          <el-form inline>
            <el-form-item>
              <el-button type="primary" @click="print">打印</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-header>
      <el-aside id="inform" :width="mainwidth" style="background:white;padding:0 10px 0 0">
        <aside style="margin:0 0 0 0">
          <el-button type="text" size="medium" @click="addpre"><i class="el-icon-circle-plus-outline"  />新增知情告知</el-button>
          <el-button type="text" size="medium" @click="deletepre"><i class="el-icon-remove-outline" />删除处方</el-button>
          <el-button type="text" size="medium" @click="apply"><i class="el-icon-circle-check" />开立处方</el-button>
          <el-button type="text" size="medium" @click="invalid"><i class="el-icon-circle-close" />作废处方</el-button>
          <el-button type="text" size="medium" @click="saveDrugPrescription"><i class="el-icon-upload2" />暂存</el-button>
          <el-button type="text" size="medium" @click="getDrugPrescription"><i class="el-icon-download" />取出暂存项</el-button>
          <el-button type="text" size="medium" @click="refresh"><i class="el-icon-refresh" />刷新</el-button>
          <el-button style="float:right" @click="controlfast"><i v-show="!isclose" class="el-icon-caret-right" /><i v-show="isclose" class="el-icon-caret-left" />  </el-button>
        </aside>
        <el-table
          ref="multipleTable"
          :data="prescriptionList"
          tooltip-effect="dark"
          style="width: 100%"
          cell-style="text-align:center"
          header-cell-style="text-align:center"
          @selection-change="handleSelectionChange">
          <el-table-column
            align="center"
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            align="center"
            label="告知名称">
            <template slot-scope="scope">{{ scope.row.name }}</template>
          </el-table-column>
          <el-table-column
            align="center"
            label="告知内容">
            <template slot-scope="scope">{{ scope.row.name }}</template>
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
    </el-container>
  </div>
</template>
<script>
  import {getdrugList,selectById} from '@/api/drug'
  import Pagination from '@/components/Pagination'
  import {apply,listByReg,invalid} from '@/api/outpatient/prescription'
  import {deepClone} from '@/utils'
  import {selectByType} from '@/api/outpatient/frequentuse'
  import {getDrugPrescription,saveDrugPrescription} from '@/api/outpatient/save'
  import {listModel} from '@/api/drugmodel'
  const defaultpre={
    name:'',
    druglist:[],
    amount:0,
    status:-1,
  }
  export default {
    props:['patient'],
    components: {Pagination},
    name:'Prescription',
    data(){
      return{
        modelvisivle:false,
        model:{},
        models:[],
        freqlist:[],
        refs:[],
        prescriptionList:[],
        oneprescription:{
          name:'',
          druglist:[],
          amount:0,
          status:-1,
        },
        edit:false,
        test:'',
        num:0,
        dialogTableVisible:false,
        activeName:'first',
        isclose:true,
        record:{
          main:'test'
        },
        page:{
          pageNum:1,
          pageSize:10,
        },
        searchdrug:'',
        drugList:[],
        total:0,
        mainwidth:"65%",
        activeNames: ['1'],
      };
    },
    watch:{
      'patient' : function(newVal, oldVal){
        this.patient = newVal
        this.listByReg()

      },
    },
    created(){
      this.listModel()
    },
    methods:{
      addmodel(val){
        val.amount = Math.floor((val.amount+0.5)*100)/100
        val.status = -1
        this.prescriptionList.push(val)
      },
      selectmodel(val){
        this.model = deepClone(val)
        this.model.amount = Math.floor((this.model.amount+0.5)*100)/100
        this.modelvisivle = true
      },
      listModel(){
        let data = {}
        data.scope = 0
        data.ownId = this.$store.getters.id
        data.type = 1
        data.pageSize = 1000
        data.pageNum = 1
        data.isAdmin = 0
        listModel(data).then(res=>{
          this.models = res.data.list
          this.models.forEach(model=>{
            model.druglist = []
            model.amount = 0
            model.dmsMedicineModelItemList.forEach(item=>{
              let data = {}
              selectById(item.id).then(res=>{
                data = res.data
                data.days = item.days
                data.frequency = item.frequency
                data.medicalAdvice = item.medicalAdvice
                data.medicineUsage = item.medicineUsage
                data.usageMeans = item.usageMeans
                data.num = item.num
                data.usageNum = item.usageNum
                data.usageNumUnit = item.usageNumUnit
                model.amount += (data.price*item.num)
                model.druglist.push(deepClone(data))
              })
            })
            model.amount = Math.floor((model.amount+0.5)*100)/100
          })
        })
      },
      saveDrugPrescription(){
        saveDrugPrescription(this.refs,this.patient.registrationId,4).then(res=>{
          this.$notify({
            title: '成功',
            message: '已暂存选中的成药处方',
            type: 'success',
            duration: 2000
          })
        })
      },
      getDrugPrescription(){
        let data = {}
        data.registrationId = this.patient.registrationId
        data.type = 4

        getDrugPrescription(data).then(res=>{
          res.data.forEach(item=>{
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
      addfreitem(val){
        this.selectCheck(val)
      },
      getfreqList(){
        let data = {}
        data.staffId = this.$store.getters.id
        data.selectType = 6
        selectByType(data).then(res=>{
          this.freqlist = res.data.drugList.filter(item=>{
            if(item.typeId === 101)
              return true
          })
        })
      },
      refresh(){
        this.$confirm('未开立的处方都将清除,确认刷新?', '刷新', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          this.listByReg()
        })
      },
      deletepre(){
        let data = this.refs[0].name
        this.prescriptionList=this.prescriptionList.filter(item=>{
          if(item.name===data)
            return false
          return true
        })
      },
      invalid(){
        let data = this.refs[0].id
        invalid(data).then(res=>{
          this.$notify({
            title: '成功',
            message: res.message,
            type: 'success',
            duration: 2000
          })
          this.listByReg()
        })
      },
      async listByReg(){
        listByReg(this.patient.registrationId).then(res=>{
          this.prescriptionList=res.data
        })
      },
      handleSelectionChange(val){
        this.refs = val
      },
      async apply(){
        let data = this.refs[0]
        data.type = 5
        data.createStaffId = this.$store.getters.id
        data.registrationId = this.patient.registrationId
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
          this.listByReg()
        })
      },
      createpre(){
        this.edit = false
        this.prescriptionList.push(this.oneprescription)
        this.dialogTableVisible = false
      },
      changepre(){
        this.dialogTableVisible =false
      },
      changenum(val){
        this.oneprescription.amount=0
        this.oneprescription.druglist.forEach(item=>{
          this.oneprescription.amount+=item.price*item.num
        })
        this.oneprescription.amount = Math.floor((this.oneprescription.amount+0.5)*100)/100
      },
      deldrug(row){
        this.oneprescription.druglist = this.oneprescription.druglist.filter(item=>{
          if(item.id===row.id)
            return false
          return true
        })
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
      async getdrugList() {
        let data = {}
        data.pageSize = this.page.pageSize
        data.pageNum = this.page.pageNum
        data.status = 1
        data.typeId = 101
        data.name = this.searchdrug
        const response = await getdrugList(data)
        this.drugList = response.data.list
        this.total = response.data.total
      },
      addpre(){
        this.getfreqList()
        this.edit = false
        this.oneprescription = deepClone(defaultpre)
        this.getdrugList()
        this.dialogTableVisible = true
      },
      showDetail(row){
        this.edit = true
        this.oneprescription = row
        this.dialogTableVisible=true
      },
      controlfast(){
        this.isclose=!this.isclose
        if(this.mainwidth==="65%")
          this.mainwidth="80%"
        else
          this.mainwidth="65%"
      },
      print(e){
        const subOutputRankPrint = document.getElementById('inform')
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

