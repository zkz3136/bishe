<template>
	<div>
		<div class="forget_view">
			<el-form :model="forgetForm" class="forget_box">
				<div class="forget_title">餐厅智能服务与预订系统{{pageType==1?'找回密码':pageType==2?'输入密保':'重置密码'}}</div>
				<div class="tab_line">
					<div class="line"></div>
					<div class="num_line">
						<div class="line_number" :class="pageType==1?'line_number1':'',pageType>1?'line_number2':''"><div class="number" v-if="pageType<2">1</div><el-icon v-else><Check /></el-icon></div>
						<div class="line_number" :class="pageType==2?'line_number1':'',pageType>2?'line_number2':''"><div class="number" v-if="pageType<3">2</div><el-icon v-else><Check /></el-icon></div>
						<div class="line_number" :class="pageType==3?'line_number1':''"><div class="number">3</div></div>
					</div>
				</div>
			</el-form>
		</div>
	</div>
</template>
<script setup>
	import {
		ref,
		getCurrentInstance,
		nextTick,
        onMounted,
	} from 'vue';
    import { useRoute } from 'vue-router'
    const route = useRoute()
	const context = getCurrentInstance()?.appContext.config.globalProperties;
    onMounted(()=>{
    })
	const pageType = ref(1)
	const forgetForm = ref({})
	const userForm = ref({})
	//返回登录
	const close = () => {
		context?.$router.push({
			path: "/login"
		});
	}
</script>

<style lang="scss" scoped>
	.forget_view {
        background-image: url("http://clfile.zggen.cn/20240917/631e2a88e74b404a879de41ace9f0d5d.webp")!important;
		// 表单盒子
		.forget_box {
			// 标题
			.forget_title {
			}
			// tab
			// 盒子
			.tab_line{
				padding: 30px 0;
				flex-direction: column;
				display: flex;
				width: 100%;
				align-items: center;
				// 中间线
				.line{
				background: #ccc;
				width: 80%;
				height: 2px;
				}
				// item盒子
				.num_line{
					margin: -20px 0 0;
					display: flex;
					width: 80%;
					justify-content: space-between;
					align-items: center;
					height: 40px;
					// 默认样式
					.line_number{
						border: 4px solid #ddd;
						border-radius: 50%;
						color: #aaa;
						background: #fff;
						display: flex;
						width: 40px;
						font-size: 18px;
						justify-content: center;
						align-items: center;
						height: 40px;
					}
					// 选中样式
					.line_number1{
						color: #d3623d;
						font-size: 22px;
						border-color: #d3623d;
					}
					// 完成样式
					.line_number2{
						color: rgba(0, 170, 0, 1.0);
						border-color: rgba(0, 170, 0, 1.0);
					}
				}
			}
			// item
			.list_item {
				// label
				.item_label {
				}
				// 输入框
				:deep(.list_inp) {
				}
				//下拉框样式
				:deep(.list_sel) {
					//去掉默认样式
					.select-trigger{
						height: 100%;
						.el-input{
							height: 100%;
						}
					}
				}
			}
			// 按钮盒子
			.list_btn {
				// 获取密保
				:deep(.el-button--success) {
				}
				// 获取密保悬浮
				:deep(.el-button--success:hover) {
				}
				// 确认密保
				:deep(.el-button--primary) {
				}
				// 确认密保悬浮
				:deep(.el-button--primary:hover) {
				}
				// 重置密码
				:deep(.el-button--warning) {
				}
				// 重置密码悬浮
				:deep(.el-button--warning:hover) {
				}
				.r-login {
				}
			}
		}
	}

	
</style>
<style>
.forget_view {
    min-height: 100vh;
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-image: url(http://clfile.zggen.cn/20240916/cc474f586aab49a6a89f35258fafd38a.jpg);
    background-position: center center;
    background-size: 100% 100% !important;
    background-repeat: no-repeat;
    background-attachment: initial;
    background-origin: initial;
    background-clip: initial;
    background-color: initial;
}


.forget_box{
    width:35%;
    margin:0 0 0 40%; 
    padding:30px 80px 30px 80px; 
    background:rgba(255,255,255,.9);
    font-size:16px;
    border-radius:0px;
    transform:scale(1); 
    -webkit-transform:scale(1);
    -ms-transform:scale(1); 
    z-index:5;
    box-shadow: inset -1px 3px 8px 5px #f6f6f6, 2px 5px 16px 0px #888, 5px 5px 15px 5px rgba(0,0,0,0);
}
.forget_box:before { content: ""; width: 100%; height: 0px; border-radius: 10px;  position: absolute;  top: -10px; background: rgba(255, 255, 255, .6);  left: 0; transform: scale(.95); -webkit-transform: scale(.95);  -ms-transform: scale(.95);  z-index: -1;  }


.forget_title{
    width:calc(100% + 0px);
    float:left;
    line-height:46px;
    font-size:22px; 
    font-weight:600;
    letter-spacing:0.5px;
    background:none;
    color:#000000;
    margin-bottom:20px;
    margin-left:0px;
    text-align:left;
}


.forget_view .outTitle_view{
    display: flex;
    align-items: center;
    padding: 0px 0px 40px;
}

.forget_view .outTitle_view .outTilte{
    font-size: 32px;
    font-weight:600;
    letter-spacing:4px;
    color: rgb(255, 255, 255);
    text-shadow: 3px 5px 2px #474747;
}
.forget_title:before { content: "";  width: 0px; height: 100%;  position: absolute;  top: 0; left: -50px; background: #8b5c7e; }


.list_item{  display: flex; align-items: center;  width: 100%; justify-content: flex-start; margin: 10px 0px; border: 1px solid #ddd; border-radius:4px; padding:0 10px;  }
.list_item .item_label { margin-right:10px; white-space: nowrap;  font-size: 16px; }
.list_item .item_label i {  font-size:24px; color:#999;  }

.list_item .list_inp{ width:100%;  }
.list_item .list_inp .el-input__inner{ border:none;  height:40px; line-height:40px; font-size:15px; background:none; }
.list_item .list_inp .el-input__inner::placeholder { color: #999; }

.list_item .list_sel{  width:100%; }
.list_item .list_sel .el-input__inner{ width:100%; border:none;  height:40px; line-height:40px; font-size:15px;  color: #999; background:none;  }

.list_item .el-range-editor.is-active,.list_item .el-range-editor.is-active:hover,.list_item .el-select .el-input.is-focus .el-input__inner {
    border-color: #ffc77f;
}

.list_type{ margin-bottom:20px;  }


.list_btn{ margin-top:20px;  text-align:center;  }

.list_btn .get_btn{  width: 100%; height: 46px; line-height: 46px; background:#d3623d; border: 0px solid #ccc; font-weight: 600; font-size: 20px; color: #fff; letter-spacing: 4px; margin-bottom:20px; padding:0; border-radius:4px;  } 
.list_btn .get_btn:hover {  } 

.list_btn .valid_btn{ width: 100%; height: 46px; line-height: 46px; background:#d3623d; border: 0px solid #ccc; font-weight: 600; font-size: 20px; color: #fff; letter-spacing: 4px; margin-bottom:20px; padding:0; border-radius:4px;  } 
.list_btn .valid_btn:hover {  } 

.list_btn .update_btn{ width: 100%; height: 46px; line-height: 46px; background:#d3623d; border: 0px solid #ccc; font-weight: 600; font-size: 20px; color: #fff; letter-spacing: 4px; margin-bottom:20px; padding:0; border-radius:4px;  } 
.list_btn .update_btn:hover {  } 

.list_btn .r-login{  font-size: 16px; color: #666; text-align:right;  } 
.list_btn .r-login:hover{ cursor:pointer; text-decoration:underline;  }

</style>