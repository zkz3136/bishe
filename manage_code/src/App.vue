<template>
	<router-view />
</template>
<script setup>
	import * as echarts from "echarts";
	import {
		provide
	} from "vue";
	provide("echarts", echarts);
	provide("baseUrl", process.env.VUE_APP_BASE_API)
	const debounce = (fn, delay) => {
		let timer = null;
		return function() {
			let context = this;
			let args = arguments;
			clearTimeout(timer);
			timer = setTimeout(function() {
				fn.apply(context, args);
			}, delay);
		}
	}
	
	const _ResizeObserver = window.ResizeObserver;
	if (_ResizeObserver) {
		window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
			constructor(callback) {
				const safeCb = (entries, observer) => {
					if (!Array.isArray(entries)) return;
					const valid = entries.filter(e => {
						const t = e && e.target;
						return t && t.nodeType === 1 && document.body.contains(t);
					});
					if (!valid.length) return;
					callback(valid, observer);
				};
				super(debounce(safeCb, 16));
			}
		}
	}
</script>
<style lang="scss">
	#app {
		font-family: Avenir, Helvetica, Arial, sans-serif;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
		// text-align: center;
		color: #2c3e50;
	}
	// 按钮盒子
	.formModel_btn_box {
		// 取消按钮
		.cancel_btn {
		}
		// 取消按钮-悬浮
		.cancel_btn:hover {
		}
		// 确定按钮
		.confirm_btn {
		}
		// 确定按钮-悬浮
		.confirm_btn:hover {
		}
	}
	.center_view {
	}
	body {
		margin: 0;
	}
	* {
		box-sizing: border-box;
	}
	.el-select .el-input {
		font-size: inherit;
	}
	.el-input__inner {
		color: inherit;
	}
	nav {
		padding: 30px;

		a {
			font-weight: bold;
			color: #2c3e50;

			&.router-link-exact-active {
				color: #42b983;
			}
		}
	}
</style>
