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
					<el-form-item label="车位编号" prop="cheweibianhao">
						<el-input class="list_inp" v-model="form.cheweibianhao" placeholder="车位编号"
							 type="text" 							:readonly="!isAdd||disabledForm.cheweibianhao?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="车位位置" prop="cheweiweizhi">
						<el-input class="list_inp" v-model="form.cheweiweizhi" placeholder="车位位置"
							 type="text" 							:readonly="!isAdd||disabledForm.cheweiweizhi?true:false" />
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
								v-for="slot in timeSlots"
								:key="slot.value"
								:label="slot.label"
								:value="slot.value"
								:disabled="slot.disabled">
							</el-option>
						</el-select>
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
					<el-form-item label="车牌号" prop="chepaihao">
						<el-input class="list_inp" v-model="form.chepaihao" placeholder="车牌号"
							 type="text" 							:readonly="!isAdd||disabledForm.chepaihao?true:false" />
					</el-form-item>
				</el-col>

			</el-row>
			<div class="formModel_btn_box">
				<el-button class="formModel_cancel" @click="backClick">取消</el-button>
				<el-button class="formModel_confirm"
                           @click="save"
                           type="success"
				>
                    提交
				</el-button>
				<div class="deposit_tip">
					<i class="el-icon-info-filled"></i>
					预约将扣除50元定金，到店后可核销退费
				</div>
                <payForm ref="pay" @close="payClose"></payForm>
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
	const tableName = 'cheweiyuyue'
	const formName = '车位预约'
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
		cheweibianhao: '',
		cheweiweizhi: '',
		yuyueshijian: '',
		xiaoshijiage: 0,
		ruchangzhuangtai: '未入场',
		zhanghao: '',
		mingcheng: '',
		chepaihao: '',
	})
	
	// 预约日期和时段分离
	const yuyueriqi = ref('')
	const yuyueshiduan = ref('')
	
	// 时段选项（8:00-20:00，按小时）
	const timeSlots = ref([
		{ label: '08:00-09:00', value: '08', disabled: false },
		{ label: '09:00-10:00', value: '09', disabled: false },
		{ label: '10:00-11:00', value: '10', disabled: false },
		{ label: '11:00-12:00', value: '11', disabled: false },
		{ label: '12:00-13:00', value: '12', disabled: false },
		{ label: '13:00-14:00', value: '13', disabled: false },
		{ label: '14:00-15:00', value: '14', disabled: false },
		{ label: '15:00-16:00', value: '15', disabled: false },
		{ label: '16:00-17:00', value: '16', disabled: false },
		{ label: '17:00-18:00', value: '17', disabled: false },
		{ label: '18:00-19:00', value: '18', disabled: false },
		{ label: '19:00-20:00', value: '19', disabled: false }
	])
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
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
	const isAdd = ref(false)
	//表单验证
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
	// 限制预约时间只能选择今天和以后的日期
	const yuyueshijianDisabledDate = (date) => {
		return date.getTime() < Date.now() - 8.64e7;
	}
	//入场状态列表
	const ruchangzhuangtaiLists = ref([])
	
	// 查询已预约的时段
	const checkBookedTimeSlots = async () => {
		if (!form.value.cheweibianhao || !yuyueriqi.value) {
			// 重置所有时段为可用
			timeSlots.value.forEach(slot => {
				slot.disabled = false
				slot.label = slot.label.replace(' (已被预约)', '')
			})
			return
		}
		
		try {
			const res = await context?.$http({
				url: 'cheweiyuyue/getBookedTimeSlots',
				method: 'get',
				params: {
					cheweibianhao: form.value.cheweibianhao,
					yuyueriqi: yuyueriqi.value
				}
			})
			
			if (res && res.data && res.data.bookedTimeSlots) {
				const bookedSlots = res.data.bookedTimeSlots
				timeSlots.value.forEach(slot => {
					const baseLabel = slot.label.replace(' (已被预约)', '')
					if (bookedSlots.includes(slot.value)) {
						slot.disabled = true
						slot.label = baseLabel + ' (已被预约)'
					} else {
						slot.disabled = false
						slot.label = baseLabel
					}
				})
			}
		} catch (e) {
			console.error('查询已预约时段失败', e)
			// 出错时重置所有时段为可用
			timeSlots.value.forEach(slot => {
				slot.disabled = false
				slot.label = slot.label.replace(' (已被预约)', '')
			})
		}
	}
	
	// 监听预约日期变化
	watch(yuyueriqi, () => {
		if (yuyueriqi.value) {
			checkBookedTimeSlots()
		}
	})
	
	// 监听车位编号变化
	watch(() => form.value.cheweibianhao, () => {
		if (form.value.cheweibianhao && yuyueriqi.value) {
			checkBookedTimeSlots()
		}
	})
	
	// 监听日期和时段变化，合并为完整时间
	watch([yuyueriqi, yuyueshiduan], ([date, hour]) => {
		if (date && hour) {
			form.value.yuyueshijian = `${date} ${hour}:00:00`
		}
	})
	
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
			
			// 编辑时，将完整时间拆分为日期和时段
			if (form.value.yuyueshijian) {
				const timeParts = form.value.yuyueshijian.split(' ')
				if (timeParts.length >= 2) {
					yuyueriqi.value = timeParts[0]
					const hourParts = timeParts[1].split(':')
					if (hourParts.length >= 1) {
						yuyueshiduan.value = hourParts[0]
					}
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
		}
		context?.$http({
			url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
			if(context?.$toolUtil.storageGet("frontRole")!="管理员") {
				disabledForm.value.ruchangzhuangtai = true;
			}
			if(json.hasOwnProperty('zhanghao') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
				form.value.zhanghao = json.zhanghao
				disabledForm.value.zhanghao = true;
			}
			if(json.hasOwnProperty('mingcheng') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
				form.value.mingcheng = json.mingcheng
				disabledForm.value.mingcheng = true;
			}
			if(json.hasOwnProperty('chepaihao') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
				form.value.chepaihao = json.chepaihao
				disabledForm.value.chepaihao = true;
			}
            if (localStorage.getItem('autoSave')) {
                localStorage.removeItem('autoSave')
                save()
            }
		})
		ruchangzhuangtaiLists.value = "已入场,未入场".split(',')
	}
	//初始化
	//取消
	const backClick = () => {
		history.back()
	}
	import payForm from '@/components/payForm'
	const pay = ref(null)
	
	// 跨表相关变量
	let crossUserId = ''
	let crossRefId = ''
	let crossOptNum = ''
	
	//提交
	const save=()=>{
		// 检查日期和时段是否选择
		if(!yuyueriqi.value) {
			context?.$toolUtil.message('请选择预约日期', 'error')
			return
		}
		if(!yuyueshiduan.value) {
			context?.$toolUtil.message('请选择预约时段', 'error')
			return
		}
		
		var table = crossTable.value
		var objcross = JSON.parse(JSON.stringify(crossRow.value))
		crossUserId = ''
		crossRefId = ''
		crossOptNum = ''
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
		
		// 如果是新增预约，调用支付流程
		if(!form.value.id) {
			showPayDialog()
			return
		}
		
		// 更新操作
		formRef.value.validate((valid)=>{
			if(valid){
				context?.$http({
					url: `${tableName}/update`,
					method: 'post', 
					data: form.value 
				}).then(res=>{
					context?.$toolUtil.message(`操作成功`,'success')
					history.back()
				})
			}
		})
	}
	
	// 显示支付弹窗
	const showPayDialog = () => {
		formRef.value.validate((valid) => {
			if (valid) {
				if (crossUserId && crossRefId) {
					form.value.crossuserid = crossUserId
					form.value.crossrefid = crossRefId
					let params = {
						page: 1,
						limit: 1000,
						crossuserid: form.value.crossuserid,
						crossrefid: form.value.crossrefid,
					}
					context?.$http({
						url: `${tableName}/page`,
						method: 'get',
						params: params
					}).then(res => {
						if (res.data.data.total >= crossOptNum) {
							context?.$toolUtil.message(`${crossTips.value}`, 'error')
							return false
						} else {
							pay.value.payClick(tableName, form.value, 50)
						}
					})
				} else {
					pay.value.payClick(tableName, form.value, 50)
				}
			}
		})
	}
	
	// 支付成功回调
	const payClose = (e) => {
		submitForm()
	}
	
	// 提交表单
	const submitForm = () => {
		form.value.zhifuzhuangtai = '已支付'
		context?.$http({
			url: `${tableName}/add`,
			method: 'post',
			data: form.value
		}).then(res => {
			context?.$toolUtil.message(`预约成功`, 'success', () => {
				history.back()
			})
		}).catch(err => {
			console.error('预约失败', err)
			if (err && err.msg) {
				context?.$toolUtil.message(err.msg, 'error')
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

.edit_view .add_form .el-form-item .el-form-item__content .list_date{
    line-height: 36px;
    border: 1px solid rgb(221, 221, 221);
    box-sizing: border-box;
    width: 100%;
    border-radius: 0px;
    background: rgb(255, 255, 255);
    font-size: 16px;
}








</style>