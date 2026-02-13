<template>
	<div>
		<div class="login_view">
			<div class="outTitle_view">
				<div class="outTilte">餐厅智能服务与预订系统登录</div>
			</div>
			<el-form :model="loginForm" class="login_form">
				<div class="list_item" v-if="loginType==1">
					<div class="list_label">
						账号：
					</div>
					<input class="list_inp" v-model="loginForm.username" name="username" placeholder="请输入账号" />
				</div>
				<div class="list_item" v-if="loginType==1">
					<div class="list_label">
						密码：
					</div>
					<input class="list_inp" v-model="loginForm.password" type="password" placeholder="请输入密码" @keydown.enter.native="handleLogin" />
				</div>
				<div class="list_type" v-if="userList.length>1">
					<div class="list_label">
						用户类型：
					</div>
				  <el-radio-group v-model="loginForm.role">
				    <el-radio v-for="(item,index) in userList" :key="index" :value="item.roleName">{{item.roleName}}</el-radio>
				  </el-radio-group>
				</div>
				<div class="btn_view">
					<el-button class="login" v-if="loginType==1" type="success" @click="handleLogin">登录</el-button>
				</div>
			</el-form>
		</div>
	</div>
</template>
<script setup>
	import {
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
	} from "vue";
	import { useStore } from 'vuex'
	const store = useStore()
	const userList = ref([])
	const menus = ref([])
	const loginForm = ref({
		role: '',
		username: '',
		password: ''
	})
	const tableName = ref('')
	const loginType = ref(1)
	const context = getCurrentInstance()?.appContext.config.globalProperties;

	const handleLogin = () => {
		if (!loginForm.value.username) {
			context?.$toolUtil.message('请输入用户名', 'error')
			return;
		}
		if (!loginForm.value.password) {
			context?.$toolUtil.message('请输入密码', 'error')
			return;
		}
		if (userList.value.length > 1) {
			if (!loginForm.value.role) {
				context?.$toolUtil.message('请选择角色', 'error')
				return;
			}
			for (let i = 0; i < userList.value.length; i++) {
				if (userList.value[i].roleName == loginForm.value.role) {
					tableName.value = userList.value[i].tableName;
				}
			}
		} else {
			if(userList.value && userList.value.length > 0) {
				tableName.value = userList.value[0].tableName;
				loginForm.value.role = userList.value[0].roleName;
			} else {
				context?.$toolUtil.message('没有可用的角色', 'error');
				return;
			}
		}
		if(tableName.value == 'yuangong') tableName.value = 'staff';
		login()
	}
	const login = () => {
		context?.$http({
			url: `${tableName.value}/login?username=${loginForm.value.username}&password=${loginForm.value.password}`,
			method: 'post'
		}).then(res => {
			context?.$toolUtil.storageSet("Token", res.data.token);
			context?.$toolUtil.storageSet("role", loginForm.value.role);
			context?.$toolUtil.storageSet("sessionTable", tableName.value);
			context?.$toolUtil.storageSet("adminName", loginForm.value.username);
			store.dispatch('user/getSession')
			context?.$router.push('/')
		}, err => {
		})
	}
	//获取菜单
	const getMenu=()=> {
      let params = {
        page: 1,
        limit: 1,
        sort: 'id',
      }

      context?.$http({
        url: "menu/list",
        method: "get",
        params: params
      }).then(res => {
          try {
              if(res.data.data.list.length > 0){
                  menus.value = JSON.parse(res.data.data.list[0].menujson)
                  // 菜单数据处理，将订单管理菜单项下的子菜单简化为“订单管理”一项
                  menus.value.forEach(item => {
                    if(item.backMenu) {
                        item.backMenu.forEach(menu => {
                            if(menu.menu == '订单管理') {
                                if(menu.child && menu.child.length > 0) {
                                    let firstChild = menu.child[0];
                                    let orderChild = menu.child.find(c => c.menu == '订单');
                                    if(orderChild) {
                                        firstChild = orderChild;
                                    }
                                    firstChild.menuJump = ''; 
                                    menu.child = [firstChild];
                                }
                            }
                        })
                    }
                  })
                  for (let i = 0; i < menus.value.length; i++) {
                    if (menus.value[i].hasBackLogin=='是') {
                      userList.value.push(menus.value[i])
                    }
                  }
              }
          } catch(e) {
              console.error("菜单数据解析失败", e)
          }
          
          // 容错处理：如果未能获取到角色列表，使用默认配置
          if (userList.value.length === 0) {
              menus.value = [
                  { 
                      roleName: '管理员', 
                      tableName: 'admin', 
                      hasBackLogin: '是', 
                      backMenu: [{
                          menu: '系统管理',
                          child: [{
                              menu: '菜单管理',
                              tableName: 'menu'
                          }]
                      }] 
                  },
                  { roleName: '员工', tableName: 'staff', hasBackLogin: '是', backMenu: [] }
              ];
              userList.value = menus.value;
          }

          if (userList.value.length > 0) {
              loginForm.value.role = userList.value[0].roleName
          }
          context?.$toolUtil.storageSet("menus", JSON.stringify(menus.value));
      }).catch(err => {
          // 网络请求失败时的容错处理
          console.error("菜单请求失败", err);
          if (userList.value.length === 0) {
              menus.value = [
                  { 
                      roleName: '管理员', 
                      tableName: 'admin', 
                      hasBackLogin: '是', 
                      backMenu: [{
                          menu: '系统管理',
                          child: [{
                              menu: '菜单管理',
                              tableName: 'menu'
                          }]
                      }] 
                  },
                  { roleName: '员工', tableName: 'staff', hasBackLogin: '是', backMenu: [] }
              ];
              userList.value = menus.value;
              if (userList.value.length > 0) {
                  loginForm.value.role = userList.value[0].roleName
              }
          }
      })
    }
	//初始化
	const init = () => {
		getMenu();
	}
	onMounted(()=>{
		init()
	})
</script>

<style lang="scss" scoped>
	.login_view {
        background-image: url("@/assets/img/loginpicture.png")!important;
		// 标题盒子
		.outTitle_view {
			.outTilte {
			}
		}
		// 表单盒子
		.login_form {
		}
		// item盒子
		.list_item {
			// label
			.list_label {
			}
			// 输入框
			.list_inp {
			}
		}
		.list_type {
			.list_label {
			}
			// 下拉框样式
			:deep(.el-select) {
				//去掉默认样式
				.select-trigger{
					height: 100%;
					.el-input{
						height: 100%;

					}
				}
			}
		}
		// 按钮盒子
		.btn_view {
			// 登录
			.login {
			}
		}
	}
</style>
<style>

.login_view {
    min-height: 100vh;
    position: relative;
    background: url("@/assets/img/loginpicture.png") no-repeat center center / cover;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    --theme-color: #ea580c;
    --theme2-color: #f97316;
}

.login_view .login_form{
    width: 600px;
    margin: 40px 0 40px 40%;
    box-shadow: rgb(187, 187, 187) 0px 4px 9px;
    padding: 30px 60px 30px 20px;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    border-radius: 10px;
    background: #fff;
    border:1px solid #ddd;
}

.login_view .outTitle_view{
    display: flex;
    align-items: center;
    padding: 0px 0px 20px;
    margin: 0 0 0 40%;
}
.login_view .outTitle_view .outTilte{
    color: rgb(51, 51, 51);
    font-size: 30px;
    font-weight: 600;
    -webkit-box-reflect: below 2px linear-gradient(transparent, rgba(0, 0, 0, .1));
    background: linear-gradient(90deg, var(--theme2-color) 0%, var(--theme-color) 50%, var(--theme2-color) 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

.login_view .login_form .list_item{
    display: flex;
    align-items: center;
    width: 100%;
    justify-content: center;
    margin: 0px 10px 20px 0px;
}
.login_view .login_form .list_item .list_label{
    width: 120px;
    text-align: right;
    font-size: 16px;
}
.login_view .login_form .list_item .list_inp{
    height: 36px;
    line-height: 36px;
    border:none;
    border-bottom: 1px solid rgb(221, 221, 221);
    padding: 0px 10px;
    width: calc(100% - 120px);
    font-size: 16px;
}

.login_view .login_form .list_type{
    display: flex;
    align-items: center;
    width: 100%;
    justify-content: center;
    margin: 0px 10px 20px 0px;
}
.login_view .login_form .list_type .list_label{
    width: 120px;
    text-align: right;
    font-size: 16px;
}
.login_view .login_form .list_type .el-radio-group{
    width: calc(100% - 120px);
    align-items: center;
    justify-content: center;
}


.login_view .login_form .btn_view{
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px 0px 10px 0px;
    width: 100%;
}
.login_view .login_form .btn_view .login{
    padding: 0px 40px;
    width: auto;
    height: 40px;
    font-size: 16px;
    color: rgb(255, 255, 255);
    border-radius: 4px;
    border: 0px;
    background: var(--theme-color);
    cursor: pointer;
    min-width: 200px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.15);
    transition: all 0.3s;
}
.login_view .login_form .btn_view .login:hover{
    background: var(--theme2-color);
    box-shadow: 0 4px 12px rgba(0,0,0,0.2);
    transform: translateY(-1px);
}
</style>
