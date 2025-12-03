<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="菜品名称" prop="caipinmingcheng">
							<el-input class="list_inp" v-model="form.caipinmingcheng" placeholder="菜品名称"
                                type="text"
								:readonly="!isAdd||disabledForm.caipinmingcheng?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item prop="caipintupian"
									  label="菜品图片"
						>
							<uploads
								:disabled="!isAdd||disabledForm.caipintupian?true:false"
								action="file/upload"
								tip="请上传菜品图片"
								style="width: 100%;text-align: left;"
								:fileUrls="form.caipintupian?form.caipintupian:''" 
								@change="caipintupianUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="菜品类型" prop="caipinleixing">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.caipinleixing?true:false"
								v-model="form.caipinleixing" 
								placeholder="请选择菜品类型"
								>
								<el-option v-for="(item,index) in caipinleixingLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="口味" prop="kouwei">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.kouwei?true:false"
								v-model="form.kouwei" 
								placeholder="请选择口味"
								>
								<el-option v-for="(item,index) in kouweiLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="推荐指数" prop="tuijianzhishu">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.tuijianzhishu?true:false"
								v-model="form.tuijianzhishu" 
								placeholder="请选择推荐指数"
								>
								<el-option v-for="(item,index) in tuijianzhishuLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="单限" prop="onelimittimes">
							<el-input class="list_inp" v-model.number="form.onelimittimes" placeholder="单限"
                                type="text"
								:readonly="!isAdd||disabledForm.onelimittimes?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="库存" prop="alllimittimes">
							<el-input class="list_inp" v-model.number="form.alllimittimes" placeholder="库存"
                                type="text"
								:readonly="!isAdd||disabledForm.alllimittimes?true:false" />
						</el-form-item>
					</el-col>

				<el-col :span="24">
					<el-form-item label="价格" prop="price">
						<el-input class="list_inp" v-model.number="form.price" placeholder="价格"
                            type="number"
							:readonly="!isAdd||disabledForm.price?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="24">
						<el-form-item label="菜品详情" prop="caipinxiangqing">
							<editor :value="form.caipinxiangqing" placeholder="请输入菜品详情" :readonly="!isAdd||disabledForm.caipinxiangqing?true:false"
								class="list_editor" @change="(e)=>editorChange(e,'caipinxiangqing')"></editor>
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
	const tableName = 'meishixinxi'
	const formName = '美食信息'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        caipinmingcheng : false,
        caipintupian : false,
        caipinleixing : false,
        caipinxiangqing : false,
        kouwei : false,
        tuijianzhishu : false,
        clicktime : false,
        onelimittimes : false,
        alllimittimes : false,
        price : false,
        storeupNumber : false,
        discussNumber : false,
        shangjiazhanghao : false,
        shangjiamingcheng : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		caipinmingcheng: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		caipintupian: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		caipinleixing: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		caipinxiangqing: [
		],
		kouwei: [
		],
		tuijianzhishu: [
		],
		clicktime: [
		],
		onelimittimes: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		alllimittimes: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		price: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		storeupNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		discussNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		shangjiazhanghao: [
		],
		shangjiamingcheng: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//菜品图片上传回调
	const caipintupianUploadSuccess=(e)=>{
		form.value.caipintupian = e
	}
	//菜品类型列表
	const caipinleixingLists = ref([])
	//口味列表
	const kouweiLists = ref([])
	//推荐指数列表
	const tuijianzhishuLists = ref([])

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			caipinmingcheng: '',
			caipintupian: '',
			caipinleixing: '',
			caipinxiangqing: '',
			kouwei: '不辣',
			tuijianzhishu: '',
			clicktime: '',
			shangjiazhanghao: '',
			shangjiamingcheng: '',
		}
	}
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			res.data.data.caipinxiangqing = res.data.data.caipinxiangqing?(res.data.data.caipinxiangqing.replace(reg,'../../../cl200442145/file')):'';
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
				if(x=='caipinmingcheng'){
					form.value.caipinmingcheng = row[x];
					disabledForm.value.caipinmingcheng = true;
					continue;
				}
				if(x=='caipintupian'){
					form.value.caipintupian = row[x];
					disabledForm.value.caipintupian = true;
					continue;
				}
				if(x=='caipinleixing'){
					form.value.caipinleixing = row[x];
					disabledForm.value.caipinleixing = true;
					continue;
				}
				if(x=='caipinxiangqing'){
					form.value.caipinxiangqing = row[x];
					disabledForm.value.caipinxiangqing = true;
					continue;
				}
				if(x=='kouwei'){
					form.value.kouwei = row[x];
					disabledForm.value.kouwei = true;
					continue;
				}
				if(x=='tuijianzhishu'){
					form.value.tuijianzhishu = row[x];
					disabledForm.value.tuijianzhishu = true;
					continue;
				}
				if(x=='clicktime'){
					form.value.clicktime = row[x];
					disabledForm.value.clicktime = true;
					continue;
				}
				if(x=='onelimittimes'){
					form.value.onelimittimes = row[x];
					disabledForm.value.onelimittimes = true;
					continue;
				}
				if(x=='alllimittimes'){
					form.value.alllimittimes = row[x];
					disabledForm.value.alllimittimes = true;
					continue;
				}
				if(x=='price'){
					form.value.price = row[x];
					disabledForm.value.price = true;
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
				if(x=='shangjiazhanghao'){
					form.value.shangjiazhanghao = row[x];
					disabledForm.value.shangjiazhanghao = true;
					continue;
				}
				if(x=='shangjiamingcheng'){
					form.value.shangjiamingcheng = row[x];
					disabledForm.value.shangjiamingcheng = true;
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
			form.value.kouwei='不辣'
			formVisible.value = true
		}

	context?.$http({
		url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
		method: 'get'
	}).then(res => {
		var json = res.data.data
		// 商家字段已废弃，改为单一餐厅系统
	})
		context?.$http({
			url: `option/caipinleixing/caipinleixing`,
			method: 'get'
		}).then(res=>{
			caipinleixingLists.value = res.data.data
		})
		kouweiLists.value = "微辣,超辣,不辣".split(',')
		tuijianzhishuLists.value = "★,★★,★★★,★★★★,★★★★★".split(',')
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
		if(form.value.caipintupian!=null) {
			form.value.caipintupian = form.value.caipintupian.replace
			(new RegExp(context?.$config.url,"g"),"");
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
