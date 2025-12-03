const config = {
    get() {
        return {
            url : process.env.VUE_APP_BASE_API_URL + process.env.VUE_APP_BASE_API + '/',
            name: process.env.VUE_APP_BASE_API,
			menuList:[
				{
					name: '信息管理',
					icon: '',
					child:[

						{
							name:'美食信息',
                            url:'/index/meishixinxiList'

						},
					]
				},
				{
					name: '店铺活动管理',
					icon: '',
					child:[

						{
							name:'店铺活动',
                            url:'/index/dianpuhuodongList'

						},
					]
				},
				{
					name: '购物车管理',
					icon: '',
					child:[

						{
							name:'购物车',
                            url:'/index/cartList'

						},
					]
				},
				{
					name: '车位信息管理',
					icon: '',
					child:[

						{
							name:'车位信息',
                            url:'/index/cheweixinxiList'

						},
					]
				},
				{
					name: '餐厅信息管理',
					icon: '',
					child:[

						{
							name:'餐厅信息',
                            url:'/index/cantingxinxiList'

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
