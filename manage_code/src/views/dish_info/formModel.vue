<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="50%" destroy-on-close :fullscreen='false'>
			<el-form class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :span="24">
						<el-form-item label="菜品名称" prop="dish_name">
							<el-input class="list_inp" v-model="form.dish_name" placeholder="菜品名称"
                                type="text"
								:readonly="!isAdd||disabledForm.dish_name?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item prop="dish_image"
									  label="菜品图片"
						>
							<uploads
								:disabled="!isAdd||disabledForm.dish_image?true:false"
								action="file/upload"
								tip="请上传菜品图片"
								style="width: 100%;text-align: left;"
								:fileUrls="form.dish_image?form.dish_image:''" 
								@change="dish_imageUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="菜品类型" prop="dish_category">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.dish_category?true:false"
								filterable
								allow-create
								default-first-option
								v-model="form.dish_category" 
								placeholder="请选择菜品类型"
								>
								<el-option v-for="(item,index) in dishCategoryLists" :label="item"
									:value="item"
									>
								</el-option>
								<template #footer>
									<div style="display:flex;justify-content:flex-end;gap:8px;padding:8px 10px;">
										<el-button size="small" @click.stop="addDishCategoryClick" :disabled="!canManageDishCategory">添加</el-button>
										<el-button size="small" type="primary" @click.stop="renameDishCategoryClick" :disabled="!canManageDishCategory || !form.dish_category">修改</el-button>
										<el-button size="small" type="danger" @click.stop="deleteDishCategoryClick" :disabled="!canManageDishCategory || !form.dish_category">删除</el-button>
									</div>
								</template>
						</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="口味" prop="flavor">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.flavor?true:false"
								v-model="form.flavor" 
								placeholder="请选择口味"
								filterable
								allow-create
								default-first-option
								>
								<el-option v-for="(item,index) in flavorLists" :label="item"
									:value="item"
									>
								</el-option>
								<template #footer>
									<div style="display:flex;justify-content:flex-end;gap:8px;padding:8px 10px;">
										<el-button size="small" @click.stop="addFlavorClick" :disabled="!canManageFlavor">添加</el-button>
										<el-button size="small" type="primary" @click.stop="renameFlavorClick" :disabled="!canManageFlavor || !form.flavor">修改</el-button>
										<el-button size="small" type="danger" @click.stop="deleteFlavorClick" :disabled="!canManageFlavor || !form.flavor">删除</el-button>
									</div>
								</template>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="推荐指数" prop="rating">
							<el-select
								class="list_sel"
								:disabled="!isAdd||disabledForm.rating?true:false"
								v-model="form.rating" 
								placeholder="请选择推荐指数"
								>
								<el-option v-for="(item,index) in ratingLists" :label="item"
									:value="item"
									>
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="单限" prop="purchase_limit">
							<el-input class="list_inp" v-model.number="form.purchase_limit" placeholder="单限"
                                type="text"
								:readonly="!isAdd||disabledForm.purchase_limit?true:false" />
						</el-form-item>
					</el-col>

					<el-col :span="24">
						<el-form-item label="库存" prop="stock">
							<el-input class="list_inp" v-model.number="form.stock" placeholder="库存"
                                type="text"
								:readonly="!isAdd||disabledForm.stock?true:false" />
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
					<el-form-item label="折扣价" prop="discountprice">
						<el-input class="list_inp" v-model.number="form.discountprice" placeholder="折扣价"
                            type="number"
							:readonly="!isAdd||disabledForm.discountprice?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="24">
						<el-form-item label="菜品详情" prop="dish_description">
							<editor :value="form.dish_description" placeholder="请输入菜品详情" :readonly="!isAdd||disabledForm.dish_description?true:false"
								class="list_editor" @change="(e)=>editorChange(e,'dish_description')"></editor>
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
	import { ElMessageBox } from 'element-plus'
    import {
        useStore
    } from 'vuex';
    const store = useStore()
    const user = computed(()=>store.getters['user/session'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;	
	const emit = defineEmits(['formModelChange'])
	//基础信息
	const tableName = 'dish_info'
	const formName = '美食信息'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        dish_name : false,
        dish_image : false,
        dish_category : false,
        dish_description : false,
        flavor : false,
        rating : false,
        click_time : false,
        purchase_limit : false,
        stock : false,
        price : false,
        discountprice : false,
        storeupNumber : false,
        staff_account : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		dish_name: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		dish_image: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		dish_category: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		dish_description: [
		],
		flavor: [
		],
		rating: [
		],
		click_time: [
		],
		purchase_limit: [
            { required: true, message: '请输入', trigger: 'blur' },
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		stock: [
            { required: true, message: '请输入', trigger: 'blur' },
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		price: [
            { required: true, message: '请输入', trigger: 'blur' },
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		discountprice: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
        storeupNumber: [
            { validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
        ],
        staff_account: [
        ],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	const isStaff = computed(() => {
		return context?.$toolUtil.storageGet('sessionTable') === 'staff'
	})
	const resetDisabledForm = () => {
		disabledForm.value = {
			dish_name : false,
			dish_image : false,
			dish_category : false,
			dish_description : false,
			flavor : false,
			rating : false,
			click_time : false,
			purchase_limit : false,
			stock : false,
			price : false,
			discountprice : false,
            storeupNumber : false,
			staff_account : false,
		}
        const canAdd = context?.$toolUtil.isAuth('dish_info','新增')
        const canEdit = context?.$toolUtil.isAuth('dish_info','修改')
        if (isStaff.value && !(canAdd || canEdit)) {
            disabledForm.value.dish_name = true
            disabledForm.value.dish_image = true
            disabledForm.value.dish_category = true
            disabledForm.value.dish_description = true
            disabledForm.value.flavor = true
            disabledForm.value.rating = true
            disabledForm.value.purchase_limit = true
            disabledForm.value.price = true
            disabledForm.value.discountprice = true
        }
	}
	//菜品图片上传回调
	const dish_imageUploadSuccess=(e)=>{
		form.value.dish_image = e
	}
	//菜品类型列表
	const dishCategoryLists = ref([])
	const canManageDishCategory = computed(() => {
		return isAdd.value && !(disabledForm.value && disabledForm.value.dish_category)
	})
	const refreshDishCategoryLists = () => {
		return context?.$http({
			url: `option/dish_info/dish_category`,
			method: 'get'
		}).then(res => {
			dishCategoryLists.value = res.data.data || []
		})
	}
	const addDishCategoryClick = () => {
		if (!canManageDishCategory.value) return
		ElMessageBox.prompt('请输入菜品类型', '添加菜品类型', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			inputValidator: (value) => {
				const v = (value || '').trim()
				if (!v) return '请输入菜品类型'
				if (dishCategoryLists.value.includes(v)) return '该菜品类型已存在'
			},
		}).then(({ value }) => {
			const v = (value || '').trim()
			if (!v) return
			if (!dishCategoryLists.value.includes(v)) {
				dishCategoryLists.value = dishCategoryLists.value.concat([v])
			}
			form.value.dish_category = v
			context?.$toolUtil.message('已添加到下拉选项，提交保存后生效', 'success')
		}).catch(() => {})
	}
	const renameDishCategoryClick = () => {
		if (!canManageDishCategory.value) return
		const oldCategory = (form.value.dish_category || '').trim()
		if (!oldCategory) {
			context?.$toolUtil.message('请先选择菜品类型', 'warning')
			return
		}
		ElMessageBox.prompt('请输入新的菜品类型', '修改菜品类型', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			inputValue: oldCategory,
			inputValidator: (value) => {
				const v = (value || '').trim()
				if (!v) return '请输入菜品类型'
				if (v === oldCategory) return '新旧类型相同'
				if (dishCategoryLists.value.includes(v)) return '该菜品类型已存在'
			},
		}).then(({ value }) => {
			const newCategory = (value || '').trim()
			if (!newCategory || newCategory === oldCategory) return
			context?.$http({
				url: `${tableName}/dish_category/rename`,
				method: 'post',
				data: { oldCategory, newCategory }
			}).then(res => {
				if (res.data && res.data.code === 0) {
					form.value.dish_category = newCategory
					return refreshDishCategoryLists().then(() => {
						context?.$toolUtil.message('修改成功', 'success')
					})
				}
				context?.$toolUtil.message(res.data?.msg || '修改失败', 'error')
			})
		}).catch(() => {})
	}
	const deleteDishCategoryClick = () => {
		if (!canManageDishCategory.value) return
		const oldCategory = (form.value.dish_category || '').trim()
		if (!oldCategory) {
			context?.$toolUtil.message('请先选择菜品类型', 'warning')
			return
		}
		ElMessageBox.confirm(`是否删除菜品类型“${oldCategory}”？将把该类型下的菜品移入“未分类”`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context?.$http({
				url: `${tableName}/dish_category/delete`,
				method: 'post',
				data: { oldCategory }
			}).then(res => {
				if (res.data && res.data.code === 0) {
					form.value.dish_category = '未分类'
					return refreshDishCategoryLists().then(() => {
						context?.$toolUtil.message('删除成功', 'success')
					})
				}
				context?.$toolUtil.message(res.data?.msg || '删除失败', 'error')
			})
		}).catch(() => {})
	}
	//口味列表
	const flavorLists = ref([])
	const canManageFlavor = computed(() => {
		return isAdd.value && !(disabledForm.value && disabledForm.value.flavor)
	})
	const refreshFlavorLists = () => {
		return context?.$http({
			url: `option/dish_info/flavor`,
			method: 'get'
		}).then(res => {
			flavorLists.value = res.data.data || []
		})
	}
	const addFlavorClick = () => {
		if (!canManageFlavor.value) return
		ElMessageBox.prompt('请输入口味', '添加口味', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			inputValidator: (value) => {
				const v = (value || '').trim()
				if (!v) return '请输入口味'
				if (flavorLists.value.includes(v)) return '该口味已存在'
			},
		}).then(({ value }) => {
			const v = (value || '').trim()
			if (!v) return
			if (!flavorLists.value.includes(v)) {
				flavorLists.value = flavorLists.value.concat([v])
			}
			form.value.flavor = v
			context?.$toolUtil.message('已添加到下拉选项，提交保存后生效', 'success')
		}).catch(() => {})
	}
	const renameFlavorClick = () => {
		if (!canManageFlavor.value) return
		const oldFlavor = (form.value.flavor || '').trim()
		if (!oldFlavor) {
			context?.$toolUtil.message('请先选择口味', 'warning')
			return
		}
		ElMessageBox.prompt('请输入新的口味', '修改口味', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			inputValue: oldFlavor,
			inputValidator: (value) => {
				const v = (value || '').trim()
				if (!v) return '请输入口味'
				if (v === oldFlavor) return '新旧口味相同'
				if (flavorLists.value.includes(v)) return '该口味已存在'
			},
		}).then(({ value }) => {
			const newFlavor = (value || '').trim()
			if (!newFlavor || newFlavor === oldFlavor) return
			context?.$http({
				url: `${tableName}/flavor/rename`,
				method: 'post',
				data: { oldFlavor, newFlavor }
			}).then(res => {
				if (res.data && res.data.code === 0) {
					form.value.flavor = newFlavor
					return refreshFlavorLists().then(() => {
						context?.$toolUtil.message('修改成功', 'success')
					})
				}
				context?.$toolUtil.message(res.data?.msg || '修改失败', 'error')
			})
		}).catch(() => {})
	}
	const deleteFlavorClick = () => {
		if (!canManageFlavor.value) return
		const oldFlavor = (form.value.flavor || '').trim()
		if (!oldFlavor) {
			context?.$toolUtil.message('请先选择口味', 'warning')
			return
		}
		ElMessageBox.confirm(`是否删除口味“${oldFlavor}”？将清空所有菜品的该口味`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context?.$http({
				url: `${tableName}/flavor/delete`,
				method: 'post',
				data: { oldFlavor }
			}).then(res => {
				if (res.data && res.data.code === 0) {
					if (form.value.flavor === oldFlavor) form.value.flavor = ''
					return refreshFlavorLists().then(() => {
						context?.$toolUtil.message('删除成功', 'success')
					})
				}
				context?.$toolUtil.message(res.data?.msg || '删除失败', 'error')
			})
		}).catch(() => {})
	}
	//推荐指数列表
	const ratingLists = ref([])

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		resetDisabledForm()
		form.value = {
			dish_name: '',
			dish_image: '',
			dish_category: '',
			dish_description: '',
			flavor: '',
			rating: '',
			click_time: '',
			purchase_limit: '',
			stock: '',
			price: '',
			discountprice: '',
            staff_account: '',
		}
	}
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			res.data.data.dish_description = res.data.data.dish_description?(res.data.data.dish_description.replace(reg,'../../../canteen/file')):'';
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
				if(x=='dish_name'){
					form.value.dish_name = row[x];
					disabledForm.value.dish_name = true;
					continue;
				}
				if(x=='dish_image'){
					form.value.dish_image = row[x];
					disabledForm.value.dish_image = true;
					continue;
				}
				if(x=='dish_category'){
					form.value.dish_category = row[x];
					disabledForm.value.dish_category = true;
					continue;
				}
				if(x=='dish_description'){
					form.value.dish_description = row[x];
					disabledForm.value.dish_description = true;
					continue;
				}
				if(x=='flavor'){
					form.value.flavor = row[x];
					disabledForm.value.flavor = true;
					continue;
				}
				if(x=='rating'){
					form.value.rating = row[x];
					disabledForm.value.rating = true;
					continue;
				}
				if(x=='click_time'){
					form.value.click_time = row[x];
					disabledForm.value.click_time = true;
					continue;
				}
				if(x=='purchase_limit'){
					form.value.purchase_limit = row[x];
					disabledForm.value.purchase_limit = true;
					continue;
				}
				if(x=='stock'){
					form.value.stock = row[x];
					disabledForm.value.stock = true;
					continue;
				}
				if(x=='price'){
					form.value.price = row[x];
					disabledForm.value.price = true;
					continue;
				}
				if(x=='discountprice'){
					form.value.discountprice = row[x];
					disabledForm.value.discountprice = true;
					continue;
				}
				if(x=='storeupNumber'){
					form.value.storeupNumber = row[x];
					disabledForm.value.storeupNumber = true;
					continue;
				}
				if(x=='staff_account'){
					form.value.staff_account = row[x];
					disabledForm.value.staff_account = true;
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
			// 不强制默认口味，保持为空由用户选择
			formVisible.value = true
		}

	context?.$http({
		url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
		method: 'get'
	}).then(res => {
		var json = res.data.data
		// 商家字段已废弃，改为单一餐厅系统
	})
		refreshDishCategoryLists()
		refreshFlavorLists()
		ratingLists.value = "★,★★,★★★,★★★★,★★★★★".split(',')
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
		if(form.value.dish_image!=null) {
			form.value.dish_image = form.value.dish_image.replace
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
                                const code = res && res.data ? res.data.code : null
                                if (code !== 0) {
                                    return
                                }
                                emit('formModelChange')
                                context?.$toolUtil.message(`操作成功`,'success')
                                formVisible.value = false
                            }).catch(()=>{})
						}
					})
				}else{
					context?.$http({
						url: `${tableName}/${!form.value.id ? "save" : "update"}`,
						method: 'post', 
						data: form.value 
					}).then(async (res)=>{
                        const code = res && res.data ? res.data.code : null
                        if (code !== 0) {
                            return
                        }
                        emit('formModelChange')
                        context?.$toolUtil.message(`操作成功`,'success')
                        formVisible.value = false
                    }).catch(()=>{})
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
