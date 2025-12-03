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
					{{scope.row.goodname}}
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
					<el-input-number v-model="scope.row.buynumber" :min="1"
						@change="numberChange(scope.row)" @click.stop></el-input-number>
				</template>
			</el-table-column>
			<el-table-column label="总价" :resizable='true' align="left" header-align="left">
				<template #default="scope">
					<span style="font-size: 12px;">￥</span>{{(scope.row.realPrice * scope.row.buynumber).toFixed(2)}}
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
		limit: 20,
		userid: context?.$toolUtil.storageGet('userid')
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
			list.value = res.data.data.list
            list.value.forEach(item=>{
                item.realPrice = item.price
            })
		})
	}
	//跳转商品详情
	const detailClick = (row) => {
		router.push(`/index/${row.tablename}Detail?id=${row.goodid}`)
	}
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	//单击选中某行
	const listChange = (row) =>{
		nextTick(()=>{
			table.value.clearSelection()
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
		context?.$http({
			url: `${row.tablename}/detail/${row.goodid}`,
			method: 'get'
		}).then(res => {
			if (res.data.data.onelimittimes&&(res.data.data.onelimittimes>0)&&(row.buynumber > res.data.data.onelimittimes)) {
				row.buynumber = res.data.data.onelimittimes
				context?.$toolUtil.message(`每人单次只能购买${res.data.data.onelimittimes}件`, 'error')
				return false
			}
			context?.$http({
				url: `cart/update`,
				method: 'post',
				data: row
			}).then(obj => {})
		})
	}
	//统计总价
	const allPrice = () => {
		let price = 0
		for (let x in selRows.value) {
			price += Number((selRows.value[x].realPrice * selRows.value[x].buynumber))
		}
		return Number(price).toFixed(2)
	}
	const payClick = async () => {
		if (selRows.value.length){
			let data = []
			for(let x in selRows.value){
                let res = await context?.$http({
					url: `${selRows.value[x].tablename}/detail/${selRows.value[x].goodid}`,
					method:'get'
				})
                if(selRows.value[x].buynumber>res.data.data.alllimittimes){
                    context?.$toolUtil.message(`${selRows.value[x].goodname}库存不足`,'error')
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
		getList()
	}
	init()
</script>

<style lang="scss" scoped>
    .section_title{
        span{
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