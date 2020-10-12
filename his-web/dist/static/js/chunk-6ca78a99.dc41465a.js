(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6ca78a99"],{"0cf7":function(e,t,a){"use strict";var r=a("1f5d"),n=a.n(r);n.a},"1f5d":function(e,t,a){},"310b":function(e,t,a){"use strict";a.r(t);var r=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container"},[a("div",{staticClass:"filter-container"},[a("el-input",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{placeholder:"药品编码"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleFilter(t)}},model:{value:e.listQuery.code,callback:function(t){e.$set(e.listQuery,"code",t)},expression:"listQuery.code"}}),e._v(" "),a("el-input",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{placeholder:"药品名称"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleFilter(t)}},model:{value:e.listQuery.name,callback:function(t){e.$set(e.listQuery,"name",t)},expression:"listQuery.name"}}),e._v(" "),a("el-select",{staticClass:"filter-item",staticStyle:{width:"130px"},attrs:{placeholder:"药品分类",clearable:"",filterable:""},model:{value:e.listQuery.typeID,callback:function(t){e.$set(e.listQuery,"typeID",t)},expression:"listQuery.typeID"}},e._l([{key:"101",label:"西药"},{key:"102",label:"中成药"},{key:"103",label:"中草药"}],(function(e){return a("el-option",{key:e.key,attrs:{label:e.label,value:e.key}})})),1),e._v(" "),a("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search"},on:{click:e.handleFilter}},[e._v("\n      搜索药品\n    ")]),e._v(" "),a("el-button",{staticClass:"filter-item",staticStyle:{"margin-left":"10px"},attrs:{type:"primary",icon:"el-icon-edit"},on:{click:e.handleAdd}},[e._v("\n      新增药品\n    ")]),e._v(" "),a("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",attrs:{loading:e.downloadLoading,type:"primary",icon:"el-icon-download"},on:{click:e.handleDownload}},[e._v("\n      导出药品\n    ")]),e._v(" "),a("el-button",{directives:[{name:"waves",rawName:"v-waves"}],staticClass:"filter-item",attrs:{loading:e.downloadLoading,type:"danger",icon:"el-icon-download"},on:{click:e.handleSomeDelete}},[e._v("\n      批量删除\n    ")])],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],staticStyle:{width:"100%","margin-top":"30px"},attrs:{data:e.drugList,border:""},on:{"selection-change":e.changedep}},[a("el-table-column",{attrs:{type:"selection",width:"55"}}),e._v(" "),a("el-table-column",{attrs:{prop:"code",align:"center",fixed:"",label:"药品编码",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.code)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",fixed:"",label:"药品名称",width:"200"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.name)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"药品规格",width:"140"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.format)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"药品单价",width:"70"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.price)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"包装单位",width:"70"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.unit)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"生产厂家",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.manufacturer)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"药品剂型"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.dosage.name)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"药品单价"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.price)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"药品类型"},scopedSlots:e._u([{key:"default",fn:function(t){return[101===t.row.typeId?a("span",[e._v("西药")]):e._e(),e._v(" "),102===t.row.typeId?a("span",[e._v("中成药")]):e._e(),e._v(" "),103===t.row.typeId?a("span",[e._v("中草药")]):e._e()]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"助记码",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.mnemonicCode)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"库存"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.stock)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"通用名"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.genericName)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",fixed:"right",label:"操作",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(a){return e.handleEdit(t.row)}}},[e._v("修改")]),e._v(" "),a("el-button",{attrs:{type:"danger",size:"small"},on:{click:function(a){return e.handleDelete(t.row)}}},[e._v("删除")])]}}])})],1),e._v(" "),a("el-dialog",{attrs:{visible:e.dialogVisible,width:"650px",title:"edit"===e.dialogType?"修改药品信息":"新增药品"},on:{"update:visible":function(t){e.dialogVisible=t}}},[a("el-form",{ref:"depart",attrs:{model:e.drug,"label-width":"80px",inline:"","label-position":"left",rules:e.rules}},[a("el-form-item",{attrs:{label:"药品编号",prop:"code"}},[a("el-input",{staticStyle:{width:"350px"},attrs:{placeholder:"编号"},model:{value:e.drug.code,callback:function(t){e.$set(e.drug,"code",t)},expression:"drug.code"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"药品名称",prop:"name"}},[a("el-input",{staticStyle:{width:"350px"},attrs:{placeholder:"药品名称"},model:{value:e.drug.name,callback:function(t){e.$set(e.drug,"name",t)},expression:"drug.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"生产厂家",prop:"manufacturer"}},[a("el-input",{staticStyle:{width:"350px"},attrs:{placeholder:"生产厂家"},model:{value:e.drug.manufacturer,callback:function(t){e.$set(e.drug,"manufacturer",t)},expression:"drug.manufacturer"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"药品类型",prop:"typeId"}},[a("el-select",{staticClass:"filter-item",staticStyle:{width:"180px"},attrs:{placeholder:"药品类型",clearable:"",filterable:""},model:{value:e.drug.typeId,callback:function(t){e.$set(e.drug,"typeId",t)},expression:"drug.typeId"}},e._l([{key:101,label:"西药"},{key:102,label:"中成药"},{key:103,label:"中草药"}],(function(e){return a("el-option",{key:e.key,attrs:{label:e.label,value:e.key}})})),1)],1),e._v(" "),a("el-form-item",{attrs:{label:"药品规格",prop:"format"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"药品规格"},model:{value:e.drug.format,callback:function(t){e.$set(e.drug,"format",t)},expression:"drug.format"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"药品单价",prop:"price"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"药品单价"},model:{value:e.drug.price,callback:function(t){e.$set(e.drug,"price",t)},expression:"drug.price"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"包装单位",prop:"unit"}},[a("el-select",{staticClass:"filter-item",staticStyle:{width:"130px"},attrs:{placeholder:"包装单位",clearable:"",filterable:""},model:{value:e.drug.unit,callback:function(t){e.$set(e.drug,"unit",t)},expression:"drug.unit"}},e._l(["支","瓶","袋","盒","克"],(function(e){return a("el-option",{key:e,attrs:{label:e,value:e}})})),1)],1),e._v(" "),a("el-form-item",{attrs:{label:"助记码",prop:"mnemonicCode"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"助记码"},model:{value:e.drug.mnemonicCode,callback:function(t){e.$set(e.drug,"mnemonicCode",t)},expression:"drug.mnemonicCode"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"药品剂型",prop:"dosage"}},[a("el-select",{staticClass:"filter-item",staticStyle:{width:"130px"},attrs:{placeholder:"药品剂型",clearable:"",filterable:""},model:{value:e.drug.dosage.id,callback:function(t){e.$set(e.drug.dosage,"id",t)},expression:"drug.dosage.id"}},e._l(e.allDosage,(function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1)],1),e._v(" "),a("el-form-item",{attrs:{label:"通用名",prop:"genericName"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"通用名"},model:{value:e.drug.genericName,callback:function(t){e.$set(e.drug,"genericName",t)},expression:"drug.genericName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"库存",prop:"stock"}},[a("el-input",{staticStyle:{width:"180px"},attrs:{placeholder:"库存"},model:{value:e.drug.stock,callback:function(t){e.$set(e.drug,"stock",t)},expression:"drug.stock"}})],1)],1),e._v(" "),a("div",{staticStyle:{"text-align":"right"}},[a("el-button",{attrs:{type:"danger"},on:{click:function(t){e.dialogVisible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.confirmDep("depart")}}},[e._v("确认")])],1)],1),e._v(" "),a("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.listQuery.pageNum,limit:e.listQuery.pageSize},on:{"update:page":function(t){return e.$set(e.listQuery,"pageNum",t)},"update:limit":function(t){return e.$set(e.listQuery,"pageSize",t)},pagination:e.getdrugList}})],1)},n=[],i=(a("96cf"),a("1da1")),l=(a("ac6a"),a("5df3"),a("df7c"),a("ed08")),s=a("1e0f"),o=a("333d"),c=a("4be7"),u={id:"",code:"",name:"",format:"",price:"",unit:"",manufacturer:"",dosage:{id:""},dosageId:"",typeID:"",mnemonicCode:"",stock:"",genericName:""},d={components:{Pagination:o["a"]},data:function(){return{drug:Object.assign({},u),drugList:[],allDosage:[],dialogVisible:!1,dialogType:"new",checkStrictly:!1,defaultProps:{children:"children",label:"title"},deptype:[1,2,3,4],total:0,listLoading:!0,allCatId:[],allDep:[],allType:[],listQuery:{id:"",code:"",name:"",format:"",price:"",unit:"",manufacturer:"",dosage:{},typeId:"",mnemonicCode:"",stock:"",genericName:"",pageNum:1,pageSize:20},downloadLoading:!1,rules:{code:[{required:!0,message:"请输入药品编码",trigger:"blur"},{min:4,max:4,message:"长度为4个字符",trigger:"blur"},{pattern:/^[a-zA-Z][0-9]*$/,message:"一个字母三个数字",trigger:"blur"}],name:[{required:!0,message:"请输入药品名称",trigger:"blur"},{min:2,max:10,message:"长度在 2 到 10 个字符",trigger:"blur"}],unit:[{required:!0,message:"请输入包装单位",trigger:"blur"}],dosage:[{required:!0,message:"请输入药品剂型",trigger:"blur"}],typeId:[{required:!0,message:"请输入药品类型",trigger:"blur"}],manufacturer:[{required:!0,message:"请输入生产厂家",trigger:"blur"},{min:2,max:8,message:"长度在 2 到 8 个字符",trigger:"blur"}],format:[{required:!0,message:"药品规格",trigger:"blur"},{min:2,max:8,message:"长度在 2 到 8 个字符",trigger:"blur"}],price:[{required:!0,message:"药品单价",trigger:"blur"},{pattern:/^\d+$|^\d+[.]?\d+$/,message:"只能输入数字",trigger:"blur"}],mnemonicCode:[{required:!0,message:"助记码",trigger:"blur"}],genericName:[{required:!0,message:"通用名",trigger:"blur"}],stock:[{required:!0,message:"库存",trigger:"blur"},{pattern:/^\d+$|^\d+[.]?\d+$/,message:"只能输入数字",trigger:"blur"}]},deplistref:[]}},computed:{},created:function(){c["Promise"].all([this.getDosage(),this.getdrugList()])},methods:{getDosage:function(){var e=Object(i["a"])(regeneratorRuntime.mark((function e(){var t;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,Object(s["c"])();case 2:t=e.sent,this.allDosage=t.data;case 4:case"end":return e.stop()}}),e,this)})));function t(){return e.apply(this,arguments)}return t}(),getdrugList:function(){var e=Object(i["a"])(regeneratorRuntime.mark((function e(){var t;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return this.listLoading=!0,e.next=3,Object(s["e"])(this.listQuery);case 3:t=e.sent,this.drugList=t.data.list,this.total=t.data.total,this.listLoading=!1;case 7:case"end":return e.stop()}}),e,this)})));function t(){return e.apply(this,arguments)}return t}(),handleFilter:function(){this.listQuery.page=1,this.listQuery.typeId=this.listQuery.typeID,this.getdrugList()},handleAdd:function(){var e=this;this.resetTemp(),this.dialogType="new",this.dialogVisible=!0,this.checkStrictly=!0,this.$nextTick((function(){e.$refs["depart"].clearValidate()}))},handleEdit:function(e){var t=this;this.resetTemp(),this.drug=Object(l["c"])(e),this.drug.typeID=Object(l["c"])(e.typeId),console.log(this.drug),this.dialogType="edit",this.dialogVisible=!0,this.checkStrictly=!0,this.$nextTick((function(){t.$refs["depart"].clearValidate()}))},resetTemp:function(){this.drug=Object.assign({},u)},formatJson:function(e,t){return t.map((function(t){return e.map((function(e){return"timestamp"===e?parseTime(t[e]):t[e]}))}))},changedep:function(e){this.deplistref=e},handleSomeDelete:function(){var e=this;0!==this.deplistref.length?this.$confirm("确认删除选中药品?","警告",{confirmButtonText:"确认",cancelButtonText:"取消",type:"warning"}).then((function(){for(var t="",a=0;a<e.deplistref.length;a++)a!==e.deplistref.length-1?t=t+e.deplistref[a].id+",":t+=e.deplistref[a].id;null!=t&&""!=t&&Object(s["b"])(t).then((function(t){e.$notify({title:"成功",message:t.message,type:"success",duration:2e3}),e.getdrugList()}))})):this.$notify({title:"提示",message:"请选择要删除的药品！",type:"warning",duration:2e3})},handleDelete:function(e){var t=this;this.$confirm("确认删除当前药品?","警告",{confirmButtonText:"确认",cancelButtonText:"取消",type:"warning"}).then((function(){Object(s["b"])(e.id).then((function(e){t.$notify({title:"成功",message:"已删除该药品",type:"success",duration:2e3}),t.getdrugList()}))}))},handleDownload:function(){var e=this;Object(s["d"])().then((function(t){var r=t.data;e.downloadLoading=!0,Promise.all([a.e("chunk-94155ae0"),a.e("chunk-6579b732"),a.e("chunk-58293907")]).then(a.bind(null,"4bf8d")).then((function(t){var a=["索引","药品编码","药品名称","药品规格","药品单价","包装单位","生产厂家","药品类型","拼音助记码","通用名","库存"],n=["id","code","name","format","price","unit","manufacturer","typeId","mnemonicCode","genericName","stock"],i=e.formatJson(n,r);t.export_json_to_excel({header:a,data:i,filename:"药品清单"}),e.downloadLoading=!1}))}))},confirmDep:function(){var e=Object(i["a"])(regeneratorRuntime.mark((function e(t){var a=this;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:this.$refs[t].validate((function(e){if(a.drug.dosageID=a.drug.dosage.id,a.drug.dosageId=a.drug.dosage.id,a.drug.typeID=a.drug.typeId,e){var t="edit"===a.dialogType;a.listLoading=!0,t?(Object(s["g"])(a.drug).then((function(e){a.getdrugList(),a.$notify({title:"成功",message:"已修改该药品",type:"success",duration:2e3})})),a.dialogVisible=!1):Object(s["a"])(a.drug).then((function(e){a.getdrugList(),a.$notify({title:"成功",message:e.message,type:"success",duration:2e3}),a.dialogVisible=!1}))}}));case 1:case"end":return e.stop()}}),e,this)})));function t(t){return e.apply(this,arguments)}return t}()}},g=d,p=(a("0cf7"),a("2877")),m=Object(p["a"])(g,r,n,!1,null,"6de2be08",null);t["default"]=m.exports}}]);