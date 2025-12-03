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
				  <el-select v-model="loginForm.role" placeholder="请选择用户类型">
				    <el-option v-for="(item,index) in userList" :label="item.roleName" :value="item.roleName"></el-option>
				  </el-select>
				</div>
				<div class="btn_view">
					<el-button class="login" v-if="loginType==1" type="success" @click="handleLogin">登录</el-button>
				</div>
				<div class="link_view">
					<el-button class="register" text @click="handleRegister('yonghu')">注册账号</el-button>
					<span class="divider">|</span>
					<el-button class="forget" text @click="handleForget">忘记密码</el-button>
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
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	import menu from '@/utils/menu'
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
	//注册
    const handleRegister = (tableName) => {
    	context?.$router.push(`/${tableName}Register`)
    	
    }
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
				verifySlider.reset()
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
			context?.$toolUtil.storageSet("frontToken", res.data.token);
			context?.$toolUtil.storageSet("frontRole", loginForm.value.role);
			context?.$toolUtil.storageSet("frontSessionTable", tableName.value);
			store.dispatch('user/getSession')
			let path = context?.$toolUtil.storageGet('toPath')
			if (path && path!='/login') {
				context?.$router.push(path)
				context?.$toolUtil.storageRemove('toPath')
				return
			}
			context?.$router.push('/index/home')
		},err=>{
		})
	}
	//忘记密码
	const handleForget = () => {
		context?.$router.push({
			name: 'forget'
		})
	}
	//获取菜单
	const getMenu= async ()=> {
		let arr = menu.list()
		if(!arr){
			let res = await context?.$http.get("menu/list")
			context?.$toolUtil.storageSet("menus", res.data.data.list[0].menujson);
			arr = JSON.parse(res.data.data.list[0].menujson)
		}
		menus.value = arr
		for (let i = 0; i < menus.value.length; i++) {
			if (menus.value[i].hasFrontLogin=='是') {
				userList.value.push(menus.value[i])
			}
		}
    }
	//初始化
	const init = async () => {
		await getMenu();
		loginForm.value.role = userList.value[0].roleName
	}
	onMounted(()=>{
		init()

	})
</script>

<style lang="scss" scoped>
	.login_view {
        background-image: url("http://clfile.zggen.cn/20241220/58d86a65b0df43398aa5b351985f8140.jpg")!important;
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
		// 链接盒子
		.link_view {
			.divider {
			}
			// 注册
			.register {
			}
			// 忘记密码
			.forget {
			}
		}
	}

</style>
<style>

.login_view {
    min-height: 100vh;
    position: relative;
    background: url(http://clfile.zggen.cn/20241220/58d86a65b0df43398aa5b351985f8140.jpg) no-repeat center center / cover;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
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
.login_view .login_form .list_type .el-select{
    line-height: 36px;
    border:none;
    border-bottom: 1px solid rgb(221, 221, 221);
    box-sizing: border-box;
    width: calc(100% - 120px);
    padding: 0px 10px;
    border-radius: 0px;
    font-size: 16px;
    color: rgb(102, 102, 102);
}
.login_view .login_form .list_type .el-select .el-input__inner{
    font-size: 16px;
}


.login_view .login_form .listCode_view{
    display: flex;
    align-items: center;
    width: 100%;
    justify-content: center;
    margin: 0px 10px 10px 0px;
}
.login_view .login_form .listCode_view .listCode_label{
    width: 120px;
    text-align: right;
    font-size: 16px;
}
.login_view .login_form .listCode_view .listCode_inp{
    height: 36px;
    line-height: 36px;
    border:none;
    border-bottom: 1px solid rgb(221, 221, 221);
    padding: 0px 10px;
    width: calc(100% - 220px);
    font-size: 16px;
}
.login_view .login_form .listCode_view .listCode_btn{
    width: 100px;
    border-style: solid;
    border-color: rgb(221, 221, 221);
    border-image: initial;
    height: 36px;
    line-height: 36px;
    text-align: center;
    border-width: 0px 0px 1px 0px;
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

.login_view .login_form .link_view{
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 10px 0px 0px 0px;
    width: 100%;
    gap: 10px;
}
.login_view .login_form .link_view .divider{
    color: #d0d0d0;
    font-size: 14px;
    padding: 0 5px;
}
.login_view .login_form .link_view .register{
    padding: 0px 10px;
    height: 32px;
    font-size: 14px;
    color: #666;
    border: 0px;
    background: none;
    cursor: pointer;
    transition: all 0.3s;
}
.login_view .login_form .link_view .register:hover{
    color: var(--theme-color);
}
.login_view .login_form .link_view .forget{
    padding: 0px 10px;
    height: 32px;
    font-size: 14px;
    color: #666;
    border: 0px;
    background: none;
    cursor: pointer;
    transition: all 0.3s;
}
.login_view .login_form .link_view .forget:hover{
    color: var(--theme-color);
}
.login_view .login_form .face{
    font-size: 16px;
    color: rgb(102, 102, 102);
    cursor: pointer;
    padding: 0px 0px 0px 120px;
    text-align: left;
    width: 100%;
}

</style>