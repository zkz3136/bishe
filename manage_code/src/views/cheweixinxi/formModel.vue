<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="车位编号" prop="cheweibianhao">
							<el-input class="list_inp" v-model="form.cheweibianhao" placeholder="车位编号"
                                type="text"
								:readonly="!isAdd||disabledForm.cheweibianhao?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item prop="cheweitupian"
									  label="车位图片"
						>
							<uploads
								:disabled="!isAdd||disabledForm.cheweitupian?true:false"
								action="file/upload"
								tip="请上传车位图片"
								style="width: 100%;text-align: left;"
								:fileUrls="form.cheweitupian?form.cheweitupian:''" 
								@change="cheweitupianUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="区域" prop="quyu">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.quyu?true:false"
								v-model="form.quyu" 
								placeholder="请选择区域"
								>
								<el-option v-for="(item,index) in quyuLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="车位位置" prop="cheweiweizhi">
							<el-input class="list_inp" v-model="form.cheweiweizhi" placeholder="车位位置"
                                type="text"
								:readonly="!isAdd||disabledForm.cheweiweizhi?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="车位状态" prop="cheweizhuangtai">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.cheweizhuangtai?true:false"
								v-model="form.cheweizhuangtai" 
								placeholder="请选择车位状态"
								>
								<el-option v-for="(item,index) in cheweizhuangtaiLists" :label="item"
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
	const tableName = 'cheweixinxi'
	const formName = '车位信息'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        cheweibianhao : false,
        cheweitupian : false,
        quyu : false,
        cheweiweizhi : false,
        cheweizhuangtai : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		cheweibianhao: [
		],
		cheweitupian: [
		],
		quyu: [
		],
		cheweiweizhi: [
		],
		cheweizhuangtai: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//车位图片上传回调
	const cheweitupianUploadSuccess=(e)=>{
		form.value.cheweitupian = e
	}
	//区域列表
	const quyuLists = ref([])
	//车位状态列表
	const cheweizhuangtaiLists = ref([])

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			cheweibianhao: '',
			cheweitupian: '',
			quyu: '',
			cheweiweizhi: '',
			cheweizhuangtai: '空闲',
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
				if(x=='cheweibianhao'){
					form.value.cheweibianhao = row[x];
					disabledForm.value.cheweibianhao = true;
					continue;
				}
				if(x=='cheweitupian'){
					form.value.cheweitupian = row[x];
					disabledForm.value.cheweitupian = true;
					continue;
				}
				if(x=='quyu'){
					form.value.quyu = row[x];
					disabledForm.value.quyu = true;
					continue;
				}
				if(x=='cheweiweizhi'){
					form.value.cheweiweizhi = row[x];
					disabledForm.value.cheweiweizhi = true;
					continue;
				}
				if(x=='cheweizhuangtai'){
					form.value.cheweizhuangtai = row[x];
					disabledForm.value.cheweizhuangtai = true;
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
			form.value.cheweizhuangtai='空闲'
			formVisible.value = true
		}

		context?.$http({
			url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
			if(context?.$toolUtil.storageGet("role")!="管理员" && !context?.$toolUtil.storageGet("isAdmin")) {
				disabledForm.value.cheweizhuangtai = true;
			}
		})
		quyuLists.value = "A,B,C,D,F".split(',')
		cheweizhuangtaiLists.value = "已使用,空闲".split(',')
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
		if(form.value.cheweitupian!=null) {
			form.value.cheweitupian = form.value.cheweitupian.replace(new RegExp(context?.$config.url,"g"),"");
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
