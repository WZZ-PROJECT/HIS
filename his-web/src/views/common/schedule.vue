<template>
  <!-- 排班表 -->
  <div style="margin:10px 10px 10px 10px">
    <el-form inline>
      <el-form-item label="排班日期">
        <el-date-picker
          v-model="starttime"
          type="date"
          placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-select placeholder="值班医生名称" clearable  v-model="listQuery.staffId" style="width:180px">
        <el-option v-for="item in staff" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-select placeholder="挂号科室" clearable  v-model="listQuery.deptId" style="width:180px">
        <el-option v-for="item in alldept" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
      </el-button>
      <el-button @click="handleReset">清空</el-button>
    </el-form>
    <div style="padding: 5px 5px 5px 5px; margin-top:20px">
      <el-table  :data="schedule" border>
        <el-table-column label="日期" prop="date"></el-table-column>
        <el-table-column label="午别" prop="noon">
          <template slot-scope="scope">
            <span v-if="scope.row.noon===0">上午</span>
            <span v-if="scope.row.noon===1">下午</span>
          </template>
        </el-table-column>
        <el-table-column label="科室" prop="deptName"></el-table-column>
        <el-table-column label="值班医生" prop="staffName"></el-table-column>
        <el-table-column label="挂号级别" prop="registrationRank"></el-table-column>
        <el-table-column label="排班限额" prop="skLimit"></el-table-column>
        <el-table-column label="剩余号数" prop="remain"></el-table-column>
      </el-table>
      <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="querySkd" />
    </div>
  </div>
</template>
<script>
import {querySkd} from '@/api/schedule'
import {parseTime} from '@/utils'
import checkPermission from '@/utils/permission' // 权限判断函数
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { getAllDep} from '@/api/department'
import { getUserListAll} from '@/api/user'
export default {
  components: {Pagination},
  data(){
    return{
      staff:[],
      schedule:[],
      alldept:[],
      total: 0,
      starttime:'',
      listQuery:{
        staffId:'',
        staffName:'',
        startDate:'',
        deptId:'',
        pageSize: 10,
        pageNum: 1
      }
    };
  },
  created(){
    this.querySkd(),
    this.queryAllStaff()
    this.getAllDep()
  },
  methods:{
    handleFilter() {
      if(this.starttime === null || this.starttime === '') {
        this.listQuery.startDate = ''
      } else {
        this.listQuery.startDate = parseTime(this.starttime)
      }
      this.listQuery.pageNum = 1
      this.listQuery.pageSize = 10
      this.querySkd()
    },
    queryAllStaff() {
      getUserListAll().then(res=>{
        this.staff = res.data
      })
    },
    handleReset() {
      this.starttime = '';
      this.listQuery.staffName = '';
      this.listQuery.deptId = '';
    },
    getAllDep(){
      getAllDep().then(res=>{
        this.alldept = res.data
      })
    },
    querySkd() {
      if (checkPermission([1, 7])) {
        querySkd(this.listQuery).then(res => {
          this.schedule = res.data.list
          this.total = res.data.total
          this.schedule.forEach(item => {
            item.date = parseTime(item.date).substr(0, 10)
          })
        })
      } else {
        querySkd(this.listQuery).then(res => {
          this.schedule = res.data.list
          this.total = res.data.total
          this.schedule.forEach(item => {
            item.date = parseTime(item.date).substr(0, 10)
          })
        })
      }

    }
  }
}
</script>
