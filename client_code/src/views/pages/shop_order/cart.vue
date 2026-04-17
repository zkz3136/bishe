<template>
	<div class="list-page" :style='{"border":"0px solid #888","padding":"40px 16% 20px","margin":"20px auto 40px","borderRadius":"6px","background":"#fff","width":"100%","fontSize":"16px","position":"relative"}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb">购物车</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="back_view" v-if="centerType">
                <el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
            </div>
        </div>
		<el-table :data="list" v-loading="listLoading" border @selection-change="handleSelectionChange" ref="table" :stripe='true'
			@row-click="listChange">
			<el-table-column type="selection" width="55" :resizable='true' align="left" header-align="left" />
			<el-table-column label="商品名称" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.good_name}}
				</template>
			</el-table-column>
			<el-table-column label="商品图片" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<img :src="scope.row.picture?($config.url + scope.row.picture):''" alt=""
						style="width: 150px;height: 150px;">
				</template>
			</el-table-column>
			<el-table-column label="价格" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span style="font-size: 12px;">￥</span>{{scope.row.realPrice}}
				</template>
			</el-table-column>
			<el-table-column label="数量" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<el-input-number v-model="scope.row.buy_number" :min="1"
						@change="numberChange(scope.row)" @click.stop></el-input-number>
				</template>
			</el-table-column>
			<el-table-column label="总价" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span style="font-size: 12px;">￥</span>{{(scope.row.realPrice * scope.row.buy_number).toFixed(2)}}
				</template>
			</el-table-column>
			<el-table-column label="操作" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<el-button class="view_btn" type="primary" @click.native="detailClick(scope.row)">查看详情</el-button>
					<el-button class="del_btn" type="danger" @click.native="delClick(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<br>
		<div class="cart_confirm">
			<div class="cart_price">
				总价：<span style="font-size: 12px;">￥</span>{{allPrice()}}
			</div>
			<el-button class="confirm_btn" @click="payClick" type="success">提交订单</el-button>
		</div>
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
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	const route = useRoute()
	//基础信息
	const tableName = 'cart'
	const formName = '购物车'
	const table = ref(null)
	const selRows = ref([])
	const list = ref([])
	const listLoading = ref(false)
	const listQuery = ref({
		page: 1,
		limit: 20
	})
	//基础信息
    //判断是否从个人中心跳转
    const centerType = ref(false)
    //返回
    const backClick = () => {
        if(centerType.value){
            history.back()
        }else{
            router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
        }
    }

    //获取列表
	const getList = () => {
		listLoading.value = true
		context?.$http({
			url: 'cart/list',
			method: 'get',
			params: listQuery.value
		}).then(res => {
			listLoading.value = false
			list.value = (res.data.data.list || []).map((raw) => {
				const item = raw || {}
				const buyNumber = Number(item.buy_number ?? item.buynumber ?? 1)
				const realPrice = Number(item.realPrice ?? item.discount_price ?? item.discountprice ?? item.price ?? 0)
				item.good_name = item.good_name ?? item.goodname ?? ''
				item.good_id = item.good_id ?? item.goodid
                item.source_table = item.source_table ?? 'dish_info'
				item.buy_number = Number.isFinite(buyNumber) && buyNumber > 0 ? buyNumber : 1
				item.buynumber = item.buy_number
				item.realPrice = Number.isFinite(realPrice) ? realPrice : 0
				item.good_type = item.good_type ?? item.goodtype ?? ''
				item.user_id = item.user_id ?? item.userid
				item.discount_price = item.discount_price ?? item.discountprice
				return item
			})
            refreshListPrices()
		})
	}
    const getDisplayPriceByGoods = (goods) => {
        const original = Number(goods?.price ?? 0) || 0
        const discount = Number(goods?.discountprice ?? goods?.discount_price ?? goods?.discountPrice ?? 0) || 0
        if (discount > 0 && discount < original) return Number(discount.toFixed(2))
        return Number(original.toFixed(2))
    }
    const refreshListPrices = async () => {
        const rows = Array.isArray(list.value) ? list.value : []
        for (let i = 0; i < rows.length; i++) {
            const row = rows[i] || {}
            const sourceTable = row?.source_table ?? 'dish_info'
            const goodId = row?.good_id ?? row?.goodid ?? row?.goodId ?? row?.ref_id ?? row?.refid ?? row?.refId
            if (!sourceTable || !goodId) continue
            try {
                const res = await context?.$http({ url: `${sourceTable}/detail/${goodId}`, method: 'get' })
                const goods = res?.data?.data || {}
                const real = getDisplayPriceByGoods(goods)
                row.realPrice = real
                row.price = Number((Number(goods?.price ?? row.price ?? 0)).toFixed(2))
            } catch (e) {
            }
        }
    }
	//跳转商品详情
	const detailClick = (row) => {
        const sourceTable = row?.source_table ?? 'dish_info'
		const goodId = row?.good_id ?? row?.goodid ?? row?.goodId ?? row?.ref_id ?? row?.refid ?? row?.refId
		if (!sourceTable || !goodId) {
			context?.$toolUtil.message('商品数据异常，无法查看详情', 'error')
			return
		}
		router.push(`/index/${sourceTable}Detail?id=${goodId}`)
	}
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	//单击选中某行
	const listChange = (row) =>{
		nextTick(()=>{
			table.value.toggleRowSelection(row)
		})
	}
	//移除购物车
	const delClick = (id = null) => {
		if (id) {
			ElMessageBox.confirm(`是否删除选中${formName}`, '提示', {
				confirmButtonText: '是',
				cancelButtonText: '否',
				type: 'warning',
			}).then(() => {
				context?.$http({
					url: 'cart/delete',
					method: 'post',
					data: [id]
				}).then(res => {
					context?.$toolUtil.message('删除成功', 'success', () => {
						getList()
					})
				})
			}).catch(_ => {})
		}
	}
	const numberChange = (row) => {
		row.buynumber = row.buy_number
        const sourceTable = row?.source_table ?? 'dish_info'
		const goodId = row?.good_id ?? row?.goodid ?? row?.goodId ?? row?.ref_id ?? row?.refid ?? row?.refId
		if (!sourceTable || !goodId) {
			context?.$toolUtil.message('商品数据异常，无法校验限购', 'error')
			return
		}
		context?.$http({
			url: `${sourceTable}/detail/${goodId}`,
			method: 'get'
		}).then(res => {
			if (res.data.data.purchase_limit&&(res.data.data.purchase_limit>0)&&(row.buy_number > res.data.data.purchase_limit)) {
				row.buy_number = res.data.data.purchase_limit
				context?.$toolUtil.message(`每人单次只能购买${res.data.data.purchase_limit}件`, 'error')
				return false
			}
			context?.$http({
				url: `cart/update`,
				method: 'post',
				data: {
					id: row.id,
					buynumber: row.buy_number
				}
			}).then(obj => {})
		})
	}
	//统计总价
	const allPrice = () => {
		let price = 0
		for (let x in selRows.value) {
			price += Number((selRows.value[x].realPrice * selRows.value[x].buy_number))
		}
		return Number(price).toFixed(2)
	}
	const payClick = async () => {
		if (selRows.value.length){
			let data = []
			for(let x in selRows.value){
				const row = selRows.value[x] || {}
				row.buy_number = Number(row.buy_number ?? row.buynumber ?? 1)
				row.buynumber = row.buy_number
				row.good_name = row.good_name ?? row.goodname ?? ''
				row.good_id = row.good_id ?? row.goodid
                row.source_table = row.source_table ?? 'dish_info'
				row.good_type = row.good_type ?? row.goodtype ?? ''
                const sourceTable = row?.source_table ?? 'dish_info'
				const goodId = row?.good_id ?? row?.goodid ?? row?.goodId ?? row?.ref_id ?? row?.refid ?? row?.refId
				if (!sourceTable || !goodId) {
					context?.$toolUtil.message('商品数据异常，无法提交订单', 'error')
					return false
				}
                let res = await context?.$http({
					url: `${sourceTable}/detail/${goodId}`,
					method:'get'
				})
                const goods = res?.data?.data || {}
                row.realPrice = getDisplayPriceByGoods(goods)
                if(selRows.value[x].buy_number>goods.stock){
                    context?.$toolUtil.message(`${selRows.value[x].good_name}库存不足`,'error')
                    return false
                }
                if(x==selRows.value.length - 1){
                    confirmOrder()
                }
			}
		}else{
			context?.$toolUtil.message('请选择需要购买的商品','error')
		}
	}
	const confirmOrder = () => {
		context?.$toolUtil.storageSet('orders_good',JSON.stringify(selRows.value))
		router.push('/index/order_confirm')
	}
	//初始化
	const init = () => {
        if(route.query.centerType){
            centerType.value = true
        }
		if(!context?.$toolUtil.storageGet('frontToken')){
			context?.$toolUtil.storageSet('toPath',window.history.state.current)
			router.push('/login')
			return
		}
		getList()
	}
	init()
</script>

<style lang="scss" scoped>
    .section_title{
        span{
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
							//未选中样式
							.el-checkbox {
								//复选框
								.el-checkbox__inner {
									background: #fff;
									border-color: #999;
								}
							}
							//选中样式
							.is-checked {
								//复选框
								.el-checkbox__inner {
									background: var(--theme);
									border-color: var(--theme);
								}
							}
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
							// 查看详情
							.view_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 10px 0 0;
								outline: none;
								color: #fff;
								background: #2abdb8;
								width: auto;
								font-size: 14px;
								height: 32px;
							}
							// 查看详情-悬浮
							.view_btn:hover {
							}
							// 删除
							.del_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 10px 0 0;
								outline: none;
								color: #fff;
								background: rgba(255, 73, 73, 1);
								width: auto;
								font-size: 14px;
								min-width: 60px;
								height: 32px;
							}
							// 删除-悬浮
							.del_btn:hover {
							}
							//未选中样式
							.el-checkbox {
								//复选框
								.el-checkbox__inner {
									background: #fff;
									border-color: #999;
								}
							}
							//选中样式
							.is-checked {
								//复选框
								.el-checkbox__inner {
									background: var(--theme);
									border-color: var(--theme);
								}
							}
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
	.cart_confirm {
		padding: 0px;
		margin: 20px 0;
		background: #fff;
		display: flex;
		width: 100%;
		justify-content: flex-end;
		align-items: center;
		box-sizing: border-box;

		.cart_price {
		margin: 0 20px 0 0;
		color: #f00;
		font-weight: bold;
		font-size: 18px;
		}
		.confirm_btn {
		border: 0;
		cursor: pointer;
		border-radius: 0px;
		padding: 0 30px;
		margin: 0 0px 0 0;
		outline: none;
		color: #fff;
		background: var(--theme);
		width: auto;
		font-size: 16px;
		height: 36px;
		}
		.confirm_btn:hover {
		}
	}
</style>
