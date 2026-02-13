<template>
	<div class="list-page" :style='{}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="back_view" v-if="centerType">
                <el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
            </div>
        </div>
		<el-form :inline="true" :model="searchQuery" class="list_search">
			<div class="search_view">
				<div class="search_label">
					菜品名称：
				</div>
				<div class="search_box">
					<el-input class="search_inp" v-model="searchQuery.dish_name" placeholder="菜品名称"
						clearable>
					</el-input>
				</div>
			</div>

    <div class="search_view">
        <div class="search_label">
            菜品类型：
        </div>
        <div class="search_box">
            <el-select
                class="search_sel"
                clearable
                v-model="searchQuery.dish_category"
                placeholder="菜品类型"
            >
                <el-option v-for="item in dishCategoryLists" :label="item" :value="item"></el-option>
            </el-select>
        </div>
    </div>
			<div class="search_btn_view">
				<el-button class="search_btn" type="primary" @click="searchClick">搜索</el-button>
				<el-button class="add_btn" type="success" v-if="btnAuth('dish_info','新增')" @click="addClick">新增</el-button>
			</div>
		</el-form>
		<div class="category_view">
			<div class="category" :class="categoryIndex==-1?'categoryActive':''" @click="categoryClick(-1)">全部
			</div>
			<div class="category" :class="categoryIndex==index?'categoryActive':''"
				v-for="(item,index) in categoryList" :key="index" @click="categoryClick(index)">{{item}}</div>
		</div>
        <div class="sort-wrapper">
            <el-button class="item price" @click="sortClick('price')" :class="{active:sortType=='price'}">
                <el-icon class="icon" v-if="sortType!='price'"><DCaret /></el-icon>
                <el-icon class="icon desc" v-else-if="sortOrder=='desc'"><SortDown /></el-icon>
                <el-icon class="icon asc" v-else><SortUp /></el-icon>
                价格
            </el-button>
            <el-button class="item storeup" @click="sortClick('storeupNumber')" :class="{active:sortType=='storeupNumber'}">
                <el-icon class="icon" v-if="sortType!='storeupNumber'"><DCaret /></el-icon>
                <el-icon class="icon desc" v-else-if="sortOrder=='desc'"><SortDown /></el-icon>
                <el-icon class="icon asc" v-else><SortUp /></el-icon>
                收藏数
            </el-button>
        </div>
		<div class="page_list">
			<div class="data_box">
				<div class="data_view">
					<div class="data_item" v-for="(item,index) in list" :key="index" @click.stop="detailClick(item.id)" >
						<div class="data_img_box" v-if="item.dish_image && item.dish_image.substr(0,4)=='http'" @click.stop="preViewClick(item.dish_image)">
							<el-image class="data_img" :src="item.dish_image" fit="cover"></el-image>
						</div>
						<div class="data_img_box" v-else @click.stop="preViewClick($config.url+item.dish_image.split(',')[0])">
							<el-image class="data_img" :src="item.dish_image?$config.url + item.dish_image.split(',')[0]:''"
								fit="cover"></el-image>
						</div>
						<div class="data_content">
							<div class="data_title1">
								<span>{{item.dish_name}}</span>
							</div>
							<div class="data_price">
								<span>￥{{item.price || item.jiage}}</span>
							</div>
							<div class="data_title2">
								<span>菜品类型：{{item.dish_category || item.dishCategory}}</span>
							</div>
							<div class="data_title3">
								<span>口味：{{item.flavor}}</span>
							</div>
						</div>
						<div class="cart_btn" @click.stop="addCart(item)">
							<img src="@/assets/cart.png" style="width: 50px; height: 50px;" />
						</div>
					</div>
				</div>
				<el-pagination
					background 
					:layout="layouts.join(',')"
					:total="total" 
					:page-size="listQuery.limit"
                    v-model:current-page="listQuery.page"
					prev-text="上一页"
					next-text="下一页"
					:hide-on-single-page="false"
					:style='{}'
					@size-change="sizeChange"
					@current-change="currentChange"/>
			</div>
		</div>
		<el-dialog v-model="preViewVisible" :title="'查看大图'" width="40%" destroy-on-close>
            <div style="text-align:center">
                <img :src="preViewUrl" style="max-width: 100%;" alt="">
            </div>
		</el-dialog>
	</div>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		nextTick,
        computed,
	} from 'vue';
	import {
		useRoute,
		useRouter
	} from 'vue-router';
    import {
        useStore
    } from 'vuex';
    const store = useStore()
    const user = computed(()=>store.getters['user/session'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	const route = useRoute()
	//基础信息
	const tableName = 'dish_info'
	const formName = '美食信息'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	const list = ref([])
	const listQuery = ref({
		page: 1,
		limit: Number(10)
	})
	const total = ref(0)
	const listLoading = ref(false)
	//权限验证
	const btnAuth = (e,a)=>{
		if(centerType.value){
			return context?.$toolUtil.isBackAuth(e,a)
		}else{
			return context?.$toolUtil.isAuth(e,a)
		}
	}
	const addClick = () => {
		router.push('/index/dish_infoAdd')
	}
	//判断是否从个人中心跳转
	const centerType = ref(false)
	//返回
	const backClick = () => {
		router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
	}
	//搜索
	const searchQuery = ref({})
	//下拉列表
	const dishCategoryLists = ref([])
	const getdishCategoryLists = () => {
		context?.$http({
			url: 'option/dish_info/dish_category',
			method:'get'
		}).then(res=>{
			dishCategoryLists.value = res.data.data
		})
	}
	getdishCategoryLists()
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	//分页
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}
	//分页
    const sortType = ref('')
    const sortOrder = ref('')
    const sortClick = (type)=>{
        if(sortType.value==type && sortOrder.value=='asc'){
            sortType.value = ''
            sortOrder.value = ''
        }else if(sortType.value==type && sortOrder.value=='desc'){
            sortOrder.value = 'asc'
        }else{
            sortType.value = type
            sortOrder.value = 'desc'
        }
        getList()
    }
	// 加入购物车
	const addCart = (item) => {
		if(!context?.$toolUtil.storageGet('frontToken')){
			context?.$toolUtil.message('请先登录','error')
			return false
		}
		const purchaseLimit = Number(item?.purchase_limit ?? item?.onelimittimes ?? 0) || 0
		const stock = Number(item?.stock ?? item?.alllimittimes ?? 0) || 0
		const dishName = item?.dish_name ?? item?.dishName ?? ''
		const goodType = item?.dish_category ?? item?.dishCategory ?? ''
		const originalPrice = Number(item?.price ?? item?.jiage ?? 0) || 0
		const discountPrice = Number(item?.discountprice ?? item?.discount_price ?? item?.discountPrice ?? 0) || 0
		const realPrice = discountPrice > 0 && discountPrice < originalPrice ? discountPrice : originalPrice
		const buyNumber = 1
		//单次购买限制
		if(purchaseLimit > 0 && purchaseLimit < buyNumber){
			context?.$toolUtil.message(`每人只能购买${purchaseLimit}件商品`,'error')
			return false
		}
		//库存不足
		if(stock <= 0){
			context?.$toolUtil.message('商品售罄','error')
			return false
		}
		//库存限制
		if(stock > 0 && stock < buyNumber){
			context?.$toolUtil.message('库存不足','error')
			return false
		}
		
        // Check if in cart
        context?.$http({url:'cart/list',method:'get',params:{goodid: item.id}}).then(res=>{
			if(res.data.data.list.length){
				context?.$toolUtil.message('该商品已在购物车，请前往购买','error')
				return false
			}else{
				let cartForm = {
					buynumber: buyNumber,
					goodid: item.id,
					goodname: dishName,
					picture: item.dish_image?item.dish_image.split(',')[0]:'',
					goodtype: goodType,
					price: originalPrice,
					discountprice: realPrice
				}
				context?.$http({
					url: 'cart/save',
					method: 'post',
					data: cartForm
				}).then(res => {
					context?.$toolUtil.message('加入购物车成功', 'success')
				})
			}
		})
	}
	//列表
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		if (categoryIndex.value != -1) {
			const dishCategory = categoryList.value[categoryIndex.value]
			params.dishCategory = dishCategory
			params.dish_category = dishCategory
		}
		if(searchQuery.value.dish_name&&searchQuery.value.dish_name!=''){
			const dishName = '%' + searchQuery.value.dish_name + '%'
			params.dishName = dishName
			params.dish_name = dishName
		}
		if(searchQuery.value.dish_category&&searchQuery.value.dish_category!=''){
			const dishCategory = searchQuery.value.dish_category
			params.dishCategory = dishCategory
			params.dish_category = dishCategory
		}
        if(sortType.value){
            params['sort'] = sortType.value
            params['order'] = sortOrder.value
        }
		context?.$http({
			url: `${tableName}/${centerType.value?'page':'list'}`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = Number(res.data.data.total)
		})
	}
	//分类
	const categoryList = ref([])
	const categoryIndex = ref(-1)
	const getCategoryList = () => {
		context?.$http({
			url: 'option/dish_info/dish_category',
			method: 'get'
		}).then(res => {
			categoryList.value = res.data.data
		})
	}
	const categoryClick = (index) => {
		listQuery.value.page = 1
		categoryIndex.value = index
		getList()
	}
	const detailClick = (id) => {
		router.push(`${tableName}Detail?id=` + id + (centerType.value?'&&centerType=1':''))
	}
	//下载文件
	const download = (file) =>{
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		const a = document.createElement('a');
		a.style.display = 'none';
		a.setAttribute('target', '_blank');
		file && a.setAttribute('download', file);
		a.href = context?.$config.url + file;
		document.body.appendChild(a);
		a.click();
		document.body.removeChild(a);
	}
	// 查看大图
	const preViewUrl = ref('')
	const preViewVisible = ref(false)
	const preViewClick = (url) =>{
		preViewUrl.value = url
		preViewVisible.value = true
	}
	const init = async() => {
		if(route.query.centerType){
			centerType.value = true
		}
		getCategoryList()
        if(context.$toolUtil.storageGet('frontToken') && !user.value.id){
            await store.dispatch("user/getSession")
        }
		getList()
	}
	init()
</script>
<style lang="scss" scoped>
	// 返回盒子
	.back_view {
		border-radius: 4px;
		padding: 10px 0px;
		margin: 10px auto;
		background: none;
		width: 100%;
		text-align: right;
		// 返回按钮
		.back_btn {
			border: 1px solid var(--theme-color);
			cursor: pointer;
			border-radius: 0px;
			padding: 0 24px;
			color: #fff;
			background: var(--theme-color);
			width: auto;
			font-size: 14px;
			height: 34px;
		}
		// 返回按钮-悬浮
		.back_btn:hover {
		}
	}
	.bread_view {
		:deep(.breadcrumb) {
			.el-breadcrumb__separator {
			}
			.first_breadcrumb {
				.el-breadcrumb__inner {
				}
			}
			.second_breadcrumb {
				.el-breadcrumb__inner {
				}
			}
		}
	}
	// 分类盒子
	.category_view {
		// 分类item
		.category {
		}
		// item-悬浮
		.category:hover {
		}
		// item-选中
		.categoryActive {
		}
	}

	//搜索
	.list_search {
		.search_view {
			.search_label {
			}
			.search_box {
				// 输入框
				:deep(.search_inp) {
					.is-focus {
						box-shadow: none !important;
					}
				}
				// 下拉框
				:deep(.search_sel) {
					//去掉默认样式
					.select-trigger{
						height: 100%;
						.el-input{
							height: 100%;
							.is-focus {
								box-shadow: none !important;
							}
						}
					}
				}
			}
		}
		.search_btn_view {
			// 搜索按钮
			.search_btn {
			}
			// 搜索按钮-悬浮
			.search_btn:hover {
			}
			// 新增按钮
			.add_btn {
			}
			// 新增按钮-悬浮
			.add_btn:hover {
			}
		}
	}

	// 数据盒子
	.page_list {
		//列表
		.data_box {
			.data_view {
				.data_item {
					position: relative;
					.cart_btn {
						position: absolute;
						bottom: 10px;
						right: 10px;
						cursor: pointer;
						z-index: 999;
					}
				}
				.data_item:nth-of-type(2n - 1) {
					// 图片盒子
					.data_img_box {
						width: 100%;
						// 图片
						.data_img {
							object-fit: cover;
							width: 100%;
							height: 100%;
						}
					}
					// 内容盒子
					.data_content {
						.data_title1 {
							font-size: 20px;
							font-weight: 600;
							color: #333;
						}
						// 价格
						.data_price {
							color: #f00;
						}
						// 标题1
						.data_title1 {
							margin: 0 0 10px;
							width: 100%;
						}
						// 标题2
						.data_title2 {
							margin: 0 0 6px;
							width: 100%;
							color: #666;
							font-size: 14px;
						}
						// 标题3
						.data_title3 {
							margin: 0 0 6px;
							width: 100%;
							color: #666;
							font-size: 14px;
						}
						// 标题4
						.data_title4 {
							margin: 0 0 10px;
							width: 100%;
						}
						// 标题5
						.data_title5 {
							margin: 0 0 10px;
							width: 100%;
						}
					}
				}
				.data_item:nth-of-type(2n) {
					// 图片盒子
					.data_img_box {
						width: 100%;
						// 图片
						.data_img {
							object-fit: cover;
							width: 100%;
							height: 100%;
						}
					}
					// 内容盒子
					.data_content {
						.data_title1 {
							font-size: 20px;
							font-weight: 600;
							color: #333;
						}
						// 价格
						.data_price {
							color: #f00;
						}
						// 标题1
						.data_title1 {
							margin: 0 0 10px;
							width: 100%;
						}
						// 标题2
						.data_title2 {
							margin: 0 0 6px;
							width: 100%;
							color: #666;
							font-size: 14px;
						}
						// 标题3
						.data_title3 {
							margin: 0 0 6px;
							width: 100%;
							color: #666;
							font-size: 14px;
						}
						// 标题4
						.data_title4 {
							margin: 0 0 10px;
							width: 100%;
						}
						// 标题5
						.data_title5 {
							margin: 0 0 10px;
							width: 100%;
						}
					}
				}
				.data_item:nth-of-type(2n - 1):hover {
					// 图片盒子
					.data_img_box {
						// 图片
						.data_img {
						}
					}
					// 内容盒子
					.data_content {
						// 价格
						.data_price {
						}
						// 标题1
						.data_title1 {
						}
						// 标题2
						.data_title2 {
						}
						// 标题3
						.data_title3 {
						}
						// 标题4
						.data_title4 {
						}
						// 标题5
						.data_title5 {
						}
					}
				}
				.data_item:nth-of-type(2n):hover {
					// 图片盒子
					.data_img_box {
						// 图片
						.data_img {
						}
					}
					// 内容盒子
					.data_content {
						// 价格
						.data_price {
						}
						// 标题1
						.data_title1 {
						}
						// 标题2
						.data_title2 {
						}
						// 标题3
						.data_title3 {
						}
						// 标题4
						.data_title4 {
						}
						// 标题5
						.data_title5 {
						}
					}
				}
			}
		}
	}

	// 分页器
	.el-pagination {
		// 总页码
		:deep(.el-pagination__total) {
		}
		// 上一页
		:deep(.btn-prev) {
		}
		// 下一页
		:deep(.btn-next) {
		}
		// 上一页禁用
		:deep(.btn-prev:disabled) {
		}
		// 下一页禁用
		:deep(.btn-next:disabled) {
		}
		// 页码
		:deep(.el-pager) {
			// 数字
			.number {
			}
			// 数字悬浮
			.number:hover {
			}
			// 选中
			.number.is-active {
			}
		}
		// sizes
		:deep(.el-pagination__sizes) {
			.el-select {
				//去掉默认样式
				.select-trigger{
					height: 100%;
					.el-input{
						height: 100%;
						.is-focus {
							box-shadow: none !important;
						}
					}
				}
			}
		}
		// 跳页
		:deep(.el-pagination__jump) {
			// 输入框
			.el-input {
				.is-focus {
					box-shadow: none !important;
				}
			}
		}
	}
	
	// 热门信息盒子
	.hot_view {
		// 标题
		.hot_title {
		}

		.hot_list {
			// item
			.hot {
				//图片盒子
				.hot_img_view {
					// 图片
					.hot_img {
					}
				}
				// 内容盒子
				.hot_content {
					// 名称
					.hot_text {
					}
				}
			}
		}
	}
</style>
