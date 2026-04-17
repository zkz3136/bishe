<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							评分：
						</div>
						<div class="search_box">
							<el-rate v-model="searchQuery.score" :max="5" :allow-half="false" :clearable="true" @change="searchClick" />
						</div>
					</div>
					<div class="search_view" v-if="hasRefContext">
						<div class="search_label">
							仅当前菜品：
						</div>
						<div class="search_box">
							<el-switch v-model="filterByCurrentDish" @change="searchClick" />
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="refreshClick()" size="small">刷新</el-button>
						<el-button class="search_btn" type="default" @click="resetClick()" size="small">重置</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" @click="addClick" v-if="btnAuth('dish_review','新增') && canAdd">
						新增
					</el-button>
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)" >
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
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="菜品" min-width="140" :resizable='true' :sortable='false' align="left" header-align="left">
					<template #default="scope">
						{{ getDishName(scope.row.ref_id) }}
					</template>
				</el-table-column>
				<el-table-column label="图片" min-width="120" width="120" :resizable='true' :sortable='false' align="left" header-align="left">
					<template #default="scope">
						<div v-if="dishMap[Array.isArray(scope.row.ref_id)?scope.row.ref_id[0]:scope.row.ref_id]?.dish_image">
							<el-image v-if="dishMap[Array.isArray(scope.row.ref_id)?scope.row.ref_id[0]:scope.row.ref_id].dish_image.substring(0,4)=='http'" preview-teleported
								:preview-src-list="[dishMap[Array.isArray(scope.row.ref_id)?scope.row.ref_id[0]:scope.row.ref_id].dish_image.split(',')[0]]"
								:src="dishMap[Array.isArray(scope.row.ref_id)?scope.row.ref_id[0]:scope.row.ref_id].dish_image.split(',')[0]" style="width:100px;height:100px"></el-image>
							<el-image v-else preview-teleported
								:preview-src-list="[$config.url+dishMap[Array.isArray(scope.row.ref_id)?scope.row.ref_id[0]:scope.row.ref_id].dish_image.split(',')[0]]"
								:src="$config.url+dishMap[Array.isArray(scope.row.ref_id)?scope.row.ref_id[0]:scope.row.ref_id].dish_image.split(',')[0]" style="width:100px;height:100px">
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
					prop="nickname"
					label="用户名">
					<template #default="scope">
						{{scope.row.nickname}}
					</template>
				</el-table-column>
				<el-table-column label="评价内容" min-width="140" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<span v-html="scope.row.content"></span>
					</template>
				</el-table-column>
				<el-table-column label="评分" min-width="120" :resizable='true' :sortable='true' align="left" header-align="left" prop="score">
					<template #default="scope">
						<el-rate :model-value="Number(scope.row.score||0)" disabled />
					</template>
				</el-table-column>
				<el-table-column label="评价时间" min-width="140" :resizable='true' :sortable='true' align="left" header-align="left" prop="addtime">
					<template #default="scope">
						{{scope.row.addtime}}
					</template>
				</el-table-column>
				<el-table-column label="回复内容" min-width="140" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<span v-html="stripReplyPrefix(scope.row.reply)"></span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="300" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="edit_btn" type="primary" @click="editClick(scope.row.id)" v-if=" btnAuth('dish_review','修改')">
							修改						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)" >
							删除						</el-button>
						<el-button class="operate_btn" v-if="btnAuth('dish_review','查看')" type="warning" @click="replyClick(scope.row.id)">
							回复
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

	const tableName = 'dish_review'
	const formName = '美食信息评价表'
	const route = useRoute()
	const canAdd = computed(() => route.query.from === 'orders')
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
	const searchQuery = ref({ score: '' })
	const getRawRefId = () => {
		const raw = Array.isArray(route.query.ref_id) ? route.query.ref_id[0] : route.query.ref_id
		if (raw !== undefined && raw !== null && raw !== '' && raw !== 'undefined' && raw !== 'null') return raw
		return ''
	}
	const hasRefContext = computed(() => !!getRawRefId())
	const filterByCurrentDish = ref(false)
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
		const rawRefId = getRawRefId()
		if (filterByCurrentDish.value && rawRefId) {
			params['ref_id'] = rawRefId
		}
		if (searchQuery.value.score) {
			params['score'] = searchQuery.value.score
		}
		context.$http({
			url: `${tableName}/page`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			const rows = res.data.data.list || []
			list.value = rows
			total.value = Number(res.data.data.total)
			const ids = Array.from(new Set(rows.map(r => {
				const v = Array.isArray(r.ref_id) ? r.ref_id[0] : r.ref_id
				return v
			}).filter(v => !(v === undefined || v === null || v === '' || v === 'undefined' || v === 'null'))))
			const reqs = ids.filter(id => !dishMap.value[id]).map(id => {
				return context.$http.get(`dish_info/info/${id}`).then(resp => {
					dishMap.value[id] = resp.data.data || {}
				})
			})
			Promise.all(reqs)
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
		ElMessageBox.confirm('是否删除所选评价', '提示', {
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
	const dishMap = ref({})
	const getDishName = (id) => {
		const rid = Array.isArray(id) ? id[0] : id
		if (rid === undefined || rid === null || rid === '' || rid === 'undefined' || rid === 'null') return ''
		return (dishMap.value[rid] && (dishMap.value[rid].dish_name || dishMap.value[rid].dishName)) || ''
	}
	//搜索
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	const refreshClick = () => {
		getList()
	}
	const resetClick = () => {
		searchQuery.value.score = ''
		filterByCurrentDish.value = false
		searchClick()
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
	const replyClick=(id=null)=>{
		formRef.value.init(id,'reply')
	}
	const stripReplyPrefix = (h) => {
		let s = String(h ?? '')
		return s.replace(/^\s*(管理员：|员工：|餐厅回复：)\s*/, '')
	}
	//初始化
	const init = () => {
		filterByCurrentDish.value = !!getRawRefId()
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
