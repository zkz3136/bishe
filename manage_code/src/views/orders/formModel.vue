<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="订单编号" prop="order_id">
							<el-input class="list_inp" v-model="form.order_id" placeholder="订单编号"
                                type="text"
								:readonly="true" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="商品名称" prop="good_name">
							<el-input class="list_inp" v-model="form.good_name" placeholder="商品名称"
                                type="text"
								:readonly="!isAdd||disabledForm.good_name?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item prop="picture"
									  label="图片"
						>
							<uploads
								:disabled="!isAdd||disabledForm.picture?true:false"
								action="file/upload"
								tip="请上传图片"
								style="width: 100%;text-align: left;"
								:fileUrls="form.picture?form.picture:''" 
								@change="pictureUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="购买数量" prop="buy_number">
							<el-input class="list_inp" v-model.number="form.buy_number" placeholder="购买数量"
                                type="text"
								:readonly="!isAdd||disabledForm.buy_number?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="单价" prop="price">
							<el-input class="list_inp" v-model.number="form.price" placeholder="单价"
                                type="number"
								:readonly="!isAdd||disabledForm.price?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="折扣价" prop="discount_price">
							<el-input class="list_inp" v-model.number="form.discount_price" placeholder="折扣价"
                                type="number"
								:readonly="!isAdd||disabledForm.discount_price?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="总价" prop="total">
							<el-input class="list_inp" v-model.number="form.total" placeholder="总价"
                                type="number"
								:readonly="!isAdd||disabledForm.total?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="折扣总价" prop="discount_total">
							<el-input class="list_inp" v-model.number="form.discount_total" placeholder="折扣总价"
                                type="number"
								:readonly="!isAdd||disabledForm.discount_total?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="餐桌名称" prop="seat_name">
							<el-input class="list_inp" v-model="form.seat_name" placeholder="餐桌名称"
                                type="text"
								:readonly="!isAdd||disabledForm.seat_name?true:false" />
						</el-form-item>
					</el-col>

                    <el-col :span="24">
						<el-form-item label="订单状态" prop="status">
                            <el-select v-model="form.status" placeholder="请选择订单状态" style="width:100%;" :disabled="!isAdd||disabledForm.status?true:false">
                                <el-option label="未支付" value="未支付"></el-option>
                                <el-option label="已支付" value="已支付"></el-option>
                                <el-option label="已完成" value="已完成"></el-option>
                                <el-option label="申请退款" value="申请退款"></el-option>
                                <el-option label="已退款" value="已退款"></el-option>
                                <el-option label="已取消" value="已取消"></el-option>
                            </el-select>
						</el-form-item>
					</el-col>

                    <el-col :span="24">
						<el-form-item label="备注" prop="remark">
							<el-input class="list_inp" v-model="form.remark" placeholder="备注"
                                type="textarea"
								:readonly="!isAdd||disabledForm.remark?true:false" />
						</el-form-item>
					</el-col>

                    <el-col :span="24">
						<el-form-item label="商品类型" prop="good_type">
							<el-input class="list_inp" v-model="form.good_type" placeholder="商品类型"
                                type="text"
								:readonly="!isAdd||disabledForm.good_type?true:false" />
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
	const tableName = 'orders'
	const formName = '订单'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        order_id : false,
        good_name : false,
        picture : false,
        buy_number : false,
        price : false,
        discount_price: false,
        total : false,
        discount_total: false,
        seat_name : false,
        status : false,
        remark : false,
        good_type : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		order_id: [
		],
		good_name: [
		],
		picture: [
		],
		buy_number: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		price: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		discount_price: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		total: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		discount_total: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		seat_name: [
		],
		status: [
		],
		remark: [
		],
        good_type: [
        ],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//图片上传回调
	const pictureUploadSuccess=(e)=>{
		form.value.picture = e
	}

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			order_id: getUUID(),
			good_name: '',
			picture: '',
			buy_number: '',
			price: '',
			discount_price: '',
			total: '',
			discount_total: '',
			seat_name: '',
			status: '未支付',
			remark: '',
            good_type: '',
		}
	}
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			const data = res?.data?.data || {}
			const normalized = { ...data }
			if (normalized.order_id === undefined && normalized.orderid !== undefined) normalized.order_id = normalized.orderid
			if (normalized.good_name === undefined && normalized.goodname !== undefined) normalized.good_name = normalized.goodname
			if (normalized.buy_number === undefined && normalized.buynumber !== undefined) normalized.buy_number = normalized.buynumber
			if (normalized.discount_price === undefined && normalized.discountprice !== undefined) normalized.discount_price = normalized.discountprice
			if (normalized.discount_total === undefined && normalized.discounttotal !== undefined) normalized.discount_total = normalized.discounttotal
			if (normalized.good_type === undefined && normalized.goodtype !== undefined) normalized.good_type = normalized.goodtype
			if (normalized.user_id === undefined && normalized.userid !== undefined) normalized.user_id = normalized.userid
			form.value = normalized
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
		} else if(formType == 'info'){
			isAdd.value = false
			formTitle.value = '查看' + formName
			getInfo()
		} else if(formType == 'edit'){
			isAdd.value = true
			formTitle.value = '修改' + formName
			getInfo()
		} else if(formType == 'cross'){
			isAdd.value = true
			formTitle.value = formNames
			// row = row
			crossRow.value = row
			crossTable.value = table
			crossTips.value = tips
			crossColumnName.value = statusColumnName
			crossColumnValue.value = statusColumnValue
			for(let x in row){
				if(x=='order_id'){
					form.value.order_id = row[x];
					disabledForm.value.order_id = true;
					continue;
				}
				if(x=='good_name'){
					form.value.good_name = row[x];
					disabledForm.value.good_name = true;
					continue;
				}
				if(x=='picture'){
					form.value.picture = row[x];
					disabledForm.value.picture = true;
					continue;
				}
				if(x=='buy_number'){
					form.value.buy_number = row[x];
					disabledForm.value.buy_number = true;
					continue;
				}
				if(x=='price'){
					form.value.price = row[x];
					disabledForm.value.price = true;
					continue;
				}
				if(x=='discount_price'){
					form.value.discount_price = row[x];
					disabledForm.value.discount_price = true;
					continue;
				}
				if(x=='total'){
					form.value.total = row[x];
					disabledForm.value.total = true;
					continue;
				}
				if(x=='discount_total'){
					form.value.discount_total = row[x];
					disabledForm.value.discount_total = true;
					continue;
				}
				if(x=='seat_name'){
					form.value.seat_name = row[x];
					disabledForm.value.seat_name = true;
					continue;
				}
				if(x=='status'){
					form.value.status = row[x];
					disabledForm.value.status = true;
					continue;
				}
				if(x=='remark'){
					form.value.remark = row[x];
					disabledForm.value.remark = true;
					continue;
				}
                if(x=='good_type'){
					form.value.good_type = row[x];
					disabledForm.value.good_type = true;
					continue;
				}
			}
			formVisible.value = true
		}
	}
	//提交
	const save = () => {
		let params = JSON.parse(JSON.stringify(form.value))
		const hasValue = (v) => v !== undefined && v !== null && v !== '' && !(typeof v === 'number' && Number.isNaN(v))
		if (params.orderid === undefined && hasValue(params.order_id)) {
			params.orderid = params.order_id
		}
		if (params.goodname === undefined && hasValue(params.good_name)) {
			params.goodname = params.good_name
		}
		if (params.buynumber === undefined && hasValue(params.buy_number)) {
			params.buynumber = params.buy_number
		}
		if (params.userid === undefined && hasValue(params.user_id)) {
			params.userid = params.user_id
		}
		if (params.goodtype === undefined && hasValue(params.good_type)) {
			params.goodtype = params.good_type
		}
		if (params.discountprice === undefined && hasValue(params.discount_price)) {
			params.discountprice = params.discount_price
		}
		if (params.discounttotal === undefined && hasValue(params.discount_total)) {
			params.discounttotal = params.discount_total
		}
		let url = `${tableName}/${!form.value.id ? 'save' : 'update'}`
		if(type.value == 'cross'){
			var statusColumnName = crossColumnName.value
			var statusColumnValue = crossColumnValue.value
			if(statusColumnName!='') {
				var obj = JSON.parse(JSON.stringify(crossRow.value))
				if(statusColumnName.indexOf("[")>=0 && statusColumnName.indexOf("]")>=0){
					if(statusColumnName.startsWith("[")){
						var subStatusColumnName = statusColumnName.substring(1,statusColumnName.length-1)
						obj[subStatusColumnName] = statusColumnValue;
					}else{
						var subStatusColumnName = statusColumnName.substring(0,statusColumnName.indexOf("["))
						obj[subStatusColumnName].push(statusColumnValue);
					}
				}else{
					obj[statusColumnName] = statusColumnValue;
				}
				
				context?.$http({
					url: `${crossTable.value}/update`,
					method: 'post',
					data: obj
				}).then(res=>{})
			}
		}
		context?.$http({
			url: url,
			method: 'post',
			data: params
		}).then(res => {
			emit('formModelChange')
			context?.$toolUtil.message('操作成功', 'success')
			formVisible.value = false
		})
	}
	//关闭
	const closeClick = () => {
		formVisible.value = false
	}
	defineExpose({
		init,
	})
</script>
<style lang="scss" scoped>
	.edit_form {
		// 模态框层级
		:deep(.el-dialog) {
		}
		// 模态框内容
		:deep(.el-dialog__body) {
			// 表单
			.el-form {
			}
		}
		// 模态框底部
		:deep(.el-dialog__footer) {
		}
		
		// 按钮盒子
		.formModel_btn_box {
			.cancel_btn {
			}
			.confirm_btn {
			}
		}
	}
</style>
