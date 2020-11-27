<template>
  <div>
    <div style="margin-top:40px;margin-left:50px;margin-bottom:30px">
      科室选择:
      <el-select placeholder="科室选择" v-model="listQuery.deptId" clearable style="width: 130px" class="filter-item" filterable @change="getdep">
        <el-option v-for="item in alldepart" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <span class="demonstration">开始时间:</span>
      <el-date-picker
        v-model="starttime"
        default-value="2019-06-01"
        align="right"
        type="date"
        placeholder="选择日期">
      </el-date-picker>
      <span style="margin-left:30px" class="demonstration">结束时间:</span>
      <el-date-picker
        v-model="endtime"
        align="right"
        type="date"
        placeholder="选择日期">
      </el-date-picker>
      <el-button type="primary" @click="queryDeptPersonalWorkloadList">查询</el-button>
    </div>
    <el-table :data="work" stripe border >
      <el-table-column label="医生姓名" prop="staffName" width="120px"></el-table-column>
      <el-table-column label="科室" width="120px" prop="deptName"></el-table-column>
      <el-table-column label="成药项目总费用" width="120px" prop="medicineAmount"></el-table-column>
      <!-- <el-table-column label="成药项目总费用" width="120px" prop="medicineAmount"></el-table-column> -->
      <el-table-column label="草药项目总费用" width="120px" prop="herbalAmount"></el-table-column>
      <el-table-column label="检查项目总费用" width="120px" prop="checkAmount"></el-table-column>
      <el-table-column label="检验项目总费用" width="120px" prop="testAmount"></el-table-column>
      <el-table-column label="处置项目总费用" width="120px" prop="dispositionAmount"></el-table-column>
      <el-table-column label="挂号总费用" width="100px" prop="registrationAmount"></el-table-column>
      <el-table-column label="费用总计" prop="amount"></el-table-column>
      <el-table-column label="挂号数量" prop="registrationNum"></el-table-column>
      <!-- <el-table-column label="执行检查项目总费用" width="150px" prop="registrationNum"></el-table-column>
      <el-table-column label="执行检查项目总费用" width="150px" prop="excuteCheckAmount"></el-table-column>
      <el-table-column label="执行检查项目总费用" width="150px" prop="excuteTestAmount"></el-table-column>
      <el-table-column label="执行非药品项目总费用" width="150px" prop="excuteDispositionAmount"></el-table-column> -->
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="queryDeptPersonalWorkloadList" />
  </div>
</template>
<script>
import {queryDeptPersonalWorkloadList} from '@/api/workload'
import {parseTime} from '@/utils'
import {getAllDep} from '@/api/department'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
export default {
  components: {Pagination},
  data(){
    return{
      starttime:'',
      endtime:'',
      work:[],
      total:0,
      alldepart:[],
      listQuery:{
        startDatetime:'',
        endDatetime:'',
        deptId:'',
        pageSize:10,
        pageNum:1
      }
    }
  },
  created(){
    this.getAlldep()
  },
  methods:{
    async getAlldep(){
      const res = await getAllDep()
      this.alldepart=res.data
    },
    getdep(val){
      this.depart = val
    },
    queryDeptPersonalWorkloadList(){
      this.listQuery.startDatetime = parseTime(this.starttime)
      this.listQuery.endDatetime = parseTime(this.endtime)
      this.listQuery.deptId = this.depart
      if (this.listQuery.deptId === null || this.listQuery.deptId === '') {
        this.$notify({
          title: '警告',
          message: '请选择科室进行查询',
          type: 'warning',
          duration: 2000
        })
        return
      }
      if (this.starttime === null || this.starttime === '') {
        this.$notify({
          title: '警告',
          message: '请选择开始时间进行查询',
          type: 'warning',
          duration: 2000
        })
        return
      }
      if (this.endtime === null || this.endtime === '') {
        this.$notify({
          title: '警告',
          message: '请选择结束时间进行查询',
          type: 'warning',
          duration: 2000
        })
        return
      }
      queryDeptPersonalWorkloadList(this.listQuery).then(res=>{
        this.work = res.data.list
        this.total = res.data.total
      })
    }
  }
}
</script>

