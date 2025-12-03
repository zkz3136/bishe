<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="餐厅名称" prop="cantingmingcheng">
							<el-input class="list_inp" v-model="form.cantingmingcheng" placeholder="餐厅名称"
                                type="text"
								:readonly="!isAdd||disabledForm.cantingmingcheng?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="餐桌名称" prop="canzhuomingcheng">
							<el-input class="list_inp" v-model="form.canzhuomingcheng" placeholder="餐桌名称"
                                type="text"
								:readonly="!isAdd||disabledForm.canzhuomingcheng?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item prop="fengmian"
									  label="封面"
						>
							<uploads
								:disabled="!isAdd||disabledForm.fengmian?true:false"
								action="file/upload"
								tip="请上传封面"
								style="width: 100%;text-align: left;"
								:fileUrls="form.fengmian?form.fengmian:''" 
								@change="fengmianUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="餐桌位置" prop="canzhuoweizhi">
							<el-input class="list_inp" v-model="form.canzhuoweizhi" placeholder="餐桌位置"
                                type="text"
								:readonly="!isAdd||disabledForm.canzhuoweizhi?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="可坐人数" prop="kezuorenshu">
							<el-input class="list_inp" v-model.number="form.kezuorenshu" placeholder="可坐人数"
                                type="text"
								:readonly="!isAdd||disabledForm.kezuorenshu?true:false" />
						</el-form-item>
					</el-col>

				<el-col :span="24">
					<el-form-item label="餐桌状态" prop="canzhuozhuangtai">
						<el-select
							class="list_sel"
							:disabled="!isAdd||disabledForm.canzhuozhuangtai?true:false"
							v-model="form.canzhuozhuangtai" 
							placeholder="请选择餐桌状态"
							>
							<el-option v-for="(item,index) in canzhuozhuangtaiLists" :label="item"
								:value="item"
								>
							</el-option>
						</el-select>
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
		computed,
		defineEmits
	} from 'vue'
    import {
        useStore
    } from 'vuex';
    const store = useStore()
    const user = computed(()=>store.getters['user/session'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;	
	const emit = defineEmits(['formModelChange'])
	//基础信息
	const tableName = 'cantingxinxi'
	const formName = '餐厅信息'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        cantingmingcheng : false,
        canzhuomingcheng : false,
        fengmian : false,
        canzhuoweizhi : false,
        kezuorenshu : false,
        canzhuozhuangtai : false,
        yuangongzhanghao : false,
        yuangongmingcheng : false,
        storeupNumber : false,
        discussNumber : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		cantingmingcheng: [
		],
		canzhuomingcheng: [
		],
		fengmian: [
		],
		canzhuoweizhi: [
		],
		kezuorenshu: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		canzhuozhuangtai: [
		],
		yuangongzhanghao: [
		],
		yuangongmingcheng: [
		],
		storeupNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		discussNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//封面上传回调
	const fengmianUploadSuccess=(e)=>{
		form.value.fengmian = e
	}
	//餐桌状态列表
	const canzhuozhuangtaiLists = ref([])

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			cantingmingcheng: '',
			canzhuomingcheng: '',
			fengmian: '',
			canzhuoweizhi: '',
			kezuorenshu: '',
			canzhuozhuangtai: '未预约',
			yuangongzhanghao: '',
			yuangongmingcheng: '',
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
				if(x=='cantingmingcheng'){
					form.value.cantingmingcheng = row[x];
					disabledForm.value.cantingmingcheng = true;
					continue;
				}
				if(x=='canzhuomingcheng'){
					form.value.canzhuomingcheng = row[x];
					disabledForm.value.canzhuomingcheng = true;
					continue;
				}
				if(x=='fengmian'){
					form.value.fengmian = row[x];
					disabledForm.value.fengmian = true;
					continue;
				}
				if(x=='canzhuoweizhi'){
					form.value.canzhuoweizhi = row[x];
					disabledForm.value.canzhuoweizhi = true;
					continue;
				}
				if(x=='kezuorenshu'){
					form.value.kezuorenshu = row[x];
					disabledForm.value.kezuorenshu = true;
					continue;
				}
				if(x=='canzhuozhuangtai'){
					form.value.canzhuozhuangtai = row[x];
					disabledForm.value.canzhuozhuangtai = true;
					continue;
				}
				if(x=='yuangongzhanghao'){
					form.value.yuangongzhanghao = row[x];
					disabledForm.value.yuangongzhanghao = true;
					continue;
				}
				if(x=='yuangongmingcheng'){
					form.value.yuangongmingcheng = row[x];
					disabledForm.value.yuangongmingcheng = true;
					continue;
				}
				if(x=='storeupNumber'){
					form.value.storeupNumber = row[x];
					disabledForm.value.storeupNumber = true;
					continue;
				}
				if(x=='discussNumber'){
					form.value.discussNumber = row[x];
					disabledForm.value.discussNumber = true;
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
			form.value.canzhuozhuangtai='未预约'
			formVisible.value = true
		}

	context?.$http({
		url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
		method: 'get'
	}).then(res => {
		var json = res.data.data
		// 商家字段已废弃，改为单一餐厅系统
	})
		canzhuozhuangtaiLists.value = "已预约,未预约".split(',')
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
		if(form.value.fengmian!=null) {
			form.value.fengmian = form.value.fengmian.replace(new RegExp(context?.$config.url,"g"),"");
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
