<template>
  <!-- 知情告知管理 -->
  <div>
    <!--查询-->
    <div style="margin-left:40px;margin-top:40px">
      <el-form inline>
        <el-form-item label="知情告知名字">
          <el-input placeholder="模板名字" v-model="selectInformParam.name" style="width:200px"></el-input>
        </el-form-item>
<!--        <el-form-item label="知情告知内容">-->
<!--          <el-input placeholder="模板内容" maxlength="500" v-model="selectInformParam.content" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" style="width:200px"></el-input>&lt;!&ndash;v-model="selectInformParam.content"&ndash;&gt;-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button type="primary" @click="showInformList">查询</el-button>
          <el-button type="primary" @click="showitemlist">增加知情告知</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--查询后渲染-->
    <el-table  :data="informList"  style="margin-left:2%;width:95%">
      <el-table-column label="知情告知名字" prop="name"></el-table-column>
      <el-table-column label="知情告知内容" prop="content"></el-table-column>
      <el-table-column label="">
        <template slot-scope="scope">
          <el-button type="primary" @click="updateInformButton(scope.row)">修改</el-button>
          <el-button type="primary" @click="deleteInform(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="selectInformParam.pageNum" :limit.sync="selectInformParam.pageSize" @pagination="showInformList" />

    <!--添加-->
    <el-dialog :visible.sync="itemvisible" title="增加知情告知" width="600px">
      <el-form>
        <el-form-item label="知情告知名字">
          <el-input placeholder="模板名字" v-model="addInformParam.name" style="width:400px"></el-input>
        </el-form-item>
        <el-form-item label="知情告知内容">
          <el-input placeholder="模板内容" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" v-model="addInformParam.content" style="width:400px"></el-input>
        </el-form-item>
        <el-button type="primary" @click="addInform">增加</el-button>
      </el-form>
    </el-dialog>

    <el-dialog :visible.sync="itemvisible1" title="修改知情告知" width="600px">
      <el-form>
        <el-form-item label="知情告知名字">
          <el-input placeholder="模板名字" v-model="updateInformParam.name" style="width:400px"></el-input>
        </el-form-item>
        <el-form-item label="知情告知内容">
          <el-input placeholder="模板内容" maxlength="500" type="textarea" :autosize="{ minRows: 2, maxRows: 3}" v-model="updateInformParam.content" style="width:400px"></el-input>
        </el-form-item>
        <el-button type="primary" @click="updateInform">修改</el-button>
      </el-form>
    </el-dialog>

  </div>
</template>
<script>
import {addInform,showInformList,deleteInform,updateInform} from '@/api/outpatient/frequentuse'
import Pagination from '@/components/Pagination'
export default {
  components: {Pagination},
  data(){
    return{
      loadItemList:false,
      itemvisible:false,  //添加弹出框
      itemvisible1:false, //修改弹出框
      total:0,
      addInformParam:{
        name:"",
        content:''
      },
      selectInformParam:{
        name:"",
        content:'',
        pageSize:10,
        pageNum:1
      },
      informList:{},
      updateInformParam:{
        id:'',
        name:"",
        content:''
      },

    }
  },
  created() {
    this.showInformList()
  },
  methods: {
    showitemlist(){
      this.itemvisible = true
    },
    //添加
    addInform(){
      addInform(this.addInformParam).then(res=>{
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
        this.itemvisible=!this.itemvisible
        this.addInformParam.name=""
        this.addInformParam.content=""
        this.showInformList()
      })
    },

    //查询
    showInformList(){
      this.informList={}
      showInformList(this.selectInformParam).then(res=>{
        this.informList=res.data.list
        this.total=res.data.total
      })
    },
    //删除
    deleteInform(data){
      deleteInform(data.id).then(res=>{
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
        this.showInformList()
      })
    },

    //修改
    updateInformButton(data){
      this.updateInformParam.id=data.id
      this.updateInformParam.name=data.name
      this.updateInformParam.content=data.content
      this.itemvisible1=!this.itemvisible1
    },
    updateInform(){
      updateInform(this.updateInformParam).then(res=>{
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
        this.itemvisible1=!this.itemvisible1
        this.updateInformParam.name=""
        this.updateInformParam.content=""
        this.showInformList()
      })
    }
  },
}
</script>
<style>

</style>
