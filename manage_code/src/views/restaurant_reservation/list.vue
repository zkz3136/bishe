<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							餐桌名称：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.seat_name" placeholder="餐桌名称"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" size="small" @click="addClick" v-if="btnAuth('restaurant_reservation','新增')">
						新增
					</el-button>
					<el-button class="slot_btn" type="primary" size="small" @click="openTimeSlotDialog" v-if="role=='管理员'">
						预约时段设置
					</el-button>
					<el-button class="hexiao_btn" type="warning" size="small" :disabled="selRows.length?false:true" @click="hexiaoClick(null)"  v-if="btnAuth('restaurant_reservation','查看')">
						核销
					</el-button>
					<el-button class="del_btn" type="danger" size="small" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('restaurant_reservation','删除')">
						删除
					</el-button>
				</div>
			</div>
			<el-table
				v-loading="listLoading"
				border
				:stripe='false'
				@selection-change="handleSelectionChange"
				ref="table"
				v-if="btnAuth('restaurant_reservation','查看')"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" :selectable="checkSelectable" />
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="seat_name"
					label="餐桌名称">
					<template #default="scope">
						{{scope.row.seat_name}}
					</template>
				</el-table-column>
				<el-table-column label="封面" min-width="140" width="120" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<div v-if="scope.row.cover_image">
							<el-image v-if="scope.row.cover_image.substring(0,4)=='http'" preview-teleported
								:preview-src-list="[scope.row.cover_image.split(',')[0]]"
								:src="scope.row.cover_image.split(',')[0]" style="width:100px;height:100px"></el-image>
							<el-image v-else preview-teleported
								:preview-src-list="[$config.url+scope.row.cover_image.split(',')[0]]"
								:src="$config.url+scope.row.cover_image.split(',')[0]" style="width:100px;height:100px">
							</el-image>
						</div>
						<div v-else>无图片</div>
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="table_location"
					label="餐桌位置">
					<template #default="scope">
						{{scope.row.table_location}}
					</template>
				</el-table-column>
			<el-table-column min-width="140"
				:resizable='true'
				:sortable='true'
				align="left"
				header-align="left"
				prop="capacity"
				label="可坐人数">
				<template #default="scope">
					{{scope.row.capacity}}
				</template>
			</el-table-column>
			<el-table-column min-width="140"
				:resizable='true'
				:sortable='true'
				align="left"
				header-align="left"
				prop="deposit"
				label="定金金额">
				<template #default="scope">
					{{scope.row.deposit}}元
				</template>
			</el-table-column>
			<el-table-column min-width="140"
				:resizable='true'
				:sortable='true'
				align="left"
				header-align="left"
				prop="verification_status"
				label="核销状态">
				<template #default="scope">
					<el-tag type="success" v-if="scope.row.verification_status=='已核销'">已核销</el-tag>
					<el-tag type="warning" v-else>未核销</el-tag>
				</template>
			</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="login_name"
					label="账号">
					<template #default="scope">
						{{scope.row.login_name}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="name"
					label="名称">
					<template #default="scope">
						{{scope.row.name}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="reservation_time"
					label="预约时间">
					<template #default="scope">
						{{scope.row.reservation_time}}
					</template>
				</el-table-column>
				<el-table-column label="操作" width="320" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" size="small" v-if=" btnAuth('restaurant_reservation','查看')" @click="infoClick(scope.row.id)">
							详情
						</el-button>
						<el-button class="edit_btn" type="primary" size="small" @click="editClick(scope.row.id)" v-if=" btnAuth('restaurant_reservation','修改') && role!='员工'">
							修改
						</el-button>
						<el-button class="del_btn" type="danger" size="small" @click="delClick(scope.row.id)"  v-if="btnAuth('restaurant_reservation','删除')">
							删除
						</el-button>
						<el-button class="hexiao_btn" type="success" size="small" v-if="btnAuth('restaurant_reservation','查看') && scope.row.payment_status=='已支付' && scope.row.verification_status=='未核销'" @click="hexiaoClick(scope.row.id)">
							核销
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				background
				:layout="layouts.join(',')"
				:total="total"
				:page-size="listQuery.limit"
                v-model:current-page="listQuery.page"
				prev-text="<"
				next-text=">"
				:hide-on-single-page="false"
				:style='{}'
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange"  />
		</div>
		<el-dialog v-model="timeSlotDialogVisible" title="预约时段设置" width="520px">
			<el-form label-width="100px">
				<el-form-item label="指定日期">
					<el-date-picker
						v-model="timeSlotDate"
						type="date"
						format="YYYY-MM-DD"
						value-format="YYYY-MM-DD"
						:disabled-date="timeSlotDateDisabledDate"
						clearable
						style="width:100%;"
						placeholder="不选择则为全局时段" />
				</el-form-item>
				<el-form-item label="可预约小时">
					<el-select
						v-model="selectedSlotHours"
						multiple
						filterable
						style="width:100%;"
						placeholder="请选择可预约的小时">
						<el-option
							v-for="opt in slotHourOptions"
							:key="opt.value"
							:label="opt.label"
							:value="opt.value" />
					</el-select>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="timeSlotDialogVisible=false">取消</el-button>
				<el-button v-if="timeSlotDate" type="warning" @click="clearDateTimeSlotSettings">恢复全局时段</el-button>
				<el-button type="success" @click="saveTimeSlotSettings">保存</el-button>
			</template>
		</el-dialog>
		<formModel ref="formRef" @formModelChange="formModelChange"></formModel>
	</div>
</template>
<script setup>
	import axios from 'axios'
    import moment from "moment"
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
		watch,
		computed,
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const avatar = ref(store.state.user.avatar)
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	import formModel from './formModel.vue'
	//基础信息

	const tableName = 'restaurant_reservation'
	const formName = '餐厅预约'
	const route = useRoute()
	const role = ref('')
	const timeSlotDialogVisible = ref(false)
	const timeSlotDate = ref('')
	const maxDaysAhead = 30
	const selectedSlotHours = ref([])
	const timeSlotDateDisabledDate = (date) => {
		const today = new Date()
		today.setHours(0, 0, 0, 0)
		const maxDate = new Date(today)
		maxDate.setDate(maxDate.getDate() + maxDaysAhead)
		return date.getTime() < today.getTime() || date.getTime() > maxDate.getTime()
	}
	const slotHourOptions = computed(() => {
		const options = []
		for (let i = 9; i <= 19; i++) {
			const start = String(i).padStart(2, '0')
			options.push({
				label: `${start}:00`,
				value: start
			})
		}
		return options
	})
	//基础信息
	onMounted(()=>{
		role.value = context?.$toolUtil.storageGet('role')
	})
	//列表数据
	const list = ref(null)
	const table = ref(null)
	const listQuery = ref({
		page: 1,
		limit: 10,
		sort: 'id',
		order: 'desc'
	})
	const searchQuery = ref({})
	const selRows = ref([])
	const listLoading = ref(false)
	const listChange = (row) =>{
		nextTick(()=>{
			//table.value.clearSelection()
			table.value.toggleRowSelection(row)
		})
	}
	//列表
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		params['sort'] = 'id'
		params['order'] = 'desc'
		if(searchQuery.value.seat_name&&searchQuery.value.seat_name!=''){
			params['seat_name'] = '%' + searchQuery.value.seat_name + '%'
		}
        
		context.$http({
			url: `${tableName}/page`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = Number(res.data.data.total)
		})
	}

	const fetchTimeSlotSettings = (date) => {
		return context?.$http({
			url: `${tableName}/time_slots`,
			method: 'get',
			params: date ? { date } : {}
		}).then(res => {
			const data = res?.data?.data
			if (Array.isArray(data)) {
				selectedSlotHours.value = data.map(v => String(v)).filter(v => v !== '')
				return
			}
			selectedSlotHours.value = ['09','10','11','12','13','14','15','16','17','18','19']
		}).catch(() => {
			selectedSlotHours.value = ['09','10','11','12','13','14','15','16','17','18','19']
		})
	}

	const openTimeSlotDialog = () => {
		timeSlotDialogVisible.value = true
		timeSlotDate.value = ''
		fetchTimeSlotSettings()
	}

	watch(timeSlotDate, (newDate) => {
		if(!timeSlotDialogVisible.value) return
		fetchTimeSlotSettings(newDate)
	})

	const saveTimeSlotSettings = () => {
		const slots = (selectedSlotHours.value || []).map(v => String(v)).filter(v => v !== '')
		if (!slots.length && !timeSlotDate.value) {
			context?.$toolUtil.message('请选择至少一个可预约小时', 'error')
			return
		}
		context?.$http({
			url: `${tableName}/time_slots/update`,
			method: 'post',
			data: { slots, date: timeSlotDate.value || null }
		}).then(res => {
			if (res?.data?.code === 0) {
				if (timeSlotDate.value && !slots.length) {
					context?.$toolUtil.message('已设置为当日不开张', 'success')
				} else {
					context?.$toolUtil.message('保存成功', 'success')
				}
				timeSlotDialogVisible.value = false
				return
			}
			context?.$toolUtil.message(res?.data?.msg || '保存失败', 'error')
		}).catch(() => {
			context?.$toolUtil.message('保存失败', 'error')
		})
	}

	const clearDateTimeSlotSettings = () => {
		if(!timeSlotDate.value) return
		context?.$http({
			url: `${tableName}/time_slots/update`,
			method: 'post',
			data: { date: timeSlotDate.value, clear: true }
		}).then(res => {
			if (res?.data?.code === 0) {
				context?.$toolUtil.message('已恢复全局时段', 'success')
				fetchTimeSlotSettings(timeSlotDate.value)
				return
			}
			context?.$toolUtil.message(res?.data?.msg || '清除失败', 'error')
		}).catch(() => {
			context?.$toolUtil.message('清除失败', 'error')
		})
	}
	//删
	const delClick = (id) => {
		let ids = ref([])
		if (id) {
			ids.value = [id]
		} else {
			if (selRows.value.length) {
				for (let x in selRows.value) {
					ids.value.push(selRows.value[x].id)
				}
			} else {
				return false
			}
		}
		ElMessageBox.confirm(`是否删除选中${formName}`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: ids.value
			}).then(res => {
				context?.$toolUtil.message('删除成功', 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	//检查行是否可选择（只有已支付且未核销的记录才能被选择进行核销）
	const checkSelectable = (row, index) => {
		return row.payment_status === '已支付' && row.verification_status === '未核销'
	}
	//列表数据
	//分页
	const total = ref(0)
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}
	//分页
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	//搜索
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	//表单
	const formRef = ref(null)
	const formModelChange=()=>{
		searchClick()
	}
	const addClick = ()=>{
		formRef.value.init()
	}
	const editClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'edit')
			return
		}
		if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'edit')
		}
	}

	const infoClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'info')
		}
		else if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'info')
		}
	}
	// 表单
	// 预览文件
	const preClick = (file) =>{
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		window.open(context?.$config.url + file)
	}
	// 下载文件
	const download = (file) => {
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		let arr = file.replace(new RegExp('file/', "g"), "")
		const url = (location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/' + arr
		const a = document.createElement('a')
		a.href = url
		a.download = arr
		a.dispatchEvent(new MouseEvent('click', {
			bubbles: true,
			cancelable: true,
			view: window
		}))
	}
	//核销退费
	const hexiaoClick = (id) => {
		let ids = ref([])
		if (id) {
			ids.value = [id]
		} else {
			if (selRows.value.length) {
				for (let x in selRows.value) {
					ids.value.push(selRows.value[x].id)
				}
			} else {
				return false
			}
		}
		ElMessageBox.confirm(`确认核销退费？将退还定金${ids.value.length * 50}元`, '提示', {
			confirmButtonText: '确认核销',
			cancelButtonText: '取消',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/hexiao`,
				method: 'post',
				data: ids.value
			}).then(res => {
				if(res.data.code == 0) {
					context?.$toolUtil.message(res.data.message || '核销退费成功', 'success',()=>{
						getList()
					})
				} else {
					context?.$toolUtil.message(res.data.msg || '核销失败', 'error')
				}
			})
		}).catch(_ => {})
	}
	//初始化
	const init = () => {
		getList()
	}
	init()
</script>
<style lang="scss" scoped>

	// 操作盒子
	.list_search_view {
		// 搜索盒子
		.search_form {
			// 子盒子
			.search_view {
				// 搜索label
				.search_label {
				}
				// 搜索item
				.search_box {
					// 输入框
					:deep(.search_inp) {
					}
					// 下拉框
					:deep(.search_sel) {
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
			// 搜索按钮盒子
			.search_btn_view {
				// 搜索按钮
				.search_btn {
				}
				// 搜索按钮-悬浮
				.search_btn:hover {
				}
			}
		}
		//头部按钮盒子
		.btn_view {
			// 统一所有按钮的垂直对齐和尺寸
			:deep(.el-button) {
				vertical-align: middle;
				margin: 0 5px;
				min-width: 70px;
				height: 32px;
				padding: 0 15px;
				line-height: 32px;
				font-size: 14px;
			}
			// 新增按钮
			:deep(.add_btn) {
				min-width: 70px;
				height: 32px;
				font-size: 14px;
			}
			// 核销按钮
			:deep(.hexiao_btn) {
				min-width: 70px;
				height: 32px;
				font-size: 14px;
			}
			// 删除按钮
			:deep(.del_btn) {
				min-width: 70px;
				height: 32px;
				font-size: 14px;
			}
			// 其他
			:deep(.el-button--default){
			}
			// 其他-悬浮
			:deep(.el-button--default:hover){
			}
			// 新增
			:deep(.el-button--success){
			}
			// 新增-悬浮
			:deep(.el-button--success:hover){
			}
			// 删除
			:deep(.el-button--danger){
			}
			// 删除-悬浮
			:deep(.el-button--danger:hover){
			}
			// 统计
			:deep(.el-button--warning){
			}
			// 统计-悬浮
			:deep(.el-button--warning:hover){
			}
		}
	}
	// 表格样式
	.el-table {
		:deep(.el-table__header-wrapper) {
			thead {
				tr {
					th {
						.cell {
						}
					}
				}
			}
		}
		:deep(.el-table__body-wrapper) {
			tbody {
				tr {
					td {
						.cell {
							// 统一所有按钮的垂直对齐
							.el-button {
								vertical-align: middle;
								margin: 2px;
							}
							// 编辑
							.el-button--primary {
							}
							// 编辑-悬浮
							.el-button--primary:hover {
							}
							// 详情
							.el-button--info {
							}
							// 详情-悬浮
							.el-button--info:hover {
							}
							// 删除
							.el-button--danger {
							}
							// 删除-悬浮
							.el-button--danger:hover {
							}
							// 跨表
							.el-button--success {
							}
							// 跨表-悬浮
							.el-button--success:hover {
							}
							// 操作
							.el-button--warning {
							}
							// 操作-悬浮
							.el-button--warning:hover {
							}
						}
					}
				}
				tr:hover {
					td {
					}
				}
			}
		}
	}
	// 分页器
	.el-pagination {
		// 总页码
		:deep(.el-pagination__total) {
		}
		// 上一页
		:deep(.btn-prev) {
		}
		// 下一页
		:deep(.btn-next) {
		}
		// 上一页禁用
		:deep(.btn-prev:disabled) {
		}
		// 下一页禁用
		:deep(.btn-next:disabled) {
		}
		// 页码
		:deep(.el-pager) {
			// 数字
			.number {
			}
			// 数字悬浮
			.number:hover {
			}
			// 选中
			.number.is-active {
			}
		}
		// sizes
		:deep(.el-pagination__sizes) {
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
			.el-select {
			}
		}
		// 跳页
		:deep(.el-pagination__jump) {
			// 输入框
			.el-input {
			}
		}
	}
</style>
