<template>
	<div>
		<div class="center_view">
			<div class="list_search_view" style="display:flex;justify-content:flex-end;">
				<el-form :model="searchQuery" class="search_form">
					<div class="search_view">
					<div class="search_label">账号：</div>
						<div class="search_box">
						<el-input class="search_inp" v-model="searchQuery.login_name" placeholder="账号" clearable />
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
				<el-table-column min-width="160" :resizable="true" :sortable="true" align="left" header-align="left" prop="login_name" label="账号">
					<template #default="scope">{{ getUserAccount(scope.row) }}</template>
				</el-table-column>
				<el-table-column min-width="120" :resizable="true" :sortable="true" align="left" header-align="left" prop="messageStatus" label="消息状态">
					<template #default="scope">
						<el-tag v-if="isUserWaiting(scope.row)" type="danger">待回复</el-tag>
						<el-tag v-else type="success">已回复</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="180" :resizable="true" :sortable="true" align="left" header-align="left" prop="lastMessageTime" label="最后会话时间">
					<template #default="scope">{{ getLastMessageTime(scope.row) }}</template>
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

		<el-drawer v-model="detailVisible" size="50%" class="chat-drawer" :modal="true" :lock-scroll="true" modal-class="chat-drawer-modal" @opened="onDetailOpened" :with-header="false">
			<div style="display: flex; flex-direction: column; height: 100%; overflow: hidden; width: 100%;">
				<div v-if="currentTicket" class="chat-header-admin" style="display:flex; justify-content: space-between; align-items: center; gap: 12px; flex-shrink: 0;">
					<div style="color: rgba(0,0,0,0.7);">
						<div>用户：{{ getUserNickname() }}</div>
					</div>
				</div>

				<div class="chat-container">
					<div class="chat-messages" ref="messageWrapRef" v-loading="messageLoading">
						<el-empty v-if="!messageLoading && (!messageList || messageList.length === 0)" description="暂无会话记录" />
						<div v-for="msg in messageList" :key="msg.id" style="margin-bottom: 12px;">
							<div :style="{ display: 'flex', justifyContent: isUserTicketMessage(msg) ? 'flex-start' : 'flex-end' }">
								<div :style="{ display: 'flex', flexDirection: isUserTicketMessage(msg) ? 'row' : 'row-reverse', alignItems: 'flex-start', gap: '10px', maxWidth: '100%' }">
									<img :src="getMessageAvatar(msg)" style="width: 34px; height: 34px; border-radius: 50%; object-fit: cover; border: 1px solid rgba(0,0,0,0.06); background: #fff;" />
									<div :style="{ display: 'flex', flexDirection: 'column', alignItems: isUserTicketMessage(msg) ? 'flex-start' : 'flex-end', maxWidth: '80%' }">
										<div :style="{ display: 'flex', justifyContent: isUserTicketMessage(msg) ? 'flex-start' : 'flex-end', color: 'rgba(0,0,0,0.45)', fontSize: '12px', gap: '10px' }">
											<div>{{ getMessageSenderName(msg) }}</div>
											<div>{{ msg.addtime ?? msg.add_time ?? msg.addTime ?? '' }}</div>
										</div>
										<div :style="{ display: 'inline-block', width: 'fit-content', maxWidth: '100%', marginTop: '6px', whiteSpace: 'pre-wrap', lineHeight: '1.7', padding: '10px 12px', borderRadius: '10px', background: isUserTicketMessage(msg) ? '#ffffff' : '#e6f4ff', border: '1px solid rgba(0,0,0,0.06)' }">{{ msg.content }}</div>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="chat-input">
						<div class="input-area">
							<el-input
								ref="replyInputRef"
								v-model="replyContent"
								type="textarea"
								:rows="3"
								placeholder="输入回复内容"
								:disabled="!currentTicket"
								@keydown.enter.exact.prevent="sendReply"
								@keydown.ctrl.enter.prevent="sendReply"
							/>
							<el-button
								type="primary"
								:disabled="!replyContent.trim() || !currentTicket"
								:loading="replyLoading"
								@click="sendReply"
								class="send-btn"
							>发送</el-button>
						</div>
					</div>
				</div>
			</div>
		</el-drawer>
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
		onMounted,
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
	const listPollingTimer = ref(null)
	const backendUnavailable = ref(false)
	const lastMessageIdByTicket = new Map()

	const listQuery = ref({
		page: 1,
		limit: 10,
		sort: 'id',
		order: 'asc'
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
		params.order = 'asc'
		if (searchQuery.value.login_name) {
			params.login_name = searchQuery.value.login_name
		}
		context.$http({
			url: 'support/ticket/page',
			method: 'get',
			params
		}).then(res => {
			const page = res?.data?.data || {}
			list.value = page.list || []
			total.value = Number(page.total || 0)
			handleNewMessages(list.value)
			backendUnavailable.value = false
		}).catch(() => {
			backendUnavailable.value = true
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
    const operatorInfoCache = ref({})
    const operatorNameCache = ref({})
    const operatorFetchPending = ref({})
	const messageList = ref([])
	const messageLoading = ref(false)
	const messageWrapRef = ref(null)
	const replyContent = ref('')
	const replyLoading = ref(false)
	const messagePollingTimer = ref(null)
	const autoStick = ref(true)
	const stickThreshold = 30
	const replyInputRef = ref(null)

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
	const parseTicketMessages = (row) => {
		if (!row) return []
		const raw = row.messages ?? row.message ?? row.msgs
		if (!raw) return []
		if (Array.isArray(raw)) return raw
		if (typeof raw !== 'string') return []
		try {
			const parsed = JSON.parse(raw)
			return Array.isArray(parsed) ? parsed : []
		} catch (_) {
			return []
		}
	}
	const getLastMessage = (row) => {
		const messages = parseTicketMessages(row)
		if (!messages.length) return null
		return messages[messages.length - 1]
	}
	const getLastMessageTime = (row) => {
		const last = getLastMessage(row)
		if (last) {
			return last.addtime ?? last.add_time ?? last.addTime ?? ''
		}
		return row?.addtime ?? row?.add_time ?? row?.addTime ?? ''
	}
	const getMessageId = (msg) => {
		if (!msg) return null
		const v = msg.id ?? msg.messageId ?? msg.message_id ?? msg.ID ?? msg.msgId ?? msg.msg_id
		return v != null ? String(v) : null
	}
	const isUserWaiting = (row) => {
		const last = getLastMessage(row)
		if (!last) return false
		const role = String(last.senderRole ?? last.sender_role ?? last.senderrole ?? '').trim().toLowerCase()
		if (role === '用户' || role === 'user') return true
		const senderIdRaw = last.senderId ?? last.sender_id ?? last.senderID ?? last.senderid
		const senderId = senderIdRaw != null ? String(senderIdRaw) : null
		const userId = row ? (row.userId ?? row.user_id ?? row.userid) : null
		if (senderId && userId != null && senderId === String(userId)) return true
		return false
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
	const getUserAccount = (row) => {
		const v = row ? (row.userId ?? row.user_id ?? row.userid) : null
		if (v == null || v === '') return ''
		const id = String(v)
		const cached = userInfoCache.value[id]
		if (cached) {
			const account = String(cached.login_name ?? cached.loginName ?? cached.name ?? '').trim()
			return account || ''
		}
		ensureUserInfo(id)
		return ''
	}
	const handleNewMessages = (rows) => {
		(rows || []).forEach(row => {
			const ticketId = row?.id
			if (ticketId == null) return
			const last = getLastMessage(row)
			const msgId = getMessageId(last)
			if (!msgId) return
			const key = String(ticketId)
			const prev = lastMessageIdByTicket.get(key)
			if (prev && prev === msgId) return
			lastMessageIdByTicket.set(key, msgId)
			if (prev && isUserWaiting(row)) {
				const account = getUserAccount(row) || '用户'
				context?.$toolUtil.notify('新会话消息', `用户 ${account} 有新消息`, 'warning')
			}
		})
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
    const getMessageSenderName = (msg) => {
        if (isUserTicketMessage(msg)) return getUserNickname()
        const role = String(msg ? (msg.senderRole ?? msg.sender_role ?? msg.senderrole ?? '') : '').trim()
        const id = getMessageSenderId(msg)
        const key = `${role}:${id || ''}`
        const cached = operatorNameCache.value[key]
        if (cached) return cached
        if (role && id) ensureOperatorInfo(role, id)
        return role || '客服'
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
    const ensureOperatorInfo = (role, operatorId) => {
        if (!role || !operatorId) return Promise.resolve(null)
        const key = `${role}:${operatorId}`
        const cached = operatorInfoCache.value[key]
        if (cached) return Promise.resolve(cached)
        if (operatorFetchPending.value[key]) return Promise.resolve(null)
        operatorFetchPending.value[key] = true
        const table = role === '管理员' ? 'admin' : (role === '员工' ? 'staff' : '')
        if (!table) {
            operatorFetchPending.value[key] = false
            return Promise.resolve(null)
        }
        return context.$http({
            url: `${table}/info/${operatorId}`,
            method: 'get'
        }).then(res => {
            const data = res?.data?.data || null
            if (data) {
                operatorInfoCache.value = {
                    ...operatorInfoCache.value,
                    [key]: data
                }
                let name = ''
                if (table === 'admin') {
                    name = String(data.login_name ?? data.loginName ?? data.name ?? '').trim()
                    if (!name) name = 'admin'
                } else {
                    name = String(data.name ?? data.login_name ?? data.loginName ?? '').trim()
                    if (!name) name = '员工'
                }
                operatorNameCache.value = {
                    ...operatorNameCache.value,
                    [key]: name
                }
            }
            return data
        }).finally(() => {
            operatorFetchPending.value[key] = false
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
					if (autoStick.value) {
						el.scrollTop = el.scrollHeight
					}
				}
			})
		}).catch(() => {
		}).finally(() => {
			messageLoading.value = false
		})
	}

	const handleMessageScroll = () => {
		const el = messageWrapRef.value
		if (!el) return
		const nearBottom = el.scrollTop + el.clientHeight >= el.scrollHeight - stickThreshold
		autoStick.value = nearBottom
	}

	const stopMessagePolling = () => {
		if (messagePollingTimer.value) {
			clearInterval(messagePollingTimer.value)
			messagePollingTimer.value = null
		}
	}
	const stopListPolling = () => {
		if (listPollingTimer.value) {
			clearInterval(listPollingTimer.value)
			listPollingTimer.value = null
		}
	}
	const startListPolling = () => {
		if (listPollingTimer.value) return
		listPollingTimer.value = setInterval(() => {
			if (listLoading.value) return
			getList()
		}, 4000)
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
			autoStick.value = true
			await loadMessages(currentTicket.value.id)
			getList()
		} finally {
			replyLoading.value = false
		}
	}

	watch(detailVisible, (v) => {
		if (v) {
			startMessagePolling()
			nextTick(() => {
				const el = messageWrapRef.value
				if (el) el.addEventListener('scroll', handleMessageScroll)
				autoStick.value = true
			})
		} else {
			stopMessagePolling()
			const el = messageWrapRef.value
			if (el) el.removeEventListener('scroll', handleMessageScroll)
		}
	})
	const onDetailOpened = () => {
		nextTick(() => {
			replyInputRef.value?.focus && replyInputRef.value.focus()
		})
	}

	onUnmounted(() => {
		stopMessagePolling()
		stopListPolling()
	})
	onMounted(() => {
		getList()
		startListPolling()
	})
</script>
<style lang="scss" scoped>
.chat-drawer {
  :deep(.el-drawer__body) {
    display: flex;
    flex-direction: column;
    padding: 0 !important;
    background: #f9fafb;
    overflow: hidden;
  }
}
.chat-container {
  display: flex;
  flex-direction: column;
  background: #f9fafb;
  flex: 1;
  min-height: 0;
  overflow: hidden;
}
.chat-header-admin {
  z-index: 3;
  background: #f9fafb;
  padding: 12px 20px;
  border-bottom: 1px solid rgba(0,0,0,0.05);
}
.chat-messages {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: 20px;
  background: #f9fafb;
}
.chat-input {
  padding: 12px 16px;
  background: #f9fafb;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  z-index: 2;
}
.input-area {
  display: flex;
  align-items: center;
  gap: 8px;
}
.send-btn {
  height: 36px;
  border-radius: 18px;
  padding: 0 20px;
}
.chat-drawer-modal {
  background: rgba(0,0,0,0.12) !important;
}
</style>
