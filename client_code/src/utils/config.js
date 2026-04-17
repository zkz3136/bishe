const config = {
    get() {
        return {
            url : process.env.VUE_APP_BASE_API_URL + process.env.VUE_APP_BASE_API + '/',
            name: process.env.VUE_APP_BASE_API,
			menuList:[
				{
					name: '菜品信息',
					icon: '',
					child:[
						{
							name:'菜品信息',
							url:'/index/dish_infoList'
						},
					]
				},
				{
					name: '餐厅信息',
					icon: '',
					child:[
						{
							name:'餐厅信息',
							url:'/index/restaurant_infoList'
						},
					]
				},
				{
					name: '车位信息',
					icon: '',
					child:[
						{
							name:'车位信息',
							url:'/index/parking_spotList'
						},
					]
				},
				{
					name: '餐厅活动',
					icon: '',
					child:[
						{
							name:'餐厅活动',
							url:'/index/restaurant_eventList'
						},
					]
				},
				{
					name: '购物车',
					icon: '',
					child:[
						{
							name:'购物车',
							url:'/index/cartList'
						},
					]
				},
			]
        }
    },
    getProjectName(){
        return {
            projectName: `餐厅智能服务与预订系统`
        } 
    }
}
export default config
