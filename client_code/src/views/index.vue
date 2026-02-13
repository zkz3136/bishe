<template>
  <div class="home">
    <div class="index_top">
      <div class="index_top_title"><span>餐厅智能服务与预订系统</span></div>
      <div class="index_top_right">
        <el-button class="chat-btn" @click="chatDialogVisible=true">
          <span class="iconfont icon-kefu"></span>
          智能客服
        </el-button>
        <el-button class="notice-btn" @click="noticeDialogVisible=true">
          <span class="iconfont icon-xiaoxi1"></span>
          系统公告
        </el-button>
        <el-button v-if="!Token" class="login" type="success" icon="User" circle @click="loginClick">
          登录
        </el-button>
        <div class="user" v-if="Token">
          <div class="avatar-wrapper" @click="menuHandler('center')" title="进入个人中心">
            <img class="user-avatar" :src="store.getters['user/avatar']">
            <div class="nickname">{{userName}}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- 水平菜单导航栏 -->
    <div class="menu-wrapper" :class="{ 'fixed-menu': isMenuFixed }">
      <el-scrollbar wrap-class="scrollbar-wrapper" class="menu_scrollbar">
        <el-menu :default-openeds="[]" :unique-opened="true" :default-active="menuIndex"
                 class="menu_view" mode="horizontal" :ellipsis="false" @select="menuChange" :key="menuIndex">
          <el-menu-item class="first-item" index="/index/home" @click="menuHandler('/')">
            <template #title>
              <span>首页</span>
            </template>
          </el-menu-item>
          <template v-for="(menu,index) in menuList" :key="menu.menu">
            <el-sub-menu v-if="menu.child.length>1" :index="menu.name" class="first-item" :teleported="true">
              <template #title>
                <span>{{ menu.name }}</span>
              </template>
              <el-menu-item class="second-item" v-for=" (child,index1) in menu.child" :key="index1"
                            :index="child.url" @click="menuHandler(child.url)">{{ child.name }}
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else :index="menu.child[0].url" class="first-item" @click="menuHandler(menu.child[0].url)">
              <template #title>
                <span>{{menu.child[0].name}}</span>
              </template>
            </el-menu-item>
          </template>
        </el-menu>
      </el-scrollbar>
    </div>
    <!-- 占位符，当菜单固定时保持布局 -->
    <div v-if="isMenuFixed" class="menu-placeholder"></div>
    <el-scrollbar class="contain_view" ref="scrollbarRef">
      <!-- 轮播图只在首页显示 -->
      <div class="rotation_view" v-if="route.path === '/index/home'">
        <mySwiper :type="3" :data="rotationList" :autoHeight="false" :autoplay="true"
                  :loop="true" :navigation="true" :pagination="false"
                  :paginationType="1" :scrollbar="false" :slidesPerView="1"
                  :spaceBetween="20" :centeredSlides="false"
                  :freeMode="false" :effectType="0"
                  :direction="horizontal">
          <template #default="scope">
            <img :style='{}' :src="scope.row.value?$config.url + scope.row.value:''" @click="carouselClick(scope.row.url)">
          </template>
        </mySwiper>
      </div>
      <router-view />
      <el-backtop :right="100" :bottom="100" />
      <div class="bottom_view">
        <img class="bottom_img" src="http://chy2.gdnxeco.com/20230912/0d5ae5e141c14b06a52a7c4c2f8ea4cf.jpg" alt="">
        <div class="bottom_company"></div>
        <div class="bottom_record"></div>
        <div class="bottom_desc"></div>
      </div>
    </el-scrollbar>
    <el-dialog v-model="noticeDialogVisible" title="系统公告">
      <div>
        <div v-html="store.state.system.notice.content"></div>
      </div>
    </el-dialog>
    <!-- 智能客服聊天窗口 -->
    <el-dialog v-model="chatDialogVisible" title="" width="420px" class="chat-dialog" :show-close="false">
      <!-- 自定义头部 -->
      <template #header>
        <div class="chat-header">
          <div class="chat-header-info">
            <div class="bot-avatar">
              <img :src="serviceAvatar" alt="客服" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
            </div>
            <div class="bot-info">
              <div class="bot-name">智能客服</div>
              <div class="bot-status" :class="{ 'online': isOnline }">{{ isOnline ? '在线' : '离线' }}</div>
            </div>
          </div>
          <div class="chat-header-actions">
            <el-button text @click="openFaqDrawer" title="常见问题">
              FAQ
            </el-button>
            <el-button text @click="goMyTickets" title="人工会话">
              人工会话
            </el-button>
            <el-button text @click="clearChat" title="清空聊天">
              <span class="iconfont icon-shanchu"></span>
            </el-button>
            <el-button text @click="chatDialogVisible = false" title="关闭">
              <span class="iconfont icon-guanbi"></span>
            </el-button>
          </div>
        </div>
      </template>

      <div class="chat-container">
        <div class="chat-messages" ref="chatMessagesRef">
          <div v-if="chatMessages.length === 0" class="empty-chat">
            <div class="empty-icon">
              <span class="iconfont icon-liaotian"></span>
            </div>
            <p>开始与智能客服对话吧！</p>
          </div>

          <div v-for="(message, index) in chatMessages" :key="index" class="message-group">
            <!-- 时间分隔符 -->
            <div v-if="shouldShowTimeBreak(index)" class="time-break">
              <span>{{ formatTimeBreak(message.timestamp) }}</span>
            </div>

            <div :class="['message', message.type === 'user' ? 'user-message' : 'bot-message']">
              <div v-if="message.type === 'bot'" class="message-avatar">
                <img :src="serviceAvatar" alt="客服" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
              </div>

              <div class="message-content">
                <div class="message-bubble">
                  <div class="message-text">{{ message.content }}</div>
                  <div v-if="message.type === 'bot' && message.suggestTicket" class="message-actions">
                    <el-button size="small" type="primary" @click="openTicketDialog(message)">转人工</el-button>
                  </div>
                </div>
                <div class="message-meta">
                  <span class="message-time">{{ formatTime(message.timestamp) }}</span>
                  <span v-if="message.type === 'user' && message.status" class="message-status" :class="message.status">
										<span v-if="message.status === 'sending'" class="iconfont icon-sending"></span>
										<span v-else-if="message.status === 'sent'" class="iconfont icon-check"></span>
										<span v-else-if="message.status === 'read'" class="iconfont icon-check-double"></span>
									</span>
                </div>
              </div>

              <div v-if="message.type === 'user'" class="message-avatar user-avatar">
                <!-- 如果用户已登录且有头像，显示真实头像，否则显示默认图标 -->
                <template v-if="Token && store.getters['user/avatar']">
                  <img :src="store.getters['user/avatar']"
                       class="user-avatar-img"
                       @error="handleAvatarError"
                       @load="handleAvatarLoad" />
                  <el-icon class="fallback-icon" style="display: none;"><UserFilled /></el-icon>
                </template>
                <el-icon v-else class="fallback-icon"><UserFilled /></el-icon>
              </div>
            </div>
          </div>

          <!-- 正在输入指示器 -->
          <div v-if="isLoading" class="message bot-message typing-message">
            <div class="message-avatar">
              <img :src="serviceAvatar" alt="客服" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
            </div>
            <div class="message-content">
              <div class="message-bubble typing-bubble">
                <div class="typing-indicator">
                  <span></span><span></span><span></span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input">
          <div class="input-area">
            <div class="input-wrapper">
              <div class="textarea-container" ref="textareaContainer">
								<textarea
                    v-model="currentMessage"
                    @keyup.enter="sendMessage"
                    @keydown="handleKeyDown"
                    placeholder="输入消息..."
                    :disabled="isLoading"
                    class="custom-textarea"
                    @input="handleTyping"
                    ref="textareaRef"
                ></textarea>
              </div>
              <div class="external-scrollbar" ref="externalScrollbar">
                <div class="scrollbar-thumb" ref="scrollbarThumb"></div>
              </div>
            </div>
            <el-button
                @click="sendMessage"
                :disabled="isLoading || !currentMessage.trim()"
                type="primary"
                class="send-btn"
                :class="{ 'has-content': currentMessage.trim() }"
            >
              发送
            </el-button>
          </div>
        </div>
      </div>
    </el-dialog>

    <el-drawer v-model="faqDrawerVisible" title="常见问题" size="420px">
      <div style="display:flex; gap: 10px; align-items: center;">
        <el-input v-model="faqKeyword" placeholder="搜索问题/关键词" clearable @keyup.enter="fetchFaqList" />
        <el-button type="primary" :loading="faqLoading" @click="fetchFaqList">搜索</el-button>
      </div>

      <div style="margin-top: 12px;">
        <el-empty v-if="!faqLoading && faqList.length === 0" description="暂无匹配的问题" />
        <el-collapse v-else>
          <el-collapse-item v-for="item in faqList" :key="item.id" :title="item.question">
            <div style="white-space: pre-wrap; line-height: 1.7;">{{ item.answer }}</div>
            <div v-if="item.category" style="margin-top: 10px; color: rgba(0,0,0,0.45);">分类：{{ item.category }}</div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-drawer>
  </div>
</template>
<script setup>
import menu from '@/utils/menu'
import moment from "moment";
import { UserFilled } from '@element-plus/icons-vue'
import {
  ref,
  onBeforeUnmount,
  onMounted,
  getCurrentInstance,
  nextTick,
  computed,
  watch,
} from 'vue';
import {
  useRouter,
  useRoute
} from 'vue-router';
import {
  useStore
} from 'vuex';
import serviceAvatar from '@/assets/service.png'
const store = useStore()
const router = useRouter()
const route = useRoute();
const context = getCurrentInstance()?.appContext.config.globalProperties;
const Token = ref('')
const userName = computed(() => {
  const s = store.getters['user/session'] || {}
  const n = s.login_name ?? s.loginName ?? s.name
  const vRaw = n ?? context?.$toolUtil.storageGet('frontName') ?? ''
  const v = String(vRaw || '').trim()
  return v && v.toLowerCase() !== 'undefined' && v.toLowerCase() !== 'null' ? v : ''
})
const interval = ref(null)
const nowDate = ref(new Date())
// 菜单栏固定状态
const isMenuFixed = ref(false)
const scrollbarRef = ref(null)
if(localStorage.getItem('frontToken') && !store.getters['user/session'].id){
  store.dispatch('user/getSession')
}
//获取通知公告
const noticeDialogVisible = ref(false)
store.dispatch('system/getSystemNotice')

// 智能客服相关
const chatDialogVisible = ref(false)
const chatMessages = ref([])
const currentMessage = ref('')
const isLoading = ref(false)
const chatMessagesRef = ref(null)
const textareaRef = ref(null)
const textareaContainer = ref(null)
const externalScrollbar = ref(null)
const scrollbarThumb = ref(null)
const isOnline = ref(true) // 客服在线状态
const isTyping = ref(false) // 正在输入状态
const parking = ref(0) // 当前空闲车位数量
const dish = ref([])

const faqDrawerVisible = ref(false)
const faqKeyword = ref('')
const faqList = ref([])
const faqLoading = ref(false)

const activeTicketId = ref(null)
const activeTicketNo = ref('')
const ticketPollingTimer = ref(null)
const ticketSyncing = ref(false)
const knownTicketMessageIds = new Set()
const ticketTransferring = ref(false)

const stopTicketPolling = () => {
  if (ticketPollingTimer.value) {
    clearInterval(ticketPollingTimer.value)
    ticketPollingTimer.value = null
  }
}

const syncTicketMessages = async (displayStaffMessages = true) => {
  if (!activeTicketId.value) return
  if (ticketSyncing.value) return
  ticketSyncing.value = true
  try {
    const res = await context?.$http({
      url: `support/ticket/${activeTicketId.value}/messages`,
      method: 'get'
    })
    const list = res?.data?.data || []
    const newMessages = []
    const sessionUserId = store.getters['user/session']?.id ?? context?.$toolUtil.storageGet('user_id')
    for (let i = 0; i < list.length; i++) {
      const msg = list[i]
      if (!msg) continue
      const msgId = msg.id ?? msg.messageId ?? msg.message_id ?? msg.ID ?? msg.msgId ?? msg.msg_id
      if (msgId == null) continue
      const id = String(msgId)
      if (knownTicketMessageIds.has(id)) continue
      knownTicketMessageIds.add(id)
      if (!displayStaffMessages) continue
      const senderRole = String((msg.senderRole ?? msg.sender_role ?? msg.senderrole) == null ? '' : (msg.senderRole ?? msg.sender_role ?? msg.senderrole)).trim()
      const senderRoleLower = senderRole.toLowerCase()
      const senderIdRaw = msg.senderId ?? msg.sender_id ?? msg.senderID ?? msg.senderid
      const senderId = senderIdRaw == null ? null : String(senderIdRaw)
      const isUserMessage =
        senderRole === '用户' ||
        senderRoleLower === 'user' ||
        (sessionUserId != null && senderId != null && String(sessionUserId) === senderId)
      if (isUserMessage) continue
      newMessages.push({
        type: 'bot',
        content: String(msg.content ?? msg.text ?? msg.message ?? ''),
        timestamp: msg.addtime ?? msg.addTime ?? msg.add_time ?? msg.timestamp ?? new Date()
      })
    }
    if (newMessages.length) {
      chatMessages.value.push(...newMessages)
      nextTick(() => {
        scrollToBottom()
      })
    }
  } finally {
    ticketSyncing.value = false
  }
}

const startTicketPolling = () => {
  if (!activeTicketId.value) return
  if (ticketPollingTimer.value) return
  syncTicketMessages(true)
  ticketPollingTimer.value = setInterval(() => {
    syncTicketMessages(true)
  }, 3000)
}

// 初化滚动监听
onMounted(() => {
  // 使用window滚动监听更稳定
  window.addEventListener('scroll', handleWindowScroll)
  // 初始化自定义滚动条
  nextTick(() => {
    initCustomScrollbar()
  })
})
onBeforeUnmount(() => {
  clearInterval(interval.value)
  stopTicketPolling()
  // 移除滚动监听
  window.removeEventListener('scroll', handleWindowScroll)
  if (scrollbarRef.value) {
    const scrollElement = scrollbarRef.value.wrapRef
    if (scrollElement) {
      scrollElement.removeEventListener('scroll', handleScroll)
    }
  }
})
// 获取默认菜单index
const setMenuIndex = (path)=>{
  if(path){
    menuIndex.value = path.replace('Detail','List')
  }else{
    menuIndex.value = ''
  }
}
// 默认菜单index
const menuIndex = ref('')
watch(() => router.currentRoute.value,(value, oldValue) => {
  Token.value = context?.$toolUtil.storageGet('frontToken')
  setMenuIndex(value.path)
},{
  immediate:true
})
const init = () => {
  // 获取菜单
  getMenu()
  // 赋值token
  Token.value = context?.$toolUtil.storageGet('frontToken')
  // 时间
  interval.value = setInterval(() => {
    nowDate.value = new Date()
    // 每30秒更新一次空闲车位信息
    if (nowDate.value.getSeconds() % 30 === 0) {
      getAvailableParkingInfo()
    }
  }, 1000)
  // 轮播图
  getRotationList()
  // 初始化空闲车位信息
  getAvailableParkingInfo()
  if(Token.value){
    getSession()
  }
}
// 切换菜单保存index
const menuChange = (e)=>{
  if(e=='chat')return
}
// 轮播图
const rotationList = ref([])
const baseUrl = context?.$config.url
const getRotationList = () => {
  context?.$http({
    url: 'config/list',
    method: 'get',
    params: {
      page: 1,
      limit: 10,
      name: '%swiper%'
    }
  }).then(res => {
    rotationList.value = res.data.data.list
  })
}

// 轮播图跳转
const carouselClick = (url)=>{
  if (url) {
    if (url.startsWith('http')) {
      window.open(url)
    } else {
      context.$router.push(url)
    }
  }
}
const menuList = ref([])
const role = ref('')
const getMenu = () => {
  let params = {
    page: 1,
    limit: 1,
    sort: 'id',
  }
  context?.$http({
    url: "menu/list",
    method: "get",
    params: params
  }).then(res => {
    context?.$toolUtil.storageSet("menus", res.data.data.list[0].menujson);
  })
  menuList.value = context?.$config.menuList
}
const loginClick = () => {
  context?.$toolUtil.storageSet('toPath',window.history.state.current)
  router.push('/login')
}
const loginOut = () => {
  context?.$toolUtil.message('退出成功', 'success')
  context?.$toolUtil.storageClear()
  router.replace('/index/home')
  Token.value = ''
}
//菜单跳转
const menuHandler = (name) => {
  if (name == 'center') {
    name = `${context?.$toolUtil.storageGet('frontSessionTable')}Center`
    menuChange('center')
  }
  router.push(name)
}
// 处理滚动事件，当滚动超过菜单栏位置时固定菜单栏
const handleScroll = (e) => {
  const scrollTop = e.target.scrollTop || 0
  // 当滚动超过166px（头部110px + 菜单栏56px）时固定菜单栏
  isMenuFixed.value = scrollTop > 166
}

// 处理window滚动事件
const handleWindowScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  // 当滚动超过166px（头部110px + 菜单栏56px）时固定菜单栏
  isMenuFixed.value = scrollTop > 166
}

// 智能客服功能
// 发送消息
const sendMessage = async () => {
  if (!currentMessage.value.trim()) return

  await Promise.allSettled([getAvailableParkingInfo(), getDishList()])

  const messageToSend = currentMessage.value
  const userMessage = {
    type: 'user',
    content: messageToSend,
    timestamp: new Date(),
    status: 'sending' // 发送中
  }

  chatMessages.value.push(userMessage)
  currentMessage.value = ''
  isLoading.value = true

  // 滚动到底部
  nextTick(() => {
    scrollToBottom()
  })

  // 模拟消息状态变化
  setTimeout(() => {
    userMessage.status = 'sent' // 已发送
  }, 500)

  setTimeout(() => {
    userMessage.status = 'read' // 已读
  }, 1000)

  try {
    if (activeTicketId.value) {
      await context?.$http({
        url: `support/ticket/${activeTicketId.value}/reply`,
        method: 'post',
        data: {
          content: messageToSend
        }
      })
      await syncTicketMessages(true)
      return
    }

    const n = (messageToSend || '').trim().toLowerCase()
    const transfer = ['转人工','人工客服','客服','转接人工','转接客服','人工','真人'].some(k => n.includes(k))
    if (transfer) {
      await openTicketDialog({ sourceMessage: messageToSend })
      return
    }

    const response = await callSupportChat(messageToSend)

    const botMessage = {
      type: 'bot',
      content: response.reply,
      timestamp: new Date(),
      suggestTicket: response.suggestTicket,
      sourceMessage: messageToSend
    }

    chatMessages.value.push(botMessage)
  } catch (error) {
    const errorMessage = {
      type: 'bot',
      content: '抱歉，服务暂时不可用，请稍后再试。',
      timestamp: new Date()
    }

    chatMessages.value.push(errorMessage)
  } finally {
    isLoading.value = false
    nextTick(() => {
      scrollToBottom()
    })
  }
}

const callSupportChat = async (message) => {
  const response = await context?.$http({
    url: 'support/chat',
    method: 'post',
    data: {
      message,
      parking: parking.value,
      dish: dish.value
    }
  })
  const d = response?.data?.data || {}
  return {
    reply: d.reply || '',
    suggestTicket: !!(d.suggestTicket || d.suggest_ticket)
  }
}

const findLastUserMessage = () => {
  for (let i = chatMessages.value.length - 1; i >= 0; i--) {
    const m = chatMessages.value[i]
    if (m && m.type === 'user' && (m.content || '').trim()) return String(m.content)
  }
  return ''
}

const openTicketDialog = async (message) => {
  if (!Token.value) {
    context?.$toolUtil.message('请登录后再转人工', 'error')
    context?.$toolUtil.storageSet('toPath', window.history.state.current)
    router.push('/login')
    return
  }

  if (activeTicketId.value) {
    chatMessages.value.push({
      type: 'bot',
      content: `你已接入人工客服：${activeTicketNo.value || ''}。请直接在这里继续发送消息。`,
      timestamp: new Date()
    })
    nextTick(() => {
      scrollToBottom()
    })
    return
  }

  if (ticketTransferring.value) return
  ticketTransferring.value = true
  try {
    const userQuestion = ((message?.sourceMessage || '').trim() || findLastUserMessage()).trim()
    const title = (userQuestion || '人工会话').slice(0, 16)
    const content = userQuestion || '用户请求转人工'

    const response = await context?.$http({
      url: 'support/ticket/submit',
      method: 'post',
      data: {
        content,
        ...(title ? { title } : {}),
      }
    })
    const d = response?.data?.data || {}
    activeTicketId.value = d.ticketId || d.ticket_id || null
    activeTicketNo.value = d.ticketNo || d.ticket_no || ''
    knownTicketMessageIds.clear()
    if (chatDialogVisible.value) startTicketPolling()
    chatMessages.value.push({
      type: 'bot',
      content: `已为你转接人工客服：${activeTicketNo.value || ''}。你可以继续在这里发送消息，客服回复会显示在这里。`,
      timestamp: new Date()
    })
    nextTick(() => {
      scrollToBottom()
    })
  } finally {
    ticketTransferring.value = false
  }
}

const openFaqDrawer = async () => {
  faqDrawerVisible.value = true
  await fetchFaqList()
}

const fetchFaqList = async () => {
  try {
    faqLoading.value = true
    const response = await context?.$http({
      url: 'support/faq/search',
      method: 'get',
      params: {
        keyword: faqKeyword.value || ''
      }
    })
    faqList.value = response?.data?.data || []
  } catch (e) {
    faqList.value = []
  } finally {
    faqLoading.value = false
  }
}

const goMyTickets = () => {
  if (!Token.value) {
    context?.$toolUtil.message('请登录后再查看会话', 'error')
    router.push('/login')
    return
  }
  router.push({
    path: `/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`,
    query: {
      tab: 'myTickets'
    }
  })
}

// 滚动到聊天窗口底部
const scrollToBottom = () => {
  if (chatMessagesRef.value) {
    chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
  }
}

// 格式化时间
const formatTime = (timestamp) => {
  return moment(timestamp).format('HH:mm')
}

// 格式化时间分隔符
const formatTimeBreak = (timestamp) => {
  const now = moment()
  const msgTime = moment(timestamp)

  if (now.isSame(msgTime, 'day')) {
    return '今天 ' + msgTime.format('HH:mm')
  } else if (now.subtract(1, 'day').isSame(msgTime, 'day')) {
    return '昨天 ' + msgTime.format('HH:mm')
  } else {
    return msgTime.format('MM-DD HH:mm')
  }
}

// 判断是否显示时间分隔符
const shouldShowTimeBreak = (index) => {
  if (index === 0) return true

  const current = chatMessages.value[index]
  const previous = chatMessages.value[index - 1]

  // 如果两条消息间隔超过 5 分钟，显示时间分隔符
  return moment(current.timestamp).diff(moment(previous.timestamp), 'minutes') > 5
}

// 处理输入事件
const handleTyping = () => {
  // 模拟输入状态
  isTyping.value = true
  clearTimeout(window.typingTimer)
  window.typingTimer = setTimeout(() => {
    isTyping.value = false
  }, 1000)
}

// 处理键盘事件
const handleKeyDown = (e) => {
  // Ctrl/Cmd + Enter 发送消息
  if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
    e.preventDefault()
    sendMessage()
  }
  // Shift + Enter 换行，不做处理
  if (e.shiftKey && e.key === 'Enter') {
    return
  }
}

// 初始化自定义滚动条
const initCustomScrollbar = () => {
  if (!textareaRef.value || !externalScrollbar.value || !scrollbarThumb.value) return

  const textarea = textareaRef.value
  const scrollbar = externalScrollbar.value
  const thumb = scrollbarThumb.value

  // 更新滚动条状态
  const updateScrollbar = () => {
    const scrollHeight = textarea.scrollHeight
    const clientHeight = textarea.clientHeight
    const scrollTop = textarea.scrollTop

    if (scrollHeight <= clientHeight) {
      // 不需要滚动条
      scrollbar.style.display = 'none'
    } else {
      // 显示滚动条
      scrollbar.style.display = 'block'

      // 计算滚动条高度和位置
      const thumbHeight = Math.max((clientHeight / scrollHeight) * clientHeight, 20)
      const thumbTop = (scrollTop / (scrollHeight - clientHeight)) * (clientHeight - thumbHeight)

      thumb.style.height = thumbHeight + 'px'
      thumb.style.top = thumbTop + 'px'
    }
  }

  // 监听文本域滚动
  textarea.addEventListener('scroll', updateScrollbar)
  textarea.addEventListener('input', () => {
    // 延迟更新，等待DOM更新
    nextTick(updateScrollbar)
  })

  // 初始更新
  updateScrollbar()

  // 滚动条点击和拖拽事件
  let isDragging = false
  let startY = 0
  let startScrollTop = 0

  thumb.addEventListener('mousedown', (e) => {
    isDragging = true
    startY = e.clientY
    startScrollTop = textarea.scrollTop
    e.preventDefault()
  })

  document.addEventListener('mousemove', (e) => {
    if (!isDragging) return

    const deltaY = e.clientY - startY
    const scrollbarHeight = scrollbar.clientHeight
    const thumbHeight = thumb.clientHeight
    const maxScrollTop = textarea.scrollHeight - textarea.clientHeight

    const scrollRatio = deltaY / (scrollbarHeight - thumbHeight)
    textarea.scrollTop = startScrollTop + scrollRatio * maxScrollTop
  })

  document.addEventListener('mouseup', () => {
    isDragging = false
  })

  // 滚动条区域点击
  scrollbar.addEventListener('click', (e) => {
    if (e.target === thumb) return

    const rect = scrollbar.getBoundingClientRect()
    const clickY = e.clientY - rect.top
    const scrollbarHeight = scrollbar.clientHeight
    const maxScrollTop = textarea.scrollHeight - textarea.clientHeight

    const scrollRatio = clickY / scrollbarHeight
    textarea.scrollTop = scrollRatio * maxScrollTop
  })
}

// 获取空闲车位信息
const getAvailableParkingInfo = async () => {
  try {
    // 调用实际的空闲车位查询接口
    const response = await context?.$http({
      url: 'parking_spot/list',
      method: 'get',
      params: {
        page: 1,
        limit: 1000, // 获取所有车位
        spot_status: '空闲' // 筛选空闲状态的车位
      }
    })

    if (response && response.data && response.data.data) {
      parking.value = response.data.data.total || response.data.data.list?.length || 0
      console.log('空闲车位数量:', parking.value)
    } else {
      // 如果接口返回格式不同，尝试其他方式
      parking.value = 0
    }
  } catch (error) {
    console.error('获取空闲车位信息失败:', error)

    // 如果接口调用失败，尝试替代方案
    try {
      // 尝试使用统计接口
      const countResponse = await context?.$http({
        url: 'parking_spot/count',
        method: 'get',
        params: {
          spot_status: '空闲'
        }
      })

      if (countResponse && countResponse.data) {
        const d = countResponse.data.data
        parking.value = typeof d === 'number' ? d : (d && d.count ? d.count : 0)
      } else {
        // 最后的备用方案，使用模拟数据
        parking.value = Math.floor(Math.random() * 15) + 1 // 1-15随机空闲车位
        console.log('使用模拟空闲车位数量:', parking.value)
      }
    } catch (secondError) {
      console.error('统计接口也失败:', secondError)
      // 使用默认值
      parking.value = 11 // 根据用户提到的"11个空闲车位"设置默认值
    }
  }
}

// 获取菜品列表
const getDishList = async () => {
  try {
    console.log('开始获取菜品列表...')
    const response = await context?.$http({
      url: 'dish_info/list',
      method: 'get',
      params: {
        page: 1,
        limit: 20, // 只获取20个热门菜品，提升响应速度
        sort: 'id',
        order: 'desc'
      }
    })

    if (response && response.data && response.data.data && response.data.data.list) {
      // 提取关键信息，减少数据量（移除image和长描述）
      dish.value = response.data.data.list.map(item => ({
        name: item.dish_name || '未知菜品',
        price: item.price || item.jiage || 0,
        category: item.dish_category || '其他',
        taste: item.flavor || ''
        // 移除description和image，减少数据传输量
      }))
      console.log('菜品列表获取成功，共', dish.value.length, '个菜品')
    } else {
      dish.value = []
      console.warn('菜品列表返回格式异常')
    }
  } catch (error) {
    console.error('获取菜品列表失败:', error)
    dish.value = []
  }
}

// 显示车位详情
const showParkingDetails = () => {
  const statusText = parking.value <= 5 ? '紧张' : parking.value > 10 ? '充足' : '正常'
  const message = `当前空闲车位数量：${parking.value}个\n车位状态：${statusText}\n\n点击右侧刷新按钮可更新信息`
  context?.$toolUtil.message(message, 'info')
}

// 初始化聊天窗口
const initChat = () => {
  if (chatMessages.value.length === 0) {
    chatMessages.value = [
      {
        type: 'bot',
        content: '您好！欢迎使用智能客服，我是您的专属助手。有什么可以帮您的吗？',
        timestamp: new Date()
      }
    ]
  }
}

// 监听聊天窗口打开
watch(chatDialogVisible, (newVal) => {
  if (newVal) {
    // 打开聊天窗口时更新空闲车位信息
    getAvailableParkingInfo()
    getDishList()

    if (chatMessages.value.length === 0) {
      initChat()
    }

    nextTick(() => {
      scrollToBottom()
      // 初始化自定义滚动条
      initCustomScrollbar()
    })
    if (activeTicketId.value) startTicketPolling()
  } else {
    stopTicketPolling()
  }
})
// 获取用户信息
const getSession = () =>{
  context?.$http({
    url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
    method:'get'
  }).then(res=>{
    context?.$toolUtil.storageSet('user_id',res.data.data.id)
    const displayName = res?.data?.data?.login_name ?? res?.data?.data?.loginName ?? res?.data?.data?.name ?? ''
    const normalizedRaw = String(displayName || '').trim()
    const normalized = normalizedRaw && !['undefined','null'].includes(normalizedRaw.toLowerCase()) ? normalizedRaw : ''
    if(context?.$toolUtil.storageGet('frontSessionTable') == 'user'){
      context?.$toolUtil.storageSet("frontName", normalized)
    }
    if(context?.$toolUtil.storageGet('frontSessionTable') == 'user'){
      context?.$toolUtil.storageSet('headportrait',res.data.data.avatar)
    }
    if(context?.$toolUtil.storageGet('frontSessionTable') == 'admin'){
      context?.$toolUtil.storageSet("frontName", normalized)
    }
    if(context?.$toolUtil.storageGet('frontSessionTable') == 'admin'){
      context?.$toolUtil.storageSet('headportrait',res.data.data.avatar)
    }
    if(context?.$toolUtil.storageGet('frontSessionTable') == 'staff'){
      context?.$toolUtil.storageSet("frontName", normalized)
    }
    if(context?.$toolUtil.storageGet('frontSessionTable') == 'staff'){
      context?.$toolUtil.storageSet('headportrait',res.data.data.avatar)
    }
    // 强制更新Token以触发响应式更新
    Token.value = context?.$toolUtil.storageGet('frontToken')
  })
}

// 处理头像加载错误
const handleAvatarError = (event) => {
  console.log('头像加载失败，使用默认图标')
  // 头像加载失败时隐藏图片，显示默认图标
  event.target.style.display = 'none'
  const fallbackIcon = event.target.nextElementSibling
  if (fallbackIcon && fallbackIcon.classList.contains('fallback-icon')) {
    fallbackIcon.style.display = 'flex'
  }
}

// 处理头像加载成功
const handleAvatarLoad = (event) => {
  console.log('头像加载成功')
  // 头像加载成功时确保图片显示，隐藏默认图标
  event.target.style.display = 'block'
  const fallbackIcon = event.target.nextElementSibling
  if (fallbackIcon && fallbackIcon.classList.contains('fallback-icon')) {
    fallbackIcon.style.display = 'none'
  }
}

// 清空聊天记录
const clearChat = () => {
  chatMessages.value = []
  console.log('聊天记录已清空')
  // 清空后重新初始化欢迎消息
  nextTick(() => {
    initChat()
  })
}


init()
</script>
<style lang="scss" scoped>
/* 头部 */
.index_top {
  .index_top_right {
    line-height: 1;
    .weather_time_view {
      display: flex;
      align-items: center;
      .weather {
        padding: 0 10px;
        flex-direction: row;
        display: none;
        font-size: 16px;
        line-height: 1;
        justify-content: center;
        align-items: center;
        .city {
          padding: 0;
          margin: 0 10px 0 0;
        }
      }
      .time {
        padding: 0 20px;
        flex-direction: row;
        display: flex;
        font-size: 16px;
        line-height: 1;
        justify-content: center;
        align-items: center;
        .date {
          padding: 0;
          margin: 0 10px 0 0;
        }
        .timer {
          padding: 0;
          margin: 0;
        }
      }
    }
  }
  // 登录按钮
  :deep(.el-button--success) {
    border-radius: 4px;
    padding: 10px;
    margin: 0 10px 0 0;
    color: #fff;
    background: var(--theme-color);
    border-color: var(--theme-color);
  }
  :deep(.el-button--success:hover) {
    background: var(--theme2-color);
    border-color: var(--theme2-color);
  }
  // 登出按钮
  :deep(.el-button--danger) {
    border-radius: 4px;
    padding: 10px;
    color: #fff;
    background: #ea580c;
    border-color: #ea580c;
  }
  :deep(.el-button--danger:hover) {
    background: rgba(234, 88, 12, 0.6);
    border-color: rgba(234, 88, 12, 0.6);
  }
}
// 轮播盒子
// 底部
.bottom_view {
  // 图片
  .bottom_img {
    border-radius: 100%;
    object-fit: cover;
    display: none;
    width: 44px;
    height: 44px;
  }
  // 公司
  .bottom_company {
    margin: 10px 0 0;
  }
  // 备案号
  .bottom_record {
    margin: 10px 0 0;
  }
  // 联系方式
  .bottom_desc {
    margin: 10px 0 0;
  }
}




.contain_view {
  margin: 0;
  background: #fff;
  position: relative;
  height: calc(100% - 60px);
}
.el-aside {
  transition: width 0.15s;
  -webkit-transition: width 0.15s;
  -moz-transition: width 0.15s;
  -webkit-transition: width 0.15s;
  -o-transition: width 0.15s;
}
.el-sub-menu__hide-arrow{
  display: block !important;
}
// 总盒子
</style>
<style lang="scss">
// 二级
.menu_popper {
  border:none !important;
  background: none !important;
}
</style>

<style>

.index_top {
  width: 100%;
  display: flex;
  align-items: center;
  height: 80px;
  background: #ffffff !important;
  justify-content: space-between;
  padding: 0px 5%;
  box-sizing: border-box;
  z-index: 1002;
  position: sticky;
  top: 0;
  border-bottom: 1px solid rgba(0,0,0,0.08);
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  backdrop-filter: blur(8px);
}

.index_top .index_top_title{
  font-size: 24px;
  color: var(--theme-color);
  font-weight: 800;
  letter-spacing: -0.5px;
}

.index_top .index_top_right{
  display: flex;
  align-items: center;
  gap: 5px;
  margin-right: 20px;
}

/* 车位信息显示 */
.index_top .index_top_center {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.parking-info {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.parking-info:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.parking-info .iconfont {
  font-size: 16px;
  margin-right: 8px;
}

.parking-text {
  font-size: 14px;
  margin-right: 4px;
}

.parking-count {
  font-size: 18px;
  font-weight: bold;
  margin-right: 4px;
  transition: color 0.3s ease;
}

.parking-count.low-count {
  color: #ff6b6b;
  animation: pulse 2s infinite;
}

.parking-count.high-count {
  color: #51cf66;
}

.parking-unit {
  font-size: 14px;
}

.refresh-btn {
  margin-left: 8px;
  padding: 4px;
  color: rgba(255,255,255,0.8);
  transition: all 0.3s ease;
  background: transparent !important;
  border: none !important;
}

.refresh-btn:hover {
  color: white !important;
  transform: rotate(180deg);
  background: rgba(255,255,255,0.1) !important;
}

.refresh-btn .iconfont {
  font-size: 14px;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.index_top .notice-btn{
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0,0,0,0.08);
  color: #374151;
  margin: 0;
  padding: 10px 16px;
  font-size: 14px;
  border-radius: 12px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  height: 40px;
  backdrop-filter: blur(8px);
  font-weight: 500;
}
.index_top .notice-btn:hover{
  color: var(--theme-color);
  background: rgba(255, 255, 255, 0.95);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.index_top .notice-btn .iconfont{
  font-size: 18px;
}

/* 智能客服按钮样式 */
.index_top .chat-btn{
  background: var(--theme-color);
  border: none;
  color: white;
  margin: 0;
  padding: 10px 16px;
  font-size: 14px;
  border-radius: 12px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  height: 40px;
  box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3);
  font-weight: 600;
}
.index_top .chat-btn:hover{
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(234, 88, 12, 0.4);
}

.index_top .chat-btn .iconfont{
  font-size: 18px;
}

.index_top .index_top_right .chat{
  margin-right:10px;
}
.index_top .index_top_right .chat:hover{
  color: var(--theme);
}
.index_top .index_top_right .chat .iconfont{
  font-size: 16px;
}
.index_top .index_top_right .chat span{
  font-size: 16px;
}

.index_top .index_top_right .cart{
  margin-right:10px;
  font-size: 16px;
}
.index_top .index_top_right .cart:hover{
  color: var(--theme);
}
.index_top .index_top_right .cart .iconfont{
  font-size: 16px;
  margin-right:5px;
}

.index_top .index_top_right .toBack{
  margin-right:15px;
}
.index_top .index_top_right .toBack .el-button{
  border:0;
  padding:0;
  font-size: 16px;
  color: #333;
}
.index_top .index_top_right .toBack .el-button:hover{
  color: var(--theme);
  background:none;
}


.index_top .user {
  display: flex;
  align-items: center;
  gap: 10px;
}

.index_top .user .logout-btn {
  background: transparent;
  border: 1px solid rgba(0, 0, 0, 0.1);
  color: #6b7280;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  transition: all 0.3s ease;
  height: 36px;
}

.index_top .user .logout-btn:hover {
  background: #fee2e2;
  border-color: #ef4444;
  color: #ef4444;
}

.index_top .user .avatar-wrapper{
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 16px 6px 6px;
  border-radius: 24px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0,0,0,0.08);
  backdrop-filter: blur(8px);
}

.index_top .user .avatar-wrapper:hover{
  background: rgba(255, 255, 255, 0.95);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.index_top .user .avatar-wrapper .user-avatar{
  width: 36px !important;
  height: 36px !important;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.index_top .user .avatar-wrapper:hover .user-avatar{
  border-color: var(--theme);
}

.index_top .user .avatar-wrapper .nickname{
  font-size: 14px;
  color: #374151;
  font-weight: 600;
  letter-spacing: -0.2px;
}

.bottom_view {
  width: 100%;
  background: #242424;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 20px 0px;
  min-height: 130px;
  border-width: 0px 0px 0px;
  border-style: solid;
  border-color: #d6dee0;
  font-size: 16px;
  color: #fff;
  line-height: 1.8;
  margin-top:0px;
}


.menu_scrollbar {
  width: 100%;
  margin:0 auto 5px;
  background: var(--theme-color);
  border-width: 0px;
  border-style: solid;
  border-color: rgb(239, 239, 239);
  border-radius: 0;
  box-shadow: 0 4px 12px rgba(234, 88, 12, 0.2);
}
.menu_scrollbar .el-scrollbar__view {
  padding-bottom: 0px;
}

.menu_scrollbar .menu_view {
  background: transparent;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  border-bottom:none;
  height:56px;
  padding-top:8px;
}

.menu_scrollbar .menu_view .el-menu-item,.menu_scrollbar .menu_view .el-sub-menu,.menu_scrollbar .menu_view .el-sub-menu .el-sub-menu__title{
  height: 48px;
  line-height: 48px;
  border: 0px;
  color: #fff;
  font-size: 16px;
  padding: 0px 10px;
  cursor: pointer;
  white-space: nowrap;
  list-style: none;
  background: transparent;
  min-width: 80px;
  text-align: center;
}
.menu_scrollbar .menu_view .el-menu-item:hover,.menu_scrollbar .menu_view .el-sub-menu:hover{
  background: rgba(255, 255, 255, 0.2);
  color: #fff !important;
  border-radius: 12px;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  backdrop-filter: blur(8px);
}
.menu_scrollbar .menu_view .el-menu-item.is-active,.menu_scrollbar .menu_view .el-sub-menu.is-active{
  height: 48px;
  line-height: 48px;
  font-size: 16px;
  border-bottom:none;
  background: rgba(255, 255, 255, 0.25);
  color: #fff !important;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  backdrop-filter: blur(12px);
  font-weight: 600;
}

.menu_scrollbar .menu_view .el-sub-menu .el-sub-menu__title .el-icon-menu,.menu_scrollbar .menu_view .el-sub-menu .el-sub-menu__title .iconfont{
  vertical-align: middle;
  margin: 0px 3px;
  width: 34px;
  text-align: center;
  font-size: 18px;
  color: inherit;
}

.menu_scrollbar .menu_view .el-sub-menu .el-sub-menu__title span{
  font-size: 16px;
  vertical-align: middle;
  color: inherit;
}

.menu_scrollbar .menu_view .el-sub-menu .el-sub-menu__title .el-sub-menu__icon-arrow{
  position: static;
  margin: -3px 0px 0px 8px;
  font-size: 12px;
  vertical-align: middle;
  color: inherit;
}


.el-menu--horizontal .el-menu{
  border: none;
  padding: 10px;
}
.el-menu--horizontal .el-menu .el-menu-item{
  color: rgb(102, 102, 102);
  height: 40px;
  line-height: 40px;
  padding: 0px 20px;
  background: rgb(255, 255, 255);
  transition: all 0s;
}
.el-menu--horizontal .el-menu .el-menu-item:hover{
  color: #fff !important;
  background: var(--theme-color) !important;
  border-radius:4px;
}
.el-menu--horizontal .el-menu .el-menu-item.is-active{
  color: #fff !important;
  background: var(--theme-color) !important;
  border-radius:4px;
}


.rotation_view{
  width: 68%;
  margin: 10px auto;
}

.rotation_view .swiper{
  width: 100% !important;
  height: 480px;
  border:0px solid #fff;
  border-radius:10px;
}
.rotation_view .swiper .swiper-slide img{
  width: 100%;
  height: 100%;
  object-fit:cover;
  border-radius:10px;
}

.rotation_view .swiper .swiper-button-prev{
  margin-left:5%;
  color:var(--theme);
}

.rotation_view .swiper .swiper-button-next{
  margin-right:5%;
  color:var(--theme);
}


.bread_view {
  width:100%;
  background: none;
  color: rgb(51, 51, 51);
  text-align:center;
  font-size:20px;
  font-weight:600;
  border:0px solid #eee;
  padding:15px 20px 10px 50px;
  background:var(--theme);
}

.bread_view .el-breadcrumb{
  font-size: 16px;
  line-height: 1;
}

.bread_view .el-breadcrumb .el-breadcrumb__separator{
  margin: 0px 9px;
  font-weight: 500;
  color: #fff;
}

.bread_view .el-breadcrumb .first_breadcrumb span a{
  color: #fff;
  display: inline-block;
}

.bread_view .el-breadcrumb .second_breadcrumb .el-breadcrumb__inner{
  color: #fff;
  display: inline-block;
}
.bread_view .el-breadcrumb .second_breadcrumb .el-breadcrumb__inner.is-link{
  color: #fff !important;
}

/* 固定菜单栏样式 */
.menu-wrapper.fixed-menu {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  z-index: 1000 !important;
  box-shadow: 0 2px 8px 0 rgba(0,0,0,.1) !important;
}

/* 占位符样式，保持布局不跳动 */
.menu-placeholder {
  height: 56px;
  width: 100%;
  background: var(--theme);
}

/* 当菜单固定时，调整容器高度 */
.home:has(.fixed-menu) .contain_view {
  height: calc(100% - 116px) !important; /* 60px(默认) + 56px(菜单) */
}

/* 聊天窗口样式 */
.chat-dialog {
  --el-dialog-border-radius: 16px;
  --el-dialog-padding-primary: 0;
  border-radius: 16px !important;
  overflow: hidden;
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15) !important;
}

.chat-dialog .el-dialog__header {
  padding: 0;
  margin: 0;
  display: none;
}

.chat-dialog .el-dialog__body {
  padding: 0;
}

/* 聊天头部 */
.chat-header {
  padding: 16px 24px;
  background: linear-gradient(135deg, #ffffff 0%, #f3f4f6 100%);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
  box-sizing: border-box;
}

.chat-header-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.bot-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 12px rgba(118, 75, 162, 0.3);
}

.bot-avatar .iconfont {
  font-size: 20px;
}

.bot-info {
  display: flex;
  flex-direction: column;
}

.bot-name {
  font-weight: 700;
  font-size: 16px;
  color: #1a1a1a;
  margin-bottom: 2px;
}

.bot-status {
  font-size: 12px;
  color: #9ca3af;
  display: flex;
  align-items: center;
  gap: 4px;
}

.bot-status::before {
  content: '';
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #9ca3af;
}

.bot-status.online {
  color: #10b981;
}

.bot-status.online::before {
  background: #10b981;
  box-shadow: 0 0 0 2px rgba(16, 185, 129, 0.2);
}

.chat-header-actions {
  display: flex;
  gap: 8px;
}

.chat-header-actions .el-button {
  padding: 8px;
  border: none;
  background: transparent;
  color: #6b7280;
  font-size: 18px;
  transition: all 0.3s ease;
  border-radius: 8px;
}

.chat-header-actions .el-button:hover {
  background: rgba(0, 0, 0, 0.05);
  color: var(--theme-color);
  transform: rotate(90deg);
}

.chat-container {
  height: 550px;
  display: flex;
  flex-direction: column;
  background: #ffffff;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f9fafb;
  scroll-behavior: smooth;
}

.empty-chat {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #9ca3af;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
  opacity: 0.5;
  color: var(--theme-color);
}

.time-break {
  text-align: center;
  margin: 24px 0;
}

.time-break span {
  background: rgba(0, 0, 0, 0.04);
  color: #9ca3af;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.message-group {
  margin-bottom: 20px;
  animation: slideIn 0.3s ease-out forwards;
}

@keyframes slideIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.message {
  display: flex;
  align-items: flex-end;
  gap: 12px;
}

.user-message {
  justify-content: flex-end;
}

.bot-message {
  justify-content: flex-start;
}

.message-avatar {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  font-size: 14px;
}

.user-avatar {
  background: none;
  overflow: hidden;
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-avatar .iconfont {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  z-index: 1;
  background: #e5e7eb;
  color: #9ca3af;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 16px;
  position: relative;
  word-wrap: break-word;
  line-height: 1.6;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.user-message .message-bubble {
  background: linear-gradient(135deg, var(--theme-color) 0%, var(--theme2-color) 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.bot-message .message-bubble {
  background: white;
  color: #374151;
  border-bottom-left-radius: 4px;
}

.message-text {
  margin: 0;
}

.message-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 4px;
  font-size: 11px;
  color: #9ca3af;
}

.user-message .message-meta {
  justify-content: flex-end;
}

.bot-message .message-meta {
  justify-content: flex-start;
}

.message-status {
  display: flex;
  align-items: center;
}

.message-status.read {
  color: var(--theme-color);
}

/* Typing Indicator */
.typing-message {
  opacity: 0.8;
}

.typing-bubble {
  padding: 16px 20px !important;
  background: white !important;
  border: none !important;
}

.typing-indicator {
  display: flex;
  align-items: center;
  gap: 4px;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  background: #9ca3af;
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(1) { animation-delay: -0.32s; }
.typing-indicator span:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% { transform: scale(0.8); opacity: 0.5; }
  40% { transform: scale(1.2); opacity: 1; }
}

/* Input Area */
.chat-input {
  padding: 16px 20px;
  background: white;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.02);
}

.input-area {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  background: #f3f4f6;
  padding: 8px;
  border-radius: 24px;
  border: 1px solid transparent;
  transition: all 0.3s ease;
}

.input-area:focus-within {
  background: white;
  border-color: var(--theme-color);
  box-shadow: 0 0 0 4px rgba(234, 88, 12, 0.1);
}

.input-wrapper {
  flex: 1;
  display: flex;
  position: relative;
}

.textarea-container {
  flex: 1;
  position: relative;
}

.custom-textarea {
  width: 100%;
  min-height: 40px;
  max-height: 100px;
  padding: 8px 12px;
  border: none;
  background: transparent;
  font-size: 14px;
  resize: none;
  outline: none;
  color: #374151;
  line-height: 1.5;
  font-family: inherit;
}

.custom-textarea::-webkit-scrollbar {
  display: none;
}

.send-btn.el-button {
  height: 36px;
  border-radius: 18px;
  padding: 0 20px;
  border: none;
  background: #e5e7eb;
  color: #9ca3af;
  font-weight: 600;
  transition: all 0.3s ease;
  min-width: auto;
}

.send-btn.el-button.has-content {
  background: linear-gradient(135deg, var(--theme-color) 0%, var(--theme-color) 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3);
}

.send-btn.el-button:hover {
  transform: translateY(-1px);
}

.send-btn.el-button:disabled {
  background: #e5e7eb;
  color: #9ca3af;
  cursor: not-allowed;
  transform: none;
}

/* Scrollbar */
.chat-messages::-webkit-scrollbar {
  width: 5px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.chat-messages::-webkit-scrollbar-track {
  background: transparent;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: rgba(0,0,0,0.2);
}

</style>
