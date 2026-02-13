<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							员工账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.loginName" placeholder="员工账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							员工姓名：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.name" placeholder="员工姓名"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" @click="addClick" v-if="btnAuth('staff','新增')">
						新增
					</el-button>
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('staff','删除')">
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
				v-if="btnAuth('staff','查看')"
                empty-text="暂无数据"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (searchQuery.page-1)*searchQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="login_name"
					label="员工账号">
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
					label="员工姓名">
					<template #default="scope">
						{{scope.row.name}}
					</template>
				</el-table-column>
				<el-table-column label="头像" min-width="140" width="120" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<div v-if="scope.row.avatar">
							<el-image v-if="scope.row.avatar.substring(0,4)=='http'" preview-teleported
								:preview-src-list="[scope.row.avatar.split(',')[0]]"
								:src="scope.row.avatar.split(',')[0]" style="width:100px;height:100px"></el-image>
							<el-image v-else preview-teleported
								:preview-src-list="[$config.url+scope.row.avatar.split(',')[0]]"
								:src="$config.url+scope.row.avatar.split(',')[0]" style="width:100px;height:100px">
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
					prop="position"
					label="职位">
					<template #default="scope">
						{{scope.row.position}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="mobile"
					label="电话">
					<template #default="scope">
						{{scope.row.mobile}}
					</template>
				</el-table-column>
				
				<el-table-column label="操作" width="300" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if=" btnAuth('staff','查看')" @click="infoClick(scope.row.id)">
							详情
						</el-button>
						<el-button class="edit_btn" type="primary" @click="editClick(scope.row.id)" v-if=" btnAuth('staff','修改')">
							修改						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)"  v-if="btnAuth('staff','删除')">
							删除						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				background
				:layout="layouts.join(',')"
				:total="total"
				:page-size="searchQuery.limit"
                v-model:current-page="searchQuery.page"
				prev-text="<"
				next-text=">"
				:hide-on-single-page="false"
				:style='{}'
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange"  />
		</div>
		<formModel ref="formRef" @formModelChange="formModelChange"></formModel>
	</div>
</template>
<script setup>
	import formModel from './formModel'
	import {
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
		computed,
		watch
	} from 'vue'
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	//基础数据
	const tableName = 'staff'
	const formRef = ref(null)
	const list = ref([])
	const total = ref(0)
	const selRows = ref([])
	const listLoading = ref(false)
	const table = ref(null)
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	//搜索表单
	const searchQuery = ref({
		page: 1,
		limit: 10,
		loginName: '',
		name: '',
		sort: 'id',
		order: 'desc',
	})
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	//获取数据
	const getList = () => {
		listLoading.value = true
		let params = {
			page: searchQuery.value.page,
			limit: searchQuery.value.limit,
			sort: searchQuery.value.sort,
			order: searchQuery.value.order,
		}
		if(searchQuery.value.loginName&&searchQuery.value.loginName!=''){
			params['loginName'] = '%' + searchQuery.value.loginName + '%'
		}
		if(searchQuery.value.name&&searchQuery.value.name!=''){
			params['name'] = '%' + searchQuery.value.name + '%'
		}
		context?.$http({
			url: `${tableName}/page`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = res.data.data.total
		}).catch(_ => {
			listLoading.value = false
            context?.$toolUtil.message('获取员工列表失败','error')
		})
	}
	//搜索
	const searchClick = () => {
		searchQuery.value.page = 1
		getList()
	}
	//添加
	const addClick = () => {
		formRef.value.init()
	}
	//详情
	const infoClick = (id) => {
		formRef.value.init(id)
	}
	//修改
	const editClick = (id) => {
		formRef.value.init(id, 'edit')
	}
	//删除
	const delClick = (id) => {
		let ids = []
		if (id) {
			ids = [id]
		} else {
			ids = selRows.value.map(item => {
				return item.id
			})
		}
		context?.$confirm(`确认删除这${ids.length}条记录吗?`, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning'
		}).then(() => {
			context?.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: ids
			}).then(res => {
				context?.$toolUtil.message('删除成功', 'success', () => {
					getList()
				})
			}).catch(_ => {})
		})
	}
	//分页
	const sizeChange = (size) => {
		searchQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		searchQuery.value.page = page
		getList()
	}
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	const listChange = (row) =>{
		nextTick(()=>{
			table.value.toggleRowSelection(row)
		})
	}
	//表单
	const formModelChange = () => {
		getList()
	}
	//初始化
	const init = () => {
		getList()
	}
	onMounted(()=>{
		init()
	})
</script>

<style lang="scss" scoped>
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
