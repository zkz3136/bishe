<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form">
					<div class="search_view">
						<div class="search_label">用户ID：</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.userId" placeholder="用户ID" clearable />
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" size="small" @click="searchClick()">搜索</el-button>
					</div>
				</el-form>
			</div>

			<el-table
				v-loading="listLoading"
				border
				:stripe="false"
				@selection-change="handleSelectionChange"
				ref="table"
				v-if="btnAuth(tableName,'查看')"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable="true" align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable="true" align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="120" :resizable="true" :sortable="true" align="left" header-align="left" prop="id" label="会话ID">
					<template #default="scope">{{ scope.row.id }}</template>
				</el-table-column>
				<el-table-column min-width="140" :resizable="true" :sortable="true" align="left" header-align="left" prop="userId" label="用户ID">
					<template #default="scope">{{ scope.row.userId ?? scope.row.user_id }}</template>
				</el-table-column>
				<el-table-column min-width="180" :resizable="true" :sortable="true" align="left" header-align="left" prop="addtime" label="创建时间">
					<template #default="scope">{{ scope.row.addtime ?? scope.row.add_time ?? scope.row.addTime ?? '' }}</template>
				</el-table-column>
				<el-table-column label="操作" width="220" :resizable="true" :sortable="true" align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if="btnAuth(tableName,'查看')" @click.stop="openDetail(scope.row)">查看聊天</el-button>
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

		<el-dialog v-model="detailVisible" title="会话详情" width="720px" destroy-on-close>
			<div v-if="currentTicket" style="display:flex; justify-content: space-between; align-items: center; margin-bottom: 12px; gap: 12px;">
				<div style="color: rgba(0,0,0,0.7);">
					<div>用户：{{ getUserNickname() }}</div>
					<div style="margin-top: 4px;">用户ID：{{ getTicketUserId() }}</div>
					<div style="margin-top: 4px;">创建时间：{{ currentTicket.addtime ?? currentTicket.add_time ?? currentTicket.addTime ?? '' }}</div>
				</div>
				<div style="display:flex; gap: 10px; align-items: center;">
					<el-button size="small" @click="loadMessages(currentTicket.id)">刷新</el-button>
				</div>
			</div>

			<div
				ref="messageWrapRef"
				v-loading="messageLoading"
				style="border: 1px solid rgba(0,0,0,0.08); border-radius: 10px; padding: 12px; height: 380px; overflow: auto; background: #fafafa;"
			>
				<el-empty v-if="!messageLoading && (!messageList || messageList.length === 0)" description="暂无会话记录" />
				<div v-for="msg in messageList" :key="msg.id" style="margin-bottom: 12px;">
					<div
						:style="{
							display: 'flex',
							justifyContent: isUserTicketMessage(msg) ? 'flex-start' : 'flex-end'
						}"
					>
						<div
							:style="{
								display: 'flex',
								flexDirection: isUserTicketMessage(msg) ? 'row' : 'row-reverse',
								alignItems: 'flex-start',
								gap: '10px',
								maxWidth: '100%'
							}"
						>
							<img
								:src="getMessageAvatar(msg)"
								style="width: 34px; height: 34px; border-radius: 50%; object-fit: cover; border: 1px solid rgba(0,0,0,0.06); background: #fff;"
							/>
							<div
								:style="{
									display: 'flex',
									flexDirection: 'column',
									alignItems: isUserTicketMessage(msg) ? 'flex-start' : 'flex-end',
									maxWidth: '80%'
								}"
							>
								<div
									:style="{
										display: 'flex',
										justifyContent: isUserTicketMessage(msg) ? 'flex-start' : 'flex-end',
										color: 'rgba(0,0,0,0.45)',
										fontSize: '12px',
										gap: '10px'
									}"
								>
									<div>{{ getMessageSenderRole(msg) }}</div>
									<div>{{ msg.addtime ?? msg.add_time ?? msg.addTime ?? '' }}</div>
								</div>
								<div
									:style="{
										display: 'inline-block',
										width: 'fit-content',
										maxWidth: '100%',
										marginTop: '6px',
										whiteSpace: 'pre-wrap',
										lineHeight: '1.7',
										padding: '10px 12px',
										borderRadius: '10px',
										background: isUserTicketMessage(msg) ? '#ffffff' : '#e6f4ff',
										border: '1px solid rgba(0,0,0,0.06)'
									}"
								>{{ msg.content }}</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div style="margin-top: 12px;">
				<el-input
					v-model="replyContent"
					type="textarea"
					:rows="3"
					placeholder="输入回复内容"
					:disabled="!currentTicket"
				/>
				<div style="display:flex; justify-content: flex-end; margin-top: 10px;">
					<el-button
						type="primary"
						:disabled="!replyContent.trim() || !currentTicket"
						:loading="replyLoading"
						@click="sendReply"
					>发送</el-button>
				</div>
			</div>
			<template #footer>
				<el-button @click="detailVisible=false">关闭</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
	import {
		ref,
		computed,
		getCurrentInstance,
		nextTick,
		watch,
		onUnmounted,
	} from 'vue'
	// import { ElMessageBox } from 'element-plus'
	import { useStore } from 'vuex'
	import default_avatar from '@/assets/img/avatar.png'

	const context = getCurrentInstance()?.appContext.config.globalProperties
	const store = useStore()

	const tableName = 'support_ticket'
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
		if (searchQuery.value.userId) {
			params.userId = searchQuery.value.userId
		}
		context.$http({
			url: 'support/ticket/page',
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

	const detailVisible = ref(false)
	const currentTicket = ref(null)
	const currentTicketUser = ref(null)
	const userInfoCache = ref({})
	const messageList = ref([])
	const messageLoading = ref(false)
	const messageWrapRef = ref(null)
	const replyContent = ref('')
	const replyLoading = ref(false)
	const messagePollingTimer = ref(null)

	const staffAvatar = computed(() => store.getters['user/avatar'])

	const normalizeFirstUrl = (v) => {
		if (!v) return ''
		const s = String(v).trim()
		if (!s) return ''
		return s.split(',')[0].trim()
	}

	const resolveFileUrl = (v) => {
		const first = normalizeFirstUrl(v)
		if (!first) return ''
		if (first.startsWith('http')) return first
		return `${context?.$config?.url || ''}${first}`
	}

	const getUserNickname = () => {
		const u = currentTicketUser.value
		const nickname = String(u ? (u.login_name ?? u.loginName ?? u.name ?? '') : '').trim()
		return nickname || '用户'
	}

	const getUserAvatar = () => {
		const u = currentTicketUser.value
		const url = resolveFileUrl(u ? u.avatar : '')
		return url || default_avatar
	}

	const getMessageAvatar = (msg) => {
		if (isUserTicketMessage(msg)) return getUserAvatar()
		return staffAvatar.value || default_avatar
	}

	const getTicketUserId = () => {
		const t = currentTicket.value
		const v = t ? (t.userId ?? t.user_id ?? t.userid) : null
		return v != null ? String(v) : null
	}

	const getMessageSenderId = (msg) => {
		const v = msg ? (msg.senderId ?? msg.sender_id ?? msg.senderID ?? msg.senderid) : null
		return v != null ? String(v) : null
	}

	const getMessageSenderRole = (msg) => {
		if (isUserTicketMessage(msg)) return getUserNickname()
		const role = String(msg ? (msg.senderRole ?? msg.sender_role ?? msg.senderrole ?? '') : '').trim()
		if (role) return role
		return '客服'
	}

	const isUserTicketMessage = (msg) => {
		const senderId = getMessageSenderId(msg)
		const ticketUserId = getTicketUserId()
		if (ticketUserId != null && senderId != null && ticketUserId === senderId) return true
		const role = String(msg ? (msg.senderRole ?? msg.sender_role ?? msg.senderrole ?? '') : '').trim().toLowerCase()
		if (role === '用户' || role === 'user') return true
		return false
	}

	const openDetail = (row) => {
		currentTicket.value = row
		detailVisible.value = true
		replyContent.value = ''
		currentTicketUser.value = null
		const userId = getTicketUserId()
		if (userId) ensureUserInfo(userId)
		loadMessages(row?.id)
	}

	const ensureUserInfo = (userId) => {
		if (!userId) return Promise.resolve(null)
		const cached = userInfoCache.value[userId]
		if (cached) {
			currentTicketUser.value = cached
			return Promise.resolve(cached)
		}
		return context.$http({
			url: `user/info/${userId}`,
			method: 'get'
		}).then(res => {
			const data = res?.data?.data || null
			if (data) {
				userInfoCache.value = {
					...userInfoCache.value,
					[userId]: data
				}
				if (getTicketUserId() === String(userId)) {
					currentTicketUser.value = data
				}
			}
			return data
		}).catch(() => null)
	}

	const loadMessages = (ticketId) => {
		if (!ticketId) return
		messageLoading.value = true
		const userId = getTicketUserId()
		Promise.all([
			context.$http({
				url: `support/ticket/${ticketId}/messages`,
				method: 'get'
			}),
			userId ? ensureUserInfo(userId) : Promise.resolve(null)
		]).then(([msgRes]) => {
			messageList.value = msgRes?.data?.data || []
			nextTick(() => {
				const el = messageWrapRef.value
				if (el && el.scrollHeight != null) {
					el.scrollTop = el.scrollHeight
				}
			})
		}).finally(() => {
			messageLoading.value = false
		})
	}

	const stopMessagePolling = () => {
		if (messagePollingTimer.value) {
			clearInterval(messagePollingTimer.value)
			messagePollingTimer.value = null
		}
	}

	const startMessagePolling = () => {
		if (messagePollingTimer.value) return
		messagePollingTimer.value = setInterval(() => {
			if (!detailVisible.value || !currentTicket.value?.id) return
			loadMessages(currentTicket.value.id)
		}, 3000)
	}

	const sendReply = async () => {
		if (!currentTicket.value?.id) return
		const content = (replyContent.value || '').trim()
		if (!content) return
		replyLoading.value = true
		try {
			await context.$http({
				url: `support/ticket/${currentTicket.value.id}/staffReply`,
				method: 'post',
				data: { content }
			})
			replyContent.value = ''
			await loadMessages(currentTicket.value.id)
			getList()
		} finally {
			replyLoading.value = false
		}
	}

	// 关闭操作已移除
	getList()

	watch(detailVisible, (v) => {
		if (v) {
			startMessagePolling()
		} else {
			stopMessagePolling()
		}
	})

	onUnmounted(() => {
		stopMessagePolling()
	})
</script>
