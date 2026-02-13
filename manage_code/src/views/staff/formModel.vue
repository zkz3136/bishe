<template>
	<el-dialog v-model="formVisible" :title="formTitle" width="50%" :before-close="closeClick">
		<el-form :model="form" class="formModel_form" ref="formRef" :rules="rules" label-width="120px">
			<el-form-item label="员工账号" prop="loginName" v-if="!isInfo">
				<el-input v-model="form.loginName" placeholder="请输入员工账号" :readonly="!isAdd"></el-input>
			</el-form-item>
			<el-form-item label="员工账号" v-if="isInfo">
				<span>{{form.loginName}}</span>
			</el-form-item>
			
			<el-form-item label="密码" prop="password" v-if="!isInfo">
				<el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
			</el-form-item>
			
			<el-form-item label="员工姓名" prop="name" v-if="!isInfo">
				<el-input v-model="form.name" placeholder="请输入员工姓名"></el-input>
			</el-form-item>
			<el-form-item label="员工姓名" v-if="isInfo">
				<span>{{form.name}}</span>
			</el-form-item>
			
			<el-form-item label="头像" prop="avatar">
				<el-upload
					v-if="!isInfo"
					class="avatar-uploader"
					:action="$config.url+'file/upload'"
					:show-file-list="false"
					:on-success="avatarUploadSuccess"
					:before-upload="beforeAvatarUpload">
					<img v-if="form.avatar" :src="form.avatar.indexOf('http')==0?form.avatar:$config.url+form.avatar" class="avatar" />
					<el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
				</el-upload>
				<img v-if="isInfo && form.avatar" :src="form.avatar.indexOf('http')==0?form.avatar:$config.url+form.avatar" class="avatar" />
			</el-form-item>
			
			<el-form-item label="职位" prop="position" v-if="!isInfo">
				<el-select v-model="form.position" placeholder="请选择职位">
					<el-option label="服务员" value="服务员"></el-option>
					<el-option label="收银员" value="收银员"></el-option>
					<el-option label="主管" value="主管"></el-option>
					<el-option label="经理" value="经理"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="职位" v-if="isInfo">
				<span>{{form.position}}</span>
			</el-form-item>
			
			<el-form-item label="电话" prop="mobile" v-if="!isInfo">
				<el-input v-model="form.mobile" placeholder="请输入电话"></el-input>
			</el-form-item>
			<el-form-item label="电话" v-if="isInfo">
				<span>{{form.mobile}}</span>
			</el-form-item>
			
			
		</el-form>
		<template #footer>
			<span class="formModel_btn_box">
				<el-button class="cancel_btn" @click="closeClick">取消</el-button>
				<el-button class="confirm_btn" type="primary" @click="confirmClick" v-if="!isInfo">确定</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup>
	import { Plus } from '@element-plus/icons-vue'
	import {
		ref,
		getCurrentInstance,
		nextTick,
		computed
	} from 'vue'
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const emit = defineEmits(['formModelChange'])
	//基础数据
	const tableName = 'staff'
	const formVisible = ref(false)
	const formRef = ref(null)
	const id = ref('')
	const type = ref('')
	const form = ref({
		loginName: '',
		password: '',
		name: '',
		avatar: '',
		position: '',
		mobile: ''
	})
	
	//计算属性
	const formTitle = computed(() => {
		if (type.value == 'info') {
			return '查看员工'
		} else if (type.value == 'edit') {
			return '修改员工'
		} else {
			return '新增员工'
		}
	})
	const isAdd = computed(() => {
		return type.value == 'add'
	})
	const isEdit = computed(() => {
		return type.value == 'edit'
	})
	const isInfo = computed(() => {
		return type.value == 'info'
	})
	
	//表单验证
	const rules = ref({
		loginName: [
			{ required: true, message: '请输入员工账号', trigger: 'blur' }
		],
		password: [
			{ required: true, message: '请输入密码', trigger: 'blur' }
		],
		name: [
			{ required: true, message: '请输入员工姓名', trigger: 'blur' }
		],
		mobile: [
			{ validator: context?.$toolUtil.validator.mobile, trigger: 'blur' }
		]
	})
	
	//头像上传
	const avatarUploadSuccess = (res) => {
		form.value.avatar = res.data.file
	}
	const beforeAvatarUpload = (file) => {
		const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
		const isLt2M = file.size / 1024 / 1024 < 2
		if (!isJPG) {
			context?.$toolUtil.message('上传头像图片只能是 JPG/PNG 格式!', 'error')
		}
		if (!isLt2M) {
			context?.$toolUtil.message('上传头像图片大小不能超过 2MB!', 'error')
		}
		return isJPG && isLt2M
	}
	
	//获取详情
	const getInfo = () => {
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			const data = res.data.data || {}
			form.value = {
				loginName: data.login_name || data.loginName || '',
				password: data.password || '',
				name: data.name || data.staff_name || '',
				avatar: data.avatar || '',
				position: data.position || '',
				mobile: data.mobile || ''
			}
		})
	}
	
	//关闭
	const closeClick = () => {
		formVisible.value = false
	}
	
	//确定
	const confirmClick = () => {
		formRef.value.validate((valid) => {
			if (valid) {
				if (type.value == 'add') {
					context?.$http({
						url: `${tableName}/save`,
						method: 'post',
						data: form.value
					}).then(res => {
						context?.$toolUtil.message('添加成功', 'success', () => {
							emit('formModelChange')
							closeClick()
						})
					})
				} else {
					context?.$http({
						url: `${tableName}/update`,
						method: 'post',
						data: form.value
					}).then(res => {
						context?.$toolUtil.message('修改成功', 'success', () => {
							emit('formModelChange')
							closeClick()
						})
					})
				}
			}
		})
	}
	
	//初始化
	const init = (formId, formType) => {
		formVisible.value = true
		type.value = formType || 'add'
		if (formId) {
			id.value = formId
			nextTick(() => {
				getInfo()
			})
		} else {
			form.value = {
				loginName: '',
				password: '',
				name: '',
				avatar: '',
				position: '',
				mobile: ''
			}
		}
		nextTick(() => {
			if (formRef.value) {
				formRef.value.clearValidate()
			}
		})
	}
	
	//暴露方法
	defineExpose({
		init
	})
</script>

<style lang="scss" scoped>
	.formModel_form {
		padding: 20px;
	}
	.formModel_btn_box {
		text-align: center;
		.el-button {
			margin: 0 10px;
		}
	}
	.avatar-uploader .avatar {
		width: 100px;
		height: 100px;
		display: block;
		border-radius: 5px;
	}
	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
		transition: .3s;
	}
	.avatar-uploader .el-upload:hover {
		border-color: #409EFF;
	}
	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 100px;
		height: 100px;
		line-height: 100px;
		text-align: center;
	}
	.avatar {
		width: 100px;
		height: 100px;
		display: block;
		border-radius: 5px;
	}
</style>
