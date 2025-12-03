<template>
	<div class="home_view">
		<div class="projectTitle">欢迎使用 {{projectName}}</div>
		<!-- 日历 -->
		<div class="home-calendar">
			<div class="header">
				<div class="btn lastYear" @click="lastYear">
					上一年
				</div>
				<div class="btn lastMonth" @click="lastMonth">
					上一月
				</div>
				<div class="title">
					<div class="year">{{year}}年</div>
					<div class="month">{{month}}月</div>
				</div>
				<div class="btn nextMonth" @click="nextMonth">
					下一月
				</div>
				<div class="btn nextYear" @click="nextYear">
					下一年
				</div>
			</div>
			<table v-if="weeks.length">
				<thead>
				<tr>
					<th v-for="(item,index) in heads" :key="index">{{item}}</th>
				</tr>
				</thead>
				<tbody>
				<tr v-for="(week,index) in weeks" :key="index">
					<td v-for="(day,key) in week.days" :key="key" :class="{other: day.isOther, festival: day.isFestival, today: day.isToday, rest: day.isRest}">
						<div class="text">
							<div class="new">{{day.day}}</div>
							<div class="old">{{day.text}}</div>
						</div>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="count_list">
			<el-collapse-transition v-if="btnAuth('orders','首页总数')">
				<el-card v-show="countTypeList.closeordersCountType" class="card_view count-item">
					<template #header>
						<div class="index_card_head">
							<div class="card_head_title">
								订单
							</div>
							<div class="card_head_right">
								<el-icon @click="countTypeClick('hiddenordersCountType')" class="showIcons"
									:class="countTypeList.hiddenordersCountType?'showIcons1':''">
									<ArrowUpBold />
								</el-icon>
								<el-icon @click="countTypeClick('closeordersCountType')" class="closeIcons">
									<CloseBold />
								</el-icon>
							</div>
						</div>
					</template>
					<el-collapse-transition>
						<div class="count_item" v-show="countTypeList.hiddenordersCountType">
							<div class="count_title">订单总数</div>
							<div class="count_num">{{ordersCount}}</div>
						</div>
					</el-collapse-transition>
				</el-card>
			</el-collapse-transition>
			<el-collapse-transition v-if="btnAuth('cheweiyuyue','首页总数')">
				<el-card v-show="countTypeList.closecheweiyuyueCountType" class="card_view count-item">
					<template #header>
						<div class="index_card_head">
							<div class="card_head_title">
								车位预约
							</div>
							<div class="card_head_right">
								<el-icon @click="countTypeClick('hiddencheweiyuyueCountType')" class="showIcons"
									:class="countTypeList.hiddencheweiyuyueCountType?'showIcons1':''">
									<ArrowUpBold />
								</el-icon>
								<el-icon @click="countTypeClick('closecheweiyuyueCountType')" class="closeIcons">
									<CloseBold />
								</el-icon>
							</div>
						</div>
					</template>
					<el-collapse-transition>
						<div class="count_item" v-show="countTypeList.hiddencheweiyuyueCountType">
							<div class="count_title">车位预约总数</div>
							<div class="count_num">{{cheweiyuyueCount}}</div>
						</div>
					</el-collapse-transition>
				</el-card>
			</el-collapse-transition>
		</div>
		<div class="card_list">
			<el-collapse-transition v-if="btnAuth('meishixinxi','首页统计')">
				<el-card v-show="cardTypeList.closemeishixinxiChartType1" class="card_view chart-item">
					<template #header>
						<div class="index_card_head">
							<div class="card_head_title">
								美食信息
							</div>
							<div class="card_head_right">
								<el-icon @click="cardTypeClick('hiddenmeishixinxiChartType1')" class="showIcons"
										 :class="cardTypeList.hiddenmeishixinxiChartType1?'showIcons1':''">
									<ArrowUpBold />
								</el-icon>
								<el-icon @click="cardTypeClick('closemeishixinxiChartType1')" class="closeIcons">
									<CloseBold />
								</el-icon>
							</div>
						</div>
					</template>
					<el-collapse-transition>
						<div class="card_item" v-show="cardTypeList.hiddenmeishixinxiChartType1">
							<div id="meishixinxicaipinleixingEchart1" class="Echart" style="width: 100%;height: 400px;"></div>
						</div>
					</el-collapse-transition>
				</el-card>
			</el-collapse-transition>
			<el-collapse-transition v-if="btnAuth('orders','首页统计')">
				<el-card v-show="cardTypeList.closeordersChartType1" class="card_view chart-item">
					<template #header>
						<div class="index_card_head">
							<div class="card_head_title">
								订单
							</div>
							<div class="card_head_right">
								<el-icon @click="cardTypeClick('hiddenordersChartType1')" class="showIcons"
										 :class="cardTypeList.hiddenordersChartType1?'showIcons1':''">
									<ArrowUpBold />
								</el-icon>
								<el-icon @click="cardTypeClick('closeordersChartType1')" class="closeIcons">
									<CloseBold />
								</el-icon>
							</div>
						</div>
					</template>
					<el-collapse-transition>
						<div class="card_item" v-show="cardTypeList.hiddenordersChartType1">
							<div id="orderstotalEchart1" class="Echart" style="width: 100%;height: 400px;"></div>
						</div>
					</el-collapse-transition>
				</el-card>
			</el-collapse-transition>
			<el-collapse-transition v-if="btnAuth('orders','首页统计')">
				<el-card v-show="cardTypeList.closeordersChartType2" class="card_view chart-item">
					<template #header>
						<div class="index_card_head">
							<div class="card_head_title">
								订单
							</div>
							<div class="card_head_right">
								<el-icon @click="cardTypeClick('hiddenordersChartType2')" class="showIcons"
										 :class="cardTypeList.hiddenordersChartType2?'showIcons2':''">
									<ArrowUpBold />
								</el-icon>
								<el-icon @click="cardTypeClick('closeordersChartType2')" class="closeIcons">
									<CloseBold />
								</el-icon>
							</div>
						</div>
					</template>
					<el-collapse-transition>
						<div class="card_item" v-show="cardTypeList.hiddenordersChartType2">
							<div id="orderstotalEchart2" class="Echart" style="width: 100%;height: 400px;"></div>
						</div>
					</el-collapse-transition>
				</el-card>
			</el-collapse-transition>
			<el-collapse-transition v-if="btnAuth('orders','首页统计')">
				<el-card v-show="cardTypeList.closeordersChartType3" class="card_view chart-item">
					<template #header>
						<div class="index_card_head">
							<div class="card_head_title">
								订单
							</div>
							<div class="card_head_right">
								<el-icon @click="cardTypeClick('hiddenordersChartType3')" class="showIcons"
										 :class="cardTypeList.hiddenordersChartType3?'showIcons3':''">
									<ArrowUpBold />
								</el-icon>
								<el-icon @click="cardTypeClick('closeordersChartType3')" class="closeIcons">
									<CloseBold />
								</el-icon>
							</div>
						</div>
					</template>
					<el-collapse-transition>
						<div class="card_item" v-show="cardTypeList.hiddenordersChartType3">
							<div id="ordersbuynumberEchart3" class="Echart" style="width: 100%;height: 400px;"></div>
						</div>
					</el-collapse-transition>
				</el-card>
			</el-collapse-transition>
		</div>
	</div>
</template>

<script setup>
	import {
		inject,
		nextTick,
		ref,
		getCurrentInstance
	} from 'vue';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const projectName = context.$project.projectName
	const countTypeList = ref({
	})
	const getCountList=()=>{
		countTypeList.value.closeordersCountType = true
		countTypeList.value.hiddenordersCountType = true
		if(btnAuth('orders','首页总数')){
			getordersCount()
		}
		countTypeList.value.closecheweiyuyueCountType = true
		countTypeList.value.hiddencheweiyuyueCountType = true
		if(btnAuth('cheweiyuyue','首页总数')){
			getcheweiyuyueCount()
		}
	}
	const ordersCount = ref(0)
	const getordersCount = () => {
		context?.$http({
			url:'orders/count',
			method: 'get'
		}).then(res=>{
			ordersCount.value = res.data.data
		})
	}
	const cheweiyuyueCount = ref(0)
	const getcheweiyuyueCount = () => {
		context?.$http({
			url:'cheweiyuyue/count',
			method: 'get'
		}).then(res=>{
			cheweiyuyueCount.value = res.data.data
		})
	}
	const countTypeClick = (e) => {
		countTypeList.value[e] = !countTypeList.value[e]
	}
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	let echarts = inject("echarts")
	const cardTypeClick = (e) =>{
		cardTypeList.value[e] = !cardTypeList.value[e]
		setTimeout(()=>{
			getCardList()
		},1000)
	}
	const cardTypeList = ref({
		closemeishixinxiChartType1: true,
		hiddenmeishixinxiChartType1: true,
		closeordersChartType1: true,
		hiddenordersChartType1: true,
		closeordersChartType2: true,
		hiddenordersChartType2: true,
		closeordersChartType3: true,
		hiddenordersChartType3: true,
	})
	const getCardList = () => {
		if(btnAuth('meishixinxi','首页统计')){
			getmeishixinxiChart1()
		}
		if(btnAuth('orders','首页统计')){
			getordersChart1()
		}
		if(btnAuth('orders','首页统计')){
			getordersChart2()
		}
		if(btnAuth('orders','首页统计')){
			getordersChart3()
		}
	}
	import '@/assets/js/echarts-theme'
	const getmeishixinxiChart1 = () => {
		nextTick(()=>{
			var caipinleixingEchart1 = echarts.init(document.getElementById("meishixinxicaipinleixingEchart1"),'theme');
			context?.$http({
				url: "meishixinxi/group/caipinleixing",
				method: "get",
			}).then(obj=>{
				let res = obj.data.data
				let xAxis = [];
				let yAxis = [];
				let dataList = []
				for(let i=0;i<res.length;i++){
				    xAxis.push(res[i].caipinleixing);
				    yAxis.push(parseFloat((res[i].total)));
					dataList.push({
				        value: parseFloat((res[i].total)),
				        name: res[i].caipinleixing
				    })
				}
				var option = {};
				option = {
    title:{
        text: '菜品类型',
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
				caipinleixingEchart1.clear()
				// 使用刚指定的配置项和数据显示图表。
				caipinleixingEchart1.setOption(option);
				//根据窗口的大小变动图表
				caipinleixingEchart1.resize();
			})
		})
	}
	const getordersChart1 = () => {
		nextTick(()=>{
			var totalEchart1 = echarts.init(document.getElementById("orderstotalEchart1"),'theme');
			context?.$http({
				url: `orders/value/addtime/total/月`,
				method: "get",
			}).then(obj=>{
				let res = obj.data.data
				let xAxis = [];
				let yAxis = [];
				let dataList = []
				for(let i=0;i<res.length;i++){
				    xAxis.push(res[i].addtime);
				    yAxis.push(parseFloat((res[i].total)));
					dataList.push({
				        value: parseFloat((res[i].total)),
				        name: res[i].addtime
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

				totalEchart1.clear()
				// 使用刚指定的配置项和数据显示图表。
				totalEchart1.setOption(option);
				//根据窗口的大小变动图表
				totalEchart1.resize();
			})
		})
	}
	const getordersChart2 = () => {
		nextTick(()=>{
			var totalEchart2 = echarts.init(document.getElementById("orderstotalEchart2"),'theme');
			context?.$http({
				url: `orders/value/addtime/total/年`,
				method: "get",
			}).then(obj=>{
				let res = obj.data.data
				let xAxis = [];
				let yAxis = [];
				let dataList = []
				for(let i=0;i<res.length;i++){
				    xAxis.push(res[i].addtime);
				    yAxis.push(parseFloat((res[i].total)));
					dataList.push({
				        value: parseFloat((res[i].total)),
				        name: res[i].addtime
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

				totalEchart2.clear()
				// 使用刚指定的配置项和数据显示图表。
				totalEchart2.setOption(option);
				//根据窗口的大小变动图表
				totalEchart2.resize();
			})
		})
	}
	const getordersChart3 = () => {
		nextTick(()=>{
			var buynumberEchart3 = echarts.init(document.getElementById("ordersbuynumberEchart3"),'theme');
			context?.$http({
				url: `orders/value/goodname/buynumber?order=desc`,
				method: "get",
			}).then(obj=>{
				let res = obj.data.data
				let xAxis = [];
				let yAxis = [];
				let dataList = []
				for(let i=0;i<res.length;i++){
				    xAxis.push(res[i].goodname);
				    yAxis.push(parseFloat((res[i].total)));
					dataList.push({
				        value: parseFloat((res[i].total)),
				        name: res[i].goodname
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
      data: dataList,
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 40,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      }
      

        }
    ]
}
				buynumberEchart3.clear()
				// 使用刚指定的配置项和数据显示图表。
				buynumberEchart3.setOption(option);
				//根据窗口的大小变动图表
				buynumberEchart3.resize();
			})
		})
	}

	// 日历
	const year = ref(0)
	const month = ref(0)
	const heads = ref(['一', '二', '三', '四', '五', '六', '日'])
	const weeks = ref([])
	const now = ref(Solar.fromDate(new Date()))
	const nextYear = ()=>{
		year.value = year.value + 1
		weeks.value = render(year.value, month.value)
	}
	const lastYear = ()=>{
		year.value = year.value - 1
		weeks.value = render(year.value, month.value)
	}
	const nextMonth = ()=>{
		month.value = month.value + 1
		if (month.value > 12) {
			month.value = 1
			year.value = year.value + 1
		}
		weeks.value = render(year.value, month.value)
	}
	const lastMonth = ()=>{
		month.value = month.value - 1
		if (month.value < 1) {
			month.value = 12
			year.value = year.value - 1
		}
		weeks.value = render(year.value, month.value)
	}
	const render = (year, month, weekStart = 1)=>{
		let months = SolarMonth.fromYm(year, month)
		let weeks = []

		months.getWeeks(weekStart).forEach(w => {
			let week = {
				index: 0,
				days: []
			}
			week.index = w.getIndexInYear()
			let days = []
			w.getDays().forEach(d => {
				days.push(buildDay(d, month))
			})
			week.days = days
			weeks.push(week)
		})
		return weeks
	}
	const buildDay = (d, month)=>{
		let lunar = d.getLunar()
		let day = {
			day: 0,
			text: '',
			isFestival: false,
			isToday: false,
			isOther: false,
			isHoliday: false,
			isRest: false
		}
		day.day = d.getDay()
		let text = lunar.getDayInChinese()
		if (1 === d.getDay()) {
			text = lunar.getMonthInChinese() + '月'
		}
		let festivals = d.getFestivals()
		if (festivals.length > 0) {
			text = festivals[0]
			day.isFestival = true
		}
		festivals = lunar.getFestivals()
		if (festivals.length > 0) {
			text = festivals[0]
			day.isFestival = true
		}
		let jq = lunar.getJieQi()
		if (jq) {
			text = jq
			day.isFestival = true
		}
		day.text = text
		if (d.toYmd() === now.value.toYmd()) {
			day.isToday = true
		}
		if (d.getMonth() !== month) {
			day.isOther = true
		}
		let h = HolidayUtil.getHoliday(d.getYear(), d.getMonth(), d.getDay())
		if (h) {
			day.isHoliday = true
			day.isRest = !h.isWork()
		}
		return day
	}

	year.value = now.value.getYear()
	month.value = now.value.getMonth()
	weeks.value = render(year.value, month.value);
	const init=()=>{
		getCountList()
		getCardList()
	}
	init()
</script>
<style lang="scss" scoped>
	// 首页盒子
	.home_view {
        background-image: url("http://clfile.zggen.cn/20240917/60616291f70642f1aba356139f3fc2db.webp")!important;
	}
	.home_view .projectTitle{
	}

	.showIcons {
		transition: transform 0.3s;
		margin-right: 10px;
	}

	.showIcons1 {
		transform: rotate(-180deg);
	}
	
	// 总数盒子
	.count_list{
		// 总数card
		.card_view {
			// card头部
			.el-card__header {
				// 头部盒子
				.index_card_head {
					// 标题
					.card_head_title {
					}
					// 按钮盒子
					.card_head_right {
						// 按钮
						.el-icon {
						}
					}
				}
			}
			// body
			.el-card__body {
				// body盒子
				.count_item{
					// 总数标题
					.count_title{
					}
					// 总数数字
					.count_num{
					}
				}
			}
		}
	}

	// 统计图盒子
	.card_list {
		// 统计图card
		.card_view {
			// 头部
			.el-card__header {
				// 头部盒子
				.index_card_head {
					// 标题
					.card_head_title {
					}
					// 按钮盒子
					.card_head_right {
						// 按钮
						.el-icon{
						}
					}
				}
			}
			// body
			.el-card__body {
				// body盒子
				.card_item{
				}
			}
		}
	}
</style>
<style>
/*总盒子*/
.home_view{
    width: 100%;
    margin: 0 auto;
    padding: 20px 40px 20px 40px;
    height: auto;
    min-height: 100vh;
    background: url(http://clfile.zggen.cn/20240917/226db5213b844e3785e554748e4d09fe.png) no-repeat center top / cover !important;
    border-radius: 0px;
    display:flex;
    flex-wrap:wrap;
    align-items:flex-start;
}
.home_view .projectTitle{
    width: 100%;
    font-size: 28px;
    font-weight: bold;
    padding: 40px 0px 20px;
    height: auto;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0px 0px 20px;
    color: #d3623d;
}

/*总数*/
/*总盒子*/
.home_view .count_list {
    width: calc(100% - 20px);
    display:flex;
    flex-wrap:wrap;
    justify-content:center;
    padding:20px 0 0px;
    align-items:flex-start;
    background:#fff;
    margin:0 auto 20px;
}
/*卡片盒子*/
.home_view .count_list .card_view {
    width:calc(25% - 40px);
    height:auto;
    margin:0px 20px 20px;
    box-sizing:border-box;
    border:2px solid #d3623d;
    border-radius:0px;
    background:url(http://clfile.zggen.cn/20240917/2626fb5752f24b44bb1d33bdc9100b99.png) no-repeat right top,#fff;
    padding:5px 20px;
    flex: 1;
    box-shadow:none;
}

/*head 总盒子*/
.home_view .count_list .card_view .el-card__header{
     width: 100%;
    border: 0px solid rgb(238, 238, 238);
    display: none;
}
/*item*/
.home_view .count_list .card_view .el-card__header .index_card_head{
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 10px;
}
/*标题*/
.home_view .count_list .card_view .el-card__header .index_card_head .card_head_title{
    font-size: 14px;
    color: rgb(51, 51, 51);
}
/*按钮盒子*/
.home_view .count_list .card_view .el-card__header .index_card_head .card_head_right{
    display: flex;
    align-items: center;
}
/*按钮*/
.home_view .count_list .card_view .el-card__header .index_card_head .card_head_right .showIcons{
    color: rgb(102, 102, 102);
    font-size: 20px;
    cursor: pointer;
}
/*body 总盒子*/
.home_view .count_list .card_view .el-card__body{
    padding: 0px;
    background: none;
}
/*item*/
.home_view .count_list .card_view .el-card__body .count_item{
    padding: 10px;
    text-align: left;
    display: flex;
    align-items: flex-start;
    justify-content: flex-start;
    flex-direction: column;
}
/*标题*/
.home_view .count_list .card_view .el-card__body .count_item .count_title{
    font-size: 18px;
    color: rgb(33, 33, 33);
    line-height: 1.5;
    display: inline-block;
    margin: 0px 0px 0px 0px;
    font-weight: 500;
    order:2;
}
/*数字*/
.home_view .count_list .card_view .el-card__body .count_item .count_num{
    font-size: 32px;
    color: #000;
    line-height: 1.5;
    display: inline-block;
    font-weight: 500;
}

/*图表*/
/*总盒子*/
.home_view .card_list{
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    padding: 0px 0px 20px;
    align-items: flex-start;
    order:11;
}
/*卡片 总盒子*/
.home_view .card_list .card_view{
    width: calc(33.33% - 20px);
    height: auto;
    margin: 0px 10px 30px;
    box-sizing: border-box;
    border-radius: 0px;
    padding: 20px 10px 10px;
    box-shadow:none;
    border:0px solid #ddd;
    background: #fff;
}

/*head 总盒子*/
.home_view .card_list .card_view .el-card__header{
    width: 100%;
    border: 0px solid rgb(238, 238, 238);
    background: rgb(255, 255, 255);
    display: none;
}
/*item*/
.home_view .card_list .card_view .el-card__header .index_card_head{
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 10px;
}
/*标题*/
.home_view .card_list .card_view .el-card__header .index_card_head .card_head_title{
}
/*按钮盒子*/
.home_view .card_list .card_view .el-card__header .index_card_head .card_head_right{
    display: none;
}
/*按钮*/
.home_view .card_list .card_view .el-card__header .index_card_head .card_head_right .showIcons{
    color: rgb(238, 238, 238);
    font-size: 20px;
    cursor: pointer;
}

/*body 总盒子*/
.home_view .card_list .card_view .el-card__body{
    padding: 0px;
    background: rgb(255, 255, 255);
}
.home_view .card_list .card_view .el-card__body .card_item{
    padding: 0px;
    text-align: center;
}
.home-calendar{
  border: 0px solid rgb(218, 218, 218);
  box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 0px;
  margin: 0px auto 30px;
  border-radius: 0px;
  color: rgb(102, 102, 102);
  background: #fff;
  width: calc(100% - 20px);
  font-size: inherit;
  height: auto;
  padding:0 0 15px;
  order:2;
  border-width: 0px;
  border-style: solid;
  border-color: #ffc77f50;
}
.home-calendar .header{
  padding: 17px 40px;
  flex-wrap: wrap;
  background: #d3623d;
  display: flex;
  width: 100%;
  justify-content: space-between;
  height: auto;
}
.home-calendar .header .btn{
  cursor: pointer;
  border: 0px solid rgb(153, 153, 153);
  padding: 0px 0px 0px 12px;
  align-items: center;
  color: #fff;
  border-radius: 4px;
  display: flex;
  width: auto;
  font-size: 16px;
  justify-content: center;
}
.home-calendar .header .title{
  padding: 0px 10px;
  font-size: 24px;
  align-items: center;
  justify-content: center;
  display: flex;
  color: #fff;
}
.home-calendar table{
  width: 100%;
  padding: 0px 0px 0px;
  height: auto;

}
.home-calendar tbody,thead{
  width: 100%;
}
.home-calendar tr{
  width: 100%;
  align-items: center;
  justify-content: center;
  display: flex;
}
.home-calendar th{
  align-items: center;
  flex: 1;
  display: flex;
  line-height: 50px;
  justify-content: center;
}
.home-calendar td{
  cursor: pointer;
  padding: 6px 12px 6px;
  flex: 1;
  display: flex;
  justify-content: center;
  text-align: center;
}
.home-calendar td.today .text{
  width: 80%;
  height: 80%;
  padding:5px 0;
  background: #d3623d !important;
  color:#fff;
  border-radius:0px;
}
.home-calendar td.festival .text{
  width: 80%;
  height: 80%;
  padding:5px 0;
  background: #d3623d20;
  border-radius:0px;
}

</style>
