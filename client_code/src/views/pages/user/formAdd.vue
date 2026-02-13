<template>
	<div class="edit_view" :style='{}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
        </div>
		<el-form ref="formRef" :model="form" class="add_form" label-width="120px" :rules="rules">
			<el-row>
				<el-col :span="12">
					<el-form-item label="账号" prop="login_name">
						<el-input class="list_inp" v-model="form.login_name" placeholder="账号"
							type="text" :readonly="!isAdd||disabledForm.login_name?true:false" />
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="头像" prop="avatar">
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
				<el-col :span="12">
					<el-form-item label="密码" prop="password">
						<el-input class="list_inp" v-model="form.password" placeholder="密码"
							type="password" :readonly="!isAdd||disabledForm.password?true:false" />
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="名称" prop="name">
						<el-input class="list_inp" v-model="form.name" placeholder="名称"
							 type="text" 							:readonly="!isAdd||disabledForm.name?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="性别" prop="gender">
						<el-select
							class="list_sel"
							:disabled="!isAdd||disabledForm.gender?true:false"
							v-model="form.gender" 
							placeholder="请选择性别"
							style="width:100%;"
							>
							<el-option v-for="(item,index) in genderLists" :label="item"
								:value="item"
								>
							</el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="手机号码" prop="mobile">
						<el-input class="list_inp" v-model="form.mobile" placeholder="手机号码"
							 type="text" 							:readonly="!isAdd||disabledForm.mobile?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="密保问题" prop="security_question">
						<el-input class="list_inp" v-model="form.security_question" placeholder="密保问题"
							 type="text" 							:readonly="!isAdd||disabledForm.security_question?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="密保答案" prop="security_answer">
						<el-input class="list_inp" v-model="form.security_answer" placeholder="密保答案"
							 type="text" 							:readonly="!isAdd||disabledForm.security_answer?true:false" />
					</el-form-item>
				</el-col>

			</el-row>
			<div class="formModel_btn_box">
				<el-button class="formModel_cancel" @click="backClick">取消</el-button>
				<el-button class="formModel_confirm"
                           @click="save"
                           type="success"
				>
					保存
				</el-button>
			</div>
		</el-form>
	</div>
</template>
<script setup>
	import {
		ref,
		getCurrentInstance,
		watch,
		onUnmounted,
		onMounted,
		nextTick,
		computed
	} from 'vue';
	import {
		useRoute,
		useRouter
	} from 'vue-router';
    import moment from "moment";
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const route = useRoute()
	const router = useRouter()
	//基础信息
	const tableName = context?.$toolUtil.storageGet('frontSessionTable') || 'user'
	const formName = '用户'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//form表单
	const form = ref({
		login_name: '',
		avatar: '',
		password: '',
		name: '',
		gender: '',
		mobile: '',
		security_question: '',
		security_answer: '',
	})
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
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
	const isAdd = ref(false)
	//表单验证
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
	//头像上传回调
	const avatarUploadSuccess=(e)=>{
		form.value.avatar = e
	}
	//性别列表
	const genderLists = ref([])
	//methods

	//methods
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			form.value = res.data.data
		})
	}
	const crossRow = ref('')
	const crossTable = ref('')
	const crossTips = ref('')
	const crossColumnName = ref('')
	const crossColumnValue = ref('')
	//初始化
	const init = (formId=null,formType='add',formNames='',row=null,table=null,statusColumnName=null,tips=null,statusColumnValue=null) => {
		if(formId){
			id.value = formId
			type.value = formType
		}
		if(formType == 'add'){
			isAdd.value = true
		}else if(formType == 'info'){
			isAdd.value = false
			getInfo()
		}else if(formType == 'edit'){
			isAdd.value = true
			getInfo()
		}
		else if(formType == 'cross'){
			isAdd.value = true
			// getInfo()
			for(let x in row){
				if(x=='login_name'){
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
		}
		context?.$http({
			url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
            if (localStorage.getItem('autoSave')) {
                localStorage.removeItem('autoSave')
                save()
            }
		})
		genderLists.value = "男,女".split(',')
	}
	//初始化
	//取消
	const backClick = () => {
		history.back()
	}
	//提交
	const save=()=>{
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
					crossUserId = context?.$toolUtil.storageGet('user_id')
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
							}).then(res=>{
                                context?.$toolUtil.message(`操作成功`,'success')
                                history.back()
							})
						}
					})
				}else{
					context?.$http({
						url: `${tableName}/${!form.value.id ? "save" : "update"}`,
						method: 'post', 
						data: form.value 
					}).then(res=>{
                        context?.$toolUtil.message(`操作成功`,'success')
                        history.back()
					})
				}
			}
		})
	}
	//修改跨表数据
	const changeCrossData=(row)=>{
        if(type.value == 'cross'){
            context?.$http({
                url: `${crossTable.value}/update`,
                method: 'post',
                data: row
            }).then(res=>{})
        }
	}
	onMounted(()=>{
		type.value = route.query.type?route.query.type:'add'
		let row = null
		let table = null
		let statusColumnName = null
		let tips = null
		let statusColumnValue = null
		if(type.value == 'cross'){
			row = context?.$toolUtil.storageGet('crossObj')?JSON.parse(context?.$toolUtil.storageGet('crossObj')):{}
			table = context?.$toolUtil.storageGet('crossTable')
			statusColumnName = context?.$toolUtil.storageGet('crossStatusColumnName')
			tips = context?.$toolUtil.storageGet('crossTips')
			statusColumnValue = context?.$toolUtil.storageGet('crossStatusColumnValue')
		}
		const qid = route.query.id
		const rawId = Array.isArray(qid) ? qid[0] : qid
		const id = rawId && rawId !== 'undefined' && rawId !== 'null' ? rawId : null
		init(id, type.value,'', row, table, statusColumnName, tips, statusColumnValue)
	})
    onUnmounted(()=>{
        Object.keys(localStorage).map(item=>{
            if(item.startsWith('cross')){
                localStorage.removeItem(item)
            }
        })
    })
</script>
<style lang="scss" scoped>
	// 面包屑盒子
	.bread_view {
		:deep(.breadcrumb) {
			.el-breadcrumb__separator {
			}
			.first_breadcrumb {
				.el-breadcrumb__inner {
				}
			}
			.second_breadcrumb {
				.el-breadcrumb__inner {
				}
			}
		}
	}
	// 表单
	.add_form{
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
		.formModel_cancel {
		}
		.formModel_cancel:hover {
		}
		
		.formModel_confirm {
		}
		.formModel_confirm:hover {
		}
	}
</style>
<style>
.edit_view {
    width: 1200px;
    margin: 20px auto;
    padding: 40px 20px 20px ;
    background: #fff;
    overflow: hidden;
    border: 0px solid #eee;
    font-size: 16px;
    color:#666;
}
.edit_view .add_form{
    width: 100%;
    padding: 30px;
    border:0px solid #eee
}
.edit_view .add_form .el-form-item{
    margin: 0px 0px 20px;
    display: flex;
}
.edit_view .add_form .el-form-item .el-form-item__label{
    width: 150px;
    background: none;
    text-align: right;
    display: block;
    font-size: 16px;
    color: rgb(51, 51, 51);
    font-weight: 500;
}
.edit_view .add_form .el-form-item .el-form-item__content{
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-wrap: wrap;
    width: calc(100% - 120px);
}
.edit_view .add_form .el-form-item .el-form-item__content .list_inp{
    height: 36px;
    line-height: 36px;
    border: 1px solid rgb(221, 221, 221);
    padding: 0px 10px;
    width: 100%;
    box-sizing: border-box;
    background: rgb(255, 255, 255);
    font-size: 16px;
}

.edit_view .add_form .el-form-item .el-form-item__content .list_sel{
    line-height: 36px;
    border: 1px solid rgb(221, 221, 221);
    box-sizing: border-box;
    width: calc(100% - 0px);
    padding: 0px 10px;
    border-radius: 0px;
    background: rgb(255, 255, 255);
    font-size: 16px;
}







.edit_view .add_form .el-form-item .el-form-item__content .el-upload--picture-card{
    background-color: rgb(255, 255, 255);
    width: 100px;
    height: 90px;
    line-height: 100px;
    text-align: center;
    border: 1px solid rgb(221, 221, 221);
    border-radius: 0px;
    cursor: pointer;
}

.edit_view .add_form .el-form-item .el-form-item__content .el-upload--picture-card .el-icon{
    font-size: 32px;
    color: #999;
}
.edit_view .add_form .el-form-item .el-form-item__content .el-upload--picture-card .el-icon svg{
    color: #999;
}


.edit_view .add_form .el-form-item .el-form-item__content .img-uploader .el-upload__tip{
    font-size: 15px;
    color: #666;
    margin: 0;
}



</style>
