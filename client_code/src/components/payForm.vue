<template>
	<div class="payDialog">
		<el-dialog v-model="payVisible" :title="'支付'" width="40%" destroy-on-close @close="close">
			<div class="payList">
				<div class="payView wx_pay">
					<el-radio v-model="payType" label="1"><img src="@/assets/pay/weixin.png" alt>微信支付</el-radio>
				</div>
				<div class="payView zfb_pay">
					<el-radio v-model="payType" label="2"><img src="@/assets/pay/zhifubao.png" alt>支付宝支付</el-radio>
				</div>
				<div class="payView yh_pay">
					<el-radio v-model="payType" label="3"><img src="@/assets/pay/yinhangka.png" alt>银行卡支付<el-icon :class="payType==3?'active':''"><ArrowDown /></el-icon></el-radio>
				</div>
				<el-collapse-transition>
					<div class="yinhang_view" v-show="payType==3">
						<div class="payView">
							<el-radio v-model="payType1" label="1"><img src="@/assets/pay/zhonghang.png" alt>中国银行</el-radio>
						</div>
						<div class="payView">
							<el-radio v-model="payType1" label="2"><img src="@/assets/pay/nongye.png" alt>中国农业银行</el-radio>
						</div>
						<div class="payView">
							<el-radio v-model="payType1" label="3"><img src="@/assets/pay/jianshe.png" alt>中国建设银行</el-radio>
						</div>
						<div class="payView">
							<el-radio v-model="payType1" label="4"><img src="@/assets/pay/gonghang.png" alt>中国工商银行</el-radio>
						</div>
						<div class="payView">
							<el-radio v-model="payType1" label="5"><img src="@/assets/pay/jiaotong.png" alt>交通银行</el-radio>
						</div>
					</div>
				</el-collapse-transition>
				<div class="payView ysf_pay">
					<el-radio v-model="payType" label="4"><img src="@/assets/pay/yunshanfu.png" alt>云闪付</el-radio>
				</div>
			</div>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="cancel">取消</el-button>
					<el-button type="primary" @click="paySave">
						确认支付
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
	import {
		ref,
		toRefs,
		getCurrentInstance,
		defineEmits
	} from 'vue';
	const emit = defineEmits(['payChange','cancel','close'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const payVisible = ref(false)
	const tableName = ref('')
	const form = ref({})
	const payType = ref('')
	const payType1 = ref('')
    const cancel = ()=>{
        payVisible.value=false
        emit('cancel')
    }
    const close = ()=>{
        emit('close')
    }
	const payClick = (table, row) => {
		tableName.value = table
		form.value = row
		payVisible.value = true
	}
	//声明父级调用
	defineExpose({
		payClick
	})
	const paySave = async () => {
		if (payType.value == '') {
			context?.$toolUtil.message('请选择支付方式', 'error')
			return false
		}
		if (payType.value==3) {
			if (payType1.value == '') {
				context?.$toolUtil.message('请选择支付银行', 'error')
				return false
			}
		}
		// 余额校验：需满足订金金额
		try {
			const deposit = Number(form.value?.deposit ?? 50)
			const res = await context?.$http({
				url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
				method: 'get'
			})
			const userBalance = parseFloat(res?.data?.data?.balance ?? res?.data?.data?.money ?? 0)
			if (!Number.isFinite(userBalance) || userBalance < deposit) {
				context?.$toolUtil.message(`余额不足，请先充值（需${deposit}元订金）`, 'error')
				return false
			}
		} catch (e) {
			// 获取余额失败时不中断支付流程，但提示用户稍后检查个人中心余额
		}
		// 设置支付状态
		form.value.payment_status = '已支付'
		// 模拟支付成功，关闭弹窗并通知父组件
		const dep = Number(form.value?.deposit ?? 50)
		context?.$toolUtil.message(`支付成功，已扣除${dep}元订金`, 'success', () => {
			payVisible.value = false
			emit('payChange')
		})
	}
</script>

<style lang="scss" scoped>
	.payDialog {
		:deep(.el-dialog) {
			border-radius: 30px;
			.el-dialog__header {
				padding: 0;
			}
			.el-dialog__title {
				padding: 0 46px;
				height: 60px;
				line-height: 60px;
				border-radius: 30px 30px 30px 0;
				display: inline-block;
				color: #fff;
				width: auto;
				background: linear-gradient(120deg, #2891FF 0%, #2B53F0 100%);
			}	
		}
	}
	.payList {
		padding: 40px;
		.yinhang_view {
			margin: 0 0 0 60px;
			width: calc(100% - 60px);
			:deep(.is-checked){
				.el-radio__inner {
					background: #f00;
					border-color: #f00;
				}
				.el-radio__label {
					color: #f00;
				}
			}
		}
		.payView {
			width: 100%;
			padding: 20px 0;
			display: flex;
			justify-content: flex-start;
			align-items: center;
			border-bottom: 1px solid #d8d8d8;
			font-size: 18px;
			:deep(.el-radio){
				.el-radio__label{
					display: flex;
					align-items: center;
					img{
						width: 30px;
						margin-right: 6px;
					}
					.el-icon{
						margin-left: 10px;
						transition: all 0.3s;
					}
					.active {
						transform: rotate(180deg);
					}
				}
			}
			
		}
	}
</style>
