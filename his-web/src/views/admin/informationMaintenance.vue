<template>

  <el-form style="margin-top:20px">
    <label>预约流程</label>
    <Editor id="process" v-model="process" :disabled="disabled" :init="init"/>
    <label>预约电话</label>
    <input  v-model="phone"></input>
    <br>
    <label>预约须知</label>
    <Editor id="instructions" v-model="instructions" :disabled="disabled" :init="init"/>
    <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="updateMaintenanceParam">修改</el-button>
  </el-form>
</template>
 
<script>
  import tinymce from 'tinymce/tinymce'
  import Editor from '@tinymce/tinymce-vue'
  import 'tinymce/skins/lightgray/skin.min.css'
  import 'tinymce/themes/modern/theme'

  import 'tinymce/plugins/image';
  import 'tinymce/plugins/media';
  import 'tinymce/plugins/table';
  import 'tinymce/plugins/lists';
  import 'tinymce/plugins/contextmenu';
  import 'tinymce/plugins/wordcount';
  import 'tinymce/plugins/colorpicker';
  import 'tinymce/plugins/textcolor';

  import {selectMaintenanceParam, updateMaintenanceParam} from '@/api/user'
  export default {
    components: {
      Editor
    },
    props: {
      //传入一个value，使组件支持v-model绑定
      value: {
        type: String,
        default: ''
      },
      disabled: {
        type: Boolean,
        default: false
      },
      plugins: {
        type: [String, Array],
        default: 'lists image media table textcolor wordcount contextmenu'
      },
      toolbar: {
        type: [String, Array],
        default: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | lists image media table | removeformat'
      }
    },
    mounted() {
      tinymce.init({
        images_upload_handler: function (blobInfo, success, failure) {
          var reader = new FileReader();
          reader.readAsDataURL(blobInfo.blob());
          reader.onload = function () {
            success(this.result);
          };
        },
      })
    },
    data() {
      return {
        //初始化配置
        init: {
          // language_url: '/tinymce/langs/zh_CN.js',//语言包的路径
          // language: 'zh_CN',//语言
          height: 300,//编辑器高度
          images_upload_handler: function (blobInfo, success, failure) {
            var reader = new FileReader();
            reader.readAsDataURL(blobInfo.blob());
            reader.onload = function () {
              success(this.result);
            };
          },
          branding: false,//是否禁用“Powered by TinyMCE”
          menubar: false,//顶部菜单栏显示,
          // plugins: this.plugins,
          // toolbar: this.toolbar,
          // paste_data_images: true,
        },
        content: '',
        process:'',
        phone:'',
        instructions:'',
        id:'',
        maintenance:{
          id:'',
          process:'',
          phone:'',
          instructions:''
        },
      }
    },
    created() {
      this.selectMaintenanceParam();
    },
    methods:{
      updateMaintenanceParam(){
        updateMaintenanceParam({
          process: this.process,
          phone: this.phone,
          instructions: this.instructions,
          id: this.id
        }).then(res=>{
          if(res.data===1){
            this.$notify({
              title: '提示',
              message: "修改成功！",
              type: 'success',
              duration: 2000
            })
          }else {
            this.$notify({
              title: '提示',
              message: "修改失败！",
              type: 'warning',
              duration: 2000
            })
          }
        })
      },
      selectMaintenanceParam(){
        selectMaintenanceParam().then(res=>{
          console.log(res)
          if(res!=null || res!=undefined || res!=""){
            this.maintenance=res.data[0]
            this.process=res.data[0].process
            this.phone=res.data[0].phone
            this.instructions=res.data[0].instructions
            this.id=res.data[0].id
          }
        })
      }
    }
  }
</script>
<style></style>
