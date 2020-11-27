<template>
  <!-- 知情告知 -->
  <el-container>
    <el-aside :width="mainwidth" style="background:white;padding:0 10px 0 0">
      <aside style="margin:0 0 0 0">
        <el-button type="text" size="medium" @click="addcheck"><i class="el-icon-circle-plus-outline" />新增知情告知</el-button>
       <!-- <el-button type="primary" @click="print">打印</el-button>-->
      </aside>
      <el-table
        id="historyNoticeInform"
        ref="multipleTable"
        :data="record"
        border
        tooltip-effect="dark"
        style="width: 100%">
        <el-table-column
          align="center"
          prop="name"
          label="知情告知名称"
          width="200">
        </el-table-column>
        <el-table-column
          align="center"
          prop="content"
          label="知情告知内容"
          width="875">
        </el-table-column>
      </el-table>
    </el-aside>



  </el-container>


</template>
<script>
  import {getNondrugList} from '@/api/non_drug'
  import {apply,list,invalid,deleteById} from '@/api/outpatient/nondrugapply'
  import {getNondrugModelList} from '@/api/nondrugmodel'
  import {deepClone} from '@/utils'
  import {selectByType} from '@/api/outpatient/frequentuse'
  import {saveNonDrug,getNonDrug} from '@/api/outpatient/save'
  import { Promise, all } from 'q';

  /*下面是自己的*/
  import {showInformList} from '@/api/outpatient/frequentuse'   /*加载知情告知模板*/
  import {insertFamiliarInform,deleteFamiliarInform,updateFamiliarInform,selectFamiliarInform} from '@/api/outpatient/dmscase'   /*加载知情告知增删改查*/

  export default {
    props:['patient'],
    name:'Inspection',
    data(){
      return{
        mainwidth:"65%",
        isclose: true,
        informList:{},  //接受模板返回值
        total:0,        //分页
        selectInformParam:{     //查询模板需要的参数
          name:"",
          content:'',
          pageSize:10,
          pageNum:1
        },
        record: [],
        itemvisible:false,  //添加弹出框的控制
        itemvisible1:false,  //修改
        addInformParam:{   //添加用的对象
          registrationId:'',
          name:"",
          content:''
        },
        updateInformParam:{   //修改用的对象
          id:'',
          name:"",
          content:''
        },
      };
    },
    watch:{
      'patient' : function(newVal, oldVal){
        this.patient = newVal
        /*this.showInformList()*/
        this.selectFamiliarInform()
      },
    },
    created(){
      this.showInformList()  /*记载知情告知模板*/
    },

    methods:{

      /*加载知情告知模板*/
      showInformList(){
        this.informList={}
        showInformList(this.selectInformParam).then(res=>{
          if (res.data) {
            this.informList=res.data.list
            this.total=res.data.total
          }
        })
      },

      /*将模板加到显示区*/
      addModel(val){
        this.$confirm('是否确定将 模板:'+val.name+' 中的内容导入该患者的知情告知中', '导入模板', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          this.addInformParam.name=val.name
          this.addInformParam.content=val.content
          this.insertFamiliarInform()
        })
      },


     /*添加弹出框*/
      addcheck(){
        this.itemvisible = true
      },

     /*查询*/
      selectFamiliarInform() {
        this.record = [];
        let data = {
          registrationId: '',
          id: '',
          name: '',
          content: '',
        };
        data.registrationId = this.patient.registrationId;
        selectFamiliarInform(data).then((res) => {
          if (res.data) {
            this.record = res.data;
          }
        });
    },
      //添加
      insertFamiliarInform(){
        this.addInformParam.registrationId=this.patient.registrationId
        insertFamiliarInform(this.addInformParam).then(res=>{
          if(res.data===1){
            this.$notify({
              title: '成功',
              message: res.message,
              type: 'success',
              duration: 2000
            })
          }else {
            this.$notify({
              title: '失败',
              message: res.message,
              type: 'warning',
              duration: 2000
            })
          }
          this.itemvisible=false
          this.addInformParam.name=""
          this.addInformParam.content=""
          this.selectFamiliarInform()
        })
      },

      //删除
      deleteFamiliarInform(data1){
        let data={
          registrationId:'',
          id:'',
        }
        data.registrationId=this.patient.registrationId
        data.id=data1.id
        deleteFamiliarInform(data).then(res=>{
          if(res.data===1){
            this.$notify({
              title: '成功',
              message: res.message,
              type: 'success',
              duration: 2000
            })
          }else {
            this.$notify({
              title: '失败',
              message: res.message,
              type: 'warning',
              duration: 2000
            })
          }
          this.selectFamiliarInform()
        })
      },

      //修改
      updateInform(data1){
        this.itemvisible1=true
        this.updateInformParam.id=data1.id
        this.updateInformParam.name=data1.name
        this.updateInformParam.content=data1.content
      },
      updateFamiliarInform(data1){
        updateFamiliarInform(this.updateInformParam).then(res=>{
          if(res.data===1){
            this.$notify({
              title: '成功',
              message: res.message,
              type: 'success',
              duration: 2000
            })
          }else {
            this.$notify({
              title: '失败',
              message: res.message,
              type: 'warning',
              duration: 2000
            })
          }
          this.itemvisible1=false
          this.updateInformParam.name=""
          this.updateInformParam.content=""
          this.selectFamiliarInform()
        })
      },

      controlfast(){
        this.isclose=!this.isclose
        if(this.mainwidth==="65%")
          this.mainwidth="80%"
        else
          this.mainwidth="65%"
      },
      print(e){
        const subOutputRankPrint = document.getElementById('noticeInform')
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

