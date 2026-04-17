<template>
	<div class="detail-page" :style='{}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="back_view">
                <el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
            </div>
        </div>
		<div class="detail_view">
			<div class="swiper_view">
				<mySwiper :data="bannerList" :type="3"
					:loop="false"
					:navigation="true"
					:pagination="false"
					:paginationType="1"
					:scrollbar="false"
					:slidesPerView="1"
					:spaceBetween="20"
					:autoHeight="false"
					:centeredSlides="false"
					:freeMode="false"
					:effectType="0"
					:direction="horizontal"
					:autoplay="false"
					:slidesPerColumn="1">
					<template #default="scope">
						<img :style='{"objectFit":"cover","width":"100%","height":"480px"}' :src="scope.row?$config.url + scope.row:''">
					</template>
				</mySwiper>
			</div>

			<div class="info_view">
				<div class="title_view">
					<div class="detail_title">
						<span>{{dishNameText}}</span>
					</div>
					<div class="collect_view" v-if="!collectType" @click="collectClick(1)">
						<i class="iconfont icon-likeline1"></i>
						<span>收藏</span>
					</div>
					<div class="collect_view" v-if="collectType" @click="collectClick(-1)">
						<i class="iconfont iconfontActive icon-likefill1"></i>
						<span class="textActive">取消收藏</span>
					</div>
				</div>
				<div class="info_item">
					<div class="info_label">价格</div>
					<div class="info_price"><span>￥</span>{{priceText}}</div>
				</div>
				<div class="info_item" v-if="detail.jf">
					<div class="info_label">积分</div>
					<div class="info_price">{{detail.jf}}</div>
				</div>
                <div class="info_item">
                    <div class="info_label">单次购买</div>
                    <div class="info_text">{{purchaseLimit}}</div>
                </div>
				<div class="info_item">
					<div class="info_label">库存</div>
					<div class="info_text">{{stockText}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">菜品类型</div>
					<div  class="info_text" >{{dishCategoryText}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">口味</div>
					<div  class="info_text" >{{flavorText}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">推荐指数</div>
					<div  class="info_text" >{{ratingText}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">收藏数</div>
					<div  class="info_text" >{{storeupCount}}</div>
				</div>
			<div class="info_item">
				<div class="info_label">评论数</div>
				<div  class="info_text" >{{discussCount}}</div>
			</div>
			<div class="btn_view">
					<el-input-number class="inputNumber" v-model="buyNumber" :min="1" :disabled="stockValue <= 0 || isOffShelf"></el-input-number>
                    <div class="break"></div>
					<el-button class="addCart_btn" type="primary" :disabled="isOffShelf" @click="addCart">加入购物车</el-button>
					<el-button class="buyNow_btn" :disabled="stockValue <= 0 || isOffShelf" type="primary" @click="buyNow">立即购买</el-button>
					<div v-if="stockValue <= 0" style="margin-left: 12px;color:#999;font-size:14px;">已售罄</div>
					<div v-if="isOffShelf" style="margin-left: 12px;color:#999;font-size:14px;">已下架</div>
				</div>
				<div class="btn_view">
					<el-button class="edit_btn" v-if="centerType&&btnAuth('dish_info','修改')" type="primary" @click="editClick">修改</el-button>
					<el-button class="del_btn" v-if="centerType&&btnAuth('dish_info','删除')" type="danger" @click="delClick">删除</el-button>
				</div>
			</div>
		</div>
		<el-tabs type="border-card" v-model="activeName" class="tabs_view">
			<el-tab-pane label="菜品详情" name="first">
				<div v-html="descHtml"></div>
			</el-tab-pane>
			<el-tab-pane label="评价" name="commentActive">
				<div v-if="centerType" class="my_comment_view">
					<el-form class="my_comment_form" ref="commentFormRef" :model="commentForm" :rules="commentRules" label-width="80px">
						<el-form-item label="评分" prop="score">
							<el-rate v-model="commentForm.score" />
						</el-form-item>
						<el-form-item label="评价内容" prop="content">
							<editor class="list_editor" :value="commentForm.content" placeholder="请输入评价内容" @change="contentChange"></editor>
						</el-form-item>
					</el-form>
					<div class="comment_btn">
						<el-button class="add_btn" type="primary" @click="commentSave">评价</el-button>
						<el-button class="reset_btn" @click="resetForm">重置</el-button>
					</div>
				</div>
				<div class="comment_list">
					<div class="comment" v-for="(item,index) in commentList" :key="index">
						<div class="comment_top">
							<div class="comment_user">
								<div class="comment_user_img">
									<img :src="item.avatar_url?$config.url + item.avatar_url:'../../../assets/avatar.png'" alt="">
								</div>
								<div class="comment_user_info">
									{{item.nickname}}
								</div>
							</div>
							<div class="comment_time">{{item.addtime}}</div>
						</div>
						<div class="comment_bottom">
                            <div class="comment_content" v-html="item.content"></div>
							<div class="comment_score" v-if="item.score">
								<el-rate :model-value="Number(item.score)" disabled />
							</div>
                            <div v-if="item.user_id==user.id" class="comment_action">
                                <span class="del" @click="commentDel(item)" style="cursor: pointer">删除</span>
                            </div>
							<div class="comment_reply" v-if="item.reply">
								餐厅回复：<span>{{ sanitizeReply(item.reply) }}</span>
							</div>
						</div>
					</div>
				</div>
				<el-pagination
					background
					:layout="layouts.join(',')"
					:total="commentTotal"
					:page-size="commentQuery.limit"
					prev-text="上一页"
					next-text="下一页"
					:hide-on-single-page="false"
					:style='{}'
					@size-change="commentSizeChange"
					@current-change="commentCurrentChange" />
			</el-tab-pane>
		</el-tabs>
	</div>
</template>
<script setup>
	import axios from 'axios'
	import moment from 'moment'
	import {
		ref,
		getCurrentInstance,
		watch,
		onUnmounted,
		onMounted,
		nextTick,
		computed
	} from 'vue';
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useRoute,
		useRouter
	} from 'vue-router';
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const userAvatar = computed(()=>store.getters['user/avatar'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const route = useRoute()
	const router = useRouter()
	//基础信息
	const tableName = 'dish_info'
	const formName = '菜品信息'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	//权限验证
	const btnAuth = (e,a)=>{
		if(centerType.value){
			return context?.$toolUtil.isBackAuth(e,a)
		}else{
			return context?.$toolUtil.isAuth(e,a)
		}
	}
	//查看权限验证
	const btnFrontAuth = (e,a)=>{
		if(centerType.value){
			return context?.$toolUtil.isBackAuth(e,a)
		}else{
			return context?.$toolUtil.isFrontAuth(e,a)
		}
	}
	// 返回
	const backClick = () =>{
		history.back()
	}
	// 轮播图
	const bannerList = ref([])
	// 详情
	const title = ref('')
	const detail = ref({})
    const activeName = ref('first')
	const storeupCount = computed(()=>{
		const d = detail.value || {}
		return (d.storeupNumber ?? d.favoritesNumber ?? d.storeup_number ?? d.favorites_number ?? 0)
	})
	const discussCount = computed(()=>{
		return Number(commentTotal.value || 0)
	})
	const dishNameText = computed(()=>{
		const d = detail.value || {}
		return (d.dish_name ?? d.dishName ?? '')
	})
	const originalPriceText = computed(()=>{
		const d = detail.value || {}
		return (d.price ?? d.jiage ?? 0)
	})
	const discountPriceText = computed(()=>{
		const d = detail.value || {}
		return (d.discountprice ?? d.discount_price ?? d.discountPrice ?? 0)
	})
	const priceText = computed(()=>{
		const original = Number(originalPriceText.value) || 0
		const discount = Number(discountPriceText.value) || 0
		if (discount > 0 && discount < original) return discount
		return original
	})
	const purchaseLimit = computed(()=>{
		const d = detail.value || {}
		return (d.purchase_limit ?? d.onelimittimes ?? 0)
	})
	const purchaseLimitValue = computed(()=>{
		const v = purchaseLimit.value
		const n = Number(v)
		return Number.isFinite(n) ? n : 0
	})
	const stockText = computed(()=>{
		const d = detail.value || {}
		return (d.stock ?? d.alllimittimes ?? 0)
	})
	const stockValue = computed(()=>{
		const v = stockText.value
		const n = Number(v)
		return Number.isFinite(n) ? n : 0
	})
	const dishCategoryText = computed(()=>{
		const d = detail.value || {}
		return (d.dish_category ?? d.dishCategory ?? '')
	})
	const flavorText = computed(()=>{
		const d = detail.value || {}
		return (d.flavor ?? '')
	})
	const ratingText = computed(()=>{
		const d = detail.value || {}
		return (d.rating ?? '')
	})
	const isOffShelf = computed(()=>{
		const d = detail.value || {}
		return (d.dish_status ?? d.dishStatus ?? '') === '下架'
	})
	const descHtml = computed(()=>{
		const d = detail.value || {}
		return (d.dish_description ?? d.dishDescription ?? '')
	})
	const getRouteId = () => {
		const v = route.query.id
		const id = Array.isArray(v) ? v[0] : v
		if (id === undefined || id === null) return ''
		const s = String(id)
		if (!s || s === 'undefined' || s === 'null') return ''
		return s
	}
	const getDetail = (id) => {
		context?.$http({
			url: `${tableName}/detail/${id}`,
			method: 'get'
		}).then(res => {
            bannerList.value = res.data.data.dish_image?res.data.data.dish_image.split(','):[]
            title.value = res.data.data.dish_name
			detail.value = res.data.data
			if (!centerType.value && isOffShelf.value) {
				context?.$toolUtil.message('该菜品已下架', 'warning')
			}
			getInCartList();
		})
	}
	// 下载文件
	const downClick = (file) => {
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		let arr = file.replace(new RegExp('file/', "g"), "")
		axios.get((location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/download?fileName=' + arr, {
			headers: {
				token: context?.$toolUtil.storageGet('frontToken')
			},
			responseType: "blob"
		}).then(({
			data
		}) => {
			const binaryData = [];
			binaryData.push(data);
			const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
				type: 'application/pdf;chartset=UTF-8'
			}))
			const a = document.createElement('a')
			a.href = objectUrl
			a.download = arr
			// a.click()
			// 下面这个写法兼容火狐
			a.dispatchEvent(new MouseEvent('click', {
				bubbles: true,
				cancelable: true,
				view: window
			}))
			window.URL.revokeObjectURL(data)
		})
	}
	// 判断是否从个人中心跳转
	const centerType = ref(false)
	const init = () => {
		const id = getRouteId()
		if (!id) {
			context?.$toolUtil.message('参数错误', 'error', () => {
				backClick()
			})
			return
		}
		if(route.query.centerType){
			centerType.value = true
		}
		commentForm.value.ref_id = id
		commentQuery.value.ref_id = id
		getDetail(id)
		// 收藏
		getCollect(id)
		// 评价
		getCommentList()
	}
	// 收藏
	const collectType = ref(false)
	const collectInfo = ref({})
	const getCollect = (id) => {
		if (context?.$toolUtil.storageGet('frontToken')) {
            context?.$http({
                url: 'favorites/page',
                method: 'get',
                params: {
                    page: 1,
                    limit: 1,
                    refid: id,
                    tablename: tableName
                }
            }).then(res => {
				if (res.data.data.list.length) {
					collectType.value = true
					collectInfo.value = res.data.data.list[0]
				}else{
					collectType.value = false
					collectInfo.value = {}
				}
			})
		}
	}
	// 收藏按钮
	const collectClick = (type) => {
		if (type == 1 && !collectType.value) {
            let params = {
                name: title.value,
                picture: bannerList.value[0],
                refid: detail.value.id,
                tablename: tableName
            }
            context?.$http({
                url: 'favorites/add',
				method: 'post',
				data: params
			}).then(res => {
				detail.value.storeupNumber += 1
				context?.$http({
					url: `${tableName}/update`,
					method: 'post',
					data: detail.value
				})
				collectType.value = true
				getCollect(getRouteId())
				context?.$toolUtil.message('收藏成功', 'success')
			})
		}
		else if (type == -1 && collectType.value) {
			let ids = []
			ids.push(collectInfo.value.id)
			context?.$http({
				url: 'storeup/delete',
				method: 'post',
				data: ids
			}).then(res => {
				detail.value.storeupNumber -= 1
				context?.$http({
					url: `${tableName}/update`,
					method: 'post',
					data: detail.value
				})
				collectInfo.value = {}
				collectType.value = false
				context?.$toolUtil.message('取消成功', 'success')
			})
		}
	}
    const contentChange = (e)=>{
        commentForm.value.content = e
    }
	//评价
	const commentForm = ref({
		content: '',
		score: 0,
		ref_id: '',
		user_id: context?.$toolUtil.storageGet('user_id'),
		nickname: context?.$toolUtil.storageGet('frontName'),
		avatar_url: context?.$toolUtil.storageGet('headportrait') ? context?.$toolUtil.storageGet('headportrait') : '',
	})
	const commentRules = ref({
		score: [{
			validator: (rule, value, callback) => {
				if (!value) return callback(new Error('请选择评分'))
				callback()
			},
			trigger: 'change'
		}],
		content: [{
			required: true,
			message: '请输入',
			trigger: 'blur'
		}, ]
	})
	const commentQuery = ref({
		page: 1,
		limit: 10,
		ref_id: ''
	})
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const commentList = ref([])
	const commentTotal = ref(0)
	const sanitizeReply = (h) => {
		let s = String(h ?? '').trim()
		s = s.replace(/^(管理员：|员工：|餐厅回复：)\s*/, '')
		s = s.replace(/<br\s*\/?>/gi, '\n')
		s = s.replace(/<\/p>/gi, '\n')
		s = s.replace(/<[^>]+>/g, '')
		return s.trim()
	}
	const commentFormRef = ref(null)
	const commentSizeChange = (size) =>{
		commentQuery.value.limit = size
		getCommentList()
	}
	const commentCurrentChange = (page) =>{
		commentQuery.value.page = page
		getCommentList()
	}
	const getCommentList = () => {
		context?.$http({
			url: `discuss${tableName}/list?sort=id&order=desc`,
			params: commentQuery.value,
			method: 'get'
		}).then(res => {
			commentList.value = res.data.data.list
			commentTotal.value = res.data.data.total

		})
	}
	//提交评价
	const commentSave = async () => {
        if(!centerType.value){
            context?.$toolUtil.message('请到个人中心的订单管理中进行评价','error')
            return
        }
        if(!commentForm.value.content ||commentForm.value.content=='<p><br></p>'){
            return context.$message.error("请输入评价内容")
        }
		if(!commentForm.value.score){
			return context.$message.error("评分不能为空")
		}
		let sensitiveWords = "妈的;妈逼;碧莲";
		let sensitiveWordsArr = [];
		if(sensitiveWords) {
		    sensitiveWordsArr = sensitiveWords.split(",");
		}
		for(var i=0; i<sensitiveWordsArr.length; i++){
		    //全局替换
		    var reg = new RegExp(sensitiveWordsArr[i],"g");
		    //判断内容中是否包括敏感词
		    if (commentForm.value.content.indexOf(sensitiveWordsArr[i]) > -1) {
		        // 将敏感词替换为 **
		        commentForm.value.content = commentForm.value.content.replace(reg,"**");
		    }
		}
		commentFormRef.value.validate((valid) => {
			if (valid) {
				context?.$http({url:'orders/list',method:'get',params:{page:1,limit:1,status:'已完成',goodid:detail.value.id,userid:context?.$toolUtil.storageGet('user_id')}}).then(res=>{
					if(res.data.data.list.length==0){
						context?.$toolUtil.message('请完成订单后再评论！','error')
						return false
					}
					context?.$http({
						url: `discuss${tableName}/add`,
						method: 'post',
						data: commentForm.value
					}).then(res => {
						context?.$toolUtil.message('评论成功', 'success')
                        resetForm()
                        getCommentList()
					})
				})
			}
		})
	}
	const resetForm = () => {
		commentForm.value.content = ''
		commentForm.value.score = 0
		if(commentFormRef.value){
			commentFormRef.value.clearValidate?.()
		}
	}
    const commentDel = (item)=>{
        context.$confirm("确定要删除该评论吗？","提示").then(()=>{
            context.$http.post(`discuss${tableName}/delete`,[item.id]).then(res=>{
                if(res.data.code==0){
                    context.$message.success("删除成功")
                    // 删除后刷新评价列表
                    getCommentList()
                }
            })
        }).catch(()=>{})
    }

	//查找是否已添加购物车
	const isInCart = ref(false)
	const inCartItem = ref({})
	const getInCartList = () => {
        if(!context?.$toolUtil.storageGet('frontToken'))return
		context?.$http({
			url:'cart/list',
			method:'get',
			params:{
				userid: context?.$toolUtil.storageGet('user_id'),
				goodid: detail.value.id
			}
		}).then(res=>{
			if(res.data.data.list.length){
				isInCart.value = true
				inCartItem.value = res.data.data.list[0] || {}
			}else{
				isInCart.value = false
				inCartItem.value = {}
			}
		})
	}
	const cartForm = ref({
		userid:context?.$toolUtil.storageGet('user_id')
	})
	const addCart = () => {
		if (isOffShelf.value) {
			context?.$toolUtil.message('该菜品已下架', 'warning')
			return
		}
		if(isInCart.value){
			const current = Number(inCartItem.value.buy_number ?? inCartItem.value.buynumber ?? 0)
			const add = Number(buyNumber.value || 0)
			const next = current + add
			if(purchaseLimitValue.value > 0 && next > purchaseLimitValue.value){
				context?.$toolUtil.message(`每人只能购买${purchaseLimitValue.value}件商品`,'error')
				return false
			}
			if(stockValue.value > 0 && next > stockValue.value){
				context?.$toolUtil.message('库存不足','error')
				return false
			}
			context?.$http({
				url: 'cart/update',
				method: 'post',
				data:{
					id: inCartItem.value.id,
					buynumber: next
				}
			}).then(()=>{
				context?.$toolUtil.message('已加入购物车','success')
				getInCartList()
			})
			return
		}
		//单次购买限制
		if(purchaseLimitValue.value > 0 && purchaseLimitValue.value < buyNumber.value){
			context?.$toolUtil.message(`每人只能购买${purchaseLimitValue.value}件商品`,'error')
			return false
		}
		if(stockValue.value > 0 && stockValue.value < buyNumber.value){
			context?.$toolUtil.message('库存不足','error')
			return false
		}
		cartForm.value.buynumber = buyNumber.value
		cartForm.value.goodid = detail.value.id
		cartForm.value.goodname = title.value
		cartForm.value.picture = bannerList.value[0]
		cartForm.value.goodtype = dishCategoryText.value
		cartForm.value.price = Number(originalPriceText.value) || 0
		cartForm.value.discountprice = Number(priceText.value) || 0
		context?.$http({
			url: 'cart/save',
			method: 'post',
			data:cartForm.value
		}).then(res=>{
			context?.$toolUtil.message('添加成功','success')
			getInCartList()
		})
	}
	const buyNumber = ref(1)
	//立即购买
	const buyNow = () => {
		if (isOffShelf.value) {
			context?.$toolUtil.message('该菜品已下架', 'warning')
			return
		}
		//单次购买限制
		if(purchaseLimitValue.value > 0 && purchaseLimitValue.value < buyNumber.value){
			context?.$toolUtil.message(`每人只能购买${purchaseLimitValue.value}件商品`,'error')
			return false
		}
		//库存不足
		if(stockValue.value <= 0){
			context?.$toolUtil.message('商品售罄','error')
			return false
		}
		//库存限制
		if(stockValue.value > 0 && stockValue.value < buyNumber.value){
			context?.$toolUtil.message('库存不足','error')
			return false
		}
		let data = {
			source_table: tableName,
			good_id: detail.value.id,
			goodid: detail.value.id,
			good_name: title.value,
			goodname: title.value,
			buy_number: buyNumber.value,
			buynumber: buyNumber.value,
			user_id: context?.$toolUtil.storageGet('user_id'),
			userid: context?.$toolUtil.storageGet('user_id'),
			discount_price: Number(priceText.value) || 0,
			discountprice: Number(priceText.value) || 0,
			good_type: dishCategoryText.value,
			goodtype: dishCategoryText.value,
			picture: bannerList.value[0],
			price: Number(originalPriceText.value) || 0
		}
		context?.$toolUtil.storageSet('orders_good',JSON.stringify([data]))
		let query = {
			type: 1
		}
		router.push({path: '/index/order_confirm', query: query})
	}
	//修改
	const editClick = () => {
		router.push(`/index/${tableName}Add?id=${detail.value.id}&&type=edit`)
	}
	//删除
	const delClick = () => {
		ElMessageBox.confirm(`是否删除此${formName}？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(()=>{
			context?.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: [detail.value.id]
			}).then(res=>{
				context?.$toolUtil.message('删除成功','success',()=>{
					history.back()
				})
			})

		}).catch(_ => {})
	}
	onMounted(()=>{
		init()
	})
</script>
<style lang="scss" scoped>
	// 返回盒子
	.back_view {
		border-radius: 4px;
		padding: 10px 0px;
		margin: 10px auto;
		background: none;
		width: 100%;
		text-align: left;
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
	// 面包屑盒子
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

	.detail_view{
		// 轮播图
		.swiper_view {
		}
		// 文字区
			.info_view {

				.title_view {

				.detail_title {
					span {
						font-size: 22px;
						font-weight: 600;
						color: #333;
					}
				}
				// 收藏盒子
				.collect_view {
					border: 0px solid #eee;
					cursor: pointer;
					border-radius: 10px;
					padding: 10px 0px;
					color: #999;
					background: #fff;
					display: flex;
					width: auto;
					line-height: 1;
					justify-content: center;
					align-items: center;
					.iconfont {
						margin: 0 5px 0 0;
						color: #888;
					}
					.iconfontActive {
						margin: 0 5px 0 0;
						color: #feb043;
					}
					span {
						color: #666;
					}
					.textActive {
						color: #feb043;
					}
				}
				.collect_view:hover {
				}
				.collect_view:active {
					transform: scale(0.8);
				}
			}

			.info_item {
				display: flex;
				align-items: center;
				gap: 12px;
				margin: 8px 0;

				.info_label {
					width: 100px;
					text-align: right;
					color: #666;
					flex-shrink: 0;
				}
				.info_text {
					flex: 1;
					color: #333;
				}
				.info_price {
					flex: 1;
					color: #333;
				}
			}
			.btn_view {
				// 数量输入框
				.inputNumber {
					:deep(.el-input-number__decrease) {
						i {
						}
					}
					:deep(.el-input-number__increase) {
						i {
						}
					}
					:deep(.el-input) {
						.el-input__wrapper {
						}
					}
				}
				// 加入购物车-按钮
				.addCart_btn {
				}
				// 悬浮
				.addCart_btn:hover {
				}
				// 立即购买-按钮
				.buyNow_btn {
				}
				// 悬浮
				.buyNow_btn:hover {
				}
				// 修改-按钮
				.edit_btn {
				}
				// 悬浮
				.edit_btn:hover {
				}
				// 删除-按钮
				.del_btn {
				}
				// 悬浮
				.del_btn:hover {
				}
			}
		}
	}

	//底部盒子
	.tabs_view {
		:deep(.el-tabs__header) {
			background: transparent;
			border: none;
		}
		// 头部
		:deep(.el-tabs__nav-scroll) {
			.el-tabs__nav {
				.el-tabs__item {
				}
				.el-tabs__item:hover {
				}
				.is-active {
				}
			}
		}
		// 内容区
		:deep(.el-tabs__content) {
		}
		//评价
		//我的评价
		.my_comment_view {

			.my_comment_form {
				// 输入框
				:deep(.el-textarea__inner) {
				}
			}
			// 按钮盒子
			.comment_btn {
				// 评价按钮
				.add_btn {
				}
				// 悬浮
				.add_btn:hover {
				}
				// 重置按钮
				.reset_btn {
				}
				// 悬浮
				.reset_btn:hover {
				}
			}
		}

		//评价列表
		.comment_list {

			.comment {
				.comment_top {

					.comment_user {
						.comment_user_img {

							img {
								border-radius: 50%;
								width: 40px;
								height: 40px;
							}
						}
						.comment_user_info {
						}
					}
					.comment_time{
					}
				}

				.comment_bottom {
					.comment_content {
						margin: 10px 0;
					}
					.comment_action {
						text-align: right;
						.del {
							color: #f56c6c;
							font-size: 14px;
							cursor: pointer;
						}
						.del:hover {
							text-decoration: underline;
						}
					}
					.comment_reply {
						margin-top: 6px;
						padding: 8px 10px;
						border-left: 3px solid var(--theme-color);
						background: #fafafa;
						border-radius: 4px;
						font-size: 14px;
						line-height: 1.6;
						color: #333;
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

					}
				}
				}
			}
			// 跳页
			:deep(.el-pagination__jump) {
				// 输入框
				.el-input {

				}
			}
		}
	}
</style>
