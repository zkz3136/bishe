import { createRouter, createWebHashHistory } from 'vue-router'
import index from '../views'
import home from '../views/pages/home.vue'
import login from '../views/pages/login.vue'
import yonghuList from '@/views/pages/yonghu/list'
import yonghuDetail from '@/views/pages/yonghu/formModel'
import yonghuAdd from '@/views/pages/yonghu/formAdd'
import yonghuRegister from '@/views/pages/yonghu/register'
import yonghuCenter from '@/views/pages/yonghu/center'
import newsList from '@/views/pages/news/list'
import caipinleixingList from '@/views/pages/caipinleixing/list'
import caipinleixingDetail from '@/views/pages/caipinleixing/formModel'
import caipinleixingAdd from '@/views/pages/caipinleixing/formAdd'
import meishixinxiList from '@/views/pages/meishixinxi/list'
import meishixinxiDetail from '@/views/pages/meishixinxi/formModel'
import meishixinxiAdd from '@/views/pages/meishixinxi/formAdd'
import systemNoticeList from '@/views/pages/systemNotice/list'
import systemNoticeDetail from '@/views/pages/systemNotice/formModel'
import systemNoticeAdd from '@/views/pages/systemNotice/formAdd'
import cartList from '@/views/pages/shop_order/cart'
import storeupList from '@/views/pages/storeup/list'
import cantingxinxiList from '@/views/pages/cantingxinxi/list'
import cantingxinxiDetail from '@/views/pages/cantingxinxi/formModel'
import cantingxinxiAdd from '@/views/pages/cantingxinxi/formAdd'
import cantingyuyueList from '@/views/pages/cantingyuyue/list'
import cantingyuyueDetail from '@/views/pages/cantingyuyue/formModel'
import cantingyuyueAdd from '@/views/pages/cantingyuyue/formAdd'
import dianpuhuodongList from '@/views/pages/dianpuhuodong/list'
import dianpuhuodongDetail from '@/views/pages/dianpuhuodong/formModel'
import dianpuhuodongAdd from '@/views/pages/dianpuhuodong/formAdd'
import cheweiyuyueList from '@/views/pages/cheweiyuyue/list'
import cheweiyuyueDetail from '@/views/pages/cheweiyuyue/formModel'
import cheweiyuyueAdd from '@/views/pages/cheweiyuyue/formAdd'
import cheweixinxiList from '@/views/pages/cheweixinxi/list'
import cheweixinxiDetail from '@/views/pages/cheweixinxi/formModel'
import cheweixinxiAdd from '@/views/pages/cheweixinxi/formAdd'
import order_confirm from '@/views/pages/shop_order/confirm'
import ordersList from '@/views/pages/shop_order/list'

const routes = [{
	path: '/',
	redirect: '/index/home'
},
	{
		path: '/index',
		component: index,
		children: [{
			path: 'home',
			component: home
		}
			, {
				path: 'yonghuList',
				component: yonghuList
			}, {
				path: 'yonghuDetail',
				component: yonghuDetail
			}, {
				path: 'yonghuAdd',
				component: yonghuAdd
			}
			, {
				path: 'yonghuCenter',
				component: yonghuCenter
			}
			, {
				path: 'newsList',
				component: newsList
			}
			, {
				path: 'caipinleixingList',
				component: caipinleixingList
			}, {
				path: 'caipinleixingDetail',
				component: caipinleixingDetail
			}, {
				path: 'caipinleixingAdd',
				component: caipinleixingAdd
			}
			, {
				path: 'meishixinxiList',
				component: meishixinxiList
			}, {
				path: 'meishixinxiDetail',
				component: meishixinxiDetail
			}, {
				path: 'meishixinxiAdd',
				component: meishixinxiAdd
			}
			, {
				path: 'systemNoticeList',
				component: systemNoticeList
			}, {
				path: 'systemNoticeDetail',
				component: systemNoticeDetail
			}, {
				path: 'systemNoticeAdd',
				component: systemNoticeAdd
			}
		, {
			path: 'cartList',
			component: cartList
		}
		, {
			path: 'storeupList',
			component: storeupList
		}
			, {
				path: 'cantingxinxiList',
				component: cantingxinxiList
			}, {
				path: 'cantingxinxiDetail',
				component: cantingxinxiDetail
			}, {
				path: 'cantingxinxiAdd',
				component: cantingxinxiAdd
			}
			, {
				path: 'cantingyuyueList',
				component: cantingyuyueList
			}, {
				path: 'cantingyuyueDetail',
				component: cantingyuyueDetail
			}, {
				path: 'cantingyuyueAdd',
				component: cantingyuyueAdd
			}
		, {
			path: 'dianpuhuodongList',
			component: dianpuhuodongList
		}, {
			path: 'dianpuhuodongDetail',
			component: dianpuhuodongDetail
		}, {
			path: 'dianpuhuodongAdd',
			component: dianpuhuodongAdd
		}
		, {
			path: 'cheweiyuyueList',
			component: cheweiyuyueList
		}, {
				path: 'cheweiyuyueDetail',
				component: cheweiyuyueDetail
			}, {
				path: 'cheweiyuyueAdd',
				component: cheweiyuyueAdd
			}
			, {
				path: 'cheweixinxiList',
				component: cheweixinxiList
			}, {
				path: 'cheweixinxiDetail',
				component: cheweixinxiDetail
			}, {
				path: 'cheweixinxiAdd',
				component: cheweixinxiAdd
			}
			, {
				path: 'order_confirm',
				component: order_confirm
			}
			, {
				path: 'ordersList',
				component: ordersList
			}
		]
	},
	{
		path: '/login',
		component: login
	}
	,{
		path: '/yonghuRegister',
		component: yonghuRegister
	}
	,{
		path: '/forget',
		name: 'forget',
		component: () => import('../views/pages/forget.vue')
	}
]

const router = createRouter({
	history: createWebHashHistory(process.env.BASE_URL),
	routes
})

export default router
