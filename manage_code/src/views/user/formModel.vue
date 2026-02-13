<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="账号" prop="login_name">
							<el-input class="list_inp" v-model="form.login_name" placeholder="账号"
								type="text" :readonly="!isAdd||disabledForm.login_name?true:false" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item prop="avatar"
									  label="头像"
						>
							<uploads
								:disabled="!isAdd||disabledForm.avatar?true:false"
								action="file/upload"
								tip="请上传头像"
								style="width: 100%;text-align: left;"
								:fileUrls="form.avatar?form.avatar:''" 
								@change="avatarUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="密码" prop="password">
							<el-input class="list_inp" v-model="form.password" placeholder="密码"
								type="password" :readonly="!isAdd||disabledForm.password?true:false" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="名称" prop="name">
							<el-input class="list_inp" v-model="form.name" placeholder="名称"
                                type="text"
								:readonly="!isAdd||disabledForm.name?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="性别" prop="gender">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.gender?true:false"
								v-model="form.gender" 
								placeholder="请选择性别"
								>
								<el-option v-for="(item,index) in genderLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="手机号码" prop="mobile">
							<el-input class="list_inp" v-model="form.mobile" placeholder="手机号码"
                                type="text"
								:readonly="!isAdd||disabledForm.mobile?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="密保问题" prop="security_question">
							<el-input class="list_inp" v-model="form.security_question" placeholder="密保问题"
                                type="text"
								:readonly="!isAdd||disabledForm.security_question?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="密保答案" prop="security_answer">
							<el-input class="list_inp" v-model="form.security_answer" placeholder="密保答案"
                                type="text"
								:readonly="!isAdd||disabledForm.security_answer?true:false" />
						</el-form-item>
					</el-col>

				</el-row>
			</el-form>
			<template #footer v-if="isAdd||type=='logistics'||type=='reply'">
				<span class="formModel_btn_box">
					<el-button class="cancel_btn" @click="closeClick">取消</el-button>
					<el-button class="confirm_btn" type="primary" @click="save"
						>
						提交
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		computed
	} from 'vue'
    import {
        useStore
    } from 'vuex';
    const store = useStore()
    const user = computed(()=>store.getters['user/session'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;	
	const emit = defineEmits(['formModelChange'])
	//基础信息
	const tableName = 'user'
	const formName = '用户'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        login_name : false,
        avatar : false,
        password : false,
        name : false,
        gender : false,
        mobile : false,
        security_question : false,
        security_answer : false,
        maxPasswordWrong : false,
        isLocked : false,
        balance : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		login_name: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		avatar: [
		],
		password: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		name: [
		],
		gender: [
		],
		mobile: [
			{ validator: context.$toolUtil.validator.mobile, trigger: 'blur' },
		],
		security_question: [
		],
		security_answer: [
		],
		maxPasswordWrong: [
			{required: true,message: '请输入',trigger: 'blur'}, 
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		isLocked: [
			{required: true,message: '请输入',trigger: 'blur'}, 
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		balance: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//头像上传回调
	const avatarUploadSuccess=(e)=>{
		form.value.avatar = e
	}
	//性别列表
	const genderLists = ref([])

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			login_name: '',
			avatar: '',
			password: '',
			name: '',
			gender: '',
			mobile: '',
			security_question: '',
			security_answer: '',
		}
	}
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			form.value = res.data.data
			formVisible.value = true
		})
	}
	const crossRow = ref('')
	const crossTable = ref('')
	const crossTips = ref('')
	const crossColumnName = ref('')
	const crossColumnValue = ref('')
	//初始化
	const init=(formId=null,formType='add',formNames='',row=null,table=null,statusColumnName=null,tips=null,statusColumnValue=null)=>{
		resetForm()
		if(formId){
			id.value = formId
			type.value = formType
		}
		if(formType == 'add'){
			isAdd.value = true
			formTitle.value = '新增' + formName
			formVisible.value = true
		}else if(formType == 'info'){
			isAdd.value = false
			formTitle.value = '查看' + formName
			getInfo()
		}else if(formType == 'edit'){
			isAdd.value = true
			formTitle.value = '修改' + formName
			getInfo()
		}
		else if(formType == 'cross'){
			isAdd.value = true
			formTitle.value = formNames
			// getInfo()
			for(let x in row){
				if(x=='login_name'){
					form.value.login_name = row[x];
					disabledForm.value.login_name = true;
					continue;
				}
				if(x=='account'){
					form.value.login_name = row[x];
					disabledForm.value.login_name = true;
					continue;
				}
				if(x=='avatar'){
					form.value.avatar = row[x];
					disabledForm.value.avatar = true;
					continue;
				}
				if(x=='password'){
					form.value.password = row[x];
					disabledForm.value.password = true;
					continue;
				}
				if(x=='name'){
					form.value.name = row[x];
					disabledForm.value.name = true;
					continue;
				}
				if(x=='gender'){
					form.value.gender = row[x];
					disabledForm.value.gender = true;
					continue;
				}
				if(x=='mobile'){
					form.value.mobile = row[x];
					disabledForm.value.mobile = true;
					continue;
				}
				if(x=='security_question'){
					form.value.security_question = row[x];
					disabledForm.value.security_question = true;
					continue;
				}
				if(x=='security_answer'){
					form.value.security_answer = row[x];
					disabledForm.value.security_answer = true;
					continue;
				}
				if(x=='maxPasswordWrong'){
					form.value.maxPasswordWrong = row[x];
					disabledForm.value.maxPasswordWrong = true;
					continue;
				}
				if(x=='isLocked'){
					form.value.isLocked = row[x];
					disabledForm.value.isLocked = true;
					continue;
				}
				if(x=='balance'){
					form.value.balance = row[x];
					disabledForm.value.balance = true;
					continue;
				}
			}
			if(row){
				crossRow.value = row
			}
			if(table){
				crossTable.value = table
			}
			if(tips){
				crossTips.value = tips
			}
			if(statusColumnName){
				crossColumnName.value = statusColumnName
			}
			if(statusColumnValue){
				crossColumnValue.value = statusColumnValue
			}
			formVisible.value = true
		}

		context?.$http({
			url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
		})
		genderLists.value = "男,女".split(',')
	}
	//初始化
	//声明父级调用
	defineExpose({
		init
	})
	//关闭
	const closeClick = () => {
		formVisible.value = false
	}
	//富文本
	const editorChange = (e,name) =>{
		form.value[name] = e
	}
	//提交
	const save= async ()=>{
		if(form.value.avatar!=null) {
			form.value.avatar = form.value.avatar.replace(new RegExp(context?.$config.url,"g"),"");
		}
		var table = crossTable.value
		var objcross = JSON.parse(JSON.stringify(crossRow.value))
		let crossUserId = ''
		let crossRefId = ''
		let crossOptNum = ''
		if(type.value == 'cross'){
			if(crossColumnName.value!=''){
				if(!crossColumnName.value.startsWith('[')){
					for(let o in objcross){
						if(o == crossColumnName.value){
							objcross[o] = crossColumnValue.value
						}
					}
					//修改跨表数据
					changeCrossData(objcross)
				}else{
					crossUserId = user.value.id
					crossRefId = objcross['id']
					crossOptNum = crossColumnName.value.replace(/\[/,"").replace(/\]/,"")
				}
			}
		}
		formRef.value.validate((valid)=>{
			if(valid){
				if(crossUserId&&crossRefId){
					form.value.crossuserid = crossUserId
					form.value.crossrefid = crossRefId
					let params = {
						page: 1,
						limit: 1000, 
						crossuserid:form.value.crossuserid,
						crossrefid:form.value.crossrefid,
					}
					context?.$http({
						url: `${tableName}/page`,
						method: 'get', 
						params: params 
					}).then(res=>{
						if(res.data.data.total>=crossOptNum){
							context?.$toolUtil.message(`${crossTips.value}`,'error')
							return false
						}else{
							context?.$http({
								url: `${tableName}/${!form.value.id ? "save" : "update"}`,
								method: 'post', 
								data: form.value 
							}).then(async res=>{
								emit('formModelChange')
								context?.$toolUtil.message(`操作成功`,'success')
                                formVisible.value = false
							})
						}
					})
				}else{
					context?.$http({
						url: `${tableName}/${!form.value.id ? "save" : "update"}`,
						method: 'post', 
						data: form.value 
					}).then(async (res)=>{
						emit('formModelChange')
						context?.$toolUtil.message(`操作成功`,'success')
                        formVisible.value = false
					})
				}
			}else{
                context.$message.error('请完善信息')
            }
		})
	}
	//修改跨表数据
	const changeCrossData = async (row)=>{
        if(type.value == 'cross'){
            await context?.$http({
                url: `${crossTable.value}/update`,
                method: 'post',
                data: row
            }).then(res=>{})
        }
	}
</script>
<style lang="scss" scoped>
	// 表单
	.formModel_form{
		// form item
		:deep(.el-form-item) {
			//label
			.el-form-item__label {
			}
			// 内容盒子
			.el-form-item__content {
				// 输入框
				.list_inp {
				}
				// 下拉框
				.list_sel {
					//去掉默认样式
					.select-trigger{
						height: 100%;
						.el-input{
							height: 100%;
						}
					}
				}
				//图片上传样式
				.el-upload-list  {
					//提示语
					.el-upload__tip {
					}
					//外部盒子
					.el-upload--picture-card {
						//图标
						.el-icon{
						}
					}
					.el-upload-list__item {
					}
				}
			}
		}
	}
	// 按钮盒子
	.formModel_btn_box {
		.cancel_btn {
		}
		.cancel_btn:hover {
		}
		
		.confirm_btn {
		}
		.confirm_btn:hover {
		}
	}
</style>
