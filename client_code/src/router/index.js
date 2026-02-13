import { createRouter, createWebHashHistory } from 'vue-router'
import index from '../views'
import home from '../views/pages/home.vue'
import login from '../views/pages/login.vue'
import userList from '@/views/pages/user/list'
import userDetail from '@/views/pages/user/formModel'
import userAdd from '@/views/pages/user/formAdd'
import userRegister from '@/views/pages/user/register'
import userCenter from '@/views/pages/user/center'
import dish_infoList from '@/views/pages/dish_info/list'
import dish_infoDetail from '@/views/pages/dish_info/formModel'
import dish_infoAdd from '@/views/pages/dish_info/formAdd'
import systemNoticeList from '@/views/pages/systemNotice/list'
import systemNoticeDetail from '@/views/pages/systemNotice/formModel'
import systemNoticeAdd from '@/views/pages/systemNotice/formAdd'
import cartList from '@/views/pages/shop_order/cart'
import storeupList from '@/views/pages/storeup/list'
import restaurant_infoList from '@/views/pages/restaurant_info/list'
import restaurant_infoDetail from '@/views/pages/restaurant_info/formModel'
import restaurant_infoAdd from '@/views/pages/restaurant_info/formAdd'
import restaurant_reservationList from '@/views/pages/restaurant_reservation/list'
import restaurant_reservationDetail from '@/views/pages/restaurant_reservation/formModel'
import restaurant_reservationAdd from '@/views/pages/restaurant_reservation/formAdd'
import restaurant_eventList from '@/views/pages/restaurant_event/list'
import restaurant_eventDetail from '@/views/pages/restaurant_event/formModel'
import restaurant_eventAdd from '@/views/pages/restaurant_event/formAdd'
import parking_spotList from '@/views/pages/parking_spot/list'
import parking_spotDetail from '@/views/pages/parking_spot/formModel'
import parking_spotAdd from '@/views/pages/parking_spot/formAdd'
import order_confirm from '@/views/pages/shop_order/confirm'
import ordersList from '@/views/pages/shop_order/list'
import myReviews from '@/views/pages/user/myReviews'

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
				path: 'userList',
				component: userList
			}, {
				path: 'userDetail',
				component: userDetail
			}, {
				path: 'userAdd',
				component: userAdd
			}
			, {
				path: 'userCenter',
				component: userCenter
			}
			, {
				path: 'dish_infoList',
				component: dish_infoList
			}, {
				path: 'dish_infoDetail',
				component: dish_infoDetail
			}, {
				path: 'dish_infoAdd',
				component: dish_infoAdd
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
				path: 'restaurant_infoList',
				component: restaurant_infoList
			}, {
				path: 'restaurant_infoDetail',
				component: restaurant_infoDetail
			}, {
				path: 'restaurant_infoAdd',
				component: restaurant_infoAdd
			}
			, {
				path: 'restaurant_reservationList',
				component: restaurant_reservationList
			}, {
				path: 'restaurant_reservationDetail',
				component: restaurant_reservationDetail
			}, {
				path: 'restaurant_reservationAdd',
				component: restaurant_reservationAdd
			}
		, {
			path: 'restaurant_eventList',
			component: restaurant_eventList
		}, {
			path: 'restaurant_eventDetail',
			component: restaurant_eventDetail
		}, {
			path: 'restaurant_eventAdd',
			component: restaurant_eventAdd
		}

			, {
				path: 'parking_spotList',
				component: parking_spotList
			}, {
				path: 'parking_spotDetail',
				component: parking_spotDetail
			}, {
				path: 'parking_spotAdd',
				component: parking_spotAdd
			}
			, {
				path: 'order_confirm',
				component: order_confirm
			}
			, {
				path: 'ordersList',
				component: ordersList
			}, {
				path: 'myReviews',
				component: myReviews
			}
		]
	},
	{
		path: '/login',
		component: login
	}
	,{
		path: '/userRegister',
		component: userRegister
	}
	,{
		path: '/forget',
		name: 'forget',
		component: () => import('../views/pages/forget.vue')
	},
	{
		path: '/:pathMatch(.*)*',
		redirect: '/index/home'
	}
]

const router = createRouter({
	history: createWebHashHistory(process.env.BASE_URL),
	routes
})

export default router
