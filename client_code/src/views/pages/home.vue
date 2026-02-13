<template>
	<div>
		<div class="home_box">
			<!-- 美食信息推荐 -->
			<div class="recomList_view">
				<div class="recomList_title">
                    <span>美食信息推荐</span>
                </div>
				<div class="categoryList">
					<div class="item" @click="dishInfoRecomCategoryChange(-1)" style="cursor: pointer" :class="{active:dishInfoRecomIndex==-1}">全部</div>
					<div class="item" v-for="(item,index) in dishInfoRecomCategories" @click="dishInfoRecomCategoryChange(index)" :class="{active:dishInfoRecomIndex==index}" style="cursor: pointer">
						{{item}}
					</div>
				</div>
				<div class="recommendList">
					<div class="item" v-for="(item,index) in dishInfoRecomList" :key="index" @click="detailClick('dish_info',item.id)">
						<div class="img-box">
							<img v-if="isHttp(item.dish_image)" :src="item.dish_image.split(',')[0]" alt="">
							<img v-else :src="item.dish_image?$config.url + item.dish_image.split(',')[0]:''" alt="">
						</div>
						<div class="content-box">
							<div class="title">
								{{item.dish_name}}
							</div>
							<div class="price">
								￥{{item.price}}
							</div>
							<div class="statistic">
								<div class="collect">
									<span class="iconfont icon-likeline4"></span>
									<div class="num">{{item.storeupNumber}}</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="recommend_more_view" @click="moreClick('dish_info')">
					<span class="recommend_more_text">查看更多 +</span>
				</div>
			</div>
			<!-- 餐厅信息首页展示 -->
			<div class="homeList_view">
				<div class="homeList_title">
                    <span>餐厅信息展示</span>
                </div>
				<div class="homeList">
					<div class="item" v-for="(item,index) in restaurantInfoHomeList" :key="index" @click="detailClick('restaurant_info',item.id)">
						<div class="img-box">
							<img v-if="isHttp(item.cover_image)" :src="item.cover_image.split(',')[0]" alt="">
							<img v-else :src="item.cover_image?$config.url + item.cover_image.split(',')[0]:''" alt="">
						</div>
						<div class="content-box">
							<div class="title">
								{{item.seat_name}}
							</div>
							<div class="title">
								可坐人数：{{item.capacity}}
							</div>
							<div class="statistic">
								<div class="collect">
									<span class="iconfont icon-likeline4"></span>
									<div class="num">{{item.storeupNumber}}</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="homeList_more_view" @click="moreClick('restaurant_event')">
					<span class="homeList_more_text">查看更多 +</span>
				</div>
			</div>


			<div class="appendBox1"></div>
		</div>

	</div>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import moment from 'moment'
	import {
		useRouter
	} from 'vue-router';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	//美食信息推荐
	const dishInfoRecomCategories = ref([])
	const dishInfoRecomIndex = ref(-1)
	const getDishInfoRecomCategories = () => {
		context?.$http({
			url: 'option/dish_info/dish_category',
			method: 'get'
		}).then(res => {
			dishInfoRecomCategories.value = res.data.data
		})
	}
	getDishInfoRecomCategories()
	const dishInfoRecomCategoryChange = (index)=>{
		dishInfoRecomIndex.value = index
		getDishInfoRecomList()
	}
	const dishInfoRecomList = ref([])
	const getDishInfoRecomList = () => {
		let autoSortUrl = 'dish_info/autoSort'
		if(context?.$toolUtil.storageGet('frontToken')){
			autoSortUrl = "dish_info/autoSort2"
		}
		let params = {
			page: 1,
			limit: 4
		}
		if(dishInfoRecomIndex.value>=0){
			const dishCategory = dishInfoRecomCategories.value[dishInfoRecomIndex.value]
            params.dishCategory = dishCategory
            params.dish_category = dishCategory
		}
		context?.$http({
			url: autoSortUrl,
			method: 'get',
			params: params
		}).then(res => {
			dishInfoRecomList.value = res.data.data.list
			dishInfoRecomList.value.forEach(item=>{
				if(!isHttp(item.dish_image)){
					item.imgUrls = item.dish_image.split(',').map(item=>context.$config.url+item)
				}
			})
		})
	}
	//餐厅信息首页展示
	const restaurantInfoHomeList = ref([])
	const getRestaurantInfoHomeList = () => {
		let params = {
			page: 1,
			limit: 4,
            sort:'id',
            order:'desc',
		}
		context?.$http({
			url: 'restaurant_info/list',
			method: 'get',
			params: params
		}).then(res => {
			restaurantInfoHomeList.value = res.data.data.list
			restaurantInfoHomeList.value.forEach(item=>{
				if(!isHttp(item.cover_image)){
					item.imgUrls = item.cover_image.split(',').map(item=>context.$config.url+item)
				}
			})
		})
	}

	//判断图片链接是否带http
	const isHttp = (str) => {
        return str && str.substr(0,4)=='http';
    }
	//跳转详情
	const detailClick = (table,id) => {
		router.push(`/index/${table}Detail?id=${id}`)
	}
	const moreClick = (table) => {
		router.push(`/index/${table}List`)
	}
	const init = () => {
		//美食信息推荐
		getDishInfoRecomList()
		//餐厅信息首页展示
		getRestaurantInfoHomeList()

	}
	init()
</script>

<style lang="scss">
	.home_box {
	}

	// 推荐
	.recomList_view {
		.recomList_title {
		}
		// list
		// list
		// animation
		.animation_box {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		.animation_box img {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box img:hover {
			transform: rotate(0deg) scale(1.05) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		// animation
		// 更多
		.recommend_more_view {
			.recommend_more_text {
			}
		}
	}
	// 推荐

	// 首页展示
	.homeList_view {

		.homeList_title {
		}
		// list
		// list
		// animation
		.animation_box {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		.animation_box img {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			z-index: initial;
		}
		.animation_box img:hover {
			transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			-webkit-perspective: 1000px;
			perspective: 1000px;
			transition: 0.3s;
		}
		// animation
		// 更多
		.homeList_more_view {
			cursor: pointer;
			.homeList_more_text {
			}
		}
	}
	// 首页展示
</style>
<style>
.home_box{
    width: 100%;
    margin: 0px auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}
.home_box .appendBox1{
    width: 100%;
    height:0px;
    background:#f00;
    order:5;
}
/* 总盒子 */
.recomList_view {
    width: 100%;
    margin:20px 0px 0;
    padding: 0px 16% 120px;
    background:var(--theme);
    border-width: 0px;
    border-style: solid;
    border-color:var(--theme2-color50);
    position: relative;
    order: 1;
    display: block;
}
.recomList_view:before {
    content: "";
    display: block;
    width: 1100px;
    height: 92px;
    background: url(http://clfile.zggen.cn/20241219/9cf7afb259b14d2b87108552ea885a27.png) no-repeat center top;
    margin: 0 auto;
}
.recomList_view:after {
    content: "";
    display: block;
    width: 1100px;
    height: 92px;
    background: url(http://clfile.zggen.cn/20241219/9cf7afb259b14d2b87108552ea885a27.png) no-repeat center top;
    position: absolute;
    bottom: -1px;
    left: 50%;
    transform: translateX(-50%) rotate(180deg);
}
/* 标题 */
.recomList_view .recomList_title{
    position: absolute;
    top: 0;
    left: 10%;
    width: 80%;
    margin: 20px 0 20px;
    z-index:999;
    text-align:center;
}
.recomList_view .recomList_title span{
    display:inline-block;
    font-size:32px;
    color:#333;
    font-weight:600;
    padding-bottom:10px;
}

/* 分类 */
.recomList_view .categoryList{
    display: none;
    flex-wrap: wrap;
}
.recomList_view .categoryList .item{
}

/* 现代化卡片样式 */
.recomList_view .recommendList{
    display: flex;
    flex-wrap: wrap;
    margin: 60px auto 0;
    gap: 20px;
}
.recomList_view .recommendList .item{
    width: calc(25% - 15px);
    background: #fff;
    border-radius: 16px;
    overflow: hidden;
    cursor: pointer;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    border: 1px solid rgba(0, 0, 0, 0.04);
    position: relative;
}

.recomList_view .recommendList .item:hover{
    transform: translateY(-8px);
    box-shadow: 0 12px 32px rgba(234, 88, 12, 0.15);
    border-color: rgba(234, 88, 12, 0.2);
}

.recomList_view .recommendList .item .img-box{
    width: 100%;
    height: 200px;
    overflow: hidden;
    position: relative;
    background: linear-gradient(135deg, #f5f5f5 0%, #eeeeee 100%);
}

.recomList_view .recommendList .item .img-box::after{
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(180deg, transparent 0%, rgba(0,0,0,0.1) 100%);
    opacity: 0;
    transition: opacity 0.3s ease;
}

.recomList_view .recommendList .item:hover .img-box::after{
    opacity: 1;
}

.recomList_view .recommendList .item .img-box img{
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.recomList_view .recommendList .item:hover .img-box img{
    transform: scale(1.05);
}

.recomList_view .recommendList .item .content-box{
    width: 100%;
    padding: 16px;
    background: #fff;
}

.recomList_view .recommendList .item .content-box .title{
    width: 100%;
    line-height: 1.4;
    color: #1a1a1a;
    font-weight: 600;
    font-size: 16px;
    margin-bottom: 8px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.recomList_view .recommendList .item .content-box .price{
    width: 100%;
    text-align: left;
    color: var(--theme-color);
    font-size: 20px;
    font-weight: 700;
    margin-bottom: 8px;
}

.recomList_view .recommendList .item .content-box .price::before{
    content: '¥';
    font-size: 14px;
    font-weight: 500;
}
.recomList_view .recommendList .item .content-box .statistic{
    width: 100%;
    display: none;
    align-items: center;
    margin:10px 0 0;
}
.recomList_view .recommendList .item .content-box .statistic .iconfont{
    color: inherit;
    margin: 0px 4px 0px 0px;
}
.recomList_view .recommendList .item .content-box .statistic .num{
    color: inherit;
}
.recomList_view .recommendList .item .content-box .statistic .like{
    display: flex;
    align-items: center;
    color: rgb(153, 153, 153);
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}
.recomList_view .recommendList .item .content-box .statistic .collect{
    display: flex;
    align-items: center;
    color: rgb(153, 153, 153);
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}
.recomList_view .recommendList .item .content-box .statistic .clickNum{
    display: flex;
    align-items: center;
    color: rgb(153, 153, 153);
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}

/* 现代化更多按钮 */
.recomList_view .recommend_more_view{
    position: absolute;
    bottom: 40px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 999;
    text-align: center;
    cursor: pointer;
}
.recomList_view .recommend_more_view span{
    display: inline-flex;
    align-items: center;
    gap: 8px;
    background: linear-gradient(135deg, var(--theme-color) 0%, var(--theme2-color) 100%);
    padding: 12px 24px;
    font-size: 16px;
    font-weight: 500;
    color: #fff;
    border-radius: 50px;
    box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    border: none;
}

.recomList_view .recommend_more_view:hover span{
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(234, 88, 12, 0.4);
}

/* 总盒子 */
.homeList_view{
    width: 100%;
    margin: 20px auto 0;
    padding: 15px 16% 120px;
    background:#fff;
    border-width: 0px;
    border-style: solid;
    border-color: var(--theme2-color50);
    position:relative;
    order: 2;
    display:flex;
    flex-wrap:wrap;
    justify-content:space-between;
    align-items: flex-start;
    align-content: flex-start;
}
.homeList_view:after {
    content: "";
    display: block;
    width: 1100px;
    height: 92px;
    background: url(http://clfile.zggen.cn/20241219/9cf7afb259b14d2b87108552ea885a27.png) no-repeat center top;
    position: absolute;
    bottom: -1px;
    left: 50%;
    transform: translateX(-50%) rotate(180deg);
}
/* 标题 */
.homeList_view .homeList_title{
    width: 101%;
    border-width: 0 0 0px;
    border-style: solid;
    border-color: var(--theme2-color);
    margin: 0 0 20px;
    text-align:center;
}
.homeList_view .homeList_title span{
    display:inline-block;
    font-size:32px;
    color:#333;
    font-weight:600;
    padding-bottom:10px;
    border-bottom:0px solid var(--theme2-color);
}

/* 现代化餐厅信息卡片 */
.homeList_view .homeList{
    width: 100%;
    margin: 0;
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    order: 2;
    flex: 1;
    gap: 20px;
}
.homeList_view .homeList .item{
    width: calc(50% - 10px);
    background: #fff;
    cursor: pointer;
    overflow: hidden;
    position: relative;
    display: flex;
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    border: 1px solid rgba(0, 0, 0, 0.04);
    padding: 20px;
    align-items: center;
    gap: 20px;
}

.homeList_view .homeList .item:hover{
    transform: translateY(-4px);
    box-shadow: 0 12px 32px rgba(234, 88, 12, 0.12);
    border-color: rgba(234, 88, 12, 0.15);
}

.homeList_view .homeList .item::before{
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 4px;
    height: 100%;
    background: linear-gradient(180deg, var(--theme-color) 0%, var(--theme2-color) 100%);
    opacity: 0;
    transition: opacity 0.3s ease;
}

.homeList_view .homeList .item:hover::before{
    opacity: 1;
}

.homeList_view .homeList .item .img-box{
    width: 120px;
    height: 120px;
    overflow: hidden;
    border-radius: 12px;
    flex-shrink: 0;
    background: linear-gradient(135deg, #f5f5f5 0%, #eeeeee 100%);
    position: relative;
}

.homeList_view .homeList .item .img-box img{
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.homeList_view .homeList .item:hover .img-box img{
    transform: scale(1.05);
}

.homeList_view .homeList .item .content-box{
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.homeList_view .homeList .item .content-box .title{
    font-size: 18px;
    font-weight: 600;
    color: #1a1a1a;
    line-height: 1.4;
    margin-bottom: 4px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.homeList_view .homeList .item .content-box .title:first-child{
    color: #1a1a1a;
}

.homeList_view .homeList .item .content-box .title:nth-child(2){
    font-size: 14px;
    font-weight: 400;
    color: #666;
    margin-bottom: 8px;
}

.homeList_view .homeList .item .content-box .statistic{
    display: flex;
    align-items: center;
    gap: 4px;
    color: #999;
    font-size: 14px;
}

.homeList_view .homeList .item .content-box .statistic .iconfont{
    color: var(--theme2-color);
    font-size: 16px;
}
.homeList_view .homeList .item .content-box .statistic .iconfont{
    color: inherit;
    margin: 0px 4px 0px 0px;
}
.homeList_view .homeList .item .content-box .statistic .num{
    color: inherit;
}
.homeList_view .homeList .item .content-box .statistic .like{
    display: flex;
    align-items: center;
    color: inherit;
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}
.homeList_view .homeList .item .content-box .statistic .collect{
    display: flex;
    align-items: center;
    color: inherit;
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}
.homeList_view .homeList .item .content-box .statistic .clickNum{
    display: flex;
    align-items: center;
    color: inherit;
    font-size: 16px;
    margin: 0px 10px 0px 0px;
}

/* 现代化查看更多按钮 */
.homeList_view .homeList_more_view{
    width: 100%;
    margin: 20px 0 0;
    text-align: center;
    cursor: pointer;
    order: 11;
}
.homeList_view .homeList_more_view span{
    display: inline-flex;
    align-items: center;
    gap: 8px;
    background: linear-gradient(135deg, var(--theme-color) 0%, var(--theme2-color) 100%);
    padding: 12px 24px;
    font-size: 16px;
    font-weight: 500;
    color: #fff;
    border-radius: 50px;
    box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.homeList_view .homeList_more_view:hover span{
    transform: translateY(-2px);
    box-shadow: 0 8px 24px rgba(234, 88, 12, 0.4);
}
</style>
