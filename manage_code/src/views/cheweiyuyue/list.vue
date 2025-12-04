<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							车位编号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.cheweibianhao" placeholder="车位编号"
								clearable>
							</el-input>
						</div>
					</div>
                    <div class="search_view">
                        <div class="search_label">
                            支付状态：
                        </div>
                        <div class="search_box">
                            <el-select
                                class="search_sel"
                                clearable
                                v-model="searchQuery.zhifuzhuangtai"
                                placeholder="支付状态"
                            >
                                <el-option label="已支付" value="已支付"></el-option>
                                <el-option label="未支付" value="未支付"></el-option>
                                <el-option label="已退款" value="已退款"></el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="search_view">
                        <div class="search_label">
                            核销状态：
                        </div>
                        <div class="search_box">
                            <el-select
                                class="search_sel"
                                clearable
                                v-model="searchQuery.hexiaozhuangtai"
                                placeholder="核销状态"
                            >
                                <el-option label="未核销" value="未核销"></el-option>
                                <el-option label="已核销" value="已核销"></el-option>
                            </el-select>
                        </div>
                    </div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" size="small" @click="addClick" v-if="btnAuth('cheweiyuyue','新增')">
						新增
					</el-button>
					<el-button class="hexiao_btn" type="warning" size="small" :disabled="selRows.length?false:true" @click="hexiaoClick(null)"  v-if="btnAuth('cheweiyuyue','查看')">
						核销
					</el-button>
					<el-button class="del_btn" type="danger" size="small" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('cheweiyuyue','删除')">
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
				v-if="btnAuth('cheweiyuyue','查看')"
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
					prop="cheweibianhao"
					label="车位编号">
					<template #default="scope">
						{{scope.row.cheweibianhao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="cheweiweizhi"
					label="车位位置">
					<template #default="scope">
						{{scope.row.cheweiweizhi}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="yuyueshijian"
					label="预约时间">
					<template #default="scope">
						{{scope.row.yuyueshijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="zhanghao"
					label="账号">
					<template #default="scope">
						{{scope.row.zhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="mingcheng"
					label="名称">
					<template #default="scope">
						{{scope.row.mingcheng}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="chepaihao"
					label="车牌号">
					<template #default="scope">
						{{scope.row.chepaihao}}
					</template>
				</el-table-column>
				<el-table-column min-width="100"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="dingjin"
					label="定金">
					<template #default="scope">
						{{scope.row.dingjin ? scope.row.dingjin + '元' : '-'}}
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="zhifuzhuangtai"
					label="支付状态">
					<template #default="scope">
						<el-tag v-if="scope.row.zhifuzhuangtai=='已支付'" type="success">已支付</el-tag>
						<el-tag v-else-if="scope.row.zhifuzhuangtai=='已退款'" type="info">已退款</el-tag>
						<el-tag v-else type="warning">未支付</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="120"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="hexiaozhuangtai"
					label="核销状态">
					<template #default="scope">
						<el-tag v-if="scope.row.hexiaozhuangtai=='已核销'" type="success">已核销</el-tag>
						<el-tag v-else type="warning">未核销</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="320" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" size="small" v-if=" btnAuth('cheweiyuyue','查看')" @click="infoClick(scope.row.id)">
							详情
						</el-button>
						<el-button class="edit_btn" type="primary" size="small" @click="editClick(scope.row.id)" v-if=" btnAuth('cheweiyuyue','修改')">
							修改
						</el-button>
						<el-button class="del_btn" type="danger" size="small" @click="delClick(scope.row.id)"  v-if="btnAuth('cheweiyuyue','删除')">
							删除
						</el-button>
						<el-button 
							class="hexiao_btn" 
							type="success" 
							size="small"
							v-if="btnAuth('cheweiyuyue','查看') && scope.row.zhifuzhuangtai=='已支付' && scope.row.hexiaozhuangtai=='未核销'" 
							@click="hexiaoClick(scope.row.id)">
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
		<formModel ref="formRef" @formModelChange="formModelChange"></formModel>
		<payForm ref="pay" @payChange="searchClick()"></payForm>
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

	const tableName = 'cheweiyuyue'
	const formName = '车位预约'
	const route = useRoute()
	//基础信息
	onMounted(()=>{
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
		if(searchQuery.value.cheweibianhao&&searchQuery.value.cheweibianhao!=''){
			params['cheweibianhao'] = '%' + searchQuery.value.cheweibianhao + '%'
		}
        if(searchQuery.value.zhifuzhuangtai && searchQuery.value.zhifuzhuangtai!=''){
            params['zhifuzhuangtai'] = searchQuery.value.zhifuzhuangtai
        }
        if(searchQuery.value.hexiaozhuangtai && searchQuery.value.hexiaozhuangtai!=''){
            params['hexiaozhuangtai'] = searchQuery.value.hexiaozhuangtai
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
		ElMessageBox.confirm('是否确认核销退费？退费后将返还50元定金到用户账户', '提示', {
			confirmButtonText: '确认',
			cancelButtonText: '取消',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/hexiao`,
				method: 'post',
				data: ids.value
			}).then(res => {
				context?.$toolUtil.message(res.data.message || '核销退费成功', 'success', () => {
					getList()
				})
			})
		}).catch(_ => {})
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
		return row.zhifuzhuangtai === '已支付' && row.hexiaozhuangtai === '未核销'
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
		axios.get((location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/download?fileName=' + arr, {
			headers: {
				token: context?.$toolUtil.storageGet('Token')
			},
			responseType: "blob"
		}).then(({
			data
		}) => {
			const binaryData = [];
			binaryData.push(data);
			const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
				type: 'application/pdf;chartset=UTF-8'
			}))
			const a = document.createElement('a')
			a.href = objectUrl
			a.download = arr
			// a.click()
			// 下面这个写法兼容火狐
			a.dispatchEvent(new MouseEvent('click', {
				bubbles: true,
				cancelable: true,
				view: window
			}))
			window.URL.revokeObjectURL(data)
		})
	}
	import payForm from '@/components/common/payForm'
	const pay = ref(null)
	const payClick = (row)=>{
		pay.value.payClick(tableName,row)
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
			// 统计/核销
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
							// 统一所有按钮的垂直对齐和间距
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
							// 跨表/核销
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
