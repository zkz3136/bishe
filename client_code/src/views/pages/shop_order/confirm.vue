<template>
	<div class="list-page" :style='{"border":"0px solid #888","padding":"40px 16% 20px","margin":"20px auto 40px","borderRadius":"6px","background":"#fff","width":"100%","fontSize":"16px","position":"relative"}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb">{{formName}}</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="back_view">
                <el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
            </div>
        </div>
		<el-card style="width: 100%">
			<el-divider content-position="center">商品清单</el-divider>
			<el-table :data="list" :stripe='true'>
				<el-table-column label="商品名称" :resizable='true' align="left" header-align="left">
					<template #default="scope">
						{{scope.row.good_name || scope.row.goodname}}
					</template>
				</el-table-column>
				<el-table-column label="商品图片" :resizable='true' align="left" header-align="left">
					<template #default="scope">
						<img :src="scope.row.picture?($config.url + scope.row.picture):''" alt=""
							style="width: 150px;height: 150px;">
					</template>
				</el-table-column>
				<el-table-column label="原价" :resizable='true' align="left" header-align="left">
					<template #default="scope">
						<span style="font-size: 12px;" >￥</span>{{ scope.row.price ?? scope.row.original_price ?? scope.row.originalPrice ?? scope.row.realPrice }}
					</template>
				</el-table-column>
				<el-table-column label="折扣价" :resizable='true' align="left" header-align="left">
					<template #default="scope">
						<span style="font-size: 12px;" >￥</span>{{scope.row.realPrice}} 
					</template>
				</el-table-column>
				<el-table-column label="数量" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{scope.row.buy_number}}
					</template>
				</el-table-column>
				<el-table-column label="折扣总价" :resizable='true' align="left" header-align="left">
					<template #default="scope">
						<span style="font-size: 12px;" >￥</span>{{(((Number(scope.row.realPrice) || 0) * (Number(scope.row.buy_number) || 0)) || 0).toFixed(2)}} 
					</template>
				</el-table-column>
			</el-table>
			<el-divider content-position="center">备注</el-divider>
			
			<el-input v-model="remark" placeholder="请输入备注" type="textarea"></el-input>
			
			<el-divider content-position="center">选择餐位</el-divider>
			
			<el-select v-model="selectedCanzhuo" placeholder="请选择您所在的餐位" style="width: 100%;" size="large">
				<el-option
					v-for="item in canzhuoList"
					:key="item.id"
					:label="item.seat_name + ' (' + item.table_location + ')'"
					:value="item.seat_name">
				</el-option>
			</el-select>
			
			<div class="order_confirm_btn" >
				<div class="order_confirm_price">
					总价：<span >￥</span>{{allPrice}}
				</div>
				<el-button class="pay_btn"  @click="payClick" type="success">余额支付</el-button>
			</div>
		</el-card>
		<br>
	</div>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		nextTick,
		computed,
		watch,
	} from 'vue';
	import {
		useRoute,
		useRouter
	} from 'vue-router';
	import moment from 'moment'
    import {
        useStore
    } from 'vuex';
    const store = useStore()
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	const route = useRoute()
	//基础信息
	const tableName = 'orders'
	const formName = '订单提交'
	//基础信息
    // 返回
    const backClick = () =>{
        history.back()
    }
	const list = ref([])
	const remark = ref('')
	//餐位列表
	const canzhuoList = ref([])
	//选中的餐位
	const selectedCanzhuo = ref('')
	const orderDraftKey = 'order_confirm_draft'
    //付款类型
    const payType = ref(1)
	const normalizeOrderGood = (raw) => {
		const item = raw || {}
		const goodId = item.good_id ?? item.goodid ?? item.goodId ?? item.ref_id ?? item.refid ?? item.refId
		const buyNumber = Number(item.buy_number ?? item.buynumber ?? item.buyNumber ?? 1)
		const realPrice = Number(item.realPrice ?? item.discount_price ?? item.discountprice ?? item.price ?? 0)
        item.source_table = item.source_table ?? item.sourceTable ?? item.table_name ?? item.tableName ?? 'dish_info'
		item.good_id = goodId
		item.goodid = item.goodid ?? goodId
		item.good_name = item.good_name ?? item.goodname ?? ''
		item.goodname = item.goodname ?? item.good_name ?? ''
		item.buy_number = Number.isFinite(buyNumber) && buyNumber > 0 ? buyNumber : 1
		item.buynumber = item.buynumber ?? item.buy_number
		item.realPrice = Number.isFinite(realPrice) ? realPrice : 0
		item.realPay = Number((Number(item.realPrice) || 0) * (Number(item.buy_number) || 0))
		item.good_type = item.good_type ?? item.goodtype ?? ''
		item.goodtype = item.goodtype ?? item.good_type ?? ''
		item.user_id = item.user_id ?? item.userid
		item.userid = item.userid ?? item.user_id
		return item
	}
    const getDisplayPriceByGoods = (goods) => {
        const original = Number(goods?.price ?? 0) || 0
        const discount = Number(goods?.discountprice ?? goods?.discount_price ?? goods?.discountPrice ?? 0) || 0
        if (discount > 0 && discount < original) return Number(discount.toFixed(2))
        return Number(original.toFixed(2))
    }
    const refreshOrderPrices = async () => {
        let changed = false
        const rows = Array.isArray(list.value) ? list.value : []
        for (let i = 0; i < rows.length; i++) {
            const row = rows[i] || {}
            const sourceTable = row?.source_table ?? 'dish_info'
            const goodId = row?.good_id ?? row?.goodid ?? row?.goodId ?? row?.ref_id ?? row?.refid ?? row?.refId
            if (!sourceTable || !goodId) continue
            try {
                const res = await context.$http.get(`${sourceTable}/detail/${goodId}`)
                const goods = res?.data?.data || {}
                const latestOriginal = Number(goods?.price ?? row.price ?? 0)
                const latestReal = getDisplayPriceByGoods(goods)
                if (Number(row.realPrice) !== latestReal || Number(row.price || 0) !== Number(latestOriginal.toFixed(2))) {
                    changed = true
                }
                row.price = Number(latestOriginal.toFixed(2))
                row.realPrice = latestReal
            } catch (e) {
            }
        }
        if (changed) {
            context.$toolUtil.message('检测到价格更新，已为您同步最新价格', 'success')
        }
    }
	//统计总价
	const allPrice = computed(() => {
		let price = 0
		list.value.forEach(item=>{
			price += (Number(item.realPrice) || 0) * (Number(item.buy_number) || 0)
		})
		return Number(price).toFixed(2)
	})
	//获取订单id
	const createOrder = () => {
		return moment().format('YYYYMMDDHHmmssSSS')+Math.random().toString().slice(2, 5);
	}
	const calcDiscountPrice = (goods, originalPrice, fallbackPrice) => {
		const original = Number(originalPrice) || 0
		const goodsDiscount = Number(goods?.discountprice ?? goods?.discount_price ?? goods?.discountPrice ?? 0) || 0
		if (goodsDiscount > 0 && goodsDiscount < original) return goodsDiscount
		const fallback = Number(fallbackPrice) || 0
		if (fallback > 0 && fallback < original) return fallback
		return original
	}
	//正常支付
	const payClick = async () => {
		//验证是否选择了餐位
		if (!selectedCanzhuo.value) {
			context.$toolUtil.message('请选择您所在的餐位', 'error')
			return false
		}
		const orderGoods = (list.value || []).map(normalizeOrderGood)
		const orders = []
		const goodsSnapshots = []
		for (let i = 0; i < orderGoods.length; i++) {
			const item = orderGoods[i]
			const sourceTable = item.source_table
			const goodId = item.good_id
			if (!sourceTable || !goodId) {
				context.$toolUtil.message('商品数据异常，无法提交订单', 'error')
				return false
			}
			const goodsRes = await context.$http.get(`${sourceTable}/info/${goodId}`)
			const goods = goodsRes.data.data
			goodsSnapshots.push({ sourceTable, goods, buyNumber: item.buy_number })
			const originalPrice = Number(goods?.price ?? item.price ?? item.realPrice ?? 0)
			const originalTotal = Number(originalPrice) * Number(item.buy_number || 0)
			const discountPrice = calcDiscountPrice(goods, originalPrice, item.realPrice)
			const discountTotal = Number(discountPrice) * Number(item.buy_number || 0)
			const orderid = createOrder()
			orders.push({
				orderid,
				tablename: item.tablename || sourceTable,
				userid: user.value.id,
				role: context.$toolUtil.storageGet('frontSessionTable'),
				goodid: item.goodid ?? goodId,
				goodname: item.goodname ?? item.good_name ?? '',
				goodtype: item.goodtype ?? item.good_type ?? '',
				picture: item.picture,
				buynumber: item.buy_number,
				price: Number(originalPrice.toFixed(2)),
				discountprice: Number((Number(discountPrice) || 0).toFixed(2)),
				total: Number(originalTotal.toFixed(2)),
				discounttotal: Number((Number(discountTotal) || 0).toFixed(2)),
				type: String(payType.value),
				remark: remark.value,
				tel: user.value.mobile || user.value.mobile,
				seat_name: selectedCanzhuo.value,
				status: '已支付',
			})
		}

		let total = 0
		orders.forEach(order => {
			total += Number(order.discounttotal ?? order.total) || 0
		})
		if (Number(user.value.balance) < Number(total.toFixed(2))) {
			const redirect = encodeURIComponent('/index/order_confirm?fromRecharge=1')
			const frontSession = context.$toolUtil.storageGet('frontSessionTable')
			const payTotal = Number(total.toFixed(2))
			const balance = Number(user.value.balance || 0)
			const needAmount = Number((payTotal - balance).toFixed(2))
			context.$toolUtil.message(`余额不足，还需充值 ${needAmount.toFixed(2)} 元`, 'error', () => {
				try {
					context.$toolUtil.storageSet(orderDraftKey, JSON.stringify({ remark: remark.value || '', seat_name: selectedCanzhuo.value || '' }))
				} catch (e) {}
				router.push(`/index/${frontSession}Center?openRecharge=1&redirect=${redirect}&needAmount=${needAmount.toFixed(2)}&balance=${balance.toFixed(2)}&payTotal=${payTotal.toFixed(2)}`)
			})
			return false
		}

		await Promise.all(orders.map(order => context.$http.post('orders/add', order)))

		user.value.balance = (parseFloat(user.value.balance) - parseFloat(total)).toFixed(2)
		context.$http.post(`${context.$toolUtil.storageGet('frontSessionTable')}/update`, user.value)

		goodsSnapshots.forEach(snapshot => {
			if (snapshot.goods && Object.prototype.hasOwnProperty.call(snapshot.goods, 'stock')) {
				const currentStock = Number(snapshot.goods.stock) || 0
				snapshot.goods.stock = currentStock - Number(snapshot.buyNumber || 0)
			}
			context.$http.post(`${snapshot.sourceTable}/update`, snapshot.goods)
		})

		list.value.forEach(item => {
			if (item.id) {
				context.$http({ url: 'cart/delete', method: 'post', data: [item.id] })
			}
		})

		context.$toolUtil.message('购买成功', 'success', () => {
			try {
				context.$toolUtil.storageRemove(orderDraftKey)
			} catch (e) {}
			router.push('/index/ordersList')
		})
	}

	//获取餐位列表
	const getCanzhuoList = () => {
		context.$http.get('restaurant_info/list', {
			params: {
				page: 1,
				limit: 100
				// 显示所有餐位，不过滤状态
			}
		}).then(res => {
			if (res.data.code === 0) {
				canzhuoList.value = res.data.data.list
				console.log('获取到的餐位列表：', canzhuoList.value)
			}
		}).catch(err => {
			console.error('获取餐位列表失败：', err)
			context.$toolUtil.message('获取餐位列表失败', 'error')
		})
	}
	//获取个人信息
	const user = ref({})
	//初始化
	const init = () => {
		if (route.query.type) {
			payType.value = route.query.type
		}
        store.dispatch('user/getSession').then(res=>{
            user.value = res.data.data
			const raw = context.$toolUtil.storageGet('orders_good')
			const parsed = raw ? JSON.parse(raw) : []
			list.value = (Array.isArray(parsed) ? parsed : []).map(normalizeOrderGood)
            refreshOrderPrices()
        })
		//获取餐位列表
		getCanzhuoList()
		const shouldRestore = String(route?.query?.fromRecharge || '') === '1'
		const draftRaw = context.$toolUtil.storageGet(orderDraftKey)
		if (shouldRestore && draftRaw) {
			try {
				const draft = JSON.parse(draftRaw)
				if (draft && typeof draft === 'object') {
					if (draft.remark != null) remark.value = String(draft.remark || '')
					if (draft.seat_name != null) selectedCanzhuo.value = String(draft.seat_name || '')
				}
			} catch (e) {}
		} else if (!shouldRestore && draftRaw) {
			try {
				context.$toolUtil.storageRemove(orderDraftKey)
			} catch (e) {}
		}
	}
	init()
	watch([remark, selectedCanzhuo], ([r, s]) => {
		try {
			context.$toolUtil.storageSet(orderDraftKey, JSON.stringify({ remark: r || '', seat_name: s || '' }))
		} catch (e) {}
	})
</script>

<style lang="scss" scoped>
    .section_title{
        margin: 0 0 10px;
        span{
            font-size: 22px;
            font-weight: 600;
            color: #333;
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
	.back_view {
		border-radius: 4px;
		padding: 10px 0px;
		margin: 10px auto;
		background: none;
		width: 100%;
		text-align: left;
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
		.back_btn:hover {
		}
	}
	// 表格样式
	.el-table {
		padding: 0;
		margin: 20px 0 0;
		background: #fff;
		width: 100%;
		font-size: 15px;
		border-color: #eee;
		border-width: 1px 0 0 1px;
		border-style: solid;
		:deep(.el-table__header-wrapper) {
			thead {
				color: #333;
				font-weight: 500;
				width: 100%;
				tr {
					background: #fff;
					th {
						padding: 8px 0;
						background: var(--theme);
						border-color: #eee;
						border-width: 0 1px 1px 0;
						border-style: solid;
						text-align: left;
						.cell {
							padding: 0 10px;
							word-wrap: normal;
							color: #fff;
							white-space: nowrap;
							font-weight: bold;
							display: flex;
							vertical-align: middle;
							line-height: 24px;
							text-overflow: ellipsis;
							word-break: break-all;
							width: 100%;
							align-items: center;
							position: relative;
							min-width: 110px;
						}
					}
				}
			}
		}
		:deep(.el-table__body-wrapper) {
			tbody {
				width: 100%;
				tr {
					background: #fff;
					td {
						padding: 12px 0;
						color: #666;
						background: #fff;
						border-color: #eee;
						border-width: 0 1px 1px 0;
						border-style: solid;
						text-align: left;
						.cell {
							padding: 0 10px;
							overflow: hidden;
							word-break: break-all;
							white-space: normal;
							line-height: 24px;
							text-overflow: ellipsis;
						}
					}
				}
				tr.el-table__row--striped {
					td {
						background: #FAFAFA !important;
					}
				}
				tr:hover {
					td {
						padding: 12px 0;
						color: #333;
						background: rgba(245, 245, 245, 1);
						border-color: #eee;
						border-width: 0 1px 1px 0;
						border-style: solid;
						text-align: left;
					}
				}
			}
		}
	}
	// 分割线样式
	:deep(.el-divider) {
		border: none;
		background: #dcdfe6;
		// 分割线文字样式
		.el-divider__text {
			font-weight: 600;
			font-size: 18px;
		}
	}
	// 底部盒子
	.order_confirm_btn {
		padding: 0;
		margin: 20px 0;
		background: #fff;
		display: flex;
		width: 100%;
		justify-content: flex-end;
		align-items: center;
		box-sizing: border-box;
		// 总价
		.order_confirm_price {
			margin: 0 20px 0 0;
			color: #f00;
			font-weight: bold;
			font-size: 18px;
		}
		// 余额支付
		.pay_btn {
			border: 0;
			cursor: pointer;
			border-radius: 0px;
			padding: 0 24px;
			margin: 0 10px 0 0;
			color: #fff;
			background: var(--theme);
			width: auto;
			font-size: 14px;
			height: 36px;
		}
		// 余额支付-悬浮
		.pay_btn:hover {
		}
	}
</style>
