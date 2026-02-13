import {
	createRouter,
	createWebHashHistory
} from 'vue-router'
import discuss_dish_info from '@/views/discuss_dish_info/list'
import storeup from '@/views/storeup/list'
import admin from '@/views/admin/list'
import parking_spot from '@/views/parking_spot/list'
import restaurant_info from '@/views/restaurant_info/list'
import user from '@/views/user/list'
import staff from '@/views/staff/list'
import systemNotice from '@/views/systemNotice/list'
import restaurant_reservation from '@/views/restaurant_reservation/list'
import dish_info from '@/views/dish_info/list'
import restaurant_event from '@/views/restaurant_event/list'
import orders from '@/views/orders/list'
import config from '@/views/config/list'
import menu_manage from '@/views/menu_manage/list'
import adminCenter from '@/views/admin/center'
import support_ticket from '@/views/support_ticket/list'
import support_faq from '@/views/support_faq/list'

export const routes = [{
	path: '/login',
	name: 'login',
	component: () => import('../views/login.vue')
},{
	path: '/',
	name: 'Index',
	component: () => import('../views/index'),
	children: [{
		path: '/',
		name: 'Home',
		component: () => import('../views/HomeView.vue'),
		meta: {
			affix: true
		}
	}, {
		path: '/updatepassword',
		name: '修改密码',
		component: () => import('../views/updatepassword.vue')
	}

		,{
			path: '/adminCenter',
			name: '管理员个人中心',
			component: adminCenter
		}

		,{
			path: '/discuss_dish_info',
			name: '美食信息评论-评论',
			component: discuss_dish_info
		}
	,{
		path: '/storeup',
		name: '我的收藏',
		component: storeup
	}
		,{
			path: '/admin',
			name: '管理员',
			component: admin
		}
		,{
			path: '/parking_spot',
			name: '车位信息',
			component: parking_spot
		}
		,{
			path: '/restaurant_info',
			name: '餐厅信息',
			component: restaurant_info
		}
		,{
			path: '/user',
			name: '用户',
			component: user
		}
		,{
			path: '/staff',
			name: '员工',
			component: staff
		}
		,{
			path: '/system_notice',
			alias: ['/systemNotice'],
			name: '系统公告',
			component: systemNotice
		}
		,{
			path: '/restaurant_reservation',
			name: '餐厅预约',
			component: restaurant_reservation
		}
		,{
			path: '/dish_info',
			name: '美食信息',
			component: dish_info
		}
		,{
			path: '/restaurant_event',
			name: '餐厅活动',
			component: restaurant_event
		}
		,{
			path: '/orders',
			name: '订单管理',
			component: orders
		}
		,{
			path: '/menu_manage',
			name: '菜单管理',
			component: menu_manage
		}
		,{
			path: '/config',
			name: '轮播图',
			component: config
		}
		,{
			path: '/support_ticket',
			name: '人工客服会话',
			component: support_ticket
		}
		,{
			path: '/support_faq',
			name: 'FAQ知识库',
			component: support_faq
		}
	]
},
]

const router = createRouter({
	history: createWebHashHistory(process.env.BASE_URL),
	routes
})

export default router
