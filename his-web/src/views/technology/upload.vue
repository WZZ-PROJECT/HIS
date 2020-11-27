<template>
  <div class="upload-container">
    <el-form :model="check" ref="check" :rules="rules">
      <el-form-item label="检查结果" prop="checkResult">
        <el-input style="width:80%" type="textarea" :autosize="{ minRows: 2, maxRows: 6}" v-model="check.checkResult" placeholder="" ></el-input>
      </el-form-item>

      <el-form-item label="" align="center">
        <el-button icon='el-icon-upload' size="mini" :style="{background:color,borderColor:color}"
                  @click=" dialogVisible=true" type="primary">上传图片
        </el-button>
        <el-button type="primary" size="mini" @click="uploadResult('check')">确 定</el-button>
      </el-form-item>
    </el-form>
    <el-dialog append-to-body :visible.sync="dialogVisible">
      <el-upload class="editor-slide-upload"
                 :action="dataObj.host"
                 :data="dataObj"
                 :multiple="true"
                 :file-list="fileList"
                 :show-file-list="true"
                 list-type="picture-card"
                 :on-remove="handleRemove"
                 :on-success="handleSuccess"
                 :before-upload="beforeUpload">
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">只能上传  jpg/png  文件</div>
      </el-upload>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="handleSubmit">确 定</el-button>
    </el-dialog>
  </div>
</template>

<script>
  import {policy,uploadResult} from '@/api/upload'

  export default {
    props:['uploadpatient'],
    name: 'Upload',
    data() {
      return {
        rules: {
          checkResult: [
            { required: true, message: '请输入检查结果', trigger: 'blur' },
            { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
          ]
        },
        check:{
          checkResult:'',
        },
        color: '#1890ff',
        dialogVisible: false,
        listObj: {},
        arr:[],
        fileList: [],
        dataObj: {
          policy: '',
          signature: '',
          key: '',
          ossaccessKeyId: '',
          dir: '',
          host: ''
        }
      }
    },
    watch:{
    'uploadpatient' : function(newVal, oldVal){
        this.uploadpatient = newVal
      },
    },
    created(){
      policy().then(res=>{
        this.dataObj = res.data
      })
    },
    methods: {
      uploadResult(check){
        this.$refs[check].validate((valid) => {
          if (valid) {
            let data = {}
            data.id = this.uploadpatient
            data.executeStaffId = this.$store.getters.id
            data.checkResult = this.check.checkResult
            data.resultImgUrlList = ''
            this.arr.forEach(item=>{
              data.resultImgUrlList += (item.url+',')
            })
            data.resultImgUrlList = data.resultImgUrlList.substr(0,data.resultImgUrlList.length-1)
            uploadResult(data).then(res=>{
              this.$emit('reflect',res)
            })
            this.dataObj = {}
            this.arr = []
            this.listObj = {}
            this.fileList = []
            this.check.checkResult=''
          }
          return
        })
      },
      checkAllSuccess() {
        return Object.keys(this.listObj).every(item => this.listObj[item].hasSuccess)
      },
      handleSubmit() {
        const arr = Object.keys(this.listObj).map(v => this.listObj[v])
        if (!this.checkAllSuccess()) {
          this.$message('请等待所有图片上传成功 或 出现了网络问题，请刷新页面重新上传！')
          return
        }
        this.arr = arr
        this.dialogVisible = false;
        this.reflect()
      },
      handleSuccess(response, file) {
        const uid = file.uid;
        const objKeyArr = Object.keys(this.listObj)
        for (let i = 0, len = objKeyArr.length; i < len; i++) {
          if (this.listObj[objKeyArr[i]].uid === uid) {
            this.listObj[objKeyArr[i]].url = this.dataObj.host + '/' + this.dataObj.dir + '/' + file.name;
            this.listObj[objKeyArr[i]].hasSuccess = true;
            return
          }
        }
        this.$notify({
            title: '成功',
            message: '上传成功',
            type: 'success',
            duration: 2000
          })
      },
      handleRemove(file) {
        const uid = file.uid;
        const objKeyArr = Object.keys(this.listObj);
        for (let i = 0, len = objKeyArr.length; i < len; i++) {
          if (this.listObj[objKeyArr[i]].uid === uid) {
            delete this.listObj[objKeyArr[i]];
            return
          }
        }
      },
      beforeUpload(file) {
        let extension = file.name.split(".")[1];
        let extensionList = ["jpg", "png"];
        if (extensionList.indexOf(extension) < 0) {
          this.$message.warning("只能上传jpg / png文件");
          return false;
        }
        console.log(file);
        const _self = this
        const fileName = file.uid;
        this.listObj[fileName] = {};
        return new Promise((resolve, reject) => {
          policy().then(response => {
            _self.dataObj.policy = response.data.policy;
            _self.dataObj.signature = response.data.signature;
            _self.dataObj.ossaccessKeyId = response.data.accessKeyId;
            _self.dataObj.key = response.data.dir + '/${filename}';
            _self.dataObj.dir = response.data.dir;
            _self.dataObj.host = response.data.host;
            _self.listObj[fileName] = {hasSuccess: false, uid: file.uid, width: this.width, height: this.height};
            resolve(true)
          }).catch(err => {

            reject(false)
          })
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .upload-container .editor-slide-upload{
    margin-bottom: 20px;
  }
</style>
