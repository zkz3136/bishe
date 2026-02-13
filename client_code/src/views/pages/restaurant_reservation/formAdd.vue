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
					<el-form-item label="餐桌名称" prop="seat_name">
						<el-input class="list_inp" v-model="form.seat_name" placeholder="餐桌名称"
							 type="text" 							:readonly="!isAdd||disabledForm.seat_name?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="24">
					<el-form-item label="封面" prop="cover_image">
						<uploads
							:disabled="true"
							action="file/upload" 
							tip="封面由餐桌信息自动带出"
							style="width: 100%;text-align: left;"
							:fileUrls="form.cover_image?form.cover_image:''" 
							@change="cover_imageUploadSuccess">
						</uploads>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="餐桌位置" prop="table_location">
						<el-input class="list_inp" v-model="form.table_location" placeholder="餐桌位置"
							 type="text" 							:readonly="!isAdd||disabledForm.table_location?true:false" />
					</el-form-item>
				</el-col>

			<el-col :span="12">
				<el-form-item label="可坐人数" prop="capacity">
					<el-input class="list_inp" v-model.number="form.capacity" placeholder="可坐人数"
						 type="text" 							:readonly="!isAdd||disabledForm.capacity?true:false" />
				</el-form-item>
			</el-col>

			<el-col :span="12">
					<el-form-item label="账号" prop="login_name">
						<el-input class="list_inp" v-model="form.login_name" placeholder="账号"
							 type="text" 							:readonly="!isAdd||disabledForm.login_name?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="名称" prop="name">
						<el-input class="list_inp" v-model="form.name" placeholder="名称"
							 type="text" 							:readonly="!isAdd||disabledForm.name?true:false" />
					</el-form-item>
				</el-col>

			<el-col :span="12">
				<el-form-item label="预约日期">
					<el-date-picker
						class="list_date"
						v-model="reservationDate"
						format="YYYY-MM-DD"
						value-format="YYYY-MM-DD"
						type="date"
						:disabled-date="reservation_timeDisabledDate"
						style="width:100%;"
						:readonly="!isAdd||disabledForm.reservation_time?true:false"
						placeholder="请选择预约日期" />
				</el-form-item>
			</el-col>
			<el-col :span="12">
				<el-form-item label="预约时段">
					<el-select
						class="list_sel"
						v-model="reservationSlotHour"
						style="width:100%;"
						:disabled="!isAdd||disabledForm.reservation_time?true:false"
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
	const tableName = 'restaurant_reservation'
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
	const reservationDate = ref('')
	const reservationSlotHour = ref('')
	const allowedSlotHours = ref([])
	const allowedSlotHoursLoaded = ref(false)
	
	// 已被预约的时段列表
	const bookedTimeSlots = ref([])
	let seatInfoTimer = null

	const fetchAllowedSlotHours = (date) => {
		return context?.$http({
			url: `${tableName}/time_slots`,
			method: 'get',
			params: date ? { date } : {}
		}).then(res => {
			const data = res?.data?.data
			if (Array.isArray(data)) {
				allowedSlotHours.value = data.map(v => String(v)).filter(v => v !== '')
				allowedSlotHoursLoaded.value = true
				return
			}
			allowedSlotHoursLoaded.value = false
			allowedSlotHours.value = ['09','10','11','12','13','14','15','16','17','18','19']
		}).catch(() => {
			allowedSlotHoursLoaded.value = false
			allowedSlotHours.value = ['09','10','11','12','13','14','15','16','17','18','19']
		})
	}
	
	// 查询已被预约的时段
	const checkBookedTimeSlots = (date, tableNameParam) => {
		context?.$http({
			url: `${tableName}/list`,
			method: 'get',
			params: {
				page: 1,
				limit: 10000,
				seat_name: tableNameParam
			}
		}).then(res => {
			bookedTimeSlots.value = []
			if (res.data && res.data.data && res.data.data.list) {
				res.data.data.list.forEach(item => {
					if (item?.verification_status === '已核销' || item?.payment_status === '已退款') {
						return
					}
					if (item.reservation_time && item.reservation_time.startsWith(date)) {
						// 提取时段（小时）
						const timeStr = item.reservation_time.split(' ')[1]
						if (timeStr) {
							const hour = timeStr.split(':')[0]
							bookedTimeSlots.value.push(hour)
						}
					}
				})
			}
		}).catch(err => {
			console.error('查询已预约时段失败:', err)
			// 即使查询失败也不影响继续操作
			bookedTimeSlots.value = []
		})
	}
	
	// 判断时段是否已被预约
	const isTimeSlotBooked = (hour) => {
		const result = bookedTimeSlots.value.includes(hour)
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
		const list = allowedSlotHoursLoaded.value
			? (allowedSlotHours.value || [])
			: ['09','10','11','12','13','14','15','16','17','18','19']
		return list.map(hour => {
			const startLabel = String(hour).padStart(2, '0')
			const label = `${startLabel}:00`
			const booked = isTimeSlotBooked(hour)
			return {
				label: booked ? `${label}（已被预约）` : label,
				value: hour,
				disabled: booked
			}
		})
	})
	
	//form表单
	const form = ref({
		seat_name: '',
		cover_image: '',
		table_location: '',
		capacity: 0,
		storeupNumber: 0,
		login_name: '',
		name: '',
		reservation_time: '',
	})
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	const disabledForm = ref({
		seat_name : false,
		cover_image : true,
		table_location : true,
		capacity : true,
		storeupNumber : false,
		login_name : false,
		name : false,
		reservation_time : false,
	})
	const isAdd = ref(false)
	
	// 监听日期和时段变化，组合成完整的预约时间
	watch([reservationDate, reservationSlotHour], ([date, hour]) => {
		if (date && hour) {
			form.value.reservation_time = `${date} ${hour}:00:00`
		}
	})
	
	// 监听日期变化，查询已被预约的时段
	watch(reservationDate, (newDate) => {
		if (newDate && form.value.seat_name) {
			checkBookedTimeSlots(newDate, form.value.seat_name)
		}
		fetchAllowedSlotHours(newDate).then(() => {
			if (reservationSlotHour.value && allowedSlotHours.value && allowedSlotHours.value.length) {
				if (!allowedSlotHours.value.includes(reservationSlotHour.value)) {
					reservationSlotHour.value = ''
				}
			}
		})
	})
	
	// 监听餐桌名称变化，如果有日期也查询已预约时段
	watch(() => form.value.seat_name, (newTableName) => {
		const v = newTableName ? String(newTableName).trim() : ''
		if (reservationDate.value && v) {
			checkBookedTimeSlots(reservationDate.value, newTableName)
		}
		if (!v) {
			form.value.cover_image = ''
			form.value.table_location = ''
			form.value.capacity = 0
			return
		}
		clearTimeout(seatInfoTimer)
		seatInfoTimer = setTimeout(() => {
			context?.$http({
				url: `restaurant_info/list`,
				method: 'get',
				params: {
					page: 1,
					limit: 1,
					seat_name: v
				}
			}).then(res => {
				const first = res?.data?.data?.list?.[0]
				if (!first) return
				form.value.cover_image = first.cover_image ?? first.coverImage ?? form.value.cover_image
				form.value.table_location = first.table_location ?? first.tableLocation ?? form.value.table_location
				if (first.capacity !== undefined && first.capacity !== null && first.capacity !== '') {
					form.value.capacity = first.capacity
				}
			}).catch(() => {})
		}, 250)
	})
	
	//表单验证
	const rules = ref({
		seat_name: [
			{ required: true, message: '餐桌名称不能为空', trigger: ['blur', 'change'] }
		],
		cover_image: [
		],
		table_location: [
		],
		capacity: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		storeupNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		login_name: [
			{ required: true, message: '账号不能为空', trigger: 'blur' }
		],
		name: [
			{ required: true, message: '名称不能为空', trigger: 'blur' }
		],
		reservation_time: [
		],
	})
	//封面上传回调
	const cover_imageUploadSuccess=(e)=>{
		form.value.cover_image = e
	}
	// 限制预约时间只能选择今天和以后的日期
	const reservation_timeDisabledDate = (date) => {
		const maxDaysAhead = 30
		const today = new Date()
		today.setHours(0, 0, 0, 0)
		const maxDate = new Date(today)
		maxDate.setDate(maxDate.getDate() + maxDaysAhead)
		return date.getTime() < today.getTime() || date.getTime() > maxDate.getTime()
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
			if (form.value.reservation_time) {
				const dateTime = form.value.reservation_time.split(' ')
				if (dateTime.length >= 2) {
					reservationDate.value = dateTime[0]
					reservationSlotHour.value = dateTime[1].split(':')[0]
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
		fetchAllowedSlotHours(reservationDate.value)
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
			const seatName = row?.seat_name ?? row?.seatName
			if(seatName) {
				form.value.seat_name = seatName
				disabledForm.value.seat_name = true
			}
			const coverImage = row?.cover_image ?? row?.coverImage
			if(coverImage) {
				form.value.cover_image = coverImage
				disabledForm.value.cover_image = true
			}
			const tableLocation = row?.table_location ?? row?.tableLocation
			if(tableLocation) {
				form.value.table_location = tableLocation
				disabledForm.value.table_location = true
			}
			const capacity = row?.capacity
			if(capacity !== undefined && capacity !== null && capacity !== '') {
				form.value.capacity = capacity
				disabledForm.value.capacity = true
			}
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
				if(x=='storeupNumber'){
					form.value.storeupNumber = row[x];
					disabledForm.value.storeupNumber = true;
					continue;
				}
				if(x=='reservation_time'){
					form.value.reservation_time = row[x];
					disabledForm.value.reservation_time = true;
					continue;
				}
			}
			if(formId){
				context?.$http({
					url: `restaurant_info/detail/${formId}`,
					method: 'get'
				}).then(res => {
					const d = res?.data?.data || {}
					if(d.seat_name && String(d.seat_name).trim() !== ''){
						form.value.seat_name = d.seat_name
						disabledForm.value.seat_name = true
					}
					const coverImageFromDetail = d.cover_image ?? d.coverImage
					if(coverImageFromDetail){
						form.value.cover_image = coverImageFromDetail
						disabledForm.value.cover_image = true
					}
					if(d.table_location){
						form.value.table_location = d.table_location
						disabledForm.value.table_location = true
					}
					if(d.capacity){
						form.value.capacity = d.capacity
						disabledForm.value.capacity = true
					}
				})
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
		if(json.hasOwnProperty('login_name') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
			form.value.login_name = json.login_name
			disabledForm.value.login_name = true;
		}
		if(json.hasOwnProperty('name') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
			form.value.name = json.name
			disabledForm.value.name = true;
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
		// 设置定金金额
		form.value.deposit = 50
		form.value.payment_status = '未支付'
		if (payRef.value) {
		payRef.value.payClick('restaurant_reservation', form.value)
		} else {
			console.error('支付组件未加载')
		}
	}
	const paySuccess = () => {
		// 支付成功后设置支付状态
		form.value.payment_status = '已支付'
		// 提交表单
		submitForm()
	}
	const isSubmitting = ref(false)
	//提交
	const save=()=>{
		if(form.value.cover_image!=null) {
			form.value.cover_image = form.value.cover_image.replace(new RegExp(context?.$config.url,"g"),"");
		}
		if(!form.value.seat_name || String(form.value.seat_name).trim() === '') {
			context?.$toolUtil.message('餐桌名称不能为空', 'error')
			return
		}
		
		// 检查日期和时段是否选择（只在点击保存时检查）
		if(!reservationDate.value) {
			context?.$toolUtil.message('请选择预约日期', 'error')
			return
		}
		if(!reservationSlotHour.value) {
			context?.$toolUtil.message('请选择预约时段', 'error')
			return
		}
		form.value.reservation_time = `${reservationDate.value} ${reservationSlotHour.value}:00:00`
		
		// 检查是否选择了已被预约的时段
		// 暂时注释掉，先确保基本功能正常
		/*
		if(isTimeSlotBooked(reservationSlotHour.value)) {
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
					crossUserId = context?.$toolUtil.storageGet('user_id')
					crossRefId = objcross['id']
					crossOptNum = crossColumnName.value.replace(/\[/,"").replace(/\]/,"")
				}
			}
		}
		
		// 新增预约时需要支付定金
		if(!form.value.id) {
			formRef.value.validate((valid)=>{
				if(valid){
					showPayDialog()
				}
			})
			return
		}
		
		// 修改时才需要表单验证
		formRef.value.validate((valid)=>{
			if(valid){
				submitForm()
			}
		})
	}
	// 实际提交表单的方法
	const submitForm=()=>{
		if (isSubmitting.value) return
		isSubmitting.value = true
		const seatName = form.value?.seat_name ? String(form.value.seat_name).trim() : ''
		if(!seatName){
			context?.$toolUtil.message('餐桌名称不能为空', 'error')
			isSubmitting.value = false
			return
		}
		if(reservationDate.value && reservationSlotHour.value) {
			form.value.reservation_time = `${reservationDate.value} ${reservationSlotHour.value}:00:00`
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
					isSubmitting.value = false
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
						const msg = err?.response?.data?.msg || err?.message || '预约失败，请稍后再试'
						context?.$toolUtil.message(msg, 'error')
					}).finally(() => {
						isSubmitting.value = false
					})
				}
			}).catch(() => {
				isSubmitting.value = false
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
				const msg = err?.response?.data?.msg || err?.message || '预约失败，请稍后再试'
				context?.$toolUtil.message(msg, 'error')
			}).finally(() => {
				isSubmitting.value = false
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
