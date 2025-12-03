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
        <div class="back_view" style="width: 100%; margin-top: 10px; text-align: right;">
            <el-button class="back_btn" @click="goToHome" type="primary">返回首页</el-button>
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
					{{scope.row.orderid}}
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
						<span style="margin-left: 10px;">{{scope.row.goodname}}</span>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="购买数量" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.buynumber}}
				</template>
			</el-table-column>
			<el-table-column label="价格" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span v-if="scope.row.type!=2" style="font-size: 12px;">￥</span>{{scope.row.price}} <span
						v-if="scope.row.type==2">积分</span>
				</template>
			</el-table-column>
			<el-table-column label="总价" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span v-if="scope.row.type!=2" style="font-size: 12px;">￥</span>{{scope.row.total}} <span
						v-if="scope.row.type==2">积分</span>
				</template>
			</el-table-column>
			<el-table-column label="备注" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.remark}}
				</template>
			</el-table-column>
			<el-table-column label="餐桌名称" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					{{scope.row.canzhuomingcheng || '未选择'}}
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
					<el-button class="refundGood_btn" v-if="scope.row.status=='已完成'" type="danger" @click="refundGoodClick(scope.row)">
						退货
					</el-button>
					<el-button class="logistics_btn" v-if="scope.row.logistics" type="primary" @click="logisticsClick(scope.row)">
						物流
					</el-button>
					<el-button class="confirm_btn" v-if="scope.row.status=='已发货'" type="success" @click="confirmGoodClick(scope.row)">
						确认收货
					</el-button>
					<el-button class="toDetail_btn" v-if="scope.row.status=='已完成'" type="warning" @click="toDetailClick(scope.row)">
						评论
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
		<el-dialog v-model="logisticsVisible" title="物流信息" width="70%">
			<div v-html="logisticsText"></div>
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
		userid: context?.$toolUtil.storageGet('userid'),
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
        router.push('/index/home')
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
			list.value = res.data.data.list
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
			context?.$http({
				url: `${row.tablename}/info/${row.goodid}`,
				method: 'get'
			}).then(res => {
				let data = res.data.data
				if (userinfo.value.money < row.total) {
					context?.$toolUtil.message('余额不足', 'error')
					return
				}
				//如果商品存在积分，则累加用户积分
				if (data.jf) {
					userinfo.value.jf = parseInt(userinfo.value.jf) + parseInt(row.total)
				}
				//减去用户余额
				userinfo.value.money = (parseFloat(userinfo.value.money) - parseFloat(row.total)).toFixed(2)
				//如果商品存在库存，则减去商品库存
				if (data.alllimittimes) {
					data.alllimittimes = parseInt(data.alllimittimes) - parseInt(row.buynumber)
				}
                //更新商品信息
                context?.$http.post(`${row.tablename}/update`,data)

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
        let res = await context.$http.get(`${order.tablename}/info/${order.goodid}`)
        let data = res.data.data
        if(data.alllimittimes){ //如果商品存在库存，则加回去
            data.alllimittimes = parseInt(data.alllimittimes) + parseInt(order.buynumber)
            context.$http.post(`${order.tablename}/update`,data)
        }
        return data
    }
	// 退款
	const refundPriceClick = (row) => {
		ElMessageBox.confirm(`是否对该订单进行退款操作？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(async () => {
            let data = await returnLimit(row)
            row.status = '已退款'
            if (row.type == 2) {
                // 如果是积分兑换，则把减去的积分加回去
                userinfo.value.jf = parseInt(userinfo.value.jf) + parseInt(row.total)
            } else {
                // 如果是购物或者团购模式，且商品存在积分，则把加上的积分减去
                if (data.jf) {
                    userinfo.value.jf = parseInt(userinfo.value.jf) - parseInt(row.total)
                }
                // 把减去的余额加回去
                userinfo.value.money = (parseFloat(userinfo.value.money) + parseFloat(row.total)).toFixed(2)
            }
            // 修改订单状态
            context.$http.post('orders/update',row)
            // 更新用户信息
            context.$http.post(`${context.$toolUtil.storageGet('frontSessionTable')}/update`,userinfo.value).then(res=>{
                context.$message.success("退款成功")
                getSession()
                statusChange()
            })
		}).catch(_ => {})
	}
	// 退货
	const refundGoodClick = (row) => {
		ElMessageBox.confirm(`是否对该订单进行退货操作？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context?.$http({
				url: `${row.tablename}/info/${row.goodid}`,
				method: 'get'
			}).then(res => {
				let data = res.data.data
				// 如果商品存在库存，则加回去
				if (data.alllimittimes) {
					data.alllimittimes = parseInt(data.alllimittimes) + parseInt(row.buynumber)
					// 更新商品库存
					context?.$http({
						url: `${row.tablename}/update`,
						method: 'post',
						data: data
					}).then(obj1 => {})
				}
				if (row.type == 2) {
					// 如果是积分兑换，则把减去的积分加回去
					userinfo.value.jf = parseInt(userinfo.value.jf) + parseInt(row.total)
					// 更新用户信息
					context?.$http({
						url: `${context?.$toolUtil.storageGet('frontSessionTable')}/update`,
						method: 'post',
						data: userinfo.value
					}).then(obj => {
						row.status = '已退款'
						// 修改订单状态
						context?.$http({
							url: 'orders/update',
							method: 'post',
							data: row
						}).then(res1 => {
                            context.$message.success('退款成功')
                            getSession()
                            statusChange()
						})
					})
				} else {
					// 如果是购物或者团购模式，且商品存在积分，则把加上的积分减去
					if (data.jf) {
						userinfo.value.jf = parseInt(userinfo.value.jf) - parseInt(row.total)
					}
					// 把减去的余额加回去
					userinfo.value.money = (parseFloat(userinfo.value.money) + parseFloat(row.total)).toFixed(2)
					// 更新用户信息
					context?.$http({
						url: `${context?.$toolUtil.storageGet('frontSessionTable')}/update`,
						method: 'post',
						data: userinfo.value
					}).then(obj => {
						row.status = '已退款'
						// 修改订单状态
						context?.$http({
							url: 'orders/update',
							method: 'post',
							data: row
						}).then(res1 => {
                            context.$message.success('退货成功')
                            getSession()
                            statusChange()
						})
					})
				}
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
	//物流
	const logisticsVisible = ref(false)
	const logisticsText = ref('')
	const logisticsClick = (row) => {
		logisticsText.value = row.logistics
		logisticsVisible.value = true
	}
	//去评论
	const toDetailClick = (row) => {
		router.push(`/index/${row.tablename}Detail?id=${row.goodid}`)
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
							// 物流
							.logistics_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 5px 10px 0;
								outline: none;
								color: #fff;
								background: rgba(80, 180, 220, 1);
								width: auto;
								font-size: 14px;
								height: 32px;
							}
							// 物流-悬浮
							.logistics_btn:hover {
								background: rgba(80, 180, 220, .8);
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
							// 评论
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
							// 评论-悬浮
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