<template>
	<div>
		<div class="center_view">
			<el-tabs
			    v-model="orderStatus"
			    type="card"
			    class="demo-tabs"
				@tab-change="orderTabClick"
				>
				<el-tab-pane label="全部" :name="''"></el-tab-pane>
				<el-tab-pane label="未支付" name="未支付" v-if="btnAuth(`orders`,'查看')"></el-tab-pane>
				<el-tab-pane label="已支付" name="已支付" v-if="btnAuth(`orders`,'查看')"></el-tab-pane>
				<el-tab-pane label="已完成" name="已完成" v-if="btnAuth(`orders`,'查看')"></el-tab-pane>
				<el-tab-pane label="申请退款" name="申请退款" v-if="btnAuth(`orders`,'查看')"></el-tab-pane>
				<el-tab-pane label="已退款" name="已退款" v-if="btnAuth(`orders`,'查看')"></el-tab-pane>
				<el-tab-pane label="已取消" name="已取消" v-if="btnAuth(`orders`,'查看')"></el-tab-pane>
			</el-tabs>
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							订单编号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.order_id" placeholder="订单编号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="add_btn" type="success" @click="addClick" v-if="btnAuth('orders','新增')">
						新增
					</el-button>
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'删除')">
						删除
					</el-button>
					<el-button class="other_btn" type="default" :disabled="selRows.length?false:true" @click="exportClick" v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'导出')">
						导出
					</el-button>
                    <el-button class="statis_btn" type="warning" @click="echartClick1" v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'月销额')">
                        月销额
                    </el-button>
                    <el-button class="statis_btn" type="warning" @click="echartClick2" v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'年销额')">
                        年销额
                    </el-button>
                    <el-button class="statis_btn" type="warning" @click="echartClick3" v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'品销量')">
                        品销量
                    </el-button>
				</div>
			</div>
			<el-table
				v-loading="listLoading"
				border
				:stripe='false'
				@selection-change="handleSelectionChange"
				ref="table"
				v-if="true"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="order_id"
					label="订单编号">
					<template #default="scope">
						{{ scope.row.order_id ?? scope.row.orderid ?? scope.row.orderId }}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="good_name"
					label="菜品">
					<template #default="scope">
						{{ scope.row.good_name ?? scope.row.goodname ?? scope.row.goodName }}
					</template>
				</el-table-column>
				<el-table-column label="图片" min-width="140" width="120" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
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
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="buy_number"
					label="购买数量">
					<template #default="scope">
						{{ scope.row.buy_number ?? scope.row.buynumber ?? scope.row.buyNumber }}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="discount_price"
					label="单价">
					<template #default="scope">
						{{ scope.row.discount_price ?? scope.row.discountprice ?? scope.row.discountPrice }}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="discount_total"
					label="总价">
					<template #default="scope">
						{{ scope.row.discount_total ?? scope.row.discounttotal ?? scope.row.discountTotal }}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="seat_name"
					label="餐位名称">
					<template #default="scope">
						{{ (scope.row.seat_name ?? scope.row.seatName) || '未选择' }}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="status"
					label="订单状态">
					<template #default="scope">
						{{scope.row.status}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="remark"
					label="备注">
					<template #default="scope">
						{{scope.row.remark}}
					</template>
				</el-table-column>
				<el-table-column prop="addtime" label="下单时间" min-width="140" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						{{scope.row.addtime}}
					</template>
				</el-table-column>
				<el-table-column label="操作" width="300" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if=" btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'查看')" @click="infoClick(scope.row.id)">
							详情
						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)"  v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'删除')">
							删除						</el-button>
					<el-button class="operate_btn" v-if="scope.row.status=='已支付'||scope.row.status=='申请退款'" type="success" @click="completeOrderClick(scope.row)">
						完成
						</el-button>
					<el-button class="operate_btn" v-if="scope.row.status=='申请退款'" type="danger" @click="refundClick(scope.row)">
						同意退款
						</el-button>
						<el-button class="operate_btn" v-if="scope.row.status=='已完成' && btnAuth('dish_review','新增')" type="warning" @click="reviewClick(scope.row)">
							评价
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
				prev-text="<"
				next-text=">"
				:hide-on-single-page="false"
				:style='{}'
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange"  />
		</div>
		<formModel ref="formRef" @formModelChange="formModelChange"></formModel>
		<!-- 统计图弹窗 -->
		<el-dialog v-model="echartVisible" title="统计图" width="70%">
			<el-tabs v-model="echartActive" class="demo-tabs" @tab-change="echartTabClick" type="card">
                <el-tab-pane label="月销额" name="1" v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'月销额')"></el-tab-pane>
                <el-tab-pane label="年销额" name="2" v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'年销额')"></el-tab-pane>
                <el-tab-pane label="品销量" name="3" v-if="btnAuth('orders'+(orderStatus?'/'+orderStatus:''),'品销量')"></el-tab-pane>
			</el-tabs>
            <div v-if="echartActive==1" id="totalEchart1" style="width:100%;height:600px;"></div>
            <div v-if="echartActive==2" id="totalEchart2" style="width:100%;height:600px;"></div>
            <div v-if="echartActive==3" id="buynumberEchart3" style="width:100%;height:600px;"></div>
			<template #footer>
				<span class="formModel_btn_box">
					<el-button class="cancel_btn" @click="echartVisible=false">取消</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import axios from 'axios'
    import moment from "moment"
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
		onBeforeUnmount,
		watch,
		computed,
		inject
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const avatar = ref(store.state.user.avatar)
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	import formModel from './formModel.vue'
	//基础信息

	const tableName = 'orders'
	const formName = '订单'
	const route = useRoute()
	watch(() => route.query,() => {
		init()
	})
	//基础信息
	onMounted(()=>{
	})
	//列表数据
	const list = ref(null)
	const table = ref(null)
	const listQuery = ref({
		page: 1,
		limit: 10,
		sort: 'id',
		order: 'desc'
	})
	const searchQuery = ref({})
	const selRows = ref([])
	const listLoading = ref(false)
	const networkDown = ref(false)
	const orderPollingTimer = ref(null)
	const startOrderPolling = () => {
		if (orderPollingTimer.value) return
		orderPollingTimer.value = setInterval(() => {
			// 避免重复并发
			if (!listLoading.value) {
				getList()
			}
		}, 5000)
	}
	const stopOrderPolling = () => {
		if (orderPollingTimer.value) {
			clearInterval(orderPollingTimer.value)
			orderPollingTimer.value = null
		}
	}
	const listChange = (row) =>{
		nextTick(()=>{
			//table.value.clearSelection()
			table.value.toggleRowSelection(row)
		})
	}
	//列表
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		params['sort'] = 'id'
		params['order'] = 'desc'
		if(orderStatus.value){
			params['status'] = orderStatus.value
		}
		if(searchQuery.value.order_id&&searchQuery.value.order_id!=''){
			params['orderid'] = '%' + searchQuery.value.order_id + '%'
			params['order_id'] = '%' + searchQuery.value.order_id + '%'
		}
		context.$http({
			url: `${tableName}/page`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = Number(res.data.data.total)
			networkDown.value = false
		}).catch(err => {
			listLoading.value = false
			const msg = String(err?.message || err || '')
			if (msg.includes('Network Error') || msg.includes('NetworkError')) {
				networkDown.value = true
				stopOrderPolling()
			}
		})
	}
	//删
	const delClick = (id) => {
		let ids = ref([])
		if (id) {
			ids.value = [id]
		} else {
			if (selRows.value.length) {
				for (let x in selRows.value) {
					ids.value.push(selRows.value[x].id)
				}
			} else {
				return false
			}
		}
		ElMessageBox.confirm(`是否删除选中${formName}`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: ids.value
			}).then(res => {
				context?.$toolUtil.message('删除成功', 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	//列表数据
	//分页
	const total = ref(0)
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
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	//搜索
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	//表单
	const formRef = ref(null)
	const formModelChange=()=>{
		searchClick()
	}
	const addClick = ()=>{
		formRef.value.init()
	}
	const editClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'edit')
			return
		}
		if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'edit')
		}
	}

	const infoClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'info')
		}
		else if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'info')
		}
	}
	// 表单
	// 预览文件
	const preClick = (file) =>{
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		window.open(context?.$config.url + file)
	}
	// 下载文件
	const download = (file) => {
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		let arr = file.replace(new RegExp('file/', "g"), "")
		const url = (location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/' + arr
		const a = document.createElement('a')
		a.href = url
		a.download = arr
		a.dispatchEvent(new MouseEvent('click', {
			bubbles: true,
			cancelable: true,
			view: window
		}))
	}
    import '@/assets/js/echarts-theme'
	// 统计图1
	const echarts = inject("echarts")
	const echartVisible = ref(false)
	const echartClick1 = ()=>{
		echartActive.value = '1'
		echartVisible.value = true
		nextTick(()=>{
			var totalEchart1 = echarts.init(document.getElementById("totalEchart1"),'theme');
			context.$http({
				url: `${tableName}/value/addtime/discounttotal/月`,
				method: 'get'
			}).then(res=>{
				let obj = res.data.data
				let xAxis = [];
				let yAxis = [];
				let dataList = []
				for(let i=0;i<obj.length;i++){
				    xAxis.push(obj[i].addtime);
				    const val = Number(parseFloat(obj[i].total).toFixed(2));
				    yAxis.push(val);
                    dataList.push({
				        value: val,
				        name: obj[i].addtime
				    })
				}
				var option = {};
				option = {
    title: {
        text: '月销额',
        left: 'center'
    },
    tooltip: {
        trigger: 'item',
        formatter: '{b} : {c}'
    },
    xAxis: {
        data: xAxis,
        type: 'category',
        axisLabel: {
        "interval": 0,
        "rotate": 30
        }
    },
    yAxis: {
        type: 'value',
        "minInterval": 1
    },
    series:{
        data: yAxis,
        type: 'bar'
    }
}
				// 使用刚指定的配置项和数据显示图表。
				totalEchart1.setOption(option);
				  //根据窗口的大小变动图表
				window.onresize = function() {
				    totalEchart1.resize();
				};
			})
		})
	}
	// 统计图2
    const echartActive = ref('1')
    const echartTabClick = () =>{
		if(echartActive.value==1){
			echartClick1()
		}
		else if(echartActive.value==2){
			echartClick2()
		}
		else if(echartActive.value==3){
			echartClick3()
		}
	}
	const echartClick2 = ()=>{
		echartActive.value = '2'
		echartVisible.value = true
		nextTick(()=>{
			var totalEchart2 = echarts.init(document.getElementById("totalEchart2"),'theme');
			context.$http({
				url: `${tableName}/value/addtime/discounttotal/年`,
				method: 'get'
			}).then(res=>{
				let obj = res.data.data
				let xAxis = [];
				let yAxis = [];
				let dataList = []
				for(let i=0;i<obj.length;i++){
				    xAxis.push(obj[i].addtime);
				    const val = Number(parseFloat(obj[i].total).toFixed(2));
				    yAxis.push(val);
                    dataList.push({
				        value: val,
				        name: obj[i].addtime
				    })
				}
				var option = {};
				option = {
    grid: {
    left: '10%',
    right: '10%',
    bottom: '10%',
    containLabel: true
  },
    title: {
        text: '年销额',
        left: 'center'
    },
    tooltip: {
        trigger: 'item',
        formatter: '{b} : {c}'
    },
    xAxis: {
        type: 'value',
        "minInterval": 1
    },
    yAxis: {
        data: xAxis,
        type: 'category',
        "minInterval": 1
    },
    series:{
        data: yAxis,
        type: 'bar'
    }
}
                var middle = option.xAxis
                option.xAxis = option.yAxis
                option.yAxis = middle
				// 使用刚指定的配置项和数据显示图表。
				totalEchart2.setOption(option);
				  //根据窗口的大小变动图表
				window.onresize = function() {
				    totalEchart2.resize();
				};
			})
		})
	}
	// 统计图3
	const echartClick3 = ()=>{
		echartActive.value = '3'
		echartVisible.value = true
		nextTick(()=>{
			var buynumberEchart3 = echarts.init(document.getElementById("buynumberEchart3"),'theme');
			context.$http({
				url: `${tableName}/value/goodname/buynumber?order=desc`,
				method: 'get'
			}).then(res=>{
				let obj = res.data.data
				let xAxis = [];
				let yAxis = [];
				let dataList = []
				for(let i=0;i<obj.length;i++){
				    xAxis.push(obj[i].goodname);
				    yAxis.push(parseFloat((obj[i].total)));
                    dataList.push({
				        value: parseFloat((obj[i].total)),
				        name: obj[i].goodname
				    })
				}
				var option = {};
				option = {
    title:{
        text: '品销量',
        left: 'center'
    },
    legend: {
        orient: 'vertical',
        left: 'left'
    },
    tooltip: {
        trigger: 'item',
        formatter: '{b} : {c} ({d}%)'
    },
    series: [
        {
            left:"30%",
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: dataList,
            emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
}
				// 使用刚指定的配置项和数据显示图表。
				buynumberEchart3.setOption(option);
				  //根据窗口的大小变动图表
				window.onresize = function() {
				    buynumberEchart3.resize();
				};
			})
		})
	}
	//导出选中数据
	const exportClick = () => {
		import('@/utils/Export2Excel').then(excel => {
			const tHeader = [
				"订单编号",
				"商品表名",
				"商品id",
				"菜品",
				"图片",
				"购买数量",
				"折扣价",
				"折扣总价",
				"支付类型",
				"餐位名称",
				"订单状态",
				"备注",
				"下单时间",
				"用户id",
				"商户名称",
				"商品类型",
			]
			const filterVal = [
				"order_id",
				"source_table",
				"good_id",
				"good_name",
				"picture",
				"buy_number",
				"discount_price",
				"discount_total",
				"type",
				"seat_name",
				"status",
				"remark",
			"addtime",
			"user_id",
			"good_type",
		]
			const exportRows = (selRows.value || []).map(r => ({
				order_id: r.order_id ?? r.orderid ?? r.orderId,
				source_table: r.source_table ?? r.tablename ?? r.sourceTable,
				good_id: r.good_id ?? r.goodid ?? r.goodId,
				good_name: r.good_name ?? r.goodname ?? r.goodName,
				picture: r.picture,
				buy_number: r.buy_number ?? r.buynumber ?? r.buyNumber,
				discount_price: r.discount_price ?? r.discountprice ?? r.discountPrice,
				discount_total: r.discount_total ?? r.discounttotal ?? r.discountTotal,
				type: r.type,
				seat_name: r.seat_name ?? r.seatName,
				status: r.status,
				remark: r.remark,
				addtime: r.addtime,
				user_id: r.user_id ?? r.userid ?? r.userId,
				good_type: r.good_type ?? r.goodtype ?? r.goodType,
			}))
			excel.export_json_to_excel2(tHeader, exportRows, filterVal, formName)
		})
	}
	const orderTabClick = () => {
		getList()
	}
	const orderStatus = ref('')
    if(['未支付'  ,'已支付','已发货','已完成','已退款','已取消'].includes(route.query.menuJump)){
        orderStatus.value = route.query.menuJump
    }
	const orderStatusFormatter =(row, column)=> {
      var temp = ''
      switch (row.type-0) {
        case 1:
          temp = '现金'
          break
        case 2:
          temp = '积分'
          break
        case 3:
          temp = '拼团'
          break
      }
      return temp
    }
	//完成订单
	const completeOrderClick = (row)=>{
		ElMessageBox.confirm(`确认菜品已送达餐位，完成该订单？`, '提示', {
			confirmButtonText: '确认',
			cancelButtonText: '取消',
			type: 'warning',
		}).then(()=>{
			const payload = {
				id: row.id,
				status: '已完成',
			}
			context.$http({
				url: 'orders/update',
				method: 'post',
				data: payload
			}).then(res=>{
				context?.$toolUtil.message('订单完成','success',()=>{
					searchClick()
				})
			})
		}).catch(_ => {})
	}
    const reviewClick = (row) => {
		const goodId = row.good_id ?? row.goodid ?? row.goodId
		if (!goodId) {
			context?.$toolUtil.message('缺少菜品信息','error')
			return
		}
        context?.$router.push(`/dish_review?ref_id=${goodId}&from=orders`)
	}
    //返回商品对象，如果商品存在库存,则返还库存
    const returnLimit = async (order)=>{
        const sourceTable = order.source_table ?? order.tablename ?? order.sourceTable
        const goodId = order.good_id ?? order.goodid ?? order.goodId
        const buyNumber = order.buy_number ?? order.buynumber ?? order.buyNumber
        if (!sourceTable || !goodId) return null
        let res = await context.$http.get(`${sourceTable}/info/${goodId}`)
        let data = res.data.data
        if(data.stock){ //如果商品存在库存，则加回去
            data.stock = parseInt(data.stock) + parseInt(buyNumber ?? 0)
            context.$http.post(`${sourceTable}/update`,data)
        }
        return data
    }
    // 同意退款
    const refundClick = (row) => {
        ElMessageBox.confirm(`是否同意该订单的退款申请？`, '提示', {
            confirmButtonText: '是',
            cancelButtonText: '否',
            type: 'warning',
        }).then(async () => {
            await returnLimit(row)
            const payload = {
                id: row.id,
                status: '已退款',
            }
            // 获取用户信息
            const uid = row.user_id ?? row.userid ?? row.userId
            let userRes = await context.$http.get(`user/info/${uid}`)
            let userinfo = userRes.data.data

            const currentBalance = parseFloat(userinfo.balance ?? 0)
            const refundAmount = parseFloat(row.discount_total ?? row.discounttotal ?? row.discountTotal ?? row.total ?? 0)
            userinfo.balance = Number((currentBalance + refundAmount).toFixed(2))
            // 修改订单状态
            await context.$http.post('orders/update',payload)
            // 更新用户信息
            await context.$http.post(`user/update`,userinfo)
            context.$message.success("退款成功")
            getList()
        }).catch(_ => {})
    }
	//初始化
	const init = () => {
        if(['未支付','已支付','已发货','已完成','已退款','已取消'].includes(route.query.menuJump)){
            orderStatus.value = route.query.menuJump
        }else{
            orderStatus.value = ''
        }
		getList()
	}
	init()
	onMounted(() => {
		startOrderPolling()
		document.addEventListener('visibilitychange', () => {
			if (document.hidden) {
				stopOrderPolling()
			} else {
				startOrderPolling()
				getList()
			}
		})
	})
	onBeforeUnmount(() => {
		stopOrderPolling()
		document.removeEventListener('visibilitychange', () => {})
	})
</script>
<style lang="scss" scoped>
	// 切换栏
	.demo-tabs {
		// 头部
		:deep(.el-tabs__header) {
			// 滑动区
			.el-tabs__nav-scroll {
				// list
				.el-tabs__nav {
					// item
					.el-tabs__item {
					}
					// item active
					.is-active {
					}
					// item hover
					.el-tabs__item:hover {
					}
				}
			}
		}
	}

	// 操作盒子
	.list_search_view {
		// 搜索盒子
		.search_form {
			// 子盒子
			.search_view {
				// 搜索label
				.search_label {
				}
				// 搜索item
				.search_box {
					// 输入框
					:deep(.search_inp) {
					}
				}
			}
			// 搜索按钮盒子
			.search_btn_view {
				// 搜索按钮
				.search_btn {
				}
				// 搜索按钮-悬浮
				.search_btn:hover {
				}
			}
		}
		//头部按钮盒子
		.btn_view {
			// 其他
			:deep(.el-button--default){
			}
			// 其他-悬浮
			:deep(.el-button--default:hover){
			}
			// 新增
			:deep(.el-button--success){
			}
			// 新增-悬浮
			:deep(.el-button--success:hover){
			}
			// 删除
			:deep(.el-button--danger){
			}
			// 删除-悬浮
			:deep(.el-button--danger:hover){
			}
			// 统计
			:deep(.el-button--warning){
			}
			// 统计-悬浮
			:deep(.el-button--warning:hover){
			}
		}
	}
	// 表格样式
	.el-table {
		:deep(.el-table__header-wrapper) {
			thead {
				tr {
					th {
						.cell {
						}
					}
				}
			}
		}
		:deep(.el-table__body-wrapper) {
			tbody {
				tr {
					td {
						.cell {
							// 编辑
							.el-button--primary {
							}
							// 编辑-悬浮
							.el-button--primary:hover {
							}
							// 详情
							.el-button--info {
							}
							// 详情-悬浮
							.el-button--info:hover {
							}
							// 删除
							.el-button--danger {
							}
							// 删除-悬浮
							.el-button--danger:hover {
							}
							// 跨表
							.el-button--success {
							}
							// 跨表-悬浮
							.el-button--success:hover {
							}
							// 操作
							.el-button--warning {
							}
							// 操作-悬浮
							.el-button--warning:hover {
							}
						}
					}
				}
				tr:hover {
					td {
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
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
			.el-select {
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
