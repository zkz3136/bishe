<template>
	<div class="list-page" :style='{"border":"0px solid #888","padding":"40px 16% 20px","margin":"20px auto 40px","borderRadius":"6px","background":"#fff","width":"100%","fontSize":"16px","position":"relative"}'>
		<div class="section_title">
            <span>{{formName}}</span>
		</div>
		<el-card style="width: 100%">
            <el-button @click="backClick">返回</el-button>
			<el-divider content-position="center">商品清单</el-divider>
			<el-table :data="list" :stripe='true'>
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
						<span style="font-size: 12px;" >￥</span>{{scope.row.realPrice}} 
					</template>
				</el-table-column>
				<el-table-column label="数量" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{scope.row.buynumber}}
					</template>
				</el-table-column>
				<el-table-column label="总价" :resizable='true' align="left" header-align="left">
					<template #default="scope">
						<span style="font-size: 12px;" >￥</span>{{(scope.row.realPrice * scope.row.buynumber).toFixed(2)}} 
					</template>
				</el-table-column>
			</el-table>
			<el-divider content-position="center">备注</el-divider>
			
			<el-input v-model="remark" placeholder="请输入备注" type="textarea"></el-input>
			
			<el-divider content-position="center">选择餐桌</el-divider>
			
			<el-select v-model="selectedCanzhuo" placeholder="请选择您所在的餐桌" style="width: 100%;" size="large">
				<el-option
					v-for="item in canzhuoList"
					:key="item.id"
					:label="item.canzhuomingcheng + ' (' + item.canzhuoweizhi + ')'"
					:value="item.canzhuomingcheng">
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
	//餐桌列表
	const canzhuoList = ref([])
	//选中的餐桌
	const selectedCanzhuo = ref('')
    //付款类型
    const payType = ref(1)
	//统计总价
	const allPrice = computed(() => {
		let price = 0
		list.value.forEach(item=>{
			price += item.realPrice * item.buynumber
		})
		return Number(price).toFixed(2)
	})
	//获取订单id
	const createOrder = () => {
		return moment().format('YYYYMMDDHHmmssSSS')+Math.random().toString().slice(2, 5);
	}
	//正常支付
	const payClick = async () => {
		//验证是否选择了餐桌
		if (!selectedCanzhuo.value) {
			context.$toolUtil.message('请选择您所在的餐桌', 'error')
			return false
		}
        let orders = []
        for(let i in list.value){
            let item = list.value[i]
            let res = await context.$http.get(`${item.tablename}/info/${item.goodid}`)
            let data = res.data.data
            let orderid = createOrder()
            orders.push({
                orderid: orderid,
                tablename: item.tablename,
                userid: user.value.id,
                role: context.$toolUtil.storageGet('frontSessionTable'),
                goodid: item.goodid,
                goodname: item.goodname,
                shangjiazhanghao: item.shangjiazhanghao,
                goodtype: item.goodtype,
                picture: item.picture,
                buynumber: item.buynumber,
                discountprice: item.realPrice,
                discounttotal: Number(item.realPay.toFixed(2)),
                price: item.realPrice,
                total: Number(item.realPay.toFixed(2)),
                type: payType.value,
                remark: remark.value,
                canzhuomingcheng: selectedCanzhuo.value,
                status: '未支付',
                goods:data
            })
        }
        //新增订单
        Promise.all(orders.map(order=>{ //批量下单，返回primise数组，等待所有请求都完成
            return context.$http.post('orders/add',order)
        })).then(resArr=>{
            let total = 0
            orders.forEach(order=>{
                total+=Number(order.total)
            })
            //判断用户余额是否充足
            if(Number(user.value.money) < Number(total.toFixed(2))){
                context.$toolUtil.message(`余额不足，请先充值`,'error',()=>{
                    router.push(`/index/${context.$toolUtil.storageGet('frontSessionTable')}Center`)
                })
                return false
            }
            //减去用户余额
            user.value.money = (parseFloat(user.value.money) - parseFloat(total)).toFixed(2)
            context.$http.post(`${context.$toolUtil.storageGet('frontSessionTable')}/update`,user.value)

            orders.forEach(order=>{
                //如果商品有库存 减去商品库存
                if(order.goods.hasOwnProperty('alllimittimes')){
                    order.goods.alllimittimes = order.goods.alllimittimes - order.buynumber
                }
                context.$http.post(`${order.tablename}/update`,order.goods)
                //更新订单状态
                context.$http.get('orders/list',{
                    params:{
                        page:1,
                        limit:1,
                        orderid: order.orderid
                    }
                }).then(res=>{
                    res.data.data.list[0].status = '已支付'
                    context.$http.post('orders/update',res.data.data.list[0])
                })
            })
            //如果存在id。说明从购物车跳转,需要删除已下单的商品
            list.value.forEach(item=>{
                if(item.id){
                    context.$http({url:'cart/delete',method:'post',data:[item.id]})
                }
            })
            //下单完成，跳转订单
            context.$toolUtil.message('购买成功','success',()=>{
                router.push('/index/ordersList')
            })
        })
	}

	//获取餐桌列表
	const getCanzhuoList = () => {
		context.$http.get('cantingxinxi/list', {
			params: {
				page: 1,
				limit: 100
				// 显示所有餐桌，不过滤状态
			}
		}).then(res => {
			if (res.data.code === 0) {
				canzhuoList.value = res.data.data.list
				console.log('获取到的餐桌列表：', canzhuoList.value)
			}
		}).catch(err => {
			console.error('获取餐桌列表失败：', err)
			context.$toolUtil.message('获取餐桌列表失败', 'error')
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
            list.value = JSON.parse(context.$toolUtil.storageGet('orders_good'))
            list.value.forEach(item=>{
                item.realPrice = item.price
                item.realPay=Number(item.realPrice*item.buynumber)
            })
        })
		//获取餐桌列表
		getCanzhuoList()
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