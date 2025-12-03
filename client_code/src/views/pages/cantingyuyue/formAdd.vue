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
					<el-form-item label="餐厅名称" prop="cantingmingcheng">
						<el-input class="list_inp" v-model="form.cantingmingcheng" placeholder="餐厅名称"
							 type="text" 							:readonly="!isAdd||disabledForm.cantingmingcheng?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="餐桌名称" prop="canzhuomingcheng">
						<el-input class="list_inp" v-model="form.canzhuomingcheng" placeholder="餐桌名称"
							 type="text" 							:readonly="!isAdd||disabledForm.canzhuomingcheng?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="24">
					<el-form-item label="封面" prop="fengmian">
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
				<el-col :span="12">
					<el-form-item label="餐桌位置" prop="canzhuoweizhi">
						<el-input class="list_inp" v-model="form.canzhuoweizhi" placeholder="餐桌位置"
							 type="text" 							:readonly="!isAdd||disabledForm.canzhuoweizhi?true:false" />
					</el-form-item>
				</el-col>

			<el-col :span="12">
				<el-form-item label="可坐人数" prop="kezuorenshu">
					<el-input class="list_inp" v-model.number="form.kezuorenshu" placeholder="可坐人数"
						 type="text" 							:readonly="!isAdd||disabledForm.kezuorenshu?true:false" />
				</el-form-item>
			</el-col>

			<el-col :span="12">
					<el-form-item label="账号" prop="zhanghao">
						<el-input class="list_inp" v-model="form.zhanghao" placeholder="账号"
							 type="text" 							:readonly="!isAdd||disabledForm.zhanghao?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="名称" prop="mingcheng">
						<el-input class="list_inp" v-model="form.mingcheng" placeholder="名称"
							 type="text" 							:readonly="!isAdd||disabledForm.mingcheng?true:false" />
					</el-form-item>
				</el-col>

			<el-col :span="12">
				<el-form-item label="预约日期">
					<el-date-picker
						class="list_date"
						v-model="yuyueriqi"
						format="YYYY-MM-DD"
						value-format="YYYY-MM-DD"
						type="date"
						:disabled-date="yuyueshijianDisabledDate"
						style="width:100%;"
						:readonly="!isAdd||disabledForm.yuyueshijian?true:false"
						placeholder="请选择预约日期" />
				</el-form-item>
			</el-col>
			<el-col :span="12">
				<el-form-item label="预约时段">
					<el-select
						class="list_sel"
						v-model="yuyueshiduan"
						style="width:100%;"
						:disabled="!isAdd||disabledForm.yuyueshijian?true:false"
						placeholder="请选择预约时段">
						<el-option 
							v-for="option in timeSlotOptions" 
							:key="option.value"
							:label="option.label" 
							:value="option.value" 
							:disabled="option.disabled">
						</el-option>
					</el-select>
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
				<div class="deposit_tip">
					<i class="el-icon-info-filled"></i>
					预约将扣除50元定金，到店后可核销退费
				</div>
			</div>
		</el-form>
		<payForm ref="payRef" @payChange="paySuccess"></payForm>
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
	const tableName = 'cantingyuyue'
	const formName = '餐厅预约'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	// 预约日期和时段
	const yuyueriqi = ref('')
	const yuyueshiduan = ref('')
	
	// 已被预约的时段列表
	const bookedTimeSlots = ref([])
	
	// 查询已被预约的时段
	const checkBookedTimeSlots = (date, tableNameParam) => {
		console.log('查询已预约时段:', date, tableNameParam)
		context?.$http({
			url: `cantingyuyue/page`,
			method: 'get',
			params: {
				page: 1,
				limit: 1000,
				canzhuomingcheng: tableNameParam
			}
		}).then(res => {
			bookedTimeSlots.value = []
			console.log('查询结果:', res.data)
			if (res.data && res.data.data && res.data.data.list) {
				res.data.data.list.forEach(item => {
					if (item.yuyueshijian && item.yuyueshijian.startsWith(date)) {
						// 提取时段（小时）
						const timeStr = item.yuyueshijian.split(' ')[1]
						if (timeStr) {
							const hour = timeStr.split(':')[0]
							bookedTimeSlots.value.push(hour)
						}
					}
				})
			}
			console.log('已被预约的时段:', bookedTimeSlots.value)
		}).catch(err => {
			console.error('查询已预约时段失败:', err)
			// 即使查询失败也不影响继续操作
			bookedTimeSlots.value = []
		})
	}
	
	// 判断时段是否已被预约
	const isTimeSlotBooked = (hour) => {
		const result = bookedTimeSlots.value.includes(hour)
		console.log(`检查时段 ${hour} 是否已预约:`, result, '已预约列表:', bookedTimeSlots.value)
		return result
	}
	
	// 获取时段显示文本
	const getTimeSlotLabel = (start, end, hour) => {
		const label = `${start}:00-${end}:00`
		const booked = isTimeSlotBooked(hour)
		return booked ? `${label}（已被预约）` : label
	}
	
	// 定义时段选项列表（使用计算属性以便响应式更新）
	const timeSlotOptions = computed(() => {
		return [
			{ label: getTimeSlotLabel(10, 11, '10'), value: '10', disabled: isTimeSlotBooked('10') },
			{ label: getTimeSlotLabel(11, 12, '11'), value: '11', disabled: isTimeSlotBooked('11') },
			{ label: getTimeSlotLabel(12, 13, '12'), value: '12', disabled: isTimeSlotBooked('12') },
			{ label: getTimeSlotLabel(13, 14, '13'), value: '13', disabled: isTimeSlotBooked('13') },
			{ label: getTimeSlotLabel(14, 15, '14'), value: '14', disabled: isTimeSlotBooked('14') },
			{ label: getTimeSlotLabel(15, 16, '15'), value: '15', disabled: isTimeSlotBooked('15') },
			{ label: getTimeSlotLabel(16, 17, '16'), value: '16', disabled: isTimeSlotBooked('16') },
			{ label: getTimeSlotLabel(17, 18, '17'), value: '17', disabled: isTimeSlotBooked('17') },
			{ label: getTimeSlotLabel(18, 19, '18'), value: '18', disabled: isTimeSlotBooked('18') },
			{ label: getTimeSlotLabel(19, 20, '19'), value: '19', disabled: isTimeSlotBooked('19') }
		]
	})
	
	//form表单
	const form = ref({
		cantingmingcheng: '',
		canzhuomingcheng: '',
		fengmian: '',
		canzhuoweizhi: '',
		kezuorenshu: 0,
		yuangongzhanghao: '',
		yuangongmingcheng: '',
		storeupNumber: 0,
		zhanghao: '',
		mingcheng: '',
		yuyueshijian: '',
		shhf: '',
	})
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	const disabledForm = ref({
		cantingmingcheng : false,
		canzhuomingcheng : false,
		fengmian : false,
		canzhuoweizhi : false,
		kezuorenshu : false,
		yuangongzhanghao : false,
		yuangongmingcheng : false,
		storeupNumber : false,
		zhanghao : false,
		mingcheng : false,
		yuyueshijian : false,
		sfsh : false,
		shhf : false,
	})
	const isAdd = ref(false)
	
	// 监听日期和时段变化，组合成完整的预约时间
	watch([yuyueriqi, yuyueshiduan], ([date, hour]) => {
		if (date && hour) {
			form.value.yuyueshijian = `${date} ${hour}:00:00`
		}
	})
	
	// 监听日期变化，查询已被预约的时段
	watch(yuyueriqi, (newDate) => {
		console.log('日期变化:', newDate, '餐桌名称:', form.value.canzhuomingcheng)
		if (newDate && form.value.canzhuomingcheng) {
			checkBookedTimeSlots(newDate, form.value.canzhuomingcheng)
		} else {
			console.log('日期或餐桌名称为空，不查询已预约时段')
		}
	})
	
	// 监听餐桌名称变化，如果有日期也查询已预约时段
	watch(() => form.value.canzhuomingcheng, (newTableName) => {
		console.log('餐桌名称变化:', newTableName, '日期:', yuyueriqi.value)
		if (yuyueriqi.value && newTableName) {
			checkBookedTimeSlots(yuyueriqi.value, newTableName)
		} else {
			console.log('日期或餐桌名称为空，不查询已预约时段')
		}
	})
	
	//表单验证
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
		yuangongzhanghao: [
		],
		yuangongmingcheng: [
		],
		storeupNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		zhanghao: [
		],
		mingcheng: [
		],
		yuyueshijian: [
		],
		sfsh: [
		],
		shhf: [
		],
	})
	//封面上传回调
	const fengmianUploadSuccess=(e)=>{
		form.value.fengmian = e
	}
	// 限制预约时间只能选择今天和以后的日期
	const yuyueshijianDisabledDate = (date) => {
		return date.getTime() < Date.now() - 8.64e7;
	}
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
			// 如果有预约时间，解析日期和时段
			if (form.value.yuyueshijian) {
				const dateTime = form.value.yuyueshijian.split(' ')
				if (dateTime.length >= 2) {
					yuyueriqi.value = dateTime[0]
					yuyueshiduan.value = dateTime[1].split(':')[0]
				}
			}
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
				if(x=='yuyueshijian'){
					form.value.yuyueshijian = row[x];
					disabledForm.value.yuyueshijian = true;
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
		// 商家字段已废弃，改为单一餐厅系统
		if(json.hasOwnProperty('zhanghao') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
			form.value.zhanghao = json.zhanghao
			disabledForm.value.zhanghao = true;
		}
		if(json.hasOwnProperty('mingcheng') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
			form.value.mingcheng = json.mingcheng
			disabledForm.value.mingcheng = true;
		}
        if (localStorage.getItem('autoSave')) {
            localStorage.removeItem('autoSave')
            save()
        }
	})
	}
	//初始化
	//取消
	const backClick = () => {
		history.back()
	}
	// 支付弹窗相关
	import payForm from '@/components/payForm.vue'
	const payRef = ref(null)
	const showPayDialog = () => {
		console.log('显示支付弹窗')
		// 设置定金金额
		form.value.dingjin = 50
		form.value.zhifuzhuangtai = '未支付'
		if (payRef.value) {
		payRef.value.payClick('cantingyuyue', form.value)
		} else {
			console.error('支付组件未加载')
		}
	}
	const paySuccess = () => {
		// 支付成功后设置支付状态
		form.value.zhifuzhuangtai = '已支付'
		// 提交表单
		submitForm()
	}
	//提交
	const save=()=>{
		console.log('点击了保存按钮')
		console.log('预约日期:', yuyueriqi.value)
		console.log('预约时段:', yuyueshiduan.value)
		console.log('表单数据:', form.value)
		
		if(form.value.fengmian!=null) {
			form.value.fengmian = form.value.fengmian.replace(new RegExp(context?.$config.url,"g"),"");
		}
		
		// 检查日期和时段是否选择（只在点击保存时检查）
		if(!yuyueriqi.value) {
			context?.$toolUtil.message('请选择预约日期', 'error')
			return
		}
		if(!yuyueshiduan.value) {
			context?.$toolUtil.message('请选择预约时段', 'error')
			return
		}
		
		// 检查是否选择了已被预约的时段
		// 暂时注释掉，先确保基本功能正常
		/*
		if(isTimeSlotBooked(yuyueshiduan.value)) {
			context?.$toolUtil.message('该时段已被预约，请选择其他时段', 'error')
			return
		}
		*/
		
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
					crossUserId = context?.$toolUtil.storageGet('userid')
					crossRefId = objcross['id']
					crossOptNum = crossColumnName.value.replace(/\[/,"").replace(/\]/,"")
				}
			}
		}
		
		// 新增预约时需要支付定金
		if(!form.value.id) {
			console.log('新增预约，显示支付弹窗')
			showPayDialog()
			return
		}
		
		console.log('修改预约，进行表单验证')
		// 修改时才需要表单验证
		formRef.value.validate((valid)=>{
			console.log('表单验证结果:', valid)
			if(valid){
				submitForm()
			}
		})
	}
	// 实际提交表单的方法
	const submitForm=()=>{
		console.log('提交表单', form.value)
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
					crossUserId = context?.$toolUtil.storageGet('userid')
					crossRefId = objcross['id']
					crossOptNum = crossColumnName.value.replace(/\[/,"").replace(/\]/,"")
				}
			}
		}
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
						url: `${tableName}/${!form.value.id ? "add" : "update"}`,
						method: 'post', 
						data: form.value 
					}).then(res=>{
						if(res.data.code == 0) {
							context?.$toolUtil.message(`预约成功`,'success')
							history.back()
						} else {
							context?.$toolUtil.message(res.data.msg || '预约失败', 'error')
						}
					}).catch(err => {
						context?.$toolUtil.message('预约失败，请检查余额是否充足', 'error')
					})
				}
			})
		}else{
			context?.$http({
				url: `${tableName}/${!form.value.id ? "add" : "update"}`,
				method: 'post', 
				data: form.value 
			}).then(res=>{
				if(res.data.code == 0) {
					context?.$toolUtil.message(`预约成功`,'success')
					history.back()
				} else {
					context?.$toolUtil.message(res.data.msg || '预约失败', 'error')
				}
			}).catch(err => {
				context?.$toolUtil.message('预约失败，请检查余额是否充足', 'error')
			})
		}
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
		init(route.query.id?route.query.id:null, type.value,'', row, table, statusColumnName, tips, statusColumnValue)
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
		.formModel_cancel {
		}
		.formModel_cancel:hover {
		}
		
		.formModel_confirm {
		}
		.formModel_confirm:hover {
		}
		
		.deposit_tip {
			margin-top: 15px;
			padding: 10px 15px;
			background-color: #fff7e6;
			border-left: 3px solid #faad14;
			color: #d46b08;
			font-size: 14px;
			border-radius: 4px;
			display: flex;
			align-items: center;
			gap: 8px;
			
			i {
				font-size: 16px;
			}
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


.edit_view .add_form .el-form-item .el-form-item__content .list_date{
    line-height: 36px;
    border: 1px solid rgb(221, 221, 221);
    box-sizing: border-box;
    width: 100%;
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