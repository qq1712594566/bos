import Mock from 'mockjs'
// 模拟用户数据
Mock.mock('http://localhost:8080/user',{
  data: {
    'name': '@name', // 随机生成姓名
    'email': '@email', // 随机生成邮箱
    'age|1-10': 5 // 区间取值
    // 其他数据
  }
});
// 模拟菜单数据
Mock.mock('http://localhost:8080/menu',{
  data: {
    'id': '@increment', // 自动增长
    'name': 'menu',
    'order|10-20': 12
    // 其他数据
  }
});
