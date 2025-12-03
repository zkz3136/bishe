<template>
	<div>
		<div class="login_view">
			<el-form :model="loginForm" class="login_form">
				<div class="title_view">餐厅智能服务与预订系统登录</div>
				<div class="list_item" v-if="loginType==1">
					<div class="list_label">
						账号：
					</div>
					<input class="list_inp" v-model="loginForm.username" placeholder="请输入账号" name="username" />
				</div>
				<div class="list_item" v-if="loginType==1">
					<div class="list_label">
						密码：
					</div>
					<input class="list_inp" v-model="loginForm.password" type="password" placeholder="请输入密码" @keydown.enter.native="handleLogin"  />
				</div>
				<div class="list_type" v-if="userList.length>1">
				  <el-radio v-for="(item,index) in userList" :key="index" v-model="loginForm.role"
				    :label="item.roleName">{{item.roleName}}</el-radio>
				</div>
				<div class="remember_view" v-if="loginType==1">
					<el-checkbox v-model="rememberPassword" label="记住密码" size="large" :true-label="true"
						:false-label="false" />
				</div>
				<div class="btn_view">
					<el-button class="login" v-if="loginType==1" type="success" @click="handleLogin">登录</el-button>
				</div>
			</el-form>
		</div>
		<Vcode :show="isShow" @success="success" @close="close" @fail='fail'></Vcode>
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
	//是否记住密码
	const rememberPassword = ref(true)
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
				verifySlider.value.reset()
				return;
			}
			for (let i = 0; i < menus.value.length; i++) {
				if (menus.value[i].roleName == loginForm.value.role) {
					tableName.value = menus.value[i].pathName||menus.value[i].tableName;
				}
			}
		} else {
			tableName.value = userList.value[0].pathName||userList.value[0].tableName;
			loginForm.value.role = userList.value[0].roleName;
		}
		login()
	}
	const login = () => {
		context?.$http({
			url: `${tableName.value}/login?username=${loginForm.value.username}&password=${loginForm.value.password}`,
			method: 'post'
		}).then(res => {
			//是否保存当前账号密码至缓存
			if (rememberPassword.value) {
				let loginForm1 = JSON.parse(JSON.stringify(loginForm.value))
				delete loginForm1.code
				context?.$toolUtil.storageSet("loginForm", JSON.stringify(loginForm1));
			} else {
				context?.$toolUtil.storageRemove("loginForm")
			}
			context?.$toolUtil.storageSet("Token", res.data.token);
			context?.$toolUtil.storageSet("role", loginForm.value.role);
			context?.$toolUtil.storageSet("sessionTable", tableName.value);
			context?.$toolUtil.storageSet("adminName", loginForm.value.username);
			store.dispatch('user/getSession')	//vuex中获取用户信息并保存
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
          menus.value = JSON.parse(res.data.data.list[0].menujson)
          for (let i = 0; i < menus.value.length; i++) {
            if (menus.value[i].hasBackLogin=='是') {
              userList.value.push(menus.value[i])
            }
          }
			//获取缓存是否有保存的账号密码
			let form = context?.$toolUtil.storageGet('loginForm')
			if (form) {
				
			}else {
				loginForm.value.role = userList.value[0].roleName
			}
          context?.$toolUtil.storageSet("menus", JSON.stringify(menus.value));
      })
    }
	//初始化
	const init = () => {
		getMenu();
		//获取缓存是否有保存的账号密码
		let form = context?.$toolUtil.storageGet('loginForm')
		if (form) {
			loginForm.value = JSON.parse(form)
		}
	}
	onMounted(()=>{
		init()
		

	})
</script>

<style lang="scss" scoped>
	.login_view {
        background-image: url("http://clfile.zggen.cn/20240916/eb2285ada5a94b00bd896b29433fd009.webp")!important;
		// 表单盒子
		.login_form {
		}
		.title_view {
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
		// 用户类型样式
		.list_type {
			// 未选中样式
			:deep(.el-radio) {
				// 单选框
				.el-radio__inner {
				}
				// 提示文字
				.el-radio__label{
				}
			}
			// 选中样式
			:deep(.is-checked) {
				// 单选框
				.el-radio__inner {
				}
				// 提示文字
				.el-radio__label{
				}
			}
		}
		// 记住密码样式
		.remember_view {
		
			// 未选中样式
			:deep(.el-checkbox) {
				// 复选框
				.el-checkbox__inner {
				}
				// 提示文字
				.el-checkbox__label{
				}
			}
			// 选中样式
			:deep(.is-checked) {
				//复选框
				.el-checkbox__inner {
				}
				// 提示文字
				.el-checkbox__label{
				}
			}
		}
		// 按钮盒子
		.btn_view {
			// 登录
			.login {
			}
			// 注册
			.register {
			}
		}
	}

</style>
<style>
.login_view {
    min-height: 100vh;
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-image: url(http://clfile.zggen.cn/20240916/cc474f586aab49a6a89f35258fafd38a.jpg);
    background-position: center center;
    background-size: 100% 100% !important;
    background-repeat: no-repeat;
    background-attachment: initial;
    background-origin: initial;
    background-clip: initial;
    background-color: initial;
}

/*表单盒子*/
.login_view .login_form{
    width:35%;
    margin:0 0 0 40%; 
    padding:40px 80px 10px 80px; 
    background:rgba(255,255,255,.9);
    font-size:16px;
    border-radius:0px;
    transform:scale(1); 
    -webkit-transform:scale(1);
    -ms-transform:scale(1); 
    z-index:5;
    box-shadow: inset -1px 3px 8px 5px #f6f6f6, 2px 5px 16px 0px #888, 5px 5px 15px 5px rgba(0,0,0,0);
}
.login_form:before{ content:""; width:100%; height:0px; border-radius:10px; position:absolute; top:-10px; background:rgba(255,255,255,.6); left:0; transform:scale(.95); -webkit-transform:scale(.95); -ms-transform:scale(.95); z-index:-1; }

/*内部标题*/
.login_view .login_form .title_view{
    width:calc(100% + 0px);
    float:left;
    line-height:46px;
    font-size:22px; 
    font-weight:600;
    letter-spacing:0.5px;
    background:none;
    color:#000000;
    margin-bottom:20px;
    margin-left:0px;
    text-align:left;
}
.login_form .title_view:before{ content:""; width:0px; height:100%; position:absolute; top:0; left:-50px; background:#8b5c7e; }

/*外部标题 盒子*/
.login_view .outTitle_view{
    display: flex;
    align-items: center;
    padding: 0px 0px 40px;
}
/*标题*/
.login_view .outTitle_view .outTilte{
    font-size: 32px;
    font-weight:600;
    letter-spacing:4px;
    color: rgb(255, 255, 255);
    text-shadow: 3px 5px 2px #474747;
}

/*list_item*/
.login_form .list_item{   display: flex; align-items: center; width: 100%;  justify-content: flex-start;  margin: 0 auto 20px;  }
.login_form .list_item .list_label { margin-right:10px; font-size:16px; white-space:nowrap;  }
.login_form .list_item .list_label i {  font-size:24px; color:#999;  }
.login_form .list_item .list_inp{ width:100%; border:none;  height:40px; line-height:40px; font-size:inherit; background:none; border: 1px solid #c4c4c4; padding:0 10px; border-radius:4px;  }
.login_form .list_item .el-select{  }

/*用户类型*/
.login_form .list_type{ display: flex; align-items: center; width: 100%; justify-content:center; padding: 10px 0px;margin-bottom:10px;  }
.login_form .list_type .el-radio{ display: flex; align-items: center; }
.login_form .list_type .el-radio .el-radio__label{ color: #666;  font-size: 16px; }
.login_form .list_type .el-radio.is-checked{ }
.login_form .list_type .el-radio.is-checked .el-radio__label{ color: #d3623d;  font-size: 16px; }
.login_form .list_type .el-radio__input{ }
.login_form .list_type .el-radio__input.is-checked{ }
.login_form .list_type .el-radio__input .el-radio__inner{ background: rgb(255, 255, 255); border: 1px solid rgb(220, 223, 230); }
.login_form .list_type .el-radio__input.is-checked  .el-radio__inner{ background-color: #d3623d; border-color: #d3623d; }

/*记住密码*/
.login_form .remember_view{  margin-bottom:20px;  }
.login_form .remember_view .el-checkbox{ width: auto; margin: 0px;  display: flex;  justify-content: flex-start; align-items: center; }
.login_form .remember_view .el-checkbox .el-checkbox__label{  background:none;  }
.login_form .remember_view .el-checkbox .el-checkbox__inner{ color: rgb(153, 153, 153); }
.login_form .remember_view .el-checkbox.is-checked .el-checkbox__label{ color:#d3623d; font-size: 15px;  }
.login_form .remember_view .el-checkbox.is-checked .el-checkbox__inner{  background: #d3623d;  border-color: #d3623d; font-size: 16px; }

/* 按钮 */
.login_form .btn_view{  text-align:center; display: flex; flex-wrap:wrap;   }
.login_form .btn_view .login{ width: 100%; height: 46px; line-height: 46px; background:#d3623d; border: 0px solid #ccc; font-weight: 600; font-size: 20px; color: #fff; letter-spacing: 4px; margin-bottom:20px; padding:0; border-radius:4px; } 
.login_form .btn_view .login:hover {  } 
.login_form .btn_view .register{ background: #ffc77f30; border:1px solid #ffc77f; font-size:16px; color: #333;  margin:0 5px 20px; } 
.login_form .btn_view .register:hover {  text-decoration:underline;  } 

.login_form .btn_view .forget{ width: 100%; margin-bottom:20px;  background: none; border:0px solid #ccc; font-size:16px; color: #666; text-align:center; order:-1;  } 
.login_form .btn_view .forget:hover { text-decoration:underline;   }

.login_form .face{ cursor:pointer; width:100%;  background: none; border: 0px solid #ccc; font-weight: 500; font-size: 16px; color: #555; margin-bottom:20px; text-align:center; padding:0;  } 
.login_form .face:hover{ text-decoration:underline;   }


</style>