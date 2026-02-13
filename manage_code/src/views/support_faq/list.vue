<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form">
					<div class="search_view">
						<div class="search_label">问题：</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.question" placeholder="问题" clearable />
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">分类：</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.category" placeholder="分类" clearable />
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">启用：</div>
						<div class="search_box">
							<el-select class="search_sel" clearable v-model="searchQuery.enabled" placeholder="是否启用">
								<el-option label="启用" :value="1" />
								<el-option label="禁用" :value="0" />
							</el-select>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" size="small" @click="searchClick()">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="primary" v-if="btnAuth(tableName,'新增')" @click="addClick">新增</el-button>
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" v-if="btnAuth(tableName,'删除')" @click="delClick(null)">删除</el-button>
				</div>
			</div>

			<el-table
				v-loading="listLoading"
				border
				:stripe="false"
				ref="table"
				v-if="btnAuth(tableName,'查看')"
				:data="list"
				@row-click="listChange"
				@selection-change="handleSelectionChange">
				<el-table-column :resizable="true" align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable="true" align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="220" :resizable="true" :sortable="true" align="left" header-align="left" prop="question" label="问题">
					<template #default="scope">{{ scope.row.question }}</template>
				</el-table-column>
				<el-table-column min-width="120" :resizable="true" :sortable="true" align="left" header-align="left" prop="category" label="分类">
					<template #default="scope">{{ scope.row.category || '' }}</template>
				</el-table-column>
				<el-table-column min-width="90" :resizable="true" :sortable="true" align="left" header-align="left" prop="enabled" label="启用">
					<template #default="scope">{{ scope.row.enabled == 1 ? '是' : '否' }}</template>
				</el-table-column>
				<el-table-column min-width="170" :resizable="true" :sortable="true" align="left" header-align="left" prop="addtime" label="创建时间">
					<template #default="scope">{{ scope.row.addtime || '' }}</template>
				</el-table-column>
				<el-table-column label="操作" width="280" :resizable="true" :sortable="true" align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if="btnAuth(tableName,'查看')" @click.stop="infoClick(scope.row)">详情</el-button>
						<el-button class="edit_btn" type="primary" v-if="btnAuth(tableName,'修改')" @click.stop="editClick(scope.row)">修改</el-button>
						<el-button class="del_btn" type="danger" v-if="btnAuth(tableName,'删除')" @click.stop="delClick(scope.row.id)">删除</el-button>
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
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange" />
		</div>

		<el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" destroy-on-close>
			<el-form :model="form" label-width="90px">
				<el-form-item label="问题">
					<el-input v-model="form.question" placeholder="问题" :disabled="dialogMode==='view'" />
				</el-form-item>
				<el-form-item label="关键词">
					<el-input v-model="form.keywords" placeholder="关键词（逗号分隔）" :disabled="dialogMode==='view'" />
				</el-form-item>
				<el-form-item label="分类">
					<el-input v-model="form.category" placeholder="分类" :disabled="dialogMode==='view'" />
				</el-form-item>
				<el-form-item label="启用">
					<el-switch v-model="enabledSwitch" :disabled="dialogMode==='view'" />
				</el-form-item>
				<el-form-item label="答案">
					<el-input v-model="form.answer" type="textarea" :rows="6" placeholder="答案" :disabled="dialogMode==='view'" />
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="dialogVisible=false">关闭</el-button>
				<el-button type="primary" v-if="dialogMode!=='view'" :loading="saveLoading" @click="saveClick">保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		nextTick,
		watch,
	} from 'vue'
	import { ElMessageBox } from 'element-plus'

	const context = getCurrentInstance()?.appContext.config.globalProperties
	const tableName = 'support_faq'
	const layouts = ref(['total', 'sizes', 'prev', 'pager', 'next', 'jumper'])

	const btnAuth = (e, a) => {
		return context?.$toolUtil.isAuth(e, a)
	}

	const list = ref([])
	const table = ref(null)
	const total = ref(0)
	const listLoading = ref(false)
	const selRows = ref([])

	const listQuery = ref({
		page: 1,
		limit: 10,
		sort: 'id',
		order: 'desc'
	})
	const searchQuery = ref({})

	const listChange = (row) => {
		nextTick(() => {
			table.value?.toggleRowSelection(row)
		})
	}

	const handleSelectionChange = (rows) => {
		selRows.value = rows || []
	}

	const getList = () => {
		listLoading.value = true
		const params = JSON.parse(JSON.stringify(listQuery.value))
		params.sort = 'id'
		params.order = 'desc'
		if (searchQuery.value.question) {
			params.question = '%' + searchQuery.value.question + '%'
		}
		if (searchQuery.value.category) {
			params.category = '%' + searchQuery.value.category + '%'
		}
		if (searchQuery.value.enabled === 0 || searchQuery.value.enabled === 1) {
			params.enabled = searchQuery.value.enabled
		}
		context.$http({
			url: 'support/faq/page',
			method: 'get',
			params
		}).then(res => {
			const page = res?.data?.data || {}
			list.value = page.list || []
			total.value = Number(page.total || 0)
		}).finally(() => {
			listLoading.value = false
		})
	}

	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}

	const sizeChange = (size) => {
		listQuery.value.limit = size
		listQuery.value.page = 1
		getList()
	}

	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}

	const dialogVisible = ref(false)
	const dialogTitle = ref('')
	const dialogMode = ref('add')
	const saveLoading = ref(false)
	const form = ref({
		id: null,
		question: '',
		keywords: '',
		answer: '',
		category: '',
		enabled: 1
	})

	const enabledSwitch = ref(true)
	watch(enabledSwitch, (v) => {
		form.value.enabled = v ? 1 : 0
	})
	watch(() => form.value.enabled, (v) => {
		enabledSwitch.value = Number(v) === 1
	})

	const resetForm = () => {
		form.value = {
			id: null,
			question: '',
			keywords: '',
			answer: '',
			category: '',
			enabled: 1
		}
	}

	const addClick = () => {
		dialogMode.value = 'add'
		dialogTitle.value = '新增FAQ'
		resetForm()
		dialogVisible.value = true
	}

	const infoClick = (row) => {
		dialogMode.value = 'view'
		dialogTitle.value = 'FAQ详情'
		form.value = JSON.parse(JSON.stringify(row || {}))
		if (form.value.enabled == null) form.value.enabled = 1
		dialogVisible.value = true
	}

	const editClick = (row) => {
		dialogMode.value = 'edit'
		dialogTitle.value = '修改FAQ'
		form.value = JSON.parse(JSON.stringify(row || {}))
		if (form.value.enabled == null) form.value.enabled = 1
		dialogVisible.value = true
	}

	const saveClick = () => {
		if (!form.value.question || !form.value.answer) {
			context?.$toolUtil.message('问题和答案不能为空', 'warning')
			return
		}
		saveLoading.value = true
		const isEdit = !!form.value.id
		context.$http({
			url: isEdit ? 'support/faq/update' : 'support/faq/save',
			method: 'post',
			data: JSON.parse(JSON.stringify(form.value))
		}).then(() => {
			context?.$toolUtil.message('保存成功', 'success', () => {
				dialogVisible.value = false
				getList()
			})
		}).finally(() => {
			saveLoading.value = false
		})
	}

	const delClick = (id) => {
		let ids = []
		if (id) {
			ids = [id]
		} else {
			if (selRows.value.length) {
				ids = selRows.value.map(r => r.id).filter(Boolean)
			} else {
				return
			}
		}
		ElMessageBox.confirm('是否删除选中FAQ？', '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning'
		}).then(() => {
			context.$http({
				url: 'support/faq/delete',
				method: 'post',
				data: ids
			}).then(() => {
				context?.$toolUtil.message('删除成功', 'success', () => {
					getList()
				})
			})
		}).catch(() => {})
	}

	getList()
</script>
