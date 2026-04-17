<template>
	<div class="list-page" :style='{"border":"0px solid #888","padding":"40px 16% 20px","margin":"20px auto 40px","borderRadius":"6px","background":"#fff","width":"100%","fontSize":"16px","position":"relative"}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb">订单</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            </div>
        <div class="back_view" style="width: 100%; margin-top: 10px; text-align: left;">
            <el-button class="back_btn" @click="goToHome" type="primary">返回</el-button>
        </div>
		<el-tabs v-model="orderStatus" type="card" class="demo-tabs" @tab-change="statusChange" style="width: 100%;">
			<el-tab-pane label="全部" :name="''"></el-tab-pane>
			<el-tab-pane v-if="btnAuth('orders/未支付','查看')" label="未支付" name="未支付"></el-tab-pane>
			<el-tab-pane label="已支付" name="已支付"></el-tab-pane>
			<el-tab-pane label="已完成" name="已完成"></el-tab-pane>
			<el-tab-pane label="已退款" name="已退款"></el-tab-pane>
			<el-tab-pane label="已取消" name="已取消"></el-tab-pane>
		</el-tabs>
		<el-table v-loading="listLoading" border :stripe='true' @selection-change="handleSelectionChange" ref="table"
			:data="list" @row-click="listChange">
			<el-table-column type="selection" width="55" :resizable='true' align="left" header-align="left" />
			<el-table-column label="序号" width="120" :resizable='true' align="left" header-align="left">
				<template #default="scope">{{ scope.$index + 1}}</template>
			</el-table-column>
			<el-table-column label="订单编号" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.order_id}}
				</template>
			</el-table-column>
			<el-table-column label="商品" width="200px" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<div style="display: flex;align-items: center;">
						<div v-if="scope.row.picture">
							<el-image v-if="scope.row.picture.substring(0,4)=='http'" preview-teleported
								:preview-src-list="[scope.row.picture.split(',')[0]]"
								:src="scope.row.picture.split(',')[0]" style="width:100px;height:100px"></el-image>
							<el-image v-else preview-teleported
								:preview-src-list="[$config.url+scope.row.picture.split(',')[0]]"
								:src="$config.url+scope.row.picture.split(',')[0]" style="width:100px;height:100px">
							</el-image>
						</div>
						<div v-else>无图片</div>
						<span style="margin-left: 10px;">{{scope.row.good_name}}</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="购买数量" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.buy_number}}
				</template>
			</el-table-column>
			<el-table-column label="价格" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span v-if="scope.row.type!=2" style="font-size: 12px;">￥</span>{{scope.row.price}} <span
						v-if="scope.row.type==2">积分</span>
				</template>
			</el-table-column>
			<el-table-column label="折扣价" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span v-if="scope.row.type!=2" style="font-size: 12px;">￥</span>{{ scope.row.discount_price ?? scope.row.discountprice ?? scope.row.discountPrice ?? '-' }}
					<span v-if="scope.row.type==2">积分</span>
				</template>
			</el-table-column>
			<el-table-column label="总价" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span v-if="scope.row.type!=2" style="font-size: 12px;">￥</span>{{scope.row.total}} <span
						v-if="scope.row.type==2">积分</span>
				</template>
			</el-table-column>
			<el-table-column label="折扣总价" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span v-if="scope.row.type!=2" style="font-size: 12px;">￥</span>{{ scope.row.discount_total ?? scope.row.discounttotal ?? scope.row.discountTotal ?? '-' }}
					<span v-if="scope.row.type==2">积分</span>
				</template>
			</el-table-column>
			<el-table-column label="备注" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.remark}}
				</template>
			</el-table-column>
			<el-table-column label="餐位名称" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.seat_name || '未选择'}}
				</template>
			</el-table-column>
			<el-table-column label="下单时间" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.addtime}}
				</template>
			</el-table-column>
			<el-table-column label="操作" :resizable='true' align="left" header-align="left" width="200px">
				<template #default="scope">
					<el-button class="pay_btn" v-if="scope.row.status=='未支付'" type="primary" @click="payClick(scope.row)">
						余额支付
					</el-button>
					<el-button class="cancel_btn" v-if="scope.row.status=='未支付'" type="danger" @click="cancelClick(scope.row)">
						取消
					</el-button>
					<el-button class="refundPrice_btn" v-if="scope.row.status=='已支付'" type="danger" @click="refundPriceClick(scope.row)">
						退款
					</el-button>

					<el-button class="confirm_btn" v-if="scope.row.status=='已发货'" type="success" @click="confirmGoodClick(scope.row)">
						确认收货
					</el-button>
                    <el-button class="toDetail_btn" v-if="scope.row.status=='已完成' && !isReviewed(scope.row)" type="warning" @click="toDetailClick(scope.row)">
						评价
					</el-button>
                    <el-button class="toDetail_btn" v-else-if="scope.row.status=='已完成' && isReviewed(scope.row)" type="info" disabled>
                        已评价
                    </el-button>
				</template>
			</el-table-column>
		</el-table>
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
			@current-change="currentChange" />
		<el-dialog v-model="commentVisible" title="评价" width="50%">
			<el-form :model="commentForm" label-width="80px">
				<el-form-item label="评分">
					<el-rate v-model="commentForm.score" />
				</el-form-item>
				<el-form-item label="评价内容">
					<el-input v-model="commentForm.content" type="textarea" :rows="5" placeholder="请输入评价内容"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="commentVisible = false">取消</el-button>
					<el-button type="primary" @click="commentSave">提交</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		nextTick,
	} from 'vue';
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useRoute,
		useRouter
	} from 'vue-router';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	const route = useRoute()
	//基础信息
	const tableName = 'orders'
	const formName = '订单'
	const table = ref(null)
	const selRows = ref([])
	const list = ref([])
	const listLoading = ref(false)
	const listQuery = ref({
		page: 1,
		limit: 20,
		user_id: context?.$toolUtil.storageGet('user_id'),
		sort:'id',
		order:'desc'
	})
    //基础信息
    const orderStatus = ref('')
    if(['未支付'  ,'已支付','已完成','已退款','已取消'].includes(route.query.menuJump)){
        orderStatus.value = route.query.menuJump
    }
    
    //判断是否从个人中心跳转
    const centerType = ref(false)
    if(route.query.centerType){
        centerType.value = true
    }

    //权限验证
    const btnAuth = (e,a)=>{
        return context?.$toolUtil.isBackAuth(e,a)
    }
    //返回首页
    const goToHome = () => {
        if(centerType.value){
            router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
        }else{
            router.push('/index/home')
        }
    }
    //返回个人中心
    const backClick = () => {
        router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
    }

	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	//列表数据
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		if (orderStatus.value) {
			params['status'] = orderStatus.value
		}
		context?.$http({
			url: 'orders/list',
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = (res.data.data.list || []).map((raw) => {
				const item = raw || {}
                item.id = item.id
				item.order_id = item.order_id ?? item.orderid ?? item.orderId ?? ''
				item.source_table = item.source_table ?? item.tablename ?? ''
				item.good_name = item.good_name ?? item.goodname ?? ''
				item.good_id = item.good_id ?? item.goodid
				item.buy_number = Number(item.buy_number ?? item.buynumber ?? 1)
				item.discount_price = item.discount_price ?? item.discountprice
				item.seat_name = item.seat_name ?? item.seatName
				return item
			})
            total.value = Number(res.data.data.total)
		})
	}
	//分页
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const total = ref(0)
	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}
	//分页

	//tab切换
	const statusChange = () => {
		listQuery.value.page = 1
		getList()
	}
	//余额支付
	const payClick = (row) => {
		ElMessageBox.confirm(`是否确定支付该订单？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			const sourceTable = row.source_table ?? row.tablename ?? ''
			context?.$http({
				url: `${sourceTable}/info/${row.good_id}`,
				method: 'get'
			}).then(res => {
				let data = res.data.data
				const payAmount = parseFloat(row.discount_total ?? row.discounttotal ?? row.discountTotal ?? row.total ?? 0)
				if (userinfo.value.balance < payAmount) {
					context?.$toolUtil.message('余额不足', 'error')
					return
				}
				//减去用户余额
				const currentBalance = parseFloat(userinfo.value.balance ?? 0)
				userinfo.value.balance = Number((currentBalance - payAmount).toFixed(2))
				//如果商品存在库存，则减去商品库存
				if (data.stock) {
					data.stock = parseInt(data.stock) - parseInt(row.buy_number)
				}
                //更新商品信息
                context?.$http.post(`${sourceTable}/update`,data)

				//更新用户信息
				context?.$http({
					url: `${context?.$toolUtil.storageGet('frontSessionTable')}/update`,
					method: 'post',
					data: userinfo.value
				}).then(obj => {
					row.status = '已支付'
					//修改订单状态
					context?.$http({
						url: 'orders/update',
						method: 'post',
						data: row
					}).then(res1 => {
                        context.$message.success('支付成功')
                        getSession()
                        statusChange()
					})
				})
			})
		}).catch(_ => {})
	}
	//取消订单
	const cancelClick = (row) => {
		ElMessageBox.confirm(`是否取消该订单？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			//未完成支付，未减去用户余额，未减去库存，未累加积分，则不需要用户操作跟商品库存操作
			row.status = '已取消'
			//修改订单状态
			context?.$http({
				url: 'orders/update',
				method: 'post',
				data: row
			}).then(res1 => {
                context.$message.success('取消成功')
                getSession()
                statusChange()
			})
		}).catch(_ => {})
	}
    //返回商品对象，如果商品存在库存,则返还库存
    const returnLimit = async (order)=>{
        const sourceTable = order.source_table ?? order.tablename ?? ''
        let res = await context.$http.get(`${sourceTable}/info/${order.good_id}`)
        let data = res.data.data
        if(data.stock){ //如果商品存在库存，则加回去
            data.stock = parseInt(data.stock) + parseInt(order.buy_number)
            context.$http.post(`${sourceTable}/update`,data)
        }
        return data
    }
	// 退款
	const refundPriceClick = (row) => {
		ElMessageBox.confirm(`是否对该订单申请退款？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(async () => {
            row.status = '申请退款'
            // 修改订单状态
            context.$http.post('orders/update',row).then(res=>{
                context.$message.success("已申请退款，等待审核")
                getSession()
                statusChange()
            })
		}).catch(_ => {})
	}

	//确认收货
	const confirmGoodClick = (row) => {
		ElMessageBox.confirm(`是否确认收货？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			//直接完成支付，已减去用户余额，已减去库存，已累加积分，则不需要用户操作跟商品库存操作
			row.status = '已完成'
			//修改订单状态
			context?.$http({
				url: 'orders/update',
				method: 'post',
				data: row
			}).then(res1 => {
                context.$message.success('确认收货成功')
                getSession()
                statusChange()
			})
		}).catch(_ => {})
	}
	//评价
	const commentVisible = ref(false)
	const commentForm = ref({
		content: '',
		score: 0,
		ref_id: '',
		user_id: '',
		nickname: '',
		avatar_url: '',
	})
    const currentOrder = ref({})

	const toDetailClick = (row) => {
        if (isReviewed(row)) {
            context?.$toolUtil.message('该订单已评价','warning')
            return
        }
        currentOrder.value = row
		commentForm.value = {
			content: '',
			score: 0,
			ref_id: row.good_id,
			user_id: context?.$toolUtil.storageGet('user_id'),
			nickname: context?.$toolUtil.storageGet('frontName'),
			avatar_url: context?.$toolUtil.storageGet('headportrait') ? context?.$toolUtil.storageGet('headportrait') : '',
		}
		commentVisible.value = true
	}

	const commentSave = () => {
        if(!commentForm.value.content){
            context?.$toolUtil.message('请输入评价内容','error')
            return
        }
		if(!commentForm.value.score){
			context?.$toolUtil.message('请选择评分','error')
			return
		}
		let sensitiveWords = "妈的;妈逼;碧莲";
		let sensitiveWordsArr = [];
		if(sensitiveWords) {
		    sensitiveWordsArr = sensitiveWords.split(",");
		}
		for(var i=0; i<sensitiveWordsArr.length; i++){
		    var reg = new RegExp(sensitiveWordsArr[i],"g");
		    if (commentForm.value.content.indexOf(sensitiveWordsArr[i]) > -1) {
		        commentForm.value.content = commentForm.value.content.replace(reg,"**");
		    }
		}
		context?.$http({
			url: `discuss${currentOrder.value.source_table}/add`,
			method: 'post',
			data: commentForm.value
		}).then(res => {
			context?.$toolUtil.message('评价成功', 'success')
			commentVisible.value = false
            markReviewed(currentOrder.value)
		})
	}
	const userinfo = ref({})
	const getSession = () => {
		context?.$http({
			url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
			method: 'get'
		}).then(res => {
			userinfo.value = res.data.data
		})
	}
    const reviewedOrders = ref({})
    const isReviewed = (row) => {
        const orderRowId = row?.id
        if (!orderRowId) return false
        return !!reviewedOrders.value[orderRowId]
    }
    const markReviewed = (row) => {
        const orderRowId = row?.id
        if (!orderRowId) return
        reviewedOrders.value[orderRowId] = true
    }
	const init = () => {
		getSession()
        getList()
	}
	init()
</script>

<style lang="scss" scoped>
    .section_title{
        span{
        }
    }
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
	// 切换栏
	.demo-tabs {
		// 头部
		:deep(.el-tabs__header) {
			border: 0px solid #e4e7ed;
			padding: 0;
			box-shadow: none;
			margin: 15px 0;
			width: 100%;
			border-width: 1px 0;
			line-height: auto;
			height: auto;
			// 滑动区
			.el-tabs__nav-scroll {
				border: 0;
				display: flex;
				width: 100%;
				justify-content: center;
				// list
				.el-tabs__nav {
					border: 0;
					margin: 30px auto;
					width: 100%;
					text-align: center;
					// item
					.el-tabs__item {
						border: 1px solid #e4e7ed;
						padding: 0 20px;
						margin: 0 10px 0 0;
						color: #333;
						background: #fff;
						font-size: 16px;
						line-height: 40px;
						transition: all 0s;
						height: 40px;
					}
					// item active
					.is-active {
						border: 1px solid var(--theme);
						padding: 0 20px;
						margin: 0 10px 0 0;
						color: #fff;
						background: var(--theme);
						font-size: 16px;
						border-color: #fff;
						line-height: 40px;
						height: 40px;
					}
					// item hover
					.el-tabs__item:hover {
						border: 1px solid var(--theme);
						padding: 0 20px;
						color: #fff;
						background: var(--theme);
						border-color: #fff;
						line-height: 40px;
						height: 40px;
					}
				}
			}
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
							// 支付
							.pay_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 5px 10px 0;
								color: #fff;
								background: var(--theme);
								width: auto;
								font-size: 14px;
								height: 32px;
							}
							// 支付-悬浮
							.pay_btn:hover {
								background: rgba(85, 200, 85, .8);
							}
							// 取消订单
							.cancel_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 5px 10px 0;
								color: #fff;
								background: rgba(200, 75, 75, 1);
								width: auto;
								font-size: 14px;
								height: 32px;
							}
							// 取消订单-悬浮
							.cancel_btn:hover {
								background: rgba(200, 75, 75, .8);
							}
							// 退款
							.refundPrice_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 5px 10px 0;
								color: #fff;
								background: rgba(240, 160, 140, 1);
								width: auto;
								font-size: 14px;
								height: 32px;
							}
							// 退款-悬浮
							.refundPrice_btn:hover {
								background: rgba(240, 160, 140, .8);
							}
							// 退货
							.refundGood_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 5px 10px 0;
								color: #fff;
								background: rgba(240, 160, 140, 1);
								width: auto;
								font-size: 14px;
								height: 32px;
							}
							// 退货-悬浮
							.refundGood_btn:hover {
								background: rgba(240, 160, 140, .8);
							}
							// 确认收货
							.confirm_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 5px 10px 0;
								color: #fff;
								background: rgba(65, 128, 230, 1);
								width: auto;
								font-size: 14px;
								height: 32px;
							}
							// 确认收货-悬浮
							.confirm_btn:hover {
								background: rgba(65, 128, 230, .8);
							}
							// 评价
							.toDetail_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 5px 10px 0;
								color: #fff;
								background: rgba(85, 0, 255, 0.5);
								width: auto;
								font-size: 14px;
								height: 32px;
							}
							// 评价-悬浮
							.toDetail_btn:hover {
								background: rgba(85, 0, 255, 0.4);
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
</style>
