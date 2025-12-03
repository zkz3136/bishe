<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="活动名称" prop="huodongmingcheng">
							<el-input class="list_inp" v-model="form.huodongmingcheng" placeholder="活动名称"
                                type="text"
								:readonly="!isAdd||disabledForm.huodongmingcheng?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item prop="huodongtupian"
									  label="活动图片"
						>
							<uploads
								:disabled="!isAdd||disabledForm.huodongtupian?true:false"
								action="file/upload"
								tip="请上传活动图片"
								style="width: 100%;text-align: left;"
								:fileUrls="form.huodongtupian?form.huodongtupian:''" 
								@change="huodongtupianUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="开始时间" prop="kaishishijian">
							<el-date-picker
								class="list_date"
								v-model="form.kaishishijian"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:readonly="!isAdd||disabledForm.kaishishijian?true:false"
								placeholder="请选择开始时间" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="活动地点" prop="huodongdidian">
							<el-input class="list_inp" v-model="form.huodongdidian" placeholder="活动地点"
                                type="text"
								:readonly="!isAdd||disabledForm.huodongdidian?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="发布时间" prop="fabushijian">
							<el-date-picker
								class="list_date"
								v-model="form.fabushijian"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:readonly="!isAdd||disabledForm.fabushijian?true:false"
								placeholder="请选择发布时间" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="结束时间" prop="reversetime">
							<el-date-picker
								class="list_date"
								v-model="form.reversetime"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:readonly="!isAdd||disabledForm.reversetime?true:false"
								placeholder="请选择结束时间" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="活动内容" prop="huodongneirong">
							<editor :value="form.huodongneirong" placeholder="请输入活动内容" :readonly="!isAdd||disabledForm.huodongneirong?true:false"
								class="list_editor" @change="(e)=>editorChange(e,'huodongneirong')"></editor>
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
	const tableName = 'dianpuhuodong'
	const formName = '店铺活动'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        huodongmingcheng : false,
        huodongtupian : false,
        kaishishijian : false,
        huodongdidian : false,
        huodongneirong : false,
        fabushijian : false,
        reversetime : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		huodongmingcheng: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		huodongtupian: [
		],
		kaishishijian: [
		],
		huodongdidian: [
		],
		huodongneirong: [
		],
		fabushijian: [
		],
		reversetime: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//活动图片上传回调
	const huodongtupianUploadSuccess=(e)=>{
		form.value.huodongtupian = e
	}

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			huodongmingcheng: '',
			huodongtupian: '',
			kaishishijian: '',
			huodongdidian: '',
			huodongneirong: '',
			fabushijian: '',
			reversetime: '',
		}
	}
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			res.data.data.huodongneirong = res.data.data.huodongneirong?(res.data.data.huodongneirong.replace(reg,'../../../cl200442145/file')):'';
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
			form.value.fabushijian = context?.$toolUtil.getCurDateTime()
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
				if(x=='huodongmingcheng'){
					form.value.huodongmingcheng = row[x];
					disabledForm.value.huodongmingcheng = true;
					continue;
				}
				if(x=='huodongtupian'){
					form.value.huodongtupian = row[x];
					disabledForm.value.huodongtupian = true;
					continue;
				}
				if(x=='kaishishijian'){
					form.value.kaishishijian = row[x];
					disabledForm.value.kaishishijian = true;
					continue;
				}
				if(x=='huodongdidian'){
					form.value.huodongdidian = row[x];
					disabledForm.value.huodongdidian = true;
					continue;
				}
				if(x=='huodongneirong'){
					form.value.huodongneirong = row[x];
					disabledForm.value.huodongneirong = true;
					continue;
				}
				if(x=='fabushijian'){
					form.value.fabushijian = row[x];
					disabledForm.value.fabushijian = true;
					continue;
				}
				if(x=='reversetime'){
					form.value.reversetime = row[x];
					disabledForm.value.reversetime = true;
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
		if(form.value.huodongtupian!=null) {
			form.value.huodongtupian = form.value.huodongtupian.replace(new RegExp(context?.$config.url,"g"),"");
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
				// 富文本
				.list_editor {
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
