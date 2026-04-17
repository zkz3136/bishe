<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="车位编号" prop="spot_number">
							<el-input class="list_inp" v-model="form.spot_number" placeholder="车位编号"
                                type="text"
								:readonly="!isAdd||disabledForm.spot_number?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item prop="spot_image"
									  label="车位图片"
						>
							<uploads
								:disabled="!isAdd||disabledForm.spot_image?true:false"
								action="file/upload"
								tip="请上传车位图片"
								style="width: 100%;text-align: left;"
								:fileUrls="form.spot_image?form.spot_image:''" 
								@change="spot_imageUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="区域" prop="area">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.area?true:false"
								v-model="form.area" 
								placeholder="请选择区域"
								>
								<el-option v-for="(item,index) in areaLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="车位位置" prop="spot_location">
							<el-input class="list_inp" v-model="form.spot_location" placeholder="车位位置"
                                type="text"
								:readonly="!isAdd||disabledForm.spot_location?true:false" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="小时价格" prop="hourly_price">
							<el-input class="list_inp" v-model="form.hourly_price" placeholder="小时价格"
                                type="number"
								:readonly="!isAdd||disabledForm.hourly_price?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="车位状态" prop="spot_status">
							<template v-if="type==='add'">
								<el-input class="list_inp" v-model="form.spot_status" readonly />
							</template>
							<template v-else>
								<el-select
									class="list_sel"
									:disabled="!isAdd||disabledForm.spot_status?true:false"
									v-model="form.spot_status" 
									placeholder="请选择车位状态"
									>
									<el-option v-for="(item,index) in spot_statusLists" :label="item"
										:value="item"
										>
									</el-option>
								</el-select>
							</template>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="车牌号" prop="plate_number">
							<el-input class="list_inp" v-model="form.plate_number" placeholder="车牌号" :readonly="true" />
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
	const tableName = 'parking_spot'
	const formName = '车位信息'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        spot_number : false,
        spot_image : false,
        area : false,
        spot_location : false,
        hourly_price : false,
        spot_status : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		spot_number: [
		],
		spot_image: [
		],
		area: [
		],
		spot_location: [
		],
		hourly_price: [
		],
		spot_status: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//车位图片上传回调
	const spot_imageUploadSuccess=(e)=>{
		form.value.spot_image = e
	}
	//区域列表
	const areaLists = ref([])
	//车位状态列表
	const spot_statusLists = ref([])

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			spot_number: '',
			spot_image: '',
			area: '',
			spot_location: '',
			hourly_price: 10,
			spot_status: '空闲',
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
			if(form.value.hourly_price===undefined||form.value.hourly_price===null||String(form.value.hourly_price)===''||Number(form.value.hourly_price)<=0){
				form.value.hourly_price = 10
			}
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
		type.value = formType
		if(formId){
			id.value = formId
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
				if(x=='spot_number'){
					form.value.spot_number = row[x];
					disabledForm.value.spot_number = true;
					continue;
				}
				if(x=='spot_image'){
					form.value.spot_image = row[x];
					disabledForm.value.spot_image = true;
					continue;
				}
				if(x=='area'){
					form.value.area = row[x];
					disabledForm.value.area = true;
					continue;
				}
				if(x=='spot_location'){
					form.value.spot_location = row[x];
					disabledForm.value.spot_location = true;
					continue;
				}
				if(x=='hourly_price'){
					form.value.hourly_price = row[x];
					disabledForm.value.hourly_price = true;
					continue;
				}
				if(x=='spot_status'){
					form.value.spot_status = row[x];
					disabledForm.value.spot_status = true;
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
			form.value.spot_status='空闲'
			formVisible.value = true
		}

		context?.$http({
			url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
			const role = String(context?.$toolUtil.storageGet("role")||'').trim()
			if(role !== "管理员"){
				disabledForm.value.hourly_price = true
				disabledForm.value.spot_status = true
			}else{
				disabledForm.value.hourly_price = false
				// 管理员新增也不允许修改状态
				if(type.value === 'add'){
					disabledForm.value.spot_status = true
				}else{
					disabledForm.value.spot_status = false
				}
			}
		})
		areaLists.value = "A,B,C,D,F".split(',')
		spot_statusLists.value = "已使用,空闲".split(',')
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
		if(form.value.spot_image!=null) {
			form.value.spot_image = form.value.spot_image.replace(new RegExp(context?.$config.url,"g"),"");
		}
		if(form.value.hourly_price!==undefined && form.value.hourly_price!==null && form.value.hourly_price!==''){
			const n = Number(form.value.hourly_price)
			if(!Number.isNaN(n)) form.value.hourly_price = n
		}
		const payload = {
			...form.value,
			spotNumber: form.value.spot_number,
			spotImage: form.value.spot_image,
			spotLocation: form.value.spot_location,
			hourlyPrice: form.value.hourly_price,
			spotStatus: form.value.spot_status,
		}
		// 车位编号唯一性校验（新增或修改后编号与其他记录重复时阻止提交）
		try {
			const checkRes = await context?.$http({
				url: `${tableName}/page`,
				method: 'get',
				params: {
					page: 1,
					limit: 1,
					spotNumber: form.value.spot_number
				}
			})
			const existsList = checkRes?.data?.data?.list || []
			if (Array.isArray(existsList) && existsList.length) {
				const dup = existsList.some(it => it && it.id !== form.value.id)
				if (dup) {
					context?.$toolUtil.message('车位编号已存在','error')
					return
				}
			}
		} catch (e) { console.error(e) }
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
								data: payload 
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
						data: payload 
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
<style>
.edit_form_modal {
	// 遮罩层
	.el-overlay {
		// 弹窗
		.el-dialog {
			// 头部
			.el-dialog__header {
				// 标题
				.el-dialog__title {
				}
				// 关闭按钮
				.el-dialog__headerbtn {
					// 图标
					.el-dialog__close {
					}
				}
			}
			// 内容
			.el-dialog__body {
			}
			// 底部
			.el-dialog__footer {
			}
		}
	}
}
</style>
