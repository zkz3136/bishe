<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="餐桌名称" prop="seat_name">
							<el-input class="list_inp" v-model="form.seat_name" placeholder="餐桌名称"
                                type="text"
								:readonly="!isAdd||disabledForm.seat_name?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item prop="cover_image"
									  label="封面"
						>
							<uploads
								:disabled="!isAdd||disabledForm.cover_image?true:false"
								action="file/upload"
								tip="请上传封面"
								style="width: 100%;text-align: left;"
								:fileUrls="form.cover_image?form.cover_image:''" 
								@change="cover_imageUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="餐桌位置" prop="table_location">
							<el-input class="list_inp" v-model="form.table_location" placeholder="餐桌位置"
                                type="text"
								:readonly="!isAdd||disabledForm.table_location?true:false" />
						</el-form-item>
					</el-col>

				<el-col :span="24">
					<el-form-item label="可坐人数" prop="capacity">
						<el-input class="list_inp" v-model.number="form.capacity" placeholder="可坐人数"
                            type="text"
							:readonly="!isAdd||disabledForm.capacity?true:false" />
					</el-form-item>
				</el-col>

					<el-col :span="24">
						<el-form-item label="账号" prop="login_name">
							<el-input class="list_inp" v-model="form.login_name" placeholder="账号"
                                type="text"
								:readonly="!isAdd||disabledForm.login_name?true:false" />
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
						<el-form-item label="预约时间" prop="reservation_time">
							<el-date-picker
								class="list_date"
								v-model="form.reservation_time"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:disabled-date="reservation_timeDisabledDate"
								:readonly="!isAdd||disabledForm.reservation_time?true:false"
								placeholder="请选择预约时间" />
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
	const tableName = 'restaurant_reservation'
	const formName = '餐厅预约'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        seat_name : false,
        cover_image : false,
        table_location : false,
        capacity : false,
        staff_account : false,
        storeupNumber : false,
        login_name : false,
        name : false,
        reservation_time : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		seat_name: [
		],
		cover_image: [
		],
		table_location: [
		],
		capacity: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
        staff_account: [
        ],
		storeupNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		login_name: [
		],
		name: [
		],
		reservation_time: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//封面上传回调
	const cover_imageUploadSuccess=(e)=>{
		form.value.cover_image = e
	}
	// 限制预约时间只能选择今天和以后的日期
	const reservation_timeDisabledDate = (date) => {
		return date.getTime() < Date.now() - 8.64e7;
	}

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			seat_name: '',
			cover_image: '',
			table_location: '',
			capacity: '',
            staff_account: '',
			storeupNumber: '',
			login_name: '',
			name: '',
			reservation_time: '',
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
				if(x=='seat_name'){
					form.value.seat_name = row[x];
					disabledForm.value.seat_name = true;
					continue;
				}
				if(x=='cover_image'){
					form.value.cover_image = row[x];
					disabledForm.value.cover_image = true;
					continue;
				}
				if(x=='table_location'){
					form.value.table_location = row[x];
					disabledForm.value.table_location = true;
					continue;
				}
				if(x=='capacity'){
					form.value.capacity = row[x];
					disabledForm.value.capacity = true;
					continue;
				}
				if(x=='staff_account'){
					form.value.staff_account = row[x];
					disabledForm.value.staff_account = true;
					continue;
				}
				if(x=='storeupNumber'){
					form.value.storeupNumber = row[x];
					disabledForm.value.storeupNumber = true;
					continue;
				}
				if(x=='account'){
					form.value.login_name = row[x];
					disabledForm.value.login_name = true;
					continue;
				}
				if(x=='name'){
					form.value.name = row[x];
					disabledForm.value.name = true;
					continue;
				}
				if(x=='reservation_time'){
					form.value.reservation_time = row[x];
					disabledForm.value.reservation_time = true;
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
		// 商家字段已废弃，改为单一餐厅系统
		if(json.hasOwnProperty('login_name')&& context?.$toolUtil.storageGet("role")!="管理员"){
			form.value.login_name = json.login_name
			disabledForm.value.login_name = true;
		}
		if(json.hasOwnProperty('name')&& context?.$toolUtil.storageGet("role")!="管理员"){
			form.value.name = json.name
			disabledForm.value.name = true;
		}
	})
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
		if(form.value.cover_image!=null) {
			form.value.cover_image = form.value.cover_image.replace(new RegExp(context?.$config.url,"g"),"");
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
				//日期选择器
				.list_date {
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

