<template>
  <!-- 知情告知 -->
  <el-container>
    <el-aside :width="mainwidth" style="background:white;padding:0 10px 0 0">
      <aside style="margin:0 0 0 0">
        <el-button type="text" size="medium" @click="addcheck"><i class="el-icon-circle-plus-outline" />新增知情告知</el-button>
        <!--<el-button style="float:right" @click="controlfast"><i v-show="!isclose" class="el-icon-caret-right" /><i v-show="isclose" class="el-icon-caret-left" />  </el-button>-->
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
  import {selectFamiliarInform} from '@/api/outpatient/dmscase'   /*加载知情告知增删改查*/

  export default {
    props:['patient'],
    name:'Inspection',
    data(){
      return{
        mainwidth:"65%",

        informList:{},  //接受模板返回值
        total:0,        //分页
        selectInformParam:{     //查询模板需要的参数
          name:"",
          content:'',
          pageSize:10,
          pageNum:1
        },
        record:{},
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

     /*查询*/
      selectFamiliarInform(){
        this.record={}
        let data={
          registrationId:'',
          id:'',
          name:'',
          content:''
        }
        data.registrationId=this.patient.registrationId
        selectFamiliarInform(data).then(res=>{
          this.record=res.data
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

