<template>
  <!-- 诊断目录管理 -->
  <div>
    <el-container style="padding: 0px; margin: 0 0 0 0">
      <!-- 列表 -->
      <el-aside width="30%" style="
          padding: 0 5px 0 0;
          background: white;
          border-style: dotted;
          border-width: 0px 1px 0px 0px;
          border-color: #c0c0c0;
        ">
        <el-button size="mini" disabled>请输入类别名 ：</el-button>
        <el-input v-model="search" size="small" placeholder="请输入类别名" style="width: 160px; margin-top: 20px; margin-left: 10px" class="filter-item" @keyup.enter.native="handleFilter"/>
        <el-table v-loading="catload" stripe max-height="660" ref="singleTable" style="margin-left: 5px; margin-top: 0px" highlight-current-row :data="menus" @current-change="handleCurrentChange">
          <el-table-column property="name" label="诊断分类目录"></el-table-column>
          <el-table-column align="right">
            <template slot="header">
              <el-button size="mini" type="text" @click="dialogVisible = true;catchange.name = '';dialogType = 'add';">
                <i class="el-icon-circle-plus" />新增
              </el-button>
            </template>
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="dialogVisible = true;catchange= scope.row;dialogType = 'edit';handleCurrentChange(row);">
                <i class="el-icon-edit"/>修改
              </el-button>
              <el-button type="text" size="mini"  @click="delDmsDiscat">
                <i class="el-icon-delete"/>删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="menuTotal > 0"
          :total="menuTotal"
          :page.sync="menuListQuery.pageNum"
          :limit.sync="menuListQuery.pageSize"
          @pagination="getMenu"
        />
      </el-aside>
      <!-- 每一列的表格详情 -->
      <el-main style="margin-left: 10px">
        <el-button disabled style="color: black">
          当前类别：{{ currentRow.name }}
        </el-button>
        <el-button type="text" v-if="currentRow.name" @click="dialogVisibleDis = true;dialogType = 'add';onedis.catId = currentRow.id;">
          <i class="el-icon-circle-plus" />新增诊断
        </el-button>
        <el-table :data="dis" v-loading="disload" style="margin-top: 30px">
          <el-table-column label="疾病编码" property="code" ></el-table-column>
          <el-table-column label="疾病名称" property="name"></el-table-column>
          <el-table-column label="ICD编码" property="icd"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="primary" size="small" @click="dialogVisibleDis = true;dialogType = 'edit';onedis = scope.row;onedis.catId = currentRow.id;">修改</el-button>
              <el-button type="danger" size="small" @click="delDmsDis(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getDis"/>
      </el-main>
    </el-container>
    <el-dialog :visible.sync="dialogVisible" width="500px" :title="dialogType === 'edit' ? '修改诊断分类目录' : '新增诊断分类目录'" @close="getDeplist">
      <el-form>
        <el-form-item label="诊断分类目录名称:">
          <el-input style="width: 300px" v-model="catchange.name"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: right; margin-top: 20px">
        <el-button type="danger" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="changeDmsDiscat">确认</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="dialogVisibleDis"
      width="500px"
      :title="dialogType === 'edit' ? '修改疾病信息' : '新增疾病信息'"
      @close="getDeplist"
    >
      <el-form :model="onedis" :rules="onedisTules" ref="onedis" >
        <el-form-item label="疾病编码:" prop="code">
          <el-input style="width: 300px" v-model="onedis.code"></el-input>
        </el-form-item>
        <el-form-item label="疾病名称:" prop="name">
          <el-input style="width: 300px" v-model="onedis.name"></el-input>
        </el-form-item>
        <el-form-item label="ICD编码:" prop="icd">
          <el-input style="width: 300px" v-model="onedis.icd"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: right; margin-top: 20px">
        <el-button type="danger" @click="dialogVisibleDis = false"
          >取消</el-button
        >
        <el-button type="primary" @click="changeDmsDis('onedis')">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getDmscatlist,
  getDmsDislist,
  createDmsDiscat,
  delDmsDiscat,
  editDmsDiscat,
  createDmsDis,
  editDmsDis,
  delDmsDis,
} from "@/api/diagnosis";
import Pagination from "@/components/Pagination";
export default {
  components: { Pagination },
  data() {
    return {
      onedis: {
        status: 1,
        catId: "",
        code: "",
        name: "",
        icd: "",
      },
      disload: false,
      dialogVisibleDis: false,
      catload: true,
      catchange: {
        name: "",
        status: 1,
      },
      dialogType: "",
      dialogVisible: false,
      search: "",
      catedit: false,
      total: 0,
      menuTotal: 0,
      iseditCat: false,
      menuListQuery: {
        name: "",
        pageSize: 10,
        pageNum: 1,
      },
      listQuery: {
        catId: "",
        code: "",
        name: "",
        icd: "",
        status: "",
        pageSize: 10,
        pageNum: 1,
      },
      dis: [],
      menus: [],
      currentRow: {
        id: "",
        name: "",
        status: 1,
      },
      onedisTules: {
        code: [
          { required: true, message: "请输入疾病编码", trigger: "blur" },
        ],
        name: [
          { required: true, message: "请输入疾病名称", trigger: "change" },
          { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
        ],
        icd: [{ required: true, message: "请输入ICD编码", trigger: "change" }],
      },
    };
  },
  created() {
    this.getMenu();
  },
  methods: {
    async getDis() {
      const res = await getDmsDislist(this.listQuery);
      this.dis = res.data.list;
      this.total = res.data.total;
    },
    async getMenu() {
      this.catload = true;
      const res = await getDmscatlist(this.menuListQuery);
      this.menus = res.data.list;
      this.menuTotal = res.data.total;
      this.catload = false;
    },
    handleFilter() {
      this.menuListQuery.pageSize = 10
      this.menuListQuery.pageNum = 1
      this.menuListQuery.name = this.search
      this.getMenu()
    },
    async handleCurrentChange(val) {
      this.listQuery.catId = val.id;
      const res = await getDmsDislist(this.listQuery);
      this.dis = res.data.list;
      this.total = res.data.total;
      this.currentRow = val;
    },
    async getDisList() {
      this.disload = true;
      const res = await getDmsDislist(this.listQuery);
      this.dis = res.data.list;
      this.total = res.data.total;
      this.disload = false;
    },
    delDmsDis(row) {
      this.$confirm("确认删除当前疾病?", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        delDmsDis(row.id).then((res) => {
          this.$notify({
            title: "提示",
            message: res.message,
            type: "success",
            duration: 2000,
          });
          this.getDisList();
        });
      });
    },

    changeDmsDis(){
      if(this.dialogType==='edit'){
        editDmsDis(this.onedis).then(res=>{
          this.$notify({
            title: '提示',
            message: res.message,
            type: 'success',
            duration: 2000
          })
          this.getDisList()
          this.dialogVisibleDis=false
        })

      }
      else{
        this.onedis.status=1
        createDmsDis(this.onedis).then(res=>{
          if(res.data === 1){
            this.$notify({
              title: '提示',
              message: res.message,
              type: 'success',
              duration: 2000
            })
            this.getDisList()
            this.dialogVisibleDis=false
          }else {
            this.$notify({
              title: '提示',
              message: "请勿添加重复数据",
              type: 'warning',
              duration: 2000
            })
          }
        })
      }
    },
    changeDmsDiscat() {
      if (!this.catchange.name) {
        this.$notify({
          title: "提示",
          message: "诊断分类目录名称不能为空",
          type: "warning",
          duration: 3500,
        });
        return;
      }
      if (this.dialogType === "edit") {
        this.catchange.id = this.currentRow.id;
        this.currentRow.name = this.catchange.name;
        editDmsDiscat(this.catchange).then((res) => {
          this.$notify({
            title: "提示",
            message: res.message,
            type: "success",
            duration: 2000,
          });
        });
        this.dialogVisible = false;
      } else {
        createDmsDiscat(this.catchange).then((res) => {
          if (res.data === 1) {
            this.$notify({
              title: "提示",
              message: res.message,
              type: "success",
              duration: 2000,
            });
            this.getMenu();
            this.dialogVisible = false;
          } else {
            this.$notify({
              title: "提示",
              message: "请勿添加重复数据",
              type: "warning",
              duration: 2000,
            });
          }
        });
      }
    },
    delDmsDiscat() {
      this.$confirm("确认删除当前目录?", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        delDmsDiscat(this.currentRow.id).then((res) => {
          this.$notify({
            title: "提示",
            message: res.message,
            type: "success",
            duration: 2000,
          });
          this.currentRow.name = "";
          this.dis = [];
          this.getMenu();
        });
      });
    },
    getDeplist(){
      this.dialogVisible=false;
      this.dialogVisibleDis=false;
      this.onedis = {}
    },
    selectsearch(){
      this.menuListQuery.name=this.search
      getDmscatlist(this.menuListQuery).then(res=>{
        this.menus = res.data.list
        this.menuTotal = res.data.total
      })
    }
  }
}
</script>
<style>
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
