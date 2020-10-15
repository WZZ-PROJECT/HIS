import Mock from 'mockjs'

Mock.mock(RegExp('/api/staff/listHistoryDoctor' + ".*"), "get", {
  'data|1-10': [{
    picture: '@image',
    staffName: '@cname',
    deptName: '@cname'
  }]
})
Mock.mock(RegExp('/api/staff/listNowDoctorRegistration' + ".*"), "get", {
  'data|1-10': [{
    picture: '@image',
    staffName: '@cname',
    title: '主任医师',
    advantages: '医生介绍',
    'noon|0-2': 0,
    'amount|0-300': 0,
    'skLimit|5-7': 0,
    'remain|0-5': 0
  }]
})
Mock.mock(RegExp('/api/staff/listDoctor' + ".*"), "get", {
  'data': {
    staffName: '@cname',
    title: '主任医师',
    advantages: '医生介绍',
    deptName: '',
    picture: '@image',
    'att|0-1': 0,
    'amount|0-300': 0,
    'skLimit|5-7': 0,
    'remain|0-5': 0,
    'skdList|0-3': [{
      'noon|0-1': 0,
      'remain|0-1': 0,
      'amount|0-300': 0,
      'date': '@date(MM-dd)',
      'week': '周一',
    }]
  }
})
Mock.mock('/api/dept/select', "post", {
  'data': {
    'list|1-10': [{
      name: '',
      address: '@city(true)'
    }]
  }
})
Mock.mock('/api/staff/listRegistrationBill', "post", {
  'data': {
    staffName: '@cname',
    deptName: '',
    address: '',
    'amount|0-300': 0,
    patientName: '@cname',
    card: '@integer(1, 100, 3, 6)',
    picture: '@image',
    date: '@date("yyyy-MM-dd")',
    time: '@time("HH:mm:ss")'
  }
})
