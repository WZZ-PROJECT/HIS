webpackJsonp([5],{byoV:function(t,a){},g7AA:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var n=e("mtWM"),i=e.n(n),s=e("jNDk"),r={components:{},data:function(){return{date:"",src:"",name:"",role:"",dept:"",advantages:"",description:"",att:"",staffId:this.$route.query.staffId,patientId:localStorage.patientId}},created:function(){var t,a,e=this;(t=this.staffId,a=this.patientId,i()({url:"/api/staff/selectStaffById?id="+t+"&patientId="+a,method:"get"})).then(function(t){e.name=t.data.data.staffName,e.role=t.data.data.title,e.dept=t.data.data.deptName,e.advantages=t.data.data.advantages,e.description=t.data.data.description,e.att=t.data.data.state,e.src=t.data.data.picture})},methods:{onReturn:function(){history.back()},attention:function(){var t=this,a={staffId:this.staffId,patientId:this.patientId};Object(s.a)(a).then(function(a){"关注成功"===a.data.data&&(t.att="已关注",t.$toast("关注成功"))})},toFive:function(){this.$router.push("/appointment/five")},formatDate:function(t){return t.getMonth()+1+"月"+t.getDate()+"日"},onConfirm:function(t){this.show=!1,this.date=this.formatDate(t),this.message=!0}}},o={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",[e("hr"),t._v(" "),e("div",[e("div",{staticStyle:{padding:"0 1rem","text-align":"left"}},[e("van-row",[e("van-col",{attrs:{span:"6"}},[e("van-image",{attrs:{width:"5rem",height:"5rem",src:t.src}})],1),t._v(" "),e("van-col",{staticStyle:{"margin-top":"1rem"},attrs:{span:"12",offset:"1"}},[e("van-row",{attrs:{span:"24"}},[e("span",{staticStyle:{"font-size":"1.3rem","font-weight":"bold"}},[t._v(t._s(t.name))]),t._v(" "),e("span",{staticStyle:{"font-size":"0.8rem"}},[t._v(t._s(t.role))])]),t._v(" "),e("van-row",{attrs:{span:"24"}},[e("span",{staticStyle:{"font-size":"0.8rem"}},[t._v(t._s(t.dept))])])],1),t._v(" "),e("van-col",{attrs:{span:"5"}},[e("van-button",{staticClass:"btn",staticStyle:{"margin-top":"1rem","font-size":"0.8rem",height:"2rem"},attrs:{type:"info",size:"normal",round:"",color:"#f1c232"},on:{click:function(a){return t.attention()}}},[t._v(" "+t._s(0==t.att?"+关注":"已关注"))])],1)],1)],1)]),t._v(" "),e("hr"),t._v(" "),e("div",{staticStyle:{"text-align":"left",padding:"0 0.4rem"}},[e("label",[t._v("医生擅长")]),t._v(" "),e("div",{staticStyle:{"margin-top":"0.5rem","font-size":"0.9rem",color:"#b0b0b0"}},[t._v(t._s(t.advantages))])]),t._v(" "),e("div",{staticStyle:{"margin-top":"0.5rem","text-align":"left",padding:"0 0.4rem"}},[e("label",[t._v("医生简介")]),t._v(" "),e("div",{staticStyle:{"margin-top":"0.5rem","font-size":"0.9rem",color:"#b0b0b0"}},[t._v(t._s(t.description))])])])},staticRenderFns:[]};var d=e("VU/8")(r,o,!1,function(t){e("byoV")},"data-v-1f1e95af",null);a.default=d.exports},jNDk:function(t,a,e){"use strict";e.d(a,"a",function(){return s});var n=e("mtWM"),i=e.n(n),s=function(t){return i()({url:"/api/staff/attentionStaff",method:"post",data:t})}}});
//# sourceMappingURL=5.0f781c2507353c40c7ad.js.map