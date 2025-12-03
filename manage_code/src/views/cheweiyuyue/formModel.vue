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
						<el-form-item label="车位位置" prop="cheweiweizhi">
							<el-input class="list_inp" v-model="form.cheweiweizhi" placeholder="车位位置"
                                type="text"
								:readonly="!isAdd||disabledForm.cheweiweizhi?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="预约时间" prop="yuyueshijian">
							<el-date-picker
								class="list_date"
								v-model="form.yuyueshijian"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:disabled-date="yuyueshijianDisabledDate"
								:readonly="!isAdd||disabledForm.yuyueshijian?true:false"
								placeholder="请选择预约时间" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="账号" prop="zhanghao">
							<el-input class="list_inp" v-model="form.zhanghao" placeholder="账号"
                                type="text"
								:readonly="!isAdd||disabledForm.zhanghao?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="名称" prop="mingcheng">
							<el-input class="list_inp" v-model="form.mingcheng" placeholder="名称"
                                type="text"
								:readonly="!isAdd||disabledForm.mingcheng?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="车牌号" prop="chepaihao">
							<el-input class="list_inp" v-model="form.chepaihao" placeholder="车牌号"
                                type="text"
								:readonly="!isAdd||disabledForm.chepaihao?true:false" />
						</el-form-item>
					</el-col>

				</el-row>
			</el-form>
			<template #footer>
				<span class="formModel_btn_box">
					<el-button class="cancel_btn" @click="closeClick">取消</el-button>
					<el-button class="confirm_btn" type="primary" @click="save" v-if="isAdd||type=='info'||type=='logistics'||type=='reply'"
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
	const tableName = 'cheweiyuyue'
	const formName = '车位预约'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        cheweibianhao : false,
        cheweiweizhi : false,
        yuyueshijian : false,
        xiaoshijiage : false,
        ruchangzhuangtai : false,
        zhanghao : false,
        mingcheng : false,
        chepaihao : false,
        ispay : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		cheweibianhao: [
		],
		cheweiweizhi: [
		],
		yuyueshijian: [
		],
		xiaoshijiage: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		ruchangzhuangtai: [
		],
		zhanghao: [
		],
		mingcheng: [
		],
		chepaihao: [
		],
		ispay: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	// 限制预约时间只能选择今天和以后的日期
	const yuyueshijianDisabledDate = (date) => {
		return date.getTime() < Date.now() - 8.64e7;
	}
	//入场状态列表
	const ruchangzhuangtaiLists = ref([])

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			cheweibianhao: '',
			cheweiweizhi: '',
			yuyueshijian: '',
			xiaoshijiage: '',
			ruchangzhuangtai: '未入场',
			zhanghao: '',
			mingcheng: '',
			chepaihao: '',
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
				if(x=='cheweiweizhi'){
					form.value.cheweiweizhi = row[x];
					disabledForm.value.cheweiweizhi = true;
					continue;
				}
				if(x=='yuyueshijian'){
					form.value.yuyueshijian = row[x];
					disabledForm.value.yuyueshijian = true;
					continue;
				}
				if(x=='xiaoshijiage'){
					form.value.xiaoshijiage = row[x];
					disabledForm.value.xiaoshijiage = true;
					continue;
				}
				if(x=='ruchangzhuangtai'){
					form.value.ruchangzhuangtai = row[x];
					disabledForm.value.ruchangzhuangtai = true;
					continue;
				}
				if(x=='zhanghao'){
					form.value.zhanghao = row[x];
					disabledForm.value.zhanghao = true;
					continue;
				}
				if(x=='mingcheng'){
					form.value.mingcheng = row[x];
					disabledForm.value.mingcheng = true;
					continue;
				}
				if(x=='chepaihao'){
					form.value.chepaihao = row[x];
					disabledForm.value.chepaihao = true;
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
			form.value.ruchangzhuangtai='未入场'
			formVisible.value = true
		}

		context?.$http({
			url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
			if(context?.$toolUtil.storageGet("role")!="管理员" && !context?.$toolUtil.storageGet("isAdmin")) {
				disabledForm.value.ruchangzhuangtai = true;
			}
			if(json.hasOwnProperty('zhanghao')&& context?.$toolUtil.storageGet("role")!="管理员"){
				form.value.zhanghao = json.zhanghao
				disabledForm.value.zhanghao = true;
			}
			if(json.hasOwnProperty('mingcheng')&& context?.$toolUtil.storageGet("role")!="管理员"){
				form.value.mingcheng = json.mingcheng
				disabledForm.value.mingcheng = true;
			}
			if(json.hasOwnProperty('chepaihao')&& context?.$toolUtil.storageGet("role")!="管理员"){
				form.value.chepaihao = json.chepaihao
				disabledForm.value.chepaihao = true;
			}
		})
		ruchangzhuangtaiLists.value = "已入场,未入场".split(',')
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
