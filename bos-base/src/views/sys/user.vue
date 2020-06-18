<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item"/>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="getList">
        查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>

      <el-button @click="exportData" class="filter-item"  type="success" icon="el-icon-download">
        导出数据
      </el-button>

      <el-upload
        style="margin-left: 10px;"
        class="filter-item"
        :show-file-list="false"
        :on-success="onSuccess"
        :on-error="onError"
        :before-upload="beforeUpload"
        action="http://localhost:80/bos/user/import">
        <el-button @click="importData" type="success" :disabled="!enabledUploadBtn" :icon="uploadBtnIcon">{{btnText}}</el-button>
      </el-upload>

     </div>
    <!--  数据表格-->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="ID" prop="id" sortable="custom" align="center" min-width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" min-width="180px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="用户名" min-width="120px">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.username }}</span>
        </template>
      </el-table-column>


      <el-table-column label="邮箱" min-width="120px">
      <template slot-scope="{row}">
        <span class="link-type">{{ row.email }}</span>
      </template>
      </el-table-column>


      <el-table-column label="电话" min-width="120px">
        <template slot-scope="{row}">
          <span class="link-type">{{ row.mobile }}</span>
        </template>
      </el-table-column>
      <!--     自定义列-->
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            修改
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页工具条  page当前页 total总记录数 limit每页显示多少条 pagination触发自定义事件，查询数据-->
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    <!--  绑定了title，是一个数组里取的，表示是修改的标题还是添加的标题
      visible.sync 对话框是否显示
    -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible" style="width: 80%">
      <!--
          rules:校验规则
          model:数据绑定
      -->
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 80%; margin-left:50px;">
        <!--        数据校验要求prop值和temp.属性名一致-->
        <el-form-item label="部门" prop="sex">
          <el-select v-model="temp.deptId" placeholder="请选择">
            <el-option-group
              v-for="group in deptList"
              :key="group.id"
              :label="group.name">
              <el-option-group
                v-for="items in group.items"
                :key="items.id"
                :label="items.name">
              <el-option
                v-for="item in items.items"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
              </el-option-group>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="姓名" prop="username">
          <el-input placeholder="请输入用户名" v-model="temp.username" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input placeholder="请输入密码" v-model="temp.password" show-password></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item label="手机" prop="mobile">
          <el-input v-model="temp.mobile" />
        </el-form-item>
        <el-form-item label="自我简介">
          <el-input
            type="textarea"
            :rows="4"
            placeholder="请输入自我简介"
            v-model="temp.introduction">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <!--
          dialogStatus==='create'?createData():updateData()
          dialogStatus需要我们根据情况去改变
        -->
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  //
  import { add, update, list, deleteUser } from '@/api/sys/user'
  import {deleteRequest} from "../../utils/api";
  import {groupDept} from "@/api/sys/dept";
  import waves from '@/directive/waves' // waves directive
  import { parseTime } from '@/utils'
  import Pagination from '@/components/Pagination' // 分页组件

  export default {
    name: 'userTable',
    components: { Pagination },
    directives: { waves },
    data() {
      return {
        tableKey: 0,
        list: null, // 后台返回，给数据表格展示的数据
        total: 0, // 总记录数
        listLoading: true, // 是否使用动画
        listQuery: {
          page: 1, // 分页需要的当前页
          limit: 10, // 分页需要的每页显示多少条
          sex: 1,
          username: ''
        },
        deptList: [], // 后台查询出来，分好组的部门信息
        temp: { // 添加、修改时绑定的表单数据
          id: undefined,
          username: '',
          password: '',
          email: '',
          mobile: '',
          deptId: '',
          introduction: '',
        },
        title: '添加', // 对话框显示的提示 根据dialogStatus create
        dialogFormVisible: false, // 是否显示对话框
        dialogStatus: '', // 表示表单是添加还是修改的
        rules: {
          // 校验规则
          username: [{ required: true, message: '用户名必填', trigger: 'blur' }]
        },

        uploadBtnIcon:'el-icon-upload2',
        enabledUploadBtn:true,
        dialogVisible:false,
        btnText:'数据导入',
        jl:{
          name:'',
          email: '111@qq.com'
        },
        jls:[],
        tls: [{
          value: '',
          label: ''
        }, {
          value: '',
          label: ''
        }, {
          value: '',
          label: ''
        }, {
          value: '',
          label: ''
        }, {
          value: '',
          label: ''
        }],
        value: ''
      }
    },
    mounted(){
      this.initJls();
    },

    // 创建实例时的钩子函数
    created() {
      this.getList()
      // 在创建时初始化获得部门信息
      this.getGroupDept()
    },

    methods: {
      // 获得分好组的部门信息
      getGroupDept(){
        groupDept().then((response) => {
          this.deptList = response.data.deptList
        })
      },

      // 去后台取数据的
      getList() {
        // 开始转圈圈
        this.listLoading = true
        // debugger // 调试
        list(this.listQuery).then(response => {
          this.list = response.data.items
          this.total = response.data.total
          // 转圈圈结束
          this.listLoading = false
        })
      },

      // 重置表单数据
      resetTemp() {
        this.temp = {
          id: undefined,
          username: '',
          password: '',
          email: '',
          mobile: '',
          deptId: '',
          introduction: '',
        }
      },

      // 显示添加的对话框
      handleCreate () {
        // 重置表单数据
        this.resetTemp()
        // 点击确定时，是执行添加操作
        this.dialogStatus = 'create'
        this.title="添加用户"
        // 显示对话框
        this.dialogFormVisible = true
        this.$nextTick(() => {
          // 表单清除验证
          this.$refs['dataForm'].clearValidate()
        })
      },


      // 添加对话框里，点击确定，执行添加操作
      createData() {
        // 表单校验
        this.$refs['dataForm'].validate((valid) => {
          // 所有的校验都通过
          if (valid) {
            // 调用api里的sys里的user.js的ajax方法
            add(this.temp).then((response) => {

              // 关闭对话框
              this.dialogFormVisible = false
              // 刷新数据表格里的数据
              this.getList()
              // 显示一个通知
              this.$notify({
                title: '成功',
                message: response.data.message,
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },


      // 显示修改对话框
      handleUpdate(row) {
        // 将row里面与temp里属性相同的值，进行copy
        this.temp = Object.assign({}, row) // copy obj
        // 将对话框里的确定点击时，改为执行修改操作
        this.dialogStatus = 'update'
        // 修改标题
        this.title = '修改用户'
        // 显示修改对话框
        this.dialogFormVisible = true
        this.$nextTick(() => {
          // 清除校验
          this.$refs['dataForm'].clearValidate()
        })
      },


      // 执行修改操作
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          // 表单校验通过
          if (valid) {
            // 将temp拷贝到tempData
            const tempData = Object.assign({}, this.temp)
            // 进行ajax提交
            update(tempData).then((response) => {
              // 提交完毕，关闭对话框
              this.dialogFormVisible = false
              // 刷新数据表格
              this.getList()
              // 显示通知
              this.$notify({
                title: '成功',
                message: response.data.message,
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },


      handleDelete(row) {
        // 先弹确认取消框
        this.$confirm('确认删除【'+row.username+'】的信息吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 调用ajax去后台删除
          deleteUser(row.id).then((response) => {
            // 刷新数据表格
            this.getList()
            // ajax去后台删除
            this.$notify({
              title: '成功',
              message: response.data.message,
              type: 'success',
              duration: 2000
            })
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });

      },

      // 导出的方法

      exportData(){
        /* 直接open好像没用*/
         window.open("http://localhost:80/bos/user/export","_parent");
      },



      onSuccess(response,file,fileList){
        this.enabledUploadBtn=true;
        this.uploadBtnIcon="el-icon-upload2";
        this.btnText='数据导入';
        alert("数据导入成功！");
        this.initJls();
      },

      onError(err,file,fileList){
        this.enabledUploadBtn=true;
        this.uploadBtnIcon="el-icon-upload2";
        this.btnText='数据导入';
        alert("数据导入失败！请检查是否有重复数据,和网络连接状况！");
      },
      beforeUpload(file){
        this.enabledUploadBtn=false;
        this.uploadBtnIcon="el-icon-loading";
        this.btnText='正在导入';
      },


      importData(){
        alert('H')
        /*debugger*/
        //1.查找到存放文件的元素
        let myfile=this.$refs.myfile;
        alert(myfile.files+"<<<<<")
        //2.钙元素内部有一个file数组，里面存放了所有选择的file。
        // 由于上传文件时，文件可以多选，因此这里拿到的files对象是一个数组
        let files=myfile.files;
        //3.从files对象中，获取自己要上传的文件（即数组中的第一项）
        let file=files[0];
        //4.构造一个FormData，用来存放上传的数据，注意FormData不可以使用链式配置
        var formData = new FormData();
        console.log(file)
        formData.append("file",file);
        formData.append("username","User表");
        console.log(formData+"上传的数据")
        console.log("123456744444444444444444444444")
        //5.构造好FormData后，就可以直接上传数据了，FormData就是要上传的数据
        //6.文件上传注意两点，①请求方法为POST，②设置Content-Type为multipart/form-data
        this.uploadFileRequest("http://localhost/bos/user/import",formData).then(resp=>{
          if (resp) {
            alert(resp);
          }
        })
      },


      initJls() {
        this.getRequest("http://localhost/bos/user/").then(resp => {
          if (resp) {
            this.jls = resp;
          }
        })
      },



    }
  }
</script>
