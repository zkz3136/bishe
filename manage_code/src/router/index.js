import {
	createRouter,
	createWebHashHistory
} from 'vue-router'
import news from '@/views/news/list'
import cheweiyuyue from '@/views/cheweiyuyue/list'
import caipinleixing from '@/views/caipinleixing/list'
import discussmeishixinxi from '@/views/discussmeishixinxi/list'
import storeup from '@/views/storeup/list'
import users from '@/views/users/list'
import cheweixinxi from '@/views/cheweixinxi/list'
import discusscantingxinxi from '@/views/discusscantingxinxi/list'
import cantingxinxi from '@/views/cantingxinxi/list'
import yonghu from '@/views/yonghu/list'
import yuangong from '@/views/yuangong/list'
import systemNotice from '@/views/systemNotice/list'
import cantingyuyue from '@/views/cantingyuyue/list'
import meishixinxi from '@/views/meishixinxi/list'
import dianpuhuodong from '@/views/dianpuhuodong/list'
import orders from '@/views/orders/list'
import config from '@/views/config/list'
import usersCenter from '@/views/users/center'

export const routes = [{
	path: '/login',
	name: 'login',
	component: () => import('../views/login.vue')
},{
	path: '/',
	name: '首页',
	component: () => import('../views/index'),
	children: [{
		path: '/',
		name: '首页',
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
			path: '/usersCenter',
			name: '管理员个人中心',
			component: usersCenter
		}
		,{
			path: '/news',
			name: '新闻资讯',
			component: news
		}
		,{
			path: '/cheweiyuyue',
			name: '车位预约',
			component: cheweiyuyue
		}
		,{
			path: '/caipinleixing',
			name: '菜品类型',
			component: caipinleixing
		}
		,{
			path: '/discussmeishixinxi',
			name: '美食信息评论-评论',
			component: discussmeishixinxi
		}
	,{
		path: '/storeup',
		name: '我的收藏',
		component: storeup
	}
	,{
		path: '/users',
		name: '管理员',
		component: users
	}
		,{
			path: '/cheweixinxi',
			name: '车位信息',
			component: cheweixinxi
		}
		,{
			path: '/discusscantingxinxi',
			name: '餐厅信息评论-评论',
			component: discusscantingxinxi
		}
		,{
			path: '/cantingxinxi',
			name: '餐厅信息',
			component: cantingxinxi
		}
		,{
			path: '/yonghu',
			name: '用户',
			component: yonghu
		}
		,{
			path: '/yuangong',
			name: '员工',
			component: yuangong
		}
		,{
			path: '/systemNotice',
			name: '系统公告',
			component: systemNotice
		}
		,{
			path: '/cantingyuyue',
			name: '餐厅预约',
			component: cantingyuyue
		}
		,{
			path: '/meishixinxi',
			name: '美食信息',
			component: meishixinxi
		}
		,{
			path: '/dianpuhuodong',
			name: '店铺活动',
			component: dianpuhuodong
		}
		,{
			path: '/orders',
			name: '订单管理',
			component: orders
		}
		,{
			path: '/config',
			name: '轮播图',
			component: config
		}
	]
},
]

const router = createRouter({
	history: createWebHashHistory(process.env.BASE_URL),
	routes
})

export default router
