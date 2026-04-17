<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="getList" size="small">刷新</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" @click="addClick" v-if="btnAuth('menu','新增')">
						新增
					</el-button>
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('menu','删除')">
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
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="id"
					label="ID">
					<template #default="scope">
						{{ scope.row.id }}
					</template>
				</el-table-column>
				<el-table-column label="菜单JSON" min-width="240" :resizable='true' :sortable='false' align="left" header-align="left">
					<template #default="scope">
						{{ previewJson(scope.row.menujson) }}
					</template>
				</el-table-column>
				<el-table-column label="操作" width="300" :resizable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if=" btnAuth('menu','查看')" @click="viewClick(scope.row)">
							查看
						</el-button>
						<el-button class="edit_btn" type="primary" @click="editClick(scope.row)" v-if=" btnAuth('menu','修改')">
							修改
						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)"  v-if="btnAuth('menu','删除')">
							删除
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
		<el-dialog v-model="jsonDialogVisible" title="菜单JSON" width="70%">
			<el-input v-model="jsonText" type="textarea" :rows="24" />
			<template #footer>
				<span class="formModel_btn_box">
					<el-button class="cancel_btn" @click="jsonDialogVisible=false">取消</el-button>
					<el-button class="edit_btn" type="primary" v-if="editMode" @click="saveJson">保存</el-button>
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
		onMounted
	} from 'vue'
	import {
		ElMessageBox
	} from 'element-plus'
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const tableName = 'menu'
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
			table.value.toggleRowSelection(row)
		})
	}
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		params['sort'] = 'id'
		params['order'] = 'desc'
		context.$http({
			url: `${tableName}/list`,
			method: 'get',
			params: {
				page: params.page,
				limit: params.limit,
				sort: 'id'
			}
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = Number(res.data.data.total || 1)
		})
	}
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
		ElMessageBox.confirm(`是否删除选中菜单`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: ids.value
			}).then(res => {
				getList()
			})
		}).catch(_ => {})
	}
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
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
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	const addClick = ()=>{
		jsonText.value = ''
		editMode.value = true
		jsonDialogVisible.value = true
	}
	const viewClick = (row)=>{
		jsonText.value = row.menujson
		editMode.value = false
		jsonDialogVisible.value = true
	}
	const editClick = (row)=>{
		jsonText.value = row.menujson
		editMode.value = true
		jsonDialogVisible.value = true
		editId.value = row.id
	}
	const jsonDialogVisible = ref(false)
	const editMode = ref(false)
	const jsonText = ref('')
	const editId = ref(0)
	const saveJson = ()=>{
		let parsed = null
		try {
			parsed = JSON.parse(jsonText.value || '[]')
		} catch(e) {
			context?.$toolUtil.message('JSON格式错误', 'error')
			return
		}
		context.$http({
			url: `${tableName}/update`,
			method: 'post',
			data: {
				id: editId.value || 1,
				menujson: JSON.stringify(parsed)
			}
		}).then(res => {
			context?.$toolUtil.message('保存成功', 'success', ()=>{
				jsonDialogVisible.value = false
				getList()
			})
		})
	}
	const previewJson = (text)=>{
		if (!text) return ''
		const s = String(text)
		return s.length > 100 ? s.slice(0, 100) + '...' : s
	}
	onMounted(()=>{
		getList()
	})
	</script>
	<style lang="scss" scoped>
	</style>
