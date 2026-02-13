<template>
	<div>
		<div class="center_view edit_form">
			<el-form class="password_form" ref="passwordFormRef" :model="form" label-width="120px" :rules="rules">
				<el-form-item label="密码" prop="password1">
					<el-input class="list_inp" v-model="form.password1" type="password" show-password />
				</el-form-item>
				<el-form-item label="新密码" prop="password">
					<el-input class="list_inp" v-model="form.password" type="password" show-password />
				</el-form-item>
				<el-form-item label="确认密码" prop="password2">
					<el-input class="list_inp" v-model="form.password2" type="password" show-password />
				</el-form-item>
				<span class="formModel_btn_box">
					<el-button class="confirm_btn" type="primary" @click="onSubmit">保存</el-button>
				</span>
					
			</el-form>
		</div>
	</div>
</template>

<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue'
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const form = ref({})
	const user = ref({})
	const sessionTable = ref('')
	const passwordFormRef = ref(null)
	const rules = ref({
		password1: [{
			required: true,
			message: '请输入',
			trigger: 'blur'
		}, ],
		password: [{
			required: true,
			message: '请输入',
			trigger: 'blur'
		}, ],
		password2: [{
			required: true,
			message: '请输入',
			trigger: 'blur'
		}, ],
	})
	const onSubmit = async () => {
		passwordFormRef.value.validate(async (valid) => {
			if (valid) {
				if(sessionTable.value == 'admin'){
					if (form.value.password1 != user.value.password) {
						context?.$toolUtil.message('原密码不正确','error')
						return false
					}
					user.value.password = form.value.password
				}else{
					if(sessionTable.value == 'staff'){
						if(form.value.password1 != user.value.password){
							context?.$toolUtil.message('原密码不正确', 'error')
							return false
						}
					}
				}
				if (form.value.password2 != form.value.password) {
					context?.$toolUtil.message('两次密码输入不一致','error')
					return false
				}
				if(sessionTable.value == 'staff'){
					user.value.password = form.value.password
				}
				context?.$http({
					url: `${sessionTable.value}/update`,
					method: 'post',
					data: user.value
				}).then(res => {
					context?.$toolUtil.message('修改成功，下次登录将使用新密码登录','success')
				})
			}
		})

	}
	const getInfo = () => {
		sessionTable.value = context?.$toolUtil.storageGet('sessionTable')
		context?.$http({
			url: `${sessionTable.value}/session`,
			method: 'get'
		}).then(res => {
			user.value = res.data.data
		})
	}
	getInfo()
</script>

<style lang="scss" scoped>
	// 表单
	.password_form{
		// form item
		:deep(.el-form-item) {
			// 内容盒子
			.el-form-item__content{
				// 输入框
				.list_inp {
				}
			}
		}
		// 按钮盒子
		.formModel_btn_box {
			// 确定按钮
			.confirm_btn {
			}
			// 确定按钮-悬浮
			.confirm_btn:hover {
			}
		}
	}
</style>

