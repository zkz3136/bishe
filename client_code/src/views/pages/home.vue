<template>
	<div>
		<div class="home_box">
			<!-- 美食信息推荐 -->
			<div class="recomList_view">
				<div class="recomList_title">
                    <span>美食信息推荐</span>
                </div>
				<div class="categoryList">
					<div class="item" @click="meishixinxiRecomCategoryChange(-1)" style="cursor: pointer" :class="{active:meishixinxiRecom_index==-1}">全部</div>
					<div class="item" v-for="(item,index) in meishixinxiRecomCategorys" @click="meishixinxiRecomCategoryChange(index)" :class="{active:meishixinxiRecom_index==index}" style="cursor: pointer">
						{{item}}
					</div>
				</div>
				<div class="recommendList">
					<div class="item" v-for="(item,index) in meishixinxiRecomList" :key="index" @click="detailClick('meishixinxi',item.id)">
						<div class="img-box">
							<img v-if="isHttp(item.caipintupian)" :src="item.caipintupian.split(',')[0]" alt="">
							<img v-else :src="item.caipintupian?$config.url + item.caipintupian.split(',')[0]:''" alt="">
						</div>
						<div class="content-box">
							<div class="title">
								{{item.caipinmingcheng}}
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
				<div class="recommend_more_view" @click="moreClick('meishixinxi')">
					<span class="recommend_more_text">查看更多 +</span>
				</div>
			</div>
			<!-- 餐厅信息首页展示 -->
			<div class="homeList_view">
				<div class="homeList_title">
                    <span>餐厅信息展示</span>
                </div>
				<div class="homeList">
					<div class="item" v-for="(item,index) in cantingxinxiHomeList" :key="index" @click="detailClick('cantingxinxi',item.id)">
						<div class="img-box">
							<img v-if="isHttp(item.fengmian)" :src="item.fengmian.split(',')[0]" alt="">
							<img v-else :src="item.fengmian?$config.url + item.fengmian.split(',')[0]:''" alt="">
						</div>
						<div class="content-box">
							<div class="title">
								{{item.canzhuomingcheng}}
							</div>
							<div class="title">
								可坐人数：{{item.kezuorenshu}}
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
				<div class="homeList_more_view" @click="moreClick('cantingxinxi')">
					<span class="homeList_more_text">查看更多 +</span>
				</div>
			</div>
			<!-- 新闻资讯 -->
			<div class="newsList_view">
				<div class="ntitle"> <div class="n2">News Information</div> <div class="n1">新闻资讯</div> </div>
				
				    <div class="nlist">
				         <ul> 
				        <li v-for="(item,index) in newsList" :key="index" @click="newsDetailClick(item)">
				          
				          <div class="tim"><div class="t1">{{moment(item.addtime).format('DD')}}</div><div class="t2">{{moment(item.addtime).format('YYYY-MM-DD')}}</div></div>
				          
				          <div class="infobox">
				            <div class="nam">{{item.title}}</div>
				            <div class="info">{{item.introduction}}</div>
				          </div>
				          
				        </li>
				         </ul> 
				    </div>
				
				<div class="nmore" @click="moreClick('news')" style="cursor:pointer"><span>查看更多+</span></div>
			</div>

			<div class="appendBox1"></div>
		</div>
		<formModel ref="newsFormModelRef"></formModel>
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
	const meishixinxiRecomCategorys = ref([])
	const meishixinxiRecom_index = ref(-1)
	const getmeishixinxiRecomCategorys = () => {
		context?.$http({
			url: 'option/caipinleixing/caipinleixing',
			method: 'get'
		}).then(res => {
			meishixinxiRecomCategorys.value = res.data.data
		})
	}
	getmeishixinxiRecomCategorys()
	const meishixinxiRecomCategoryChange = (index)=>{
		meishixinxiRecom_index.value = index
		getmeishixinxiRecomList()
	}
	const meishixinxiRecomList = ref([])
	const getmeishixinxiRecomList = () => {
		let autoSortUrl = 'meishixinxi/autoSort'
		if(context?.$toolUtil.storageGet('frontToken')){
			autoSortUrl = "meishixinxi/autoSort2"
		}
		let params = {
			page: 1,
			limit: 4
		}
		if(meishixinxiRecom_index.value>=0){
            params.caipinleixing = meishixinxiRecomCategorys.value[meishixinxiRecom_index.value]
		}
		context?.$http({
			url: autoSortUrl,
			method: 'get',
			params: params
		}).then(res => {
			meishixinxiRecomList.value = res.data.data.list
			meishixinxiRecomList.value.forEach(item=>{
				if(!isHttp(item.caipintupian)){
					item.imgUrls = item.caipintupian.split(',').map(item=>context.$config.url+item)
				}
			})
		})
	}
	//餐厅信息首页展示
	const cantingxinxiHomeList = ref([])
	const getcantingxinxiHomeList = () => {
		let params = {
			page: 1,
			limit: 4,
            sort:'id',
            order:'desc',
		}
		context?.$http({
			url: 'cantingxinxi/list',
			method: 'get',
			params: params
		}).then(res => {
			cantingxinxiHomeList.value = res.data.data.list
			cantingxinxiHomeList.value.forEach(item=>{
				if(!isHttp(item.fengmian)){
					item.imgUrls = item.fengmian.split(',').map(item=>context.$config.url+item)
				}
			})
		})
	}
	//新闻资讯弹窗
	import formModel from './news/formModel'
	const newsFormModelRef = ref(null)
	//新闻资讯
	const newsList = ref([])
	const getNewsList = () => {
		context?.$http({
			url: 'news/list',
			method: 'get',
			params:{
				page:1,
				limit: 6,
                sort:'id',
                order:'desc',
			}
		}).then(res=>{
			newsList.value = res.data.data.list
			newsList.value.forEach(item=>{
				if(!isHttp(item.picture)){
					item.imgUrl=context.$config.url+item.picture.split(',')[0]
				}
			})
		})
	}
	const newsDetailClick = (item) => {
		if (item && item.id){
			newsFormModelRef.value.init(item.id)
		}
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
		getmeishixinxiRecomList()
		//餐厅信息首页展示
		getcantingxinxiHomeList()
		//新闻资讯
		getNewsList()
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
	// 新闻资讯
	.newsList_view {

		.newsList_title {
		}
		// list
		.news_list_one {
			display: flex;
			flex-wrap: wrap;
			.news_item {
				box-shadow: 0 4px 6px rgba(0,0,0,.3);
				margin: 0 10px 10px;
				background: #fff;
				display: flex;
				width: calc(33% - 20px);
				align-items: center;
				.news_img_box {
					width: 40%;
					font-size: 0;
					.news_img {
						object-fit: cover;
						width: 100%;
						height: 200px;
					}
				}
				.news_content {
					margin: 0 0 0 20px;
					width: calc(60% - 20px);
					.news_title {
						font-weight: bold;
						font-size: 20px;
					}
					.news_text {
						font-size: 14px;
						line-height: 1.5;
					}
					.news_time {
						color: #999;
						width: 100%;
						text-align: right;
					}
				}
			}
		}
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
		.news_more_view {
			.news_more_text {
			}
			.el-icon {
			}
		}
	}
	// 新闻资讯
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

/* 样式七 总盒子 */
.recomList_view .recommendList{
    display: flex;
    flex-wrap: wrap;
    margin: 60px auto 0;
}
.recomList_view .recommendList .item{
    width: calc(23.5% - 0px);
    margin: 0px 2% 20px 0;
    background: #fff;
    padding:10px;
    cursor: pointer;
}
.recomList_view .recommendList .item:nth-child(4n){
    margin-right:0px;
} 

.recomList_view .recommendList .item .img-box{
    width: 100%;
    height: 240px;
    overflow: hidden;
    margin: 0px 0px 5px;
}
.recomList_view .recommendList .item .img-box img{
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.recomList_view .recommendList .item .content-box{
    width: 100%;
    background: none;
}
.recomList_view .recommendList .item .content-box .title{
    width: 100%;
    line-height: 24px;
    color:#333;
    white-space:nowrap;
    overflow:hidden;
    text-overflow:ellipsis;
}
.recomList_view .recommendList .item .content-box .price{
    width: 100%;
    text-align:right;
    color: #f00;
    font-size:16px;
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

/* 更多 */
.recomList_view .recommend_more_view{
    position: absolute;
    bottom: 40px;
    left: 10%;
    width: 80%;
    margin: 0px;
    z-index:999;
    text-align:center;
    cursor:pointer;
}
.recomList_view .recommend_more_view span{
    display: inline-block;
    background:var(--theme);
    padding:5px 10px;
    font-size: 16px;
    color:#fff;
}

/* 总盒子 */
.homeList_view{
    width: 100%;
    margin: 20px auto 0;
    padding: 15px 16%;
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

/* 分类 */
.homeList_view .categoryList{
    width: 220px;
    background: #222;
    display: block;
    flex-wrap: wrap;
    margin-right:40px;
    order:1;
    border:0;
}
.homeList_view .categoryList .item{
    background: none;
    line-height:60px;
    color:#fff;
    text-align: center;
    margin:0 0 0px;
    cursor:pointer;
    font-size: 15px;
    border-bottom: 1px solid rgba(255,255,255,.3);
}
.homeList_view .categoryList .item:last-child{
    border-bottom:none;
}
.homeList_view .categoryList .item:hover{
    background:var(--theme-color);
    color:#fff;
}
.homeList_view .categoryList .item.active{
    background:var(--theme-color);
    color:#fff;
}

/* 样式七 总盒子 */
.homeList_view .homeList{
    width: calc(100% - 240px);
    margin: 0;
    display: flex;
    flex-wrap: wrap;
    justify-content:flex-start;
    order:2;
    flex:1;
}
.homeList_view .homeList .item{
    width: calc(50% - 20px);
    margin: 0px 0px 20px 20px;
    padding:20px;
    background: none;
    cursor: pointer;
    overflow:hidden;
    position:relative;
    display:flex;
    flex-wrap:wrap;
    box-shadow: inset 0 0 0 5px #f7f6f6;
    justify-content:space-between;
}
.homeList_view .homeList .item::before,.homeList_view .homeList .item::after {
    box-sizing: inherit;
    content: '';
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
}
.homeList_view .homeList .item::before {
    border-top: 5px solid var(--theme);
    border-bottom: 5px solid var(--theme);
    -webkit-transform: scale3d(0,1,1);
    transform: scale3d(0,1,1);
    -webkit-transform-origin: left;
    transform-origin: left
}
.homeList_view .homeList .item::after {
    border-left: 5px solid var(--theme);
    border-right: 5px solid var(--theme);
    -webkit-transform: scale3d(1,0,1);
    transform: scale3d(1,0,1);
    -webkit-transform-origin: bottom;
    transform-origin: bottom
}
.homeList_view .homeList .item:hover::before,.homeList_view .homeList .item:hover::after {
    -webkit-transform: scale3d(1,1,1);
    transform: scale3d(1,1,1);
    transition: -webkit-transform 0.4s;
    transition: transform 0.4s;
    transition: transform 0.4s,-webkit-transform 0.4s
}

.homeList_view .homeList .item .img-box{
    width: 200px;
    height: 168px;
    overflow: hidden;
    margin: 0px;
    text-align:center;
}
.homeList_view .homeList .item .img-box img{
    width: 100%;
    height: 100%;
    object-fit: cover;
}
.homeList_view .homeList .item .content-box{
    width: calc(100% - 220px);
}

.homeList_view .homeList .item .content-box .title{
    width: 100%;
    line-height: 24px;
    white-space:nowrap;
    overflow:hidden;
    text-overflow:ellipsis;
}
.homeList_view .homeList .item:hover .content-box .title{
    color: #000;
}
.homeList_view .homeList .item .content-box .price{
    width: 100%;
    text-align:left;
    color: #f00;
    font-size:16px;
}
.homeList_view .homeList .item:hover .content-box .price{
}
.homeList_view .homeList .item .content-box .statistic{
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    margin:10px 0 0;
}
.homeList_view .homeList .item:hover .content-box .statistic{
    color: #000;
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

/* 查看更多 */
.homeList_view .homeList_more_view{
    width: 100%;
    margin: 0px;
    z-index:999;
    text-align:center;
    cursor:pointer;
    order:11;
}
.homeList_view .homeList_more_view span{
    display: inline-block;
    background:var(--theme);
    padding:5px 10px;
    font-size: 16px;
    color:#fff;
}
.newsList_view .news_more_view .el-icon-d-arrow-right{
}

/* 总盒子 */
.newsList_view {
    width: 100%;
    margin:20px 0px 0;
    padding: 0 16% 120px;
    background:url(http://clfile.zggen.cn/20241219/92aae42c463d48bd947f23e977b6ede8.jpg) no-repeat center top / cover;
    overflow: hidden;
    position:relative;
    order: 5;
    display:block;
}
.newsList_view:before {
    content: "";
    display: block;
    width: 1100px;
    height: 92px;
    background: url(http://clfile.zggen.cn/20241219/9cf7afb259b14d2b87108552ea885a27.png) no-repeat center top;
    margin: 0 auto;
}
.newsList_view:after {
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

/* 自定义 start*/
.newsList_view .ntitle{
    position: absolute;
    top: 0;
    left: 10%;
    width: 80%;
    margin: 20px 0 20px;
    z-index:999;
    text-align:center;
}
.newsList_view .ntitle .n1{
    display:inline-block;
    font-size:32px;
    color:#333;
    font-weight:600;
    padding-bottom:10px;
}
.newsList_view .ntitle .n2{
    display:none;
    font-size: 18px;
    line-height:30px;
    color:#888;
    margin-left:5px;
}

.newsList_view .nlist{
    width: 100%;
    margin:80px auto 0;
}
.newsList_view .nlist ul{
    margin:0;
    padding:0;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
}
.newsList_view .nlist ul li{
    width: 48%;
    text-align: left;
    background:#fff;
    border: 0px dashed #aaa;
    cursor:pointer;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
    margin:0 0 30px;
    padding:20px;
}
.newsList_view .nlist ul li:hover {
    background:var(--theme);
}
.newsList_view .nlist ul li:last-child{
    border-bottom: none;
}
.newsList_view .nlist ul li .tim {
    display: flex;
    flex-wrap: wrap;
    width: 80px;
}
.newsList_view .nlist ul li .tim .t1 {
    width: 100%;
    text-align: center;
    font-size: 48px;
    line-height: 1;
}
.newsList_view .nlist ul li:hover .tim .t1 {
    color:#fff;
}
.newsList_view .nlist ul li .tim .t2 {
    width: 100%;
    text-align: center;
    line-height: 30px;
    font-size: 14px;
    color:#fff;
    background:var(--theme);
}
.newsList_view .nlist ul li:hover .tim .t2 {
    color:var(--theme);
    background:#fff;
}
.newsList_view .nlist ul li .infobox {
    width: calc(100% - 110px);
    flex: 1;
    padding:0 20px 20px;
}
.newsList_view .nlist ul li .infobox .nam {
    font-size: 16px;
    font-weight: 600;
}
.newsList_view .nlist ul li:hover .infobox .nam {
    color:#fff;
}
.newsList_view .nlist ul li:hover .infobox .nam{
    color:#fff;
}
.newsList_view .nlist ul li .infobox .info {
    font-size: 15px;
    color: #888;
    line-height: 24px;
    height:48px;
    overflow:hidden;
    margin-top: 5px;
}
.newsList_view .nlist ul li:hover .infobox .info {
    color:#ddd;
}

/* 更多 */
.newsList_view .nmore{
    position: absolute;
    bottom: 40px;
    left: 10%;
    width: 80%;
    margin: 0px;
    z-index:999;
    text-align:center;
    cursor:pointer;
}
.newsList_view .nmore span{
    display: inline-block;
    background:var(--theme);
    padding:5px 10px;
    font-size: 16px;
    color:#fff;
}
/* 自定义 end*/

/* 首页新闻详情 */
.news-detail-dialog{
    --el-dialog-width: 60%;
}
.news-detail-dialog .news_detail{
}
.news-detail-dialog .news_detail .news_detail_title{
    font-size: 22px;
    font-weight: 600;
}
.news-detail-dialog .news_detail .news_detail_time{
    width: 100%;
    text-align: right;
    margin: 10px 0;
    font-size: 14px;
    color: #999;
}
.news-detail-dialog .news_detail .news_detail_content{
    width: 100%;
    font-size: 15px;
    line-height: 24px;
}

</style>