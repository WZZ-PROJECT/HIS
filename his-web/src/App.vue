
<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
<style>
  .el-form-item__label {
    position: relative;
  }
.el-form-item.is-required:not(.is-no-asterisk)>.el-form-item__label:before {
  position: absolute;
  left: -8px;
}
body .el-table th.gutter{
      display: table-cell!important;
  }

  body .el-table colgroup.gutter{
      display: table-cell!important;
  }
   .el-table .warning-row {
    background: oldlace;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }
</style>

